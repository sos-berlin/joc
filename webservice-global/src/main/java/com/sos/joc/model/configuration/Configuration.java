
package com.sos.joc.model.configuration;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * save and response configuration
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "jobschedulerId",
    "id",
    "account",
    "configurationType",
    "objectType",
    "name",
    "shared",
    "configurationItem"
})
public class Configuration {

    @JsonProperty("jobschedulerId")
    private String jobschedulerId;
    /**
     * non negative long
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    private Long id;
    @JsonProperty("account")
    private String account;
    /**
     * configuration type
     * <p>
     * 
     * 
     */
    @JsonProperty("configurationType")
    private ConfigurationType configurationType;
    /**
     * configuration object type
     * <p>
     * 
     * 
     */
    @JsonProperty("objectType")
    private ConfigurationObjectType objectType;
    /**
     * required if configurationType equals CUSTOMIZATION
     * 
     */
    @JsonProperty("name")
    private String name;
    @JsonProperty("shared")
    private Boolean shared = false;
    /**
     * JSON object as string,  depends on configuration type
     * 
     */
    @JsonProperty("configurationItem")
    private String configurationItem;

    /**
     * 
     * @return
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * 
     * @param jobschedulerId
     *     The jobschedulerId
     */
    @JsonProperty("jobschedulerId")
    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    /**
     * non negative long
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    /**
     * non negative long
     * <p>
     * 
     * (Required)
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The account
     */
    @JsonProperty("account")
    public String getAccount() {
        return account;
    }

    /**
     * 
     * @param account
     *     The account
     */
    @JsonProperty("account")
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * configuration type
     * <p>
     * 
     * 
     * @return
     *     The configurationType
     */
    @JsonProperty("configurationType")
    public ConfigurationType getConfigurationType() {
        return configurationType;
    }

    /**
     * configuration type
     * <p>
     * 
     * 
     * @param configurationType
     *     The configurationType
     */
    @JsonProperty("configurationType")
    public void setConfigurationType(ConfigurationType configurationType) {
        this.configurationType = configurationType;
    }

    /**
     * configuration object type
     * <p>
     * 
     * 
     * @return
     *     The objectType
     */
    @JsonProperty("objectType")
    public ConfigurationObjectType getObjectType() {
        return objectType;
    }

    /**
     * configuration object type
     * <p>
     * 
     * 
     * @param objectType
     *     The objectType
     */
    @JsonProperty("objectType")
    public void setObjectType(ConfigurationObjectType objectType) {
        this.objectType = objectType;
    }

    /**
     * required if configurationType equals CUSTOMIZATION
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * required if configurationType equals CUSTOMIZATION
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
     *     The shared
     */
    @JsonProperty("shared")
    public Boolean getShared() {
        return shared;
    }

    /**
     * 
     * @param shared
     *     The shared
     */
    @JsonProperty("shared")
    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    /**
     * JSON object as string,  depends on configuration type
     * 
     * @return
     *     The configurationItem
     */
    @JsonProperty("configurationItem")
    public String getConfigurationItem() {
        return configurationItem;
    }

    /**
     * JSON object as string,  depends on configuration type
     * 
     * @param configurationItem
     *     The configurationItem
     */
    @JsonProperty("configurationItem")
    public void setConfigurationItem(String configurationItem) {
        this.configurationItem = configurationItem;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(jobschedulerId).append(id).append(account).append(configurationType).append(objectType).append(name).append(shared).append(configurationItem).toHashCode();
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
        return new EqualsBuilder().append(jobschedulerId, rhs.jobschedulerId).append(id, rhs.id).append(account, rhs.account).append(configurationType, rhs.configurationType).append(objectType, rhs.objectType).append(name, rhs.name).append(shared, rhs.shared).append(configurationItem, rhs.configurationItem).isEquals();
    }

}
