package com.sos.joc.classes.processclasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sos.jitl.reporting.db.DBItemInventoryProcessClass;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.db.inventory.processclasses.InventoryProcessClassesDBLayer;
import com.sos.joc.model.processClass.ProcessClassP;


public class ProcessClassPermanent {

    public static Map<String, ProcessClassP> getProcessClassesMap(InventoryProcessClassesDBLayer dbLayer,
            List<DBItemInventoryProcessClass> processClassesFromDb, String regex) throws Exception {
        Map<String, ProcessClassP> processClassesToAdd = new HashMap<String, ProcessClassP>();
        if (processClassesFromDb != null) {
            for (DBItemInventoryProcessClass processClassFromDb : processClassesFromDb) {
                if (!FilterAfterResponse.matchRegex(regex, processClassFromDb.getName())) {
                    continue;
                }
                processClassesToAdd.put(processClassFromDb.getName(), getProcessClassP(dbLayer, processClassFromDb));
            }
        }
        return processClassesToAdd;
    }
    
    public static List<ProcessClassP> getProcessClassesList(InventoryProcessClassesDBLayer dbLayer,
            List<DBItemInventoryProcessClass> processClassesFromDb, String regex) throws Exception {
        return new ArrayList<ProcessClassP>(getProcessClassesMap(dbLayer, processClassesFromDb, regex).values());
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
