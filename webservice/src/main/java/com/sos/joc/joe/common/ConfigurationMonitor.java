package com.sos.joc.joe.common;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.jitl.reporting.db.DBItemInventoryJobChain;
import com.sos.jitl.reporting.db.DBItemInventoryJobChainNode;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.job.Job;
import com.sos.joc.model.joe.job.Monitor;
import com.sos.joc.model.joe.job.Script;
import com.sos.joc.model.joe.jobchain.JobChain;
import com.sos.joc.model.joe.jobchain.JobChainNode;
import com.sos.joc.model.joe.nodeparams.Config;
import com.sos.joc.model.joe.nodeparams.ConfigNode;
import com.sos.joc.model.joe.nodeparams.ConfigOrder;

public class ConfigurationMonitor {

    private static final String NAME = "configuration_monitor";
    private static final String LANGUAGE = "java";
    private static final String JAVACLASS = "com.sos.jitl.jobchainnodeparameter.monitor.JobchainNodeSubstituteMonitor";
    private static final String EXTENSION = ".job.xml";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationMonitor.class);
    private final SOSHibernateSession sosHibernateSession;
    private final JOCHotFolder jocHotFolder;
    private final String account;
    private final Long instanceId;
    private final String jobschedulerId;
    private Set<DBItemJoeObject> nodeParams = new HashSet<DBItemJoeObject>();

    public ConfigurationMonitor(SOSHibernateSession sosHibernateSession, JOCHotFolder jocHotFolder, Long instanceId, String jobschedulerId,
            String bodyAccount, String curAccount) {
        this.sosHibernateSession = sosHibernateSession;
        this.jocHotFolder = jocHotFolder;
        this.instanceId = instanceId;
        this.jobschedulerId = jobschedulerId;
        if (bodyAccount == null || bodyAccount.isEmpty()) {
            this.account = curAccount;
        } else {
            this.account = bodyAccount;
        }
    }

    public Set<DBItemJoeObject> addConfigurationMonitor(Set<DBItemJoeObject> curJobs) {
        if (curJobs == null) {
            curJobs = new HashSet<DBItemJoeObject>();
        }
        if (!nodeParams.isEmpty() && instanceId != null && jobschedulerId != null && !jobschedulerId.isEmpty()) {
            Set<String> jobs = new HashSet<String>();
            for (DBItemJoeObject nodeParam : nodeParams) {
                jobs.addAll(getJobs(instanceId, jobschedulerId, nodeParam.getPath(), nodeParam.getConfiguration()));
            }
            // change current jobs from same deploy
            for (DBItemJoeObject curJob : curJobs) {
                if (curJob.getConfiguration() == null) {
                    continue;
                }
                if (jobs.contains(curJob.getPath())) {
                    jobs.remove(curJob.getPath());
                } else {
                    continue;
                }
                String conf = null;
                try {
                    Job jobPojo = Globals.objectMapper.readValue(curJob.getConfiguration(), Job.class);
                    if (jobHasConfigurationMonitor(jobPojo)) {
                        jobs.remove(curJob.getPath());
                    } else {
                        jobPojo = addConfigurationMonitor(jobPojo);
                        conf = Globals.objectMapper.writeValueAsString(jobPojo);
                    }
                } catch (Exception e) {
                    LOGGER.warn("", e);
                }
                if (conf != null) {
                    curJob.setConfiguration(conf);
                }
            }
            // load other Jobs from Hot Folder into the deploy if necessary
            for (String liveJob : jobs) {
                String conf = null;
                try {
                    Job jobPojo = Globals.xmlMapper.readValue(jocHotFolder.getFile(liveJob + EXTENSION), Job.class);
                    if (jobPojo != null && !jobHasConfigurationMonitor(jobPojo)) {
                        jobPojo = addConfigurationMonitor(jobPojo);
                        conf = Globals.objectMapper.writeValueAsString(jobPojo);
                    }
                } catch (Exception e) {
                    LOGGER.warn("", e);
                }
                if (conf != null) {
                    DBItemJoeObject liveJobItem = new DBItemJoeObject();
                    liveJobItem.setId(null);
                    liveJobItem.setConfiguration(conf);
                    liveJobItem.setPath(liveJob);
                    liveJobItem.setObjectType(JobSchedulerObjectType.JOB.value());
                    liveJobItem.setAccount(account);
                    liveJobItem.setOperation("store");
                    liveJobItem.setSchedulerId(jobschedulerId);
                    liveJobItem.setFolder(Paths.get(liveJob).getParent().toString().replace('\\', '/'));
                    // liveJobItem.setModified(jocHotFolder.getLastModifiedDate());
                    curJobs.add(liveJobItem);
                }
            }
        }
        return curJobs;
    }
    
    public void addNodeParams(DBItemJoeObject nodeParams) {
        this.nodeParams.add(nodeParams);
    }

    private Set<String> getJobs(Long instanceId, String jobschedulerId, String nodeParamsPath, String nodeParamsConfiguration) {
        Set<String> states = getStates(nodeParamsConfiguration);
        if (states != null) {
            String jobChain = getJobChainPath(nodeParamsPath);
            Set<String> jobs = getJobsFromDraft(jobschedulerId, jobChain, states);
            jobs.addAll(getJobsFromInventory(instanceId, jobChain, states));
            return jobs;
        }
        return new HashSet<String>();
    }

    private Set<String> getJobsFromInventory(Long instanceId, String jobChain, Set<String> states) {
        if (instanceId == null || jobChain == null || jobChain.isEmpty()) {
            return new HashSet<String>();
        }
        InventoryJobChainsDBLayer dbLayer = new InventoryJobChainsDBLayer(sosHibernateSession);
        try {
            DBItemInventoryJobChain dbItem = dbLayer.getJobChainByPath(jobChain, instanceId);
            if (dbItem != null) {
                List<DBItemInventoryJobChainNode> nodes = dbLayer.getJobChainNodesByJobChainId(dbItem.getId(), instanceId);
                if (nodes != null && !nodes.isEmpty()) {
                    if (states.isEmpty()) {
                        return nodes.stream().filter(i -> i.getNodeType() == 1).map(DBItemInventoryJobChainNode::getJobName).collect(Collectors.toSet());
                    } else {
                        return nodes.stream().filter(i -> i.getNodeType() == 1 && states.contains(i.getJob())).map(DBItemInventoryJobChainNode::getJobName)
                                .collect(Collectors.toSet());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.warn("", e);
        }
        return new HashSet<String>();
    }

    private Set<String> getJobsFromDraft(String jobschedulerId, String jobChain, Set<String> states) {
        if (jobschedulerId == null || jobschedulerId.isEmpty() || jobChain == null || jobChain.isEmpty()) {
            return new HashSet<String>();
        }
        DBLayerJoeObjects dbLayer = new DBLayerJoeObjects(sosHibernateSession);
        FilterJoeObjects filter = new FilterJoeObjects();
        filter.setObjectType(JobSchedulerObjectType.JOBCHAIN);
        filter.setPath(jobChain);
        filter.setSchedulerId(jobschedulerId);
        try {
            DBItemJoeObject dbItem = dbLayer.getJoeObject(filter);
            if (dbItem != null && dbItem.getConfiguration() != null) {
                final Path jobChainFolder = Paths.get(jobChain).getParent();
                JobChain jobChainPojo = Globals.objectMapper.readValue(dbItem.getConfiguration(), JobChain.class);
                if (jobChainPojo != null && jobChainPojo.getJobChainNodes() != null) {
                    if (states.isEmpty()) {
                        return jobChainPojo.getJobChainNodes().stream().filter(i -> i.getJob() != null).map(i -> jobChainFolder.resolve(i.getJob())
                                .toString().replace('\\', '/')).collect(Collectors.toSet());
                    } else {
                        return jobChainPojo.getJobChainNodes().stream().filter(i -> i.getJob() != null && states.contains(i.getState())).map(
                                i -> jobChainFolder.resolve(i.getJob()).toString().replace('\\', '/')).collect(Collectors.toSet());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.warn("", e);
        }
        return new HashSet<String>();
    }

    private String getJobChainPath(String nodeParamsPath) {
        if (nodeParamsPath.contains(",")) {
            return nodeParamsPath.split(",", 2)[0];
        }
        return nodeParamsPath;
    }

    /** returns empty list if params are defined for all nodes returns null if no params are defined
     * 
     * @param nodeParamsConfiguration
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException */
    private Set<String> getStates(String nodeParamsConfiguration) {
        try {
            Config nodeParams = Globals.objectMapper.readValue(nodeParamsConfiguration, Config.class);
            Set<String> states = new HashSet<String>();
            if (nodeParams == null || nodeParams.getJobChain() == null || nodeParams.getJobChain().getOrder() == null) {
                return null;
            }
            ConfigOrder configOrder = nodeParams.getJobChain().getOrder();
            if (configOrder.getParams() != null && configOrder.getParams().getParamList() != null && configOrder.getParams().getParamList().size() > 0) {
                return states;
            }
            if (configOrder.getJobChainNodes() != null) {
                for (ConfigNode configNode : configOrder.getJobChainNodes()) {
                    if (configNode.getParams() != null && configNode.getParams().getParamList() != null && configNode.getParams().getParamList()
                            .size() > 0) {
                        states.add(configNode.getState());
                    }
                }
                if (states.isEmpty()) {
                    return null;
                }
                return states;
            }
        } catch (Exception e) {
            LOGGER.warn("", e);
        }
        return null;
    }

    private Predicate<Monitor> containsConfigurationMonitor = i -> i.getScript() != null && LANGUAGE.equals(i.getScript().getLanguage()) && JAVACLASS
            .equals(i.getScript().getJavaClass());

//    private Predicate<Monitor> notContainsConfigurationMonitor = i -> i.getScript() == null || !LANGUAGE.equals(i.getScript().getLanguage())
//            || !JAVACLASS.equals(i.getScript().getJavaClass());

    private Monitor create() {
        Monitor monitor = new Monitor();
        monitor.setOrdering(0);
        monitor.setName(NAME);
        Script script = new Script();
        script.setLanguage(LANGUAGE);
        script.setJavaClass(JAVACLASS);
        monitor.setScript(script);
        return monitor;
    }
    
    private boolean jobHasConfigurationMonitor(Job job) {
        if (job == null) {
            return true;
        }
        return jobHasConfigurationMonitor(job.getMonitors());
    }
    
    private boolean jobHasConfigurationMonitor(List<Monitor> monitors) {
        if (monitors == null) {
            return false;
        }
        return monitors.stream().filter(containsConfigurationMonitor).count() > 0;
    }

    private List<Monitor> addConfigurationMonitor(List<Monitor> monitors) {
        if (jobHasConfigurationMonitor(monitors)) {
            return monitors;
        }
        if (monitors == null) {
            monitors = new ArrayList<Monitor>();
        }
        monitors.add(0, create());
        return monitors;
    }

    private Job addConfigurationMonitor(Job job) {
        job.setMonitors(addConfigurationMonitor(job.getMonitors()));
        return job;
    }

//    private List<Monitor> deleteConfigurationMonitor(List<Monitor> monitors) {
//        if (monitors == null || !jobHasConfigurationMonitor(monitors)) {
//            return monitors;
//        }
//        return monitors.stream().filter(notContainsConfigurationMonitor).collect(Collectors.toList());
//    }
//
//    private Job deleteConfigurationMonitor(Job job) {
//        job.setMonitors(deleteConfigurationMonitor(job.getMonitors()));
//        return job;
//    }

}
