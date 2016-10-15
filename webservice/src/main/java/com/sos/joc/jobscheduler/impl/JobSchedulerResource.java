package com.sos.joc.jobscheduler.impl;

import java.util.Date;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.JobSchedulerV200;
import com.sos.joc.model.jobscheduler.JobSchedulerState;
import com.sos.joc.model.jobscheduler.JobSchedulerV;
import com.sos.scheduler.model.commands.JSCmdShowState;

public class JobSchedulerResource extends JOCResourceImpl {
    private static final String TERMINATING = "terminating";
    private static final String WAITING_FOR_ACTIVATION = "waiting_for_activation";
    private static final String UNREACHABLE = "unreachable";
    private static final String DEAD = "dead";
    private static final String WAITING_FOR_DATABASE = "waiting_for_database";
    private static final String PAUSED = "paused";
    private static final String RUNNING = "running";
    private static final String JOBSCHEDULER_STATE = "state";
    private static final String XPATH_SPOOLER_STATE = "//spooler/answer/state";
    private static final String SPOOLER_RUNNING_SINCE = "spooler_running_since";
    private static final String TCP_PORT = "tcp_port";
    private static final String ID = "id";
    private static final String HOST = "host";
    private static final String FOLDER = "folder";
    private static final String DOES_NOT_EXIST = "/does/not/exist";
    private static final String FOLDERS_NO_SUBFOLDERS = "folders no_subfolders";
    private JobSchedulerId jobSchedulerId;

    public void setJobSchedulerFilterSchema(JobSchedulerId jobSchedulerId) {
        this.jobSchedulerId = jobSchedulerId;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedulerResource.class);
    private String accessToken;

    public JobSchedulerResource(String accessToken, JobSchedulerId jobSchedulerId) {
        super();
        this.jobSchedulerId = jobSchedulerId;
        jobschedulerUser = new JobSchedulerUser(accessToken);
        this.accessToken = accessToken;
    }

    
    public JOCDefaultResponse postJobscheduler() {
        LOGGER.debug("init jobscheduler");
        try {
            JOCDefaultResponse jocDefaultResponse = init(jobSchedulerId.getJobschedulerId(),getPermissons(accessToken).getJobschedulerMaster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JobSchedulerV200 entity = new JobSchedulerV200();
            entity.setDeliveryDate(new Date());
            
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getCommandUrl());
            
            JSCmdShowState jsCmdShowState = Globals.schedulerObjectFactory.createShowState();
            jsCmdShowState.setWhat(FOLDERS_NO_SUBFOLDERS);;
            jsCmdShowState.setPath(DOES_NOT_EXIST);
            jsCmdShowState.setSubsystems(FOLDER);
            String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdShowState);
            jocXmlCommand.excutePost(xml);

            JobSchedulerV jobscheduler = new JobSchedulerV();
            jobscheduler.setSurveyDate(jocXmlCommand.getSurveyDate());

            jocXmlCommand.executeXPath(XPATH_SPOOLER_STATE);

            jobscheduler.setHost(jocXmlCommand.getAttribute(HOST));

            jobscheduler.setJobschedulerId(jocXmlCommand.getAttribute(ID));
            jobscheduler.setPort(jocXmlCommand.getAttributeAsInteger(TCP_PORT));
            jobscheduler.setStartedAt(jocXmlCommand.getAttributeAsDate(SPOOLER_RUNNING_SINCE));
            String jobschedulerState = jocXmlCommand.getAttribute(JOBSCHEDULER_STATE);

            JobSchedulerState state = new JobSchedulerState();
           
            state.setSeverity(getSeverityFromState(jobschedulerState));
            state.set_text(getText(jobschedulerState.toUpperCase()));
            jobscheduler.setState(state);
            
            entity.setJobscheduler(jobscheduler);
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getMessage());
        }
    }
    
   
    private Integer getSeverityFromState(String state){
        HashMap<String,Integer> h = new HashMap<String, Integer>();
        
        if (state==null){
            return null;
        }
        
        state = state.toLowerCase();
        h.put(RUNNING, 0);
        h.put(PAUSED, 1);
        h.put(WAITING_FOR_DATABASE, 2);
        h.put(DEAD, 2);
        h.put(UNREACHABLE, 2);
        h.put(WAITING_FOR_ACTIVATION, 3);
        h.put(TERMINATING, 3);
        
        Integer severity = h.get(state);
        if (severity != null){
            return severity;
        }else{
            return -1;
        }
    }

}
