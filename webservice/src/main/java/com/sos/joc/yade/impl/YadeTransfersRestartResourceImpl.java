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


@Path("/yade/transfers/restart")
public class YadeTransfersRestartResourceImpl extends JOCResourceImpl implements IYadeTransfersRestartResource {

    private static final String API_CALL = "./yade/transfers/restart";

    @Override
    public JOCDefaultResponse postYadeTransfersRestart(String accessToken, ModifyTransfers modifyTransfersFilterBody) throws Exception {
        SOSHibernateSession connection = null;
        try {
            SOSPermissionJocCockpit sosPermission = getPermissonsJocCockpit(accessToken);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, modifyTransfersFilterBody, accessToken, "",  
                    sosPermission.getYADE().getExecute().isTransferStart());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            JocDBLayerYade yadeDbLayer = new JocDBLayerYade(connection);
            // TODO put Implementation of the Yade Transfer WebService here!
            List<ModifyTransfer> transfers = modifyTransfersFilterBody.getTransfers();
            AuditParams auditParams = modifyTransfersFilterBody.getAuditLog();
            String comment = auditParams.getComment();
            Integer timeSpent = auditParams.getTimeSpent();
            String ticketLink = auditParams.getTicketLink();
            // TODO: for each transfer read configuration from DB, adjust configuration (remove regExp, add file list)
            // and create the order with the adjusted configuration
            for (ModifyTransfer modifyTransfer : transfers) {
                Long transferId = modifyTransfer.getTransferId();
                DBItemYadeTransfers transferDbItem = yadeDbLayer.getTransfer(transferId);
                List<Long> fileIds = modifyTransfer.getFileIds();
                String jobName = transferDbItem.getJob();
                String jobChainName = transferDbItem.getJobChain();
                String orderId = transferDbItem.getOrderId();
                String state = null;
                // get History Entry for the specific order
                // determine the node the order is resumed on
                // remove or clear the fileSpec and FileList parameter from the order parameters
                // add the filePath parameter with the adjusted list of file paths
                // resume the order with the adjusted parameters
                //                   OR
                // start a new order with the calculated node as start AND end node
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
//                String configuration = transferDbItem.getConfiguration();
//                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
//                URL schemaUrl = new URL("http://www.sos-berlin.com/schema/yade/YADE_configuration_v1.0.xsd");
//                Schema schema = schemaFactory.newSchema(schemaUrl);
//                JAXBContext context = JAXBContext.newInstance(Configurations.class);
//                Unmarshaller unmarshaller = context.createUnmarshaller();
//                unmarshaller.setSchema(schema);
//                Reader reader = new StringReader(configuration);
//                unmarshaller.unmarshal(reader);
//                Configurations configurations = (Configurations)unmarshaller.unmarshal(reader);
//                SOSXMLXPath xPathTransferConfig = new SOSXMLXPath(new StringBuffer(configuration));
//                Element root = xPathTransferConfig.getRoot();
                // Get all FileSpecSelection configurations from config
//                NodeList fileSpecSelections = root.getElementsByTagName("/Configurations/Profiles/Profile/Operation/Copy/CopySource/SourceFileOptions/Selection/FileSpecSelection");
                // Check all fileSpecSelections for directory values
                // than check all failed files for path to determine which config element has to be adjusted
                // Remove FileSpecSelections from configuration
                // add FilePathSelections for all failed files
//                for (int i = 0; i < fileSpecSelections.getLength(); i++) {
//                  Node node = fileSpecSelections.item(i);
//                }
                
                
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
