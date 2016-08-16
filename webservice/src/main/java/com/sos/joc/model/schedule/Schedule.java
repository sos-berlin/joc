
package com.sos.joc.model.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * schedule (permant part)
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "surveyDate",
    "path",
    "name",
    "title",
    "substitute",
    "usedByOrders",
    "usedByJobs",
    "configurationDate"
})
public class Schedule {

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     */
    @JsonProperty("surveyDate")
    private Date surveyDate;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     */
    @JsonProperty("path")
    private String path;
    @JsonProperty("name")
    private String name;
    @JsonProperty("title")
    private String title;
    /**
     * this field and substitutedBy from the volatile part are exclusive
     * 
     */
    @JsonProperty("substitute")
    private Substitute substitute;
    @JsonProperty("usedByOrders")
    private List<UsedByOrder> usedByOrders = new ArrayList<UsedByOrder>();
    @JsonProperty("usedByJobs")
    private List<UsedByJob> usedByJobs = new ArrayList<UsedByJob>();
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
    @JsonProperty("configurationDate")
    private Date configurationDate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @return
     *     The surveyDate
     */
    @JsonProperty("surveyDate")
    public Date getSurveyDate() {
        return surveyDate;
    }

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * 
     * @param surveyDate
     *     The surveyDate
     */
    @JsonProperty("surveyDate")
    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @return
     *     The path
     */
    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * 
     * @param path
     *     The path
     */
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * this field and substitutedBy from the volatile part are exclusive
     * 
     * @return
     *     The substitute
     */
    @JsonProperty("substitute")
    public Substitute getSubstitute() {
        return substitute;
    }

    /**
     * this field and substitutedBy from the volatile part are exclusive
     * 
     * @param substitute
     *     The substitute
     */
    @JsonProperty("substitute")
    public void setSubstitute(Substitute substitute) {
        this.substitute = substitute;
    }

    /**
     * 
     * @return
     *     The usedByOrders
     */
    @JsonProperty("usedByOrders")
    public List<UsedByOrder> getUsedByOrders() {
        return usedByOrders;
    }

    /**
     * 
     * @param usedByOrders
     *     The usedByOrders
     */
    @JsonProperty("usedByOrders")
    public void setUsedByOrders(List<UsedByOrder> usedByOrders) {
        this.usedByOrders = usedByOrders;
    }

    /**
     * 
     * @return
     *     The usedByJobs
     */
    @JsonProperty("usedByJobs")
    public List<UsedByJob> getUsedByJobs() {
        return usedByJobs;
    }

    /**
     * 
     * @param usedByJobs
     *     The usedByJobs
     */
    @JsonProperty("usedByJobs")
    public void setUsedByJobs(List<UsedByJob> usedByJobs) {
        this.usedByJobs = usedByJobs;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @return
     *     The configurationDate
     */
    @JsonProperty("configurationDate")
    public Date getConfigurationDate() {
        return configurationDate;
    }

    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     * @param configurationDate
     *     The configurationDate
     */
    @JsonProperty("configurationDate")
    public void setConfigurationDate(Date configurationDate) {
        this.configurationDate = configurationDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(path).append(name).append(title).append(substitute).append(usedByOrders).append(usedByJobs).append(configurationDate).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Schedule) == false) {
            return false;
        }
        Schedule rhs = ((Schedule) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(name, rhs.name).append(title, rhs.title).append(substitute, rhs.substitute).append(usedByOrders, rhs.usedByOrders).append(usedByJobs, rhs.usedByJobs).append(configurationDate, rhs.configurationDate).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
