package com.sos.joc.classes.yade;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.sos.jade.db.DBItemYadeFiles;
import com.sos.jade.db.DBItemYadeTransfers;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.orders.OrderVolatile;
import com.sos.joc.db.yade.JocDBLayerYade;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.exceptions.YADERequestException;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.order.OrderStateText;
import com.sos.joc.model.order.OrderV;
import com.sos.joc.model.yade.FileTransferState;
import com.sos.joc.model.yade.FileTransferStateText;
import com.sos.joc.model.yade.ModifyTransfer;
import com.sos.joc.model.yade.TransferFile;

import sos.util.SOSString;

public class TransferFileUtils {

    public static TransferFile initTransferFileFromDbItem(DBItemYadeFiles file, boolean normalizeErrorMessage) {
        TransferFile transferFile = new TransferFile();
        if (!SOSString.isEmpty(file.getErrorMessage())) {
            Err error = new Err();
            error.setMessage(normalizeErrorMessage ? TransferFileUtils.normalizeErrorMessage(file.getErrorMessage()) : file.getErrorMessage());
            transferFile.setError(error);
        }
        transferFile.setId(file.getId());
        transferFile.setIntegrityHash(file.getIntegrityHash());
        transferFile.setInterventionTransferId(file.getInterventionTransferId());
        transferFile.setModificationDate(file.getModificationDate());
        transferFile.setSize(file.getSize());
        transferFile.setSourceName(getBasenameFromPath(file.getSourcePath()));
        transferFile.setSourcePath(file.getSourcePath());
        if (!SOSString.isEmpty(file.getTargetPath())) {
            transferFile.setTargetName(getBasenameFromPath(file.getTargetPath()));
            transferFile.setTargetPath(file.getTargetPath());
        }
        transferFile.setTransferId(file.getTransferId());
        // determine state value and severity
        transferFile.setState(initFileTransferStateFromDBItemState(file.getState()));
        // no Created-Date in DB, therefore use ModificationDate as surveyDate also
        transferFile.setSurveyDate(file.getModificationDate());
        return transferFile;
    }

    public static FileTransferState initFileTransferStateFromDBItemState(Integer dbItemState) {
        FileTransferState state = new FileTransferState();
        switch (dbItemState) {
        case 1:
            state.set_text(FileTransferStateText.UNDEFINED);
            state.setSeverity(3);
            break;
        case 2:
            state.set_text(FileTransferStateText.WAITING);
            state.setSeverity(5);
            break;
        case 3:
            state.set_text(FileTransferStateText.TRANSFERRING);
            state.setSeverity(5);
            break;
        case 4:
            state.set_text(FileTransferStateText.IN_PROGRESS);
            state.setSeverity(5);
            break;
        case 5:
            state.set_text(FileTransferStateText.TRANSFERRED);
            state.setSeverity(0);
            break;
        case 6:
            state.set_text(FileTransferStateText.SKIPPED);
            state.setSeverity(1);
            break;
        case 7:
            state.set_text(FileTransferStateText.FAILED);
            state.setSeverity(2);
            break;
        case 8:
            state.set_text(FileTransferStateText.ABORTED);
            state.setSeverity(2);
            break;
        case 9:
            state.set_text(FileTransferStateText.COMPRESSED);
            state.setSeverity(0);
            break;
        case 10:
            state.set_text(FileTransferStateText.NOT_OVERWRITTEN);
            state.setSeverity(1);
            break;
        case 11:
            state.set_text(FileTransferStateText.DELETED);
            state.setSeverity(2);
            break;
        case 12:
            state.set_text(FileTransferStateText.RENAMED);
            state.setSeverity(0);
            break;
        case 13:
            state.set_text(FileTransferStateText.IGNORED_DUE_TO_ZEROBYTE_CONSTRAINT);
            state.setSeverity(1);
            break;
        case 14:
            state.set_text(FileTransferStateText.ROLLED_BACK);
            state.setSeverity(5);
            break;
        case 15:
            state.set_text(FileTransferStateText.POLLING);
            state.setSeverity(5);
            break;
        }
        return state;
    }

