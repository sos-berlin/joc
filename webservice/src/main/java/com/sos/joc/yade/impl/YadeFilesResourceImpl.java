package com.sos.joc.yade.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.sos.joc.model.yade.FileTransferStateText;
import com.sos.joc.model.yade.FilesFilter;
import com.sos.joc.model.yade.TransferFile;
import com.sos.joc.model.yade.TransferFiles;
import com.sos.joc.yade.resource.IYadeFilesResource;


@Path("/yade/files")
public class YadeFilesResourceImpl extends JOCResourceImpl implements IYadeFilesResource {

    private static final String API_CALL = "./yade/files";

    @Override
    public JOCDefaultResponse postYadeFiles(String accessToken, FilesFilter filterBody) throws Exception {
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
            List<Long> transferIds = filterBody.getTransferIds();
            List<FileTransferStateText> states = filterBody.getStates(); 
            List<String> sources = filterBody.getSources();
            List<String> targets = filterBody.getTargets();
            List<Long> interventionTransferIds = filterBody.getInterventionTransferIds();
//            Integer limit = filterBody.getLimit();
            Integer limit = null;
            if (limit == null) {
                limit = 10000;  // default
            } else if (limit == -1) {
                limit = null;   // unlimited
            }
            JocDBLayerYade dbLayer = new JocDBLayerYade(connection);
            List<DBItemYadeFiles> dbFiles = dbLayer.getFilteredTransferFiles(transferIds, states, sources, targets,
                    interventionTransferIds, limit); 
            TransferFiles entity = new TransferFiles();
            List<TransferFile> files = new ArrayList<TransferFile>();
            for(DBItemYadeFiles dbFile : dbFiles) {
                TransferFile transferFile = TransferFileUtils.initTransferFileFromDbItem(dbFile);
                files.add(transferFile);
            }
            entity.setFiles(files);
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
