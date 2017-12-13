package com.sos.joc.yade.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jade.db.DBItemYadeProtocols;
import com.sos.jade.db.DBItemYadeTransfers;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.db.yade.JocDBLayerYade;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.yade.Operation;
import com.sos.joc.model.yade.Protocol;
import com.sos.joc.model.yade.ProtocolFragment;
import com.sos.joc.model.yade.Transfer;
import com.sos.joc.model.yade.TransferFilter;
import com.sos.joc.model.yade.TransferState;
import com.sos.joc.model.yade.TransferStateText;
import com.sos.joc.model.yade.Transfers;
import com.sos.joc.yade.resource.IYadeTransfersResource;

@Path("yade")
public class YadeTransfersResourceImpl extends JOCResourceImpl implements IYadeTransfersResource {

    private static final String API_CALL = "./yade/transfers";

    @Override
    public JOCDefaultResponse postYadeTransfers(String accessToken, TransferFilter filterBody) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, filterBody, accessToken, filterBody.getJobschedulerId(), getPermissonsJocCockpit(
                    accessToken).getYADE().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            
            Date dateFrom = null;
            Date dateTo = null;
            String from = filterBody.getDateFrom();
            String to = filterBody.getDateTo();
            String timezone = filterBody.getTimeZone();
            Boolean compact = filterBody.getCompact();
            if (from != null && !from.isEmpty()) {
                dateFrom = JobSchedulerDate.getDateFrom(from, timezone);
            }
            if (to != null && !to.isEmpty()) {
                dateTo = JobSchedulerDate.getDateTo(to, timezone);
            }
            Integer limit = filterBody.getLimit();
            if (limit == null) {
                limit = 10000;  // default
            } else if (limit == -1) {
                limit = null;   // unlimited
            }
            JocDBLayerYade dbLayer = new JocDBLayerYade(connection);
            List<TransferStateText> states = filterBody.getStates();
            Set<Integer> stateValues = new HashSet<Integer>();
            for (TransferStateText state : states) {
                switch (state) {
                case SUCCESSFUL:
                    stateValues.add(1);
                    break;
                case INCOMPLETE:
                    stateValues.add(2);
                    break;
                case FAILED:
                    stateValues.add(3);
                    break;
                }
            }
            Set<String> sourceHosts = null;
            if (filterBody.getSources() != null && !filterBody.getSources().isEmpty()) {
                sourceHosts = new HashSet<String>();
                for (ProtocolFragment source : filterBody.getSources()) {
                    sourceHosts.add(source.getHost());
                }
            }
            Set<String> targetHosts = null;
            if (filterBody.getTargets() != null && !filterBody.getTargets().isEmpty()) {
                targetHosts = new HashSet<String>();
                for (ProtocolFragment target : filterBody.getTargets()) {
                    targetHosts.add(target.getHost());
                }
            }
            List<Operation> operations = filterBody.getOperations();
            Set<Integer> operationValues = null;
            if (filterBody.getOperations() != null && !filterBody.getOperations().isEmpty()) {
                operationValues = new HashSet<Integer>();
                for (Operation operation : filterBody.getOperations()) {
                    switch(operation) {
                    case COPY:
                        operationValues.add(1);
                        break;
                    case MOVE:
                        operationValues.add(2);
                        break;
                    case GETLIST:
                        operationValues.add(3);
                        break;
                    case RENAME:
                        operationValues.add(4);
                        break;
                    case COPYTOINTERNET:
                        operationValues.add(5);
                        break;
                    case COPYFROMINTERNET:
                        operationValues.add(6);
                        break;
                    default:
                        operationValues.add(0);
                        break;
                    }
                }
            }
            List<DBItemYadeTransfers> transfersFromDb = dbLayer.getFilteredTransfers(operationValues, stateValues, filterBody
                    .getMandator(), sourceHosts, targetHosts, filterBody.getIsIntervention(), filterBody.getHasIntervention(),
                    filterBody.getProfiles(), limit, dateFrom, dateTo);
            Transfers entity = new Transfers();
            List<Transfer> transfers = new ArrayList<Transfer>();
            for (DBItemYadeTransfers transferFromDb : transfersFromDb) {
                Transfer transfer = new Transfer();
                transfer.set_operation(getOperationFromValue(transferFromDb.getOperation()));
                transfer.setEnd(transferFromDb.getEnd());
                Err err = new Err();
                err.setCode(transferFromDb.getErrorCode());
                err.setMessage(transferFromDb.getErrorMessage());
                transfer.setError(err);
                transfer.setHasIntervention(transferFromDb.getHasIntervention());
                transfer.setId(transferFromDb.getId());
                transfer.setJobschedulerId(transferFromDb.getJobschedulerId());
                if (!compact && transferFromDb.getJumpProtocolId() != null) {
                    DBItemYadeProtocols protocol = dbLayer.getProtocolById(transferFromDb.getJumpProtocolId());
                    if (protocol != null) {
                        ProtocolFragment jumpFragment = new ProtocolFragment();
                        jumpFragment.setAccount(protocol.getAccount());
                        jumpFragment.setHost(protocol.getHostname());
                        jumpFragment.setPort(protocol.getPort());
                        jumpFragment.setProtocol(getProtocolFromValue(protocol.getProtocol()));
                        transfer.setJump(jumpFragment);
                    }
                }
                transfer.setMandator(transferFromDb.getMandator());
                transfer.setNumOfFiles(transferFromDb.getNumOfFiles().intValue());
                transfer.setParent_id(transferFromDb.getParentTransferId());
                transfer.setProfile(transferFromDb.getProfileName());
                transfer.setState(getTransferStateFromValue(transferFromDb.getState()));
                if (transferFromDb.getSourceProtocolId() != null) {
                    DBItemYadeProtocols protocol = dbLayer.getProtocolById(transferFromDb.getSourceProtocolId());
                    if (protocol != null) {
                        ProtocolFragment sourceFragment = new ProtocolFragment();
                        sourceFragment.setHost(protocol.getHostname());
                        if (!compact) {
                            sourceFragment.setAccount(protocol.getAccount());
                            sourceFragment.setPort(protocol.getPort());
                            sourceFragment.setProtocol(getProtocolFromValue(protocol.getProtocol()));
                        }
                        transfer.setSource(sourceFragment);
                    }
                }
                transfer.setStart(transferFromDb.getStart());
                transfer.setSurveyDate(transferFromDb.getModified());
                if (transferFromDb.getTargetProtocolId() != null) {
                    DBItemYadeProtocols protocol = dbLayer.getProtocolById(transferFromDb.getTargetProtocolId());
                    if (protocol != null) {
                        ProtocolFragment targetFragment = new ProtocolFragment();
                        targetFragment.setHost(protocol.getHostname());
                        targetFragment.setProtocol(getProtocolFromValue(protocol.getProtocol()));
                        if (!compact) {
                            targetFragment.setAccount(protocol.getAccount());
                            targetFragment.setPort(protocol.getPort());
                        }
                        transfer.setTarget(targetFragment);
                    }
                }
                if (!compact) {
                    transfer.setJob(transferFromDb.getJob());
                    transfer.setJobChain(transferFromDb.getJobChain());
                    transfer.setOrderId(transferFromDb.getOrderId());
                }
                transfers.add(transfer);
            }
            entity.setTransfers(transfers);
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