    public static Integer initDbItemStateFromFileTransferState(FileTransferStateText fileTransferStateText) {
        switch (fileTransferStateText) {
        case UNDEFINED:
            return 1;
        case WAITING:
            return 2;
        case TRANSFERRING:
            return 3;
        case IN_PROGRESS:
            return 4;
        case TRANSFERRED:
        case SUCCESS:
            return 5;
        case SKIPPED:
            return 6;
        case FAILED:
            return 7;
        case ABORTED:
            return 8;
        case COMPRESSED:
            return 9;
        case NOT_OVERWRITTEN:
            return 10;
        case DELETED:
            return 11;
        case RENAMED:
            return 12;
        case IGNORED_DUE_TO_ZEROBYTE_CONSTRAINT:
            return 13;
        case ROLLED_BACK:
            return 14;
        case POLLING:
            return 15;
        }
        return null;
    }

    public static OrderV getOrderForResume(JocDBLayerYade yadeDbLayer, ModifyTransfer transfer, JOCResourceImpl jocResourceImpl) throws JocException {
        if (transfer.getTransferId() == null) {
            throw new JocMissingRequiredParameterException("undefined 'transferId'");
        }
        DBItemYadeTransfers dbTransferItem = yadeDbLayer.getTransfer(transfer.getTransferId());
        List<String> files = yadeDbLayer.getSourceFilesByIdsAndTransferId(transfer.getTransferId(), transfer.getFileIds());
        return getOrderForResume(dbTransferItem, files, jocResourceImpl);
    }

    public static OrderV getOrderForResume(DBItemYadeTransfers dbTransferItem, List<String> sourceFiles, JOCResourceImpl jocResourceImpl)
            throws JocException {
        if (dbTransferItem.getState() == 0) {
            throw new YADERequestException("The original transfer was successful.");
        }
        if (dbTransferItem.getJobChain() == null || dbTransferItem.getOrderId() == null || dbTransferItem.getJobChainNode() == null) {
            throw new YADERequestException("The original transfer order cannot be determined.");
        }
        OrderV order = OrderVolatile.getOrder(dbTransferItem.getJobChain(), dbTransferItem.getOrderId(), false, jocResourceImpl);
        if (order == null || order.getState() == null) {
            throw new YADERequestException(String.format("The original transfer order %1$s,%2$s was not found on node %3$s!", dbTransferItem
                    .getJobChain(), dbTransferItem.getOrderId(), dbTransferItem.getJobChainNode()));
        } else {
            if (!order.getState().equals(dbTransferItem.getJobChainNode())) {
                throw new YADERequestException(String.format(
                        "The original transfer order %1$s,%2$s is on the job chain node %3$s but the node %4$s is expected.", dbTransferItem
                                .getJobChain(), dbTransferItem.getOrderId(), order.getState(), dbTransferItem.getJobChainNode()));
            }
            if (order.getProcessingState().get_text() != OrderStateText.SUSPENDED) {
                throw new YADERequestException(String.format("The original transfer order %1$s,%2$s has to be suspended", dbTransferItem
                        .getJobChain(), dbTransferItem.getOrderId()));
            }
        }
        NameValuePair param = new NameValuePair();
        param.setName("yade_file_path_restriction");
        if (sourceFiles != null && !sourceFiles.isEmpty()) {
            param.setValue(Joiner.on(";").join(sourceFiles));
        } else {
            param.setValue("");
        }
        if (order.getParams() == null) {
            order.setParams(new ArrayList<NameValuePair>());
        }
        order.getParams().add(param);

        return order;
    }

    public static String normalizeErrorMessage(String val) {
        if (val != null && val.length() > 100) {
            val = val.substring(0, 97) + "...";
        }
        return val;
    }

    private static String getBasenameFromPath(String path) {
        int li = path.lastIndexOf("/");
        return li > -1 ? path.substring(li + 1) : path;
    }
}