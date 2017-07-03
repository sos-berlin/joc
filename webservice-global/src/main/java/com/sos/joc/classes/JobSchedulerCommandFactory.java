package com.sos.joc.classes;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.sos.auth.rest.SOSShiroFolderPermissions;
import com.sos.auth.rest.permission.model.SOSPermissionCommands;
import com.sos.joc.model.commands.JobChainModify;
import com.sos.joc.model.commands.JobChainNodeModify;
import com.sos.joc.model.commands.JobWhy;
import com.sos.joc.model.commands.KillTask;
import com.sos.joc.model.commands.LockRemove;
import com.sos.joc.model.commands.ModifyHotFolder;
import com.sos.joc.model.commands.ModifyJob;
import com.sos.joc.model.commands.ModifyOrder;
import com.sos.joc.model.commands.ModifySpooler;
import com.sos.joc.model.commands.ObjectFactory;
import com.sos.joc.model.commands.Order;
import com.sos.joc.model.commands.ParamGet;
import com.sos.joc.model.commands.Params;
import com.sos.joc.model.commands.ParamsGet;
import com.sos.joc.model.commands.ProcessClass;
import com.sos.joc.model.commands.ProcessClassRemove;
import com.sos.joc.model.commands.RemoveJobChain;
import com.sos.joc.model.commands.RemoveOrder;
import com.sos.joc.model.commands.ScheduleRemove;
import com.sos.joc.model.commands.SchedulerLogLogCategoriesReset;
import com.sos.joc.model.commands.SchedulerLogLogCategoriesSet;
import com.sos.joc.model.commands.SchedulerLogLogCategoriesShow;
import com.sos.joc.model.commands.ShowCalendar;
import com.sos.joc.model.commands.ShowHistory;
import com.sos.joc.model.commands.ShowJob;
import com.sos.joc.model.commands.ShowJobChain;
import com.sos.joc.model.commands.ShowJobChains;
import com.sos.joc.model.commands.ShowJobs;
import com.sos.joc.model.commands.ShowOrder;
import com.sos.joc.model.commands.ShowState;
import com.sos.joc.model.commands.ShowTask;
import com.sos.joc.model.commands.StartJob;
import com.sos.joc.model.commands.Terminate;

public class JobSchedulerCommandFactory {
    private ObjectFactory objectFactory;
    private JAXBElement<?> jaxbElement;
    private Object command;

    public JobSchedulerCommandFactory() {
        super();
        objectFactory = new ObjectFactory();
        jaxbElement = null;
    }

