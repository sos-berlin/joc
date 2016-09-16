package com.sos.joc.classes.job;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.w3c.dom.Element;

import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.job.Job_;


public class JobsVCallable implements Callable<Map<String,Job_>> {
    private final Element job;
    private final FoldersSchema folder;
    private final JOCXmlCommand jocXmlCommand;
    private final Boolean withOrderQueue;
    private final Boolean compact;

    public JobsVCallable(Element job, JOCXmlCommand jocXmlCommand, Boolean compact, Boolean withOrderQueue) {
        this.job = job;
        this.folder = null;
        this.jocXmlCommand = jocXmlCommand;
        this.compact = compact;
        this.withOrderQueue = withOrderQueue;
    }
    
    public JobsVCallable(FoldersSchema folder, JOCXmlCommand jocXmlCommand, Boolean compact) {
        this.job = null;
        this.folder = folder;
        this.jocXmlCommand = jocXmlCommand;
        this.compact = compact;
        this.withOrderQueue = null;
    }

    @Override
    public Map<String, Job_> call() throws Exception {
        if(job != null) {
            return getJobs(job, jocXmlCommand, compact, withOrderQueue);
        } else {
            return getJobs(folder, jocXmlCommand, compact);
        }
    }
    
    public Job_ getJob() throws Exception {
        Map<String, Job_> jobMap = getJobs(job, jocXmlCommand, compact, withOrderQueue);
        return jobMap.values().iterator().next();
    }

    private Map<String, Job_> getJobs(Element job, JOCXmlCommand jocXmlCommand, Boolean compact, Boolean withOrderQueue) throws Exception {
        Map<String, Job_> jobMap = new HashMap<String,Job_>();
        JobV jobV = new JobV(job, jocXmlCommand, withOrderQueue);
        jobV.setFields(compact);
        jobMap.put(jobV.getPath(), jobV);
        return jobMap;
    }
    
    private Map<String, Job_> getJobs(FoldersSchema folder, JOCXmlCommand jocXmlCommand, Boolean compact) {
        // TODO Auto-generated method stub
        return null;
    }

}
