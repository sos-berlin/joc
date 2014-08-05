package com.sos.joc.cockpit.listeners;

import java.util.List;

import com.sos.scheduler.db.SchedulerInstancesDBItem;
import com.sos.scheduler.db.SchedulerInstancesFilter;


public interface ISchedulerInstancesListener {

	/**
	 * filters the SchedulerInstancesDBItems with the given SchedulerInstancesFilter 
	 * @param filter the SchedulerInstancesFilter
	 */
	void filterSchedulerInstances(SchedulerInstancesFilter filter);

	void getException(Exception e);
	
	void closeSchedulerInstancesDbSession();
	
	List<SchedulerInstancesDBItem> getSchedulerInstances();
}
