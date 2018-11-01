package com.sos.joc.classes;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.classes.security.SOSSecurityConfigurationUserEntry;
import com.sos.joc.classes.security.SOSSecurityHashSettings;


public class SOSSecurityConfigurationUserEntryTest {

    @Test
    public void test() {
        if (Globals.sosShiroProperties == null) {
            Globals.sosShiroProperties = new JocCockpitProperties();
        }
        SOSSecurityHashSettings sosSecurityHashSettings = new SOSSecurityHashSettings();
        SOSSecurityConfigurationUserEntry sosSecurityConfigurationUserEntry = new SOSSecurityConfigurationUserEntry("",null,sosSecurityHashSettings);
        String s = sosSecurityConfigurationUserEntry.crypt("root");
        Assert.assertEquals("Crypt","root", s);

    }

}
