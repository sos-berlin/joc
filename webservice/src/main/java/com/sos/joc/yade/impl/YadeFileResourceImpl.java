package com.sos.joc.yade.impl;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.yade.FileFilter;
import com.sos.joc.model.yade.TransferFile200;
import com.sos.joc.yade.resource.IYadeFileResource;


@Path("/yade/file")
public class YadeFileResourceImpl extends JOCResourceImpl implements IYadeFileResource {

    private static final String API_CALL = "./yade/file";

    @Override
    public JOCDefaultResponse postYadeFile(String accessToken, FileFilter filterBody) throws Exception {
        try {
            // TODO new Permissions for YADE
            SOSPermissionJocCockpit sosPermission = getPermissonsJocCockpit(accessToken);
            // TODO new init method for Yade without JobSchedulerId and new permissions
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, filterBody, accessToken, null, true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            // TODO put Implementation of the Yade Transfer WebService here!
            TransferFile200 entity = new TransferFile200();
            // fill the entity
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}
