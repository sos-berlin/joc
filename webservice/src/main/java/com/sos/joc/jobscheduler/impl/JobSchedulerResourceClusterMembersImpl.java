package com.sos.joc.jobscheduler.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.jobscheduler.JobSchedulerVolatile;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerResourceClusterMembers;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.jobscheduler.JobSchedulerState;
import com.sos.joc.model.jobscheduler.JobSchedulerStateText;
import com.sos.joc.model.jobscheduler.JobSchedulerV;
import com.sos.joc.model.jobscheduler.MastersV;

@Path("jobscheduler")
public class JobSchedulerResourceClusterMembersImpl extends JOCResourceImpl implements IJobSchedulerResourceClusterMembers {

    private static final String API_CALL = "./jobscheduler/cluster/members";

    @Override
    public JOCDefaultResponse postJobschedulerClusterMembers(String accessToken, JobSchedulerId jobSchedulerFilter) {
        String jobSchedulerId = jobSchedulerFilter.getJobschedulerId();
        try {
            initLogging(API_CALL, jobSchedulerFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerId, getPermissons(accessToken)
                    .getJobschedulerMasterCluster().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(this);
            boolean isUnreachable = false;
            List<JobSchedulerV> masters = new ArrayList<JobSchedulerV>();
            try {
                String clusterCommand = jocXmlCommand.getShowStateCommand("folder", "folders no_subfolders", "/does/not/exist");
                jocXmlCommand.executePostWithRetry(clusterCommand, accessToken);
            } catch (JocException e) {
                isUnreachable = true;
            }
            
            if (isUnreachable) {
                InventoryInstancesDBLayer instanceLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
                List<DBItemInventoryInstance> schedulersFromDb = instanceLayer.getInventoryInstancesBySchedulerId(jobSchedulerId);
                if(schedulersFromDb != null && !schedulersFromDb.isEmpty()) {
                    for (DBItemInventoryInstance instance : schedulersFromDb) {
                        JobSchedulerV jobscheduler = new JobSchedulerV();
                        jobscheduler.setJobschedulerId(jobSchedulerId);
                        jobscheduler.setHost(instance.getHostname());
                        jobscheduler.setPort(instance.getPort());
                        jobscheduler.setStartedAt(null);
                        JobSchedulerState state = new JobSchedulerState();
                        state.setSeverity(2);
                        state.set_text(JobSchedulerStateText.UNREACHABLE);
                        jobscheduler.setState(state);
                        jobscheduler.setSurveyDate(null);
                    }
                }
            } else {
                NodeList clusterMembers = jocXmlCommand.getSosxml().selectNodeList("/spooler/answer/state/cluster/cluster_member");

                InventoryInstancesDBLayer instancesDbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
                Globals.beginTransaction();
                List<DBItemInventoryInstance> schedulerInstances = instancesDbLayer.getInventoryInstancesBySchedulerId(jobSchedulerId);

                if (clusterMembers.getLength() == 0) {
                    // standalone
                    JobSchedulerV jobscheduler = new JobSchedulerVolatile(schedulerInstances.get(0), accessToken).getJobScheduler();
                    masters.add(jobscheduler);
                } else {
                    Map<String, DBItemInventoryInstance> jobSchedulerClusterMemberUrls = new HashMap<String, DBItemInventoryInstance>();
                    for (DBItemInventoryInstance schedulerInstance : schedulerInstances) {
                        String clusterMemberKey = schedulerInstance.getHostname() + ":" + schedulerInstance.getPort();
                        jobSchedulerClusterMemberUrls.put(clusterMemberKey.toLowerCase(), schedulerInstance);
                    }
                    Date surveyDate = jocXmlCommand.getSurveyDate();

                    for (int i = 0; i < clusterMembers.getLength(); i++) {
                        Element clusterMember = (Element) clusterMembers.item(i);
                        JobSchedulerState state = new JobSchedulerState();
                        JobSchedulerV jobscheduler = null;
                        String port = clusterMember.getAttribute("http_port");
                        if (port == null || port.isEmpty()) {
                            port = "0";
                        }
                        if ("yes".equals(clusterMember.getAttribute("dead"))) {
                            state.setSeverity(2);
                            state.set_text(JobSchedulerStateText.DEAD);
                            jobscheduler = getJobScheduler(clusterMember, state, jobSchedulerId, port, surveyDate);
                        } else {
                            DBItemInventoryInstance schedulerInstance = jobSchedulerClusterMemberUrls.get(clusterMember.getAttribute("host").toLowerCase()
                                    + ":" + port);
                            if (schedulerInstance != null) {
                                jobscheduler = new JobSchedulerVolatile(schedulerInstance, accessToken).getJobScheduler();
                            } else {
                                if ("yes".equals(clusterMember.getAttribute("active"))) {
                                    state.setSeverity(0);
                                    state.set_text(JobSchedulerStateText.RUNNING);
                                    // TODO RUNNING is not necessarily right. It could be PAUSED too
                                } else if ("yes".equals(clusterMember.getAttribute("backup"))) {
                                    state.setSeverity(3);
                                    state.set_text(JobSchedulerStateText.WAITING_FOR_ACTIVATION);
                                }
                                jobscheduler = getJobScheduler(clusterMember, state, jobSchedulerId, port, surveyDate);
                            }
                        }
                        if (jobscheduler != null) {
                            masters.add(jobscheduler);
                        }
                    }
                }
            }

            MastersV entity = new MastersV();
            entity.setMasters(masters);
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.rollback();
        }
    }

    private JobSchedulerV getJobScheduler(Element clusterMember, JobSchedulerState state, String id, String port, Date surveyDate) {
        JobSchedulerV jobscheduler = new JobSchedulerV();
        jobscheduler.setHost(clusterMember.getAttribute("host"));
        jobscheduler.setJobschedulerId(id);
        jobscheduler.setPort(Integer.parseInt(port));
        jobscheduler.setStartedAt(JobSchedulerDate.getDateFromISO8601String(clusterMember.getAttribute("running_since")));
        jobscheduler.setState(state);
        jobscheduler.setSurveyDate(surveyDate);
        return jobscheduler;
    }
}
