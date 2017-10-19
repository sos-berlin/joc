package com.sos.joc.orders.impl;

import javax.ws.rs.Path;

import com.sos.jitl.reporting.db.filter.FilterFolder;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.Orders;
import com.sos.joc.exceptions.JobSchedulerConnectionResetException;
import com.sos.joc.exceptions.JobSchedulerObjectNotExistException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.order.OrdersSnapshot;
import com.sos.joc.orders.resource.IOrdersResourceOverviewSnapshot;

@Path("orders")
public class OrdersResourceOverviewSnapshotImpl extends JOCResourceImpl implements IOrdersResourceOverviewSnapshot {

    private static final String API_CALL = "./orders/overview/snapshot";

    @Override
    public JOCDefaultResponse postOrdersOverviewSnapshot(String xAccessToken, String accessToken, JobChainsFilter jobChainsFilter) throws Exception {
        return postOrdersOverviewSnapshot(getAccessToken(xAccessToken, accessToken), jobChainsFilter);
    }

    public JOCDefaultResponse postOrdersOverviewSnapshot(String accessToken, JobChainsFilter jobChainsFilter) throws Exception {
        String folders = "";
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobChainsFilter, accessToken, jobChainsFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            if (jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().size() > 0) {
                for (int i = 0; i < jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().size(); i++) {
                    FilterFolder folder = jobschedulerUser.getSosShiroCurrentUser().getSosShiroFolderPermissions().get(i);
                    folders = folders + folder.getFolder() + ",";
                    com.sos.joc.model.common.Folder f = new com.sos.joc.model.common.Folder();
                    f.setFolder(folder.getFolder());
                    f.setRecursive(folder.isRecursive());
                    jobChainsFilter.getFolders().add(f);
                }
            }

            OrdersSnapshot entity = Orders.getSnapshot(new JOCJsonCommand(this), accessToken, jobChainsFilter);

            return JOCDefaultResponse.responseStatus200(entity);

        } catch (JobSchedulerConnectionResetException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatus434JSError(e);
        } catch (JobSchedulerObjectNotExistException e) {
            JocError err = new JocError();
            err.setMessage(String.format("%s: Please check your folders in the Account Management (%s)", e.getMessage(), folders));
            JocException ee = new JocException(err);
            return JOCDefaultResponse.responseStatusJSError(ee);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }

    }
}
