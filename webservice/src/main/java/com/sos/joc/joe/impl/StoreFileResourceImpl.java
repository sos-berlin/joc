package com.sos.joc.joe.impl;

import java.sql.Date;
import java.time.Instant;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.jitl.reporting.helper.EConfigFileExtensions;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.common.Helper;
import com.sos.joc.joe.resource.IReadFileResource;
import com.sos.joc.joe.resource.IStoreFileResource;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.JSObjectEdit;
import com.sos.joc.model.joe.processclass.ProcessClass;

@Path("joe")
public class StoreFileResourceImpl extends JOCResourceImpl implements IStoreFileResource {

    private static final String API_CALL = "./joe/read/file";

    @Override
    public JOCDefaultResponse storeFile(final String accessToken, final byte[] jsObj) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            JSObjectEdit body = Globals.objectMapper.readValue(jsObj, JSObjectEdit.class);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), Helper.hasPermission(body
                    .getObjectType(), getPermissonsJocCockpit(body.getJobschedulerId(), accessToken)));
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("path", body.getPath());
            String path = normalizePath(body.getPath() + "/");

            if (!this.folderPermissions.isPermittedForFolder(getParent(path))) {
                return accessDeniedResponse();
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            DBLayerJoeObjects dbLayerJoeObjects = new DBLayerJoeObjects(sosHibernateSession);
          // Satz in db schreiben.


            /*DBItemJoeObject dbItemJoeObject = dbLayerJoeObjects.getJoeObject(filterJoeObjects);

            JOCHotFolder jocHotFolder = new JOCHotFolder(this);
            byte[] fileContent = jocHotFolder.getFile(path + Helper.getFileExtension(body.getObjectType()));

            if (dbItemJoeObject == null || jocHotFolder.getLastModifiedDate().after(dbItemJoeObject.getModified())) {
                body.setConfigurationDate(jocHotFolder.getLastModifiedDate());
            } else {
                body.setConfigurationDate(dbItemJoeObject.getModified());
                fileContent = dbItemJoeObject.getConfiguration().getBytes();
            }

            body.setDeployed(false);

            switch (body.getObjectType()) {
            case JOB:
                body.setConfiguration(Globals.xmlMapper.readValue(fileContent, ProcessClass.class));
                break;
            case ORDER:
                body.setConfiguration(Globals.xmlMapper.readValue(fileContent, com.sos.joc.model.joe.order.Order.class));
                break;
            case JOBCHAIN:
                body.setConfiguration(Globals.xmlMapper.readValue(fileContent, com.sos.joc.model.joe.jobchain.JobChain.class));
                break;
            case LOCK:
                body.setConfiguration(Globals.xmlMapper.readValue(fileContent, com.sos.joc.model.joe.lock.Lock.class));
                break;
            case PROCESSCLASS:
            case AGENTCLUSTER:
                body.setConfiguration(Globals.xmlMapper.readValue(fileContent, com.sos.joc.model.joe.processclass.ProcessClass.class));
                break;
            case SCHEDULE:
                body.setConfiguration(Globals.xmlMapper.readValue(fileContent, com.sos.joc.model.joe.schedule.Schedule.class));
                break;
            case MONITOR:
                body.setConfiguration(Globals.xmlMapper.readValue(fileContent, ProcessClass.class));
                break;

            default:
                break;
            }
*/
            body.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(body);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseHTMLStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseHTMLStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

}
