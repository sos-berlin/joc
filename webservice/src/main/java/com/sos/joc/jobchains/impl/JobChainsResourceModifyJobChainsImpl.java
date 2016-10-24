package com.sos.joc.jobchains.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.jobscheduler.BulkError;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.jobchains.resource.IJobChainsResourceModifyJobChains;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.jobChain.ModifyJobChain;
import com.sos.joc.model.jobChain.ModifyJobChains;
import com.sos.scheduler.model.commands.JSCmdJobChainModify;
import com.sos.scheduler.model.commands.JSCmdJobChainModify.enu4State;

@Path("job_chains")
public class JobChainsResourceModifyJobChainsImpl extends JOCResourceImpl implements IJobChainsResourceModifyJobChains {
    private static final String UNSTOP = "unstop";
    private static final String STOP = "stop";
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainsResourceModifyJobChainsImpl.class);
    private List<Err419> listOfErrors = new ArrayList<Err419>();
    private static String API_CALL = "./job_chains/";
    
    @Override
    public JOCDefaultResponse postJobChainsStop(String accessToken, ModifyJobChains modifyJobChains) {
        API_CALL += STOP;
        LOGGER.debug(API_CALL);
        try {
            return postJobChainsCommand(STOP, accessToken, getPermissons(accessToken).getJobChain().isStop(), modifyJobChains);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, modifyJobChains));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyJobChains));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    @Override
    public JOCDefaultResponse postJobChainsUnStop(String accessToken, ModifyJobChains modifyJobChains) {
        API_CALL += UNSTOP;
        LOGGER.debug(API_CALL);
        try {
            return postJobChainsCommand(UNSTOP, accessToken, getPermissons(accessToken).getJobChain().isUnstop(), modifyJobChains);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, modifyJobChains));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyJobChains));
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
    
    private JOCDefaultResponse postJobChainsCommand(String command, String accessToken, boolean permission, ModifyJobChains modifyJobChains) throws Exception {
        JOCDefaultResponse jocDefaultResponse = init(accessToken, modifyJobChains.getJobschedulerId(), permission);
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        if (modifyJobChains.getJobChains().size() == 0) {
            throw new JocMissingRequiredParameterException("undefined 'jobChains'");
        }
        Date surveyDate = new Date();
        for (ModifyJobChain jobChain : modifyJobChains.getJobChains()) {
            surveyDate = executeModifyJobChainCommand(jobChain, command);
        }
        if (listOfErrors.size() > 0) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, modifyJobChains));
            return JOCDefaultResponse.responseStatus419(listOfErrors, err);
        }
        return JOCDefaultResponse.responseStatusJSOk(surveyDate);
    }

    private Date executeModifyJobChainCommand(ModifyJobChain jobChain, String cmd) {
        try {
            checkRequiredParameter("jobChain", jobChain.getJobChain());
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            JSCmdJobChainModify jsCmdJobChainModify = new JSCmdJobChainModify(Globals.schedulerObjectFactory);
            jsCmdJobChainModify.setJobChain(jobChain.getJobChain());
            switch(cmd) {
            case STOP:
                jsCmdJobChainModify.setState(enu4State.STOPPED); 
                break;
            case UNSTOP:
                jsCmdJobChainModify.setState(enu4State.RUNNING); 
                break;
            }
            String xml = jsCmdJobChainModify.toXMLString();
            jocXmlCommand.executePostWithThrowBadRequest(xml);
            
            return jocXmlCommand.getSurveyDate();
        } catch (JocException e) {
            listOfErrors.add(new BulkError().get(e, jobChain));
        } catch (Exception e) {
            listOfErrors.add(new BulkError().get(e, jobChain));
        }
        return null;
    }
}
