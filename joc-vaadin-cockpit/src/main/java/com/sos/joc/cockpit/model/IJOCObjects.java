package com.sos.joc.cockpit.model;

import java.io.Serializable;

import com.sos.scheduler.model.SchedulerObjectFactoryOptions;


public interface IJOCObjects<T> extends Serializable {
	
	public String getName();
	public String getPath();
	public T getAnswerObject();
	public String getItemId();
	public String getIcon();
	public SchedulerObjectFactoryOptions getOptions();
}
