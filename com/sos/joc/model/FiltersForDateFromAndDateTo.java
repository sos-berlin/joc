
package com.sos.joc.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * filters for date_from and date_to
 * <p>
 * dates in ISO 8601 format
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "date_from",
    "date_to"
})
public class FiltersForDateFromAndDateTo {

    @JsonProperty("date_from")
    private Date dateFrom;
    @JsonProperty("date_to")
    private Date dateTo;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The dateFrom
     */
    @JsonProperty("date_from")
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * 
     * @param dateFrom
     *     The date_from
     */
    @JsonProperty("date_from")
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public FiltersForDateFromAndDateTo withDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    /**
     * 
     * @return
     *     The dateTo
     */
    @JsonProperty("date_to")
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * 
     * @param dateTo
     *     The date_to
     */
    @JsonProperty("date_to")
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public FiltersForDateFromAndDateTo withDateTo(Date dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public FiltersForDateFromAndDateTo withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
