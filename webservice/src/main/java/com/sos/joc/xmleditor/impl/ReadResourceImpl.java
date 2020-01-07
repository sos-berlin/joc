package com.sos.joc.xmleditor.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemXmlEditorObject;
import com.sos.jitl.xmleditor.common.JobSchedulerXmlEditor;
import com.sos.jitl.xmleditor.db.DbLayerXmlEditor;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.xmleditor.JocXmlEditor;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.xmleditor.common.ObjectType;
import com.sos.joc.model.xmleditor.read.ReadConfiguration;
import com.sos.joc.model.xmleditor.read.other.AnswerConfiguration;
import com.sos.joc.model.xmleditor.read.other.ReadOtherConfigurationAnswer;
import com.sos.joc.model.xmleditor.read.standard.ReadStandardConfigurationAnswer;
import com.sos.joc.xmleditor.common.standard.ReadConfigurationHandler;
import com.sos.joc.xmleditor.resource.IReadResource;

import sos.util.SOSString;

@Path(JocXmlEditor.APPLICATION_PATH)
public class ReadResourceImpl extends JOCResourceImpl implements IReadResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadResourceImpl.class);
    private static final boolean isTraceEnabled = LOGGER.isTraceEnabled();

    @Override
    public JOCDefaultResponse read(final String accessToken, final ReadConfiguration in) {
        try {
            checkRequiredParameters(in);

            JOCDefaultResponse response = checkPermissions(accessToken, in);
            if (response == null) {
                JocXmlEditor.setRealPath();

                if (in.getObjectType().equals(ObjectType.OTHER)) {
                    ReadOtherConfigurationAnswer answer = handleOtherConfigurations(in);
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

    private void checkRequiredParameters(final ReadConfiguration in) throws Exception {
        checkRequiredParameter("jobschedulerId", in.getJobschedulerId());
        JocXmlEditor.checkRequiredParameter("objectType", in.getObjectType());
    }

    private JOCDefaultResponse checkPermissions(final String accessToken, final ReadConfiguration in) throws Exception {
        SOSPermissionJocCockpit permissions = getPermissonsJocCockpit(in.getJobschedulerId(), accessToken);
        boolean permission = permissions.getJobschedulerMaster().getAdministration().getConfigurations().isView();
        JOCDefaultResponse response = init(IMPL_PATH, in, accessToken, in.getJobschedulerId(), permission);
        if (response == null) {
            if (versionIsOlderThan(JocXmlEditor.AVAILABILITY_STARTING_WITH)) {
                throw new JobSchedulerBadRequestException(JocXmlEditor.MESSAGE_UNSUPPORTED_WEB_SERVICE);
            }
        }
        return response;
    }

    private ReadStandardConfigurationAnswer handleStandardConfiguration(ReadConfiguration in) throws Exception {
        DBItemXmlEditorObject item = getItem(in.getJobschedulerId(), in.getObjectType().name(), JocXmlEditor.getConfigurationName(in
                .getObjectType()));

        ReadConfigurationHandler handler = new ReadConfigurationHandler(this, in.getObjectType());
        handler.readCurrent(item, in.getJobschedulerId(), (in.getForceLive() != null && in.getForceLive()));
        return handler.getAnswer();
    }

    private ReadOtherConfigurationAnswer handleOtherConfigurations(ReadConfiguration in) throws Exception {

        ReadOtherConfigurationAnswer answer = new ReadOtherConfigurationAnswer();

        if (in.getId() == null || in.getId() <= 0) {
            ArrayList<String> schemas = new ArrayList<String>();
            // https locations
            schemas.add(JobSchedulerXmlEditor.SCHEMA_URI_NOTIFICATION);
            schemas.add(JobSchedulerXmlEditor.SCHEMA_URI_YADE);

            List<Map<String, Object>> items = getOtherProperties(in, "id,name,schemaLocation");
            if (items != null && items.size() > 0) {
                ArrayList<AnswerConfiguration> configurations = new ArrayList<AnswerConfiguration>();
                for (int i = 0; i < items.size(); i++) {
                    Map<String, Object> item = items.get(i);
                    AnswerConfiguration configuration = new AnswerConfiguration();
                    configuration.setId(Integer.parseInt(item.get("0").toString()));
                    configuration.setName(item.get("1").toString());
                    configuration.setSchemaIdentifier(item.get("2").toString());// fileName or http(s) location

                    if (!schemas.contains(configuration.getSchemaIdentifier())) {
                        schemas.add(configuration.getSchemaIdentifier());
                    }

                    configurations.add(configuration);
                }
                answer.setConfigurations(configurations);
            }

            List<java.nio.file.Path> files = JocXmlEditor.getOthersAbsoluteSchemaLocations();
            if (files != null && files.size() > 0) {
                for (int i = 0; i < files.size(); i++) {
                    // fileName
                    String schema = JocXmlEditor.getOthersSchemaIdentifier(files.get(i));
                    if (!schemas.contains(schema)) {
                        schemas.add(schema);
                    }
                }
            }

            if (schemas.size() > 0) {
                Collections.sort(schemas);
                answer.setSchemas(schemas);
            }

        } else {
            DBItemXmlEditorObject item = getItem(in.getId());
            answer.setConfiguration(new AnswerConfiguration());
            if (item == null) {
                throw new JocException(new JocError(JocXmlEditor.CODE_NO_CONFIGURATION_EXIST, String.format("[%s][%s][%s]no configuration found", in
                        .getJobschedulerId(), in.getObjectType().name(), in.getId())));
            } else {
                answer.getConfiguration().setId(item.getId().intValue());
                answer.getConfiguration().setName(item.getName());
                answer.getConfiguration().setSchema(JocXmlEditor.readOthersSchemaFile(item.getSchemaLocation()));
                answer.getConfiguration().setSchemaIdentifier(JocXmlEditor.getOthersSchemaIdentifier(item.getSchemaLocation()));
                answer.getConfiguration().setConfiguration(item.getConfigurationDraft());
                answer.getConfiguration().setConfigurationJson(item.getConfigurationDraftJson());
                answer.getConfiguration().setModified(item.getModified());
            }
        }
        return answer;
    }

    private DBItemXmlEditorObject getItem(Integer id) throws Exception {
        SOSHibernateSession session = null;
        try {
            session = Globals.createSosHibernateStatelessConnection(IMPL_PATH);
            DbLayerXmlEditor dbLayer = new DbLayerXmlEditor(session);

            session.beginTransaction();
            DBItemXmlEditorObject item = dbLayer.getObject(id.longValue());
            session.commit();

            if (isTraceEnabled) {
                LOGGER.trace(String.format("[%s]%s", id, SOSString.toString(item, Arrays.asList("configuration"))));
            }
            return item;
        } catch (Throwable e) {
            Globals.rollback(session);
            throw e;
        } finally {
            Globals.disconnect(session);
        }
    }

    private DBItemXmlEditorObject getItem(String schedulerId, String objectType, String name) throws Exception {
        SOSHibernateSession session = null;
        try {
            session = Globals.createSosHibernateStatelessConnection(IMPL_PATH);
            DbLayerXmlEditor dbLayer = new DbLayerXmlEditor(session);

            session.beginTransaction();
            DBItemXmlEditorObject item = dbLayer.getObject(schedulerId, objectType, name);
            session.commit();
            if (isTraceEnabled) {
                LOGGER.trace(String.format("[%s][%s][%s]%s", schedulerId, objectType, name, SOSString.toString(item, Arrays.asList(
                        "configuration"))));
            }
            return item;
        } catch (Throwable e) {
            Globals.rollback(session);
            throw e;
        } finally {
            Globals.disconnect(session);
        }
    }

    private List<Map<String, Object>> getOtherProperties(ReadConfiguration in, String properties) throws Exception {
        SOSHibernateSession session = null;
        try {
            session = Globals.createSosHibernateStatelessConnection(IMPL_PATH);
            DbLayerXmlEditor dbLayer = new DbLayerXmlEditor(session);

            session.beginTransaction();
            List<Map<String, Object>> items = dbLayer.getObjectProperties(in.getJobschedulerId(), ObjectType.OTHER.name(), properties);
            session.commit();
            return items;
        } catch (Throwable e) {
            Globals.rollback(session);
            throw e;
        } finally {
            Globals.disconnect(session);
        }
    }
}
