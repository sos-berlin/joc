package com.sos.joc.classes.tree;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.sos.auth.rest.SOSShiroFolderPermissions;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.db.inventory.files.InventoryFilesDBLayer;
import com.sos.joc.db.joe.DBLayerJoeLocks;
import com.sos.joc.db.joe.DBLayerJoeObjects;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.tree.JoeTree;
import com.sos.joc.model.tree.Tree;
import com.sos.joc.model.tree.TreeFilter;

public class TreePermanent {

	public static List<JobSchedulerObjectType> getAllowedTypes(TreeFilter treeBody,
			SOSPermissionJocCockpit sosPermission, boolean treeForJoe) {
		List<JobSchedulerObjectType> types = new ArrayList<JobSchedulerObjectType>();
		
		for (JobSchedulerObjectType type : treeBody.getTypes()) {
			switch (type) {
			case JOE:
                if (sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().isView()) {
                    if (sosPermission.getJob().getChange().isHotfolder()) {
                        types.add(JobSchedulerObjectType.JOB);
                        types.add(JobSchedulerObjectType.MONITOR);
                    }
                    if (sosPermission.getJobChain().getChange().isHotFolder()) {
                        types.add(JobSchedulerObjectType.JOBCHAIN);
                        types.add(JobSchedulerObjectType.NODEPARAMS);
                    }
                    if (sosPermission.getOrder().getChange().isHotFolder()) {
                        types.add(JobSchedulerObjectType.ORDER);
                    }
                    if (sosPermission.getProcessClass().getChange().isHotFolder()) {
                        types.add(JobSchedulerObjectType.PROCESSCLASS);
                        types.add(JobSchedulerObjectType.AGENTCLUSTER);
                    }
                    if (sosPermission.getLock().getChange().isHotFolder()) {
                        types.add(JobSchedulerObjectType.LOCK);
                    }
                    if (sosPermission.getSchedule().getChange().isHotFolder()) {
                        types.add(JobSchedulerObjectType.SCHEDULE);
                        types.add(JobSchedulerObjectType.HOLIDAYS);
                    }
                    types.add(JobSchedulerObjectType.FOLDER);
                    types.add(JobSchedulerObjectType.OTHER);
                }
			    
			case MONITOR:
			case JOB:
                if (treeForJoe) {
                    if (sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().isView()) {
                        types.add(type);
                    }
                } else {
                    if (sosPermission.getJob().getView().isStatus()) {
                        types.add(type);
                    }
                }
				break;
			case JOBCHAIN:
				if (treeForJoe) {
                    if (sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().isView()) {
                        types.add(type);
                    }
                } else {
                    if (sosPermission.getJobChain().getView().isStatus()) {
                        types.add(type);
                    }
                }
				break;
			case ORDER:
				if (treeForJoe) {
                    if (sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().isView()) {
                        types.add(type);
                    }
                } else {
                    if (sosPermission.getOrder().getView().isStatus()) {
                        types.add(type);
                    }
                }
				break;
			case PROCESSCLASS:
			case AGENTCLUSTER:
				if (treeForJoe) {
                    if (sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().isView()) {
                        types.add(type);
                    }
                } else {
                    if (sosPermission.getProcessClass().getView().isStatus()) {
                        types.add(type);
                    }
                }
				break;
			case LOCK:
				if (treeForJoe) {
                    if (sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().isView()) {
                        types.add(type);
                    }
                } else {
                    if (sosPermission.getLock().getView().isStatus()) {
                        types.add(type);
                    }
                }
				break;
			case SCHEDULE:
				if (treeForJoe) {
                    if (sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().isView()) {
                        types.add(type);
                    }
                } else {
                    if (sosPermission.getSchedule().getView().isStatus()) {
                        types.add(type);
                    }
                }
				break;
			case WORKINGDAYSCALENDAR:
			case NONWORKINGDAYSCALENDAR:
                if (treeForJoe) {
                    if (sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().isView()) {
                        types.add(type);
                    }
                } else {
                    if (sosPermission.getCalendar().getView().isStatus()) {
                        types.add(type);
                    }
                }
                break;
            case FOLDER:
				break;
			default:
				types.add(type);
				break;
			}
		}
		return types;
	}
	
