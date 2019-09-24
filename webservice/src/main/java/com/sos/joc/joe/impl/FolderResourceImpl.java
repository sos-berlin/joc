package com.sos.joc.joe.impl;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonString;
import javax.ws.rs.Path;

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCHotFolder;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joe.resource.IFolderResource;
import com.sos.joc.model.joe.common.JSObjectEdit;
import com.sos.joc.model.joe.other.Folder;

@Path("joe/read")
public class FolderResourceImpl extends JOCResourceImpl implements IFolderResource {

    private static final String API_CALL = "./joe/read/folder";

    @Override
    public JOCDefaultResponse read(final String accessToken, final JSObjectEdit body) {
        try {
            // permission??
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, body, accessToken, body.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("path", body.getPath());
            String path = normalizeFolder(body.getPath() + "/");
            JOCHotFolder httpClient = new JOCHotFolder(this);
            JsonArray folder = httpClient.getFolder(path);
            
            List<String> folders = new ArrayList<String>();
            List<String> jobs = new ArrayList<String>();
            List<String> jobChains = new ArrayList<String>();
            List<String> orders = new ArrayList<String>();
            List<String> processClasses = new ArrayList<String>();
            List<String> schedules = new ArrayList<String>();
            List<String> locks = new ArrayList<String>();
            List<String> monitors = new ArrayList<String>();
            List<String> nodeParams = new ArrayList<String>();
            List<String> others = new ArrayList<String>();
            
            for (JsonString jsonStr : folder.getValuesAs(JsonString.class)) {
                String s = jsonStr.getString();
                if (s.startsWith(".")) {
                    continue;
                }
                if (s.endsWith("/")) {
                    folders.add(s);
                } else if (s.endsWith(".job.xml")) {
                    jobs.add(s);
                } else if (s.endsWith(".job_chain.xml")) {
                    jobChains.add(s);
                } else if (s.endsWith(".order.xml")) {
                    orders.add(s);
                } else if (s.endsWith(".process_class.xml")) {
                    processClasses.add(s);
                } else if (s.endsWith(".schedule.xml")) {
                    schedules.add(s);
                } else if (s.endsWith(".lock.xml")) {
                    locks.add(s);
                } else if (s.endsWith(".config.xml")) { //maybe test if job chain exist
                    nodeParams.add(s);
                } else {
                    others.add(s);
                }
            }
            Folder entity = new Folder();
            if (!folders.isEmpty()) {
                entity.setFolders(folders);
            }
            if (!jobs.isEmpty()) {
                entity.setJobs(jobs);
            }
            if (!jobChains.isEmpty()) {
                entity.setJobChains(jobChains);
            }
            if (!orders.isEmpty()) {
                entity.setOrders(orders);
            }
            if (!processClasses.isEmpty()) {
                entity.setProcessClasses(processClasses);
            }
            if (!schedules.isEmpty()) {
                entity.setSchedules(schedules);
            }
            if (!locks.isEmpty()) {
                entity.setLocks(locks);
            }
            if (!monitors.isEmpty()) {
                entity.setMonitors(monitors);
            }
            if (!nodeParams.isEmpty()) {
                entity.setNodeParams(nodeParams);
            }
            if (!others.isEmpty()) {
                entity.setOthers(others);
            }
            entity.setDeliveryDate(Date.from(Instant.now()));
            entity.setPath(path);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseHTMLStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseHTMLStatusJSError(e, getJocError());
        }
    }

}
