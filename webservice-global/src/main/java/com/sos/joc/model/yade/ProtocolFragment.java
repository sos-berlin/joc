
package com.sos.joc.model.yade;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * protocol, host, port, account
 * <p>
 * compact=true -> only required fields
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "host",
    "protocol",
    "port",
    "account"
})
public class ProtocolFragment {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("host")
    private String host;
    /**
     * protocol
     * <p>
     * 
     * (Required)
     * 
     */
    @JsonProperty("protocol")
    private Protocol protocol;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("port")
    private Integer port;
    @JsonProperty("account")
    private String account;

    /**
     * 
     * (Required)
     * 
     * @return
     *     The host
     */
    @JsonProperty("host")
    public String getHost() {
        return host;
    }

    /**
     * 
     * (Required)
     * 
     * @param host
     *     The host
     */
    @JsonProperty("host")
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * protocol
     * <p>
     * 
     * (Required)
     * 
     * @return
     *     The protocol
     */
    @JsonProperty("protocol")
    public Protocol getProtocol() {
        return protocol;
    }

    /**
     * protocol
     * <p>
     * 
     * (Required)
     * 
     * @param protocol
     *     The protocol
     */
    @JsonProperty("protocol")
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @return
     *     The port
     */
    @JsonProperty("port")
    public Integer getPort() {
        return port;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     * @param port
     *     The port
     */
    @JsonProperty("port")
    public void setPort(Integer port) {
        this.port = port;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(host).append(protocol).append(port).append(account).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ProtocolFragment) == false) {
            return false;
        }
        ProtocolFragment rhs = ((ProtocolFragment) other);
        return new EqualsBuilder().append(host, rhs.host).append(protocol, rhs.protocol).append(port, rhs.port).append(account, rhs.account).isEquals();
    }

}
