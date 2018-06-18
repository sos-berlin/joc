package com.sos.joc.task.impl;
 
import org.junit.Test;

import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JOCDefaultResponse;
 
public class TaskResourceLogHtmlImplTest {
    private static final String LDAP_PASSWORD = "secret";
    private static final String LDAP_USER = "root";
     
    @Test
    public void postOrderTest() throws Exception   {
         
        SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
        SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", LDAP_USER, LDAP_PASSWORD).getEntity();
        TaskLogResourceImpl taskLogHtmlResourceImpl = new TaskLogResourceImpl();
        JOCDefaultResponse okResponse = taskLogHtmlResourceImpl.getTaskLogHtml(sosShiroCurrentUserAnswer.getAccessToken(),"","scheduler_id","0", null);
     }

}

