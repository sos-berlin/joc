package com.sos.joc.joe.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.jobscheduler.model.event.CustomEvent;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOEHelper;
import com.sos.joc.classes.calendar.SendCalendarEventsUtil;
import com.sos.joc.db.joe.DBLayerJoeLocks;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JoeFolderAlreadyLockedException;
import com.sos.joc.joe.common.XmlSerializer;
import com.sos.joc.joe.resource.IStoreFileResource;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.JSObjectEdit;

@Path("joe")
public class StoreFileResourceImpl extends JOCResourceImpl implements IStoreFileResource {

    private static final String API_CALL = "./joe/store";

    @Override
    public JOCDefaultResponse storeFile(final String accessToken, final byte[] jsObj) {
        SOSHibernateSession sosHibernateSession = null;
        try {
                      
            JSObjectEdit body = Globals.objectMapper.readValue(jsObj, JSObjectEdit.class);
            checkRequiredParameter("objectType", body.getObjectType());
            
            SOSPermissionJocCockpit sosPermissionJocCockpit = getPermissonsJocCockpit(body.getJobschedulerId(), accessToken); 
            boolean permission = sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().isEdit();

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            if (versionIsOlderThan("1.13.1")) {
                throw new JobSchedulerBadRequestException("Unsupported web service: JobScheduler needs at least version 1.13.1");
            }

            checkRequiredParameter("path", body.getPath());
            boolean isDirectory = body.getObjectType() == JobSchedulerObjectType.FOLDER;
            String folder = null;

            if (isDirectory) {
                body.setPath(normalizeFolder(body.getPath()));
                folder = body.getPath();

            } else {
                body.setPath(normalizePath(body.getPath()));
                folder = getParent(body.getPath());
            }
            if (!this.folderPermissions.isPermittedForFolder(folder)) {
                return accessDeniedResponse();
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            LockResourceImpl.unForcelock(new DBLayerJoeLocks(sosHibernateSession), body.getJobschedulerId(), folder, getAccount());

            DBLayerJoeObjects dbLayer = new DBLayerJoeObjects(sosHibernateSession);
            FilterJoeObjects filter = new FilterJoeObjects();
            filter.setConstraint(body);
            DBItemJoeObject item = dbLayer.getJoeObject(filter);
            if (item != null) {
                if (!item.operationIsDelete()) {
                    item.setOperation("store");
                    item.setModified(Date.from(Instant.now()));
                    item.setAccount(getAccount());
                    if (!isDirectory) {
                        if (body.getConfiguration() != null) {
                            item.setConfiguration(Globals.objectMapper.writeValueAsString(XmlSerializer.serialize(body.getConfiguration(),
                                    JOEHelper.CLASS_MAPPING.get(body.getObjectType().value()))));
                        }
                    } else {
                        item.setConfiguration(null);
                    }
                    dbLayer.update(item);
                }
            } else {
                item = new DBItemJoeObject();
                item.setId(null);
                item.setAccount(getAccount());
                item.setSchedulerId(body.getJobschedulerId());
                item.setAuditLogId(null);
                item.setCreated(Date.from(Instant.now()));
                if (!isDirectory) {
                    if (body.getConfiguration() != null) {
                        item.setConfiguration(Globals.objectMapper.writeValueAsString(XmlSerializer.serialize(body.getConfiguration(),
                                JOEHelper.CLASS_MAPPING.get(body.getObjectType().value()))));
                    }
                } else {
                    item.setConfiguration(null);
                }
                item.setObjectType(body.getObjectType().value());
                item.setOperation("store");
                item.setPath(body.getPath());
                if ("/".equals(body.getPath())) {
                    item.setFolder(".");
                } else {
                    item.setFolder(getParent(body.getPath()));
                }
                dbLayer.save(item);
                
                try {
                    CustomEvent evt = JOEHelper.getJoeUpdatedEvent(folder);
                    SendCalendarEventsUtil.sendEvent(evt, dbItemInventoryInstance, accessToken);
                } catch (Exception e) {
                    //
                }
            }
            
            return JOCDefaultResponse.responseStatusJSOk(item.getModified());

        } catch (JoeFolderAlreadyLockedException e) {
            return JOEHelper.get434Response(e);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

}