    public boolean isPermitted(SOSPermissionCommands permissions, SOSShiroFolderPermissions sosShiroFolderPermissions) {
        if (this.command == null){
            return false;
        }
        
        String folder = "";
        boolean returnValue = false;
        
        
        if (command instanceof Order){
            Order c = (Order)command;
            folder = c.getJobChain();
            returnValue = permissions.getJobChain().getExecute().isAddOrder();
        }
        
        if (command instanceof ShowJob){
            ShowJob c = (ShowJob)command;
            folder = c.getJob();
            returnValue = permissions.getJob().getView().isStatus();
        }
        
        if (command instanceof JobChainNodeModify){
            JobChainNodeModify jobchainNodeModify = (JobChainNodeModify)command;
            folder = jobchainNodeModify.getJobChain();
            
            if ("stop".equalsIgnoreCase(jobchainNodeModify.getAction().value())){
                returnValue = permissions.getJobChain().getExecute().isStopJobChainNode();
            }
            if ("next_state".equalsIgnoreCase(jobchainNodeModify.getAction().value())){
                returnValue = permissions.getJobChain().getExecute().isSkipJobChainNode();
            }
            if ("process".equalsIgnoreCase(jobchainNodeModify.getAction().value())){
                returnValue = permissions.getJobChain().getExecute().isProcessJobChainNode();
            }
        }

        if (command instanceof JobChainModify){
            JobChainModify jobchainModify = (JobChainModify)command;
            folder = jobchainModify.getJobChain();
           
            if ("stopped".equalsIgnoreCase(jobchainModify.getState())){
                returnValue = permissions.getJobChain().getExecute().isStop();
            }
            if ("running".equalsIgnoreCase(jobchainModify.getState())){
                returnValue = permissions.getJobChain().getExecute().isUnstop();
            }
        }
            
        if (command instanceof JobWhy){
            JobWhy c = (JobWhy)command;
            folder = c.getJob();
            returnValue = permissions.getJob().getView().isStatus();
        }
        
        if (command instanceof KillTask){
            KillTask c = (KillTask)command;
            folder = c.getJob();

            returnValue = permissions.getJob().getExecute().isKill();
        }
        
        if (command instanceof LockRemove){
            LockRemove c = (LockRemove)command;
            folder = c.getLock();
            
            returnValue = permissions.getLock().isRemove();
        }
        
        if (command instanceof ModifyHotFolder){
            ModifyHotFolder modifyHotFolder = (ModifyHotFolder)command;
            folder = modifyHotFolder.getFolder();
            
            if (modifyHotFolder.getJob() != null){
                returnValue = permissions.getJob().getChange().isHotFolder();
            }
            if (modifyHotFolder.getJobChain() != null){
                returnValue = permissions.getJobChain().getChange().isHotFolder();
            }
            if (modifyHotFolder.getLock() != null){
                returnValue = permissions.getLock().getChange().isHotFolder();
            }
            if (modifyHotFolder.getOrder() != null){
                returnValue = permissions.getOrder().getChange().isHotFolder();
            }
            if (modifyHotFolder.getProcessClass() != null){
                returnValue = permissions.getProcessClass().getChange().isHotFolder();
            }
            if (modifyHotFolder.getSchedule() != null){
                returnValue = permissions.getSchedule().getChange().isHotFolder();
            }
        }
        
        if (command instanceof ModifyJob){
            ModifyJob modifyJob = (ModifyJob)command;
            folder = modifyJob.getJob();

            if ("start".equalsIgnoreCase(modifyJob.getCmd())){
                returnValue = permissions.getJob().getExecute().isStart();
            }
            if ("wake".equalsIgnoreCase(modifyJob.getCmd())){
                returnValue = permissions.getJob().getExecute().isStart();
            }
            if ("stop".equalsIgnoreCase(modifyJob.getCmd())){
                returnValue = permissions.getJob().getExecute().isStop();
            }
            if ("unstop".equalsIgnoreCase(modifyJob.getCmd())){
                returnValue = permissions.getJob().getExecute().isUnstop();
            }
            if ("suspend".equalsIgnoreCase(modifyJob.getCmd())){
                returnValue = permissions.getJob().getExecute().isSuspendAllTasks();
            }
            if ("continue".equalsIgnoreCase(modifyJob.getCmd())){
                returnValue = permissions.getJob().getExecute().isContinueAllTasks();
            }
            if ("end".equalsIgnoreCase(modifyJob.getCmd())){
                returnValue = permissions.getJob().getExecute().isEndAllTasks();
            }
        }

        if (command instanceof ModifyOrder){
           ModifyOrder modifyOrder = (ModifyOrder)command;
           folder = modifyOrder.getJobChain();

           if (modifyOrder.getEndState() != null){
               returnValue = permissions.getOrder().getChange().isStartAndEndNode();
           }
           if (modifyOrder.getAt() != null){
               returnValue = permissions.getOrder().getChange().isTimeForAdhocOrder();
           }
           if (modifyOrder.getParams() != null){
               returnValue = permissions.getOrder().getChange().isParameter();
           }
           returnValue = permissions.getOrder().getChange().isOther();
        }
        
        if (command instanceof ModifySpooler){
            ModifySpooler modifySpooler = (ModifySpooler)command;
            if ("pause".equalsIgnoreCase(modifySpooler.getCmd())){
                returnValue = permissions.getJobschedulerMaster().getExecute().isPause();
            }
            if ("continue".equalsIgnoreCase(modifySpooler.getCmd())){
                returnValue = permissions.getJobschedulerMaster().getExecute().isContinue();
            }
            if ("stop".equalsIgnoreCase(modifySpooler.getCmd())){
                returnValue = permissions.getJobschedulerMaster().getExecute().isStop();
            }
            if ("reload".equalsIgnoreCase(modifySpooler.getCmd())){
                returnValue = permissions.getJobschedulerMaster().getExecute().isReload();
            }
            if ("terminate".equalsIgnoreCase(modifySpooler.getCmd())){
                returnValue = permissions.getJobschedulerMaster().getExecute().isTerminate();
            }
            if ("terminate_and_restart".equalsIgnoreCase(modifySpooler.getCmd())){
                returnValue = permissions.getJobschedulerMaster().getExecute().getRestart().isTerminate();
            }
            if ("let_run_terminate".equalsIgnoreCase(modifySpooler.getCmd())){
                returnValue = permissions.getJobschedulerMaster().getExecute().getRestart().isTerminate();
            }
            if ("abort_immediately".equalsIgnoreCase(modifySpooler.getCmd())){
                returnValue = permissions.getJobschedulerMaster().getExecute().isAbort();
            }
            if ("abort_immediately_and_restart".equalsIgnoreCase(modifySpooler.getCmd())){
                returnValue = permissions.getJobschedulerMaster().getExecute().getRestart().isAbort();
            }
        }

        if (command instanceof ParamGet){
            returnValue = permissions.getJobschedulerMaster().getView().isParameter();
        }

        if (command instanceof ParamsGet){
        	returnValue = permissions.getJobschedulerMaster().getView().isParameter();
        }
        
        if (command instanceof Params){
            returnValue = permissions.getJobschedulerMaster().getView().isParameter();
        }

        if (command instanceof ProcessClassRemove){
            ProcessClassRemove c = (ProcessClassRemove)command;
            folder = c.getProcessClass();

            returnValue = permissions.getProcessClass().isRemove();
        }

        if (command instanceof ProcessClass){
            ProcessClass c = (ProcessClass)command;
            folder = c.getName();

            returnValue = permissions.getProcessClass().getView().isStatus();
        }

        if (command instanceof RemoveJobChain){
            RemoveJobChain c = (RemoveJobChain)command;
            folder = c.getJobChain();

            returnValue = permissions.getJobChain().getExecute().isRemove();
        }

        if (command instanceof RemoveOrder){
            RemoveOrder c = (RemoveOrder)command;
            folder = c.getJobChain();

            returnValue = permissions.getOrder().isDelete();
        }
        
        if (command instanceof ScheduleRemove){
            ScheduleRemove c = (ScheduleRemove)command;
            folder = c.getSchedule();

            returnValue = permissions.getSchedule().isRemove();
        }
        
        if (command instanceof SchedulerLogLogCategoriesReset){
            returnValue = permissions.getJobschedulerMaster().getAdministration().isManageCategories();
        }
        
        if (command instanceof SchedulerLogLogCategoriesSet){
            returnValue = permissions.getJobschedulerMaster().getAdministration().isManageCategories();
        }

        if (command instanceof SchedulerLogLogCategoriesShow){
            returnValue = permissions.getJobschedulerMaster().getAdministration().isManageCategories();
        }
        
        if (command instanceof ShowCalendar){
            returnValue = permissions.getDailyPlan().getView().isStatus();
        }

        if (command instanceof ShowHistory){
            ShowHistory c = (ShowHistory)command;
            folder = c.getJob();
            
            returnValue = permissions.getHistory().isView();
        }
        
        if (command instanceof ShowJob){
            ShowJob c = (ShowJob)command;
            folder = c.getJob();
            
            returnValue = permissions.getJob().getView().isStatus();
        }

        if (command instanceof ShowJobs){
            returnValue = permissions.getJob().getView().isStatus();
        }
        
        if (command instanceof ShowJobChain){
            ShowJobChain c = (ShowJobChain)command;
            folder = c.getJobChain();
            
            returnValue = permissions.getJobChain().getView().isStatus();
        }
        
        if (command instanceof ShowJobChains){
            returnValue = permissions.getJobChain().getView().isStatus();
        }
        
        if (command instanceof ShowOrder){
            ShowOrder c = (ShowOrder)command;
            folder = c.getJobChain();
            
            returnValue = permissions.getOrder().getView().isStatus();
        }
        
        if (command instanceof ShowState){
            returnValue = permissions.getJobschedulerMaster().getView().isStatus();
        }
        
        if (command instanceof ShowTask){          
            returnValue = permissions.getJob().getView().isStatus();
        }
        
        if (command instanceof StartJob){
            StartJob c = (StartJob)command;
            folder = c.getJob();

            returnValue = permissions.getJob().getExecute().isStart();
        }
        
        if (command instanceof Terminate){
            returnValue = permissions.getJobschedulerMaster().getExecute().isTerminate();
        }
        
        return ("".equals(folder) || sosShiroFolderPermissions.isPermittedForFolder(folder)) && returnValue;

    }

