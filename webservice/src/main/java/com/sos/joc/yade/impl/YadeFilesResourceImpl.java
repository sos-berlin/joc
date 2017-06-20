package com.sos.joc.yade.impl;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.yade.TransferFiles;
import com.sos.joc.model.yade.TransferFilter;
import com.sos.joc.yade.resource.IYadeFilesResource;


@Path("/yade/files")
public class YadeFilesResourceImpl extends JOCResourceImpl implements IYadeFilesResource {

    private static final String API_CALL = "./yade/files";

    @Override
    public JOCDefaultResponse postYadeFiles(String accessToken, TransferFilter filterBody) throws Exception {
        try {
            // TODO new Permissions for YADE
            SOSPermissionJocCockpit sosPermission = getPermissonsJocCockpit(accessToken);
            // TODO new init method for Yade without JobSchedulerId and new permissions
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, filterBody, accessToken, null, true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            // TODO put Implementation of the Yade Transfer WebService here!
            
            TransferFiles entity = new TransferFiles();
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
