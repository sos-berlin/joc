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

import com.sos.jobstreams.db.DBItemInCondition;
import com.sos.jobstreams.db.DBItemOutCondition;
import com.sos.jobstreams.db.DBLayerInConditions;
import com.sos.jobstreams.db.DBLayerOutConditions;
import com.sos.jobstreams.db.FilterInConditions;
import com.sos.jobstreams.db.FilterOutConditions;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobstreams.resource.IJobStreamFoldersResource;
import com.sos.joc.model.jobstreams.Folders2Jobstream;
import com.sos.joc.model.jobstreams.JobStreams;

@Path("jobstreams")
public class JobStreamFoldersImpl extends JOCResourceImpl implements IJobStreamFoldersResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobStreamFoldersImpl.class);
    private static final String API_CALL = "./conditions/jobstream_folders";

    @Override
    public JOCDefaultResponse jobStreamFolders(String accessToken, JobStreams jobStreams) throws Exception {
        SOSHibernateSession sosHibernateSession = null;
        try {

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobStreams, accessToken, jobStreams.getJobschedulerId(), getPermissonsJocCockpit(
                    jobStreams.getJobschedulerId(), accessToken).getJobStream().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            Map<String, Set<String>> mapOfJobStream2Folders = new HashMap<String, Set<String>>();
            DBLayerInConditions dbLayerInConditions = new DBLayerInConditions(sosHibernateSession);
            FilterInConditions filterInConditions = new FilterInConditions();
            filterInConditions.setJobStream(jobStreams.getJobStreamFilter());
            filterInConditions.setJobSchedulerId(jobStreams.getJobschedulerId());
            List<DBItemInCondition> listOfInConditions = dbLayerInConditions.getSimpleInConditionsList(filterInConditions, 0);

            for (DBItemInCondition dbItemInCondition : listOfInConditions) {
                String jobStream = dbItemInCondition.getJobStream();
                if (mapOfJobStream2Folders.get(jobStream) == null) {
                    Set<String> listOfFolders = new LinkedHashSet<String>();
                    mapOfJobStream2Folders.put(jobStream, listOfFolders);
                }
                mapOfJobStream2Folders.get(jobStream).add(Paths.get(dbItemInCondition.getJob()).getParent().toString());
            }

            DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
            FilterOutConditions filterOutConditions = new FilterOutConditions();
            filterOutConditions.setJobSchedulerId(jobStreams.getJobschedulerId());
            filterOutConditions.setJobStream(jobStreams.getJobStreamFilter());
            List<DBItemOutCondition> listOfOutConditions = dbLayerOutConditions.getSimpleOutConditionsList(filterOutConditions, 0);
            for (DBItemOutCondition dbItemOutCondition : listOfOutConditions) {
                String jobStream = dbItemOutCondition.getJobStream();
                if (mapOfJobStream2Folders.get(jobStream) == null) {
                    Set<String> listOfFolders = new LinkedHashSet<String>();
                    mapOfJobStream2Folders.put(jobStream, listOfFolders);
                }
                mapOfJobStream2Folders.get(jobStream).add(Paths.get(dbItemOutCondition.getJob()).getParent().toString());
            }

            mapOfJobStream2Folders.forEach((jobStream, value) -> {
                Folders2Jobstream folders2Jobstream = new Folders2Jobstream();
                folders2Jobstream.setJobStream(jobStream);
                mapOfJobStream2Folders.get(jobStream).forEach(folder -> {
                    folders2Jobstream.getFolders().add(folder.replaceAll("\\\\", "/"));
                });
                jobStreams.getJobStreamFolders().add(folders2Jobstream);
            });

            jobStreams.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(jobStreams);

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