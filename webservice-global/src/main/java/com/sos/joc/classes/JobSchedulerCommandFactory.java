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

    public JobSchedulerCommandFactory() {
        objectFactory = new ObjectFactory();
    }

    public boolean isPermitted(Object command, SOSPermissionCommands permissions, SOSShiroFolderPermissions sosShiroFolderPermissions) {
        if (command == null) {
            return false;
        }

        String folder = "";
        boolean returnValue = false;

        if (command instanceof Order) {
            Order c = (Order) command;
            folder = c.getJobChain();
            returnValue = permissions.getJobChain().getExecute().isAddOrder();

        } else if (command instanceof ShowJob) {
            ShowJob c = (ShowJob) command;
            folder = c.getJob();
            returnValue = permissions.getJob().getView().isStatus();

        } else if (command instanceof JobChainNodeModify) {
            JobChainNodeModify jobchainNodeModify = (JobChainNodeModify) command;
            folder = jobchainNodeModify.getJobChain();

            switch (jobchainNodeModify.getAction().value().toLowerCase()) {
            case "stop":
                returnValue = permissions.getJobChain().getExecute().isStopJobChainNode();
                break;
            case "next_state":
                returnValue = permissions.getJobChain().getExecute().isSkipJobChainNode();
                break;
            case "process":
                returnValue = permissions.getJobChain().getExecute().isProcessJobChainNode();
                break;
            }

        } else if (command instanceof JobChainModify) {
            JobChainModify jobchainModify = (JobChainModify) command;
            folder = jobchainModify.getJobChain();

            switch (jobchainModify.getState().toLowerCase()) {
            case "stopped":
                returnValue = permissions.getJobChain().getExecute().isStop();
                break;
            case "running":
                returnValue = permissions.getJobChain().getExecute().isUnstop();
                break;
            }

        } else if (command instanceof JobWhy) {
            JobWhy c = (JobWhy) command;
            folder = c.getJob();
            returnValue = permissions.getJob().getView().isStatus();

        } else if (command instanceof KillTask) {
            KillTask c = (KillTask) command;
            folder = c.getJob();
            returnValue = permissions.getJob().getExecute().isKill();
            
        } else if (command instanceof LockRemove) {
            LockRemove c = (LockRemove) command;
            folder = c.getLock();
            returnValue = permissions.getLock().isRemove();
            
        } else if (command instanceof ModifyHotFolder) {
            ModifyHotFolder modifyHotFolder = (ModifyHotFolder) command;
            folder = modifyHotFolder.getFolder();

            if (modifyHotFolder.getJob() != null) {
                returnValue = permissions.getJob().getChange().isHotFolder();
            } else if (modifyHotFolder.getJobChain() != null) {
                returnValue = permissions.getJobChain().getChange().isHotFolder();
            } else if (modifyHotFolder.getLock() != null) {
                returnValue = permissions.getLock().getChange().isHotFolder();
            } else if (modifyHotFolder.getOrder() != null) {
                returnValue = permissions.getOrder().getChange().isHotFolder();
            } else if (modifyHotFolder.getProcessClass() != null) {
                returnValue = permissions.getProcessClass().getChange().isHotFolder();
            } else if (modifyHotFolder.getSchedule() != null) {
                returnValue = permissions.getSchedule().getChange().isHotFolder();
            }
            
        } else if (command instanceof ModifyJob) {
            ModifyJob modifyJob = (ModifyJob) command;
            folder = modifyJob.getJob();

            switch (modifyJob.getCmd().toLowerCase()) {
            case "start":
            case "wake":
                returnValue = permissions.getJob().getExecute().isStart();
                break;
            case "stop":
                returnValue = permissions.getJob().getExecute().isStop();
                break;
            case "unstop":
                returnValue = permissions.getJob().getExecute().isUnstop();
                break;
            case "suspend":
                returnValue = permissions.getJob().getExecute().isSuspendAllTasks();
                break;
            case "continue":
                returnValue = permissions.getJob().getExecute().isContinueAllTasks();
                break;
            case "end":
                returnValue = permissions.getJob().getExecute().isEndAllTasks();
                break;
            }
            
        } else if (command instanceof ModifyOrder) {
            ModifyOrder modifyOrder = (ModifyOrder) command;
            folder = modifyOrder.getJobChain();

            if (modifyOrder.getEndState() != null) {
                returnValue = permissions.getOrder().getChange().isStartAndEndNode();
            } else if (modifyOrder.getAt() != null) {
                returnValue = permissions.getOrder().getChange().isTimeForAdhocOrder();
            } else if (modifyOrder.getParams() != null) {
                returnValue = permissions.getOrder().getChange().isParameter();
            } else {
                returnValue = permissions.getOrder().getChange().isOther();
            }
            
        } else if (command instanceof ModifySpooler) {
            ModifySpooler modifySpooler = (ModifySpooler) command;
            switch (modifySpooler.getCmd().toLowerCase()) {
            case "pause":
                returnValue = permissions.getJobschedulerMaster().getExecute().isPause();
                break;
            case "continue":
                returnValue = permissions.getJobschedulerMaster().getExecute().isContinue();
                break;
            case "stop":
                returnValue = permissions.getJobschedulerMaster().getExecute().isStop();
                break;
            case "reload":
                returnValue = permissions.getJobschedulerMaster().getExecute().isReload();
                break;
            case "terminate":
                returnValue = permissions.getJobschedulerMaster().getExecute().isTerminate();
                break;
            case "terminate_and_restart":
            case "let_run_terminate":
                returnValue = permissions.getJobschedulerMaster().getExecute().getRestart().isTerminate();
                break;
            case "abort_immediately":
                returnValue = permissions.getJobschedulerMaster().getExecute().isAbort();
                break;
            case "abort_immediately_and_restart":
                returnValue = permissions.getJobschedulerMaster().getExecute().getRestart().isAbort();
                break;
            }
            
        } else if (command instanceof ParamGet) {
            returnValue = permissions.getJobschedulerMaster().getView().isParameter();
            
        } else if (command instanceof ParamsGet) {
            returnValue = permissions.getJobschedulerMaster().getView().isParameter();
            
        } else if (command instanceof Params) {
            returnValue = permissions.getJobschedulerMaster().getView().isParameter();
            
        } else if (command instanceof ProcessClassRemove) {
            ProcessClassRemove c = (ProcessClassRemove) command;
            folder = c.getProcessClass();
            returnValue = permissions.getProcessClass().isRemove();
            
        } else if (command instanceof ProcessClass) {
            ProcessClass c = (ProcessClass) command;
            folder = c.getName();
            returnValue = permissions.getProcessClass().getView().isStatus();
            
        } else if (command instanceof RemoveJobChain) {
            RemoveJobChain c = (RemoveJobChain) command;
            folder = c.getJobChain();
            returnValue = permissions.getJobChain().getExecute().isRemove();
            
        } else if (command instanceof RemoveOrder) {
            RemoveOrder c = (RemoveOrder) command;
            folder = c.getJobChain();
            returnValue = permissions.getOrder().isDelete();
            
        } else if (command instanceof ScheduleRemove) {
            ScheduleRemove c = (ScheduleRemove) command;
            folder = c.getSchedule();
            returnValue = permissions.getSchedule().isRemove();
            
        } else if (command instanceof SchedulerLogLogCategoriesReset) {
            returnValue = permissions.getJobschedulerMaster().getAdministration().isManageCategories();
            
        } else if (command instanceof SchedulerLogLogCategoriesSet) {
            returnValue = permissions.getJobschedulerMaster().getAdministration().isManageCategories();
            
        } else if (command instanceof SchedulerLogLogCategoriesShow) {
            returnValue = permissions.getJobschedulerMaster().getAdministration().isManageCategories();
            
        } else if (command instanceof ShowCalendar) {
            returnValue = permissions.getDailyPlan().getView().isStatus();
            
        } else if (command instanceof ShowHistory) {
            ShowHistory c = (ShowHistory) command;
            folder = c.getJob();
            returnValue = permissions.getHistory().isView();
            
        } else if (command instanceof ShowJob) {
            ShowJob c = (ShowJob) command;
            folder = c.getJob();
            returnValue = permissions.getJob().getView().isStatus();
            
        } else if (command instanceof ShowJobs) {
            returnValue = permissions.getJob().getView().isStatus();
            
        } else if (command instanceof ShowJobChain) {
            ShowJobChain c = (ShowJobChain) command;
            folder = c.getJobChain();
            returnValue = permissions.getJobChain().getView().isStatus();
            
        } else if (command instanceof ShowJobChains) {
            returnValue = permissions.getJobChain().getView().isStatus();
            
        } else if (command instanceof ShowOrder) {
            ShowOrder c = (ShowOrder) command;
            folder = c.getJobChain();
            returnValue = permissions.getOrder().getView().isStatus();
            
        } else if (command instanceof ShowState) {
            returnValue = permissions.getJobschedulerMaster().getView().isStatus();
            
        } else if (command instanceof ShowTask) {
            returnValue = permissions.getJob().getView().isStatus();
            
        } else if (command instanceof StartJob) {
            StartJob c = (StartJob) command;
            folder = c.getJob();
            returnValue = permissions.getJob().getExecute().isStart();
            
        } else if (command instanceof Terminate) {
            returnValue = permissions.getJobschedulerMaster().getExecute().isTerminate();
        }

        return ("".equals(folder) || sosShiroFolderPermissions.isPermittedForFolder(folder)) && returnValue;

    }

    private String asXml(Object command, JAXBElement<?> j) throws JAXBException {
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
        JAXBElement<?> jaxbElement = null;
        if (command instanceof JAXBElement) {
            jaxbElement = (JAXBElement<?>) command;

            if ("show_state".equals(jaxbElement.getName().toString())) {
                jaxbElement = objectFactory.createShowState((ShowState) jaxbElement.getValue());
                command = (ShowState) jaxbElement.getValue();
            } else if ("add_order".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createAddOrder((Order) jaxbElement.getValue());
                command = (Order) jaxbElement.getValue();
            } else if ("process_class".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createProcessClass((ProcessClass) jaxbElement.getValue());
                command = (ProcessClass) jaxbElement.getValue();
            } else if ("lock.remove".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createLockRemove((LockRemove) jaxbElement.getValue());
                command = (LockRemove) jaxbElement.getValue();
            } else if ("modify_hot_folder".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createModifyHotFolder((ModifyHotFolder) jaxbElement.getValue());
                command = (ModifyHotFolder) jaxbElement.getValue();
            } else if ("process_class.remove".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createProcessClassRemove((ProcessClassRemove) jaxbElement.getValue());
                command = (ProcessClassRemove) jaxbElement.getValue();
            } else if ("s".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createS((ShowState) jaxbElement.getValue());
                command = (ShowState) jaxbElement.getValue();
            } else if ("schedule.remove".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createScheduleRemove((ScheduleRemove) jaxbElement.getValue());
                command = (ScheduleRemove) jaxbElement.getValue();
            } else if ("params.get".equals(jaxbElement.getName().getLocalPart().toString())) {
                command = (ParamsGet) objectFactory.createParamsGet();
            }
        }

        return asXml(command, jaxbElement);
    }

}