    private Operation getOperationFromValue(Integer value) {
        switch (value) {
        case 1:
            return Operation.COPY;
        case 2:
            return Operation.MOVE;
        case 3:
            return Operation.GETLIST;
        case 4:
            return Operation.RENAME;
        default:
            return null;
        }
    }

    private Protocol getProtocolFromValue(Integer value) {
        switch (value) {
        case 1:
            return Protocol.LOCAL;
        case 2:
            return Protocol.FTP;
        case 3:
            return Protocol.FTPS;
        case 4:
            return Protocol.SFTP;
        case 5:
            return Protocol.HTTP;
        case 6:
            return Protocol.HTTPS;
        case 7:
            return Protocol.WEBDAV;
        case 8:
            return Protocol.WEBDAVS;
        case 9:
            return Protocol.SMB;
        default:
            return null;

        }
    }
    
    private TransferState getTransferStateFromValue(Integer value) {
        TransferState state = new TransferState();
        switch(value) {
        case 1:
            state.setSeverity(0);
            state.set_text(TransferStateText.SUCCESSFUL);
            return state;
        case 2:
            state.setSeverity(1);
            state.set_text(TransferStateText.INCOMPLETE);
            return state;
        case 3:
            state.setSeverity(2);
            state.set_text(TransferStateText.FAILED);
            return state;
        default:
            return null;
        }
    }
}
