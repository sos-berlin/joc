package com.sos.joc.cockpit;

import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;

import org.vaadin.peter.contextmenu.ContextMenu;

import com.sos.joc.cockpit.model.JOCState;
import com.sos.joc.cockpit.model.JobSchedulerCommand;
import com.sos.scheduler.model.SchedulerObjectFactoryOptions;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("joc")
@Title("JOC")
@Push
public class JOCCockpitUI extends UI implements JobSchedulerCommand.StateListener
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = JOCCockpitUI.class, widgetset = "com.sos.joc.cockpit.AppWidgetSet")
    public static class Servlet extends VaadinServlet {

		/**
		 * 
		 */
		private static final long	serialVersionUID	= -3919961733811003302L;
    }
    
    
    private static final long serialVersionUID = 6285469537503889885L;
	private final String conSVNVersion = "$Id: JOCCockpitUI.java 21535 2014-01-23 08:57:04Z oh $";
	@SuppressWarnings("unused")
	private final String conClassName = "JOCCockpitUI";
	private final static Logger logger = Logger.getLogger(JOCCockpitUI.class.getName());
	private Label statusLabel;
	private Label errorLabel;
	private ProgressBar progressIndicator;
	

    @Override
    protected void init(final VaadinRequest request) {
    	logger.info(conSVNVersion);
		
		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(true);
		
		
//		Button button = new Button("Command", new Button.ClickListener() {
//
//			private static final long serialVersionUID = 8402396232781106762L;
//
//			@Override
//			public void buttonClick(final ClickEvent event) {
//				statusLabel.setCaption("wait");
//				errorLabel.setCaption("");
//				JobSchedulerCommand command = new JobSchedulerCommand();
//				try {
//					SchedulerObjectFactoryOptions options = new SchedulerObjectFactoryOptions();
//					options.ServerName.Value("homer.sos");
//					options.PortNumber.value(4432);
//					progressIndicator.setEnabled(true);
//					progressIndicator.setVisible(true);
//					command.fetchState(JOCCockpitUI.this, options);
//				} catch (Exception e) {
//					System.err.print("Exception at fetchState");
//					e.printStackTrace();
////					errorLabel.setCaption(e.getMessage());
//				}
//			}
//			
//		});
//		
//		button.setWidth("250px");
//		layout.addComponent(button);
//		
		statusLabel = new Label("", ContentMode.TEXT);
		layout.addComponent(statusLabel);
		
		errorLabel = new Label("", ContentMode.TEXT);
		layout.addComponent(errorLabel);
		
		progressIndicator = new ProgressBar();
		progressIndicator.setIndeterminate(true);
		progressIndicator.setEnabled(false);
		progressIndicator.setVisible(false);
		layout.addComponent(progressIndicator);
		
		
		
		
		HorizontalSplitPanel hLayout = new HorizontalSplitPanel(new HotFolders(), layout);
		hLayout.setSizeFull();
//		HorizontalLayout hLayout = new HorizontalLayout();
//		hLayout.setSizeFull();
//		hLayout.addComponents(new HotFolders(), layout);
		setContent(hLayout);
		

		//Contextmenu -Test
		ContextMenu contextMenu = new ContextMenu();
		contextMenu.addItem("Root Item").addItem("Sub Item");
		contextMenu.setAsContextMenuOf(layout);
		
    }

    @Override
	public void getState(final JOCState state) {
		access(new Runnable() {

			@Override
			public void run() {
				statusLabel.setCaption(state.getState().getState());
				errorLabel.setCaption("");
				progressIndicator.setEnabled(false);
				progressIndicator.setVisible(false);
			}
		});
		
	}

	@Override
	public void getException(final Exception e) {
		access(new Runnable() {

			@Override
			public void run() {
				statusLabel.setCaption("");
				errorLabel.setCaption(e.getLocalizedMessage());
				progressIndicator.setEnabled(false);
				progressIndicator.setVisible(false);
			}
		});
	}

}
