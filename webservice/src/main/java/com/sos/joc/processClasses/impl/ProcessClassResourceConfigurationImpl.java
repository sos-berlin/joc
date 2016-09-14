package com.sos.joc.processClasses.impl;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.ConfigurationSchema;
import com.sos.joc.model.processClass.ProcessClassConfigurationFilterSchema;
import com.sos.joc.processClasses.resource.IProcessClassResourceConfiguration;
import com.sos.scheduler.model.commands.JSCmdShowState;

@Path("process_class")
public class ProcessClassResourceConfigurationImpl extends JOCResourceImpl implements IProcessClassResourceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessClassResourceConfigurationImpl.class);

    @Override
    public JOCDefaultResponse postProcessClassConfiguration(String accessToken, ProcessClassConfigurationFilterSchema processClassConfigurationFilterSchema) throws Exception {

        LOGGER.debug("init process_class/configuration");
        JOCDefaultResponse jocDefaultResponse = init(processClassConfigurationFilterSchema.getJobschedulerId(), getPermissons(accessToken).getProcessClass().getView().isConfiguration());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            ConfigurationSchema entity = new ConfigurationSchema();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (jocXmlCommand.checkRequiredParameter("schedule", processClassConfigurationFilterSchema.getProcessClass())) {
                boolean responseInHtml = processClassConfigurationFilterSchema.getMime() == ProcessClassConfigurationFilterSchema.Mime.HTML;
                String xPath = "/spooler/answer//process_classes/process_class[@path='"+("/"+processClassConfigurationFilterSchema.getProcessClass()).replaceAll("//+", "/")+"']"; 
                entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, createProcessClassConfigurationPostCommand(), xPath, "process_class", responseInHtml);
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
    
    private String createProcessClassConfigurationPostCommand() {
        JSCmdShowState showProcessClasss = new JSCmdShowState(Globals.schedulerObjectFactory);
        showProcessClasss.setSubsystems("folder schedule");
        showProcessClasss.setWhat("folders source");
        return Globals.schedulerObjectFactory.toXMLString(showProcessClasss);
    }

}