	public static List<JobSchedulerObjectType> getAllowedJoeTypes(TreeFilter treeBody, SOSPermissionJocCockpit sosPermission) {
        List<JobSchedulerObjectType> types = new ArrayList<JobSchedulerObjectType>();
        
        for (JobSchedulerObjectType type : treeBody.getTypes()) {
            switch (type) {
            case JOE:
                if (sosPermission.getJob().getChange().isHotfolder()) {
                    types.add(JobSchedulerObjectType.JOB);
                    types.add(JobSchedulerObjectType.MONITOR);
                }
                if (sosPermission.getJobChain().getChange().isHotFolder()) {
                    types.add(JobSchedulerObjectType.JOBCHAIN);
                    types.add(JobSchedulerObjectType.NODEPARAMS);
                }
                if (sosPermission.getOrder().getChange().isHotFolder()) {
                    types.add(JobSchedulerObjectType.ORDER);
                }
                if (sosPermission.getProcessClass().getChange().isHotFolder()) {
                    types.add(JobSchedulerObjectType.PROCESSCLASS);
                    types.add(JobSchedulerObjectType.AGENTCLUSTER);
                }
                if (sosPermission.getLock().getChange().isHotFolder()) {
                    types.add(JobSchedulerObjectType.LOCK);
                }
                if (sosPermission.getSchedule().getChange().isHotFolder()) {
                    types.add(JobSchedulerObjectType.SCHEDULE);
                    types.add(JobSchedulerObjectType.HOLIDAYS);
                }
                types.add(JobSchedulerObjectType.FOLDER);
                types.add(JobSchedulerObjectType.OTHER);

            case MONITOR:
            case JOB:
                if (sosPermission.getJob().getChange().isHotfolder()) {
                    types.add(type);
                }
                break;
            case JOBCHAIN:
                if (sosPermission.getJobChain().getChange().isHotFolder()) {
                    types.add(type);
                }
                break;
            case ORDER:
                if (sosPermission.getOrder().getChange().isHotFolder()) {
                    types.add(type);
                }
                break;
            case PROCESSCLASS:
            case AGENTCLUSTER:
                if (sosPermission.getProcessClass().getChange().isHotFolder()) {
                    types.add(type);
                }
                break;
            case LOCK:
                if (sosPermission.getLock().getChange().isHotFolder()) {
                    types.add(type);
                }
                break;
            case SCHEDULE:
                if (sosPermission.getSchedule().getChange().isHotFolder()) {
                    types.add(type);
                }
                break;
            case FOLDER:
                break;
            default:
                types.add(type);
                break;
            }
        }
        return types;
    }

