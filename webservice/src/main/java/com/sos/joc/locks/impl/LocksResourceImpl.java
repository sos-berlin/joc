package com.sos.joc.locks.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.locks.resource.ILocksResource;
import com.sos.joc.model.lock.Holders;
import com.sos.joc.model.lock.LockVSchema;
import com.sos.joc.model.lock.LocksFilterSchema;
import com.sos.joc.model.lock.LocksVSchema;
import com.sos.joc.model.lock.Queue;
import com.sos.joc.model.lock.Task;

@Path("locks")
public class LocksResourceImpl extends JOCResourceImpl implements ILocksResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocksResourceImpl.class);

    @Override
    public JOCDefaultResponse postLocks(String accessToken, LocksFilterSchema locksFilterSchema) throws Exception {

        LOGGER.debug("init locks");
        try {
            JOCDefaultResponse jocDefaultResponse = init(locksFilterSchema.getJobschedulerId(), getPermissons(accessToken).getLock().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            LocksVSchema entity = new LocksVSchema();
            entity.setDeliveryDate(new Date());
            List<LockVSchema> listOfLocks = new ArrayList<LockVSchema>();
            LockVSchema lockVSchema = new LockVSchema();
            lockVSchema.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus());
            Holders holders = new Holders();
            holders.setExclusive(true);

            List<Task> listOfTasks = new ArrayList<Task>();
            Task task = new Task();
            task.setJob("myJob");
            task.setTaskId(-1);
            listOfTasks.add(task);
            holders.setTasks(listOfTasks);
            lockVSchema.setHolders(holders);

            lockVSchema.setMaxNonExclusive(-1);
            lockVSchema.setName("myName");
            List<Queue> listOfQueues = new ArrayList<Queue>();
            Queue queue = new Queue();
            queue.setExclusive(false);
            queue.setJob("myJob");
            listOfQueues.add(queue);
            lockVSchema.setQueue(listOfQueues);

            listOfLocks.add(lockVSchema);
            lockVSchema.setSurveyDate(new Date());

            entity.setLocks(listOfLocks);
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
    }

}