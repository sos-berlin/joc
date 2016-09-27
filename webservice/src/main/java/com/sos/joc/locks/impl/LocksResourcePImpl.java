package com.sos.joc.locks.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.locks.resource.ILocksResourceP;
import com.sos.joc.model.lock.LockPSchema;
import com.sos.joc.model.lock.LocksFilterSchema;
import com.sos.joc.model.lock.LocksPSchema;

@Path("locks")
public class LocksResourcePImpl extends JOCResourceImpl implements ILocksResourceP {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocksResourcePImpl.class);

    @Override
    public JOCDefaultResponse postLocksP(String accessToken, LocksFilterSchema locksFilterSchema) throws Exception {
        LOGGER.debug("init locks");
        try {
            JOCDefaultResponse jocDefaultResponse = init(locksFilterSchema.getJobschedulerId(), getPermissons(accessToken).getLock().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            LocksPSchema entity = new LocksPSchema();
            entity.setDeliveryDate(new Date());
            List<LockPSchema> listOfLocks = new ArrayList<LockPSchema>();
            LockPSchema lockPSchema = new LockPSchema();
            lockPSchema.setMaxNonExclusive(-1);
            lockPSchema.setName("myName");
            lockPSchema.setPath("myPath");
            lockPSchema.setSurveyDate(new Date());
            listOfLocks.add(lockPSchema);
            entity.setLocks(listOfLocks);
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e.getCause() + ":" + e.getMessage());
        }
    }

}