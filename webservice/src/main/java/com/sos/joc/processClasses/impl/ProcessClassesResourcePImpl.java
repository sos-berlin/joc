package com.sos.joc.processClasses.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.processClasses.resource.IProcessClassesResourceP;
import com.sos.joc.model.processClass.ProcessClassP;
import com.sos.joc.model.processClass.ProcessClassesFilter;
import com.sos.joc.model.processClass.ProcessClassesP;

@Path("processClasses")
public class ProcessClassesResourcePImpl extends JOCResourceImpl implements IProcessClassesResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessClassesResourcePImpl.class);

    @Override
    public JOCDefaultResponse postProcessClassesP(String accessToken, ProcessClassesFilter processClassFilter) throws Exception {

        LOGGER.debug("init processClasses");

        try {
            JOCDefaultResponse jocDefaultResponse = init(processClassFilter.getJobschedulerId(), getPermissons(accessToken).getLock().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            ProcessClassesP entity = new ProcessClassesP();
            entity.setDeliveryDate(new Date());
            List<ProcessClassP> listOfProcessClasses = new ArrayList<ProcessClassP>();
            ProcessClassP processClassP = new ProcessClassP();
            processClassP.setMaxProcesses(-1);
            processClassP.setName("myName");
            processClassP.setPath("myPath");
            processClassP.setSurveyDate(new Date());
            listOfProcessClasses.add(processClassP);
            entity.setProcessClasses(listOfProcessClasses);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
    }

}