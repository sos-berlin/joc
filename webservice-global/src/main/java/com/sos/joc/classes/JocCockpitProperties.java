package com.sos.joc.classes;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;

import sos.util.ParameterSubstitutor;

 
public class JocCockpitProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(JocCockpitProperties.class);
    private Properties properties = new Properties();
    private String propertiesFile = "/joc/joc.properties";
    private Path propertiesPath;
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
    
    public JocCockpitProperties(Path propertiesPath) {
        this.propertiesPath = propertiesPath;
        readPropertiesFromPath();
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
        String parent = "";
        if (p != null){
             parent =  p.toString().replace('\\', '/') + "/";
         }
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
    
    public DBItemInventoryInstance setUrlMapping(DBItemInventoryInstance instance) {
        return setUrlMapping(instance, false);
    }
    
    public DBItemInventoryInstance setUrlMapping(DBItemInventoryInstance instance, boolean verbose) {
        if (instance != null) {
            String urlMappingKey = String.format("jobscheduler_url_%s_%d", instance.getHostname().toLowerCase(), instance.getPort());
            String urlFromConfFile = getProperty(urlMappingKey);
            if (urlFromConfFile != null && !urlFromConfFile.isEmpty()) {
                if (verbose && !urlFromConfFile.equals(instance.getUrl())) {
                    LOGGER.info(String.format("JobScheduler url mapped %s -> %s", instance.getUrl(), urlFromConfFile));
                }
                if (instance.originalUrl() == null) {
                    instance.setOriginalUrl(instance.getUrl());
                }
                instance.setUrl(urlFromConfFile);
            } else if (instance.originalUrl() != null) {
                if (verbose) {
                    LOGGER.info(String.format("JobScheduler url remapped %s -> %s", instance.getUrl(), instance.originalUrl())); 
                }
                instance.setUrl(instance.originalUrl());
                instance.setOriginalUrl(null);
            }
        }
        return instance;
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
    
    public long getProperty(String property, long defaultValue) {
        String s = getProperty(property);
        if (s == null){
            return defaultValue;
        } else {
            try{
                return Long.parseLong(s);
            } catch (NumberFormatException e){
                LOGGER.warn(String.format("Property value for %1$s is not a Long. Returning default %2$s: %3$s", property, defaultValue, e.getMessage()));
                return defaultValue;
            }
        }
    }
    
    public long getFileSizeProperty(String property, long defaultValue) {
        String s = getProperty(property);
        if (s == null) {
            return defaultValue;
        } else {
            try {
                if (!s.trim().toUpperCase().matches("\\d+(K|M|G)?B?")) {
                    throw new NumberFormatException("");
                }
                long value = Long.parseLong(s.trim().replaceFirst("\\D*$", ""));
                String unit = s.toUpperCase().replaceFirst("^\\d+", "").replaceFirst("B$", "").trim();
                switch (unit) {
                case "K":
                    return value * 1024;
                case "M":
                    return value * 1024 * 1024;
                case "G":
                    return value * 1024 * 1024 * 1024;
                default:
                    return value;
                }
            } catch (NumberFormatException e) {
                LOGGER.warn(String.format("Property value for %1$s is not of format [0-9]+(K|M|G)?B?. Returning default %2$s", property,
                        defaultValue));
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
    
    private void setLog4JConfiguration() {
        String propKeyLog4J = "log4j.configuration";
        String log4jConf = getProperty(propKeyLog4J);
        if (log4jConf != null) {
            Path p = resolvePath(log4jConf);
            if (p != null) {
                if (Files.exists(p)) {
                    try {
                        LoggerContext context = (LoggerContext) LogManager.getContext(false);
                        context.setConfigLocation(p.toUri());
                    } catch (Exception e) {
                        LOGGER.warn("", e);
                    }
                } else {
                    LOGGER.warn(String.format("%1$s=%2$s is set but file (%3$s) not found.", propKeyLog4J, log4jConf, p.toString()));
                }
            }
        }
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
    
    private void readPropertiesFromPath() {
        InputStream stream = null;
        InputStreamReader streamReader = null;
        try {
            if (propertiesPath != null) {
                stream = new FileInputStream(propertiesPath.toFile());
                if (stream != null) {
                    streamReader = new InputStreamReader(stream, "UTF-8");
                    properties.load(streamReader);
                    substituteProperties();
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Error while reading %1$s:", propertiesPath.toString()), e);
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
