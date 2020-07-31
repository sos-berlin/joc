package com.sos.joc.xmleditor.impl;

import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.xmleditor.db.DbLayerXmlEditor;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.delete.all.DeleteAll;
import com.sos.joc.model.xmleditor.delete.all.DeleteAllAnswer;
import com.sos.joc.xmleditor.resource.IDeleteAllResource;
import com.sos.schema.JsonValidator;

import jersey.repackaged.com.google.common.base.Joiner;

@Path(JocXmlEditor.APPLICATION_PATH)
public class DeleteAllResourceImpl extends JOCResourceImpl implements IDeleteAllResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteAllResourceImpl.class);
    private static final boolean isTraceEnabled = LOGGER.isTraceEnabled();

    @Override
    public JOCDefaultResponse process(final String accessToken, final byte[] filterBytes) {
        try {
            JsonValidator.validateFailFast(filterBytes, DeleteAll.class);
            DeleteAll in = Globals.objectMapper.readValue(filterBytes, DeleteAll.class);

            checkRequiredParameters(in);

            JOCDefaultResponse response = checkPermissions(accessToken, in);
            if (response == null) {
                ObjectType type = in.getObjectTypes().get(0);
                if (type.equals(ObjectType.OTHER)) {
                    deleteOtherItems(in.getJobschedulerId());
                    response = JOCDefaultResponse.responseStatus200(getSuccess());
                } else {
                    throw new JocException(new JocError(JocXmlEditor.ERROR_CODE_UNSUPPORTED_OBJECT_TYPE, String.format(
                            "[%s][%s]unsupported object type(s) for delete all", in.getJobschedulerId(), Joiner.on(",").join(in.getObjectTypes()))));
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

    private void checkRequiredParameters(final DeleteAll in) throws Exception {
        checkRequiredParameter("jobschedulerId", in.getJobschedulerId());
        JocXmlEditor.checkRequiredParameter("objectTypes", in.getObjectTypes());
    }

    private JOCDefaultResponse checkPermissions(final String accessToken, final DeleteAll in) throws Exception {
        SOSPermissionJocCockpit permissions = getPermissonsJocCockpit(in.getJobschedulerId(), accessToken);
        boolean permission = permissions.getJobschedulerMaster().getAdministration().getConfigurations().isDelete();
        JOCDefaultResponse response = init(IMPL_PATH, in, accessToken, in.getJobschedulerId(), permission);
        if (permission && response == null) {
            if (versionIsOlderThan(JocXmlEditor.AVAILABILITY_STARTING_WITH)) {
                throw new JobSchedulerBadRequestException(JocXmlEditor.MESSAGE_UNSUPPORTED_WEB_SERVICE);
            }
        }
        return response;
    }

    private DeleteAllAnswer getSuccess() throws Exception {
        DeleteAllAnswer answer = new DeleteAllAnswer();
        answer.setDeleted(new Date());
        return answer;
    }

    private boolean deleteOtherItems(String jobschedulerId) throws Exception {
        SOSHibernateSession session = null;
        try {
            session = Globals.createSosHibernateStatelessConnection(IMPL_PATH);
            DbLayerXmlEditor dbLayer = new DbLayerXmlEditor(session);

            session.beginTransaction();
            int deleted = dbLayer.deleteOtherObjects(jobschedulerId);
            session.commit();
            if (isTraceEnabled) {
                LOGGER.trace(String.format("deleted=%s", deleted));
            }
            return Math.abs(deleted) > 0;
        } catch (Throwable e) {
            Globals.rollback(session);
            throw e;
        } finally {
            Globals.disconnect(session);
        }
    }

}
