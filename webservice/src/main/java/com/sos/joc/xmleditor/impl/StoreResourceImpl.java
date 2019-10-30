package com.sos.joc.xmleditor.impl;

import java.util.Date;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemXmlEditorObject;
import com.sos.jitl.xmleditor.db.DbLayerXmlEditor;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.store.StoreConfiguration;
import com.sos.joc.model.xmleditor.store.StoreConfigurationAnswer;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.xmleditor.resource.IStoreResource;

import sos.util.SOSString;

@Path(JocXmlEditor.APPLICATION_PATH)
public class StoreResourceImpl extends JOCResourceImpl implements IStoreResource {

    @Override
    public JOCDefaultResponse store(final String accessToken, final StoreConfiguration in) {
        SOSHibernateSession session = null;
        try {
            checkRequiredParameters(in);

            JOCDefaultResponse response = checkPermissions(accessToken, in);
            if (response == null) {
                session = Globals.createSosHibernateStatelessConnection(IMPL_PATH);
                session.beginTransaction();

                DbLayerXmlEditor dbLayer = new DbLayerXmlEditor(session);
                DBItemXmlEditorObject item = dbLayer.getObject(in.getJobschedulerId(), in.getObjectType().name(), JocXmlEditor.getConfigurationName(in
                        .getObjectType(), in.getName()));

                if (item == null) {
                    item = new DBItemXmlEditorObject();
                    item.setSchedulerId(in.getJobschedulerId());
                    item.setObjectType(in.getObjectType().name());
                    item.setName(JocXmlEditor.getConfigurationName(in.getObjectType(), in.getName()));
                    item.setConfiguration(in.getConfiguration());
                    item.setSchemaLocation(JocXmlEditor.getSchemaLocation(in.getObjectType(), in.getSchema()));

                    item.setAuditLogId(new Long(0));// TODO
                    item.setAccount(getAccount());
                    item.setCreated(new Date());
                    item.setModified(item.getCreated());
                    session.save(item);

                } else {
                    item.setConfiguration(SOSString.isEmpty(in.getConfiguration()) ? null : in.getConfiguration());
                    item.setSchemaLocation(JocXmlEditor.getSchemaLocation(in.getObjectType(), in.getSchema()));

                    item.setAuditLogId(new Long(0));// TODO
                    item.setAccount(getAccount());
                    item.setModified(new Date());
                    session.update(item);
                }

                session.commit();
                response = JOCDefaultResponse.responseStatus200(getSuccess(item.getModified()));
            }
            return response;
        } catch (JocException e) {
            Globals.rollback(session);
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            Globals.rollback(session);
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(session);
        }
    }

    private void checkRequiredParameters(final StoreConfiguration in) throws Exception {
        checkRequiredParameter("jobschedulerId", in.getJobschedulerId());
        JocXmlEditor.checkRequiredParameter("objectType", in.getObjectType());
        checkRequiredParameter("configuration", in.getConfiguration());
        if (in.getObjectType().equals(ObjectType.OTHER)) {
            checkRequiredParameter("name", in.getName());
            checkRequiredParameter("schema", in.getSchema());
        }
    }

    private JOCDefaultResponse checkPermissions(final String accessToken, final StoreConfiguration in) throws Exception {
        SOSPermissionJocCockpit permissions = getPermissonsJocCockpit(in.getJobschedulerId(), accessToken);
        boolean permission = permissions.getJobschedulerMaster().getAdministration().getConfigurations().isEdit();
        JOCDefaultResponse response = init(IMPL_PATH, in, accessToken, in.getJobschedulerId(), permission);
        if (response == null) {
            if (versionIsOlderThan(JocXmlEditor.AVAILABILITY_STARTING_WITH)) {
                throw new JobSchedulerBadRequestException(JocXmlEditor.MESSAGE_UNSUPPORTED_WEB_SERVICE);
            }
        }
        return response;
    }

    private StoreConfigurationAnswer getSuccess(Date date) {
        StoreConfigurationAnswer answer = new StoreConfigurationAnswer();
        answer.setModified(date);
        return answer;
    }

}
