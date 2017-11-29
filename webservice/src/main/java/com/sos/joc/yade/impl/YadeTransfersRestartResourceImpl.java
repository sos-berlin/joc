package com.sos.joc.yade.impl;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jade.db.DBItemYadeFiles;
import com.sos.jade.db.DBItemYadeTransfers;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.yade.JocDBLayerYade;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.audit.AuditParams;
import com.sos.joc.model.common.Ok;
import com.sos.joc.model.yade.ModifyTransfer;
import com.sos.joc.model.yade.ModifyTransfers;
import com.sos.joc.yade.resource.IYadeTransfersRestartResource;


@Path("yade")
public class YadeTransfersRestartResourceImpl extends JOCResourceImpl implements IYadeTransfersRestartResource {

    private static final String API_CALL = "./yade/transfers/restart";

    @Override
    public JOCDefaultResponse postYadeTransfersRestart(String accessToken, ModifyTransfers filterBody) throws Exception {
        SOSHibernateSession connection = null;
        try {
            SOSPermissionJocCockpit sosPermission = getPermissonsJocCockpit(accessToken);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, filterBody, accessToken, filterBody.getJobschedulerId(),  
                    sosPermission.getYADE().getExecute().isTransferStart());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            JocDBLayerYade yadeDbLayer = new JocDBLayerYade(connection);
            // TODO put Implementation of the Yade Transfer WebService here!
            List<ModifyTransfer> transfers = filterBody.getTransfers();
            AuditParams auditParams = filterBody.getAuditLog();
            String comment = auditParams.getComment();
            Integer timeSpent = auditParams.getTimeSpent();
            String ticketLink = auditParams.getTicketLink();
            // TODO: for each transfer create the order with the adjusted configuration
            for (ModifyTransfer modifyTransfer : transfers) {
                Long transferId = modifyTransfer.getTransferId();
                DBItemYadeTransfers transferDbItem = yadeDbLayer.getTransfer(transferId);
                List<Long> fileIds = modifyTransfer.getFileIds();
                String jobName = transferDbItem.getJob();
                String jobChainName = transferDbItem.getJobChain();
                String jobChainNodeName = transferDbItem.getJobChainNode();
                String orderId = transferDbItem.getOrderId();
                Long taskId = transferDbItem.getTaskId();
                String state = null;
                List<DBItemYadeFiles> filesFromDb = yadeDbLayer.getFilesById(fileIds);
                StringBuilder newFilePathsSB = new StringBuilder();
                boolean first = true;
                for (DBItemYadeFiles file : filesFromDb) {
                    if (first) {
                        newFilePathsSB.append(file.getSourcePath());
                        first = false;
                    } else {
                        newFilePathsSB.append(";").append(file.getSourcePath());
                    }
                }
                String newFilePaths = newFilePathsSB.toString();
                // WS
                // get History Entry for the specific order or state of the order
                // determine if the order is finished OR suspended
                // determine the node the order has to be resumed on (transferDbItem.getJobChainNode())
                // resume the order with the adjusted parameters
                //                   OR
                // start a new order with the calculated node as start AND end node
                // set parameters for the new order to restart the YADEJob
                // set newFilePaths
                // set set transferId as new parentTransferId
                // update transfer with properties hasIntevention=true and 
                // set the new TransferId as InterventionTransferId in YADEJob
                 // JOB
                // remove or clear the fileSpec and FileList parameter from the order parameters
                // add the filePath parameter with the adjusted list of file paths

                
            }
            
            
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
