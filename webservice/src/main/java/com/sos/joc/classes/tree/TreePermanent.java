package com.sos.joc.classes.tree;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

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
            case AGENTCLUSTER: 
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
            case FOLDER:
                break;
            case OTHER: 
                types.add(type);
                break;
            }
        }
        return types;
    }

    public static SortedSet<String> initFoldersByFoldersFromBody(TreeFilter treeBody, Long instanceId) throws Exception {
        SortedSet<String> folders = new TreeSet<String>(new Comparator<String>(){
            public int compare(String a, String b){
                return b.compareTo(a);
            }
        });
        List<String> bodyTypes = new ArrayList<String>();
        if(treeBody.getTypes() != null && !treeBody.getTypes().isEmpty()) {
            for(JobSchedulerObjectType type : treeBody.getTypes()) {
                switch (type) {
                case JOBCHAIN: 
                    bodyTypes.add("job_chain");
                    break;
                case AGENTCLUSTER:
                case PROCESSCLASS: 
                    bodyTypes.add("process_class");
                    break;
                default: 
                    bodyTypes.add(type.name().toLowerCase());
                    break;
                }
            }
        }
        InventoryFilesDBLayer dbLayer = new InventoryFilesDBLayer(Globals.sosHibernateConnection);
        List<String> results = null;
        if (treeBody.getFolders() != null && !treeBody.getFolders().isEmpty()) {
            for (Folder folder : treeBody.getFolders()) {
                String normalizedFolder = ("/"+folder.getFolder()).replaceAll("//+", "/");
                results = dbLayer.getFoldersByFolderAndType(instanceId, normalizedFolder, bodyTypes);
                if (results != null && !results.isEmpty()) {
                    if (folder.getRecursive() == null || folder.getRecursive()) {
                        folders.addAll(results);
                    } else {
                        Path parent = Paths.get(normalizedFolder);
                        for (String result : results) {
                            folders.add("/"+Paths.get(result).subpath(0, parent.getNameCount()+1).toString());
                        } 
                    } 
                }
            }
        } else {
            results = dbLayer.getFoldersByFolderAndType(instanceId, "/", bodyTypes);
            if(results != null && !results.isEmpty()) {
                folders.addAll(results);
            }
        }
        return folders;
    }
    
    public static Tree getTree(SortedSet<String> folders) {
        Map<Path, TreeModel> treeMap = new HashMap<Path, TreeModel>();
        for(String folder : folders) {
            Path pFolder = Paths.get(folder);
            TreeModel tree = new TreeModel();
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
        if (treeMap.isEmpty()) {
            return null;
        }
        return treeMap.get(Paths.get("/"));
    }
    
    private static void fillTreeMap(Map<Path, TreeModel> treeMap, Path folder, TreeModel tree) {
        Path parent = folder.getParent();
        if (parent != null) {
            TreeModel parentTree = new TreeModel();
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
                    treeList.add(0, tree);
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