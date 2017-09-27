package com.sos.joc.classes.yade;

import com.sos.jade.db.DBItemYadeFiles;
import com.sos.joc.model.common.Err;
import com.sos.joc.model.yade.FileTransferState;
import com.sos.joc.model.yade.FileTransferStateText;
import com.sos.joc.model.yade.TransferFile;


public class TransferFileUtils {

    public static TransferFile initTransferFileFromDbItem(DBItemYadeFiles file) {
        TransferFile transferFile = new TransferFile();
        if (file.getErrorMessage() != null && !file.getErrorMessage().isEmpty()){
            Err error = new Err();
            error.setMessage(file.getErrorMessage());
            transferFile.setError(error);
        }
        transferFile.setId(file.getId());
        transferFile.setIntegrityHash(file.getIntegrityHash());
        transferFile.setInterventionTransferId(file.getInterventionTransferId());
        transferFile.setModificationDate(file.getModificationDate());
        transferFile.setSize(file.getSize());
        transferFile.setSourcePath(file.getSourcePath());
        transferFile.setTargetPath(file.getTargetPath());
        transferFile.setTransferId(file.getTransferId());
        // determine state value and severity
        transferFile.setState(initFileTransferStateFromDBItemState(file.getState()));
        // no Created-Date in DB, therefore use ModificationDate as surveyDate also
        transferFile.setSurveyDate(file.getModificationDate());
        return transferFile;
    }
    
    public static FileTransferState initFileTransferStateFromDBItemState(Integer dbItemState) {
        FileTransferState state = new FileTransferState();
        switch(dbItemState) {
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
            state.set_text(FileTransferStateText.SETBACK);
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
        case SETBACK:
            return 14;
        case POLLING:
            return 15;
        }
        return null;
    }
}