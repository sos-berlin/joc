package com.sos.joc.cockpit.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.scheduler.model.LiveConnector;
import com.sos.scheduler.model.SchedulerHotFolder;
import com.sos.scheduler.model.SchedulerHotFolderFileList;
import com.sos.scheduler.model.SchedulerObjectFactory;
import com.sos.scheduler.model.SchedulerObjectFactoryOptions;
import com.sos.scheduler.model.answers.Answer;
import com.sos.scheduler.model.answers.ERROR;
import com.sos.scheduler.model.answers.State;
import com.sos.scheduler.model.commands.JSCmdShowState;
import com.sos.scheduler.model.exceptions.JSCommandErrorException;


public class JobSchedulerCommand implements Serializable {
	
	private static final long serialVersionUID = 5355906604171033202L;
	private final String conClassName = "JOCCockpitUI";
	private final static Logger logger = LoggerFactory.getLogger(JobSchedulerCommand.class);
	private SchedulerObjectFactory objSchedulerObjectFactory = null;
	
	public JobSchedulerCommand() {
		if (objSchedulerObjectFactory == null) {
			objSchedulerObjectFactory = new SchedulerObjectFactory();
		}
	}
	
	public SchedulerObjectFactory getSchedulerObjectFactory() {
		return objSchedulerObjectFactory;
	}

	public void fetchState(final StateListener updater, final SchedulerObjectFactoryOptions options, final String what, final String subsystems, final String path) {
        new Thread() {
            @Override
            public void run() {
                try {
                	updater.getState(fetchState(options, what, subsystems, path));
                } catch (final Exception e) {
                    e.printStackTrace();
                    updater.getException(e);
                }
            };
        }.start();
    }
	
	
	public void fetchState(final StateListener updater, final SchedulerObjectFactoryOptions options) {
		fetchState(updater, options, null, null, null);
    }
	
	
	
	public void fetchHotFolder(final HotfolderListener updater, final String liveFolderName) {
        new Thread() {
            @Override
            public void run() {
                try {
                	updater.getHotFolderFileList(fetchHotFolder(liveFolderName));
                } catch (final Exception e) {
                    e.printStackTrace();
                    updater.getException(e);
                }
            };
        }.start();
    }
	
	
	private SchedulerHotFolderFileList fetchHotFolder(final String liveFolderName)  throws MalformedURLException {
		LiveConnector connector = new LiveConnector( new URL(liveFolderName) );
		SchedulerHotFolder hotFolder = objSchedulerObjectFactory.createSchedulerHotFolder(connector.getHotFolderHandle());
//		SchedulerHotFolderFileList fileList = hotFolder.loadRecursive();
		SchedulerHotFolderFileList fileList = hotFolder.getHotFolderFileList();
		return fileList;
	}
	

//	<show_state subsystems="job order standing_order lock schedule process_class folder" what="folders job_chains" path="/" max_orders="10" max_task_history="0"/>
	
	private JOCState fetchState(final SchedulerObjectFactoryOptions options, final String what, final String subsystems, final String path) {
		
		objSchedulerObjectFactory.Options().ServerName.Value(options.ServerName.Value());
		objSchedulerObjectFactory.Options().PortNumber.value(options.PortNumber.value());
		State state = null;
		
		String whatAttribute = "folders no_subfolders";
		if (what != null) {
			whatAttribute = what;
		}
		String subsystemsAttribute = "folder";
		if (subsystems != null) {
			subsystemsAttribute = subsystems;
		}
		String pathAttribute = "_null_";
		if (path != null) {
			pathAttribute = path;
		}
		
//		JSCmdCommands command = objSchedulerObjectFactory.createCmdCommands();
//		List<Object> commands = command.getAddJobsOrAddOrderOrCheckFolders();
		JSCmdShowState objShowState = objSchedulerObjectFactory.createShowState();
		if (subsystemsAttribute.length() != 0) {
			objShowState.setSubsystems(subsystems);
		}
		if (whatAttribute.length() != 0) {
			objShowState.setWhat(whatAttribute);
		}
		if (pathAttribute.length() != 0) {
			objShowState.setPath(path);
		}
		objShowState.setMaxOrderHistory(BigInteger.valueOf(0));
		objShowState.setMaxOrders(BigInteger.valueOf(0));
		objShowState.setMaxTaskHistory(BigInteger.valueOf(0));
//		commands.add(objShowState);
//		System.out.println( command.toXMLString() );
//		command.run();
//		Answer objAnswer = command.getAnswer();
//		System.out.println( objShowState.toXMLString() );
		logger.info(objShowState.toXMLString());
		objShowState.run();
		Answer objAnswer = objShowState.getAnswer();
		
		objSchedulerObjectFactory.closeSocket();
		
		ERROR objERROR = objAnswer.getERROR();
		if (objERROR != null) {
			throw new JSCommandErrorException(objERROR.getText());
		}
		state = objAnswer.getState();
		if (state == null) {
			throw new JSCommandErrorException("fetchState: unknown error");
		}
//		logger.info(objSchedulerObjectFactory.answerToXMLString(state));
		JOCState jsState = new JOCState(state, options);
		return jsState;
	}


	public interface StateListener {
		void getState(JOCState state);
		void getException(Exception e);
	}
	
	public interface HotfolderListener {
		void getHotFolderFileList(SchedulerHotFolderFileList fileList);
		void getException(Exception e);
	}
	
}
