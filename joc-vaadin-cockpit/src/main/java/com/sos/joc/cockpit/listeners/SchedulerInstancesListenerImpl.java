package com.sos.joc.cockpit.listeners;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.JSHelper.io.Files.JSFile;
import com.sos.scheduler.db.SchedulerInstancesDBItem;
import com.sos.scheduler.db.SchedulerInstancesDBLayer;
import com.sos.scheduler.db.SchedulerInstancesFilter;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.UI;

public class SchedulerInstancesListenerImpl implements ISchedulerInstancesListener, Serializable{
	private static final long serialVersionUID = 1L;
	private SchedulerInstancesDBLayer schedulerInstancesDBLayer;
	private Logger log = LoggerFactory.getLogger(SchedulerInstancesListenerImpl.class);
	List<SchedulerInstancesDBItem> schedulerInstances;
	private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss.SSS");
	
	public SchedulerInstancesListenerImpl() {
		String absolutePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		this.schedulerInstancesDBLayer = new SchedulerInstancesDBLayer(new JSFile(absolutePath + "/WEB-INF/classes/hibernate.cfg.xml"));
	}

	@Override
	public void filterSchedulerInstances(SchedulerInstancesFilter filter) {
		if(filter != null){
			this.schedulerInstancesDBLayer.setFilter(filter);
		}
		getFilteredSchedulerInstances();
	}

	@Override
	public void getException(Exception e) {
		e.printStackTrace();
	}

	@Override
	public void closeSchedulerInstancesDbSession() {
		// let some time pass before closing the actual hibernate session
		new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(10000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				UI.getCurrent().access(new Runnable() {
					@Override
					public void run() {
				        schedulerInstancesDBLayer.closeSession();
						log.debug("Hibernate SESSION finally closed at " + sdf.format(new Date()) + "!");
					}
				});
			};
		}.start();
	}

	private void getFilteredSchedulerInstances(){
		initSchedulerInstancesDbSession();
		schedulerInstances = getSchedulerInstancesFromDb();
	}
	
	private List<SchedulerInstancesDBItem> getSchedulerInstancesFromDb(){
    	return schedulerInstancesDBLayer.getSchedulerInstancesList();
	}

	private void initSchedulerInstancesDbSession(){
        try {
        	schedulerInstancesDBLayer.initSession();
		} catch (Exception e) {
			try {
				log.error("Exception occurred while initializing Session for the first time" + e);
				// retry
				schedulerInstancesDBLayer.initSession();
			} catch (Exception e1) {
				log.error("Exception occurred while initializing Session for the second time" + e1);
			}
		}
	}

	@Override
	public List<SchedulerInstancesDBItem> getSchedulerInstances() {
		return schedulerInstances;
	}

}
