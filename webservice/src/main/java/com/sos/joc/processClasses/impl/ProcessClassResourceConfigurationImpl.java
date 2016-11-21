package com.sos.joc.processClasses.impl;

import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.processClass.ProcessClassConfigurationFilter;
import com.sos.joc.processClasses.resource.IProcessClassResourceConfiguration;

@Path("process_class")
public class ProcessClassResourceConfigurationImpl extends JOCResourceImpl implements IProcessClassResourceConfiguration {

    private static final String API_CALL = "./process_class/configuration";

    @Override
    public JOCDefaultResponse postProcessClassConfiguration(String accessToken, ProcessClassConfigurationFilter processClassConfigurationFilter)
            throws Exception {

        try {
            initLogging(API_CALL, processClassConfigurationFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, processClassConfigurationFilter.getJobschedulerId(), getPermissons(accessToken)
                    .getProcessClass().getView().isConfiguration());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            Configuration200 entity = new Configuration200();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (checkRequiredParameter("processClass", processClassConfigurationFilter.getProcessClass())) {
                boolean responseInHtml = processClassConfigurationFilter.getMime() == ConfigurationMime.HTML;
                String processClassPath = normalizePath(processClassConfigurationFilter.getProcessClass());
                String processClassParent = getParent(processClassPath);
                String command = jocXmlCommand.getShowStateCommand("folder process_class", "folders no_subfolders source", processClassParent);
                if ("/(default)".equals(processClassPath)) {
                    String xPath = "/spooler/answer//process_classes/process_class[@path='']";
                    entity = ConfigurationUtils.getConfigurationSchemaOfDefaultProcessClass(jocXmlCommand, command, xPath, responseInHtml,
                            accessToken);
                } else {
                    String xPath = String.format("/spooler/answer//process_classes/process_class[@path='%s']", processClassPath);
                    entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, command, xPath, "process_class", responseInHtml, accessToken);
                }
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
}