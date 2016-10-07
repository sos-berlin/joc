package com.sos.joc.tree.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.permission.model.SOSPermissionJocCockpit;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Configuration.Type;
import com.sos.joc.model.tree.TreeFilterSchema;
import com.sos.joc.model.tree.TreeSchema;
import com.sos.joc.model.tree.TreeViewSchema;
import com.sos.joc.tree.resource.ITreeResource;

@Path("tree")
public class TreeResourceImpl extends JOCResourceImpl implements ITreeResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TreeResourceImpl.class);

    @Override
    public JOCDefaultResponse postTree(String accessToken, TreeFilterSchema treeBody) throws Exception {
        LOGGER.debug("init tree");

        try {
            boolean permission = false;
            SOSPermissionJocCockpit sosPermission = getPermissons(accessToken);
            if (treeBody.getTypes() == null || treeBody.getTypes().size() == 0) {
                permission = true;
            } else {
                for (Type type : treeBody.getTypes()) {
                    switch (type) {
                    case JOB: 
                        permission = sosPermission.getJob().getView().isStatus();
                        break;
                    case JOBCHAIN: 
                        permission = sosPermission.getJobChain().getView().isStatus();
                        break;
                    case ORDER: 
                        permission = sosPermission.getOrder().getView().isStatus();
                        break;
                    case PROCESSCLASS: 
                        permission = sosPermission.getProcessClass().getView().isStatus();
                        break;
                    case LOCK: 
                        permission = sosPermission.getLock().getView().isStatus();
                        break;
                    case SCHEDULE: 
                        permission = sosPermission.getSchedule().getView().isStatus();
                        break;
                    case OTHER: 
                        permission = true;
                        break;
                    }
                    if (!permission) {
                        break;
                    }
                }
            }
            
            JOCDefaultResponse jocDefaultResponse = init(treeBody.getJobschedulerId(), permission);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            TreeSchema sos = new TreeSchema();
            sos.setPath("/sos");
            sos.setName("sos");
            sos.setFolders(null);
            TreeSchema root = new TreeSchema();
            root.setPath("/");
            root.setName("");
            root.getFolders().add(sos);
            
            TreeViewSchema entity = new TreeViewSchema();
            entity.setDeliveryDate(Date.from(Instant.now()));
            entity.getFolders().add(root);
            
            return JOCDefaultResponse.responseStatus200(entity);
        
        } catch (JocException e) {
            return JOCDefaultResponse.responseStatusJSError(e);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e);
        }
    }
}
