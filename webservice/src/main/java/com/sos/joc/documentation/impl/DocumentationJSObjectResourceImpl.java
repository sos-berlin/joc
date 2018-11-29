package com.sos.joc.documentation.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.db.documentation.DocumentationDBLayer;
import com.sos.joc.documentation.resource.IDocumentationJSObjectResource;
import com.sos.joc.exceptions.DBConnectionRefusedException;
import com.sos.joc.exceptions.DBInvalidDataException;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.exceptions.JocConfigurationException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.docu.DocumentationShowFilter;


public class DocumentationJSObjectResourceImpl extends JOCResourceImpl implements IDocumentationJSObjectResource {

    private static final String API_CALL = "/documentation/";

    @Override
    public JOCDefaultResponse orderDocu(String xAccessToken, String accessToken, String jobschedulerId, String jobChain, String orderId) throws Exception {
        return showDocu(getAccessToken(xAccessToken, accessToken), jobschedulerId, jobChain + "," + orderId, JobSchedulerObjectType.ORDER, "order");
    }
    
    @Override
    public JOCDefaultResponse jobDocu(String xAccessToken, String accessToken, String jobschedulerId, String path) throws Exception {
        return showDocu(getAccessToken(xAccessToken, accessToken), jobschedulerId, path, JobSchedulerObjectType.JOB, "job");
    }

    @Override
    public JOCDefaultResponse jobChainDocu(String xAccessToken, String accessToken, String jobschedulerId, String path) throws Exception {
        return showDocu(getAccessToken(xAccessToken, accessToken), jobschedulerId, path, JobSchedulerObjectType.JOBCHAIN, "job_chain");
    }

    @Override
    public JOCDefaultResponse scheduleDocu(String xAccessToken, String accessToken, String jobschedulerId, String path) throws Exception {
        return showDocu(getAccessToken(xAccessToken, accessToken), jobschedulerId, path, JobSchedulerObjectType.SCHEDULE, "schedule");
    }

    @Override
    public JOCDefaultResponse lockDocu(String xAccessToken, String accessToken, String jobschedulerId, String path) throws Exception {
        return showDocu(getAccessToken(xAccessToken, accessToken), jobschedulerId, path, JobSchedulerObjectType.LOCK, "lock");
    }

    @Override
    public JOCDefaultResponse processClassDocu(String xAccessToken, String accessToken, String jobschedulerId, String path) throws Exception {
        return showDocu(getAccessToken(xAccessToken, accessToken), jobschedulerId, path, JobSchedulerObjectType.PROCESSCLASS, "process_class");
    }

    @Override
    public JOCDefaultResponse calendarDocu(String xAccessToken, String accessToken, String jobschedulerId, String path) throws Exception {
        return showDocu(getAccessToken(xAccessToken, accessToken), jobschedulerId, path, JobSchedulerObjectType.WORKINGDAYSCALENDAR, "calendar");
    }
    
    public JOCDefaultResponse showDocu(String xAccessToken, String jobschedulerId, String path, JobSchedulerObjectType type, String apiCall) throws Exception {
        DocumentationShowFilter documentationFilter = new DocumentationShowFilter();
        documentationFilter.setJobschedulerId(jobschedulerId);
        documentationFilter.setPath(path);
        documentationFilter.setType(type);
        return show(xAccessToken, documentationFilter, apiCall);
    }
    
    public JOCDefaultResponse show(String xAccessToken, DocumentationShowFilter documentationFilter, String apiCall) throws Exception {
        try {
            // TODO Permission
            JOCDefaultResponse jocDefaultResponse = init(API_CALL+apiCall, documentationFilter, xAccessToken, documentationFilter.getJobschedulerId(),
                    true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            String entity = String.format(
                    "<!DOCTYPE html>%n<html>\n<head>%n  <meta http-equiv=\"refresh\" content=\"0;URL='../../documentation/%s'\" />%n</head>%n<body>%n</body>%n</html>",
                    getUrl(apiCall, xAccessToken, documentationFilter));

            return JOCDefaultResponse.responseHtmlStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
    
    private String getUrl(String apiCall, String xAccessToken, DocumentationShowFilter documentationFilter)
            throws JocMissingRequiredParameterException, JocConfigurationException, DBConnectionRefusedException, DBInvalidDataException,
            DBMissingDataException, UnsupportedEncodingException {
        SOSHibernateSession connection = null;
        try {
            checkRequiredParameter("jobschedulerId", documentationFilter.getJobschedulerId());
            checkRequiredParameter("path", documentationFilter.getPath());

            documentationFilter.setPath(normalizePath(documentationFilter.getPath()));
            connection = Globals.createSosHibernateStatelessConnection(API_CALL+apiCall);
            DocumentationDBLayer dbLayer = new DocumentationDBLayer(connection);
            String path = dbLayer.getDocumentationPath(documentationFilter);
            if (path == null && "calendar".equals(apiCall)) {
                documentationFilter.setType(JobSchedulerObjectType.NONWORKINGDAYSCALENDAR);
                path = dbLayer.getDocumentationPath(documentationFilter); 
            }
            if (path == null) {
                throw new DBMissingDataException("The documentation couldn't determine");
            }

            return documentationFilter.getJobschedulerId() + "/" + xAccessToken + urlEncodedPath(path);
        } finally {
            Globals.disconnect(connection);
        }
    }
    
    private String urlEncodedPath(String path) throws UnsupportedEncodingException {
        if ("/".equals(path)) {
            return "/";
        }
        String[] pathParts = path.split("/");
        StringBuilder s = new StringBuilder();
        for (int i = 1; i < pathParts.length; i++) {
            s.append("/").append(URLEncoder.encode(pathParts[i], "UTF-8"));
        }
        return s.toString();
    }



    

}
