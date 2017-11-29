package com.sos.joc.yade.impl;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.yade.YadeSnapshot;
import com.sos.joc.yade.resource.IYadeOverviewSnapshotResource;


@Path("yade")
public class YadeOverviewSnapshotResourceImpl extends JOCResourceImpl implements IYadeOverviewSnapshotResource {

    private static final String API_CALL = "./yade/overview/snapshot";

    @Override
    public JOCDefaultResponse postYadeOverviewSnapshot(String accessToken, JobSchedulerId jobschedulerId) throws Exception {
        SOSHibernateSession connection = null;
        try {
            SOSPermissionJocCockpit sosPermission = getPermissonsJocCockpit(accessToken);
            // JobSchedulerId has to be "" to prevent exception to be thrown
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, jobschedulerId, accessToken, jobschedulerId.getJobschedulerId(), 
                    sosPermission.getYADE().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            
            YadeSnapshot snapshot = new YadeSnapshot();
            // TODO: fill the entity
            return JOCDefaultResponse.responseStatus200(snapshot);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}
