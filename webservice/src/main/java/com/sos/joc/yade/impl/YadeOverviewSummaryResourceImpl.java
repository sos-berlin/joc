package com.sos.joc.yade.impl;

import java.util.Date;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.db.yade.JocDBLayerYade;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.yade.TransferFilesSummary;
import com.sos.joc.model.yade.TransferFilter;
import com.sos.joc.yade.resource.IYadeOverviewSummaryResource;

@Path("yade")
public class YadeOverviewSummaryResourceImpl extends JOCResourceImpl implements IYadeOverviewSummaryResource {

    private static final String API_CALL = "./yade/overview/summary";

    @Override
    public JOCDefaultResponse postYadeOverviewSummary(String accessToken, TransferFilter filterBody) throws Exception {
        SOSHibernateSession connection = null;
        try {
            SOSPermissionJocCockpit sosPermission = getPermissonsJocCockpit(accessToken);
            // JobSchedulerId has to be "" to prevent exception to be thrown
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, filterBody, accessToken, filterBody.getJobschedulerId(), 
                    sosPermission.getYADE().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            // filter values
            String dateFrom = filterBody.getDateFrom();
            String dateTo = filterBody.getDateTo();
            String timeZone = filterBody.getTimeZone();
            Date from = null;
            Date to = null;
            if (dateFrom != null && !dateFrom.isEmpty()) {
                from = JobSchedulerDate.getDateFrom(dateFrom, timeZone);
            }
            if (dateTo != null && !dateTo.isEmpty()) {
                to = JobSchedulerDate.getDateTo(dateTo, timeZone);
            }
            JocDBLayerYade dbLayer = new JocDBLayerYade(connection);
            Integer successful = dbLayer.getSuccessfulTransferredFilesCount(from, to);
            Integer failed = dbLayer.getFailedTransferredFilesCount(from, to);

            TransferFilesSummary entity = new TransferFilesSummary();
            entity.setSuccessful(successful);
            entity.setFailed(failed);
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
