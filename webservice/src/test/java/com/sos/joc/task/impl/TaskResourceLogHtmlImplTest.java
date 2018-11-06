package com.sos.joc.task.impl;

import org.junit.Before;
import org.junit.Test;
import com.sos.joc.GlobalsTest;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.exceptions.DBMissingDataException;

public class TaskResourceLogHtmlImplTest {

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        accessToken = GlobalsTest.getAccessToken();
    }

    @Test
    public void postOrderTest() throws Exception {

        TaskLogResourceImpl taskLogHtmlResourceImpl = new TaskLogResourceImpl();
        try {
            JOCDefaultResponse okResponse = taskLogHtmlResourceImpl.getTaskLogHtml(accessToken, "", GlobalsTest.SCHEDULER_ID, "0", null);
        } catch (DBMissingDataException e) {
        }
    }

}
