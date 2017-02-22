package com.sos.joc.classes;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.scheduler.misc.ParameterSubstitutor;

public class JocCockpitProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(JocCockpitProperties.class);
    private Properties properties = new Properties();
    private String propertiesFile = "/joc/joc.properties";
    private ParameterSubstitutor parameterSubstitutor = new ParameterSubstitutor();

    public JocCockpitProperties() {
        readProperties();
        setLog4JConfiguration();
    }
    
    public JocCockpitProperties(String propertiesFile) {
        this.propertiesFile = propertiesFile;
        readProperties();
        setLog4JConfiguration();
    }

    public Properties getProperties() {
        return properties;
    }

    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }
    
    public String getPropertiesFile() {
        return propertiesFile;
    }
    
    public String getPropertiesFileClassPathParent() {
        Path p = Paths.get(propertiesFile).getParent();
        String parent = (p != null) ? p.toString().replace('\\', '/') + "/" : "";
        return parent.replaceFirst("^/", "");
    }

    public String getProperty(String property) {
        String s = properties.getProperty(property);
        if (s != null){
            s = parameterSubstitutor.replaceEnvVars(s);
            s = parameterSubstitutor.replace(s);
        }
        return s;
    }
    
    public String getProperty(String property, String defaultValue) {
        String s = getProperty(property);
        if (s == null){
            return defaultValue;
        } else {
            return s;
        }
    }

    public int getProperty(String property, int defaultValue) {
        String s = getProperty(property);
        if (s == null){
            return defaultValue;
        } else {
            try{
                return Integer.parseInt(s);
            } catch (NumberFormatException e){
                LOGGER.warn(String.format("Property value for %1$s is not an Integer. Returning default %2$s: %3$s", property, defaultValue, e.getMessage()));
                return defaultValue;
            }
        }
    }
    
    public boolean getProperty(String property, boolean defaultValue) {
        String s = getProperty(property);
        if (s == null){
            return defaultValue;
        } else {
            try{
                return Boolean.parseBoolean(s);
            } catch (Exception e){
                LOGGER.warn(String.format("Property value for %1$s could not be parsed to boolean. Returning default %2$s: %3$s", property, defaultValue, e.getMessage()));
                return defaultValue;
            }
        }
    }
    
    public Path resolvePath(String path) {
        if (path != null) {
            Path p = getResourceDir();
            if (p != null) {
                p = p.resolve(path).normalize();
                LOGGER.info(String.format("Resolved path of %1$s = %2$s",path, p.toString().replace('\\', '/')));
                return p;
            }
        }
        return null;
    }
    
//    public void setAuditLogFile() {
//        String propKeyAuditLog = "audit_log_file";
//        System.setProperty(propKeyAuditLog, getProperty(propKeyAuditLog, System.getProperty("java.io.tmpdir")+"/JOCAuditLog.log"));
//    }
//    
//    public void setLogLevel() {
//        String propKeyLogLevel = "log_level";
//        String logLevel = getProperty(propKeyLogLevel, null);
//        if (logLevel != null) {
//            try {
//                switch (logLevel.toLowerCase()) {
//                case "error" : org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.ERROR); break;
//                case "info"  : org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO); break;
//                case "warn"  : org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.WARN); break;
//                case "debug" : org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.DEBUG); break;
//                case "trace" : org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.TRACE); break;
//                }
//             } catch(Exception e) {
//                 LOGGER.warn("",e);
//             }
//        }
//    }

    private void setLog4JConfiguration() {
        String propKeyLog4J = "log4j.configuration"; 
        //String sysLog4JProp = System.getProperty(propKeyLog4J);
        //if (sysLog4JProp == null || sysLog4JProp.isEmpty()) {
            String log4jConf = getProperty(propKeyLog4J, "log4j.properties");
            Path p = resolvePath(log4jConf);
            if (p != null) {
                if (Files.exists(p)) {
                    //System.setProperty(propKeyLog4J, p.toUri().toString());
                    try {
                        org.apache.log4j.LogManager.resetConfiguration();
                        org.apache.log4j.PropertyConfigurator.configure(p.toUri().toURL());
                    } catch (Exception e) {
                        LOGGER.warn("",e);
                    }
                    //LOGGER.info(propKeyLog4J + "=" + System.getProperty(propKeyLog4J));
                } else {
                    LOGGER.warn(String.format("%1$s=%2$s is set but file (%3$s) not found.", propKeyLog4J, log4jConf, p.toString()));
                }
            }
        //}
    }
    
    private void substituteProperties() {
        parameterSubstitutor = new ParameterSubstitutor();
        for (Map.Entry<Object, Object> e : properties.entrySet()) {
            String key = (String) e.getKey();
            String value = (String) e.getValue();
            parameterSubstitutor.addKey(key, value);
        }
    }
    
    private Path getResourceDir() {
        try {
            Path parentDirOfPropFilePath = Paths.get(propertiesFile).getParent();
            String parentDirOfPropFile = "/";
            if (parentDirOfPropFilePath != null && parentDirOfPropFilePath.getNameCount() != 0) {
                parentDirOfPropFile = parentDirOfPropFilePath.toString().replace('\\', '/');
            }
            URL url = this.getClass().getResource(parentDirOfPropFile);
            if (url != null) {
                Path p = Paths.get(url.toURI());
                if (Files.exists(p)) {
                    return p;
                } else {
                    LOGGER.error("Cannot determine resource path");
                }
            }
        } catch (Exception e) {
            LOGGER.error("Cannot determine resource path", e);
        }
        return null;
    }

    private void readProperties() {
        InputStream stream = null;
        InputStreamReader streamReader = null;
        try {
            stream = this.getClass().getResourceAsStream(propertiesFile);
            if (stream != null) {
                streamReader = new InputStreamReader(stream, "UTF-8");
                properties.load(streamReader);
                substituteProperties();
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Error while reading %1$s:", propertiesFile), e);
        } finally {
            try {
                if(stream != null) {
                    stream.close();
                }
            } catch (Exception e) {
            }
            try {
                if(streamReader != null) {
                    streamReader.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
