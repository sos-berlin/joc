
package com.sos.joc.model.jobscheduler;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "dbms"
})
public class Database {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("dbms")
    private Database.Dbms dbms;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The dbms
     */
    @JsonProperty("dbms")
    public Database.Dbms getDbms() {
        return dbms;
    }

    /**
     * 
     * (Required)
     * 
     * @param dbms
     *     The dbms
     */
    @JsonProperty("dbms")
    public void setDbms(Database.Dbms dbms) {
        this.dbms = dbms;
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
        return new HashCodeBuilder().append(dbms).append(additionalProperties).toHashCode();
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
        return new EqualsBuilder().append(dbms, rhs.dbms).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public enum Dbms {

        MY_SQL("MySQL"),
        ORACLE("Oracle"),
        POSTGRES("Postgres"),
        SYBASE("Sybase"),
        DB_2("DB2"),
        MS_SQL_SERVER("MS SQL Server");
        private final String value;
        private final static Map<String, Database.Dbms> CONSTANTS = new HashMap<String, Database.Dbms>();

        static {
            for (Database.Dbms c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Dbms(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static Database.Dbms fromValue(String value) {
            Database.Dbms constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
