package com.sos.joc;
import com.sos.auth.rest.SOSServicePermissionShiro;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
 
public class TestEnvWebserviceTest {

        private static final String PASSWORD = "root";
        private static final String USER = "root";
        public static final String SCHEDULER_ID = "scheduler.1.12";
        public static final String JOB_CHAIN = "/sos/dailyplan/CreateDailyPlan";
        public static final String SCHEDULE = "/mySchedule";
        public static final String LOCK = "/myLock";
        public static final String JOB_CHAIN_FOLDER = "/sos/dailyplan";
        public static final String ORDER = "createDailyPlan";
        public static final String JOB = "/sos/dailyplan/CreateDailyPlan";
        public static final String VALID_HISTORY_ID="204879";
        public static final String JOB_USED = "/myJob";
                 
        public static String getAccessToken() throws Exception   {
            SOSServicePermissionShiro sosServicePermissionShiro = new SOSServicePermissionShiro();
            SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = (SOSShiroCurrentUserAnswer) sosServicePermissionShiro.loginPost(null,"", "JOC Cockpit",USER, PASSWORD).getEntity();
            return sosShiroCurrentUserAnswer.getAccessToken();        
        }

        public static String getOrderPath() {
            return JOB_CHAIN + "," + ORDER;
        }
    
}