    private String asXml(Object command, JAXBElement<?> j) throws JAXBException {
        this.command = command;
        String xml = "";
        JAXBContext jaxbContext = JAXBContext.newInstance(command.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter s = new StringWriter();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        if (j == null) {
            jaxbMarshaller.marshal(command, s);
        } else {
            jaxbMarshaller.marshal(j, s);
        }
        xml = s.toString();
        return xml;
    }

    public String getXml(Object command) throws JAXBException {
        jaxbElement = null;
        if (command instanceof JAXBElement) {
            jaxbElement = (JAXBElement<?>) command;

            if ("show_state".equals(jaxbElement.getName().toString())) {
                jaxbElement = objectFactory.createShowState((ShowState) jaxbElement.getValue());
                command = (ShowState) jaxbElement.getValue();
            }
            if ("add_order".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createAddOrder((Order) jaxbElement.getValue());
                command = (Order) jaxbElement.getValue();
            }
            if ("process_class".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createProcessClass((ProcessClass) jaxbElement.getValue());
                command = (ProcessClass) jaxbElement.getValue();
            }
            if ("lock.remove".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createLockRemove((LockRemove) jaxbElement.getValue());
                command = (LockRemove) jaxbElement.getValue();
            }
            if ("modify_hot_folder".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createModifyHotFolder((ModifyHotFolder) jaxbElement.getValue());
                command = (ModifyHotFolder) jaxbElement.getValue();
            }
            if ("process_class.remove".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createProcessClassRemove((ProcessClassRemove) jaxbElement.getValue());
                command = (ProcessClassRemove) jaxbElement.getValue();
            }
            if ("s".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createS((ShowState) jaxbElement.getValue());
                command = (ShowState) jaxbElement.getValue();
            }
            if ("schedule.remove".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createScheduleRemove((ScheduleRemove) jaxbElement.getValue());
                command = (ScheduleRemove) jaxbElement.getValue();
            }
            if ("params.get".equals(jaxbElement.getName().getLocalPart().toString())) {
                command = (ParamsGet) objectFactory.createParamsGet();
            }
        
            
        }

        return asXml(command, jaxbElement);
    }

}
