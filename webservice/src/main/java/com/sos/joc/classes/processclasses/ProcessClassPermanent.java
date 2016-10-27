package com.sos.joc.classes.processclasses;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sos.jitl.reporting.db.DBItemInventoryProcessClass;
import com.sos.joc.db.inventory.processclasses.InventoryProcessClassesDBLayer;
import com.sos.joc.model.processClass.ProcessClassP;


public class ProcessClassPermanent {

    public static List<ProcessClassP> getProcessClassesToAdd(InventoryProcessClassesDBLayer dbLayer,
            List<DBItemInventoryProcessClass> processClassesFromDb, String regex) throws Exception {
        List<ProcessClassP> processClassesToAdd = new ArrayList<ProcessClassP>();
        if (processClassesFromDb != null) {
            for (DBItemInventoryProcessClass processClassFromDb : processClassesFromDb) {
                if (regex != null && !regex.isEmpty()) {
                    Matcher regExMatcher = Pattern.compile(regex).matcher(processClassFromDb.getName());
                    if (regExMatcher.find()) {
                        processClassesToAdd.add(getProcessClassP(dbLayer, processClassFromDb));
                    }
                } else {
                    processClassesToAdd.add(getProcessClassP(dbLayer, processClassFromDb));
                }
            }
        }
        return processClassesToAdd;
    }

    public static ProcessClassP getProcessClassP(InventoryProcessClassesDBLayer dbLayer, DBItemInventoryProcessClass processClassFromDb)
            throws Exception {
        if (processClassFromDb != null) {
            ProcessClassP processClassP = new ProcessClassP();
            processClassP.setMaxProcesses(processClassFromDb.getMaxProcesses());
            processClassP.setName(processClassFromDb.getBasename());
            processClassP.setPath(processClassFromDb.getName());
            processClassP.setSurveyDate(processClassFromDb.getModified());
            processClassP.setConfigurationDate(dbLayer.getProcessClassConfigurationDate(processClassFromDb.getId()));
            return processClassP;
        } else {
            return null;
        }
    }
    
}
