package com.sos.joc.cockpit.composite;

import java.io.File;

import com.sos.joc.cockpit.model.IJOCObjects;
import com.sos.joc.cockpit.model.JOCFolder;
import com.sos.joc.cockpit.model.JOCJob;
import com.sos.joc.cockpit.model.JOCJobChain;
import com.sos.joc.cockpit.model.JOCLock;
import com.sos.joc.cockpit.model.JOCObjects;
import com.sos.joc.cockpit.model.JOCOrder;
import com.sos.joc.cockpit.model.JOCProcessClass;
import com.sos.joc.cockpit.model.JOCState;
import com.sos.joc.cockpit.model.JobSchedulerCommand;
import com.sos.scheduler.model.SchedulerObjectFactoryOptions;
import com.sos.scheduler.model.answers.Folder;
import com.sos.scheduler.model.answers.Folders;
import com.sos.scheduler.model.answers.Job;
import com.sos.scheduler.model.answers.JobChain;
import com.sos.scheduler.model.answers.JobChains;
import com.sos.scheduler.model.answers.Jobs;
import com.sos.scheduler.model.answers.Lock;
import com.sos.scheduler.model.answers.Locks;
import com.sos.scheduler.model.answers.Order;
import com.sos.scheduler.model.answers.Orders;
import com.sos.scheduler.model.answers.ProcessClass;
import com.sos.scheduler.model.answers.ProcessClasses;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;

public class HotFoldersTree extends Tree implements JobSchedulerCommand.StateListener {

	private static final long serialVersionUID = -8265635612531324720L;
	private HierarchicalContainer container = null;
	private final JobSchedulerCommand command = new JobSchedulerCommand();
	
	private static final String ICON_FOLDER = "images/"; //relative to WebContent/VAADIN/themes/joc
	private static final String FETCH_STATE_WHAT = "folders job_chains no_subfolders";
	private static final String FETCH_STATE_SUBSYSTEMS = "job order standing_order lock schedule process_class folder";
	
	public HotFoldersTree() {
		setContainerDataSource(createContainer());
		setItemCaptionPropertyId("caption");
		setItemIconPropertyId("icon");
		setSizeUndefined();
		setItemDescriptionGenerator(new ItemDescriptionGenerator() {                             
			private static final long serialVersionUID = -7746543560487238949L;

			@Override
			public String generateDescription(final Component source, final Object itemId, final Object propertyId) {
//			    if(propertyId == null){
//			        return "Row description "+ itemId;
//			    } else if(propertyId == COLUMN1_PROPERTY_ID) {
//			        return "Cell description " + itemId +","+propertyId;
//			    }
				//TODO doesn't work
			    return (String) propertyId;
			}
		});
		
		addExpandListener(new HotFoldersTree.ExpandListener() {
			
			private static final long serialVersionUID = -1735963793435668052L;

			@Override
			public void nodeExpand(final ExpandEvent event) {
//				String host = (String) hotFolders.getContainerProperty(event.getItemId(), "host").getValue();
//				Integer port = (Integer) hotFolders.getContainerProperty(event.getItemId(), "port").getValue();
//				String path = (String) hotFolders.getContainerProperty(event.getItemId(), "path").getValue();
//				options.ServerName.Value(host);
//				options.PortNumber.value(port);
//				command.fetchState(thisObj, options, FETCH_STATE_WHAT, FETCH_STATE_SUBSYSTEMS, path);
				updateTree(event);
			}
		});
		
//		ContextMenu contextMenu = new ContextMenu();
//		contextMenu.setAsTreeContextMenu(this);
//	
//		contextMenu.addContextMenuTreeListener(new ContextMenuOpenedListener.TreeListener() {
//		    
//		    @Override
//			public void onContextMenuOpenFromTreeItem(ContextMenuOpenedOnTreeItemEvent event) {
//		    	Object curJSObj = getContainerProperty(event.getItemId(), "jsobj").getValue();
//		    	Notification.show("Tree item clicked " + event.getItemId() + ":" + curJSObj.getClass().getName());
////				Notification.show(command.getSchedulerObjectFactory().answerToXMLString(curJSObj));
//			}
//		});
		
	}
	
	
	private HierarchicalContainer createContainer() {
		if (container == null) {
			container = new HierarchicalContainer();
			container.addContainerProperty("icon", Resource.class, null);
			container.addContainerProperty("jsobj", IJOCObjects.class, null);
			container.addContainerProperty("caption", String.class, null);
			container.addContainerProperty("sort", Integer.class, 1);
		}
		return container;
	}
	
	
	private void sort() {
        Object[] properties = new Object[2];
        properties[0] = "sort";
        properties[1] = "caption";

        boolean[] ascending = new boolean[2];
        ascending[0] = true;
        ascending[1] = true;

        container.sort(properties, ascending);
    }
	
	
	public void initTree(final SchedulerObjectFactoryOptions options) {
//		command.fetchHotFolder(this, "ftp://test:12345@homer.sos:80/scheduler.current32/config/live/");
		command.fetchState(this, options, FETCH_STATE_WHAT, FETCH_STATE_SUBSYSTEMS, "/");
	}
	
	
	@SuppressWarnings("unchecked")
	protected void updateTree(final ExpandEvent event) {
		IJOCObjects<Object> obj = (IJOCObjects<Object>) getContainerProperty(event.getItemId(), "jsobj").getValue();
		command.fetchState(this, obj.getOptions(), FETCH_STATE_WHAT, FETCH_STATE_SUBSYSTEMS, obj.getPath());
	}
	
	
	protected void updateJSObjectToContainer(final JOCState state) {
		Folder folder = state.getState().getFolder();
		String parent = new JOCObjects(state.getOptions()).getItemId(new File(folder.getPath()).getParent());
		updateJSObjectToContainer(folder, state, parent);
		//TODO remove children which not in JobScheduler answer
		sort();
	}
	
