package com.sos.joc.cockpit.containers;

import java.util.Date;
import java.util.List;

import com.sos.joc.cockpit.enums.SchedulerInstanceColumns;
import com.sos.scheduler.db.SchedulerInstancesDBItem;
import com.vaadin.data.util.IndexedContainer;

public class SchedulerInstancesContainer extends IndexedContainer {
	private List<SchedulerInstancesDBItem> schedulerInstances;
	private static final long serialVersionUID = 1L;

	public SchedulerInstancesContainer(List<SchedulerInstancesDBItem> schedulerInstances) {
		this.schedulerInstances = schedulerInstances;
		addContainerProperties();
		addItems(schedulerInstances);
	}

	private void addItems(List<SchedulerInstancesDBItem> schedulerInstances){
		
	}
	
	private void addContainerProperties(){
		addContainerProperty(SchedulerInstanceColumns.ID.getName(), SchedulerInstanceColumns.ID.getType(), SchedulerInstanceColumns.ID.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.SCHEDULER_ID.getName(), SchedulerInstanceColumns.SCHEDULER_ID.getType(), SchedulerInstanceColumns.SCHEDULER_ID.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.HOST_NAME.getName(), SchedulerInstanceColumns.HOST_NAME.getType(), SchedulerInstanceColumns.HOST_NAME.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.TCP_PORT.getName(), SchedulerInstanceColumns.TCP_PORT.getType(), SchedulerInstanceColumns.TCP_PORT.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.UDP_PORT.getName(), SchedulerInstanceColumns.UDP_PORT.getType(), SchedulerInstanceColumns.UDP_PORT.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.SUPERVISOR_TCP_PORT.getName(), SchedulerInstanceColumns.SUPERVISOR_TCP_PORT.getType(), SchedulerInstanceColumns.SUPERVISOR_TCP_PORT.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.JETTY_HTTP_PORT.getName(), SchedulerInstanceColumns.JETTY_HTTP_PORT.getType(), SchedulerInstanceColumns.JETTY_HTTP_PORT.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.JETTY_HTTPS_PORT.getName(), SchedulerInstanceColumns.JETTY_HTTPS_PORT.getType(), SchedulerInstanceColumns.JETTY_HTTPS_PORT.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.START_TIME.getName(), SchedulerInstanceColumns.START_TIME.getType(), SchedulerInstanceColumns.START_TIME.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.STOP_TIME.getName(), SchedulerInstanceColumns.STOP_TIME.getType(), SchedulerInstanceColumns.STOP_TIME.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.DB_NAME.getName(), SchedulerInstanceColumns.DB_NAME.getType(), SchedulerInstanceColumns.DB_NAME.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.DB_HISTORY_TABLE_NAME.getName(), SchedulerInstanceColumns.DB_HISTORY_TABLE_NAME.getType(), SchedulerInstanceColumns.DB_HISTORY_TABLE_NAME.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.DB_ORDER_HISTORY_TABLE_NAME.getName(), SchedulerInstanceColumns.DB_ORDER_HISTORY_TABLE_NAME.getType(), SchedulerInstanceColumns.DB_ORDER_HISTORY_TABLE_NAME.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.DB_ORDERS_TABLE_NAME.getName(), SchedulerInstanceColumns.DB_ORDERS_TABLE_NAME.getType(), SchedulerInstanceColumns.DB_ORDERS_TABLE_NAME.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.DB_TASKS_TABLE_NAME.getName(), SchedulerInstanceColumns.DB_TASKS_TABLE_NAME.getType(), SchedulerInstanceColumns.DB_TASKS_TABLE_NAME.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.DB_VARIABLES_TABLE_NAME.getName(), SchedulerInstanceColumns.DB_VARIABLES_TABLE_NAME.getType(), SchedulerInstanceColumns.DB_VARIABLES_TABLE_NAME.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.WORKING_DIRECTORY.getName(), SchedulerInstanceColumns.WORKING_DIRECTORY.getType(), SchedulerInstanceColumns.WORKING_DIRECTORY.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.LIVE_DIRECTORY.getName(), SchedulerInstanceColumns.LIVE_DIRECTORY.getType(), SchedulerInstanceColumns.LIVE_DIRECTORY.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.LOG_DIR.getName(), SchedulerInstanceColumns.LOG_DIR.getType(), SchedulerInstanceColumns.LOG_DIR.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.INCLUDE_PATH.getName(), SchedulerInstanceColumns.INCLUDE_PATH.getType(), SchedulerInstanceColumns.INCLUDE_PATH.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.INI_PATH.getName(), SchedulerInstanceColumns.INI_PATH.getType(), SchedulerInstanceColumns.INI_PATH.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.IS_SERVICE.getName(), SchedulerInstanceColumns.IS_SERVICE.getType(), SchedulerInstanceColumns.IS_SERVICE.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.IS_RUNNING.getName(), SchedulerInstanceColumns.IS_RUNNING.getType(), SchedulerInstanceColumns.IS_RUNNING.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.IS_PAUSED.getName(), SchedulerInstanceColumns.IS_PAUSED.getType(), SchedulerInstanceColumns.IS_PAUSED.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.IS_CLUSTER.getName(), SchedulerInstanceColumns.IS_CLUSTER.getType(), SchedulerInstanceColumns.IS_CLUSTER.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.IS_AGENT.getName(), SchedulerInstanceColumns.IS_AGENT.getType(), SchedulerInstanceColumns.IS_AGENT.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.IS_SOS_COMMAND_WEBSERVICE.getName(), SchedulerInstanceColumns.IS_SOS_COMMAND_WEBSERVICE.getType(), SchedulerInstanceColumns.IS_SOS_COMMAND_WEBSERVICE.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.PARAM.getName(), SchedulerInstanceColumns.PARAM.getType(), SchedulerInstanceColumns.PARAM.getDefaultValue());
		addContainerProperty(SchedulerInstanceColumns.SUPERVISOR_HOST_NAME.getName(), SchedulerInstanceColumns.SUPERVISOR_HOST_NAME.getType(), SchedulerInstanceColumns.SUPERVISOR_HOST_NAME.getDefaultValue());
	}
	
}
