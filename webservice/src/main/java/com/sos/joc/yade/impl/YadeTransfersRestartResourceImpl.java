package com.sos.joc.yade.impl;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Ok;
import com.sos.joc.model.yade.ModifyTransfer;
import com.sos.joc.model.yade.ModifyTransfers;
import com.sos.joc.model.yade.TransferFilter;
import com.sos.joc.yade.resource.IYadeTransfersRestartResource;


@Path("/yade/transfers/restart")
public class YadeTransfersRestartResourceImpl extends JOCResourceImpl implements IYadeTransfersRestartResource {

    private static final String API_CALL = "./yade/transfers/restart";

    @Override
    public JOCDefaultResponse postYadeTransfersRestart(String accessToken, ModifyTransfers modifyTransfersFilterBody) throws Exception {
        try {
            SOSPermissionJocCockpit sosPermission = getPermissonsJocCockpit(accessToken);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, modifyTransfersFilterBody, accessToken, "",  
                    sosPermission.getYADE().getExecute().isTransferStart());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            // TODO put Implementation of the Yade Transfer WebService here!
            // What is the correct filter for this call ???
            List<ModifyTransfer> transfers = modifyTransfersFilterBody.getTransfers();

            Ok ok = new Ok();
            // fill the entity
            ok.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(ok);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}
