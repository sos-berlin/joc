package com.sos.joc.joc.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joc.resource.IPropertiesResource;
import com.sos.joc.model.Properties;
import com.sos.joc.model.ShowViewProperties;

@javax.ws.rs.Path("")
public class PropertiesImpl extends JOCResourceImpl implements IPropertiesResource {

    private static final String API_CALL = "./properties";
    private static Logger LOGGER = LoggerFactory.getLogger(PropertiesImpl.class);
    private List<String> showViews = new ArrayList<String>();
    private List<String> hideViews = new ArrayList<String>();

    @Override
    public JOCDefaultResponse postProperties(String xAccessToken, String accessToken) throws Exception {
        return postProperties(getAccessToken(xAccessToken, accessToken));
    }

    public JOCDefaultResponse postProperties(String accessToken) throws Exception {

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, null, accessToken, "", true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            Properties entity = new Properties();
            entity.setForceCommentsForAuditLog(Globals.auditLogCommentsAreRequired);
            entity.setComments(readCommentsFromJocProperties());
            ShowViewProperties showViewProps = new ShowViewProperties();
            showViewProps.setAuditLog(readShowViewFromJocProperties("auditlog"));
            showViewProps.setDailyPlan(readShowViewFromJocProperties("dailyplan"));
            showViewProps.setDashboard(readShowViewFromJocProperties("dashboard"));
            showViewProps.setFileTransfers(readShowViewFromJocProperties("filetransfers"));
            showViewProps.setHistory(readShowViewFromJocProperties("history"));
            showViewProps.setJobChains(readShowViewFromJocProperties("jobchains"));
            showViewProps.setJobs(readShowViewFromJocProperties("jobs"));
            showViewProps.setOrders(readShowViewFromJocProperties("orders"));
            showViewProps.setResources(readShowViewFromJocProperties("resources"));
            showViewProps.setConditions(readShowViewFromJocProperties("conditions"));
            entity.setShowViews(showViewProps);
            entity.setDeliveryDate(Date.from(Instant.now()));
            
            logShowViewSettings();

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private List<String> readCommentsFromJocProperties() {
        List<String> commentsList = new ArrayList<String>();
        if (Globals.sosShiroProperties != null) {
            String[] comments = Globals.sosShiroProperties.getProperty("comments", "").split(";");
            for (int i = 0; i < comments.length; i++) {
                commentsList.add(comments[i].trim());
            }
        }
        return commentsList;
    }

    private Boolean readShowViewFromJocProperties(String key) {
        String showViewItem = Globals.sosShiroProperties.getProperty("show_view_" + key);
        if (showViewItem != null) {
            if ("true".equals(showViewItem.toLowerCase())) {
                showViews.add(key);
                return true;
            } else if ("false".equals(showViewItem.toLowerCase())) {
                hideViews.add(key);
                return false;
            }
        }
        return null;
    }
    
    private void logShowViewSettings() {
        StringBuilder msg = new StringBuilder();
        msg.append("Views ");
        if (!hideViews.isEmpty()) {
            msg.append(hideViews.toString()).append(" are hidden");
        }
        if (!hideViews.isEmpty() && !showViews.isEmpty()) {
            msg.append(" and ");
        }
        if (!showViews.isEmpty()) {
            msg.append(showViews.toString()).append(" are shown");
        }
        if (!hideViews.isEmpty() || !showViews.isEmpty()) {
            msg.append(" because of ./joc.properties settings");
            LOGGER.info(msg.toString());
        }
    }

}
