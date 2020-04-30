package com.sos.joc;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.json.Json;
import javax.json.JsonReader;
import javax.ws.rs.core.UriInfo;

import org.apache.shiro.config.Ini;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sos.auth.rest.SOSShiroCurrentUser;
import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateConfigurationException;
import com.sos.hibernate.exceptions.SOSHibernateFactoryBuildException;
import com.sos.hibernate.exceptions.SOSHibernateOpenSessionException;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JocCockpitProperties;
import com.sos.joc.classes.JocWebserviceDataContainer;
import com.sos.joc.exceptions.DBOpenSessionException;
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.exceptions.JocException;

public class Globals {

    private static final String SHIRO_INI_FILENAME = "shiro.ini";
    private static final String HIBERNATE_CONFIGURATION_FILE = "hibernate_configuration_file";
    private static final String HIBERNATE_CONFIGURATION_SCHEDULER_DEFAULT_FILE = "hibernate_configuration_scheduler_default_file";
    private static final Logger LOGGER = LoggerFactory.getLogger(Globals.class);
    private static String trustStoreLocationDefault = "?????";
    private static String trustStorePasswordDefault = "?????";
    public static final String SESSION_KEY_FOR_SEND_EVENTS_IMMEDIATLY = "send_events_immediatly";
    public static final String DEFAULT_SHIRO_INI_PATH = "classpath:shiro.ini";
    public static final String DEFAULT_SHIRO_INI_FILENAME = "shiro.ini";
    public static SOSHibernateFactory sosHibernateFactory;
    public static Map<String, SOSHibernateFactory> sosSchedulerHibernateFactories;
    public static JocCockpitProperties sosShiroProperties;
    public static Map<String, DBItemInventoryInstance> urlFromJobSchedulerId = new HashMap<String, DBItemInventoryInstance>();
    public static Map<String, Boolean> jobSchedulerIsRunning = new HashMap<String, Boolean>();
    public static int httpConnectionTimeout = 2000;
    public static int httpSocketTimeout = 2000;
    public static boolean withHostnameVerification = false;
    public static boolean auditLogCommentsAreRequired = false;
    public static long maxSizeOfLogsToDisplay = 1024 * 1024 * 10L; // 10MB
    public static JocWebserviceDataContainer jocWebserviceDataContainer = JocWebserviceDataContainer.getInstance();
    public static JocCockpitProperties jocConfigurationProperties;
    public static IniSecurityManagerFactory factory = null;
    public static long timeoutToDeleteTempFiles = 1000 * 60 * 3L;
    public static TimeZone jocTimeZone = TimeZone.getDefault();
    public static boolean rollbackJobHistoryWithJSON = false;
    public static boolean rollbackJobChainWithJSON = false;
    public static ObjectMapper xmlMapper = new XmlMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(
            SerializationFeature.INDENT_OUTPUT, true);
    public static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    public static String servletContextContextPath = null; // /joc
    public static Path servletContextRealPath = null;
    public static URI servletBaseUri = null;
    public static Map<String,String> schedulerVariables=null;
    
    public static SOSHibernateFactory getHibernateFactory() throws JocConfigurationException {
        if (sosHibernateFactory == null || sosHibernateFactory.getSessionFactory() == null) {
            try {
                String confFile = getHibernateConfFile(null);
                sosHibernateFactory = new SOSHibernateFactory(confFile);
                sosHibernateFactory.addClassMapping(DBLayer.getInventoryClassMapping());
                sosHibernateFactory.addClassMapping(DBLayer.getReportingClassMapping());
                sosHibernateFactory.addClassMapping(DBLayer.getJobStreamClassMapping());
                sosHibernateFactory.addClassMapping(DBLayer.getYadeClassMapping());
                sosHibernateFactory.setAutoCommit(true);
                sosHibernateFactory.build();
            } catch (SOSHibernateConfigurationException | SOSHibernateFactoryBuildException e) {
                sosHibernateFactory = null;
                throw new JocConfigurationException(e);
            }
        }
        return sosHibernateFactory;
    }

