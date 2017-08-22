package com.sos.joc.classes;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import com.sos.joc.Globals;
import com.sos.joc.classes.security.SOSSecurityConfigurationUserEntry;


public class SOSSecurityConfigurationUserEntryTest {

    @Test
    public void test() {
        if (Globals.sosShiroProperties == null) {
            Globals.sosShiroProperties = new JocCockpitProperties();
        }
        SOSSecurityConfigurationUserEntry sosSecurityConfigurationUserEntry = new SOSSecurityConfigurationUserEntry("",null,true,"");
        String s = sosSecurityConfigurationUserEntry.crypt("root");
        Assert.assertEquals("Crypt","", s);

    }

}
