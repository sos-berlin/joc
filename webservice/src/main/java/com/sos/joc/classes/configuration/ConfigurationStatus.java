package com.sos.joc.classes.configuration;

import javax.json.JsonArray;
import javax.json.JsonObject;

import org.apache.xpath.CachedXPathAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.model.common.ConfigurationStatusSchema;
import com.sos.joc.model.common.ConfigurationStatusSchema.Text;

public class ConfigurationStatus {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationStatus.class);

    /**
     * @param element
     *            has to be an <order>...</order>, <job>...</job> ,
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
            fileBasedElement = (Element) xPath.selectSingleNode(element, "replacement");
            if (fileBasedElement != null) {
                setSeverity(confStatus, ConfigurationStatusSchema.Text.REPLACEMENT_IS_STANDING_BY);
                // doesn't have a message
                return confStatus;
            }
            fileBasedElement = (Element) xPath.selectSingleNode(element, "file_based/removed/ERROR");
            if (fileBasedElement != null) {
                setSeverity(confStatus, ConfigurationStatusSchema.Text.REMOVING_DELAYED);
                setMessage(confStatus, fileBasedElement);
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
                    }
                }
                setSeverity(confStatus, ConfigurationStatusSchema.Text.RESOURCE_IS_MISSING);
                confStatus.setMessage(s.toString());
                return confStatus;
            }

        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return confStatus;
    }

    /**
     * @param jsonObject
     *            has to be a json object of orders[], jobs[], job_chains[],
     *            etc. from a JobScheduler response
     * 
     * @return ConfigurationStatusSchema
     */
    public static ConfigurationStatusSchema getConfigurationStatus(JsonObject jsonObject) {
        ConfigurationStatusSchema confStatus = new ConfigurationStatusSchema();
        try {
            JsonObject liveChanged = jsonObject.getJsonObject("liveChanged");
            switch (liveChanged.getString("TYPE", "").toLowerCase()) {
            case "notinitialized": // not yet in JSON response
                setSeverity(confStatus, ConfigurationStatusSchema.Text.ERROR_IN_CONFIGURATION_FILE);
                setMessage(confStatus, jsonObject);
                break;
            case "removed":
                setSeverity(confStatus, ConfigurationStatusSchema.Text.REMOVING_DELAYED);
                setMessage(confStatus, jsonObject);
                break;
            case "replaced":
                if (!liveChanged.containsKey("error")) {
                    setSeverity(confStatus, ConfigurationStatusSchema.Text.REPLACEMENT_IS_STANDING_BY);
                    // doesn't have a message
                } else {
                    setSeverity(confStatus, ConfigurationStatusSchema.Text.CHANGED_FILE_NOT_LOADED);
                    setMessage(confStatus, jsonObject);
                }
                break;
            case "resourceismissing": // not yet in JSON response
                setSeverity(confStatus, ConfigurationStatusSchema.Text.RESOURCE_IS_MISSING);
                JsonArray resources = liveChanged.getJsonArray("resources");
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < resources.size(); i++) {
                    JsonObject resource = resources.getJsonObject(i);
                    if (resource.containsKey("path")) {
                        if (resource.containsKey("type")) {
                            s.append(resource.getString("type").toLowerCase().replace('_', ' '));
                            s.append(": ");
                        }
                        s.append(resource.getString("path"));
                        if (i < resources.size() - 1) {
                            s.append("; ");
                        }
                    }
                }
                confStatus.setMessage(s.toString());
                break;
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return confStatus;
    }

    private static void setMessage(ConfigurationStatusSchema confStatus, Element errorElement) {
        String message = errorElement.getAttribute("text");
        if (message != null) {
            confStatus.setMessage(message);
        }
    }

    private static void setMessage(ConfigurationStatusSchema confStatus, JsonObject jsonObj) {
        String message = jsonObj.getString("error");
        if (message != null) {
            confStatus.setMessage(message);
        }
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

    public static ConfigurationStatusSchema getConfigurationStatus() {
        ConfigurationStatusSchema configurationStatusSchema = new ConfigurationStatusSchema();
        configurationStatusSchema.setMessage("myMessage");
        configurationStatusSchema.setSeverity(-1);
        configurationStatusSchema.setText(Text.CHANGED_FILE_NOT_LOADED);
        return configurationStatusSchema;
    }
}
