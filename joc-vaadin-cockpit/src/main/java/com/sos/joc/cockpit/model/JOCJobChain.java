package com.sos.joc.cockpit.model;

import com.sos.scheduler.model.SchedulerObjectFactoryOptions;
import com.sos.scheduler.model.answers.JobChain;


public class JOCJobChain extends JOCObjects implements IJOCObjects<JobChain> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7633402864404606134L;
	private JobChain jobChain = null;
	private final String EXTENSION = ".job_chain";
	private final String ICON = "explorer_item_jobchain.gif";
	
	public JOCJobChain(final JobChain jobChain, final SchedulerObjectFactoryOptions options) {
		super(options);
		this.jobChain = jobChain;
	}
	
	@Override
	public String toString() {
		return jobChain.getName();
	}

	@Override
	public String getName() {
		return jobChain.getName();
	}

	@Override
	public String getPath() {
		return jobChain.getPath();
	}

	@Override
	public JobChain getAnswerObject() {
		return jobChain;
	}

	@Override
	public String getItemId() {
		return super.getItemId(getPath(), EXTENSION);
	}

	@Override
	public String getIcon() {
		return ICON;
	}

}
