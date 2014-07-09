package com.sos.joc.cockpit.model;

import com.sos.scheduler.model.SchedulerObjectFactoryOptions;
import com.sos.scheduler.model.answers.Job;


public class JOCJob extends JOCObjects implements IJOCObjects<Job> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6073443161661412134L;
	private Job job = null;
	private final String EXTENSION = ".job";
	private final String ICON_STANDALONE = "explorer_item_job.gif";
	private final String ICON_ORDER = "explorer_item_orderjob.gif";
	
	
	public JOCJob(final Job job, final SchedulerObjectFactoryOptions options) {
		super(options);
		this.job = job;
	}
	
	@Override
	public String toString() {
		return job.getName();
	}
	
	@Override
	public String getName() {
		return job.getName();
	}

	@Override
	public String getPath() {
		return job.getPath();
	}

	@Override
	public Job getAnswerObject() {
		return job;
	}
	
	
	@Override
	public String getItemId() {
		return super.getItemId(getPath(), EXTENSION);
	}

	@Override
	public String getIcon() {
		String icon = "";
		if (job.getOrder() != null && job.getOrder() == "yes") {
			icon = ICON_ORDER;
		}
		else {
			icon = ICON_STANDALONE;
		}
		return icon;
	}

}
