package com.sos.joc.yade.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import sos.yade.db.DBItemYadeFiles;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.yade.TransferFileUtils;
import com.sos.joc.db.yade.JocDBLayerYade;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.yade.FileFilter;
import com.sos.joc.model.yade.FileTransferState;
import com.sos.joc.model.yade.FileTransferStateText;
import com.sos.joc.model.yade.TransferFile;
import com.sos.joc.model.yade.TransferFile200;
import com.sos.joc.yade.resource.IYadeFileResource;


@Path("/yade/file")
public class YadeFileResourceImpl extends JOCResourceImpl implements IYadeFileResource {

    private static final String API_CALL = "./yade/file";

    @Override
    public JOCDefaultResponse postYadeFile(String accessToken, FileFilter filterBody) throws Exception {
        SOSHibernateSession connection = null;
        try {
            SOSPermissionJocCockpit sosPermission = getPermissonsJocCockpit(accessToken);
            // TODO new init method for Yade without JobSchedulerId and new permissions
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, filterBody, accessToken, "", 
                    sosPermission.getYADE().getView().isFiles());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            // Filters
            Boolean compact = filterBody.getCompact();
            Long fileId = filterBody.getFileId();
            JocDBLayerYade dbLayer = new JocDBLayerYade(connection);
            TransferFile transferFile = null;
            if (fileId != null) {
                DBItemYadeFiles file = dbLayer.getTransferFile(fileId);
                transferFile = TransferFileUtils.initTransferFileFromDbItem(file);
            }
            TransferFile200 entity = new TransferFile200();
            entity.setFile(transferFile);
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
