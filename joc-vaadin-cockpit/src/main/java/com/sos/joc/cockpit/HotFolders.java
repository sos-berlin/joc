package com.sos.joc.cockpit;


import com.sos.joc.cockpit.composite.HotFoldersTree;
import com.sos.scheduler.model.SchedulerObjectFactoryOptions;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

public class HotFolders extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6360569365086475617L;
	private final SchedulerObjectFactoryOptions options = new SchedulerObjectFactoryOptions();
	private TabSheet tsServers;
	private HotFoldersTree hotFolders;
	
	public HotFolders() {
		
		setSizeFull();
		setSpacing(true);
		setMargin(true);
		
		
//		BeanItem<SchedulerObjectFactoryOptions> schedulerFactoryItem = new BeanItem<SchedulerObjectFactoryOptions>(options);
//		
//		FieldGroup fieldGroup = new FieldGroup(schedulerFactoryItem);
//		fieldGroup.bindMemberFields(jsForm);
//		
		
		hotFolders = new HotFoldersTree();
		
        Panel panel = new Panel();
        panel.setSizeFull();
        // removed, hotFolders are now added to the first Tab in the TabSheet
//        panel.setContent(hotFolders);
        panel.setStyleName("hotfolder-tree");
                
        addComponents(createJsFormLayout(), panel);
        setExpandRatio(panel, 1);
        
        //initial tree load
        options.ServerName.Value("homer.sos");
		options.PortNumber.value(4432);
		hotFolders.initTree(options);
		// create a TabSheet to show different scheduler instances
		createSchedulerInstancesTabSheet();
		panel.setContent(tsServers);
	}
	
	private FormLayout createJsFormLayout() {
		FormLayout form = new FormLayout();
		form.setWidth("100%");
		form.setHeight("50px");
		form.setStyleName("hotfolder-form");
		form.setImmediate(true);
		return form;
	}

	private void createSchedulerInstancesTabSheet(){
		tsServers = new TabSheet();
		tsServers.addTab(hotFolders, options.ServerName.Value() + ":" + options.PortNumber.Value());
		tsServers.getTab(hotFolders).setClosable(true);
		VerticalLayout vlDummy = new VerticalLayout(); 
		tsServers.addTab(vlDummy, "+");
		tsServers.getTab(vlDummy).setClosable(false);
		tsServers.addSelectedTabChangeListener(new SelectedTabChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if(event.getTabSheet().getSelectedTab() instanceof VerticalLayout && !(event.getTabSheet().getSelectedTab() instanceof HotFoldersTree)){
					getUI().getCurrent().addWindow(((JOCCockpitUI)getUI().getCurrent()).getAddServerWindow());
				}
			}
		});
		
	}

	public TabSheet getTsServers() {
		return tsServers;
	}
	
	
	
}
