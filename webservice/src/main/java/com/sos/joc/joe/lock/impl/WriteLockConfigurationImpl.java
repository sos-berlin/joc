package com.sos.joc.joe.lock.impl;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.joe.lock.resource.IWriteLockConfigurationResource;
import com.sos.joc.model.joe.lock.LockEdit;


@Path("joe/configuration")
public class WriteLockConfigurationImpl extends JOCResourceImpl implements IWriteLockConfigurationResource {

    private static final String API_CALL = "./joe/configuration/writeLocks";

    @Override
    public JOCDefaultResponse writeLocks(String accessToken, LockEdit lock) throws Exception {
        try {
            SOSHibernateSession sosHibernateSession = null;
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            sosHibernateSession.setAutoCommit(false);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, lock, accessToken, lock.getJobschedulerId(), getPermissonsJocCockpit(
                    null, accessToken).getJobStream().getChange().isConditions());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            this.checkRequiredParameter("jobSchedulerId", lock.getJobschedulerId());

            
            return JOCDefaultResponse.responseStatus200(null);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {

        }
    }

}