package com.sos.joc.schedules.impl;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.classes.schedule.ScheduleVolatile;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.schedule.SchedulePath;
import com.sos.joc.model.schedule.ScheduleV;
import com.sos.joc.model.schedule.SchedulesFilter;
import com.sos.joc.model.schedule.SchedulesV;
import com.sos.joc.schedules.resource.ISchedulesResource;

@Path("schedules")
public class SchedulesResourceImpl extends JOCResourceImpl implements ISchedulesResource {

    private static final String API_CALL = "./schedules";

    @Override
    public JOCDefaultResponse postSchedules(String accessToken, SchedulesFilter schedulesFilter) throws Exception {
        try {
            initLogging(API_CALL, schedulesFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, schedulesFilter.getJobschedulerId(), getPermissonsJocCockpit(accessToken).getSchedule()
                    .getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
            String command = jocXmlCommand.getShowStateCommand("folder schedule", "folders", null);
            jocXmlCommand.executePostWithThrowBadRequestAfterRetry(command, accessToken);
            Date surveyDate = jocXmlCommand.getSurveyDate();
            
            NodeList schedules = jocXmlCommand.getSosxml().selectNodeList("/spooler/answer//schedules/schedule");
            List<ScheduleV> listOfSchedules = new ArrayList<ScheduleV>();

            Set<String> setOfSchedules = new HashSet<String>();
            for (SchedulePath schedule : schedulesFilter.getSchedules()) {
                checkRequiredParameter("schedules.schedule", schedule.getSchedule());
                setOfSchedules.add(normalizePath(schedule.getSchedule()));
            }

            for (int i = 0; i < schedules.getLength(); i++) {
                Element scheduleElement = (Element) schedules.item(i);
                ScheduleVolatile scheduleV = new ScheduleVolatile(surveyDate, scheduleElement);
                
                if(!setOfSchedules.isEmpty() && !setOfSchedules.contains(scheduleV.getPath())) {
                    continue;
                }
                if (!FilterAfterResponse.matchRegex(schedulesFilter.getRegex(), scheduleV.getPath())) {
                    continue;
                }
                if (!FilterAfterResponse.filterStateHasState(schedulesFilter.getStates(), scheduleV.getState().get_text())) {
                    continue;
                }
                if (!isInFolderList(schedulesFilter.getFolders(), scheduleV.getPath())) {
                    continue;
                }
                listOfSchedules.add(scheduleV);
            }

            SchedulesV entity = new SchedulesV();
            entity.setSchedules(listOfSchedules);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private boolean isInFolderList(List<Folder> folders, String path) throws JocMissingRequiredParameterException {
        if (folders == null || folders.isEmpty()) {
            return true;
        }
        java.nio.file.Path folderOfSchedule = Paths.get(path).getParent();
        for (Folder folder : folders) {
            checkRequiredParameter("folders.folder", folder.getFolder());
            java.nio.file.Path folderFromFilter = Paths.get(normalizeFolder(folder.getFolder()));
            if (folder.getRecursive() == null || folder.getRecursive() ) {
                if (folderOfSchedule.startsWith(folderFromFilter)) {
                    return true;
                }
            } else {
                if (folderOfSchedule.equals(folderFromFilter)) {
                    return true;
                }
            }
        }
        return false;
    }
}