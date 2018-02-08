package com.sos.joc.classes.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sos.joc.model.security.SecurityConfigurationMainEntry;

public class SOSSecurityConfigurationMainEntry {
	private SecurityConfigurationMainEntry securityConfigurationMainEntry;

	public SOSSecurityConfigurationMainEntry(SecurityConfigurationMainEntry securityConfigurationMainEntry) {
		super();
		this.securityConfigurationMainEntry = securityConfigurationMainEntry;

	}

	public SOSSecurityConfigurationMainEntry() {
		super();
	}


	public String getIniWriteString() {
		if (securityConfigurationMainEntry.getEntryName().contains(".groupRolesMap")) {
			String s = "  ";
			if (securityConfigurationMainEntry.getEntryValue().size() > 1) {
				s = "\\" + "\n" + "  ";
			}

			for (int i = 0; i < securityConfigurationMainEntry.getEntryValue().size(); i++) {
				s = s + securityConfigurationMainEntry.getEntryValue().get(i).trim();
				if (i < securityConfigurationMainEntry.getEntryValue().size() - 1) {
					s = s + ", \\" + "\n" + "  ";
				} else {
					s = s + "\n";
				}
			}
			return s;
		} else {
			String value = securityConfigurationMainEntry.getEntryValue().get(0).trim();
			if (value.isEmpty()) {
				value = ".";
			}
			return value;
		}
	}

	private String removeCharInQuotes(String s, String source, String target) {
		boolean inQuote = false;
		StringBuffer str = new StringBuffer(s);
		for (int i = 0; i < s.length(); i++) {
			String charAt = String.valueOf(s.charAt(i));
			if ("\"".equals(charAt)) {
				inQuote = !inQuote;
			}
			if (inQuote && source.equals(charAt)) {
				str.setCharAt(i, target.charAt(0));
			}

		}
		return str.toString();

	}

	public String removeCharInQuotesTest(String s) {
		return removeCharInQuotes(s, ",", "^");
	}

	public List<String> getMultiLineValue(String entryKey, String entryMultiLineValue) {
		List<String> entryValue = new ArrayList<String>();
		if (entryKey.contains(".groupRolesMap")) {
			entryMultiLineValue = removeCharInQuotes(entryMultiLineValue, ",", "°");
			String s[] = entryMultiLineValue.split(",");
			for (int i = 0; i < s.length; i++) {
				entryValue.add(removeCharInQuotes(s[i], "°", ","));
			}
		} else {
			entryValue.add(entryMultiLineValue);
		}
		return entryValue;
	}

	public List<String> getMultiLineComment(String main, HashMap<String, String> comments) {
		List<String> entryComment = new ArrayList<String>();
		if (comments.get(main) != null) {
			String s[] = comments.get(main).split("\\r\\n|\\n|\\r");
			for (int i = 0; i < s.length; i++) {
				entryComment.add(s[i]);
			}
		}
		return entryComment;
	}

}
