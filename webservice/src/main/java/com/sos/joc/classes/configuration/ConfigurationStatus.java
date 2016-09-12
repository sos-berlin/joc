package com.sos.joc.classes.configuration;

import java.util.Optional;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import org.apache.xpath.CachedXPathAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.model.common.ConfigurationStatusSchema;

public class ConfigurationStatus {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationStatus.class);

    public static ConfigurationStatusSchema getConfigurationStatus() {
        ConfigurationStatusSchema confStatus = new ConfigurationStatusSchema();
        confStatus.setMessage("myMessage");
        confStatus.setSeverity(-1);
        confStatus.setText(ConfigurationStatusSchema.Text.CHANGED_FILE_NOT_LOADED);
        return confStatus;
    }

    /** @param element has to be an <order>...</order>, <job>...</job> ,
     *            <job_chain>...</job_chain> element etc. from a JobScheduler
     *            response
     * 
     * @return ConfigurationStatusSchema
     */
    public static ConfigurationStatusSchema getConfigurationStatus(Element element) {
        ConfigurationStatusSchema confStatus = new ConfigurationStatusSchema();
        CachedXPathAPI xPath = new CachedXPathAPI();
        try {
            Element fileBasedElement = (Element) xPath.selectSingleNode(element, "file_based/ERROR");
            if (fileBasedElement != null) {
                setSeverity(confStatus, ConfigurationStatusSchema.Text.ERROR_IN_CONFIGURATION_FILE);
                setMessage(confStatus, fileBasedElement);
                return confStatus;
            }
            fileBasedElement = (Element) xPath.selectSingleNode(element, "replacement/*/file_based/ERROR");
            if (fileBasedElement != null) {
                setSeverity(confStatus, ConfigurationStatusSchema.Text.CHANGED_FILE_NOT_LOADED);
                setMessage(confStatus, fileBasedElement);
                return confStatus;
            }
            fileBasedElement = (Element) xPath.selectSingleNode(element, "file_based/removed/ERROR");
            if (fileBasedElement != null) {
                setSeverity(confStatus, ConfigurationStatusSchema.Text.REMOVING_DELAYED);
                setMessage(confStatus, fileBasedElement);
                return confStatus;
            }
            fileBasedElement = (Element) xPath.selectSingleNode(element, "replacement");
            if (fileBasedElement != null) {
                setSeverity(confStatus, ConfigurationStatusSchema.Text.REPLACEMENT_IS_STANDING_BY);
                // doesn't have a message
                return confStatus;
            }
            NodeList fileBasedElements = xPath.selectNodeList(element, "file_based/requisites/requisite[@is_missing='yes'] | lock.requestor/lock.use[@is_missing='yes'] ");
            if (fileBasedElements.getLength() > 0) {
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < fileBasedElements.getLength(); i++) {
                    fileBasedElement = (Element) fileBasedElements.item(i);
                    if (fileBasedElement.hasAttribute("path")) {
                        if (fileBasedElement.hasAttribute("type")) {
                            s.append(fileBasedElement.getAttribute("type").toLowerCase().replace('_', ' '));
                            s.append(": ");
                        }
                        s.append(fileBasedElement.getAttribute("path"));
                        if (i < fileBasedElements.getLength() - 1) {
                            s.append("; ");
                        }
                    } else if (fileBasedElement.hasAttribute("lock")) {
                        s.append("lock: ");
                        s.append(fileBasedElement.getAttribute("lock"));
                        if (i < fileBasedElements.getLength() - 1) {
                            s.append("; ");
                        }
                    }
                }
                setSeverity(confStatus, ConfigurationStatusSchema.Text.RESOURCE_IS_MISSING);
                confStatus.setMessage(s.toString());
                return confStatus;
            }

        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return null;
    }

    /** @param jsonObject has to be the 'obstacles' json array of a json object
     *            of orders[], jobs[], job_chains[], etc. collection from a
     *            JobScheduler response
     * 
     * @return ConfigurationStatusSchema
     */
    public static ConfigurationStatusSchema getConfigurationStatus(JsonArray obstacles) {
        try {
            Optional<JsonValue> fileBasedObstacleItem = obstacles.stream().filter(p -> "fileBasedObstacles".equals(((JsonObject) p).getString("TYPE"))).findFirst();
            if (!fileBasedObstacleItem.isPresent()) {
                return null;
            }
            JsonArray fileBasedObstacles = ((JsonObject) fileBasedObstacleItem.get()).getJsonArray("fileBasedObstacles");
            ConfigurationStatusSchema errorInConfFileStatus = null;
            ConfigurationStatusSchema removingDelayedStatus = null;
            ConfigurationStatusSchema replacementStatus = null;
            ConfigurationStatusSchema notLoadedStatus = null;
            StringBuilder s = new StringBuilder();
            for (JsonObject fileBasedObstacle : fileBasedObstacles.getValuesAs(JsonObject.class)) {
                switch (fileBasedObstacle.getString("TYPE", "").toLowerCase()) {
                case "notinitialized": // not yet in JSON response
                    errorInConfFileStatus = new ConfigurationStatusSchema();
                    setSeverity(errorInConfFileStatus, ConfigurationStatusSchema.Text.ERROR_IN_CONFIGURATION_FILE);
                    setMessage(errorInConfFileStatus, fileBasedObstacle);
                    break;
                case "removed":
                    removingDelayedStatus = new ConfigurationStatusSchema();
                    setSeverity(removingDelayedStatus, ConfigurationStatusSchema.Text.REMOVING_DELAYED);
                    setMessage(removingDelayedStatus, fileBasedObstacle);
                    break;
                case "replaced":
                    if (!fileBasedObstacle.containsKey("error")) {
                        replacementStatus = new ConfigurationStatusSchema();
                        setSeverity(replacementStatus, ConfigurationStatusSchema.Text.REPLACEMENT_IS_STANDING_BY);
                        // doesn't have a message
                    } else {
                        notLoadedStatus = new ConfigurationStatusSchema();
                        setSeverity(notLoadedStatus, ConfigurationStatusSchema.Text.CHANGED_FILE_NOT_LOADED);
                        setMessage(notLoadedStatus, fileBasedObstacle);
                    }
                    break;
                case "missingrequisite": // not yet in JSON response
                    if (fileBasedObstacle.containsKey("path")) {
                        if (fileBasedObstacle.containsKey("objType")) {
                            s.append(fileBasedObstacle.getString("objType").toLowerCase().replace('_', ' '));
                            s.append(": ");
                        }
                        s.append(fileBasedObstacle.getString("path"));
                        s.append("; ");
                    }
                    break;
                }
            }
            if (errorInConfFileStatus != null) {
                return errorInConfFileStatus;
            }
            if (notLoadedStatus != null) {
                return notLoadedStatus;
            }
            if (removingDelayedStatus != null) {
                return removingDelayedStatus;
            }
            if (replacementStatus != null) {
                return replacementStatus;
            }
            if (s.length() > 0) {
                ConfigurationStatusSchema missingResourceStatus = new ConfigurationStatusSchema();
                setSeverity(missingResourceStatus, ConfigurationStatusSchema.Text.RESOURCE_IS_MISSING);
                missingResourceStatus.setMessage(s.toString().replaceFirst(";\\s*$", ""));
                return missingResourceStatus;
            }

        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return null;
    }

    private static void setMessage(ConfigurationStatusSchema confStatus, Element errorElement) {
        confStatus.setMessage(errorElement.getAttribute("text"));
    }

    private static void setMessage(ConfigurationStatusSchema confStatus, JsonObject jsonObj) {
        confStatus.setMessage(jsonObj.getString("error"));
    }

    private static void setSeverity(ConfigurationStatusSchema confStatus, ConfigurationStatusSchema.Text text) {
        confStatus.setText(text);
        switch (confStatus.getText()) {
        case CHANGED_FILE_NOT_LOADED:
        case RESOURCE_IS_MISSING:
        case ERROR_IN_CONFIGURATION_FILE:
            confStatus.setSeverity(2);
            break;
        case REMOVING_DELAYED:
        case REPLACEMENT_IS_STANDING_BY:
            confStatus.setSeverity(5);
            break;
        case OK:
            confStatus.setSeverity(4);
            break;
        }
    }
}
