package com.sos.joc.classes;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.sos.auth.rest.SOSShiroFolderPermissions;
import com.sos.auth.rest.permission.model.SOSPermissionCommands;
import com.sos.joc.Globals;
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
import com.sos.joc.model.commands.ParamsGet;
import com.sos.joc.model.commands.ProcessClass;
import com.sos.joc.model.commands.ProcessClassRemove;
import com.sos.joc.model.commands.RemoveJobChain;
import com.sos.joc.model.commands.RemoveOrder;
import com.sos.joc.model.commands.ScheduleRemove;
import com.sos.joc.model.commands.ShowHistory;
import com.sos.joc.model.commands.ShowJob;
import com.sos.joc.model.commands.ShowJobChain;
import com.sos.joc.model.commands.ShowOrder;
import com.sos.joc.model.commands.ShowState;
import com.sos.joc.model.commands.StartJob;
import com.sos.joc.model.commands.SubsystemShow;

public class JobSchedulerCommandFactory {

    private ObjectFactory objectFactory;
    private JAXBElement<?> jaxbElement;
    private Object command;
    private String job = null;
    private String jobChain = null;
    private String orderId = null;
    private String folder = null;

    public JobSchedulerCommandFactory() {
        objectFactory = new ObjectFactory();
    }
    
    public JobSchedulerCommandFactory(Object command) throws JAXBException {
        objectFactory = new ObjectFactory();
        init(command);
    }
    
    public String getCommandName() {
        return command.getClass().getSimpleName();
    }
    
    public String getJob() {
        return job;
    }
    
