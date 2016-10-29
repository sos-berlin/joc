package com.sos.joc.processClasses.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.processClass.Process;
import com.sos.joc.model.processClass.ProcessClassPath;
import com.sos.joc.model.processClass.ProcessClassV;
import com.sos.joc.model.processClass.ProcessClassesFilter;
import com.sos.joc.model.processClass.ProcessClassesV;
import com.sos.joc.processClasses.resource.IProcessClassesResource;
import com.sos.scheduler.model.commands.JSCmdShowState;

@Path("process_classes")
public class ProcessClassesResourceImpl extends JOCResourceImpl implements IProcessClassesResource {

    private static final String SLASH = "/";
    private static final String DEFAULT_NAME = "(default)";
    private static final String ATTRIBUTE_RUNNING_SINCE = "running_since";
    private static final String ATTRIBUTE_PID = "pid";
    private static final String ATTRIBUTE_TASK_ID = "task_id";
    private static final String ATTRIBUTE_JOB = "job";
    private static final String KEY_PROCESSES = "processes";
    private static final String XPATH_PROCESSES = "//process_class[@path='%s']/processes/process";
    private static final String ATTRIBUTE_PATH = "path";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String WHAT = "folders";
    private static final String SUBSYSTEMS = "folder process_class";
    private static final String XPATH_PROCESS_CLASSES = "//process_class";
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessClassesResourceImpl.class);
    private static final String API_CALL = "./process_classes";

    private ProcessClassesFilter processClassFilter;
    private HashMap<String, String> mapOfProcessClasses;

    @Override
    public JOCDefaultResponse postProcessClasses(String accessToken, ProcessClassesFilter processClassFilter) throws Exception {
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, processClassFilter.getJobschedulerId(), getPermissons(accessToken)
                    .getProcessClass().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            this.processClassFilter = processClassFilter;

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());

            JSCmdShowState jsCmdShowState = Globals.schedulerObjectFactory.createShowState();
            jsCmdShowState.setSubsystems(SUBSYSTEMS);
            jsCmdShowState.setWhat(WHAT);
            String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdShowState);
            jocXmlCommand.executePostWithThrowBadRequest(xml);

            jocXmlCommand.createNodeList(XPATH_PROCESS_CLASSES);

            int count = jocXmlCommand.getNodeList().getLength();

            List<ProcessClassV> listOfProcessClasses = new ArrayList<ProcessClassV>();
            ProcessClassesV entity = new ProcessClassesV();

            Pattern pattern = null;
            if (!("".equals(processClassFilter.getRegex()) || processClassFilter.getRegex() == null)) {
                pattern = Pattern.compile(processClassFilter.getRegex());
            }

            createProcessClassesMap();

            for (int i = 0; i < count; i++) {
                Element processClassElement = jocXmlCommand.getElementFromList(i);

                String path = jocXmlCommand.getAttributeWithDefault(ATTRIBUTE_PATH, SLASH + DEFAULT_NAME);

                boolean addProccessClass = (!processClassFilter.getProcessClasses().isEmpty() && isInProceccClassMap(path)) || (processClassFilter
                        .getProcessClasses().isEmpty() && (matchesRegex(pattern, path) && isInFolderList(path)));

                if (addProccessClass) {

                    ProcessClassV processClassV = new ProcessClassV();
                    processClassV.setSurveyDate(jocXmlCommand.getSurveyDate());
                    processClassV.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(processClassElement));
                    processClassV.setName(jocXmlCommand.getAttributeWithDefault(ATTRIBUTE_NAME, DEFAULT_NAME));
                    processClassV.setPath(path);

                    jocXmlCommand.createNodeList(KEY_PROCESSES, String.format(XPATH_PROCESSES, path));
                    List<Process> listOfProcesses = new ArrayList<Process>();
                    int countProcesses = jocXmlCommand.getNodeList(KEY_PROCESSES).getLength();
                    processClassV.setNumOfProcesses(countProcesses);

                    for (int ii = 0; ii < countProcesses; ii++) {
                        jocXmlCommand.getElementFromList(KEY_PROCESSES, ii);
                        Process process = new Process();
                        process.setJob(jocXmlCommand.getAttribute(KEY_PROCESSES, ATTRIBUTE_JOB));
                        process.setPid(jocXmlCommand.getAttributeAsInteger(KEY_PROCESSES, ATTRIBUTE_PID));
                        process.setRunningSince(jocXmlCommand.getAttributeAsDate(KEY_PROCESSES, ATTRIBUTE_RUNNING_SINCE));
                        process.setTaskId(jocXmlCommand.getAttribute(KEY_PROCESSES, ATTRIBUTE_TASK_ID));
                        // TODO agent is missing
                        listOfProcesses.add(process);
                    }

                    processClassV.setProcesses(listOfProcesses);
                    listOfProcessClasses.add(processClassV);
                }
            }
            entity.setProcessClasses(listOfProcessClasses);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);

        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, processClassFilter));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, processClassFilter));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }

    private boolean isInProceccClassMap(String path) {
        return (processClassFilter.getProcessClasses().isEmpty() || mapOfProcessClasses.get(path) != null);
    }

    private void createProcessClassesMap() throws JocMissingRequiredParameterException {
        mapOfProcessClasses = new HashMap<String, String>();
        for (ProcessClassPath processClass : processClassFilter.getProcessClasses()) {
            String processClassName = processClass.getProcessClass();
            checkRequiredParameter("processClasses.processClass", processClassName);

            mapOfProcessClasses.put(processClass.getProcessClass(), processClass.getProcessClass());
        }

    }

    private boolean isInFolderList(String path) throws JocMissingRequiredParameterException {
        if (processClassFilter.getFolders().size() == 0) {
            return true;
        }

        path = getParent(path);
        for (Folder folder : processClassFilter.getFolders()) {
            boolean isRecursive = (folder.getRecursive() || folder.getRecursive() == null);

            String folderName = folder.getFolder();
            checkRequiredParameter("folders.folder", folderName);

            folderName = normalizePath(folderName);
            path = normalizePath(path);

            if (path.equals(folderName) || (isRecursive && path.startsWith(folderName))) {
                return true;
            }

        }
        return false;
    }

}