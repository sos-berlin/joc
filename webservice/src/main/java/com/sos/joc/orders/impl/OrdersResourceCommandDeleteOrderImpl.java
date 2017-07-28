package com.sos.joc.orders.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.classes.audit.ModifyOrderAudit;
import com.sos.joc.exceptions.BulkError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;
import com.sos.joc.orders.resource.IOrdersResourceCommandDeleteOrder;

@Path("orders")
public class OrdersResourceCommandDeleteOrderImpl extends JOCResourceImpl implements IOrdersResourceCommandDeleteOrder {

    private static final String API_CALL = "./orders/delete";
    private List<Err419> listOfErrors = new ArrayList<Err419>();

    @Override
    public JOCDefaultResponse postOrdersDelete(String xAccessToken, String accessToken, ModifyOrders modifyOrders) {
        return postOrdersDelete(getAccessToken(xAccessToken, accessToken), modifyOrders);
    }

    public JOCDefaultResponse postOrdersDelete(String accessToken, ModifyOrders modifyOrders) {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, modifyOrders, accessToken, modifyOrders.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getOrder().getDelete().isTemporary());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredComment(modifyOrders.getAuditLog());
            if (modifyOrders.getOrders().size() == 0) {
                throw new JocMissingRequiredParameterException("undefined 'orders'");
            }
            Date surveyDate = new Date();
            for (ModifyOrder order : modifyOrders.getOrders()) {
                surveyDate = executeDeleteOrderCommand(order, modifyOrders);
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
        }
    }

    private Date executeDeleteOrderCommand(ModifyOrder order, ModifyOrders modifyOrders) {
        try {
            if (order.getParams() != null && order.getParams().isEmpty()) {
                order.setParams(null);
            }
            ModifyOrderAudit orderAudit = new ModifyOrderAudit(order, modifyOrders);
            logAuditMessage(orderAudit);

            checkRequiredParameter("jobChain", order.getJobChain());
            checkRequiredParameter("orderId", order.getOrderId());
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
            XMLBuilder xml = new XMLBuilder("remove_order");
            xml.addAttribute("order", order.getOrderId()).addAttribute("job_chain", normalizePath(order.getJobChain()));
            jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
            storeAuditLogEntry(orderAudit);

            return jocXmlCommand.getSurveyDate();
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), order));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, getJocError(), order));
        }
        return null;
    }
}