    public String getJobChain() {
        return jobChain;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public String getFolder() {
        return folder;
    }

    public boolean isPermitted(SOSPermissionCommands permissions, SOSShiroFolderPermissions sosShiroFolderPermissions) {
        if (command == null) {
            return false;
        }

        boolean returnValue = false;
        
        switch (command.getClass().getSimpleName()) {
        case "Order":
            Order order = (Order) command;
            jobChain = order.getJobChain();
            orderId = order.getId(); 
            folder = jobChain;
            returnValue = permissions.getJobChain().getExecute().isAddOrder();
            break;
        case "ShowJob":
            folder = ((ShowJob) command).getJob();
            returnValue = permissions.getJob().getView().isStatus();
            break;
        case "JobChainNodeModify":
            JobChainNodeModify jobchainNodeModify = (JobChainNodeModify) command;
            jobChain = jobchainNodeModify.getJobChain();
            folder = jobChain;

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
            break;
        case "JobChainModify":
            JobChainModify jobchainModify = (JobChainModify) command;
            jobChain = jobchainModify.getJobChain();
            folder = jobChain;
            
            switch (jobchainModify.getState().toLowerCase()) {
            case "stopped":
                returnValue = permissions.getJobChain().getExecute().isStop();
                break;
            case "running":
                returnValue = permissions.getJobChain().getExecute().isUnstop();
                break;
            }
            break;
        case "JobWhy":
            job = ((JobWhy) command).getJob();
            folder = job;
            returnValue = permissions.getJob().getView().isStatus();
            break;
        case "KillTask":
            job = ((KillTask) command).getJob();
            folder = job;
            returnValue = permissions.getJob().getExecute().isKill();
            break;
        case "LockRemove":
            folder = ((LockRemove) command).getLock();
            returnValue = permissions.getLock().isRemove();
            break;
        case "ModifyHotFolder":
            ModifyHotFolder modifyHotFolder = (ModifyHotFolder) command;
            folder = modifyHotFolder.getFolder() + "/dummy";

            if (modifyHotFolder.getJob() != null) {
                job = modifyHotFolder.getJob().getName();
                returnValue = permissions.getJob().getChange().isHotFolder();
            } else if (modifyHotFolder.getJobChain() != null) {
                jobChain = modifyHotFolder.getJobChain().getName();
                returnValue = permissions.getJobChain().getChange().isHotFolder();
            } else if (modifyHotFolder.getLock() != null) {
                returnValue = permissions.getLock().getChange().isHotFolder();
            } else if (modifyHotFolder.getOrder() != null) {
                jobChain = modifyHotFolder.getOrder().getJobChain();
                orderId = modifyHotFolder.getOrder().getId();
                returnValue = permissions.getOrder().getChange().isHotFolder();
            } else if (modifyHotFolder.getProcessClass() != null) {
                returnValue = permissions.getProcessClass().getChange().isHotFolder();
            } else if (modifyHotFolder.getSchedule() != null) {
                returnValue = permissions.getSchedule().getChange().isHotFolder();
            }
            break;
        case "ModifyJob":
            ModifyJob modifyJob = (ModifyJob) command;
            job = modifyJob.getJob();
            folder = job;
            
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
            break;
        case "ModifyOrder":
            ModifyOrder modifyOrder = (ModifyOrder) command;
            jobChain = modifyOrder.getJobChain();
            orderId = modifyOrder.getOrder();
            folder = jobChain;

            if (modifyOrder.getEndState() != null) {
                returnValue = permissions.getOrder().getChange().isStartAndEndNode();
            } else if (modifyOrder.getAt() != null) {
                returnValue = permissions.getOrder().getChange().isTimeForAdhocOrder();
            } else if (modifyOrder.getParams() != null) {
                returnValue = permissions.getOrder().getChange().isParameter();
            } else {
                returnValue = permissions.getOrder().getChange().isOther();
            }
            break;
        case "ModifySpooler":
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
            break;
        case "ParamGet":
        case "ParamsGet":
        case "Params":
            returnValue = permissions.getJobschedulerMaster().getView().isParameter();
            break;
        case "ProcessClassRemove":
            folder = ((ProcessClassRemove) command).getProcessClass();
            returnValue = permissions.getProcessClass().isRemove();
            break;
        case "ProcessClass":
            folder = ((ProcessClass) command).getName();
            returnValue = permissions.getProcessClass().getView().isStatus();
            break;
        case "RemoveJobChain":
            jobChain = ((RemoveJobChain) command).getJobChain();
            folder = jobChain;
            returnValue = permissions.getJobChain().getExecute().isRemove();
            break;
        case "RemoveOrder":
            RemoveOrder removeOrder = (RemoveOrder) command;
            jobChain = removeOrder.getJobChain();
            orderId = removeOrder.getOrder();
            folder = jobChain;
            returnValue = permissions.getOrder().isDelete();
            break;
        case "ScheduleRemove":
            folder = ((ScheduleRemove) command).getSchedule();
            returnValue = permissions.getSchedule().isRemove();
            break;
        case "SchedulerLogLogCategoriesReset":
        case "SchedulerLogLogCategoriesSet":
        case "SchedulerLogLogCategoriesShow":
            returnValue = permissions.getJobschedulerMaster().getAdministration().isManageCategories();
            break;
        case "ShowCalendar":
            returnValue = permissions.getDailyPlan().getView().isStatus();
            break;
        case "ShowHistory":
            folder = ((ShowHistory) command).getJob();
            returnValue = permissions.getHistory().isView();
            break;
        case "ShowJobs":
            returnValue = permissions.getJob().getView().isStatus();
            break;
        case "ShowJobChain":
            folder = ((ShowJobChain) command).getJobChain();
            returnValue = permissions.getJobChain().getView().isStatus();
            break;
        case "ShowJobChains":
            returnValue = permissions.getJobChain().getView().isStatus();
            break;
        case "ShowOrder":
            folder = ((ShowOrder) command).getJobChain();
            returnValue = permissions.getOrder().getView().isStatus();
            break;
        case "ShowState":
            returnValue = permissions.getJobschedulerMaster().getView().isStatus();
            break;
        case "ShowTask":
            returnValue = permissions.getJob().getView().isStatus();
            break;
        case "StartJob":
            job = ((StartJob) command).getJob();
            folder = job;
            returnValue = permissions.getJob().getExecute().isStart();
            break;
        case "Terminate":
            returnValue = permissions.getJobschedulerMaster().getExecute().isTerminate();
            break;
        case "SubsystemShow":
            returnValue = permissions.getSubsystemShow().getView().isSetStatus();
            break;
        }

        if (folder != null) {
            folder = Globals.getParent(folder);
        }
        return (folder == null || folder.isEmpty() || sosShiroFolderPermissions.isPermittedForFolder(folder)) && returnValue;
    }
    
    public boolean withAuditLog() {
        if (command == null) {
            return false;
        }
        switch (command.getClass().getSimpleName()) {
        case "Order":
        case "StartJob":
        case "JobChainNodeModify":
        case "JobChainModify":
        case "KillTask":
        case "LockRemove":
        case "ModifyHotFolder":
        case "ModifyJob":
        case "ModifyOrder":
        case "ModifySpooler":
        case "ProcessClassRemove":
        case "ProcessClass":
        case "RemoveJobChain":
        case "RemoveOrder":
        case "ScheduleRemove":
        case "Terminate":
            return true;
        default:
            return false;
        }

    }

    public String asXml() throws JAXBException {
        String xml = "";
        JAXBContext jaxbContext = JAXBContext.newInstance(command.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter s = new StringWriter();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        if (jaxbElement == null) {
            jaxbMarshaller.marshal(command, s);
        } else {
            jaxbMarshaller.marshal(jaxbElement, s);
        }
        xml = s.toString();
        return xml;
    }

    public void init(Object command) throws JAXBException {
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
            }else if ("subsystem.show".equals(jaxbElement.getName().getLocalPart().toString())) {
                    command = (SubsystemShow) objectFactory.createSubsystemShow();
            }
        }
        this.jaxbElement = jaxbElement;
        this. command = command;
    }

}
