package com.sos.joc.xmleditor.impl;

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
import com.sos.joc.classes.xmleditor.exceptions.XsdValidatorException;
import com.sos.joc.classes.xmleditor.validator.XsdValidator;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.xmleditor.apply.ApplyConfiguration;
import com.sos.joc.model.xmleditor.apply.ApplyConfigurationAnswer;
import com.sos.joc.model.xmleditor.common.AnswerMessage;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.validate.ErrorMessage;
import com.sos.joc.xmleditor.common.Xml2JsonConverter;
import com.sos.joc.xmleditor.resource.IApplyResource;
import com.sos.schema.JsonValidator;

import sos.util.SOSString;

@Path(JocXmlEditor.APPLICATION_PATH)
public class ApplyResourceImpl extends JOCResourceImpl implements IApplyResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplyResourceImpl.class);

    @Override
    public JOCDefaultResponse process(final String accessToken, final byte[] filterBytes) {
        SOSHibernateSession session = null;
        try {
            JsonValidator.validateFailFast(filterBytes, ApplyConfiguration.class);
            ApplyConfiguration in = Globals.objectMapper.readValue(filterBytes, ApplyConfiguration.class);

            // TODO check folder permissions
            checkRequiredParameters(in);

            JOCDefaultResponse response = checkPermissions(accessToken, in);
            if (response == null) {
                // step 1 - check for vulnerabilities and validate
                response = check(in, false);
                if (response != null) {
                    return response;
                }

                // step 2 - xml2json
                String json = convertXml2Json(in);

                // step 3 - store in the database TODO same code as in store
                session = Globals.createSosHibernateStatelessConnection(IMPL_PATH);
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
                response = JOCDefaultResponse.responseStatus200(getSuccess(in, item, json));
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

    private JOCDefaultResponse check(ApplyConfiguration in, boolean validate) throws Exception {
        if (validate) {
            java.nio.file.Path schema = null;
            if (in.getObjectType().equals(ObjectType.OTHER)) {
                schema = JocXmlEditor.getOthersSchema(in.getSchemaIdentifier(), false);
            } else {
                schema = JocXmlEditor.getStandardAbsoluteSchemaLocation(in.getObjectType());
            }
            // check for vulnerabilities and validate
            XsdValidator validator = new XsdValidator(schema);
            try {
                validator.validate(in.getConfiguration());
            } catch (XsdValidatorException e) {
                LOGGER.error(String.format("[%s]%s", validator.getSchema(), e.toString()), e);
                return JOCDefaultResponse.responseStatus200(getError(e));
            }
        } else {
            // check for vulnerabilities
            JocXmlEditor.parseXml(in.getConfiguration());
        }
        return null;
    }

    private ApplyConfigurationAnswer getSuccess(ApplyConfiguration in, DBItemXmlEditorObject item, String json) throws Exception {
        ApplyConfigurationAnswer answer = new ApplyConfigurationAnswer();
        answer.setId(item.getId().intValue());
        answer.setName(item.getName());
        if (in.getObjectType().equals(ObjectType.OTHER)) {
            answer.setSchemaIdentifier(JocXmlEditor.getOthersSchemaIdentifier(item.getSchemaLocation()));
        } else {
            answer.setSchemaIdentifier(JocXmlEditor.getStandardSchemaIdentifier(in.getObjectType()));
        }
        answer.setConfiguration(item.getConfigurationDraft());
        answer.setConfigurationJson(json);
        answer.setRecreateJson(true);
        answer.setModified(item.getModified());
        if (!in.getObjectType().equals(ObjectType.OTHER)) {
            answer.setMessage(new AnswerMessage());
            if (item.getDeployed() == null) {
                answer.getMessage().setCode(JocXmlEditor.MESSAGE_CODE_LIVE_NOT_EXIST);
                answer.getMessage().setMessage(JocXmlEditor.MESSAGE_LIVE_NOT_EXIST);
            } else {
                answer.getMessage().setCode(JocXmlEditor.MESSAGE_CODE_DRAFT_IS_NEWER);
                answer.getMessage().setMessage(JocXmlEditor.MESSAGE_DRAFT_IS_NEWER);
            }
        }
        return answer;
    }

    private String convertXml2Json(ApplyConfiguration in) throws Exception {
        java.nio.file.Path schema = null;
        if (in.getObjectType().equals(ObjectType.OTHER)) {
            schema = JocXmlEditor.getOthersSchema(in.getSchemaIdentifier(), false);
        } else {
            schema = JocXmlEditor.getStandardAbsoluteSchemaLocation(in.getObjectType());
        }

        Xml2JsonConverter converter = new Xml2JsonConverter();
        return converter.convert(in.getObjectType(), schema, in.getConfiguration());
    }

    public static ApplyConfigurationAnswer getError(XsdValidatorException e) {
        ApplyConfigurationAnswer answer = new ApplyConfigurationAnswer();
        answer.setValidationError(getErrorMessage(e));
        return answer;
    }

    private static ErrorMessage getErrorMessage(XsdValidatorException e) {
        ErrorMessage m = new ErrorMessage();
        m.setCode(JocXmlEditor.ERROR_CODE_VALIDATION_ERROR);
        try {
            m.setMessage(String.format("'%s', line=%s, column=%s, %s", e.getElementName(), e.getLineNumber(), e.getColumnNumber(), e.getCause()
                    .getMessage()));
        } catch (Throwable ex) {
            m.setMessage(ex.toString());
        }
        m.setLine(e.getLineNumber());
        m.setColumn(e.getColumnNumber());
        m.setElementName(e.getElementName());
        m.setElementPosition(e.getElementPosition());
        return m;
    }

    private void checkRequiredParameters(final ApplyConfiguration in) throws Exception {
        checkRequiredParameter("jobschedulerId", in.getJobschedulerId());
        JocXmlEditor.checkRequiredParameter("objectType", in.getObjectType());
        if (in.getObjectType().equals(ObjectType.OTHER)) {
            checkRequiredParameter("id", in.getId());
            checkRequiredParameter("name", in.getName());
            checkRequiredParameter("schemaIdentifier", in.getSchemaIdentifier());
        } else {
            checkRequiredParameter("configuration", in.getConfiguration());
        }

    }

    private JOCDefaultResponse checkPermissions(final String accessToken, final ApplyConfiguration in) throws Exception {
        SOSPermissionJocCockpit permissions = getPermissonsJocCockpit(in.getJobschedulerId(), accessToken);
        boolean permission = permissions.getJobschedulerMaster().getAdministration().getConfigurations().isEdit();
        JOCDefaultResponse response = init(IMPL_PATH, in, accessToken, in.getJobschedulerId(), permission);
        if (permission && response == null) {
            if (versionIsOlderThan(JocXmlEditor.AVAILABILITY_STARTING_WITH)) {
                throw new JobSchedulerBadRequestException(JocXmlEditor.MESSAGE_UNSUPPORTED_WEB_SERVICE);
            }
        }
        return response;
    }

    private DBItemXmlEditorObject getOthersObject(DbLayerXmlEditor dbLayer, ApplyConfiguration in, String name) throws Exception {
        // TODO currently the same code as in store
        DBItemXmlEditorObject item = null;
        if (in.getId() != null && in.getId() > 0) {
            item = dbLayer.getObject(in.getId().longValue());
            if (item != null && !item.getObjectType().equals(ObjectType.OTHER.name())) {
                item = null;// dbLayer.getObject(in.getJobschedulerId(), ObjectType.OTHER.name(), name);
            }
        }
        return item;
    }

    private DBItemXmlEditorObject getStandardObject(DbLayerXmlEditor dbLayer, ApplyConfiguration in) throws Exception {
        return dbLayer.getObject(in.getJobschedulerId(), in.getObjectType().name(), JocXmlEditor.getConfigurationName(in.getObjectType(), in
                .getName()));
    }

    private DBItemXmlEditorObject create(SOSHibernateSession session, ApplyConfiguration in, String name) throws Exception {
        DBItemXmlEditorObject item = new DBItemXmlEditorObject();
        item.setSchedulerId(in.getJobschedulerId());
        item.setObjectType(in.getObjectType().name());
        item.setName(name.trim());
        item.setConfigurationDraft(in.getConfiguration());
        item.setConfigurationDraftJson(null);
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

    private DBItemXmlEditorObject update(SOSHibernateSession session, ApplyConfiguration in, DBItemXmlEditorObject item, String name)
            throws Exception {
        item.setName(name.trim());
        item.setConfigurationDraft(SOSString.isEmpty(in.getConfiguration()) ? null : in.getConfiguration());
        item.setConfigurationDraftJson(null);
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

}
