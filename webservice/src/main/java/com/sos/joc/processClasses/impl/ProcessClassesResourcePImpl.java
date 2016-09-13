package com.sos.joc.processClasses.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.processClasses.resource.IProcessClassesResourceP;
import com.sos.joc.model.processClass.ProcessClassPSchema;
import com.sos.joc.model.processClass.ProcessClassesFilterSchema;
import com.sos.joc.model.processClass.ProcessClassesPSchema;

@Path("processClasses")
public class ProcessClassesResourcePImpl extends JOCResourceImpl implements IProcessClassesResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessClassesResourcePImpl.class);

    @Override
    public JOCDefaultResponse postProcessClassesP(String accessToken, ProcessClassesFilterSchema processClassFilterSchema) throws Exception {
        JOCDefaultResponse jocDefaultResponse = init(processClassFilterSchema.getJobschedulerId(), getPermissons(accessToken).getLock().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }

        try {
            LOGGER.debug("init processClasses");

            ProcessClassesPSchema entity = new ProcessClassesPSchema();
            entity.setDeliveryDate(new Date());
            List<ProcessClassPSchema> listOfProcessClasses = new ArrayList<ProcessClassPSchema>();
            ProcessClassPSchema processClassPSchema  = new ProcessClassPSchema();
            processClassPSchema.setMaxProcesses(-1);
            processClassPSchema.setName("myName");
            processClassPSchema.setPath("myPath");
            processClassPSchema.setSurveyDate(new Date());
            listOfProcessClasses.add(processClassPSchema);
            entity.setProcessClasses(listOfProcessClasses);
           
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
    }

}