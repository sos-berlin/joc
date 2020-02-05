package com.sos.joc.tree.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import javax.ws.rs.Path;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.tree.TreePermanent;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.tree.JoeTree;
import com.sos.joc.model.tree.JoeTreeView;
import com.sos.joc.model.tree.Tree;
import com.sos.joc.model.tree.TreeFilter;
import com.sos.joc.model.tree.TreeView;
import com.sos.joc.tree.resource.ITreeResource;
import com.sos.schema.JsonValidator;

@Path("tree")
public class TreeResourceImpl extends JOCResourceImpl implements ITreeResource {

    private static final String API_CALL = "./tree";

    @Override
	public JOCDefaultResponse postTree(String accessToken, byte[] treeBodyBytes) {
        try {
		    JsonValidator.validateFailFast(treeBodyBytes, TreeFilter.class);
		    TreeFilter treeBody = Globals.objectMapper.readValue(treeBodyBytes, TreeFilter.class);
            
            List<JobSchedulerObjectType> types = null;
            boolean permission = false;
            SOSPermissionJocCockpit sosPermission = getPermissonsJocCockpit(treeBody.getJobschedulerId(), accessToken);
            boolean treeForJoe = (treeBody.getForJoe() != null && treeBody.getForJoe()) || treeBody.getTypes().contains(JobSchedulerObjectType.JOE);
            if (treeBody.getTypes() == null || treeBody.getTypes().isEmpty()) {
                permission = true;
            } else {
                types = TreePermanent.getAllowedTypes(treeBody, sosPermission, treeForJoe);
                treeBody.setTypes(types);
                permission = types.size() > 0;
            }
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, treeBody, accessToken, treeBody.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            // Boolean compact = treeBody.getCompact();
            if (treeBody.getFolders() != null && !treeBody.getFolders().isEmpty()) {
                checkFoldersFilterParam(treeBody.getFolders());
            }
            SortedSet<Tree> folders = TreePermanent.initFoldersByFoldersFromBody(treeBody, dbItemInventoryInstance.getId(), dbItemInventoryInstance
                    .getSchedulerId(), treeForJoe);
            folderPermissions.setForce(treeBody.getForce());

            if (treeBody.getTypes().size() > 0 && treeBody.getTypes().get(0) == JobSchedulerObjectType.WORKINGDAYSCALENDAR) {
                folderPermissions = jobschedulerUser.getSosShiroCurrentUser().getSosShiroCalendarFolderPermissions();
                folderPermissions.setSchedulerId(treeBody.getJobschedulerId());
            }

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
        }
    }

    private void checkFoldersFilterParam(List<Folder> folders) throws Exception {
        if (folders != null && !folders.isEmpty()) {
            for (Folder folder : folders) {
                checkRequiredParameter("folder", folder.getFolder());
            }
        }

    }

    @Override
    public JOCDefaultResponse postJoeTree(String accessToken, byte[] treeBodyBytes) {
        try {
            JsonValidator.validateFailFast(treeBodyBytes, TreeFilter.class);
            TreeFilter treeBody = Globals.objectMapper.readValue(treeBodyBytes, TreeFilter.class);
            
            List<JobSchedulerObjectType> types = null;
            SOSPermissionJocCockpit sosPermission = getPermissonsJocCockpit(treeBody.getJobschedulerId(), accessToken);
            boolean permission = sosPermission.getJobschedulerMaster().getAdministration().getConfigurations().isView();

            if (treeBody.getTypes() == null || treeBody.getTypes().isEmpty()) {
                treeBody.setTypes(Arrays.asList(JobSchedulerObjectType.JOE));
            }
            types = TreePermanent.getJoeTypes(treeBody);
            treeBody.setTypes(types);
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, treeBody, accessToken, treeBody.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            // Boolean compact = treeBody.getCompact();
            if (treeBody.getFolders() != null && !treeBody.getFolders().isEmpty()) {
                checkFoldersFilterParam(treeBody.getFolders());
            }
            SortedSet<JoeTree> folders = TreePermanent.initJoeFoldersByFoldersFromBody(treeBody, dbItemInventoryInstance.getId(), dbItemInventoryInstance
                    .getSchedulerId(), ZoneId.of(dbItemInventoryInstance.getTimeZone()));
            folderPermissions.setForce(treeBody.getForce());

            JoeTree root = TreePermanent.getJoeTree(folders, folderPermissions);

            JoeTreeView entity = new JoeTreeView();
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
        }
    }
}