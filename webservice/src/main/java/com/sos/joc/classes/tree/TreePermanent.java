package com.sos.joc.classes.tree;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.sos.auth.rest.SOSShiroFolderPermissions;
import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.db.calendars.CalendarsDBLayer;
import com.sos.joc.db.inventory.files.InventoryFilesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.tree.Tree;
import com.sos.joc.model.tree.TreeFilter;

public class TreePermanent {

	public static List<JobSchedulerObjectType> getAllowedTypes(TreeFilter treeBody,
			SOSPermissionJocCockpit sosPermission) {
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
			case WORKINGDAYSCALENDAR:
                if (sosPermission.getCalendar().getView().isStatus()) {
                    types.add(type);
                }
                break;
			case NONWORKINGDAYSCALENDAR:
                if (sosPermission.getCalendar().getView().isStatus()) {
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

	public static SortedSet<String> initFoldersByFoldersFromBody(TreeFilter treeBody, Long instanceId)
			throws JocException {
		SortedSet<String> folders = new TreeSet<String>(new Comparator<String>() {

			public int compare(String a, String b) {
				return b.compareTo(a);
			}
		});
		Set<String> bodyTypes = new HashSet<String>();
		Set<String> calendarTypes = new HashSet<String>();
        if (treeBody.getTypes() != null && !treeBody.getTypes().isEmpty()) {
			for (JobSchedulerObjectType type : treeBody.getTypes()) {
				switch (type) {
				case ORDER:
				case JOBCHAIN:
					bodyTypes.add("job_chain");
					break;
				case AGENTCLUSTER:
					bodyTypes.add("agent_cluster");
					break;
				case PROCESSCLASS:
					bodyTypes.add("process_class");
					bodyTypes.add("agent_cluster");
					break;
				case WORKINGDAYSCALENDAR:
				    bodyTypes.add("working_days_calendar");
				    calendarTypes.add("WORKING_DAYS");
				    break;
				case NONWORKINGDAYSCALENDAR:
                    bodyTypes.add("non_working_days_calendar");
                    calendarTypes.add("NON_WORKING_DAYS");
                    break;
				default:
					bodyTypes.add(type.name().toLowerCase());
					break;
				}
			}
		} else {
		    calendarTypes.add("WORKING_DAYS");
		    calendarTypes.add("NON_WORKING_DAYS"); 
		}

		SOSHibernateSession connection = null;

		try {

			connection = Globals.createSosHibernateStatelessConnection("initFoldersByFoldersFromBody");

			Globals.beginTransaction(connection);
			InventoryFilesDBLayer dbLayer = new InventoryFilesDBLayer(connection);
			CalendarsDBLayer dbCalendarLayer = new CalendarsDBLayer(connection);
			List<String> results = null;
			List<String> calendarResults = null;
			if (treeBody.getFolders() != null && !treeBody.getFolders().isEmpty()) {
				for (Folder folder : treeBody.getFolders()) {
					String normalizedFolder = ("/" + folder.getFolder()).replaceAll("//+", "/");
					results = dbLayer.getFoldersByFolderAndType(instanceId, normalizedFolder, bodyTypes);
					if (!calendarTypes.isEmpty()) {
					    calendarResults = dbCalendarLayer.getFoldersByFolder(instanceId, normalizedFolder, calendarTypes);
					    if (calendarResults != null && !calendarResults.isEmpty()) {
					        if (results == null) {
					            results = new ArrayList<String>();
					        } 
					        results.addAll(calendarResults);
					    }
					}
					if (results != null && !results.isEmpty()) {
						if (folder.getRecursive() == null || folder.getRecursive()) {
							folders.addAll(results);
						} else {
							Path parent = Paths.get(normalizedFolder);
							for (String result : results) {
							    Path r = Paths.get(result);
							    int endIndex = Math.min(r.getNameCount(), parent.getNameCount() + 1);
								folders.add("/" + r.subpath(0, endIndex).toString().replace('\\', '/'));
							}
						}
					}
				}
			} else {
				results = dbLayer.getFoldersByFolderAndType(instanceId, "/", bodyTypes);
				if (!calendarTypes.isEmpty()) {
                    calendarResults = dbCalendarLayer.getFoldersByFolder(instanceId, "/", calendarTypes);
                    if (calendarResults != null && !calendarResults.isEmpty()) {
                        if (results == null) {
                            results = new ArrayList<String>();
                        } 
                        results.addAll(calendarResults);
                    }
                }
				if (results != null && !results.isEmpty()) {
					folders.addAll(results);
				}
			}
			return folders;
		} catch (JocException e) {
			throw e;
		} finally {
			Globals.disconnect(connection);
		}
	}

	public static Tree getTree(SortedSet<String> folders, SOSShiroFolderPermissions sosShiroFolderPermissions) {
		Map<Path, TreeModel> treeMap = new HashMap<Path, TreeModel>();
		for (String folder : folders) {
			if (sosShiroFolderPermissions != null && sosShiroFolderPermissions.isPermittedForFolder(folder)) {

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