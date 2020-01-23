package com.sos.joc.xmleditor.impl;

import java.util.Arrays;
import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.delete.DeleteDraft;
import com.sos.joc.model.xmleditor.delete.DeleteOtherDraftAnswer;
import com.sos.joc.model.xmleditor.read.standard.ReadStandardConfigurationAnswer;
import com.sos.joc.xmleditor.common.standard.ReadConfigurationHandler;
import com.sos.joc.xmleditor.resource.IDeleteResource;
import com.sos.schema.JsonValidator;

import sos.util.SOSString;

@Path(JocXmlEditor.APPLICATION_PATH)
public class DeleteResourceImpl extends JOCResourceImpl implements IDeleteResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteResourceImpl.class);
    private static final boolean isTraceEnabled = LOGGER.isTraceEnabled();

    @Override
    public JOCDefaultResponse delete(final String accessToken, final byte[] filterBytes) {
        try {
            JsonValidator.validateFailFast(filterBytes, DeleteDraft.class);
            DeleteDraft in = Globals.objectMapper.readValue(filterBytes, DeleteDraft.class);
            
            checkRequiredParameters(in);

            JOCDefaultResponse response = checkPermissions(accessToken, in);
            if (response == null) {
                if (in.getObjectType().equals(ObjectType.OTHER)) {
                    checkRequiredParameter("id", in.getId());

                    DeleteOtherDraftAnswer answer = handleOtherConfigurations(in);
                    response = JOCDefaultResponse.responseStatus200(Globals.objectMapper.writeValueAsBytes(answer));
                } else {
                    ReadStandardConfigurationAnswer answer = handleStandardConfiguration(in);
                    response = JOCDefaultResponse.responseStatus200(Globals.objectMapper.writeValueAsBytes(answer));
                }
            }
            return response;
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private void checkRequiredParameters(final DeleteDraft in) throws Exception {
        checkRequiredParameter("jobschedulerId", in.getJobschedulerId());
        JocXmlEditor.checkRequiredParameter("objectType", in.getObjectType());
    }

    private JOCDefaultResponse checkPermissions(final String accessToken, final DeleteDraft in) throws Exception {
        SOSPermissionJocCockpit permissions = getPermissonsJocCockpit(in.getJobschedulerId(), accessToken);
        boolean permission = permissions.getJobschedulerMaster().getAdministration().getConfigurations().isDelete();
        JOCDefaultResponse response = init(IMPL_PATH, in, accessToken, in.getJobschedulerId(), permission);
        if (response == null) {
            if (versionIsOlderThan(JocXmlEditor.AVAILABILITY_STARTING_WITH)) {
                throw new JobSchedulerBadRequestException(JocXmlEditor.MESSAGE_UNSUPPORTED_WEB_SERVICE);
            }
        }
        return response;
    }

    private ReadStandardConfigurationAnswer handleStandardConfiguration(DeleteDraft in) throws Exception {
        DBItemXmlEditorObject item = updateItem(in.getJobschedulerId(), in.getObjectType().name(), JocXmlEditor.getConfigurationName(in
                .getObjectType()));

        ReadConfigurationHandler handler = new ReadConfigurationHandler(this, in.getObjectType());
        handler.readLive(item, in.getJobschedulerId());
        return handler.getAnswer();
    }

    private DeleteOtherDraftAnswer handleOtherConfigurations(DeleteDraft in) throws Exception {

        boolean deleted = deleteOtherItem(in.getId());

        DeleteOtherDraftAnswer answer = new DeleteOtherDraftAnswer();
        if (deleted) {
            answer.setDeleted(new Date());
        } else {
            answer.setFound(false);
        }
        return answer;
    }

    private boolean deleteOtherItem(Integer id) throws Exception {
        SOSHibernateSession session = null;
        try {
            session = Globals.createSosHibernateStatelessConnection(IMPL_PATH);
            DbLayerXmlEditor dbLayer = new DbLayerXmlEditor(session);

            session.beginTransaction();
            int deleted = dbLayer.deleteOtherObject(id.longValue());
            session.commit();
            if (isTraceEnabled) {
                LOGGER.trace(String.format("[id=%s]deleted=%s", id, deleted));
            }
            return Math.abs(deleted) > 0;
        } catch (Throwable e) {
            Globals.rollback(session);
            throw e;
        } finally {
            Globals.disconnect(session);
        }
    }

    private DBItemXmlEditorObject updateItem(String schedulerId, String objectType, String name) throws Exception {
        SOSHibernateSession session = null;
        try {
            session = Globals.createSosHibernateStatelessConnection(IMPL_PATH);
            DbLayerXmlEditor dbLayer = new DbLayerXmlEditor(session);

            session.beginTransaction();
            DBItemXmlEditorObject item = dbLayer.getObject(schedulerId, objectType, name);
            if (item == null) {
                if (isTraceEnabled) {
                    LOGGER.trace(String.format("[%s][%s][%s]not found", schedulerId, objectType, name));
                }
            } else {
                item.setConfigurationDraft(null);
                item.setAccount(getAccount());
                item.setModified(new Date());
                session.update(item);

                if (isTraceEnabled) {
                    LOGGER.trace(String.format("[%s][%s][%s]%s", schedulerId, objectType, name, SOSString.toString(item, Arrays.asList(
                            "configuration"))));
                }
            }
            session.commit();
            return item;
        } catch (Throwable e) {
            Globals.rollback(session);
            throw e;
        } finally {
            Globals.disconnect(session);
        }
    }
}
