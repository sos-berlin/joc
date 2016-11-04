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
import com.sos.scheduler.model.commands.JSCmdShowState;

@Path("jobscheduler")
public class JobSchedulerResourceClusterMembersImpl extends JOCResourceImpl implements IJobSchedulerResourceClusterMembers {

    private static final String API_CALL = "./jobscheduler/cluster/members";

    @Override
    public JOCDefaultResponse postJobschedulerClusterMembers(String accessToken, JobSchedulerId jobSchedulerFilter) {
        try {
            initLogging(API_CALL, jobSchedulerFilter);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, jobSchedulerFilter.getJobschedulerId(), getPermissons(accessToken)
                    .getJobschedulerMasterCluster().getView().isClusterStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            jocXmlCommand.executePostWithThrowBadRequest(getXMLClusterCommand(), accessToken);
            NodeList clusterMembers = jocXmlCommand.getSosxml().selectNodeList("/spooler/answer/state/cluster/cluster_member");

            InventoryInstancesDBLayer instancesDbLayer = new InventoryInstancesDBLayer(Globals.sosHibernateConnection);
            Globals.beginTransaction();
            List<DBItemInventoryInstance> schedulerInstances = instancesDbLayer.getInventoryInstancesBySchedulerId(jobSchedulerFilter
                    .getJobschedulerId());
            List<JobSchedulerV> masters = new ArrayList<JobSchedulerV>();

            if (clusterMembers.getLength() == 0) {
                // standalone
                JobSchedulerV jobscheduler = new JobSchedulerVolatile(schedulerInstances.get(0), accessToken).getJobScheduler();
                masters.add(jobscheduler);
            } else {
                Map<String, DBItemInventoryInstance> jobSchedulerClusterMemberUrls = new HashMap<String, DBItemInventoryInstance>();
                for (DBItemInventoryInstance schedulerInstance : schedulerInstances) {
                    String clusterMemberId = schedulerInstance.getHostname() + ":" + schedulerInstance.getPort();
                    jobSchedulerClusterMemberUrls.put(clusterMemberId.toLowerCase(), schedulerInstance);
                }
                Date surveyDate = jocXmlCommand.getSurveyDate();

                for (int i = 0; i < clusterMembers.getLength(); i++) {
                    Element clusterMember = (Element) clusterMembers.item(i);
                    JobSchedulerState state = new JobSchedulerState();
                    JobSchedulerV jobscheduler = null;
                    if ("yes".equals(clusterMember.getAttribute("dead"))) {
                        state.setSeverity(2);
                        state.set_text(JobSchedulerStateText.DEAD);
                        jobscheduler = getJobScheduler(clusterMember, state, surveyDate);
                    } else {
                        DBItemInventoryInstance schedulerInstance = jobSchedulerClusterMemberUrls.get(clusterMember.getAttribute("host").toLowerCase()
                                + ":" + clusterMember.getAttribute("tcp_port"));
                        if (schedulerInstance != null) {
                            jobscheduler = new JobSchedulerVolatile(schedulerInstance, accessToken).getJobScheduler();
                        } else {
                            if ("yes".equals(jocXmlCommand.getAttribute("active"))) {
                                state.setSeverity(0);
                                state.set_text(JobSchedulerStateText.RUNNING);
                                // TODO Running is not necessarily right
                            } else if ("yes".equals(jocXmlCommand.getAttribute("backup"))) {
                                state.setSeverity(3);
                                state.set_text(JobSchedulerStateText.WAITING_FOR_ACTIVATION);
                            }
                            jobscheduler = getJobScheduler(clusterMember, state, surveyDate);
                        }
                    }
                    if (jobscheduler != null) {
                        masters.add(jobscheduler);
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

    private String getXMLClusterCommand() {
        JSCmdShowState jsCmdShowState = Globals.schedulerObjectFactory.createShowState();
        jsCmdShowState.setWhat("folders no_subfolders cluster");
        jsCmdShowState.setPath("/does/not/exist");
        jsCmdShowState.setSubsystems("folder");
        return jsCmdShowState.toXMLString();
    }

    private JobSchedulerV getJobScheduler(Element clusterMember, JobSchedulerState state, Date surveyDate) {
        JobSchedulerV jobscheduler = new JobSchedulerV();
        jobscheduler.setHost(clusterMember.getAttribute("host"));
        jobscheduler.setJobschedulerId(clusterMember.getAttribute("cluster_member_id").replaceFirst("/[^/]+$", ""));
        jobscheduler.setPort(Integer.parseInt(clusterMember.getAttribute("tcp_port")));
        jobscheduler.setStartedAt(JobSchedulerDate.getDateFromISO8601String(clusterMember.getAttribute("running_since")));
        jobscheduler.setState(state);
        jobscheduler.setSurveyDate(surveyDate);
        return jobscheduler;
    }
}