    public static SOSHibernateFactory getHibernateFactory(String schedulerId) throws JocConfigurationException {
        if (sosSchedulerHibernateFactories == null) {
            sosSchedulerHibernateFactories = new HashMap<String, SOSHibernateFactory>();
        }
        SOSHibernateFactory sosHibernateFactory = sosSchedulerHibernateFactories.get(schedulerId);

        if (sosHibernateFactory == null || sosHibernateFactory.getSessionFactory() == null) {
            try {
                String confFile = getHibernateConfFile(schedulerId);
                sosHibernateFactory = new SOSHibernateFactory(confFile);
                sosHibernateFactory.addClassMapping(DBLayer.getSchedulerClassMapping());
                sosHibernateFactory.setAutoCommit(true);
                sosHibernateFactory.build();
                sosSchedulerHibernateFactories.put(schedulerId, sosHibernateFactory);
            } catch (SOSHibernateConfigurationException | SOSHibernateFactoryBuildException e) {
                sosHibernateFactory = null;
                throw new JocConfigurationException(e);
            }
        }
        return sosHibernateFactory;
    }

    public static SOSHibernateSession createSosHibernateStatelessConnection(String identifier) throws JocConfigurationException,
            DBOpenSessionException {
        try {
            getHibernateFactory();
            SOSHibernateSession sosHibernateSession = sosHibernateFactory.openStatelessSession(identifier);
            return sosHibernateSession;
        } catch (SOSHibernateOpenSessionException e) {
            throw new DBOpenSessionException(e);
        }
    }

    public static IniSecurityManagerFactory getShiroIniSecurityManagerFactory() {
        String iniFile = getShiroIniInClassPath();
        if (factory == null) {
            factory = new IniSecurityManagerFactory(getIniFileForShiro(iniFile));
        } else {
            Ini oldShiroIni = factory.getIni();
            Ini currentShiroIni = Ini.fromResourcePath(getIniFileForShiro(iniFile));
            if (!oldShiroIni.equals(currentShiroIni)) {
                LOGGER.debug(getIniFileForShiro(iniFile) + " is changed");
                factory = new IniSecurityManagerFactory();
                factory.setIni(currentShiroIni);
            }
        }
        return factory;
    }

    public static Ini getIniFromSecurityManagerFactory() {
        if (factory == null) {
            String iniFile = getShiroIniInClassPath();
            factory = new IniSecurityManagerFactory(getIniFileForShiro(iniFile));
        }
        return factory.getIni();
    }

    public static String getIniFileForShiro(String iniFile) {
        return iniFile + ".active";
    }

    public static String getShiroIniInClassPath() {
        if (sosShiroProperties != null) {
            Path p = sosShiroProperties.resolvePath(SHIRO_INI_FILENAME);
            return "file:" + p.toString().replace('\\', '/');
        }
        return DEFAULT_SHIRO_INI_PATH;
    }

    public static Path getShiroIniFile() {
        if (sosShiroProperties != null) {
            return sosShiroProperties.resolvePath(getIniFileForShiro(SHIRO_INI_FILENAME));
        }
        return Paths.get(getIniFileForShiro(DEFAULT_SHIRO_INI_FILENAME));
    }

