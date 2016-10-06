package com.sos.joc.classes;

import java.math.BigInteger;

import org.w3c.dom.Element;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.db.history.task.JobSchedulerTaskHistoryDBLayer;
import com.sos.joc.model.job.TaskFilterSchema;
import com.sos.scheduler.model.commands.JSCmdShowTask;

public class LogTaskContent extends LogContent {
    private static final String XPATH_TASK_LOG = "//spooler/answer/task/log";
    private static final String WHAT_LOG = "log";
    private TaskFilterSchema taskFilterSchema;

    public LogTaskContent(TaskFilterSchema taskFilterSchema, DBItemInventoryInstance dbItemInventoryInstance) {
        super(dbItemInventoryInstance);
        this.taskFilterSchema = taskFilterSchema;
    }

     public String getLog() throws Exception {
        SOSHibernateConnection sosHibernateConnection = Globals.getConnection(taskFilterSchema.getJobschedulerId());
        sosHibernateConnection.beginTransaction();
        JobSchedulerTaskHistoryDBLayer jobSchedulerTaskHistoryDBLayer = new JobSchedulerTaskHistoryDBLayer(sosHibernateConnection);
        String log = jobSchedulerTaskHistoryDBLayer.getLogAsString(taskFilterSchema.getTaskId());
        if (log==null){
            log = getTaskLogFromXmlCommand();
        }
        sosHibernateConnection.rollback();
        return log;
    }
    
    private String getTaskLogFromXmlCommand() throws Exception{
        
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getCommandUrl());

        JSCmdShowTask jsCmdShowTask = Globals.schedulerObjectFactory.createShowTask();
        jsCmdShowTask.setWhat(WHAT_LOG);
        jsCmdShowTask.setId(BigInteger.valueOf(taskFilterSchema.getTaskId()));

        String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdShowTask);

        jocXmlCommand.excutePost(xml);
        Element logElement = jocXmlCommand.executeXPath(WHAT_LOG, XPATH_TASK_LOG);
        return logElement.getFirstChild().getNodeValue() ;
}

    
    

}
