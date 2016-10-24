package com.sos.joc.jobchains.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventoryJobChain;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobchains.JobChainPermanent;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobchains.resource.IJobChainsResourceP;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.jobChain.JobChainP;
import com.sos.joc.model.jobChain.JobChainPath;
import com.sos.joc.model.jobChain.JobChainStateText;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.jobChain.JobChainsP;

@Path("job_chains")
public class JobChainsResourcePImpl extends JOCResourceImpl implements IJobChainsResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobChainsResourcePImpl.class);  
    private Boolean compact;
    private List<Folder> folders;
    private List<JobChainPath> jobChainPaths;
    private String jobschedulerId;
    private String regex;
    private List<JobChainP> allNestedJobChains = new ArrayList<JobChainP>();
    private Long instanceId;
  
     
    @Override
    public JOCDefaultResponse postJobChainsP(String accessToken, JobChainsFilter jobChainsFilter) throws Exception {
        LOGGER.debug("init job_chains/p");
        JOCDefaultResponse jocDefaultResponse = init(jobChainsFilter.getJobschedulerId(),
                getPermissons(accessToken).getJobChain().getView().isStatus());
        if (jocDefaultResponse != null) {
            return jocDefaultResponse;
        }
        try {
            //FILTER
            compact = jobChainsFilter.getCompact();
            folders = jobChainsFilter.getFolders();
            jobChainPaths = jobChainsFilter.getJobChains();
            jobschedulerId = jobChainsFilter.getJobschedulerId();
            regex = jobChainsFilter.getRegex();
            
            InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            DBItemInventoryInstance instance = instanceLayer.getInventoryInstanceBySchedulerId(jobChainsFilter.getJobschedulerId());
            instanceId = instance.getId();

            JobChainsP entity = new JobChainsP();
            InventoryJobChainsDBLayer dbLayer = new InventoryJobChainsDBLayer(Globals.sosHibernateConnection);
            List<JobChainP> jobChains = new ArrayList<JobChainP>();
            if (jobChainPaths != null && !jobChainPaths.isEmpty()) {
                for (JobChainPath jobChainPath : jobChainPaths) {
                    DBItemInventoryJobChain jobChainFromDb = dbLayer.getJobChainByPath(jobChainPath.getJobChain(), instanceId);
                    JobChainP jobChain = JobChainPermanent.initJobChainP(dbLayer, jobChainFromDb, compact, instanceId);
                    if (jobChain != null) {
                        jobChains.add(jobChain);
                        initNestedJobChainsIfExists(dbLayer, jobChain);
                    }
                }
            } else if (folders != null && !folders.isEmpty()) {
                for (Folder folder : folders) {
                    List<DBItemInventoryJobChain> jobChainsFromDb =
                            dbLayer.getJobChainsByFolder(folder.getFolder(), folder.getRecursive(), instanceId);
                    for(DBItemInventoryJobChain jobChainFromDb : jobChainsFromDb) {
                        JobChainP jobChain = null;
                        if (regex != null && !regex.isEmpty()) {
                            Matcher regExMatcher = Pattern.compile(regex).matcher(jobChainFromDb.getName());
                            if (regExMatcher.find()) {
                                jobChain = JobChainPermanent.initJobChainP(dbLayer, jobChainFromDb, compact, instanceId); 
                            }
                        } else {
                            jobChain = JobChainPermanent.initJobChainP(dbLayer, jobChainFromDb, compact, instanceId);
                        }
                        if (jobChain != null) {
                            jobChains.add(jobChain);
                            initNestedJobChainsIfExists(dbLayer, jobChain);
                        }
                    }
                }
            } else {
                List<DBItemInventoryJobChain> jobChainsFromDb = dbLayer.getJobChains(instanceId);
                for(DBItemInventoryJobChain jobChainFromDb : jobChainsFromDb) {
                    JobChainP jobChain = null;
                    if (regex != null && !regex.isEmpty()) {
                        Matcher regExMatcher = Pattern.compile(regex).matcher(jobChainFromDb.getName());
                        if (regExMatcher.find()) {
                            jobChain = JobChainPermanent.initJobChainP(dbLayer, jobChainFromDb, compact, instanceId);
                        }
                    } else {
                        jobChain = JobChainPermanent.initJobChainP(dbLayer, jobChainFromDb, compact, instanceId);
                    }
                    if (jobChain != null) {
                        jobChains.add(jobChain);
                        initNestedJobChainsIfExists(dbLayer, jobChain);
                    }
                }
            }
            if(compact != null && !compact) {
                if(allNestedJobChains != null && !allNestedJobChains.isEmpty()) {
                    entity.setNestedJobChains(allNestedJobChains);
                }else {
                    entity.setNestedJobChains(null);
                }
            } else {
                entity.setNestedJobChains(null);
            }
            entity.setJobChains(jobChains);
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }

    private void initNestedJobChainsIfExists(InventoryJobChainsDBLayer dbLayer, JobChainP jobChain) throws Exception {
        if(!JobChainPermanent.NESTED_JOB_CHAIN_NAMES.isEmpty()) {
            List<JobChainP> nestedJobChains = new ArrayList<JobChainP>();
            for(String nestedJobChainName : JobChainPermanent.NESTED_JOB_CHAIN_NAMES) {
                DBItemInventoryJobChain nestedJobChain = null;
                if(nestedJobChainName.contains("/")) {
                    nestedJobChain = dbLayer.getJobChainByPath(nestedJobChainName, instanceId);
                } else {
                    nestedJobChain = dbLayer.getJobChainByName(nestedJobChainName, instanceId);
                }
                if (nestedJobChain != null) {
                    JobChainP nestedJobChainP = JobChainPermanent.initJobChainP(dbLayer, nestedJobChain, compact, instanceId);
                    if (nestedJobChainP != null) {
                        nestedJobChains.add(nestedJobChainP); 
                    }
                }
            }
            if(!nestedJobChains.isEmpty()) {
                allNestedJobChains.addAll(nestedJobChains);
                JobChainPermanent.NESTED_JOB_CHAIN_NAMES.clear();
            }
        }
    }
    
}