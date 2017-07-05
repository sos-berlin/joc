package com.sos.joc.yade.impl;

import java.util.Date;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.db.yade.DBLayerYade;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.yade.TransferFilesSummary;
import com.sos.joc.model.yade.TransferFilter;
import com.sos.joc.yade.resource.IYadeSummaryResource;


@Path("/yade/summary")
public class YadeSummaryResourceImpl extends JOCResourceImpl implements IYadeSummaryResource {

    private static final String API_CALL = "./yade/summary";

    @Override
    public JOCDefaultResponse postYadeSummary(String accessToken, TransferFilter filterBody) throws Exception {
        SOSHibernateSession connection = null;
        try {
            // TODO new Permissions for YADE
            SOSPermissionJocCockpit sosPermission = getPermissonsJocCockpit(accessToken);
            // TODO new init method for Yade without JobSchedulerId and new permissions
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, filterBody, accessToken, null, true);
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
            
            DBLayerYade dbLayer = new DBLayerYade(connection);
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
        }
    }

}
