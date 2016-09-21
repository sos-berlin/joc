
package com.sos.joc.model.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Configuration {

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    private Date surveyDate;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    private String path;
    /**
     * JobScheduler object type
     * <p>
     * 
     * 
     */
    private Configuration.Type type;
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     */
    private Date configurationDate;
    /**
     * configuration content
     * <p>
     * A parameter can specify if the content is xml or html. Either 'xml' or 'html' is required
     * (Required)
     * 
     */
    private Content content;

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @return
     *     The surveyDate
     */
    public Date getSurveyDate() {
        return surveyDate;
    }

    /**
     * survey date of the JobScheduler Master/Agent
     * <p>
     * Current date of the JobScheduler Master/Agent. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     * @param surveyDate
     *     The surveyDate
     */
    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The path
     */
    public String getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param path
     *     The path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * JobScheduler object type
     * <p>
     * 
     * 
     * @return
     *     The type
     */
    public Configuration.Type getType() {
        return type;
    }

    /**
     * JobScheduler object type
     * <p>
     * 
     * 
     * @param type
     *     The type
     */
    public void setType(Configuration.Type type) {
        this.type = type;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @return
     *     The configurationDate
     */
    public Date getConfigurationDate() {
        return configurationDate;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * (Required)
     * 
     * @param configurationDate
     *     The configurationDate
     */
    public void setConfigurationDate(Date configurationDate) {
        this.configurationDate = configurationDate;
    }

    /**
     * configuration content
     * <p>
     * A parameter can specify if the content is xml or html. Either 'xml' or 'html' is required
     * (Required)
     * 
     * @return
     *     The content
     */
    public Content getContent() {
        return content;
    }

    /**
     * configuration content
     * <p>
     * A parameter can specify if the content is xml or html. Either 'xml' or 'html' is required
     * (Required)
     * 
     * @param content
     *     The content
     */
    public void setContent(Content content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(path).append(type).append(configurationDate).append(content).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Configuration) == false) {
            return false;
        }
        Configuration rhs = ((Configuration) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(type, rhs.type).append(configurationDate, rhs.configurationDate).append(content, rhs.content).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Type {

        JOB("JOB"),
        JOBCHAIN("JOBCHAIN"),
        ORDER("ORDER"),
        PROCESSCLASS("PROCESSCLASS"),
        LOCK("LOCK"),
        SCHEDULE("SCHEDULE"),
        PARAMS("PARAMS"),
        OTHER("OTHER");
        private final String value;
        private final static Map<String, Configuration.Type> CONSTANTS = new HashMap<String, Configuration.Type>();

        static {
            for (Configuration.Type c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static Configuration.Type fromValue(String value) {
            Configuration.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
