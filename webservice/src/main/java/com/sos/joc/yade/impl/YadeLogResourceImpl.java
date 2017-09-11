package com.sos.joc.yade.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.LogContent200;
import com.sos.joc.model.yade.FileFilter;
import com.sos.joc.yade.resource.IYadeLogResource;


@Path("/yade/log")
public class YadeLogResourceImpl extends JOCResourceImpl implements IYadeLogResource {

    private static final String API_CALL = "./yade/log";

    @Override
    public JOCDefaultResponse postYadeFiles(String accessToken, FileFilter filterBody) throws Exception {
        try {
            SOSPermissionJocCockpit sosPermission = getPermissonsJocCockpit(accessToken);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, filterBody, accessToken, "",  
                    sosPermission.getYADE().getView().isLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            // TODO put Implementation of the Yade Transfer WebService here!
            LogContent200 entity = new LogContent200();
            // fill the entity
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}
