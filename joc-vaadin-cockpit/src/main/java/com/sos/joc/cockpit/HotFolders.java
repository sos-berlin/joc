package com.sos.joc.cockpit;


import com.sos.joc.cockpit.composite.HotFoldersTree;
import com.sos.scheduler.model.SchedulerObjectFactoryOptions;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class HotFolders extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6360569365086475617L;
	private final SchedulerObjectFactoryOptions options = new SchedulerObjectFactoryOptions();
	
	public HotFolders() {
		
		setSizeFull();
		setSpacing(true);
		setMargin(true);
		
		
//		BeanItem<SchedulerObjectFactoryOptions> schedulerFactoryItem = new BeanItem<SchedulerObjectFactoryOptions>(options);
//		
//		FieldGroup fieldGroup = new FieldGroup(schedulerFactoryItem);
//		fieldGroup.bindMemberFields(jsForm);
//		
		
		HotFoldersTree hotFolders = new HotFoldersTree();
		
        Panel panel = new Panel();
        panel.setSizeFull();
        panel.setContent(hotFolders);
        panel.setStyleName("hotfolder-tree");
                
        addComponents(createJsFormLayout(), panel);
        setExpandRatio(panel, 1);
        
        
        //initial tree load
        options.ServerName.Value("homer.sos");
		options.PortNumber.value(4432);
		hotFolders.initTree(options);
    }

	private FormLayout createJsFormLayout() {
		FormLayout form = new FormLayout();
		form.setWidth("100%");
		form.setHeight("50px");
		form.setStyleName("hotfolder-form");
		form.setImmediate(true);
		return form;
	}
	
}
