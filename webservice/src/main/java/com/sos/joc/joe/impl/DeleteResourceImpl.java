package com.sos.joc.joe.impl;

import java.sql.Date;
import java.time.Instant;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joe.DBItemJoeObject;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.db.joe.FilterJoeObjects;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.common.Helper;
import com.sos.joc.joe.resource.IDeleteResource;
import com.sos.joc.model.joe.common.JSObjectEdit;

@Path("joe")
public class DeleteResourceImpl extends JOCResourceImpl implements IDeleteResource {

    private static final String API_CALL = "./joe/delete";

    @Override
    public JOCDefaultResponse delete(final String accessToken, final JSObjectEdit body) {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("path", body.getPath());
            checkRequiredParameter("objectType", body.getObjectType().value());
            boolean isDirectory = body.getPath().endsWith("/");
            String path = isDirectory ? normalizeFolder(body.getPath()) : normalizePath(body.getPath());
            
            if (!Helper.hasPermission(body.getObjectType(), getPermissonsJocCockpit(body.getJobschedulerId(), accessToken))) {
                return accessDeniedResponse();
            }
            if (!isPermittedForFolder(path)) {
                return accessDeniedResponse();
            }
            
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            DBLayerJoeObjects dbLayer = new DBLayerJoeObjects(connection);
            FilterJoeObjects filter = new FilterJoeObjects();
            filter.setConstraint(body);
            DBItemJoeObject item = dbLayer.getJoeObject(filter);
            
            if (item != null) {
                item.setOperation("delete");
                item.setAccount(getAccount());
                if (isDirectory) {
                    Globals.beginTransaction(connection);
                    dbLayer.updateOperationRecursive(item);
                    Globals.commit(connection);
                } else {
                    dbLayer.update(item);
                }
            }

            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

}
