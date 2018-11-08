package com.sos.joc.locks.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.model.lock.LocksFilter;
import com.sos.joc.model.lock.LocksV;

public class LocksResourceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocksResourceImplTest.class);

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postLocksTest() throws Exception {
        LocksFilter locksFilterSchema = new LocksFilter();
        locksFilterSchema.setJobschedulerId(TestEnvWebserviceTest.SCHEDULER_ID);
        LocksResourceImpl locksResourceImpl = new LocksResourceImpl();
        JOCDefaultResponse jobsResponse = locksResourceImpl.postLocks(accessToken, locksFilterSchema);
        LocksV locksVSchema = (LocksV) jobsResponse.getEntity();
        assertEquals("postLocksTest", TestEnvWebserviceTest.LOCK, Globals.normalizePath(locksVSchema.getLocks().get(0).getName()));
        LOGGER.info(jobsResponse.toString());
    }

}
