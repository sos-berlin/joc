package com.sos.joc.joe.impl;

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
import com.sos.joc.joe.resource.IStoreFileResource;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.joe.common.JSObjectEdit;

@Path("joe")
public class StoreFileResourceImpl extends JOCResourceImpl implements IStoreFileResource {

    private static final String API_CALL = "./joe/read/file";

    @Override
    public JOCDefaultResponse storeFile(final String accessToken, final byte[] jsObj) {
        SOSHibernateSession sosHibernateSession = null;
        try {
            JSObjectEdit body = Globals.objectMapper.readValue(jsObj, JSObjectEdit.class);
            checkRequiredParameter("objectType", body.getObjectType());
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), Helper.hasPermission(body
                    .getObjectType(), getPermissonsJocCockpit(body.getJobschedulerId(), accessToken)));
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("path", body.getPath());
            String path = normalizePath(body.getPath() + "/");

            if (body.getObjectType() == JobSchedulerObjectType.FOLDER) {
                if (!this.folderPermissions.isPermittedForFolder(path)) {
                    return accessDeniedResponse();
                }

            } else {
                if (!this.folderPermissions.isPermittedForFolder(getParent(path))) {
                    return accessDeniedResponse();
                }
            }

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            DBLayerJoeObjects dbLayer = new DBLayerJoeObjects(sosHibernateSession);
            FilterJoeObjects filter = new FilterJoeObjects();
            filter.setConstraint(body);
            DBItemJoeObject item = dbLayer.getJoeObject(filter);
            final byte[] bytes = Globals.objectMapper.writeValueAsBytes(body.getConfiguration());
            if (item != null) {
                item.setOperation("store");
                item.setAccount(getAccount());
                item.setConfiguration(new String(bytes));
                dbLayer.update(item);

            } else {
                item = new DBItemJoeObject();
                item.setId(null);
                item.setAccount(getAccount());
                item.setSchedulerId(body.getJobschedulerId());
                item.setAuditLogId(null);
                item.setConfiguration(new String(bytes));
                item.setObjectType(body.getObjectType().value());
                item.setOperation("store");
                item.setPath(body.getPath());
                dbLayer.save(item);
            }

            return JOCDefaultResponse.responseStatusJSOk(null);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseHTMLStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseHTMLStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

}
