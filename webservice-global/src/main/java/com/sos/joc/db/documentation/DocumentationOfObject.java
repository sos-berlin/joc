package com.sos.joc.db.documentation;


public class DocumentationOfObject {
    private String docPath;
    private String objPath;
    
    public DocumentationOfObject(String docPath, String objPath) {
        this.docPath = docPath;
        this.objPath = objPath;
    }

    public String getDocPath() {
        return docPath;
    }
    
    public String getObjPath() {
        return objPath;
    }

}
