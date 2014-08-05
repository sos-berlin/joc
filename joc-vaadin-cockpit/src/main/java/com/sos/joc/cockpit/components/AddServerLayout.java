package com.sos.joc.cockpit.components;

import java.util.ArrayList;
import java.util.List;

import com.sos.joc.cockpit.HotFolders;
import com.sos.joc.cockpit.JOCCockpitUI;
import com.sos.joc.cockpit.composite.HotFoldersTree;
import com.sos.joc.cockpit.listeners.ISchedulerInstancesListener;
import com.sos.joc.cockpit.listeners.SchedulerInstancesListenerImpl;
import com.sos.scheduler.db.SchedulerInstancesDBItem;
import com.sos.scheduler.db.SchedulerInstancesFilter;
import com.sos.scheduler.model.SchedulerObjectFactoryOptions;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class AddServerLayout extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private SchedulerObjectFactoryOptions options = null;
	private List<SchedulerInstancesDBItem> schedulerInstances;
	private ProgressBar progress = new ProgressBar();
	
	public AddServerLayout() {
		progress.setIndeterminate(true);
		progress.setVisible(true);
		addComponent(progress);
		setImmediate(true);
		initSchedulerInstances();
		
	}
	
	private void initSchedulerInstances(){
		filterData(new SchedulerInstancesListenerImpl(), null);
	}
	
	private void initComponents(){
		List<String> activeServers = new ArrayList<String>();
		for(SchedulerInstancesDBItem schedulerInstance : schedulerInstances){
//			if(schedulerInstance.getIsRunning()){
				activeServers.add(schedulerInstance.getHostName() + ":" + schedulerInstance.getTcpPort());
//			}
		}
		NativeSelect nsServer = new NativeSelect("Server", activeServers);
		nsServer.setWidth(200.0f, Unit.PIXELS);
		addComponent(nsServer);
		nsServer.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				options = new SchedulerObjectFactoryOptions();
				String[] values = event.getProperty().getValue().toString().split(":");
				options.ServerName.Value(values[0]);
				options.PortNumber.Value(values[1]);
				
			}
		});
		HorizontalLayout hl = new HorizontalLayout();
		addComponent(hl);
		Button btnOK = new Button("OK", new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				HotFoldersTree hotFolders = new HotFoldersTree();
				hotFolders.initTree(options);
				TabSheet ts = ((HotFolders)((JOCCockpitUI)getUI().getCurrent()).getHLayout().getFirstComponent()).getTsServers();
//				((HotFolders)((JOCCockpitUI)getUI().getCurrent()).getHLayout().getFirstComponent()).
//				getTsServers()
				ts.addTab(hotFolders, options.ServerName.Value() + ":" + options.PortNumber.Value(), null, ts.getComponentCount() - 1);
				ts.getTab(hotFolders).setClosable(true);
				((JOCCockpitUI)getUI().getCurrent()).getAddServerWindow().close();
			}
		});
		hl.addComponent(btnOK);
		Button btnDiscard = new Button("discard", new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				options = null;
				((JOCCockpitUI)getUI().getCurrent()).getAddServerWindow().close();
			}
		});
		hl.addComponent(btnDiscard);
	}

	/**
	 * tells the {@link ISchedulerInstancesListener} to filter the SchedulerInstancesDBItems with the given
	 * SchedulerInstancesFilter
	 * 
	 * @param listener the {@link ISchedulerInstancesListener} which holds the methods to access the SchedulerInstancesDBLayer
	 * @param filter the SchedulerInstancesFilter to filter SchedulerInstancesDBItems and the related SchedulerInstancesDBItem with
	 */
	private void filterData(final ISchedulerInstancesListener listener, final SchedulerInstancesFilter filter) {
		new Thread() {
			@Override
	        public void run() {
	            try {
	            	listener.filterSchedulerInstances(filter);
	            } catch (final Exception e) {
	                listener.getException(e);
	            }
				UI.getCurrent().access(new Runnable() {
					@Override
					public void run() {
						progress.setVisible(false);
						schedulerInstances = listener.getSchedulerInstances();
						listener.closeSchedulerInstancesDbSession();
						initComponents();
					}
				});
	        }
		}.start();
    }
}
