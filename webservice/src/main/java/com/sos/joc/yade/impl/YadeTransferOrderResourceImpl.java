package com.sos.joc.yade.impl;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
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
            SOSPermissionJocCockpit sosPermission = getPermissonsJocCockpit(accessToken);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, filterBody, accessToken, filterBody.getJobschedulerId(),  
                    sosPermission.getYADE().getExecute().isTransferStart());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            // TODO: Filter
            
            OrderV200 order = new OrderV200();
            // TODO: fill the order
            return JOCDefaultResponse.responseStatus200(order);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}