	public static SortedSet<Tree> initFoldersByFoldersFromBody(TreeFilter treeBody, Long instanceId, String schedulerId, boolean treeForJoe)
			throws JocException {
	    Comparator<Tree> byPath = Comparator.comparing(Tree::getPath).reversed();
		SortedSet<Tree> folders = new TreeSet<Tree>(byPath);
		Set<String> bodyTypes = new HashSet<String>();
		Set<String> calendarTypes = new HashSet<String>();
		Set<String> docTypes = new HashSet<String>();
		Set<String> joeTypes = new HashSet<String>();
        if (treeBody.getTypes() != null && !treeBody.getTypes().isEmpty()) {
			for (JobSchedulerObjectType type : treeBody.getTypes()) {
				switch (type) {
				case ORDER:
				    bodyTypes.add("job_chain");
				    joeTypes.add(type.value());
                    break;
				case JOBCHAIN:
					bodyTypes.add("job_chain");
					joeTypes.add(type.value());
					break;
                case AGENTCLUSTER:
					bodyTypes.add("agent_cluster");
					joeTypes.add(type.value());
					break;
				case PROCESSCLASS:
					bodyTypes.add("process_class");
					bodyTypes.add("agent_cluster");
					joeTypes.add(type.value());
					break;
				case WORKINGDAYSCALENDAR:
				    bodyTypes.add("working_days_calendar");
				    calendarTypes.add("WORKING_DAYS");
				    break;
				case NONWORKINGDAYSCALENDAR:
                    bodyTypes.add("non_working_days_calendar");
                    calendarTypes.add("NON_WORKING_DAYS");
                    break;
				case DOCUMENTATION:
				    bodyTypes.add("documentation");
				    docTypes.add("documentation");
				    break;
				default:
					bodyTypes.add(type.value().toLowerCase());
					joeTypes.add(type.value());
					break;
				}
			}
		} else {
		    calendarTypes.add("WORKING_DAYS");
		    calendarTypes.add("NON_WORKING_DAYS");
		    docTypes.add("documentation");
		}

		SOSHibernateSession connection = null;

		try {

			connection = Globals.createSosHibernateStatelessConnection("initFoldersByFoldersFromBody");

			Globals.beginTransaction(connection);
			InventoryFilesDBLayer dbLayer = new InventoryFilesDBLayer(connection);
			CalendarsDBLayer dbCalendarLayer = new CalendarsDBLayer(connection);
			DocumentationDBLayer dbDocLayer = new DocumentationDBLayer(connection);
			DBLayerJoeObjects dbJoeObjectLayer = new DBLayerJoeObjects(connection);
            Set<Tree> results = new HashSet<Tree>();
			Set<Tree> calendarResults = null;
			Set<Tree> docResults = null;
			List<Tree> joeResults = null;
			if (treeBody.getFolders() != null && !treeBody.getFolders().isEmpty()) {
				for (Folder folder : treeBody.getFolders()) {
					String normalizedFolder = ("/" + folder.getFolder()).replaceAll("//+", "/");
					if (treeForJoe) {
                        joeResults = dbJoeObjectLayer.getFoldersByFolder(schedulerId, normalizedFolder, joeTypes, true);
                        if (joeResults != null && !joeResults.isEmpty()) {
                            results.addAll(joeResults);
                        }
                    }
					results.addAll(dbLayer.getFoldersByFolderAndType(instanceId, normalizedFolder, bodyTypes));
					if (!calendarTypes.isEmpty()) {
					    calendarResults = dbCalendarLayer.getFoldersByFolder(schedulerId, normalizedFolder, calendarTypes);
					    if (calendarResults != null && !calendarResults.isEmpty()) {
					        results.addAll(calendarResults);
					    }
					}
					if (!docTypes.isEmpty()) {
                        docResults = dbDocLayer.getFoldersByFolder(schedulerId, normalizedFolder);
                        if (docResults != null && !docResults.isEmpty()) {
                            results.addAll(docResults);
                        }
                    }
					if (results != null && !results.isEmpty()) {
						if (folder.getRecursive() == null || folder.getRecursive()) {
							folders.addAll(results);
						} else {
							Path parent = Paths.get(normalizedFolder);
							for (Tree result : results) {
							    Path r = Paths.get(result.getPath());
							    int endIndex = Math.min(r.getNameCount(), parent.getNameCount() + 1);
							    result.setPath("/" + r.subpath(0, endIndex).toString().replace('\\', '/'));
								folders.add(result);
							}
						}
					}
				}
			} else {
			    if (treeForJoe) {
                    joeResults = dbJoeObjectLayer.getFoldersByFolder(schedulerId, "/", joeTypes, true);
                    if (joeResults != null && !joeResults.isEmpty()) {
                        results.addAll(joeResults);
                    }
                }
				results.addAll(dbLayer.getFoldersByFolderAndType(instanceId, "/", bodyTypes));
				if (!calendarTypes.isEmpty()) {
                    calendarResults = dbCalendarLayer.getFoldersByFolder(schedulerId, "/", calendarTypes);
                    if (calendarResults != null && !calendarResults.isEmpty()) {
                        results.addAll(calendarResults);
                    }
                }
				if (!docTypes.isEmpty()) {
				    docResults = dbDocLayer.getFoldersByFolder(schedulerId, "/");
                    if (docResults != null && !docResults.isEmpty()) {
                        results.addAll(docResults);
                    }
                }
				if (results != null && !results.isEmpty()) {
					folders.addAll(results);
				}
			}
			if (treeForJoe) {
			    DBLayerJoeLocks dbJoeLockLayer = new DBLayerJoeLocks(connection);
			    folders = dbJoeLockLayer.setLockedBy(schedulerId, folders, byPath);
			}
			return folders;
		} catch (JocException e) {
			throw e;
		} finally {
			Globals.disconnect(connection);
		}
	}
	
