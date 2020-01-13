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
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.xmleditor.common.AnswerMessage;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.store.StoreConfiguration;
import com.sos.joc.model.xmleditor.store.StoreConfigurationAnswer;
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

                DBItemXmlEditorObject item = null;
                String name = null;
                if (in.getObjectType().equals(ObjectType.OTHER)) {
                    name = in.getName();
                    item = getOthersObject(dbLayer, in, name);
                } else {
                    name = JocXmlEditor.getConfigurationName(in.getObjectType());
                    item = getStandardObject(dbLayer, in);
                }

                if (item == null) {
                    item = create(session, in, name);

                } else {
                    item = update(session, in, item, name);
                }

                session.commit();
                response = JOCDefaultResponse.responseStatus200(getSuccess(in.getObjectType(), item.getId(), item.getModified(), item.getDeployed()));
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

    private DBItemXmlEditorObject getOthersObject(DbLayerXmlEditor dbLayer, StoreConfiguration in, String name) throws Exception {
        DBItemXmlEditorObject item = null;
        if (in.getId() != null && in.getId() > 0) {
            item = dbLayer.getObject(in.getId().longValue());
            if (item != null && !item.getObjectType().equals(ObjectType.OTHER.name())) {
                item = null;// dbLayer.getObject(in.getJobschedulerId(), ObjectType.OTHER.name(), name);
            }
        }
        return item;
    }

    private DBItemXmlEditorObject getStandardObject(DbLayerXmlEditor dbLayer, StoreConfiguration in) throws Exception {
        return dbLayer.getObject(in.getJobschedulerId(), in.getObjectType().name(), JocXmlEditor.getConfigurationName(in.getObjectType(), in
                .getName()));
    }

    private DBItemXmlEditorObject create(SOSHibernateSession session, StoreConfiguration in, String name) throws Exception {
        DBItemXmlEditorObject item = new DBItemXmlEditorObject();
        item.setSchedulerId(in.getJobschedulerId());
        item.setObjectType(in.getObjectType().name());
        item.setName(name.trim());
        item.setConfigurationDraft(in.getConfiguration());
        item.setConfigurationDraftJson(in.getConfigurationJson());
        if (in.getObjectType().equals(ObjectType.OTHER)) {
            item.setSchemaLocation(in.getSchemaIdentifier());
        } else {
            item.setSchemaLocation(JocXmlEditor.getStandardRelativeSchemaLocation(in.getObjectType()));
        }
        item.setAuditLogId(new Long(0));// TODO
        item.setAccount(getAccount());
        item.setCreated(new Date());
        item.setModified(item.getCreated());
        session.save(item);
        return item;
    }

    private DBItemXmlEditorObject update(SOSHibernateSession session, StoreConfiguration in, DBItemXmlEditorObject item, String name)
            throws Exception {
        item.setName(name.trim());
        item.setConfigurationDraft(SOSString.isEmpty(in.getConfiguration()) ? null : in.getConfiguration());
        item.setConfigurationDraftJson(in.getConfigurationJson());
        if (in.getObjectType().equals(ObjectType.OTHER)) {
            item.setSchemaLocation(in.getSchemaIdentifier());
        } else {
            item.setSchemaLocation(JocXmlEditor.getStandardRelativeSchemaLocation(in.getObjectType()));
        }
        // item.setAuditLogId(new Long(0));// TODO
        item.setAccount(getAccount());
        item.setModified(new Date());
        session.update(item);
        return item;
    }

    private void checkRequiredParameters(final StoreConfiguration in) throws Exception {
        checkRequiredParameter("jobschedulerId", in.getJobschedulerId());
        JocXmlEditor.checkRequiredParameter("objectType", in.getObjectType());
        checkRequiredParameter("configuration", in.getConfiguration());
        checkRequiredParameter("configurationJson", in.getConfigurationJson());
        if (in.getObjectType().equals(ObjectType.OTHER)) {
            checkRequiredParameter("id", in.getId());
            checkRequiredParameter("name", in.getName());
            checkRequiredParameter("schemaIdentifier", in.getSchemaIdentifier());
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

    private StoreConfigurationAnswer getSuccess(ObjectType type, Long id, Date modified, Date deployed) {
        StoreConfigurationAnswer answer = new StoreConfigurationAnswer();
        answer.setId(id.intValue());
        answer.setModified(modified);
        if (!type.equals(ObjectType.OTHER)) {
            answer.setMessage(new AnswerMessage());
            if (deployed == null) {
                answer.getMessage().setCode(JocXmlEditor.MESSAGE_CODE_LIVE_NOT_EXIST);
                answer.getMessage().setMessage(JocXmlEditor.MESSAGE_LIVE_NOT_EXIST);
            } else {
                answer.getMessage().setCode(JocXmlEditor.MESSAGE_CODE_DRAFT_IS_NEWER);
                answer.getMessage().setMessage(JocXmlEditor.MESSAGE_DRAFT_IS_NEWER);
            }
        }
        return answer;
    }

}
