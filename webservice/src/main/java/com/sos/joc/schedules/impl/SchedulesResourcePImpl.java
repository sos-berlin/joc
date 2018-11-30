package com.sos.joc.schedules.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventorySchedule;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.schedule.SchedulePermanent;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.db.inventory.schedules.InventorySchedulesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.schedule.ScheduleP;
import com.sos.joc.model.schedule.SchedulePath;
import com.sos.joc.model.schedule.SchedulesFilter;
import com.sos.joc.model.schedule.SchedulesP;
import com.sos.joc.schedules.resource.ISchedulesResourceP;

@Path("schedules")
public class SchedulesResourcePImpl extends JOCResourceImpl implements ISchedulesResourceP {

    private static final String API_CALL = "./schedules/p";

    @Override
    public JOCDefaultResponse postSchedulesP(String xAccessToken, String accessToken, SchedulesFilter schedulesFilter) throws Exception {
        return postSchedulesP(getAccessToken(xAccessToken, accessToken), schedulesFilter);
    }

    public JOCDefaultResponse postSchedulesP(String accessToken, SchedulesFilter schedulesFilter) throws Exception {
        SOSHibernateSession connection = null;

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, schedulesFilter, accessToken, schedulesFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(schedulesFilter.getJobschedulerId(), accessToken).getSchedule().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            // FILTER
            boolean withFolderFilter = schedulesFilter.getFolders() != null && !schedulesFilter.getFolders().isEmpty();
            List<Folder> folders = addPermittedFolder(schedulesFilter.getFolders());
            List<SchedulePath> schedules = schedulesFilter.getSchedules();

            Globals.beginTransaction(connection);
            InventorySchedulesDBLayer dbLayer = new InventorySchedulesDBLayer(connection);
            List<ScheduleP> listOfSchedules = new ArrayList<ScheduleP>();
            DocumentationDBLayer dbDocLayer = new DocumentationDBLayer(connection);
            Map<String, String> documentations = dbDocLayer.getDocumentationPaths(schedulesFilter.getJobschedulerId(),
                    JobSchedulerObjectType.SCHEDULE);

            if (schedules != null && !schedules.isEmpty()) {
                List<ScheduleP> schedulesToAdd = new ArrayList<ScheduleP>();
                Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
                for (SchedulePath schedulePath : schedules) {
                    if (schedulePath != null && canAdd(schedulePath.getSchedule(), permittedFolders)) {
                        DBItemInventorySchedule scheduleFromDb = dbLayer.getSchedule(normalizePath(schedulePath.getSchedule()),
                                dbItemInventoryInstance.getId());
                        if (scheduleFromDb == null) {
                            continue;
                        }
                        ScheduleP scheduleP = SchedulePermanent.initSchedule(dbLayer, scheduleFromDb, documentations.get(scheduleFromDb.getName()),  dbItemInventoryInstance);
                        if (scheduleP != null) {
                            schedulesToAdd.add(scheduleP);
                        }
                    }
                }
                if (schedulesToAdd != null && !schedulesToAdd.isEmpty()) {
                    listOfSchedules.addAll(schedulesToAdd);
                }
            } else if (withFolderFilter && (folders == null || folders.isEmpty())) {
                // no permission
            } else if (folders != null && !folders.isEmpty()) {
                for (Folder folder : folders) {
                    List<DBItemInventorySchedule> schedulesFromDb = null;
                    schedulesFromDb = dbLayer.getSchedulesByFolders(normalizeFolder(folder.getFolder()), dbItemInventoryInstance.getId(), folder
                            .getRecursive().booleanValue());
                    List<ScheduleP> schedulesToAdd = getSchedulesToAdd(dbLayer, schedulesFromDb, documentations, dbItemInventoryInstance, schedulesFilter.getRegex());
                    if (schedulesToAdd != null && !schedulesToAdd.isEmpty()) {
                        listOfSchedules.addAll(schedulesToAdd);
                    }
                }
            } else {
                List<DBItemInventorySchedule> processClassesFromDb = dbLayer.getSchedules(dbItemInventoryInstance.getId());
                List<ScheduleP> schedulesToAdd = getSchedulesToAdd(dbLayer, processClassesFromDb, documentations, dbItemInventoryInstance, schedulesFilter
                        .getRegex());
                if (schedulesToAdd != null && !schedulesToAdd.isEmpty()) {
                    listOfSchedules.addAll(schedulesToAdd);
                }
            }
            SchedulesP entity = new SchedulesP();
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

    private List<ScheduleP> getSchedulesToAdd(InventorySchedulesDBLayer dbLayer, List<DBItemInventorySchedule> schedulesFromDb,
            Map<String, String> documentations, DBItemInventoryInstance instance, String regex) throws Exception {
        List<ScheduleP> schedulesToAdd = new ArrayList<ScheduleP>();
        if (schedulesFromDb != null) {
            for (DBItemInventorySchedule scheduleFromDb : schedulesFromDb) {
                if (regex != null && !regex.isEmpty()) {
                    Matcher regExMatcher = Pattern.compile(regex).matcher(scheduleFromDb.getName());
                    if (regExMatcher.find()) {
                        schedulesToAdd.add(SchedulePermanent.initSchedule(dbLayer, scheduleFromDb, documentations.get(scheduleFromDb.getName()), instance));
                    }
                } else {
                    schedulesToAdd.add(SchedulePermanent.initSchedule(dbLayer, scheduleFromDb, documentations.get(scheduleFromDb.getName()), instance));
                }
            }
        }
        return schedulesToAdd;
    }
}