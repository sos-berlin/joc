
package com.sos.joc.model.job;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.sos.joc.model.common.NameValuePairsSchema;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * start job command
 * <p>
 * 
 * 
 */
@Generated("org.jsonschema2pojo")
public class StartJobSchema {

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     */
    private String job;
    /**
     * Field to comment manually job starts which can be logged.
     * 
     */
    private String comment;
    /**
     * timestamp with now
     * <p>
     * ISO format yyyy-mm-dd HH:MM[:SS] or now or now + HH:MM[:SS] or now + SECONDS
     * 
     */
    private String at;
    /**
     * params or environment variables
     * <p>
     * 
     * 
     */
    private List<NameValuePairsSchema> params = new ArrayList<NameValuePairsSchema>();
    /**
     * params or environment variables
     * <p>
     * 
     * 
     */
    private List<NameValuePairsSchema> environment = new ArrayList<NameValuePairsSchema>();

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @return
     *     The job
     */
    public String getJob() {
        return job;
    }

    /**
     * path
     * <p>
     * absolute path based on live folder of a JobScheduler object.
     * (Required)
     * 
     * @param job
     *     The job
     */
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * Field to comment manually job starts which can be logged.
     * 
     * @return
     *     The comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Field to comment manually job starts which can be logged.
     * 
     * @param comment
     *     The comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * timestamp with now
     * <p>
     * ISO format yyyy-mm-dd HH:MM[:SS] or now or now + HH:MM[:SS] or now + SECONDS
     * 
     * @return
     *     The at
     */
    public String getAt() {
        return at;
    }

    /**
     * timestamp with now
     * <p>
     * ISO format yyyy-mm-dd HH:MM[:SS] or now or now + HH:MM[:SS] or now + SECONDS
     * 
     * @param at
     *     The at
     */
    public void setAt(String at) {
        this.at = at;
    }

    /**
     * params or environment variables
     * <p>
     * 
     * 
     * @return
     *     The params
     */
    public List<NameValuePairsSchema> getParams() {
        return params;
    }

    /**
     * params or environment variables
     * <p>
     * 
     * 
     * @param params
     *     The params
     */
    public void setParams(List<NameValuePairsSchema> params) {
        this.params = params;
    }

    /**
     * params or environment variables
     * <p>
     * 
     * 
     * @return
     *     The environment
     */
    public List<NameValuePairsSchema> getEnvironment() {
        return environment;
    }

    /**
     * params or environment variables
     * <p>
     * 
     * 
     * @param environment
     *     The environment
     */
    public void setEnvironment(List<NameValuePairsSchema> environment) {
        this.environment = environment;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(job).append(comment).append(at).append(params).append(environment).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof StartJobSchema) == false) {
            return false;
        }
        StartJobSchema rhs = ((StartJobSchema) other);
        return new EqualsBuilder().append(job, rhs.job).append(comment, rhs.comment).append(at, rhs.at).append(params, rhs.params).append(environment, rhs.environment).isEquals();
    }

}
