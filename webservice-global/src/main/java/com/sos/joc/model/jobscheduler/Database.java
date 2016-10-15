
package com.sos.joc.model.jobscheduler;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "dbms",
    "surveyDate",
    "version",
    "state"
})
public class Database {

    /**
     * Possible values are MySQL,Oracle,Postgres,Sybase,DB2,MS SQL Server
     * (Required)
     * 
     */
    @JsonProperty("dbms")
    private String dbms;
    /**
     * survey date of the inventory data; last time the inventory job has checked the live folder
     * <p>
     * Date of the inventory data. Value is UTC timestamp in ISO 8601 YYYY-MM-DDThh:mm:ss.sZ
     * (Required)
     * 
     */
    @JsonProperty("surveyDate")
    private Date surveyDate;
    @JsonProperty("version")
    private String version;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("state")
    private DBState state;

    /**
     * Possible values are MySQL,Oracle,Postgres,Sybase,DB2,MS SQL Server
     * (Required)
     * 
     * @return
     *     The dbms
     */
    @JsonProperty("dbms")
    public String getDbms() {
        return dbms;
    }

    /**
     * Possible values are MySQL,Oracle,Postgres,Sybase,DB2,MS SQL Server
     * (Required)
     * 
     * @param dbms
     *     The dbms
     */
    @JsonProperty("dbms")
    public void setDbms(String dbms) {
        this.dbms = dbms;
    }

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
     * 
     * @return
     *     The version
     */
    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    /**
     * 
     * @param version
     *     The version
     */
    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 
     * (Required)
     * 
     * @return
     *     The state
     */
    @JsonProperty("state")
    public DBState getState() {
        return state;
    }

    /**
     * 
     * (Required)
     * 
     * @param state
     *     The state
     */
    @JsonProperty("state")
    public void setState(DBState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(dbms).append(surveyDate).append(version).append(state).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Database) == false) {
            return false;
        }
        Database rhs = ((Database) other);
        return new EqualsBuilder().append(dbms, rhs.dbms).append(surveyDate, rhs.surveyDate).append(version, rhs.version).append(state, rhs.state).isEquals();
    }

}
