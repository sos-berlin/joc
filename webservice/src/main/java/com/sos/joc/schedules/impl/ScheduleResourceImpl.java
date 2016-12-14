package com.sos.joc.schedules.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import org.w3c.dom.Element;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.schedule.ScheduleVolatile;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.schedule.SchedulePath;
import com.sos.joc.model.schedule.ScheduleStateText;
import com.sos.joc.model.schedule.ScheduleV;
import com.sos.joc.model.schedule.SchedulesFilter;
import com.sos.joc.model.schedule.SchedulesV;
import com.sos.joc.schedules.resource.ISchedulesResource;

@Path("schedules")
public class ScheduleResourceImpl extends JOCResourceImpl implements ISchedulesResource {

    private static final String API_CALL = "./schedules";
    private SchedulesFilter schedulesFilter;
    private HashMap<String, String> mapOfSchedules;

    @Override
    public JOCDefaultResponse postSchedules(String accessToken, SchedulesFilter schedulesFilter) throws Exception {
        try {
            initLogging(API_CALL, schedulesFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, schedulesFilter.getJobschedulerId(), getPermissons(accessToken).getSchedule()
                    .getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            this.schedulesFilter = schedulesFilter;

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
            String command = jocXmlCommand.getShowStateCommand("folder schedule", "folders", null);
            jocXmlCommand.executePostWithThrowBadRequestAfterRetry(command, accessToken);

            jocXmlCommand.createNodeList("//schedule");

            int count = jocXmlCommand.getNodeList().getLength();

            List<ScheduleV> listOfSchedules = new ArrayList<ScheduleV>();

            Pattern pattern = null;
            if (!("".equals(schedulesFilter.getRegex()) || schedulesFilter.getRegex() == null)) {
                pattern = Pattern.compile(schedulesFilter.getRegex());
            }

            createSchedulesMap();

            for (int i = 0; i < count; i++) {
                Element scheduleElement = jocXmlCommand.getElementFromList(i);

                String path = jocXmlCommand.getAttribute("path");
                String activeValue = jocXmlCommand.getAttribute("active");
                ScheduleStateText stateText;
                if ("yes".equals(activeValue)) {
                    stateText = ScheduleStateText.ACTIVE;
                } else {
                    stateText = ScheduleStateText.INACTIVE;
                }
                boolean addSchedule = (!schedulesFilter.getSchedules().isEmpty() && isInScheduleMap(path)) || (schedulesFilter.getSchedules()
                        .isEmpty() && (matchesRegex(pattern, path) && isInFolderList(path) && isInActiveList(stateText)));

                if (addSchedule) {
                    ScheduleVolatile schedule = new ScheduleVolatile(jocXmlCommand, scheduleElement);
                    schedule.setValues();
                    listOfSchedules.add(schedule);
                }
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

    private boolean isInScheduleMap(String path) {
        return (schedulesFilter.getSchedules().isEmpty() || mapOfSchedules.get(path) != null);
    }

    private void createSchedulesMap() throws JocMissingRequiredParameterException {
        mapOfSchedules = new HashMap<String, String>();
        for (SchedulePath schedule : schedulesFilter.getSchedules()) {
            String scheduleName = normalizePath(schedule.getSchedule());
            checkRequiredParameter("schedules.schedule", scheduleName);
            mapOfSchedules.put(scheduleName, scheduleName);
        }

    }

    private boolean isInFolderList(String path) throws JocMissingRequiredParameterException {
        if (schedulesFilter.getFolders().size() == 0) {
            return true;
        }

        path = getParent(path);
        for (Folder folder : schedulesFilter.getFolders()) {
            boolean isRecursive = (folder.getRecursive() || folder.getRecursive() == null);

            String folderName = folder.getFolder();
            checkRequiredParameter("folders.folder", folderName);

            folderName = normalizePath(folderName);
            path = normalizePath(path);

            if (path.equals(folderName) || (isRecursive && path.startsWith(folderName))) {
                return true;
            }

        }
        return false;
    }

    private boolean isInActiveList(ScheduleStateText stateText) {
        if (schedulesFilter.getStates().size() == 0) {
            return true;
        }
        for (ScheduleStateText stateEntry : schedulesFilter.getStates()) {
            if (stateEntry == stateText) {
                return true;
            }
        }
        return false;
    }
}