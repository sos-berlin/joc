package com.sos.joc.tree.impl;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.tree.TreePermanent;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.tree.Tree;
import com.sos.joc.model.tree.TreeFilter;
import com.sos.joc.model.tree.TreeView;
import com.sos.joc.tree.resource.ITreeResource;

@Path("tree")
public class TreeResourceImpl extends JOCResourceImpl implements ITreeResource {

	private static final String API_CALL = "./tree";

	@Override
	public JOCDefaultResponse postTree(String xAccessToken, String accessToken, TreeFilter treeBody) throws Exception {
		return postTree(getAccessToken(xAccessToken, accessToken), treeBody);
	}

	public JOCDefaultResponse postTree(String accessToken, TreeFilter treeBody) throws Exception {
		try {
			List<JobSchedulerObjectType> types = null;
			boolean permission = false;
			SOSPermissionJocCockpit sosPermission = getPermissonsJocCockpit(treeBody.getJobschedulerId(), accessToken);
			if (treeBody.getTypes() == null || treeBody.getTypes().isEmpty()) {
				permission = true;
			} else {
				types = TreePermanent.getAllowedTypes(treeBody, sosPermission);
				treeBody.setTypes(types);
				permission = types.size() > 0;
			}
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, treeBody, accessToken, treeBody.getJobschedulerId(),
					permission);
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}
			// Boolean compact = treeBody.getCompact();
			if (treeBody.getFolders() != null && !treeBody.getFolders().isEmpty()) {
				checkFoldersFilterParam(treeBody.getFolders());
			}
			SortedSet<String> folders = TreePermanent.initFoldersByFoldersFromBody(treeBody, dbItemInventoryInstance.getId(), dbItemInventoryInstance.getSchedulerId());
            folderPermissions.setForce(treeBody.getForce());
            Tree root = TreePermanent.getTree(folders, folderPermissions);

			TreeView entity = new TreeView();
			if (root != null) {
				entity.getFolders().add(root);
			}
			entity.setDeliveryDate(Date.from(Instant.now()));
			return JOCDefaultResponse.responseStatus200(entity);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		} finally {
		}
	}

	private void checkFoldersFilterParam(List<Folder> folders) throws Exception {
		if (folders != null && !folders.isEmpty()) {
			for (Folder folder : folders) {
				checkRequiredParameter("folder", folder.getFolder());
			}
		}

	}
}