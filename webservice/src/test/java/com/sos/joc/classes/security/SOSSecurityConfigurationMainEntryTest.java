package com.sos.joc.classes.security;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.sos.joc.model.security.SecurityConfigurationMainEntry;

public class SOSSecurityConfigurationMainEntryTest {

	@Test
	public void removeCharInQuotesTest() {
		SOSSecurityConfigurationMainEntry s = new SOSSecurityConfigurationMainEntry();
		String oldString = "sos : it_operator,\"CN=user_sudouser,OU=privilegedgroups,OU=accessmgmnt,DC=ers,DC=XXX,DC=com\",apl : administrator|application_manager";
		String newString = s.removeCharInQuotesTest(oldString);
		Assert.assertEquals("removeCharInQuotesTest",
				"sos : it_operator,\"CN=user_sudouser^OU=privilegedgroups^OU=accessmgmnt^DC=ers^DC=XXX^DC=com\",apl : administrator|application_manager",
				newString);

	}

	@Test
	public void getMultilineValueTest() {
		SOSSecurityConfigurationMainEntry s = new SOSSecurityConfigurationMainEntry();
		List<String> entry = s.getMultiLineValue("ldapRealm.groupRolesMap",
				"\"CN=rs_uk_rosalind_prod_sudouser,OU=privilegedgroups,OU=accessmgmnt,DC=ers,DC=equifax,DC=com\":\"all\", \"CN=rs_uk_rosalind_prod_sudouser,OU=privilegedgroups,OU=accessmgmnt,DC=ers,DC=equifax,DC=com\":\"all\"");
		SecurityConfigurationMainEntry securityConfigurationMainEntry = new SecurityConfigurationMainEntry();
		securityConfigurationMainEntry.setEntryName("ldapRealm.groupRolesMap");
		for (int i = 0; i < entry.size(); i++) {
			securityConfigurationMainEntry.getEntryValue().add(entry.get(i));
		}

		SOSSecurityConfigurationMainEntry sosSecurityConfigurationMainEntry = new SOSSecurityConfigurationMainEntry(
				securityConfigurationMainEntry);
		String newEntry = sosSecurityConfigurationMainEntry.getIniWriteString();
	}

}