	public static SortedSet<JoeTree> initJoeFoldersByFoldersFromBody(TreeFilter treeBody, Long instanceId, String schedulerId, ZoneId zoneId)
            throws JocException {
        Comparator<JoeTree> byPath = Comparator.comparing(JoeTree::getPath).reversed();
        SortedSet<JoeTree> folders = new TreeSet<JoeTree>(byPath);
        Set<String> bodyTypes = new HashSet<String>();
        Set<String> joeTypes = new HashSet<String>();
        joeTypes.add("FOLDER");
        if (treeBody.getTypes() != null && !treeBody.getTypes().isEmpty()) {
            for (JobSchedulerObjectType type : treeBody.getTypes()) {
                switch (type) {
                case ORDER:
                    bodyTypes.add("job_chain");
                    joeTypes.add(type.value());
                    break;
                case JOBCHAIN:
                    bodyTypes.add("job_chain");
                    joeTypes.add(type.value());
                    break;
                case AGENTCLUSTER:
                    bodyTypes.add("agent_cluster");
                    joeTypes.add(type.value());
                    break;
                case PROCESSCLASS:
                    bodyTypes.add("process_class");
                    bodyTypes.add("agent_cluster");
                    joeTypes.add(type.value());
                    break;
                case WORKINGDAYSCALENDAR:
                case NONWORKINGDAYSCALENDAR:
                case DOCUMENTATION:
                    break;
                default:
                    bodyTypes.add(type.value().toLowerCase());
                    joeTypes.add(type.value());
                    break;
                }
            }
        }

        SOSHibernateSession connection = null;

        try {

            connection = Globals.createSosHibernateStatelessConnection("initJoeFoldersByFoldersFromBody");

            Globals.beginTransaction(connection);
            InventoryFilesDBLayer dbLayer = new InventoryFilesDBLayer(connection);
            DBLayerJoeObjects dbJoeObjectLayer = new DBLayerJoeObjects(connection);
            Set<JoeTree> results = new HashSet<JoeTree>();
            List<JoeTree> joeResults = null;
            if (treeBody.getFolders() != null && !treeBody.getFolders().isEmpty()) {
                for (Folder folder : treeBody.getFolders()) {
                    String normalizedFolder = ("/" + folder.getFolder()).replaceAll("//+", "/");
                    joeResults = dbJoeObjectLayer.getFoldersByFolder(schedulerId, normalizedFolder, joeTypes, false);
                    if (joeResults != null && !joeResults.isEmpty()) {
                        results.addAll(joeResults);
                    }
                    results.addAll(dbLayer.getFoldersByFolderAndType(instanceId, normalizedFolder, bodyTypes, joeResults, zoneId));
                    if (results != null && !results.isEmpty()) {
                        if (folder.getRecursive() == null || folder.getRecursive()) {
                            folders.addAll(results);
                        } else {
                            final int parentDepth = Paths.get(normalizedFolder).getNameCount();
                            folders.addAll(results.stream().filter(item -> Paths.get(item.getPath()).getNameCount() == parentDepth + 1).collect(
                                    Collectors.toSet()));
                        }
                    }
                }
            } else {
                joeResults = dbJoeObjectLayer.getFoldersByFolder(schedulerId, "/", joeTypes, false);
                if (joeResults != null && !joeResults.isEmpty()) {
                    results.addAll(joeResults);
                }
                results.addAll(dbLayer.getFoldersByFolderAndType(instanceId, "/", bodyTypes, joeResults, zoneId));
                if (results != null && !results.isEmpty()) {
                    folders.addAll(results);
                }
            }
            DBLayerJoeLocks dbJoeLockLayer = new DBLayerJoeLocks(connection);
            folders = dbJoeLockLayer.setLockedBy(schedulerId, folders, byPath);
            return folders;
        } catch (JocException e) {
            throw e;
        } finally {
            Globals.disconnect(connection);
        }
    }

	@SuppressWarnings("unchecked")
    public static <T extends Tree> T getTree(SortedSet<T> folders, SOSShiroFolderPermissions sosShiroFolderPermissions) {
		Map<Path, TreeModel> treeMap = new HashMap<Path, TreeModel>();
		Set<Folder> listOfFolders = sosShiroFolderPermissions.getListOfFolders();
		for (T folder : folders) {
			if (sosShiroFolderPermissions.isPermittedForFolder(folder.getPath(), listOfFolders)) {

				Path pFolder = Paths.get(folder.getPath());
				TreeModel tree = new TreeModel();
				if (treeMap.containsKey(pFolder)) {
					tree = treeMap.get(pFolder);
					tree = setFolderItemProps(folder, tree);
				} else {
					tree.setPath(folder.getPath());
					Path fileName = pFolder.getFileName();
					tree.setName(fileName == null ? "" : fileName.toString());
					tree.setFolders(null);
					tree = setFolderItemProps(folder, tree);
					treeMap.put(pFolder, tree);
				}
				fillTreeMap(treeMap, pFolder, tree);
			}
		}
		if (treeMap.isEmpty()) {
			return null;
		}

		return (T) treeMap.get(Paths.get("/"));
	}
	
