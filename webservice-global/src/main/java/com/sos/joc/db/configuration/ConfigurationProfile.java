package com.sos.joc.db.configuration;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sos.joc.model.configuration.Profile;

public class ConfigurationProfile extends Profile {
    
    private String lowerAccount;

    public ConfigurationProfile(String account, Date modified) {
        setAccount(account);
        setModified(modified);
        this.lowerAccount = account.toLowerCase();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(lowerAccount).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Profile) == false) {
            return false;
        }
        ConfigurationProfile rhs = ((ConfigurationProfile) other);
        return new EqualsBuilder().append(lowerAccount, rhs.lowerAccount).isEquals();
    }
}
