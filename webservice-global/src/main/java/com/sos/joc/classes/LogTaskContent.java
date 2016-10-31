package com.sos.joc.classes;

import java.math.BigInteger;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.db.history.task.JobSchedulerTaskHistoryDBLayer;
import com.sos.joc.model.job.TaskFilter;
import com.sos.scheduler.model.commands.JSCmdShowTask;

public class LogTaskContent extends LogContent {

    private static final String XPATH_TASK_LOG = "//spooler/answer/task/log";
    private static final String WHAT_LOG = "log";
    private TaskFilter taskFilter;
    
    public LogTaskContent(TaskFilter taskFilter, DBItemInventoryInstance dbItemInventoryInstance, String accessToken) {
        super(dbItemInventoryInstance, accessToken);
        this.taskFilter = taskFilter;
    }

    public String getLog() throws Exception {
        SOSHibernateConnection sosHibernateConnection = Globals.getConnection(taskFilter.getJobschedulerId());
        sosHibernateConnection.beginTransaction();
        try {
            JobSchedulerTaskHistoryDBLayer jobSchedulerTaskHistoryDBLayer = new JobSchedulerTaskHistoryDBLayer(sosHibernateConnection);
            String log = jobSchedulerTaskHistoryDBLayer.getLogAsString(taskFilter.getTaskId());
            if (log == null) {
                log = getTaskLogFromXmlCommand();
            }
            return log;
        } catch (Exception e) {
            throw e;
        } finally {
            sosHibernateConnection.rollback();
        }
    }

    private String getTaskLogFromXmlCommand() throws Exception {

        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

        JSCmdShowTask jsCmdShowTask = Globals.schedulerObjectFactory.createShowTask();
        jsCmdShowTask.setWhat(WHAT_LOG);
        jsCmdShowTask.setId(new BigInteger(taskFilter.getTaskId()));
        String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdShowTask);
        jocXmlCommand.executePostWithThrowBadRequest(xml, getAccessToken());
        return jocXmlCommand.getSosxml().selectSingleNodeValue(XPATH_TASK_LOG, null);
    }
}
