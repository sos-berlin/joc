package com.sos.joc.orders.impl;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.Orders;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.order.OrdersSnapshot;
import com.sos.joc.orders.resource.IOrdersResourceOverviewSnapshot;

@Path("orders")
public class OrdersResourceOverviewSnapshotImpl extends JOCResourceImpl implements IOrdersResourceOverviewSnapshot {

    private static final String API_CALL = "./orders/overview/snapshot";

    @Override
    public JOCDefaultResponse postOrdersOverviewSnapshot(String accessToken, JobChainsFilter jobChainsFilter) throws Exception {
        try {
            initLogging(API_CALL, jobChainsFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobChainsFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken).getOrder()
                    .getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            OrdersSnapshot entity = Orders.getSnapshot(dbItemInventoryInstance.getUrl(), accessToken, jobChainsFilter);
            
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }

    }
}
