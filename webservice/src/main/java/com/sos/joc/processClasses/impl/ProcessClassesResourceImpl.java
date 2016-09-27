package com.sos.joc.processClasses.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
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
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.FoldersSchema;
import com.sos.joc.model.processClass.ProcessClass;
import com.sos.joc.model.processClass.ProcessClassVSchema;
import com.sos.joc.model.processClass.ProcessClassesFilterSchema;
import com.sos.joc.model.processClass.ProcessClassesVSchema;
import com.sos.joc.model.processClass.ProcessSchema;
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

    private ProcessClassesFilterSchema processClassFilterSchema;
    private HashMap<String, String> mapOfProcessClasses;

    @Override
    public JOCDefaultResponse postProcessClasses(String accessToken, ProcessClassesFilterSchema processClassFilterSchema) throws Exception {

        LOGGER.debug("init processClasses");
        try {
            JOCDefaultResponse jocDefaultResponse = init(processClassFilterSchema.getJobschedulerId(), getPermissons(accessToken).getProcessClass().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            this.processClassFilterSchema = processClassFilterSchema;

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getCommandUrl());

            JSCmdShowState jsCmdShowState = Globals.schedulerObjectFactory.createShowState();
            jsCmdShowState.setSubsystems(SUBSYSTEMS);
            jsCmdShowState.setWhat(WHAT);
            String xml = Globals.schedulerObjectFactory.toXMLString(jsCmdShowState);
            jocXmlCommand.excutePost(xml);

            jocXmlCommand.createNodeList(XPATH_PROCESS_CLASSES);

            int count = jocXmlCommand.getNodeList().getLength();

            List<ProcessClassVSchema> listOfProcessClasses = new ArrayList<ProcessClassVSchema>();
            ProcessClassesVSchema entity = new ProcessClassesVSchema();
            entity.setDeliveryDate(new Date());

            Pattern pattern = null;
            if (!("".equals(processClassFilterSchema.getRegex()) || processClassFilterSchema.getRegex() == null)) {
                pattern = Pattern.compile(processClassFilterSchema.getRegex());
            }

            createProcessClassesMap();

            for (int i = 0; i < count; i++) {
                Element processClassElement = jocXmlCommand.getElementFromList(i);

                String path = jocXmlCommand.getAttributeWithDefault(ATTRIBUTE_PATH, SLASH + DEFAULT_NAME);

                boolean addProccessClass = 
                        (!processClassFilterSchema.getProcessClasses().isEmpty() && isInProceccClassMap(path)) || 
                        (processClassFilterSchema.getProcessClasses().isEmpty() && (matchesRegex(pattern, path) && isInFolderList(path)));

                if (addProccessClass) {

                    ProcessClassVSchema processClassVSchema = new ProcessClassVSchema();
                    processClassVSchema.setSurveyDate(jocXmlCommand.getSurveyDate());
                    processClassVSchema.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(processClassElement));
                    processClassVSchema.setName(jocXmlCommand.getAttributeWithDefault(ATTRIBUTE_NAME, DEFAULT_NAME));
                    processClassVSchema.setPath(path);

                    jocXmlCommand.createNodeList(KEY_PROCESSES, String.format(XPATH_PROCESSES, path));
                    List<ProcessSchema> listOfProcesses = new ArrayList<ProcessSchema>();
                    int countProcesses = jocXmlCommand.getNodeList(KEY_PROCESSES).getLength();
                    processClassVSchema.setNumOfProcesses(countProcesses);

                    for (int ii = 0; ii < countProcesses; ii++) {
                        jocXmlCommand.getElementFromList(KEY_PROCESSES, ii);
                        ProcessSchema processSchema = new ProcessSchema();
                        processSchema.setJob(jocXmlCommand.getAttribute(KEY_PROCESSES, ATTRIBUTE_JOB));
                        processSchema.setPid(jocXmlCommand.getAttributeAsInteger(KEY_PROCESSES, ATTRIBUTE_PID));
                        processSchema.setRunningSince(jocXmlCommand.getAttributeAsDate(KEY_PROCESSES, ATTRIBUTE_RUNNING_SINCE));
                        processSchema.setTaskId(jocXmlCommand.getAttributeAsInteger(KEY_PROCESSES, ATTRIBUTE_TASK_ID));
                        listOfProcesses.add(processSchema);
                    }

                    processClassVSchema.setProcesses(listOfProcesses);
                    listOfProcessClasses.add(processClassVSchema);
                }
            }
            entity.setProcessClasses(listOfProcessClasses);
            return JOCDefaultResponse.responseStatus200(entity);

        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
    }

    private boolean matchesRegex(Pattern p, String path) {
        if (p != null) {
            Matcher m = p.matcher(path);
            return m.matches();
        } else {
            return true;
        }
    }

    private boolean isInProceccClassMap(String path) {
        return (processClassFilterSchema.getProcessClasses().isEmpty() || mapOfProcessClasses.get(path) != null);
    }

    private void createProcessClassesMap() {
        mapOfProcessClasses = new HashMap<String, String>();
        for (ProcessClass processClass : processClassFilterSchema.getProcessClasses()) {
            mapOfProcessClasses.put(processClass.getProcessClass(), processClass.getProcessClass());
        }

    }

    private boolean isInFolderList(String path) throws JocMissingRequiredParameterException {
        if (processClassFilterSchema.getFolders().size() == 0) {
            return true;
        }

        path = getParent(path);
        for (FoldersSchema folder : processClassFilterSchema.getFolders()) {
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