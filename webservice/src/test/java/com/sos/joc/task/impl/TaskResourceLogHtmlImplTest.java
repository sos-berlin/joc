package com.sos.joc.task.impl;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.TestEnvWebserviceTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.exceptions.DBMissingDataException;

public class TaskResourceLogHtmlImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = TestEnvWebserviceTest.getAccessToken();
    }

    @Test
    public void postOrderTest() throws Exception {

        TaskLogResourceImpl taskLogHtmlResourceImpl = new TaskLogResourceImpl();
        try {
            JOCDefaultResponse okResponse = taskLogHtmlResourceImpl.getTaskLogHtml(accessToken, "", TestEnvWebserviceTest.SCHEDULER_ID, "0", null);
        } catch (DBMissingDataException e) {
        }
    }

}
