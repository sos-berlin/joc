package com.sos.joc.order.impl;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryOrder;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.runtime.RunTime;
import com.sos.joc.db.inventory.orders.InventoryOrdersDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.RunTime200;
import com.sos.joc.model.order.OrderFilter;
import com.sos.joc.order.resource.IOrderRunTimeResource;

@Path("order")
public class OrderRunTimeResourceImpl extends JOCResourceImpl implements IOrderRunTimeResource {

    private static final String API_CALL = "./order/run_time";

    @Override
    public JOCDefaultResponse postOrderRunTime(String accessToken, OrderFilter orderFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, orderFilter, accessToken, orderFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            RunTime200 runTimeAnswer = new RunTime200();
            if (checkRequiredParameter("orderId", orderFilter.getOrderId()) && checkRequiredParameter("jobChain", orderFilter.getJobChain())) {
                JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
                String jobChainPath = normalizePath(orderFilter.getJobChain());
                String orderCommand = jocXmlCommand.getShowOrderCommand(jobChainPath, orderFilter.getOrderId(), "source run_time");
                connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                InventoryOrdersDBLayer dbLayer = new InventoryOrdersDBLayer(connection);
                DBItemInventoryOrder dbItem = dbLayer.getInventoryOrderByOrderId(jobChainPath, orderFilter.getOrderId(), dbItemInventoryInstance
                        .getId());
                Boolean runTimeIsTemporary = Boolean.FALSE;
                if (dbItem != null && dbItem.getRunTimeIsTemporary() != null) {
                    runTimeIsTemporary = dbItem.getRunTimeIsTemporary();
                }
                runTimeAnswer = RunTime.set(jobChainPath, jocXmlCommand, orderCommand, "//order/run_time", accessToken, runTimeIsTemporary);
            }
            return JOCDefaultResponse.responseStatus200(runTimeAnswer);
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
