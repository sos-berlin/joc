package com.sos.joc.classes.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        InventoryFilesDBLayer dbLayer = new InventoryFilesDBLayer(Globals.sosHibernateConnection);
        for (Folder folder : treeBody.getFolders()) {
            List<String> results = dbLayer.getFoldersByFolder(instanceId, folder.getFolder().substring(1), folder.getRecursive());
            if(results != null && !results.isEmpty()) {
                folders.addAll(results);
            }
        }
        return folders;
    }
    
    public static List<String> initFoldersByTypeFromBody(TreeFilter treeBody, Long instanceId) throws Exception {
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
                case OTHER:
                    break;
                default:
                    break;
                }
            }
        }
        return folders;
    }
 
    public static void getTree(Tree rootTree, Set<String> folders) {
        for(String folder : folders) {
            Tree childTree = initTreeNode(rootTree, folder, null);
            if(childTree != null) {
                if(folder.contains("/")) {
                    String[] splitFolders = folder.split("/");
                    childTree.setName(splitFolders[0]);
                    childTree.setPath("/" + splitFolders[0]);
                } else {
                    childTree.setName(folder);
                    childTree.setPath("/" + folder);
                }            
                rootTree.getFolders().add(childTree);
            }
        }
    }

    private static Tree initTreeNode(Tree tree, String folder, String parentFolder) {
        if(folder.startsWith(tree.getPath())){
           return null; 
        } else {
            Tree childTree = new Tree();
            if(folder.contains("/")) {
                childTree.setName(folder.substring(0, folder.indexOf("/")));
            } else {
                childTree.setName(folder);
            }
            String newPath = null;
            if(parentFolder != null) {
                if(parentFolder.contains("/")) {
                    String[] splits = parentFolder.split("/");
                    if(splits.length > 1){
                        newPath = parentFolder.substring(0, parentFolder.indexOf("/" + childTree.getName()) + ("/" + childTree.getName()).length());
                        childTree.setPath(newPath);
                    } else {
                        childTree.setPath(parentFolder);
                    }
                }
            } else {
                newPath = "/" + folder;
                childTree.setPath(newPath);
                parentFolder = newPath;
            }
            if(folder != null && folder.contains("/")) {
                String subfolder = folder.substring(folder.indexOf("/") + 1);
                if(subfolder == null || subfolder.isEmpty()) {
                    childTree.setFolders(null);
                    return childTree;
                } else {
                    Tree newChild = initTreeNode(childTree, subfolder, parentFolder);
                    if (newChild != null) {
                        childTree.getFolders().add(newChild);
                    }
                    return childTree;
                }
            } else {
                childTree.setFolders(null);
                return childTree;
            }
        }
    }

    public static Tree mergeTreeDuplications(Tree tree) {
        Set<Tree> foldersToRemove = new HashSet<Tree>();
        int i = 1;
        for (Tree treeItem : tree.getFolders()) {
            for (int counter = i ; counter < tree.getFolders().size(); counter++) {
                if (tree.getFolders().get(counter).getName().equals(treeItem.getName())) {
                    for(Tree folder : treeItem.getFolders()) {
                        if(!tree.getFolders().get(counter).getFolders().contains(folder)) {
                            tree.getFolders().get(counter).getFolders().add(folder);
                        }
                    }
                    foldersToRemove.add(treeItem);
                }
            }
            i++;
        }
        tree.getFolders().removeAll(foldersToRemove);
        return tree;
    }
    
}