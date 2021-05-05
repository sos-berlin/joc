package com.sos.joc.locks.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.classes.locks.LockVolatile;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.locks.resource.ILocksResource;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.lock.LockPath;
import com.sos.joc.model.lock.LockV;
import com.sos.joc.model.lock.LocksFilter;
import com.sos.joc.model.lock.LocksV;
import com.sos.schema.JsonValidator;

@Path("locks")
public class LocksResourceImpl extends JOCResourceImpl implements ILocksResource {

    private static final String API_CALL = "./locks";
    private JOCXmlCommand jocXmlCommand;

    @Override
    public JOCDefaultResponse postLocks(String accessToken, byte[] locksFilterBytes) {
        try {
            JsonValidator.validateFailFast(locksFilterBytes, LocksFilter.class);
            LocksFilter locksFilter = Globals.objectMapper.readValue(locksFilterBytes, LocksFilter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, locksFilter, accessToken, locksFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    locksFilter.getJobschedulerId(), accessToken).getLock().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            boolean withFolderFilter = locksFilter.getFolders() != null && !locksFilter.getFolders().isEmpty();
            final Set<Folder> folders = addPermittedFolders(locksFilter.getFolders());
            boolean filteredByLocks = (locksFilter.getLocks() != null && locksFilter.getLocks().size() > 0);
            Set<String> lockPathFilter = new HashSet<String>();
            Set<Folder> permittedFolders = folderPermissions.getListOfFolders();
            jocXmlCommand = new JOCXmlCommand(this);
            String command = null;
            if (filteredByLocks) {
                Set<Folder> filteredFolders = new HashSet<Folder>();
                for (LockPath l : locksFilter.getLocks()) {
                    l.setLock(normalizePath(l.getLock()));
                    if (canAdd(l.getLock(), permittedFolders)) {
                        Folder folder = new Folder();
                        folder.setFolder(getParent(l.getLock()));
                        folder.setRecursive(false);
                        folders.add(folder);
                        filteredFolders.add(folder);
                        lockPathFilter.add(l.getLock());
                    }
                }
                if (!filteredFolders.isEmpty()) {
                    command = createLocksPostCommand(filteredFolders);
                }
            } else if (withFolderFilter && (folders == null || folders.isEmpty())) {
                // no folder permissions
            } else if (folders != null && !folders.isEmpty()) {
                command = createLocksPostCommand(folders);
            } else {
                command = createLocksPostCommand(permittedFolders);
            }
            
            LocksV entity = new LocksV();
            if (command != null) {
                jocXmlCommand.executePostWithThrowBadRequestAfterRetry(command, accessToken);
                NodeList locks = jocXmlCommand.selectNodelist("//locks/lock");

                List<LockV> locksV = new ArrayList<LockV>();
                for (int i = 0; i < locks.getLength(); i++) {
                    Element lock = (Element) locks.item(i);

                    if (filteredByLocks && !lockPathFilter.contains(lock.getAttribute("path"))) {
                        continue;
                    }
                    if (!FilterAfterResponse.matchRegex(locksFilter.getRegex(), lock.getAttribute("path"))) {
                        continue;
                    }

                    LockVolatile lockV = new LockVolatile(lock, jocXmlCommand);
                    lockV.setFields();
                    locksV.add(lockV);
                }
                entity.setLocks(locksV);
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

    private String createLocksPostCommand(Set<Folder> folders) throws JocMissingRequiredParameterException {
        if (folders == null || folders.size() == 0) {
            return createShowStatePostCommand(null, true);
        } else {
            StringBuilder commands = new StringBuilder();
            commands.append("<commands>");
            for (Folder folder : folders) {
                if (folder.getFolder() == null || folder.getFolder().isEmpty()) {
                    throw new JocMissingRequiredParameterException("undefined folder");
                }
                commands.append(createShowStatePostCommand(folder.getFolder(), folder.getRecursive()));
            }
            commands.append("</commands>");
            return commands.toString();
        }
    }

    private String createShowStatePostCommand(String folder, Boolean recursive) {
        String what = "folders";
        String path = null;
        if (!recursive) {
            what += " no_subfolders";
        }
        if (folder != null) {
            path = ("/" + folder.trim()).replaceAll("//+", "/");
        }
        return jocXmlCommand.getShowStateCommand("folder lock", what, path);
    }
}