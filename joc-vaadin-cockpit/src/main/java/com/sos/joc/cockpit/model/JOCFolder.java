package com.sos.joc.cockpit.model;

import com.sos.scheduler.model.SchedulerObjectFactoryOptions;
import com.sos.scheduler.model.answers.Folder;


public class JOCFolder extends JOCObjects implements IJOCObjects<Folder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7133095330348050202L;
	private Folder folder = null;
	private final String EXTENSION = "";
	private final String ICON = "explorer_folder_closed.gif";
	
	public JOCFolder(final Folder folder, final SchedulerObjectFactoryOptions options) {
		super(options);
		this.folder = folder;
	}
	
	@Override
	public String toString() {
		String name = getName();
		if (name == null) {
			name = String.format("%1$s:%2$d", options.ServerName.Value(), options.PortNumber.value());
		}
		return name;
	}

	@Override
	public String getName() {
		return folder.getName();
	}

	@Override
	public String getPath() {
		return folder.getPath();
	}

	@Override
	public Folder getAnswerObject() {
		return folder;
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
