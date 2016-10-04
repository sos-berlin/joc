package com.sos.joc.classes;

import java.util.Scanner;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.joc.Globals;
import com.sos.joc.db.history.order.JobSchedulerOrderHistoryDBLayer;
import com.sos.joc.model.order.OrderFilterWithHistoryIdSchema;

public class LogContent {
    
    private static final String SPAN_LINE = "<span class=%s>%s</span>";
    private static final String HTML_END = "</pre></body></html>";
    private static final String HTML_BEGIN = "<html><head><style type='text/css'>@import 'scheduler.css';pre { font-family: Lucida Console, monospace; font-size: 10pt }</style><title>Scheduler log</title></head><body class='log'><script type='text/javascript'></script><script type='text/javascript' src='show_log.js'></script><pre class='log'>";
    private static final String DEBUG_MARKER = "[debug]";
    private static final String DEBUG2_MARKER = "[debug2]";
    private static final String DEBUG3_MARKER = "[debug3]";
    private static final String DEBUG4_MARKER = "[debug4]";
    private static final String DEBUG5_MARKER = "[debug5]";
    private static final String DEBUG6_MARKER = "[debug6]";
    private static final String DEBUG7_MARKER = "[debug7]";
    private static final String DEBUG8_MARKER = "[debug8]";
    private static final String DEBUG9_MARKER = "[debug9]";
    private static final String WARN_MARKER = "[WARN]";
    private static final String ERROR_MARKER = "[ERROR]";
    private static final String INFO_MARKER = "[info]";
    private static final String LOG_DEBUG_CLASS = "log_debug";
    private static final String LOG_DEBUG2_CLASS = "log_debug2";
    private static final String LOG_DEBUG3_CLASS = "log_debug3";
    private static final String LOG_DEBUG4_CLASS = "log_debug4";
    private static final String LOG_DEBUG5_CLASS = "log_debug5";
    private static final String LOG_DEBUG6_CLASS = "log_debug6";
    private static final String LOG_DEBUG7_CLASS = "log_debug7";
    private static final String LOG_DEBUG8_CLASS = "log_debug8";
    private static final String LOG_DEBUG9_CLASS = "log_debug9";
    private static final String LOG_ERROR_CLASS = "log_error";
    private static final String LOG_WARN_CLASS = "log_warn";
    private static final String LOG_INFO_CLASS = "log_info";
    private static final String LOG_STDERR_CLASS = "log_stderr";

    OrderFilterWithHistoryIdSchema orderFilterWithHistoryIdSchema;
    
    public LogContent(OrderFilterWithHistoryIdSchema orderFilterWithHistoryIdSchema) {
        super();
        this.orderFilterWithHistoryIdSchema = orderFilterWithHistoryIdSchema;
    }

    private String colorLine(String line, String marker, String cssClass) {

        String s = line;
        if (line.contains(marker)) {
            s = String.format(SPAN_LINE, cssClass, line);
        }
        return s;

    }

    private String addStyle(String line) {

        line = colorLine(line, ERROR_MARKER, LOG_ERROR_CLASS);
        line = colorLine(line, WARN_MARKER, LOG_WARN_CLASS);
        line = colorLine(line, DEBUG_MARKER, LOG_DEBUG_CLASS);
        line = colorLine(line, DEBUG2_MARKER, LOG_DEBUG2_CLASS);
        line = colorLine(line, DEBUG3_MARKER, LOG_DEBUG3_CLASS);
        line = colorLine(line, DEBUG4_MARKER, LOG_DEBUG4_CLASS);
        line = colorLine(line, DEBUG5_MARKER, LOG_DEBUG5_CLASS);
        line = colorLine(line, DEBUG6_MARKER, LOG_DEBUG6_CLASS);
        line = colorLine(line, DEBUG7_MARKER, LOG_DEBUG7_CLASS);
        line = colorLine(line, DEBUG8_MARKER, LOG_DEBUG8_CLASS);
        line = colorLine(line, DEBUG9_MARKER, LOG_DEBUG9_CLASS);
        line = colorLine(line, INFO_MARKER, LOG_INFO_CLASS);
        return line;
    }


    private String colouredLog(String log){
        Scanner scanner = new Scanner(log);
        StringBuilder s = new StringBuilder();
        while (scanner.hasNextLine()) {
            s.append(addStyle(scanner.nextLine()));
        }
        scanner.close();

        return s.toString();
    
    }
    
    
    public String htmlWithColouredLogContent(String log){
        StringBuilder s = new StringBuilder();
        s.append(colouredLog(log));
        return s.toString();
    }
    
    public String htmlPageWithColouredLogContent(String log) {
        StringBuilder s = new StringBuilder();

        s.append(HTML_BEGIN);
        s.append(colouredLog(log));
        s.append(HTML_END);

        return s.toString();
    }
    
    public String getOrderLog() throws Exception{
        SOSHibernateConnection sosHibernateConnection = Globals.getConnection(orderFilterWithHistoryIdSchema.getJobschedulerId());
        sosHibernateConnection.beginTransaction();
        JobSchedulerOrderHistoryDBLayer jobSchedulerOrderHistoryDBLayer = new JobSchedulerOrderHistoryDBLayer(sosHibernateConnection);
        String log = jobSchedulerOrderHistoryDBLayer.getLogAsString(orderFilterWithHistoryIdSchema.getHistoryId());
        if (log==null){
            log = getOrderLogFromXmlCommand();
        }
        sosHibernateConnection.rollback();
        return log;
    }
    
    private String getOrderLogFromXmlCommand(){
        return "";
    }
    
}



