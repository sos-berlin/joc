package com.sos.joc.locks.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.filters.FilterAfterResponse;
import com.sos.joc.classes.locks.LockVolatile;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.locks.resource.ILocksResource;
import com.sos.joc.model.common.Folder;
import com.sos.joc.model.lock.LockPath;
import com.sos.joc.model.lock.LockV;
import com.sos.joc.model.lock.LocksFilter;
import com.sos.joc.model.lock.LocksV;
import com.sos.scheduler.model.commands.JSCmdShowState;

@Path("locks")
public class LocksResourceImpl extends JOCResourceImpl implements ILocksResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocksResourceImpl.class);
    private static final String API_CALL = "./locks";

    @Override
    public JOCDefaultResponse postLocks(String accessToken, LocksFilter locksFilter) throws Exception {
        LOGGER.debug(API_CALL);
        try {
            JOCDefaultResponse jocDefaultResponse = init(accessToken, locksFilter.getJobschedulerId(), getPermissons(accessToken).getLock().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance.getUrl());
            jocXmlCommand.executePost(createLocksPostCommand(locksFilter));
            jocXmlCommand.throwJobSchedulerError();
            NodeList locks = jocXmlCommand.selectNodelist("//locks/lock");
            boolean filteredByLocks = (locksFilter.getLocks() != null && locksFilter.getLocks().size() > 0);

            List<LockV> locksV = new ArrayList<LockV>();
            for (int i = 0; i < locks.getLength(); i++) {
                Element lock = (Element) locks.item(i);

                if (filteredByLocks && !locksFilter.getLocks().contains(lock.getAttribute("path"))) {
                    continue;
                }
                if (!FilterAfterResponse.matchRegex(locksFilter.getRegex(), lock.getAttribute("path"))) {
                    continue;
                }

                LockVolatile lockV = new LockVolatile(lock, jocXmlCommand);
                lockV.setFields();
                locksV.add(lockV);
            }

            LocksV entity = new LocksV();
            entity.setLocks(locksV);
            entity.setDeliveryDate(Date.from(Instant.now()));

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getMetaInfo(API_CALL, locksFilter));
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            JocError err = new JocError();
            err.addMetaInfoOnTop(getMetaInfo(API_CALL, locksFilter));
            return JOCDefaultResponse.responseStatusJSError(e, err);
        }
    }

    private String createLocksPostCommand(LocksFilter lockFilter) throws JocMissingRequiredParameterException {
        List<LockPath> locks = lockFilter.getLocks();
        if (locks == null || locks.size() == 0) {
            return createLocksPostCommand(new HashSet<Folder>(lockFilter.getFolders()));
        } else {
            Set<Folder> folders = new HashSet<Folder>();
            for (LockPath lockPath : locks) {
                Folder folder = new Folder();
                folder.setFolder(("/" + lockPath.getLock()).replaceAll("//+", "/").replaceFirst("/[^/]+$", ""));
                folder.setRecursive(false);
                folders.add(folder);
            }
            return createLocksPostCommand(folders);
        }
    }

    private String createLocksPostCommand(Set<Folder> folders) throws JocMissingRequiredParameterException {
        if (folders == null || folders.size() == 0) {
            return createShowStatePostCommand(null, true);
        } else {
            StringBuilder s = new StringBuilder();
            s.append("<commands>");
            for (Folder folder : folders) {
                if (folder.getFolder() == null || folder.getFolder().isEmpty()) {
                    throw new JocMissingRequiredParameterException("undefined folder");
                }
                s.append(createShowStatePostCommand(folder.getFolder(), folder.getRecursive()));
            }
            s.append("</commands>");
            return s.toString();
        }
    }

    private String createShowStatePostCommand(String folder, Boolean recursive) {
        JSCmdShowState showState = Globals.schedulerObjectFactory.createShowState();
        showState.setSubsystems("folder lock");
        showState.setWhat("folders");
        if (!recursive) {
            showState.setWhat("no_subfolders " + showState.getWhat());
        }
        if (folder != null) {
            showState.setPath(("/" + folder).replaceAll("//+", "/"));
        }
        return showState.toXMLString();
    }
}