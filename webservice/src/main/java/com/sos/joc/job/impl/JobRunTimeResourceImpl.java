package com.sos.joc.job.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.runtime.RunTime;
import com.sos.joc.db.calendars.CalendarUsageDBLayer;
import com.sos.joc.db.configuration.CalendarUsageConfiguration;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.job.resource.IJobRunTimeResource;
import com.sos.joc.model.calendar.Calendar;
import com.sos.joc.model.common.RunTime200;
import com.sos.joc.model.job.JobFilter;
import com.sos.schema.JsonValidator;

@Path("job")
public class JobRunTimeResourceImpl extends JOCResourceImpl implements IJobRunTimeResource {

	private static final String API_CALL = "./job/run_time";

	@Override
	public JOCDefaultResponse postJobRunTime(String accessToken, byte[] jobFilterBytes) {
		SOSHibernateSession connection = null;
		try {
		    JsonValidator.validateFailFast(jobFilterBytes, JobFilter.class);
            JobFilter jobFilter = Globals.objectMapper.readValue(jobFilterBytes, JobFilter.class);
            
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobFilter, accessToken,
					jobFilter.getJobschedulerId(),
					getPermissonsJocCockpit(jobFilter.getJobschedulerId(), accessToken).getJob().getView().isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			checkRequiredParameter("job", jobFilter.getJob());
			RunTime200 runTimeAnswer = new RunTime200();
			String jobPath = normalizePath(jobFilter.getJob());
			JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
			String runTimeCommand = jocXmlCommand.getShowJobCommand(jobPath, "run_time", 0, 0);
			runTimeAnswer = RunTime.set(jobPath, jocXmlCommand, runTimeCommand, "//job/run_time", accessToken);

			connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            CalendarUsageDBLayer calendarUsageDBLayer = new CalendarUsageDBLayer(connection);
            List<CalendarUsageConfiguration> dbCalendars = calendarUsageDBLayer.getConfigurationsOfAnObject(dbItemInventoryInstance.getSchedulerId(), "JOB",
                    jobPath);
            if (dbCalendars != null && !dbCalendars.isEmpty()) {
                List<Calendar> calendars = new ArrayList<Calendar>();
                for (CalendarUsageConfiguration dbCalendar : dbCalendars) {
                    if (dbCalendar.getCalendar() != null) {
                        calendars.add(dbCalendar.getCalendar());
                    }
                }
                runTimeAnswer.getRunTime().setCalendars(calendars);
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
