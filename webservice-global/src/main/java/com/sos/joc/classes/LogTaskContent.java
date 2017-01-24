package com.sos.joc.classes;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.hibernate.classes.SOSHibernateStatelessConnection;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.db.history.task.JobSchedulerTaskHistoryDBLayer;
import com.sos.joc.model.job.TaskFilter;

public class LogTaskContent extends LogContent {

    private static final String XPATH_TASK_LOG = "/spooler/answer/task/log";
    private TaskFilter taskFilter;
    
    public LogTaskContent(TaskFilter taskFilter, DBItemInventoryInstance dbItemInventoryInstance, String accessToken) {
        super(dbItemInventoryInstance, accessToken);
        this.taskFilter = taskFilter;
    }

    public String getLog() throws Exception {
        SOSHibernateFactory sosHibernateFactory = Globals.getConnection(taskFilter.getJobschedulerId());
        SOSHibernateConnection connection = new SOSHibernateStatelessConnection(sosHibernateFactory);

        connection.beginTransaction();
        try {
            JobSchedulerTaskHistoryDBLayer jobSchedulerTaskHistoryDBLayer = new JobSchedulerTaskHistoryDBLayer(connection);
            String log = jobSchedulerTaskHistoryDBLayer.getLogAsString(taskFilter.getTaskId());
            if (log == null) {
                log = getTaskLogFromXmlCommand();
            }
            return log;
        } catch (Exception e) {
            throw e;
        } finally {
            connection.rollback();
        }
    }

    private String getTaskLogFromXmlCommand() throws Exception {

        String xml = String.format("<show_task id=\"%1$s\" what=\"log\" />", taskFilter.getTaskId());
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePostWithThrowBadRequest(xml, getAccessToken());
        return jocXmlCommand.getSosxml().selectSingleNodeValue(XPATH_TASK_LOG, null);
    }
}
