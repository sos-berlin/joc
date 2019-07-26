package com.sos.joc.conditions.impl;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.eventhandlerservice.db.DBItemInConditionWithCommand;
import com.sos.eventhandlerservice.db.DBItemOutConditionWithEvent;
import com.sos.eventhandlerservice.db.DBLayerInConditions;
import com.sos.eventhandlerservice.db.DBLayerOutConditions;
import com.sos.eventhandlerservice.db.FilterInConditions;
import com.sos.eventhandlerservice.db.FilterOutConditions;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.conditions.resource.IWorkflowFoldersResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.conditions.WorkflowFolders;

@Path("conditions")
public class WorkflowFoldersImpl extends JOCResourceImpl implements IWorkflowFoldersResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowFoldersImpl.class);
    private static final String API_CALL = "./conditions/workflow_folders";

    @Override
    public JOCDefaultResponse workflowFolders(String accessToken, WorkflowFolders workflowFolders) throws Exception {
        SOSHibernateSession sosHibernateSession = null;
        try {

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, workflowFolders, accessToken, workflowFolders.getJobschedulerId(),
                    getPermissonsJocCockpit(workflowFolders.getJobschedulerId(), accessToken).getCondition().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            checkRequiredParameter("jobschedulerId", workflowFolders.getJobschedulerId());

            Map<String, String> mapOfFolders = new HashMap<String, String>();
            DBLayerInConditions dbLayerInConditions = new DBLayerInConditions(sosHibernateSession);
            FilterInConditions filterInConditions = new FilterInConditions();
            filterInConditions.setJobSchedulerId(workflowFolders.getJobschedulerId());
            filterInConditions.setWorkflow(workflowFolders.getWorkflow());
            List<DBItemInConditionWithCommand> listOfInConditions = dbLayerInConditions.getInConditionsList(filterInConditions, 0);
            for (DBItemInConditionWithCommand dbItemInCondition : listOfInConditions) {
                mapOfFolders.put(Paths.get(dbItemInCondition.getJob()).getParent().toString(), dbItemInCondition.getDbItemInCondition()
                        .getWorkflow());
            }

            DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
            FilterOutConditions filterOutConditions = new FilterOutConditions();
            filterOutConditions.setJobSchedulerId(workflowFolders.getJobschedulerId());
            filterOutConditions.setWorkflow(workflowFolders.getWorkflow());
            List<DBItemOutConditionWithEvent> listOfOutConditions = dbLayerOutConditions.getOutConditionsList(filterOutConditions, 0);
            for (DBItemOutConditionWithEvent dbItemOutCondition : listOfOutConditions) {
                mapOfFolders.put(Paths.get(dbItemOutCondition.getJob()).getParent().toString(), dbItemOutCondition.getDbItemOutCondition()
                        .getWorkflow());
            }

            mapOfFolders.forEach((key, value) -> workflowFolders.getWorkflowFolders().add(key.replaceAll("\\\\", "/")));

            workflowFolders.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(workflowFolders);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

}