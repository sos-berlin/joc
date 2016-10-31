package com.sos.joc.processClasses.impl;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationUtils;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.processClass.ProcessClassConfigurationFilter;
import com.sos.joc.processClasses.resource.IProcessClassResourceConfiguration;
import com.sos.scheduler.model.commands.JSCmdShowState;

@Path("process_class")
public class ProcessClassResourceConfigurationImpl extends JOCResourceImpl implements IProcessClassResourceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessClassResourceConfigurationImpl.class);
    private static final String API_CALL = "./process_class/configuration";

    @Override
    public JOCDefaultResponse postProcessClassConfiguration(String accessToken, ProcessClassConfigurationFilter processClassConfigurationFilter)
            throws Exception {

        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, processClassConfigurationFilter.getJobschedulerId(), getPermissons(accessToken)
                    .getProcessClass().getView().isConfiguration());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            Configuration200 entity = new Configuration200();
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            if (checkRequiredParameter("processClass", processClassConfigurationFilter.getProcessClass())) {
                boolean responseInHtml = processClassConfigurationFilter.getMime() == ConfigurationMime.HTML;
                String xPath = String.format("/spooler/answer//process_classes/process_class[@path='%s']", normalizePath(
                        processClassConfigurationFilter.getProcessClass()));
                entity = ConfigurationUtils.getConfigurationSchema(jocXmlCommand, createProcessClassConfigurationPostCommand(), xPath,
                        "process_class", responseInHtml, accessToken);
            }
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, processClassConfigurationFilter));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, processClassConfigurationFilter));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }

    private String createProcessClassConfigurationPostCommand() {
        JSCmdShowState showProcessClasss = new JSCmdShowState(Globals.schedulerObjectFactory);
        showProcessClasss.setSubsystems("folder schedule");
        showProcessClasss.setWhat("folders source");
        return Globals.schedulerObjectFactory.toXMLString(showProcessClasss);
    }

}