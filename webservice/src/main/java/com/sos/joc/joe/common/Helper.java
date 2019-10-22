package com.sos.joc.joe.common;

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

public class Helper {

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
            return sosPermission.getJob().getChange().isHotfolder() && sosPermission.getJobChain().getChange().isHotFolder() && sosPermission
                    .getProcessClass().getChange().isHotFolder() && sosPermission.getOrder().getChange().isHotFolder() && sosPermission.getSchedule()
                            .getChange().isHotFolder() && sosPermission.getLock().getChange().isHotFolder();
        }

        switch (objectType) {
        case MONITOR:
        case JOB:
            return sosPermission.getJob().getChange().isHotfolder();
        case NODEPARAMS:
        case JOBCHAIN:
            return sosPermission.getJobChain().getChange().isHotFolder();
        case AGENTCLUSTER:
        case PROCESSCLASS:
            return sosPermission.getProcessClass().getChange().isHotFolder();
        case ORDER:
            return sosPermission.getOrder().getChange().isHotFolder();
        case HOLIDAYS:
        case SCHEDULE:
            return sosPermission.getSchedule().getChange().isHotFolder();
        case LOCK:
            return sosPermission.getLock().getChange().isHotFolder();
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
        return path.endsWith(Helper.getFileExtension(jobSchedulerObjectType));
    }

    public static byte[] concatByteArray(byte[] a, byte[] b) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(a);
        outputStream.write(b);

        return outputStream.toByteArray();
    }
    
    public static CustomEvent sendEvent(String path, String objectType) {
        CustomEvent evt = new CustomEvent();
        evt.setKey("JoeUpdated");
        CustomEventVariables evtVars = new CustomEventVariables();
        evtVars.setAdditionalProperty("path", path);
        evtVars.setAdditionalProperty("objectType", objectType);
        evt.setVariables(evtVars);
        return evt;
    }

}
