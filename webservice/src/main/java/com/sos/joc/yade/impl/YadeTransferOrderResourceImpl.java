package com.sos.joc.yade.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.yade.TransferFileUtils;
import com.sos.joc.db.yade.JocDBLayerYade;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.order.OrderV200;
import com.sos.joc.model.yade.ModifyTransfer;
import com.sos.joc.yade.resource.IYadeTransferOrderResource;

@Path("yade")
public class YadeTransferOrderResourceImpl extends JOCResourceImpl implements IYadeTransferOrderResource {

	private static final String API_CALL = "./yade/transfer/order";

	@Override
	public JOCDefaultResponse postYadeTransferOrder(String accessToken, ModifyTransfer filterBody) throws Exception {
		SOSHibernateSession connection = null;
		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, filterBody, accessToken,
					filterBody.getJobschedulerId(), getPermissonsJocCockpit(filterBody.getJobschedulerId(), accessToken)
							.getYADE().getExecute().isTransferStart());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			connection = Globals.createSosHibernateStatelessConnection(API_CALL);
			JocDBLayerYade yadeDbLayer = new JocDBLayerYade(connection);

			OrderV200 entity = new OrderV200();
			entity.setOrder(TransferFileUtils.getOrderForResume(yadeDbLayer, filterBody, this));
			entity.setDeliveryDate(Date.from(Instant.now()));

			return JOCDefaultResponse.responseStatus200(entity);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		} finally {
			Globals.disconnect(connection);
		}
	}

}
