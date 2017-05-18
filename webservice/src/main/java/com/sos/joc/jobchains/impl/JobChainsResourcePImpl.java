package com.sos.joc.jobchains.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryJobChain;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.jobchains.JobChainPermanent;
import com.sos.joc.db.inventory.jobchains.InventoryJobChainsDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobchains.resource.IJobChainsResourceP;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.jobChain.JobChainP;
import com.sos.joc.model.jobChain.JobChainPath;
import com.sos.joc.model.jobChain.JobChainsFilter;
import com.sos.joc.model.jobChain.JobChainsP;

@Path("job_chains")
public class JobChainsResourcePImpl extends JOCResourceImpl implements IJobChainsResourceP {

    private static final String API_CALL = "./job_chains/p";
    private Boolean compact;
    private List<Folder> folders;
    private List<JobChainPath> jobChainPaths;
    private String regex;
    private List<JobChainP> allNestedJobChains = new ArrayList<JobChainP>();

    @Override
    public JOCDefaultResponse postJobChainsP(String accessToken, JobChainsFilter jobChainsFilter) {

        SOSHibernateSession connection = null;

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobChainsFilter, accessToken, jobChainsFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getJobChain().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);

            compact = jobChainsFilter.getCompact();
            folders = jobChainsFilter.getFolders();
            jobChainPaths = jobChainsFilter.getJobChains();
            regex = jobChainsFilter.getRegex();

            Long instanceId = dbItemInventoryInstance.getId();

            JobChainsP entity = new JobChainsP();
            InventoryJobChainsDBLayer dbLayer = new InventoryJobChainsDBLayer(connection);
            List<JobChainP> jobChains = new ArrayList<JobChainP>();
            if (jobChainPaths != null && !jobChainPaths.isEmpty()) {
                for (JobChainPath jobChainPath : jobChainPaths) {
                    DBItemInventoryJobChain jobChainFromDb = dbLayer.getJobChainByPath(normalizePath(jobChainPath.getJobChain()), instanceId);
                    if (jobChainFromDb == null) {
                        continue;
                    }
                    JobChainP jobChain = JobChainPermanent.initJobChainP(dbLayer, jobChainFromDb, compact, instanceId);
                    if (jobChain != null) {
                        jobChains.add(jobChain);
                        initNestedJobChainsIfExists(dbLayer, jobChain);
                    }
                }
            } else if (folders != null && !folders.isEmpty()) {
                for (Folder folder : folders) {
                    List<DBItemInventoryJobChain> jobChainsFromDb = dbLayer.getJobChainsByFolder(normalizeFolder(folder.getFolder()), folder
                            .getRecursive(), instanceId);
                    if (jobChainsFromDb != null) {
                        for (DBItemInventoryJobChain jobChainFromDb : jobChainsFromDb) {
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
                }
            } else {
                List<DBItemInventoryJobChain> jobChainsFromDb = dbLayer.getJobChains(instanceId);
                if (jobChainsFromDb != null) {
                    for (DBItemInventoryJobChain jobChainFromDb : jobChainsFromDb) {
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
            }
            if (compact != null && !compact) {
                if (allNestedJobChains != null && !allNestedJobChains.isEmpty()) {
                    entity.setNestedJobChains(allNestedJobChains);
                } else {
                    entity.setNestedJobChains(null);
                }
            } else {
                entity.setNestedJobChains(null);
            }
            entity.setJobChains(jobChains);
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

    private void initNestedJobChainsIfExists(InventoryJobChainsDBLayer dbLayer, JobChainP jobChain) throws Exception {
        if (!JobChainPermanent.NESTED_JOB_CHAIN_NAMES.isEmpty()) {
            List<JobChainP> nestedJobChains = new ArrayList<JobChainP>();
            for (String nestedJobChainName : JobChainPermanent.NESTED_JOB_CHAIN_NAMES) {
                DBItemInventoryJobChain nestedJobChain = null;
                if (nestedJobChainName.contains("/")) {
                    nestedJobChain = dbLayer.getJobChainByPath(nestedJobChainName, dbItemInventoryInstance.getId());
                } else {
                    nestedJobChain = dbLayer.getJobChainByName(nestedJobChainName, dbItemInventoryInstance.getId());
                }
                if (nestedJobChain != null) {
                    JobChainP nestedJobChainP = JobChainPermanent.initJobChainP(dbLayer, nestedJobChain, compact, dbItemInventoryInstance.getId());
                    if (nestedJobChainP != null) {
                        nestedJobChains.add(nestedJobChainP);
                    }
                }
            }
            if (!nestedJobChains.isEmpty()) {
                allNestedJobChains.addAll(nestedJobChains);
                JobChainPermanent.NESTED_JOB_CHAIN_NAMES.clear();
            }
        }
    }

}