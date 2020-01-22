package com.sos.joc.yade.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.dom4j.Element;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.audit.ModifyOrderAudit;
import com.sos.joc.classes.yade.TransferFileUtils;
import com.sos.joc.db.yade.JocDBLayerYade;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.OrderV;
import com.sos.joc.model.yade.ModifyTransfer;
import com.sos.joc.model.yade.ModifyTransfers;
import com.sos.joc.yade.resource.IYadeTransfersRestartResource;
import com.sos.schema.JsonValidator;

@Path("yade")
public class YadeTransfersRestartResourceImpl extends JOCResourceImpl implements IYadeTransfersRestartResource {

	private static final String API_CALL = "./yade/transfers/restart";
	private List<Err419> listOfErrors = new ArrayList<Err419>();

	@Override
	public JOCDefaultResponse postYadeTransfersRestart(String accessToken, byte[] filterBytes) {
		SOSHibernateSession connection = null;
		try {
		    JsonValidator.validateFailFast(filterBytes, ModifyTransfers.class);
            ModifyTransfers filterBody = Globals.objectMapper.readValue(filterBytes, ModifyTransfers.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, filterBody, accessToken,
					filterBody.getJobschedulerId(), getPermissonsJocCockpit(filterBody.getJobschedulerId(), accessToken)
							.getYADE().getExecute().isTransferStart());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			checkRequiredComment(filterBody.getAuditLog());
			if (filterBody.getTransfers() == null || filterBody.getTransfers().size() == 0) {
				throw new JocMissingRequiredParameterException("undefined 'transferIds'");
			}

			connection = Globals.createSosHibernateStatelessConnection(API_CALL);
			JocDBLayerYade yadeDbLayer = new JocDBLayerYade(connection);

			Date surveyDate = new Date();
			for (ModifyTransfer transfer : filterBody.getTransfers()) {
				surveyDate = executeResumeOrderCommand(transfer, filterBody, yadeDbLayer);
			}
			if (listOfErrors.size() > 0) {
				return JOCDefaultResponse.responseStatus419(listOfErrors);
			}
			return JOCDefaultResponse.responseStatusJSOk(surveyDate);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		} finally {
			Globals.disconnect(connection);
		}
	}

	private Date executeResumeOrderCommand(ModifyTransfer transfer, ModifyTransfers filterBody,
			JocDBLayerYade yadeDbLayer) {
		ModifyOrder modifyOrder = new ModifyOrder();
		modifyOrder.setJobChain("");
		modifyOrder.setOrderId(null);
		try {
			OrderV order = TransferFileUtils.getOrderForResume(yadeDbLayer, transfer, this);

			modifyOrder.setJobChain(order.getJobChain());
			modifyOrder.setOrderId(order.getOrderId());
			modifyOrder.setParams(order.getParams());
			modifyOrder.setResume(true);

			ModifyOrderAudit orderAudit = new ModifyOrderAudit(modifyOrder, filterBody);
			logAuditMessage(orderAudit);

			JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
			XMLBuilder xml = new XMLBuilder("modify_order");
			xml.addAttribute("order", order.getOrderId()).addAttribute("job_chain", order.getJobChain())
					.addAttribute("suspended", "no");
			if (order.getParams() != null && !order.getParams().isEmpty()) {
				xml.add(getParams(order.getParams()));
			}
			jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
			storeAuditLogEntry(orderAudit);

			return jocXmlCommand.getSurveyDate();
		} catch (JocException e) {
			listOfErrors.add(new BulkError().get(e, getJocError(), modifyOrder));
		} catch (Exception e) {
			listOfErrors.add(new BulkError().get(e, getJocError(), modifyOrder));
		}
		return null;
	}

	private Element getParams(List<NameValuePair> params) {
		Element paramsElem = XMLBuilder.create("params");
		for (NameValuePair param : params) {
			paramsElem.addElement("param").addAttribute("name", param.getName()).addAttribute("value",
					param.getValue());
		}
		return paramsElem;
	}

}