    public static void setProperties() throws JocException {
        readVersion();
        setJobSchedulerConnectionTimeout();
        setJobSchedulerSocketTimeout();
        setHostnameVerification();
        setForceCommentsForAuditLog();
        setRollbackJobHistoryWithJSON();
        setRollbackJobChainyWithJSON();
        setTrustStore();
        setTrustStoreType();
        setTrustStorePassword();
        // setMaxSizeOfLogsToDisplay();
        setTimeoutForTempFiles();
        setConfigurationProperties();
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

    public static void forceClosingHttpClients(SOSShiroCurrentUser sosShiroCurrentUser, String accessToken) {
        if (sosShiroCurrentUser != null) {
            try {
                for (JOCJsonCommand command : sosShiroCurrentUser.getJocJsonCommands()) {
                    command.forcedClosingHttpClient();
                }
            } catch (Exception e) {
            }
        }
    }

    private static String getHibernateConfFile(String schedulerId) throws JocConfigurationException {
        String confFile = null;
        String propKey = null;

        if (sosShiroProperties == null) {
            throw new JocConfigurationException("sosShiroProperties are not initialized");
        }

        if (schedulerId != null && !schedulerId.isEmpty()) {
            propKey = HIBERNATE_CONFIGURATION_FILE + "_" + schedulerId;
            confFile = sosShiroProperties.getProperty(propKey);

            if (confFile == null) {
                propKey = HIBERNATE_CONFIGURATION_SCHEDULER_DEFAULT_FILE;
                confFile = sosShiroProperties.getProperty(propKey);
            }

            if (confFile == null || confFile.trim().isEmpty()) {
                throw new JocConfigurationException(String.format("Neither property '%1$s' nor '%2$s' found in %3$s",
                        HIBERNATE_CONFIGURATION_SCHEDULER_DEFAULT_FILE, HIBERNATE_CONFIGURATION_FILE + "_" + schedulerId, sosShiroProperties
                                .getPropertiesFile()));
            }
        } else {
            confFile = sosShiroProperties.getProperty(HIBERNATE_CONFIGURATION_FILE, "reporting.hibernate.cfg.xml");
            if (confFile.trim().isEmpty()) {
                throw new JocConfigurationException(String.format("Property '%1$s' not found in %2$s", HIBERNATE_CONFIGURATION_FILE,
                        sosShiroProperties.getPropertiesFile()));
            }
        }

        confFile = confFile.trim();
        Path p = sosShiroProperties.resolvePath(confFile);
        if (p != null) {
            if (!Files.exists(p) || Files.isDirectory(p)) {
                throw new JocConfigurationException(String.format("hibernate configuration (%1$s) is set but file (%2$s) not found.", confFile, p
                        .toString()));
            } else {
                confFile = p.toString().replace('\\', '/');
            }
        }
        return confFile;
    }

    private static void readVersion() {
        InputStream stream = null;
        String versionFile = "/version.json";
        LOGGER.info("Java version = " + System.getProperty("java.version"));
        try {
            stream = Globals.class.getClassLoader().getResourceAsStream(versionFile);
            if (stream != null) {
                JsonReader reader = Json.createReader(stream);
                LOGGER.info("JOC Cockpit version = " + reader.readObject().getString("version", "unknown"));
                reader.close();
            } else {
                LOGGER.warn(String.format("Version file %1$s not found in classpath", versionFile));
            }
        } catch (Exception e) {
            LOGGER.warn(String.format("Error while reading %1$s from classpath: ", versionFile), e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private static void setJobSchedulerConnectionTimeout() {
        int defaultSeconds = 2;
        if (sosShiroProperties != null) {
            int seconds = sosShiroProperties.getProperty("jobscheduler_connection_timeout", defaultSeconds);
            httpConnectionTimeout = seconds * 1000;
            LOGGER.info("HTTP(S) connection timeout = " + seconds + "s");
        }
    }

    private static void setJobSchedulerSocketTimeout() {
        int defaultSeconds = 5;
        if (sosShiroProperties != null) {
            int seconds = sosShiroProperties.getProperty("jobscheduler_socket_timeout", defaultSeconds);
            httpSocketTimeout = seconds * 1000;
            LOGGER.info("HTTP(S) socket timeout = " + seconds + "s");
        }
    }

    private static void setHostnameVerification() {
        boolean defaultVerification = false;
        if (sosShiroProperties != null) {
            withHostnameVerification = sosShiroProperties.getProperty("https_with_hostname_verification", defaultVerification);
            LOGGER.info("HTTPS with hostname verification in certificate = " + withHostnameVerification);
        }
    }

    private static void setTrustStore() throws JocException {
        if ("?????".equals(trustStoreLocationDefault)) {
            trustStoreLocationDefault = System.getProperty("javax.net.ssl.trustStore");
        }
        if (sosShiroProperties != null) {
            String truststore = sosShiroProperties.getProperty("truststore_path");
            if (truststore != null && !truststore.trim().isEmpty()) {
                Path p = sosShiroProperties.resolvePath(truststore.trim());
                if (p != null) {
                    if (!Files.exists(p)) {
                        LOGGER.error(String.format("truststore path (%1$s) is set but file (%2$s) not found.", truststore, p.toString()));
                    } else {
                        truststore = p.toString();
                        System.setProperty("javax.net.ssl.trustStore", truststore);
                    }
                }
            } else {
                if (trustStoreLocationDefault == null) {
                    System.clearProperty("javax.net.ssl.trustStore");
                } else {
                    System.setProperty("javax.net.ssl.trustStore", trustStoreLocationDefault);
                }
            }
        }
    }

    private static void setTrustStoreType() throws JocException {
        if (sosShiroProperties != null) {
            String truststoreType = sosShiroProperties.getProperty("truststore_type", KeyStore.getDefaultType());
            System.setProperty("javax.net.ssl.trustStoreType", truststoreType);
        }
    }

    private static void setTrustStorePassword() throws JocException {
        if ("?????".equals(trustStorePasswordDefault)) {
            trustStorePasswordDefault = System.getProperty("javax.net.ssl.trustStorePassword");
        }
        if (sosShiroProperties != null) {
            String truststorePassw = sosShiroProperties.getProperty("truststore_password");
            if (truststorePassw != null) {
                System.setProperty("javax.net.ssl.trustStorePassword", truststorePassw);
            } else {
                if (trustStorePasswordDefault == null) {
                    System.clearProperty("javax.net.ssl.trustStorePassword");
                } else {
                    System.setProperty("javax.net.ssl.trustStorePassword", trustStorePasswordDefault);
                }
            }
        }
    }

    private static void setConfigurationProperties() throws JocException {
        if (sosShiroProperties != null) {
            String confFile = sosShiroProperties.getProperty("configuration_file", "");
            if (confFile != null && !confFile.trim().isEmpty()) {
                String defaultConfFile = "joc.configuration.properties";
                Path p = sosShiroProperties.resolvePath(confFile.trim());
                if (p != null) {
                    if (!Files.exists(p)) {
                        if (!confFile.equals(defaultConfFile)) {
                            LOGGER.error(String.format("configuration file (%1$s) is set but file (%2$s) not found.", confFile, p.toString()));
                        }
                    } else {
                        jocConfigurationProperties = new JocCockpitProperties(p);
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

    // rollback option for JS-1802; see https://sourceforge.net/p/jobscheduler/bugs/145/
    private static void setRollbackJobHistoryWithJSON() {
        boolean defaultRollbackJobHistoryWithJSON = false;
        if (sosShiroProperties != null) {
            rollbackJobHistoryWithJSON = sosShiroProperties.getProperty("disable_job_history_with_json", defaultRollbackJobHistoryWithJSON);
        }
    }

    // rollback option for JS-1795
    private static void setRollbackJobChainyWithJSON() {
        boolean defaultRollbackJobChainWithJSON = false;
        if (sosShiroProperties != null) {
            rollbackJobChainWithJSON = sosShiroProperties.getProperty("disable_jobchain_with_json", defaultRollbackJobChainWithJSON);
        }
    }

    // for JOC-483 cancelled but should be implement for 2.0
    // private static void setMaxSizeOfLogsToDisplay() {
    // long defaultMaxSizeOfLogsToDisplay = 1024 * 1024 * 10L;
    // if (sosShiroProperties != null) {
    // maxSizeOfLogsToDisplay = sosShiroProperties.getFileSizeProperty("max_size_of_logs_for_display", defaultMaxSizeOfLogsToDisplay);
    // LOGGER.info("max size of logs to display = " + maxSizeOfLogsToDisplay);
    // }
    // }

    private static void setTimeoutForTempFiles() {
        long defaultTimeout = 1000 * 60 * 3L;
        if (sosShiroProperties != null) {
            timeoutToDeleteTempFiles = sosShiroProperties.getProperty("timeout_to_delete_temp_files", defaultTimeout);
            // LOGGER.info("timeout to delete temp files = " + TIMEOUT_TO_DELETE_TEMP_FILES);
        }
    }

    public static void forceRollback(Object object) {
    }

    public static void disconnect(SOSHibernateSession sosHibernateSession) {
        if (sosHibernateSession != null) {
            sosHibernateSession.close();
        }
    }

    public static void closeFactory() {
        if (sosHibernateFactory != null) {
            sosHibernateFactory.close();
            sosHibernateFactory = null;
        }
    }

    public static String normalizePath(String path) {
        if (path == null) {
            return null;
        }
        return ("/" + path.trim()).replaceAll("//+", "/").replaceFirst("/$", "");
    }

    public static String getParent(String path) {
        Path p = Paths.get(path).getParent();
        if (p == null) {
            return null;
        } else {
            return p.toString().replace('\\', '/');
        }
    }

    public static void setServletBaseUri(UriInfo uriInfo) {
        // request.getServletPath=/api
        // request.getPathInfo=/xmleditor/validate
        // [JOC unzipped]request.getPathTranslated=D:\joc\data\webapps\joc\xmleditor\validate
        // [JOC as war] request.getPathTranslated=D:\joc\data\temp\jetty-0.0.0.0-4446-joc.war-_joc-any-7137785786755971224.dir\webapp\xmleditor\validate
        // request.getRequestURL=http://localhost:4446/joc/api/xmleditor/validate
        // request.getRequestURI=/joc/api/xmleditor/validate
        // request.getServletContext()=/joc
        //
        // JocXmlEditor.getBaseUri=http://localhost:4446/joc
        // return request.getRequestURL().substring(0, request.getRequestURL().length() - request.getRequestURI().length()) + request.getContextPath();

        // uriInfo.getAbsolutePath().toString()=http://localhost:4446/joc/api/xmleditor/validate
        // uriInfo.getAbsolutePath().getPath()=/joc/api/xmleditor/validate
        // uriInfo.getPath()= xmleditor/validate
        // uriInfo.getBaseUri().toString()=http://localhost:4446/joc/api/
        // uriInfo.getBaseUri().getPath()=/joc/api/
        // uriInfo.getRequestUri().getPath()=/joc/api/xmleditor/validate
        // uriInfo.getRequestUri().toString()=http://localhost:4446/joc/api/xmleditor/validate

        if (uriInfo == null) {
            return;
        }
        try {
            if (servletBaseUri == null) {
                if (servletContextContextPath == null) {
                    servletBaseUri = uriInfo.getBaseUri();
                } else {
                    String baseUri = uriInfo.getBaseUri().toString();
                    // baseUri = http://localhost:4446/joc/api/
                    // Globals.servletContextContextPath = /joc
                    LOGGER.debug(String.format("servletContextContextPath=%s, baseUri=%s", servletContextContextPath, baseUri));
                    int indx = baseUri.indexOf(servletContextContextPath);
                    if (indx > -1) {
                        baseUri = baseUri.substring(0, indx + servletContextContextPath.length());
                    }
                    servletBaseUri = new URI(baseUri + "/");
                }
                LOGGER.info("servletBaseUri=" + servletBaseUri);
            }
        } catch (Throwable e) {
            LOGGER.error(String.format("can't evaluate the base url: %s", e.toString()), e);
        }
    }

}
