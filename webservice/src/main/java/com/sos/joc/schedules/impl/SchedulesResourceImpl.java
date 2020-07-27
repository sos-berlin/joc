package com.sos.joc.schedules.impl;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.classes.schedule.ScheduleVolatile;
import com.sos.joc.db.inventory.schedules.InventorySchedulesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.schedule.SchedulePath;
import com.sos.joc.model.schedule.ScheduleV;
import com.sos.joc.model.schedule.SchedulesFilter;
import com.sos.joc.model.schedule.SchedulesV;
import com.sos.joc.schedules.resource.ISchedulesResource;
import com.sos.schema.JsonValidator;

@Path("schedules")
public class SchedulesResourceImpl extends JOCResourceImpl implements ISchedulesResource {

    private static final String API_CALL = "./schedules";

    @Override
    public JOCDefaultResponse postSchedules(String accessToken, byte[] schedulesFilterBytes) {
        SOSHibernateSession connection = null;

		try {
		    JsonValidator.validateFailFast(schedulesFilterBytes, SchedulesFilter.class);
		    SchedulesFilter schedulesFilter = Globals.objectMapper.readValue(schedulesFilterBytes, SchedulesFilter.class);
            
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, schedulesFilter, accessToken,
					schedulesFilter.getJobschedulerId(),
					getPermissonsJocCockpit(schedulesFilter.getJobschedulerId(), accessToken).getSchedule().getView()
							.isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

            List<ScheduleV> listOfSchedules = new ArrayList<ScheduleV>();
            boolean withFolderFilter = schedulesFilter.getFolders() != null && !schedulesFilter.getFolders().isEmpty();
            boolean hasPermission = true;
            Set<Folder> folders = addPermittedFolders(schedulesFilter.getFolders());

            Set<String> setOfSchedules = new HashSet<String>();
            if (schedulesFilter.getSchedules() != null && !schedulesFilter.getSchedules().isEmpty()) {
                Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
                hasPermission = false;
                for (SchedulePath schedule : schedulesFilter.getSchedules()) {
                    if (schedule != null) {
                        checkRequiredParameter("schedules.schedule", schedule.getSchedule());
                        String schedulePath = normalizePath(schedule.getSchedule());
                        if (canAdd(schedulePath, permittedFolders)) {
                            setOfSchedules.add(schedulePath);
                            hasPermission = true;
                        }
                    }
                }
            } else if (withFolderFilter && (folders == null || folders.isEmpty())) {
                hasPermission = false;
            }

            if (hasPermission) {

                JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
                String command = jocXmlCommand.getShowStateCommand("folder schedule", "folders", null);
                jocXmlCommand.executePostWithThrowBadRequestAfterRetry(command, accessToken);
                Date surveyDate = jocXmlCommand.getSurveyDate();

                NodeList schedules = jocXmlCommand.getSosxml().selectNodeList("/spooler/answer//schedules/schedule");

                InventorySchedulesDBLayer dbLayer = null;
                try {
                    connection = Globals.createSosHibernateStatelessConnection(API_CALL);
                    Globals.beginTransaction(connection);
                    dbLayer = new InventorySchedulesDBLayer(connection);
                } catch (Exception e) {
                }

                Set<Folder> permittedFolders = folderPermissions.getListOfFolders();

                for (int i = 0; i < schedules.getLength(); i++) {
                    Element scheduleElement = (Element) schedules.item(i);
                    ScheduleVolatile scheduleV = new ScheduleVolatile(surveyDate, scheduleElement, dbLayer, dbItemInventoryInstance);

                    if (!setOfSchedules.isEmpty() && !setOfSchedules.contains(scheduleV.getPath())) {
                        continue;
                    }
                    if (!setOfSchedules.isEmpty() && !canAdd(scheduleV.getPath(), permittedFolders)) {
                        continue;
                    }
                    if (!FilterAfterResponse.matchRegex(schedulesFilter.getRegex(), scheduleV.getPath())) {
                        continue;
                    }
                    if (!FilterAfterResponse.filterStateHasState(schedulesFilter.getStates(), scheduleV.getState().get_text())) {
                        continue;
                    }
                    if (!isInFolderList(folders, scheduleV.getPath())) {
                        continue;
                    }
                    listOfSchedules.add(scheduleV);
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
        } finally {
            Globals.disconnect(connection);
        }
    }

    private boolean isInFolderList(Collection<Folder> folders, String path) throws JocMissingRequiredParameterException {
        if (folders == null || folders.isEmpty()) {
            return true;
        }
        java.nio.file.Path folderOfSchedule = Paths.get(path).getParent();
        for (Folder folder : folders) {
            checkRequiredParameter("folders.folder", folder.getFolder());
            java.nio.file.Path folderFromFilter = Paths.get(normalizeFolder(folder.getFolder()));
            if (folder.getRecursive() == null || folder.getRecursive()) {
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