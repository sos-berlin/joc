package com.sos.joc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JocCockpitProperties;
import com.sos.joc.classes.JocWebserviceDataContainer;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;

public class Globals {

    private static final String HIBERNATE_CONFIGURATION_FILE = "hibernate_configuration_file";
    private static final String HIBERNATE_CONFIGURATION_SCHEDULER_DEFAULT_FILE = "hibernate_configuration_scheduler_default_file";
    private static final Logger LOGGER = LoggerFactory.getLogger(Globals.class);
    public static final String SESSION_KEY_FOR_USED_HTTP_CLIENTS_BY_EVENTS = "event_http_clients";
    public static final String SESSION_KEY_FOR_SEND_EVENTS_IMMEDIATLY = "send_events_immediatly";
    public static final String DEFAULT_SHIRO_INI_PATH = "classpath:shiro.ini";
    public static SOSHibernateFactory sosHibernateFactory;
    public static Map<String, SOSHibernateFactory> sosSchedulerHibernateFactories;
    public static JocCockpitProperties sosShiroProperties;
    public static Map<String, DBItemInventoryInstance> urlFromJobSchedulerId = new HashMap<String, DBItemInventoryInstance>();
    public static Map<String, Boolean> jobSchedulerIsRunning = new HashMap<String, Boolean>();
    public static int httpConnectionTimeout = 2000;
    public static int httpSocketTimeout = 2000;
    public static boolean withHostnameVerification = false;
    public static boolean auditLogCommentsAreRequired = false;
    public static JocWebserviceDataContainer jocWebserviceDataContainer = JocWebserviceDataContainer.getInstance();
    public static Map<String, Boolean> sendEventImmediately = new HashMap<String, Boolean>();

    public static SOSHibernateFactory getHibernateFactory() throws JocException {
        if (sosHibernateFactory == null) {
            try {
                String confFile = getConfFile(null);
                sosHibernateFactory = new SOSHibernateFactory(confFile);
                sosHibernateFactory.addClassMapping(DBLayer.getInventoryClassMapping());
                sosHibernateFactory.addClassMapping(DBLayer.getReportingClassMapping());
                sosHibernateFactory.setAutoCommit(true);
                sosHibernateFactory.build();
            } catch (JocException e) {
                sosHibernateFactory = null;
                throw e;
            } catch (Exception e) {
                sosHibernateFactory = null;
                throw new DBConnectionRefusedException(e);
            }
        }
        return sosHibernateFactory;
    }

    public static SOSHibernateFactory getHibernateFactory(String schedulerId) throws JocException {
        if (sosSchedulerHibernateFactories == null) {
            sosSchedulerHibernateFactories = new HashMap<String, SOSHibernateFactory>();
        }
        SOSHibernateFactory sosHibernateFactory = sosSchedulerHibernateFactories.get(schedulerId);

        if (sosHibernateFactory == null) {
            try {
                String confFile = getConfFile(schedulerId);
                sosHibernateFactory = new SOSHibernateFactory(confFile);
                sosHibernateFactory.addClassMapping(DBLayer.getSchedulerClassMapping());
                sosHibernateFactory.setAutoCommit(true);
                sosHibernateFactory.build();
                sosSchedulerHibernateFactories.put(schedulerId, sosHibernateFactory);
            } catch (JocException e) {
                throw e;
            } catch (Exception e) {
                throw new DBConnectionRefusedException(e);
            }
        }

        return sosHibernateFactory;
    }

    public static SOSHibernateSession createSosHibernateStatelessConnection(String identifier) throws JocException {
        if (sosHibernateFactory == null) {
            getHibernateFactory();
        }
        try {
            SOSHibernateSession sosHibernateSession = sosHibernateFactory.openStatelessSession(identifier);
            return sosHibernateSession;
        } catch (Exception e) {
            throw new DBConnectionRefusedException(e);
        }
    }

    public static String getShiroIniInClassPath() {
        if (sosShiroProperties != null) {
            return "classpath:" + sosShiroProperties.getPropertiesFileClassPathParent() + "shiro.ini";
        }
        return DEFAULT_SHIRO_INI_PATH;
    }

    public static void setProperties() throws JocException {
        setJobSchedulerConnectionTimeout();
        setJobSchedulerSocketTimeout();
        setHostnameVerification();
        setForceCommentsForAuditLog();
        setTrustStore();
    }

    public static void beginTransaction(SOSHibernateSession connection) {
        try {
            if (connection != null) {
                connection.beginTransaction();
            }
        } catch (Exception e) {
        }
    }

