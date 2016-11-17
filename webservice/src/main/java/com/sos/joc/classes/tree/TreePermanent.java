package com.sos.joc.classes.tree;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.Globals;
import com.sos.joc.db.inventory.files.InventoryFilesDBLayer;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.tree.Tree;
import com.sos.joc.model.tree.TreeFilter;


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

    public static List<String> initFoldersByFoldersFromBody(TreeFilter treeBody, Long instanceId) throws Exception {
        List<String> folders = new ArrayList<String>();
        List<String> bodyTypes = new ArrayList<String>();
        if(treeBody.getTypes() != null && !treeBody.getTypes().isEmpty()) {
            for(JobSchedulerObjectType type : treeBody.getTypes()) {
                bodyTypes.add(type.name().toLowerCase().replaceFirst("([bs])c", "$1_c"));
            }
        }
        InventoryFilesDBLayer dbLayer = new InventoryFilesDBLayer(Globals.sosHibernateConnection);
        List<String> results = null;
        if (treeBody.getFolders() != null && !treeBody.getFolders().isEmpty()) {
            for (Folder folder : treeBody.getFolders()) {
                results = dbLayer.getFoldersByFolderAndType(instanceId, ("/"+folder.getFolder()).replaceAll("//+", "/"), folder.getRecursive(), bodyTypes);
            }
        } else {
            results = dbLayer.getFoldersByFolderAndType(instanceId, "/", true, bodyTypes);
        }
        if(results != null && !results.isEmpty()) {
            folders.addAll(results);
        }
    
        return folders;
    }
    
    public static Tree getTree(SortedSet<String> folders) {
        Map<Path, Tree> treeMap = new HashMap<Path, Tree>();
        for(String folder : folders) {
            Path pFolder = Paths.get(folder);
            Tree tree = new Tree();
            if (treeMap.containsKey(pFolder)) {
                tree = treeMap.get(pFolder);
            } else {
                tree.setPath(folder);
                Path fileName = pFolder.getFileName();
                tree.setName(fileName == null ? "" : fileName.toString());
                tree.setFolders(null);
                treeMap.put(pFolder, tree);
            }
            fillTreeMap(treeMap, pFolder, tree);
        }
        return treeMap.get(Paths.get("/"));
    }
    
    private static void fillTreeMap(Map<Path, Tree> treeMap, Path folder, Tree tree) {
        Path parent = folder.getParent();
        if (parent != null) {
            Tree parentTree = new Tree();
            if (treeMap.containsKey(parent)) {
                parentTree = treeMap.get(parent);
                List<Tree> treeList = parentTree.getFolders();
                if (treeList == null) {
                    treeList = new ArrayList<Tree>();
                    treeList.add(tree);
                    parentTree.setFolders(treeList);
                } else {
                    if (treeList.contains(tree)) {
                        treeList.remove(tree);
                    }
                    treeList.add(tree);
                }
            } else {
                parentTree.setPath(parent.toString().replace('\\', '/'));
                Path fileName = parent.getFileName();
                parentTree.setName(fileName == null ? "" : fileName.toString());
                List<Tree> treeList = new ArrayList<Tree>();
                treeList.add(tree);
                parentTree.setFolders(treeList);
                treeMap.put(parent, parentTree);
            }
            fillTreeMap(treeMap, parent, parentTree); 
        }
    }
}