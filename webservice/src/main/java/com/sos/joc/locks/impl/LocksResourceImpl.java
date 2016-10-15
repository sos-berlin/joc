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
import com.sos.joc.model.lock.LockHolder;
import com.sos.joc.model.lock.LockHolders;
import com.sos.joc.model.lock.LockV;
import com.sos.joc.model.lock.LocksFilter;
import com.sos.joc.model.lock.LocksV;
import com.sos.joc.model.lock.Queue;

@Path("locks")
public class LocksResourceImpl extends JOCResourceImpl implements ILocksResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocksResourceImpl.class);

    @Override
    public JOCDefaultResponse postLocks(String accessToken, LocksFilter locksFilter) throws Exception {

        LOGGER.debug("init locks");
        try {
            JOCDefaultResponse jocDefaultResponse = init(locksFilter.getJobschedulerId(), getPermissons(accessToken).getLock().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            LocksV entity = new LocksV();
            entity.setDeliveryDate(new Date());
            List<LockV> listOfLocks = new ArrayList<LockV>();
            LockV lockVSchema = new LockV();
            lockVSchema.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus());
            LockHolders holders = new LockHolders();
            holders.setExclusive(true);

            List<LockHolder> listOfLockHolders = new ArrayList<LockHolder>();
            LockHolder lockHolder = new LockHolder();
            lockHolder.setJob("myJob");
            lockHolder.setTaskId("-1");
            listOfLockHolders.add(lockHolder);
            holders.setTasks(listOfLockHolders);
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