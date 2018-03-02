package com.sos.joc.classes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateOpenSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.db.history.task.JobSchedulerTaskHistoryDBLayer;
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.model.job.TaskFilter;

public class LogTaskContent extends LogContent {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogTaskContent.class);
    private static final String XPATH_TASK_LOG = "/spooler/answer/task/log";
    private TaskFilter taskFilter;
    
    public LogTaskContent(TaskFilter taskFilter, DBItemInventoryInstance dbItemInventoryInstance, String accessToken) {
        super(dbItemInventoryInstance, accessToken);
        this.taskFilter = taskFilter;
    }
    
    public String getLog() throws Exception {

        String log = getLogFromDB();
        if (log == null) {
            log = getTaskLogFromXmlCommand();
        }
        return log;
    }

    private String getLogFromDB() {
        SOSHibernateSession sosHibernateSession = null;
        try {
            SOSHibernateFactory sosHibernateFactory = Globals.getHibernateFactory(taskFilter.getJobschedulerId());
            sosHibernateSession = sosHibernateFactory.openStatelessSession("getTaskLog");
            try {
                Globals.beginTransaction(sosHibernateSession);
                JobSchedulerTaskHistoryDBLayer jobSchedulerTaskHistoryDBLayer = new JobSchedulerTaskHistoryDBLayer(sosHibernateSession);
                return jobSchedulerTaskHistoryDBLayer.getLogAsString(taskFilter);
            } finally {
                Globals.rollback(sosHibernateSession);
            }
        } catch (JocConfigurationException | SOSHibernateOpenSessionException e) {
            LOGGER.warn(e.toString());
            return null;
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }
    
    private String getTaskLogFromXmlCommand() throws Exception {

        String xml = String.format("<show_task id=\"%1$s\" what=\"log\" />", taskFilter.getTaskId());
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePostWithThrowBadRequest(xml, getAccessToken());
        return jocXmlCommand.getSosxml().selectSingleNodeValue(XPATH_TASK_LOG, null);
    }
}
