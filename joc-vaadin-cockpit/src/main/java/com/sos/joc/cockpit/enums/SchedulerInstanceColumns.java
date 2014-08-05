package com.sos.joc.cockpit.enums;

import java.util.Date;

public enum SchedulerInstanceColumns {
	ID("id", Long.class, null), SCHEDULER_ID("schedulerId", String.class, null), HOST_NAME("hostName", String.class, null),
	TCP_PORT("tcpPort", Integer.class, null), UDP_PORT("udpPort", Integer.class, null), SUPERVISOR_TCP_PORT("supervisorTcpPort", Integer.class, null),
	JETTY_HTTP_PORT("jettyHttpPort", Integer.class, null), JETTY_HTTPS_PORT("jettyHttpsPort", Integer.class, null), START_TIME("startTime", Date.class, null),
	STOP_TIME("stopTime", Date.class, null), DB_NAME("dbName", String.class, null), DB_HISTORY_TABLE_NAME("dbHistoryTableName", String.class, null),
	DB_ORDER_HISTORY_TABLE_NAME("dbOrderHistoryTableName", String.class, null), DB_ORDERS_TABLE_NAME("dbOrdersTableName", String.class, null), 
	DB_TASKS_TABLE_NAME("dbTasksTableName", String.class, null), DB_VARIABLES_TABLE_NAME("dbVariablesTableName", String.class, null), 
	WORKING_DIRECTORY("workingDirectory", String.class, null), LIVE_DIRECTORY("liveDirectory", String.class, null),
	LOG_DIR("logDir", String.class, null), INCLUDE_PATH("includePath", String.class, null), INI_PATH("iniPath", String.class, null),
	IS_SERVICE("isService", String.class, null), IS_RUNNING("isRunning", String.class, null), IS_PAUSED("isPaused", String.class, null), 
	IS_CLUSTER("isCluster", String.class, null), IS_AGENT("isAgent", String.class, null), IS_SOS_COMMAND_WEBSERVICE("isSosCommandWebservice", String.class, null), 
	PARAM("param", String.class, null), SUPERVISOR_HOST_NAME("supervisorHostName", String.class, null);
	
	private String name;
	private Class<?> type;
	private Object defaultValue;
	
	private SchedulerInstanceColumns(String name, Class<?> type, Object defaultValue) {
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
	}
	
	public String getName() {
		return name;
	}

	public Class<?> getType() {
		return type;
	}
	
	public Object getDefaultValue() {
		return defaultValue;
	}
}
