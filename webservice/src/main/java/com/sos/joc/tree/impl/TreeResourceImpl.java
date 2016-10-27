package com.sos.joc.tree.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.tree.TreePermanent;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.tree.Tree;
import com.sos.joc.model.tree.TreeFilter;
import com.sos.joc.model.tree.TreeView;
import com.sos.joc.tree.resource.ITreeResource;

@Path("tree")
public class TreeResourceImpl extends JOCResourceImpl implements ITreeResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TreeResourceImpl.class);
    private static final String API_CALL = "./tree";

    @Override
    public JOCDefaultResponse postTree(String accessToken, TreeFilter treeBody) throws Exception {
        LOGGER.debug(API_CALL);

        try {
            boolean permission = false;
            SOSPermissionJocCockpit sosPermission = getPermissons(accessToken);
            if (treeBody.getTypes() == null || treeBody.getTypes().isEmpty()) {
                permission = true;
            } else {
                List<JobSchedulerObjectType> types = TreePermanent.getAllowedTypes(treeBody, sosPermission);
                treeBody.setTypes(types);
                permission = types.size() > 0;
            }
            JOCDefaultResponse jocDefaultResponse = init(treeBody.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            List<String> folders = TreePermanent.initFoldersFromBody(treeBody, dbItemInventoryInstance.getId());
            Set<String> folderSet = new HashSet<String>();
            if(folders != null && !folders.isEmpty()) {
                folderSet.addAll(folders);
            }
            Tree root = new Tree();
            root.setPath("/");
            root.setName("");
            TreePermanent.getTree(root, folderSet);
            TreePermanent.mergeTreeDuplications(root);
            
            TreeView entity = new TreeView();
            entity.getFolders().add(root);
            if(treeBody.getTypes() == null || treeBody.getTypes().isEmpty()) {
                entity.setJobChains(null);
                entity.setJobs(null);
                entity.setOrders(null);
                entity.setLocks(null);
                entity.setProcessClasses(null);
                entity.setSchedules(null);
            }            
            entity.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, treeBody));
           return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, treeBody));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }

}