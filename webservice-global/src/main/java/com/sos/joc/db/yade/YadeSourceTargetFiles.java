package com.sos.joc.db.yade;

public class YadeSourceTargetFiles {
    
    private Long transferId;
    private String sourcePath;
    private String targetPath;

    public YadeSourceTargetFiles(Long transferId, String sourcePath, String targetPath) {
        this.transferId = transferId;
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
    }

    public Long getTransferId() {
        return transferId;
    }
    
    public String getSourcePath() {
        return sourcePath;
    }
    
    public String getTargetPath() {
        return targetPath;
    }
}
