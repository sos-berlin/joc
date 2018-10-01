package com.sos.joc.job.impl;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.calendar.CalendarsOfAnObject;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobCalendarsResource;
import com.sos.joc.model.calendar.CalendarType;
import com.sos.joc.model.calendar.Calendars;
import com.sos.joc.model.job.JobFilter;

@Path("job")
public class JobCalendarsResourceImpl extends JOCResourceImpl implements IJobCalendarsResource {

    private static final String API_CALL = "./job/calendars";

    @Override
    public JOCDefaultResponse postJobCalendars(String accessToken, JobFilter jobFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobFilter, accessToken, jobFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    jobFilter.getJobschedulerId(), accessToken).getJob().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("job", jobFilter.getJob());
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);

            Calendars entity = CalendarsOfAnObject.get(connection, dbItemInventoryInstance.getSchedulerId(), CalendarType.JOB, normalizePath(jobFilter
                    .getJob()), jobFilter.getCompact());

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
