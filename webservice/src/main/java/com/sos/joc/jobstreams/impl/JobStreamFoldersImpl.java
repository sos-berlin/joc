package com.sos.joc.jobstreams.impl;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.jobstreams.db.DBItemInCondition;
import com.sos.jitl.jobstreams.db.DBItemOutCondition;
import com.sos.jitl.jobstreams.db.DBLayerInConditions;
import com.sos.jitl.jobstreams.db.DBLayerOutConditions;
import com.sos.jitl.jobstreams.db.FilterInConditions;
import com.sos.jitl.jobstreams.db.FilterOutConditions;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocFolderPermissionsException;
import com.sos.joc.jobstreams.resource.IJobStreamFoldersResource;
import com.sos.joc.model.jobstreams.Folders2Jobstream;
import com.sos.joc.model.jobstreams.JobStreamFolders;
import com.sos.joc.model.jobstreams.JobStreams;
import com.sos.schema.JsonValidator;

@Path("jobstreams")
public class JobStreamFoldersImpl extends JOCResourceImpl implements IJobStreamFoldersResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobStreamFoldersImpl.class);
    private static final String API_CALL = "./conditions/jobstream_folders";

    @Override
    public JOCDefaultResponse jobStreamFolders(String accessToken, byte[] filterBytes) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            JsonValidator.validateFailFast(filterBytes, JobStreams.class);
            JobStreamFolders jobStreamFolders = Globals.objectMapper.readValue(filterBytes, JobStreamFolders.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobStreamFolders, accessToken, jobStreamFolders.getJobschedulerId(),
                    getPermissonsJocCockpit(jobStreamFolders.getJobschedulerId(), accessToken).getJobStream().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            Map<String, Set<String>> mapOfJobStream2Folders = new HashMap<String, Set<String>>();
            DBLayerInConditions dbLayerInConditions = new DBLayerInConditions(sosHibernateSession);
            FilterInConditions filterInConditions = new FilterInConditions();
            filterInConditions.setJobStream(jobStreamFolders.getJobStreamFilter());
            filterInConditions.setJobSchedulerId(jobStreamFolders.getJobschedulerId());
            List<DBItemInCondition> listOfInConditions = dbLayerInConditions.getSimpleInConditionsList(filterInConditions, 0);

            for (DBItemInCondition dbItemInCondition : listOfInConditions) {
                try {
                    checkFolderPermissions(dbItemInCondition.getJob());

                    String jobStream = dbItemInCondition.getJobStream();
                    if (mapOfJobStream2Folders.get(jobStream) == null) {
                        Set<String> listOfFolders = new LinkedHashSet<String>();
                        mapOfJobStream2Folders.put(jobStream, listOfFolders);
                    }
                    mapOfJobStream2Folders.get(jobStream).add(Paths.get(dbItemInCondition.getJob()).getParent().toString());
                } catch (JocFolderPermissionsException e) {
                    LOGGER.debug("Folder permission for " + dbItemInCondition.getJob() + " is missing for inconditons");
                }
            }

            DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
            FilterOutConditions filterOutConditions = new FilterOutConditions();
            filterOutConditions.setJobSchedulerId(jobStreamFolders.getJobschedulerId());
            filterOutConditions.setJobStream(jobStreamFolders.getJobStreamFilter());
            List<DBItemOutCondition> listOfOutConditions = dbLayerOutConditions.getSimpleOutConditionsList(filterOutConditions, 0);
            for (DBItemOutCondition dbItemOutCondition : listOfOutConditions) {
                try {
                    checkFolderPermissions(dbItemOutCondition.getJob());
                    String jobStream = dbItemOutCondition.getJobStream();
                    if (mapOfJobStream2Folders.get(jobStream) == null) {
                        Set<String> listOfFolders = new LinkedHashSet<String>();
                        mapOfJobStream2Folders.put(jobStream, listOfFolders);
                    }
                    mapOfJobStream2Folders.get(jobStream).add(Paths.get(dbItemOutCondition.getJob()).getParent().toString());
                } catch (JocFolderPermissionsException e) {
                    LOGGER.debug("Folder permission for " + dbItemOutCondition.getJob() + " is missing for outconditons");
                }
            }

            mapOfJobStream2Folders.forEach((jobStream, value) -> {
                Folders2Jobstream folders2Jobstream = new Folders2Jobstream();
                folders2Jobstream.setJobStream(jobStream);
                mapOfJobStream2Folders.get(jobStream).forEach(folder -> {
                    folders2Jobstream.getFolders().add(folder.replaceAll("\\\\", "/"));
                });
                jobStreamFolders.getJobStreams().add(folders2Jobstream);
            });

            jobStreamFolders.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(jobStreamFolders);

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