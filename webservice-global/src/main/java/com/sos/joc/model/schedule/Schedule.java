
package com.sos.joc.model.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * schedule (permant part)
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class Schedule {

    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
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
     * 
     * (Required)
     * 
     */
    private String name;
    private String title;
    /**
     * this field and substitutedBy from the volatile part are exclusive
     * 
     */
    private Substitute substitute;
    /**
     * 
     * (Required)
     * 
     */
    private List<UsedByOrder> usedByOrders = new ArrayList<UsedByOrder>();
    /**
     * 
     * (Required)
     * 
     */
    private List<UsedByJob> usedByJobs = new ArrayList<UsedByJob>();
    /**
     * timestamp
     * <p>
     * Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ or empty
     * 
     */
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
     * 
     * (Required)
     * 
     * @return
     *     The name
     */
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
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * this field and substitutedBy from the volatile part are exclusive
     * 
     * @return
     *     The substitute
     */
    public Substitute getSubstitute() {
        return substitute;
    }

    /**
     * this field and substitutedBy from the volatile part are exclusive
     * 
     * @param substitute
     *     The substitute
     */
    public void setSubstitute(Substitute substitute) {
        this.substitute = substitute;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The usedByOrders
     */
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
    public void setConfigurationDate(Date configurationDate) {
        this.configurationDate = configurationDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(surveyDate).append(path).append(name).append(title).append(substitute).append(usedByOrders).append(usedByJobs).append(configurationDate).toHashCode();
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
        return new EqualsBuilder().append(surveyDate, rhs.surveyDate).append(path, rhs.path).append(name, rhs.name).append(title, rhs.title).append(substitute, rhs.substitute).append(usedByOrders, rhs.usedByOrders).append(usedByJobs, rhs.usedByJobs).append(configurationDate, rhs.configurationDate).isEquals();
    }

}
