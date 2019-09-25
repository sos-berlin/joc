package com.sos.joc.joe.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.common.Helper;
import com.sos.joc.joe.resource.IReadFileResource;
import com.sos.joc.model.joe.common.Filter;
import com.sos.joc.model.joe.common.JSObjectEdit;
import com.sos.joc.model.joe.job.JobEdit;
import com.sos.joc.model.joe.job.MonitorEdit;
import com.sos.joc.model.joe.jobchain.JobChainEdit;
import com.sos.joc.model.joe.lock.LockEdit;
import com.sos.joc.model.joe.order.OrderEdit;
import com.sos.joc.model.joe.processclass.ProcessClass;
import com.sos.joc.model.joe.processclass.ProcessClassEdit;
import com.sos.joc.model.joe.schedule.ScheduleEdit;

@Path("joe")
public class ReadFileResourceImpl extends JOCResourceImpl implements IReadFileResource {

    private static final String API_CALL = "./joe/read/file";

    @Override
    public JOCDefaultResponse readFile(final String accessToken, final Filter body) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), Helper.hasPermission(body
                    .getObjectType(), getPermissonsJocCockpit(body.getJobschedulerId(), accessToken)));
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("path", body.getPath());
            String path = normalizePath(body.getPath());

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            DBLayerJoeObjects dbLayerJoeObjects = new DBLayerJoeObjects(sosHibernateSession);
            FilterJoeObjects filterJoeObjects = new FilterJoeObjects();
            filterJoeObjects.setConstraint(body);
            
            DBItemJoeObject dbItemJoeObject = dbLayerJoeObjects.getJoeObject(filterJoeObjects);

            JOCHotFolder jocHotFolder = new JOCHotFolder(this);
            byte[] fileContent = jocHotFolder.getFile(path + Helper.getFileExtension(body.getObjectType()));
            Date configurationDate = null;
            
            if (dbItemJoeObject == null || jocHotFolder.getLastModifiedDate().after(dbItemJoeObject.getModified())) {
                //TODO muss hier der Satz in der DB geloescht werden, wenn vorhanden??
                configurationDate = jocHotFolder.getLastModifiedDate();
            } else {
                configurationDate = dbItemJoeObject.getModified();
                fileContent = dbItemJoeObject.getConfiguration().getBytes();
            }
            
            JSObjectEdit jsObjectEdit = null;
            
            switch (body.getObjectType()) {
            case JOB:
                jsObjectEdit = new JobEdit();
                jsObjectEdit.setConfiguration(Globals.xmlMapper.readValue(fileContent, com.sos.joc.model.joe.job.Job.class));
                break;
            case ORDER:
                jsObjectEdit = new OrderEdit();
                jsObjectEdit.setConfiguration(Globals.xmlMapper.readValue(fileContent, com.sos.joc.model.joe.order.Order.class));
                break;
            case JOBCHAIN:
                jsObjectEdit = new JobChainEdit();
                jsObjectEdit.setConfiguration(Globals.xmlMapper.readValue(fileContent, com.sos.joc.model.joe.jobchain.JobChain.class));
                break;
            case LOCK:
                jsObjectEdit = new LockEdit();
                jsObjectEdit.setConfiguration(Globals.xmlMapper.readValue(fileContent, com.sos.joc.model.joe.lock.Lock.class));
                break;
            case PROCESSCLASS:
            case AGENTCLUSTER:
                jsObjectEdit = new ProcessClassEdit();
                jsObjectEdit.setConfiguration(Globals.xmlMapper.readValue(fileContent, com.sos.joc.model.joe.processclass.ProcessClass.class));
                break;
            case SCHEDULE:
                jsObjectEdit = new ScheduleEdit();
                jsObjectEdit.setConfiguration(Globals.xmlMapper.readValue(fileContent, com.sos.joc.model.joe.schedule.Schedule.class));
                break;
            case MONITOR:
                jsObjectEdit = new MonitorEdit();
                jsObjectEdit.setConfiguration(Globals.xmlMapper.readValue(fileContent, ProcessClass.class));
                break;
            //TODO case for NODEPARAMS, HOLIDAYS, OTHERS
            default:
                throw new JobSchedulerBadRequestException("unsupported object type: " + body.getObjectType().value());
            }
            
            jsObjectEdit.setDeployed(dbItemJoeObject == null);
            jsObjectEdit.setConfigurationDate(configurationDate);
            jsObjectEdit.setJobschedulerId(body.getJobschedulerId());
            jsObjectEdit.setPath(path);
            jsObjectEdit.setObjectType(body.getObjectType());
            jsObjectEdit.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(jsObjectEdit);

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
