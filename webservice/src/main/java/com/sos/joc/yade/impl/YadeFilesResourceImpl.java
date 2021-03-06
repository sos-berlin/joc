package com.sos.joc.yade.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jade.db.DBItemYadeFiles;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.yade.TransferFileUtils;
import com.sos.joc.db.yade.JocDBLayerYade;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.yade.FilesFilter;
import com.sos.joc.model.yade.TransferFile;
import com.sos.joc.model.yade.TransferFiles;
import com.sos.joc.yade.resource.IYadeFilesResource;
import com.sos.schema.JsonValidator;

@Path("yade")
public class YadeFilesResourceImpl extends JOCResourceImpl implements IYadeFilesResource {

    private static final String API_CALL = "./yade/files";

    @Override
    public JOCDefaultResponse postYadeFiles(String accessToken, byte[] filterBytes) {
        SOSHibernateSession connection = null;
        try {
            JsonValidator.validateFailFast(filterBytes, FilesFilter.class);
            FilesFilter filterBody = Globals.objectMapper.readValue(filterBytes, FilesFilter.class);

            if (filterBody.getJobschedulerId() == null) {
                filterBody.setJobschedulerId("");
            }
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, filterBody, accessToken, filterBody.getJobschedulerId(), getPermissonsJocCockpit(
                    filterBody.getJobschedulerId(), accessToken).getYADE().getView().isFiles());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            Integer limit = filterBody.getLimit();
            Integer maxLimit = 1_000; // tmp solution
            if (limit == null) {
                // limit = 10000; // default
                limit = maxLimit;
            } else if (limit == -1) {
                // limit = null; // unlimited
                limit = maxLimit;
            } else if (limit > maxLimit) {
                limit = maxLimit;
            }

            JocDBLayerYade dbLayer = new JocDBLayerYade(connection);
            List<DBItemYadeFiles> dbFiles = dbLayer.getFilteredTransferFiles(filterBody.getTransferIds(), filterBody.getStates(), filterBody
                    .getSourceFiles(), filterBody.getTargetFiles(), filterBody.getInterventionTransferIds(), limit);
            TransferFiles entity = new TransferFiles();
            List<TransferFile> files = new ArrayList<TransferFile>();
            for (DBItemYadeFiles dbFile : dbFiles) {
                TransferFile transferFile = TransferFileUtils.initTransferFileFromDbItem(dbFile, true);
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
        } finally {
            Globals.disconnect(connection);
        }
    }

}
