package com.sos.joc.classes.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.Globals;
import com.sos.joc.db.inventory.files.InventoryFilesDBLayer;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.tree.Tree;
import com.sos.joc.model.tree.TreeFilter;


public class TreePermanent {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreePermanent.class);
    
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
 
    public static void getTree(Tree rootTree, Set<String> folders) {
        for(String folder : folders) {
            LOGGER.debug("*** getTree() processing folder: " + folder);
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
            LOGGER.debug("*** initTreeNode() processing folder: " + folder);
            Tree childTree = new Tree();
            if(folder.contains("/")) {
                childTree.setName(folder.substring(0, folder.indexOf("/")));
                LOGGER.debug("*** initTreeNode() processing name: " + childTree.getName());
            } else {
                childTree.setName(folder);
                LOGGER.debug("*** initTreeNode() processing name: " + childTree.getName());
            }
            String newPath = null;
            if(parentFolder != null) {
                if(parentFolder.contains("/")) {
                    String[] splits = parentFolder.split("/");
                    if(splits.length > 1){
                        newPath = parentFolder.substring(0, parentFolder.indexOf("/" + childTree.getName()) + ("/" + childTree.getName()).length());
                        childTree.setPath(newPath);
                        LOGGER.debug("*** initTreeNode() processing path: " + newPath);
                    } else {
                        childTree.setPath(parentFolder);
                        LOGGER.debug("*** initTreeNode() processing path: " + parentFolder);
                    }
                }
            } else {
                newPath = "/" + folder;
                childTree.setPath(newPath);
                LOGGER.debug("*** initTreeNode() processing path: " + newPath);
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