package com.sos.joc.cockpit.model;

import com.sos.scheduler.model.SchedulerObjectFactoryOptions;
import com.sos.scheduler.model.answers.Lock;



public class JOCLock extends JOCObjects implements IJOCObjects<Lock> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4405656217749806679L;
	private Lock lock = null;
	private final String EXTENSION = ".lock";
	private final String ICON = "explorer_item_lock.gif";
	
	public JOCLock(final Lock lock, final SchedulerObjectFactoryOptions options) {
		super(options);
		this.lock = lock;
	}

	@Override
	public String getName() {
		return lock.getName();
	}

	@Override
	public String getPath() {
		return lock.getPath();
	}

	@Override
	public Lock getAnswerObject() {
		return lock;
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
