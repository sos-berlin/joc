package com.sos.joc.joe.common;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.jitl.reporting.helper.EConfigFileExtensions;
import com.sos.joc.model.common.JobSchedulerObjectType;

public class Helper {
    
    public static boolean hasPermission(JobSchedulerObjectType objectType, SOSPermissionJocCockpit sosPermission) {
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

        default:
            break;
        }
        return fileExtension;

    }
    
}
