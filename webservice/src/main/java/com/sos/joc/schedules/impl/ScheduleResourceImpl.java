package com.sos.joc.schedules.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.schedules.resource.ISchedulesResource;
import com.sos.scheduler.model.commands.JSCmdShowState;
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.schedule.Schedule_;
import com.sos.joc.model.schedule.Schedule__;
import com.sos.joc.model.schedule.SchedulesFilterSchema;
import com.sos.joc.model.schedule.SchedulesVSchema;
import com.sos.joc.model.schedule.State;
import com.sos.joc.model.schedule.State.Text;
import com.sos.joc.model.schedule.State_;

@Path("schedules")
public class ScheduleResourceImpl extends JOCResourceImpl implements ISchedulesResource {
    private static final String ATTRIBUTE_ACTIVE = "active";
    private static final String ATTRIBUTE_NOW_COVERED_BY_SCHEDULE = "now_covered_by_schedule";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_PATH = "path";
    private static final String SLASH = "/";
    private static final String DEFAULT_NAME = "(default)";
    private static final String WHAT = "folders";
    private static final String SUBSYSTEMS = "folder schedule";
    private static final String XPATH_SCHEDULES = "//schedule";

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleResourceImpl.class);
    private SchedulesFilterSchema schedulesFilterSchema;
    private HashMap<String, String> mapOfSchedules;


    @Override
    public JOCDefaultResponse postSchedules(String accessToken, SchedulesFilterSchema schedulesFilterSchema) throws Exception {
        LOGGER.debug("init schedules");
        try {
            JOCDefaultResponse jocDefaultResponse = init(schedulesFilterSchema.getJobschedulerId(), getPermissons(accessToken).getSchedule().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            this.schedulesFilterSchema = schedulesFilterSchema;

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getCommandUrl());

            JSCmdShowState jsCmdShowState = Globals.schedulerObjectFactory.createShowState();
            jsCmdShowState.setSubsystems(SUBSYSTEMS);
            jsCmdShowState.setWhat(WHAT);
            String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdShowState);
            jocXmlCommand.excutePost(xml);

            jocXmlCommand.createNodeList(XPATH_SCHEDULES);

            int count = jocXmlCommand.getNodeList().getLength();

            List<Schedule_> listOfSchedules = new ArrayList<Schedule_>();
            SchedulesVSchema entity = new SchedulesVSchema();
            entity.setDeliveryDate(new Date());

            Pattern pattern = null;
            if (!("".equals(schedulesFilterSchema.getRegex()) || schedulesFilterSchema.getRegex() == null)) {
                pattern = Pattern.compile(schedulesFilterSchema.getRegex());
            }

            createSchedulesMap();

            for (int i = 0; i < count; i++) {
                Element scheduleElement = jocXmlCommand.getElementFromList(i);

                String path = jocXmlCommand.getAttributeWithDefault(ATTRIBUTE_PATH, SLASH + DEFAULT_NAME);
                String activeValue = jocXmlCommand.getAttributeWithDefault(ATTRIBUTE_ACTIVE, SLASH + DEFAULT_NAME);
                Text stateText;
                if ("yes".equals(activeValue)){
                    stateText = State.Text.ACTIVE;
                }else{
                    stateText = State.Text.INACTIVE;
                }

                boolean addSchedule = (!schedulesFilterSchema.getSchedules().isEmpty() && isInScheduleMap(path)) || (schedulesFilterSchema.getSchedules()
                        .isEmpty() && (matchesRegex(pattern, path) && isInFolderList(path) && isInActiveList(stateText)));

                if (addSchedule) {

                    Schedule_ schedule = new Schedule_();
                 //   schedule.set(jocXmlCommand.getSurveyDate());
                    schedule.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(scheduleElement));
                    schedule.setName(jocXmlCommand.getAttribute(ATTRIBUTE_NAME));
                    schedule.setPath(path);
                    State state = new State();
                    if ("yes".equals(activeValue)){
                        state.setSeverity(0);
                        state.setText(State.Text.ACTIVE);
                    }else{
                        state.setSeverity(1);
                        state.setText(State.Text.INACTIVE);
                    }
                    
                    schedule.setState(state);
                    schedule.setSubstitutedBy(jocXmlCommand.getAttribute(ATTRIBUTE_NOW_COVERED_BY_SCHEDULE));
                    listOfSchedules.add(schedule);
                }
            }
            entity.setSchedules(listOfSchedules);
            return JOCDefaultResponse.responseStatus200(entity);

        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
    }
 

    private boolean isInScheduleMap(String path) {
        return (schedulesFilterSchema.getSchedules().isEmpty() || mapOfSchedules.get(path) != null);
    }

    private void createSchedulesMap() throws JocMissingRequiredParameterException {
        mapOfSchedules = new HashMap<String, String>();
        for (Schedule__ schedule : schedulesFilterSchema.getSchedules()) {
            String scheduleName = schedule.getSchedule();
            checkRequiredParameter("schedules.schedule", scheduleName);
            mapOfSchedules.put(scheduleName, scheduleName);
        }

    }

    private boolean isInFolderList(String path) throws JocMissingRequiredParameterException {
        if (schedulesFilterSchema.getFolders().size() == 0) {
            return true;
        }

        path = getParent(path);
        for (FoldersSchema folder : schedulesFilterSchema.getFolders()) {
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

    private boolean isInActiveList(Text stateText)  {
        if (schedulesFilterSchema.getState().size() == 0) {
            return true;
        }

        for (State_ stateEntry : schedulesFilterSchema.getState()) {
            stateEntry.toString();
            if (stateEntry.toString().equals(stateText.toString())) {
                return true;
            }
        }
        return false;
    }
}