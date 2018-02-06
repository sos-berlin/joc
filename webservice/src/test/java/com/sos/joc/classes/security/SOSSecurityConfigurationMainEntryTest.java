package com.sos.joc.classes.security;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class SOSSecurityConfigurationMainEntryTest {

	@Test
	public void removeCharInQuotesTest() {
		SOSSecurityConfigurationMainEntry s = new SOSSecurityConfigurationMainEntry();
		String oldString = "sos : it_operator,\"CN=user_sudouser,OU=privilegedgroups,OU=accessmgmnt,DC=ers,DC=XXX,DC=com\",apl : administrator|application_manager";
		String newString = s.removeCharInQuotes(oldString);
        Assert.assertEquals("removeCharInQuotesTest","sos : it_operator,\"CN=user_sudouser^OU=privilegedgroups^OU=accessmgmnt^DC=ers^DC=XXX^DC=com\",apl : administrator|application_manager", newString);

	}

}