	public static JoeTree getJoeTree(SortedSet<JoeTree> folders, SOSShiroFolderPermissions sosShiroFolderPermissions) {
        Map<Path, TreeModel> treeMap = new HashMap<Path, TreeModel>();
        Set<Folder> listOfFolders = sosShiroFolderPermissions.getListOfFolders();
        for (JoeTree folder : folders) {
            if (sosShiroFolderPermissions.isPermittedForFolder(folder.getPath(), listOfFolders)) {

                Path pFolder = Paths.get(folder.getPath());
                TreeModel tree = new TreeModel();
                if (treeMap.containsKey(pFolder)) {
                    tree = treeMap.get(pFolder);
                    tree = setJoeFolderItemProps(folder, tree);
                } else {
                    tree.setPath(folder.getPath());
                    Path fileName = pFolder.getFileName();
                    tree.setName(fileName == null ? "" : fileName.toString());
                    tree.setFolders(null);
                    tree = setJoeFolderItemProps(folder, tree);
                    treeMap.put(pFolder, tree);
                }
                fillTreeMap(treeMap, pFolder, tree);
            }
        }
        if (treeMap.isEmpty()) {
            return null;
        }

        return treeMap.get(Paths.get("/"));
    }

    private static <T extends Tree> TreeModel setFolderItemProps(T folder, TreeModel tree) {
        if (folder.getDeleted() != null && folder.getDeleted()) {
            tree.setDeleted(true);
        }
        if (folder.getLockedBy() != null && !folder.getLockedBy().isEmpty()) {
            tree.setLockedBy(folder.getLockedBy());
        }
        if (folder.getLockedSince() != null) {
            tree.setLockedSince(folder.getLockedSince());
        }
        tree.setAgentClusters(null);
        tree.setJobChains(null);
        tree.setJobs(null);
        tree.setLocks(null);
        tree.setMonitors(null);
        tree.setOrders(null);
        tree.setProcessClasses(null);
        tree.setSchedules(null);
        return tree;
    }
	
	private static TreeModel setJoeFolderItemProps(JoeTree folder, TreeModel tree) {
	    if (folder.getDeleted() != null && folder.getDeleted()) {
            tree.setDeleted(true); 
        }
        if (folder.getLockedBy() != null && !folder.getLockedBy().isEmpty()) {
            tree.setLockedBy(folder.getLockedBy()); 
        }
        if (folder.getLockedSince() != null) {
            tree.setLockedSince(folder.getLockedSince()); 
        }
        if (folder.getAgentClusters() != null && !folder.getAgentClusters().isEmpty()) {
            tree.setAgentClusters(folder.getAgentClusters()); 
        } else {
            tree.setAgentClusters(null); 
        }
        if (folder.getJobChains() != null && !folder.getJobChains().isEmpty()) {
            tree.setJobChains(folder.getJobChains()); 
        } else {
            tree.setJobChains(null); 
        }
        if (folder.getJobs() != null && !folder.getJobs().isEmpty()) {
            tree.setJobs(folder.getJobs()); 
        } else {
            tree.setJobs(null); 
        }
        if (folder.getLocks() != null && !folder.getLocks().isEmpty()) {
            tree.setLocks(folder.getLocks()); 
        } else {
            tree.setLocks(null); 
        }
        if (folder.getMonitors() != null && !folder.getMonitors().isEmpty()) {
            tree.setMonitors(folder.getMonitors()); 
        } else {
            tree.setMonitors(null); 
        }
        if (folder.getOrders() != null && !folder.getOrders().isEmpty()) {
            tree.setOrders(folder.getOrders()); 
        } else {
            tree.setOrders(null); 
        }
        if (folder.getProcessClasses() != null && !folder.getProcessClasses().isEmpty()) {
            tree.setProcessClasses(folder.getProcessClasses()); 
        } else {
            tree.setProcessClasses(null); 
        }
        if (folder.getSchedules() != null && !folder.getSchedules().isEmpty()) {
            tree.setSchedules(folder.getSchedules()); 
        } else {
            tree.setSchedules(null); 
        }
        return tree;
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
                parentTree.setAgentClusters(null);
                parentTree.setJobChains(null);
                parentTree.setJobs(null);
                parentTree.setLocks(null);
                parentTree.setMonitors(null);
                parentTree.setOrders(null);
                parentTree.setProcessClasses(null);
                parentTree.setSchedules(null);
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