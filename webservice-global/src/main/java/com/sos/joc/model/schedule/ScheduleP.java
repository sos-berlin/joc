
package com.sos.joc.model.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
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
    "substitutedBy",
    "usedByOrders",
    "usedByJobs",
    "configurationDate"
})
public class ScheduleP {

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("surveyDate")
    private Date surveyDate;
    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    @JsonProperty("path")
    private String path;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    private String name;
    @JsonProperty("title")
    private String title;
    /**
     * substitute
     * <p>
     * 
     * 
     */
    @JsonProperty("substitute")
    private Substitute substitute;
    @JsonProperty("substitutedBy")
    private List<Substitute> substitutedBy = new ArrayList<Substitute>();
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("usedByOrders")
    private List<UsedByOrder> usedByOrders = new ArrayList<UsedByOrder>();
    /**
     * 
     * (Required)
     * 
     */
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

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
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
     * (Required)
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
     * (Required)
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
     * (Required)
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
     * (Required)
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
     * (Required)
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
     * substitute
     * <p>
     * 
     * 
     * @return
     *     The substitute
     */
    @JsonProperty("substitute")
    public Substitute getSubstitute() {
        return substitute;
    }

    /**
     * substitute
     * <p>
     * 
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
     *     The substitutedBy
     */
    @JsonProperty("substitutedBy")
    public List<Substitute> getSubstitutedBy() {
        return substitutedBy;
    }

    /**
     * 
     * @param substitutedBy
     *     The substitutedBy
     */
    @JsonProperty("substitutedBy")
    public void setSubstitutedBy(List<Substitute> substitutedBy) {
        this.substitutedBy = substitutedBy;
    }

    /**
     * 
     * (Required)
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
     * (Required)
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
     * (Required)
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
     * (Required)
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(path).append(name).append(title).append(substitute).append(substitutedBy).append(usedByOrders).append(usedByJobs).append(configurationDate).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ScheduleP) == false) {
            return false;
        }
        ScheduleP rhs = ((ScheduleP) other);
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(name, rhs.name).append(title, rhs.title).append(substitute, rhs.substitute).append(substitutedBy, rhs.substitutedBy).append(usedByOrders, rhs.usedByOrders).append(usedByJobs, rhs.usedByJobs).append(configurationDate, rhs.configurationDate).isEquals();
    }

}