    public static void rollback(SOSHibernateSession connection) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (Exception e) {
        }
    }

    public static void commit(SOSHibernateSession connection) {
        try {
            if (connection != null) {
                connection.commit();
            }
        } catch (Exception e) {
        }
    }

    public static void forceClosingHttpClients(Session session) {
        forceClosingHttpClients(session, SESSION_KEY_FOR_USED_HTTP_CLIENTS_BY_EVENTS);
    }

    @SuppressWarnings("unchecked")
    public static void forceClosingHttpClients(Session session, String sessionKey) {
        try {
            if (session != null && session.getAttribute(sessionKey) != null) {
                try {
                    for (JOCJsonCommand command : (List<JOCJsonCommand>) session.getAttribute(sessionKey)) {
                        command.forcedClosingHttpClient();
                    }
                    //session.removeAttribute(sessionKey);
                } catch (Exception e) {
                }
            }
        } catch (InvalidSessionException e) {
        }
    }

    private static String getConfFile(String schedulerId) throws JocException {
        String confFile = null;
        JocError error = new JocError();
        error.setCode("JOC-003");
        String propertyKey = null;

        if (sosShiroProperties == null) {
            error.setMessage("sosShiroProperties are not initialized");
            throw new JocException(error);
        }

        if (schedulerId != null) {
            propertyKey = HIBERNATE_CONFIGURATION_FILE + "_" + schedulerId;
            confFile = sosShiroProperties.getProperty(propertyKey);

            if (confFile == null) {
                propertyKey = HIBERNATE_CONFIGURATION_SCHEDULER_DEFAULT_FILE;
                confFile = sosShiroProperties.getProperty(propertyKey);
            }
        }

        if (confFile == null) {
            propertyKey = HIBERNATE_CONFIGURATION_FILE;
            confFile = sosShiroProperties.getProperty(propertyKey, "reporting.hibernate.cfg.xml");
        }

        if (confFile != null) {
            confFile = confFile.trim();
        }

        Path p = sosShiroProperties.resolvePath(confFile);
        if (p != null) {
            if (!Files.exists(p)) {
                error.setMessage(String.format("hibernate configuration (%1$s) is set but file (%2$s) not found.", confFile, p.toString()));
                throw new JocException(error);
            } else {
                confFile = p.toString().replace('\\', '/');
            }
        }
        return confFile;
    }

    private static void setJobSchedulerConnectionTimeout() {
        int defaultSeconds = 2;
        if (sosShiroProperties != null) {
            int seconds = sosShiroProperties.getProperty("jobscheduler_connection_timeout", defaultSeconds);
            httpConnectionTimeout = seconds * 1000;
            LOGGER.info("HTTP(S) connection timeout = " + seconds);
        }
    }

    private static void setJobSchedulerSocketTimeout() {
        int defaultSeconds = 2;
        if (sosShiroProperties != null) {
            int seconds = sosShiroProperties.getProperty("jobscheduler_socket_timeout", defaultSeconds);
            httpSocketTimeout = seconds * 1000;
            LOGGER.info("HTTP(S) socket timeout = " + seconds);
        }
    }

    private static void setHostnameVerification() {
        boolean defaultVerification = false;
        if (sosShiroProperties != null) {
            withHostnameVerification = sosShiroProperties.getProperty("https_with_hostname_verification", defaultVerification);
            LOGGER.info("HTTPS with hostname verification in certicate = " + withHostnameVerification);
        }
    }

    private static void setTrustStore() throws JocException {
        if (sosShiroProperties != null) {
            JocError error = new JocError();
            error.setCode("JOC-311");
            String truststore = sosShiroProperties.getProperty("truststore_path", "");
            if (truststore != null && !truststore.trim().isEmpty()) {
                Path p = sosShiroProperties.resolvePath(truststore.trim());
                if (p != null) {
                    if (!Files.exists(p)) {
                        error.setMessage(String.format("truststore path (%1$s) is set but file (%2$s) not found.", truststore, p.toString()));
                        // throw new JocException(error);
                        LOGGER.error(error.getMessage());
                    } else {
                        truststore = p.toString();
                        System.setProperty("javax.net.ssl.trustStore", truststore);
                    }
                }
            }
        }
    }

    private static void setForceCommentsForAuditLog() {
        boolean defaultForceCommentsForAuditLog = false;
        if (sosShiroProperties != null) {
            auditLogCommentsAreRequired = sosShiroProperties.getProperty("force_comments_for_audit_log", defaultForceCommentsForAuditLog);
            LOGGER.info("force comments for audit log = " + auditLogCommentsAreRequired);
        }
    }

    public static void forceRollback(Object object) {
    }

    public static void disconnect(SOSHibernateSession sosHibernateSession) {
        if (sosHibernateSession != null) {
            sosHibernateSession.close();
        }
    }
}
