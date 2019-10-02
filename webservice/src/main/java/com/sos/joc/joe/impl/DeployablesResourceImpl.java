package com.sos.joc.joe.impl;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.resource.IDeployablesResource;
import com.sos.joc.model.common.JobSchedulerId;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.Deployable;
import com.sos.joc.model.joe.common.Deployables;

@Path("joe")
public class DeployablesResourceImpl extends JOCResourceImpl implements IDeployablesResource {

    private static final String API_CALL = "./joe/deploy";

    @Override
    public JOCDefaultResponse deployables(final String accessToken, final JobSchedulerId body) {
        SOSHibernateSession sosHibernateSession = null;
        try {

            SOSPermissionJocCockpit sosPermissionJocCockpit = getPermissonsJocCockpit(body.getJobschedulerId(), accessToken);
            boolean permission = sosPermissionJocCockpit.getJobschedulerMaster().getAdministration().getConfigurations().isDeploy();

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);
            Globals.beginTransaction(sosHibernateSession);

            DBLayerJoeObjects dbLayerJoeObjects = new DBLayerJoeObjects(sosHibernateSession);
            FilterJoeObjects filterJoeObjects = new FilterJoeObjects();

            filterJoeObjects.setSchedulerId(body.getJobschedulerId());
            filterJoeObjects.setOrderCriteria("created");

            List<DBItemJoeObject> listOfJoeObjects = dbLayerJoeObjects.getJoeObjectList(filterJoeObjects, 0);

            Deployables deployables = new Deployables();
            deployables.setDeployables(new ArrayList<Deployable>());
            for (DBItemJoeObject joeObject : listOfJoeObjects) {
                Deployable deployable = new Deployable();
                deployable.setAccount(joeObject.getAccount());
                deployable.setFolder(getParent(joeObject.getPath()));
                deployable.setJobschedulerId(joeObject.getSchedulerId());
                deployable.setModified(joeObject.getModified());

                deployable.setObjectName(Paths.get(joeObject.getPath()).getFileName().toString());
                deployable.setObjectType(JobSchedulerObjectType.fromValue(joeObject.getObjectType()));
                deployable.setOperation(joeObject.getOperation());
                deployables.getDeployables().add(deployable);
            }
            Globals.commit(sosHibernateSession);

            return JOCDefaultResponse.responseStatus200(deployables);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.rollback(sosHibernateSession);
            Globals.disconnect(sosHibernateSession);
        }
    }
}
