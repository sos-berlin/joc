package com.sos.joc.classes.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.Globals;
import com.sos.joc.db.inventory.files.InventoryFilesDBLayer;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.tree.Tree;
import com.sos.joc.model.tree.TreeFilter;
import com.sos.joc.model.tree.TreeView;


public class TreePermanent {

    public static List<JobSchedulerObjectType> getAllowedTypes(TreeFilter treeBody, SOSPermissionJocCockpit sosPermission) {
        List<JobSchedulerObjectType> types = new ArrayList<JobSchedulerObjectType>();
        for (JobSchedulerObjectType type : treeBody.getTypes()) {
            switch (type) {
            case JOB: 
                if (sosPermission.getJob().getView().isStatus()) {
                    types.add(type);
                }
                break;
            case JOBCHAIN: 
                if (sosPermission.getJobChain().getView().isStatus()) {
                    types.add(type);
                }
                break;
            case ORDER: 
                if (sosPermission.getOrder().getView().isStatus()) {
                    types.add(type);
                }
                break;
            case PROCESSCLASS: 
                if (sosPermission.getProcessClass().getView().isStatus()) {
                    types.add(type);
                }
                break;
            case LOCK: 
                if (sosPermission.getLock().getView().isStatus()) {
                    types.add(type);
                }
                break;
            case SCHEDULE: 
                if (sosPermission.getSchedule().getView().isStatus()) {
                    types.add(type);
                }
                break;
            case OTHER: 
                types.add(type);
                break;
            }
        }
        return types;
    }
    
    public static List<String> initFoldersFromBody(TreeFilter treeBody, Long instanceId) throws Exception {
        List<String> folders = new ArrayList<String>();
        InventoryFilesDBLayer dbLayer = new InventoryFilesDBLayer(Globals.sosHibernateConnection);
        if(treeBody.getTypes().isEmpty()) {
            folders = dbLayer.getFolders(instanceId);
        } else {
            for(JobSchedulerObjectType type : treeBody.getTypes()) {
                List<String> fromDb = null;
                switch (type) {
                case JOB:
                    fromDb = dbLayer.getFoldersByFileType(instanceId, "job");
                    if(fromDb != null && !fromDb.isEmpty()) {
                        folders.addAll(fromDb);
                    }
                    break;
                case JOBCHAIN: 
                    fromDb = dbLayer.getFoldersByFileType(instanceId, "job_chain");
                    if(fromDb != null && !fromDb.isEmpty()) {
                        folders.addAll(fromDb);
                    }
                    break;
                case ORDER: 
                    fromDb = dbLayer.getFoldersByFileType(instanceId, "order");
                    if(fromDb != null && !fromDb.isEmpty()) {
                        folders.addAll(fromDb);
                    }
                    break;
                case PROCESSCLASS: 
                    fromDb = dbLayer.getFoldersByFileType(instanceId, "process_class");
                    if(fromDb != null && !fromDb.isEmpty()) {
                        folders.addAll(fromDb);
                    }
                    break;
                case LOCK: 
                    fromDb = dbLayer.getFoldersByFileType(instanceId, "lock");
                    if(fromDb != null && !fromDb.isEmpty()) {
                        folders.addAll(fromDb);
                    }
                    break;
                case SCHEDULE: 
                    fromDb = dbLayer.getFoldersByFileType(instanceId, "schedule");
                    if(fromDb != null && !fromDb.isEmpty()) {
                        folders.addAll(fromDb);
                    }
                    break;
                }
            }
        }
        return folders;
    }
 
    public static Tree getTree(Tree rootTree, Set<String> folders) {
        Tree tree = new Tree();
        for(String folder : folders) {
            Tree child = initTree(rootTree, folder);
            if(child != null) {
                tree.getFolders().add(child);
            }
        }
        return tree;
    }

    private static Tree initTree(Tree tree, String folder) {
        if(folder.startsWith(tree.getPath())){
           return null; 
        } else {
            Tree child = new Tree();
            child.setName(folder.split("/")[0]);
            child.setPath("/" + folder);
            String subfolder = folder.substring(folder.indexOf("/") + 1);
            if(subfolder == null || subfolder.isEmpty()) {
                child.setFolders(null);
                return child;
            } else {
                Tree newChild = initTree(child, subfolder);
                if (newChild != null) {
                    child.getFolders().add(newChild);
                }
                return child;
            }
        }
    }
    
}