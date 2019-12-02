package com.sos.joc.classes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.jitl.reporting.helper.EConfigFileExtensions;
import com.sos.jobscheduler.model.event.CustomEvent;
import com.sos.jobscheduler.model.event.CustomEventVariables;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.job.Job;
import com.sos.joc.model.joe.job.Monitor;
import com.sos.joc.model.joe.jobchain.JobChain;
import com.sos.joc.model.joe.lock.Lock;
import com.sos.joc.model.joe.nodeparams.Config;
import com.sos.joc.model.joe.order.Order;
import com.sos.joc.model.joe.other.Other;
import com.sos.joc.model.joe.processclass.ProcessClass;
import com.sos.joc.model.joe.schedule.HolidaysFile;
import com.sos.joc.model.joe.schedule.RunTime;
import com.sos.joc.model.joe.schedule.Schedule;

public class JOEHelper {

    public static final Map<String, Class<?>> CLASS_MAPPING = Collections.unmodifiableMap(new HashMap<String, Class<?>>() {

        private static final long serialVersionUID = 1L;

        {
            put("JOB", Job.class);
            put("ORDER", Order.class);
            put("JOBCHAIN", JobChain.class);
            put("PROCESSCLASS", ProcessClass.class);
            put("AGENTCLUSTER", ProcessClass.class);
            put("LOCK", Lock.class);
            put("SCHEDULE", Schedule.class);
            put("MONITOR", Monitor.class);
            put("NODEPARAMS", Config.class);
            put("HOLIDAYS", HolidaysFile.class);
            put("RUNTIME", RunTime.class);
            put("OTHER", Other.class);
        }
    });

    public static boolean hasPermission(JobSchedulerObjectType objectType, SOSPermissionJocCockpit sosPermission) {
        if (objectType == null) {
            return  sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isJob() && 
                    sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isJobChain() && 
                    sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isMonitor() &&
                    sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isProcessClass() &&
                    sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isOrder() &&
                    sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isSchedule() &&
                    sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isLock();
        }

        switch (objectType) {
        case MONITOR:
            return sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isMonitor();
        case JOB:
            return sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isJob();
        case NODEPARAMS:
        case JOBCHAIN:
            return sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isJobChain();
        case AGENTCLUSTER:
        case PROCESSCLASS:
            return sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isProcessClass();
        case ORDER:
            return sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isOrder();
        case HOLIDAYS:
        case SCHEDULE:
            return sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isSchedule();
        case LOCK:
            return sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().getDeploy().isLock();
        case OTHER:
        case FOLDER:
            return true;
        default:
            return false;
        }
    }

    public static String getFileExtension(JobSchedulerObjectType jobSchedulerObjectType) {
        String fileExtension = "";
        switch (jobSchedulerObjectType) {
        case JOB:
            fileExtension = EConfigFileExtensions.JOB.extension();
            break;
        case ORDER:
            fileExtension = EConfigFileExtensions.ORDER.extension();
            break;
        case JOBCHAIN:
            fileExtension = EConfigFileExtensions.JOB_CHAIN.extension();
            break;
        case LOCK:
            fileExtension = EConfigFileExtensions.LOCK.extension();
            break;
        case PROCESSCLASS:
        case AGENTCLUSTER:
            fileExtension = EConfigFileExtensions.PROCESS_CLASS.extension();
            break;
        case SCHEDULE:
            fileExtension = EConfigFileExtensions.SCHEDULE.extension();
            break;
        case MONITOR:
            fileExtension = EConfigFileExtensions.MONITOR.extension();
            break;
        case NODEPARAMS:
            fileExtension = ".config.xml";
            break;
        case FOLDER:
            fileExtension = "/";
            break;
        default:
            break;
        }
        return fileExtension;
    }

    public static String getPathWithoutExtension(String path, JobSchedulerObjectType jobSchedulerObjectType) {
        if (path.length() - getFileExtension(jobSchedulerObjectType).length() < 0) {
            return path;
        } else {
            return path.substring(0, path.length() - getFileExtension(jobSchedulerObjectType).length());
        }
    }

    public static boolean pathIsObjectOf(String path, JobSchedulerObjectType jobSchedulerObjectType) {
        return path.endsWith(JOEHelper.getFileExtension(jobSchedulerObjectType));
    }

    public static byte[] concatByteArray(byte[] a, byte[] b) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(a);
        outputStream.write(b);

        return outputStream.toByteArray();
    }

    public static CustomEvent getJoeUpdatedEvent(String path) {
        CustomEvent evt = new CustomEvent();
        evt.setKey("JoeUpdated");
        CustomEventVariables evtVars = new CustomEventVariables();
        evtVars.setAdditionalProperty("path", path);
        evtVars.setAdditionalProperty("objectType", JobSchedulerObjectType.FOLDER.value());
        evt.setVariables(evtVars);
        return evt;
    }

}
