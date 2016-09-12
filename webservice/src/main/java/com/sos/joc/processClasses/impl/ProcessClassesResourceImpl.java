package com.sos.joc.processClasses.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.model.processClass.ProcessClassFilterSchema;
import com.sos.joc.model.processClass.ProcessClassVSchema;
import com.sos.joc.model.processClass.ProcessClassesVSchema;
import com.sos.joc.model.processClass.Processes;
import com.sos.joc.processClasses.resource.IProcessClassesResource;

@Path("processClasses")
public class ProcessClassesResourceImpl extends JOCResourceImpl implements IProcessClassesResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessClassesResourceImpl.class);
    
    @Override
    public JOCDefaultResponse postProcessClasses(String accessToken, ProcessClassFilterSchema processClassFilterSchema) throws Exception {
        JOCDefaultResponse jocDefaultResponse = init(processClassFilterSchema.getJobschedulerId(), getPermissons(accessToken).getLock().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            LOGGER.debug("init processClasses");
 
            ProcessClassesVSchema entity = new ProcessClassesVSchema();
            entity.setDeliveryDate(new Date());
            List<ProcessClassVSchema> listOfProcessClasses = new ArrayList<ProcessClassVSchema>();
            ProcessClassVSchema processClassVSchema = new ProcessClassVSchema();
            processClassVSchema.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus());
            processClassVSchema.setName("myName");
            processClassVSchema.setNumOfProcesses(-1);
            processClassVSchema.setPath("myPath");

            Processes processes = new Processes();
            processes.setJob("myJob");
            processes.setPid(-1);
            processes.setRunningSince(new Date());
            processes.setTaskId(-1);
            
            processClassVSchema.setProcesses(processes);
            processClassVSchema.setSurveyDate(new Date());
            listOfProcessClasses.add(processClassVSchema);
            entity.setProcessClasses(listOfProcessClasses);
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
    }

}