package com.sos.joc.processClasses.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryProcessClass;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.processclasses.ProcessClassPermanent;
import com.sos.joc.db.inventory.processclasses.InventoryProcessClassesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.processClass.ProcessClassP;
import com.sos.joc.model.processClass.ProcessClassPath;
import com.sos.joc.model.processClass.ProcessClassesFilter;
import com.sos.joc.model.processClass.ProcessClassesP;
import com.sos.joc.processClasses.resource.IProcessClassesResourceP;

@Path("process_classes")
public class ProcessClassesResourcePImpl extends JOCResourceImpl implements IProcessClassesResourceP {

	private static final String API_CALL = "./process_classes/p";

	@Override
	public JOCDefaultResponse postProcessClassesP(String xAccessToken, String accessToken,
			ProcessClassesFilter processClassFilter) throws Exception {
		return postProcessClassesP(getAccessToken(xAccessToken, accessToken), processClassFilter);
	}

	public JOCDefaultResponse postProcessClassesP(String accessToken, ProcessClassesFilter processClassFilter)
			throws Exception {
		SOSHibernateSession connection = null;

		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, processClassFilter, accessToken,
					processClassFilter.getJobschedulerId(),
					getPermissonsJocCockpit(processClassFilter.getJobschedulerId(), accessToken).getLock().getView()
							.isStatus());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			connection = Globals.createSosHibernateStatelessConnection(API_CALL);
			Globals.beginTransaction(connection);
			ProcessClassesP entity = new ProcessClassesP();
			InventoryProcessClassesDBLayer dbLayer = new InventoryProcessClassesDBLayer(connection);
			List<ProcessClassP> listOfProcessClasses = new ArrayList<ProcessClassP>();
			List<ProcessClassPath> processClassPaths = processClassFilter.getProcessClasses();
			List<Folder> folders = addPermittedFolder(processClassFilter.getFolders());

			if (processClassPaths != null && !processClassPaths.isEmpty()) {
                Set<Folder> foldersSet = folderPermissions.getListOfFolders();
				for (ProcessClassPath processClassPath : processClassPaths) {
					checkRequiredParameter("processClass", processClassPath.getProcessClass());
					DBItemInventoryProcessClass processClassFromDb = dbLayer.getProcessClass(
							normalizePath(processClassPath.getProcessClass()), dbItemInventoryInstance.getId());
					if (processClassFromDb == null) {
						continue;
					}
                    if (canAdd(processClassFromDb.getName(), foldersSet)) {
                        listOfProcessClasses.add(ProcessClassPermanent.getProcessClassP(dbLayer, processClassFromDb));
                    }
                }
			} else if (folders != null && !folders.isEmpty()) {
				Map<String, ProcessClassP> mapOfProcessClasses = new HashMap<String, ProcessClassP>();
				for (Folder folder : folders) {
					List<DBItemInventoryProcessClass> processClassesFromDb = null;
					processClassesFromDb = dbLayer.getProcessClassesByFolders(normalizeFolder(folder.getFolder()),
							dbItemInventoryInstance.getId(), folder.getRecursive().booleanValue());
                    processClassesFromDb = addAllPermittedProcessClasses(processClassesFromDb);
                    Map<String, ProcessClassP> processClassesToAdd = ProcessClassPermanent.getProcessClassesMap(dbLayer, processClassesFromDb,
                            processClassFilter.getRegex());
                    mapOfProcessClasses.putAll(processClassesToAdd);
                }
                listOfProcessClasses = new ArrayList<ProcessClassP>(mapOfProcessClasses.values());
            } else {
                List<DBItemInventoryProcessClass> processClassesFromDb = dbLayer.getProcessClasses(dbItemInventoryInstance.getId());
                processClassesFromDb = addAllPermittedProcessClasses(processClassesFromDb);
				listOfProcessClasses = ProcessClassPermanent.getProcessClassesList(dbLayer, processClassesFromDb,
						processClassFilter.getRegex());
			}
			entity.setProcessClasses(listOfProcessClasses);
			entity.setDeliveryDate(Date.from(Instant.now()));
			return JOCDefaultResponse.responseStatus200(entity);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		} finally {
			Globals.disconnect(connection);
		}
    }
    
    private List<DBItemInventoryProcessClass> addAllPermittedProcessClasses(List<DBItemInventoryProcessClass> processClassesToAdd) {
        if (folderPermissions == null) {
            return processClassesToAdd;
        }
        Set<Folder> folders = folderPermissions.getListOfFolders();
        if (folders.isEmpty()) {
            return processClassesToAdd;
        }
        List<DBItemInventoryProcessClass> listOfProcessClasses = new ArrayList<DBItemInventoryProcessClass>();
        for (DBItemInventoryProcessClass processClass : processClassesToAdd) {
            if (processClass != null && canAdd(processClass.getName(), folders)) {
                listOfProcessClasses.add(processClass);
            }
        }
        return listOfProcessClasses;
	}

}