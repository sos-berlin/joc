package com.sos.joc;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
 
public class GlobalsTest {

        private static final String PASSWORD = "root";
        private static final String USER = "root";
        public static final String SCHEDULER_ID = "scheduler.1.12";
        public static final String JOB_CHAIN = "/sos/dailyplan/CreateDailyPlan";
        public static final String ORDER = "createDailyPlan";
        public static final String JOB = "/sos/dailyplan/CreateDailyPlan";
        public static final String VALID_HISTORY_ID="204879";
                 
        public static String getAccessToken() throws Exception   {
            SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
            SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost("", USER, PASSWORD).getEntity();
            return sosShiroCurrentUserAnswer.getAccessToken();        
        }
    
}
