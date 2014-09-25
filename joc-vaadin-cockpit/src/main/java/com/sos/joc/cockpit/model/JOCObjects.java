package com.sos.joc.cockpit.model;

import com.sos.scheduler.model.SchedulerObjectFactoryOptions;

public class JOCObjects {

	SchedulerObjectFactoryOptions options = null;
	
	public JOCObjects(final SchedulerObjectFactoryOptions options) {
		this.options = options;
	}
	
	public SchedulerObjectFactoryOptions getOptions() {
		return options;
	}
	
	public String getItemId(final String path, final String extension) {
		if (path == null) {
			return null;
		}
		String prefix = String.format("%1$s:%2$d", options.ServerName.Value(), options.PortNumber.value());
		return prefix+path+extension;
	}
	
	public String getItemId(final String path) {
		return getItemId(path, "");
	}

}
