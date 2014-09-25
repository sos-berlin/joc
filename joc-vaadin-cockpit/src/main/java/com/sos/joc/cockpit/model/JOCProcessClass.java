package com.sos.joc.cockpit.model;

import com.sos.scheduler.model.SchedulerObjectFactoryOptions;
import com.sos.scheduler.model.answers.ProcessClass;

public class JOCProcessClass extends JOCObjects implements
		IJOCObjects<ProcessClass> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3466218178803833071L;
	private ProcessClass processClass = null;
	private final String EXTENSION = ".process_class";
	private final String ICON = "explorer_item_processclass.gif";
	
	public JOCProcessClass(final ProcessClass processClass, final SchedulerObjectFactoryOptions options) {
		super(options);
		this.processClass = processClass;
	}
	
	@Override
	public String toString() {
		String name = getName();
		if (name == null) {
			name = "(default)";
		}
		return name;
	}

	@Override
	public String getName() {
		return processClass.getName();
	}

	@Override
	public String getPath() {
		return processClass.getPath();
	}

	@Override
	public ProcessClass getAnswerObject() {
		return processClass;
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
