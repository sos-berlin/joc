package com.sos.joc.classes.jobstreams;

import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.jobstreams.db.DBItemInCondition;
import com.sos.jitl.jobstreams.db.DBItemJobStream;
import com.sos.jitl.jobstreams.db.DBItemJobStreamStarter;
import com.sos.jitl.jobstreams.db.DBItemJobStreamStarterJob;
import com.sos.jitl.jobstreams.db.DBItemOutCondition;
import com.sos.jitl.jobstreams.db.DBLayerInConditions;
import com.sos.jitl.jobstreams.db.DBLayerJobStreams;
import com.sos.jitl.jobstreams.db.DBLayerOutConditions;
import com.sos.jitl.jobstreams.db.FilterInConditions;
import com.sos.jitl.jobstreams.db.FilterJobStreams;
import com.sos.jitl.jobstreams.db.FilterOutConditions;
import com.sos.joc.Globals;
import com.sos.joc.exceptions.DBOpenSessionException;
import com.sos.joc.exceptions.JocConfigurationException;

public class JobStreamMigrator {

    private static final String EMPTY_RUN_TIME = "{\"letRun\":\"false\",\"runOnce\":\"false\"}";

    private static final String ACTIVE = "active";

    private static final Logger LOGGER = LoggerFactory.getLogger(JobStreamMigrator.class);

    private Set<String> setOfJobStreams134;
    private Set<String> setOfJobsFromIn;
    private List<DBItemInCondition> listOfInConditions;
    private List<DBItemOutCondition> listOfOutConditions;
    private Long counter;

    private void addStarter(SOSHibernateSession sosHibernateSession, DBItemJobStream dbItemJobStream) throws SOSHibernateException {
        for (DBItemOutCondition dbItemOutCondition : listOfOutConditions) {
            if (dbItemJobStream.getJobStream().equals(dbItemOutCondition.getJobStream()) && !setOfJobsFromIn.contains(dbItemOutCondition.getJob())) {
                DBItemJobStreamStarter dbItemJobStreamStarter = new DBItemJobStreamStarter();
                dbItemJobStreamStarter.setCreated(new Date());
                dbItemJobStreamStarter.setJobStream(dbItemJobStream.getId());
                dbItemJobStreamStarter.setState(ACTIVE);
                dbItemJobStreamStarter.setTitle("Start: " + Paths.get(dbItemOutCondition.getJob()).getFileName().toString());
                dbItemJobStreamStarter.setRunTime(EMPTY_RUN_TIME);
                sosHibernateSession.save(dbItemJobStreamStarter);
                DBItemJobStreamStarterJob dbItemJobStreamStarterJob = new DBItemJobStreamStarterJob();
                dbItemJobStreamStarterJob.setCreated(new Date());
                dbItemJobStreamStarterJob.setDelay(0L);
                dbItemJobStreamStarterJob.setJob(dbItemOutCondition.getJob());
                dbItemJobStreamStarterJob.setJobStreamStarter(dbItemJobStreamStarter.getId());
                sosHibernateSession.save(dbItemJobStreamStarterJob);
            }
        }
    }

    private void createJobStreams(SOSHibernateSession sosHibernateSession) throws SOSHibernateException {
        for (DBItemInCondition inCondition : listOfInConditions) {

            if (!setOfJobStreams134.contains(inCondition.getJobStream())) {
                setOfJobStreams134.add(inCondition.getJobStream());
                DBItemJobStream dbItemJobStream = new DBItemJobStream();
                dbItemJobStream.setCreated(new Date());
                dbItemJobStream.setJobStream(inCondition.getJobStream());
                dbItemJobStream.setSchedulerId(inCondition.getSchedulerId());
                dbItemJobStream.setState(ACTIVE);
                dbItemJobStream.setFolder(Paths.get(inCondition.getJob()).getParent().toString().replace("\\", "/"));
                sosHibernateSession.save(dbItemJobStream);
                counter = counter + 1;
                addStarter(sosHibernateSession, dbItemJobStream);
            }
        }

        for (DBItemOutCondition outCondition : listOfOutConditions) {

            if (!setOfJobStreams134.contains(outCondition.getJobStream())) {
                setOfJobStreams134.add(outCondition.getJobStream());
                DBItemJobStream dbItemJobStream = new DBItemJobStream();
                dbItemJobStream.setCreated(new Date());
                dbItemJobStream.setJobStream(outCondition.getJobStream());
                dbItemJobStream.setSchedulerId(outCondition.getSchedulerId());
                dbItemJobStream.setState(ACTIVE);
                dbItemJobStream.setFolder(Paths.get(outCondition.getJob()).getParent().toString().replace("\\", "/"));
                sosHibernateSession.save(dbItemJobStream);
                counter = counter + 1;
                addStarter(sosHibernateSession, dbItemJobStream);
            }
        }
    }

    public void migrate(SOSHibernateSession sosHibernateSession) throws JocConfigurationException, DBOpenSessionException, SOSHibernateException {

        sosHibernateSession = Globals.createSosHibernateStatelessConnection("JOBSTREAM_MIGRATOR");
        sosHibernateSession.beginTransaction();
        DBLayerJobStreams dbLayerJobStreams = new DBLayerJobStreams(sosHibernateSession);
        Long count = dbLayerJobStreams.getJobStreamCount();

        if (count == 0) {

            counter = 0L;
            DBLayerInConditions dbLayerInConditions = new DBLayerInConditions(sosHibernateSession);
            DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);

            FilterJobStreams filterJobStreams = new FilterJobStreams();
            FilterInConditions filterInConditions = new FilterInConditions();
            FilterOutConditions filterOutConditions = new FilterOutConditions();
            List<DBItemJobStream> listOfJobStreamsIn134 = dbLayerJobStreams.getJobStreamsList(filterJobStreams, 0);
            listOfInConditions = dbLayerInConditions.getSimpleInConditionsList(filterInConditions, 0);
            listOfOutConditions = dbLayerOutConditions.getSimpleOutConditionsList(filterOutConditions, 0);
            if (listOfOutConditions.size() > 0) {
                LOGGER.info("Starting migration of Job Stream configurations");

                setOfJobsFromIn = new HashSet<String>();
                setOfJobStreams134 = new HashSet<String>();

                for (DBItemJobStream dbItemJobStream : listOfJobStreamsIn134) {
                    setOfJobStreams134.add(dbItemJobStream.getJobStream());
                }

                for (DBItemInCondition dbItemInCondition : listOfInConditions) {
                    dbItemInCondition.setFolder(Paths.get(dbItemInCondition.getJob()).getParent().toString().replace("\\", "/"));
                    setOfJobsFromIn.add(dbItemInCondition.getJob());
                    sosHibernateSession.update(dbItemInCondition);
                }
                for (DBItemOutCondition dbItemOutCondition : listOfOutConditions) {
                    dbItemOutCondition.setFolder(Paths.get(dbItemOutCondition.getJob()).getParent().toString().replace("\\", "/"));
                    sosHibernateSession.update(dbItemOutCondition);
                }
                this.createJobStreams(sosHibernateSession);

                sosHibernateSession.commit();
                LOGGER.info("Migration of Job Stream configurations finished. %s job streams imported successful",counter);
            }
        }
        // Für alle Jobs, die eine Run-Time haben, einen Starter und einen StarterJob erzeugen

    }

}
