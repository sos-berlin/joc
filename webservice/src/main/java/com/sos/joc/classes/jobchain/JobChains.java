package com.sos.joc.classes.jobchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sos.joc.model.jobChain.File;
import com.sos.joc.model.jobChain.FileWatchingNodeVSchema;

public class JobChains {

    public static List<FileWatchingNodeVSchema> getFileOrderSources() {
        List<FileWatchingNodeVSchema> listOfFileOrderSources = new ArrayList<FileWatchingNodeVSchema>();
        FileWatchingNodeVSchema fileWatchingNodeVSchema = new FileWatchingNodeVSchema();
        fileWatchingNodeVSchema.setDirectory("myDirectory");
        List<File> listOfFiles = new ArrayList<File>();
        File f1 = new File();
        f1.setModified(new Date());
        f1.setPath("myFile1");
        File f2 = new File();
        f2.setModified(new Date());
        f2.setPath("myFile2");
        listOfFiles.add(f1);
        listOfFiles.add(f2);

        fileWatchingNodeVSchema.setFiles(listOfFiles);
        fileWatchingNodeVSchema.setRegex("myRegEx");
        listOfFileOrderSources.add(fileWatchingNodeVSchema);
        return listOfFileOrderSources;
    }
}