	protected void updateJSObjectToContainer(final Folder folder, final JOCState state, final String parent) {
		String itemId = updateItemToContainer(new JOCFolder(folder, state.getOptions()), parent);
		folder.getFileBasedOrJobsOrFolders();
		for (Object folderItem : folder.getFileBasedOrJobsOrFolders()) {
			if (folderItem instanceof Folders) {
				for (Folder subfolder : ((Folders) folderItem).getFolder()) {
					updateJSObjectToContainer(subfolder, state, itemId);
				}
			}
			else if (folderItem instanceof Jobs) {
				for (Job job : ((Jobs) folderItem).getJob()) {
					updateItemToContainer(new JOCJob(job, state.getOptions()), itemId);
				}
			}
			else if (folderItem instanceof ProcessClasses) {
				for (ProcessClass processClass : ((ProcessClasses) folderItem).getProcessClass()) {
					updateItemToContainer(new JOCProcessClass(processClass, state.getOptions()), itemId);
				}
			}
			else if (folderItem instanceof JobChains) {
				for (JobChain jobChain : ((JobChains) folderItem).getJobChain()) {
					updateItemToContainer(new JOCJobChain(jobChain, state.getOptions()), itemId);
				}
			}
			else if (folderItem instanceof Orders) {
				for (Order order : ((Orders) folderItem).getOrder()) {
					updateItemToContainer(new JOCOrder(order, state.getOptions()), itemId);
				}
			}
			else if (folderItem instanceof Locks) {
				for (Lock lock : ((Locks) folderItem).getLock()) {
					updateItemToContainer(new JOCLock(lock, state.getOptions()), itemId);
				}
			}
//			else if (folderItem instanceof Schedules) {
//				for (Schedule schedule : ((Schedules) folderItem).getSchedule()) {
//					updateItemToContainer(new JOCSchedule(schedule, state.getOptions()), itemId);
//				}
//			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private String updateItemToContainer(final IJOCObjects<?> jocObj, final String parentId) {
		String itemId = jocObj.getItemId();
		
		if (container.containsId(itemId) == false) {
			container.addItem(itemId);
		}
		Item item = container.getItem(itemId);
		if (jocObj instanceof JOCFolder) {
			item.getItemProperty("sort").setValue(0);
			container.setChildrenAllowed(itemId, true);
		}
		else {
			container.setChildrenAllowed(itemId, false);
		}
		container.setParent(itemId, parentId);
		item.getItemProperty("jsobj").setValue(jocObj);
		item.getItemProperty("caption").setValue(jocObj.toString());
		item.getItemProperty("icon").setValue(new ThemeResource(ICON_FOLDER+jocObj.getIcon()));
		return itemId;
	}
	
	
	/**
	 * Method from interface JobSchedulerCommand.StateListener
	 */
	@Override
	public void getException(final Exception e) {
		UI.getCurrent().access(new Runnable() {

			@Override
			public void run() {
				Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
			}
		});
	}
	
	
	/**
	 * Method from interface JobSchedulerCommand.StateListener
	 */
	@Override
	public void getState(final JOCState state) {
		UI.getCurrent().access(new Runnable() {

			@Override
			public void run() {
				updateJSObjectToContainer(state);
			}
		});
	}

}
