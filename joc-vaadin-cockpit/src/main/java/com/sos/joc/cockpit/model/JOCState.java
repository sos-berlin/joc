package com.sos.joc.cockpit.model;

import java.io.Serializable;

import com.sos.scheduler.model.SchedulerObjectFactoryOptions;
import com.sos.scheduler.model.answers.State;

public class JOCState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2902197683973275095L;
	private State state = null;
	private SchedulerObjectFactoryOptions options = null;
	
	public JOCState() {
		//
	}
	
	public JOCState(final State state, final SchedulerObjectFactoryOptions options) {
		this.state = state;
		this.options = options;
	}

	public SchedulerObjectFactoryOptions getOptions() {
		return options;
	}

	public void setOptions(final SchedulerObjectFactoryOptions options) {
		this.options = options;
	}

	public State getState() {
		return state;
	}

	public void setState(final State state) {
		this.state = state;
	}
}
