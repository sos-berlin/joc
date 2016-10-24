package com.sos.joc.classes;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Err419;
import com.sos.joc.model.common.Err420;
import com.sos.joc.model.common.Errs;
import com.sos.joc.model.common.Ok;

public class JOCDefaultResponse extends com.sos.joc.classes.ResponseWrapper {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCDefaultResponse.class);
    private static final String TIMEOUT = "X-JOC-Timeout";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String ERROR_HTML = "<!DOCTYPE html>%n"
            + "<html>%n"
            + "<head>%n"
            + "  <title>%1$s</title>%n"
            + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>%n"
            + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>%n"
            + "  <style type=\"text/css\">%n"
            + "      div.frame{margin:16px auto 0;max-width:900px;min-height:100%%;height:auto !important;background-color:#eee;padding:2px 0;border-radius:16px;border:solid 2px #007da6;}%n"
            + "      div.innerframe{margin:0 auto;max-width:748px;text-align:center;font-family:\"Open Sans\",\"lucida grande\",\"Segoe UI\",arial,verdana,\"lucida sans unicode\",tahoma,serif;text-rendering:optimizeLegibility;}%n"
            + "      div.code {color:#eb8814;font-size:18pt;font-weight:bold;text-align:center;}%n"
            + "      div.message {color:#666;font-size:12pt;font-weight:normal;text-align:left;margin:4px 10px;}%n"
            + "  </style>"
            + "</head>%n"
            + "<body>%n"
            + "  <div class=\"frame\">%n"
            + "    <div class=\"innerframe\">%n"
            + "      <div class=\"code\">&#x26a0; %1$s<div>%n"
            + "      <div class=\"message\">%2$s</div>%n"
            + "    </div>%n"
            + "  </div>%n"
            + "</body>%n"
            + "</html>%n";
    
    private JOCDefaultResponse(Response delegate) {
        super(delegate);
    }
    
    public static JOCDefaultResponse responseStatus200(Object entity, String mediaType) {
        Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", mediaType);
        responseBuilder.entity(entity);
        return new JOCDefaultResponse(responseBuilder.build());
    }

    public static JOCDefaultResponse responseStatus200(Object entity) {
        return responseStatus200(entity, MediaType.APPLICATION_JSON);
    }
    
    public static JOCDefaultResponse responseHtmlStatus200(Object entity) {
        return responseStatus200(entity, MediaType.TEXT_HTML + "; charset=UTF-8");
    }

    public static JOCDefaultResponse responseStatus200WithHeaders(Object entity, String accessToken, long timeout) {
        Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON);
        responseBuilder.entity(entity);
        responseBuilder.header(ACCESS_TOKEN, accessToken);
        responseBuilder.header(TIMEOUT, timeout);
        return new JOCDefaultResponse(responseBuilder.build());
    }

    public static JOCDefaultResponse responseStatusJSOk(Date surveyDate) {
        Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON);
        Ok entity = new Ok();
        if (surveyDate != null) {
            entity.setSurveyDate(surveyDate);
        }
        entity.setDeliveryDate(new Date());
        entity.setOk(true);
        responseBuilder.entity(entity);
        return new JOCDefaultResponse(responseBuilder.build());
    }

    public static JOCDefaultResponse responseStatusJSError(String message) {
        Err420 entity = new Err420();
        entity.setError(new JocError(message));
        entity.setSurveyDate(new Date());
        entity.setDeliveryDate(new Date());
        LOGGER.error(message);
        return responseStatus420(entity);
    }
    
    public static JOCDefaultResponse responseStatusJSError(JocException e, String mediaType) {
        if (!"".equals(e.getError().printMetaInfo())) {
            LOGGER.info(e.getError().printMetaInfo());
        }
        
        String errorMsg = ((e.getCause() != null) ? e.getCause().toString() : e.getClass().getSimpleName()) + ": " + e.getError().getMessage();
        e.getError().setMessage(errorMsg);
        Err420 entity = new Err420();
        entity.setError(e.getError());
        entity.setSurveyDate(new Date());
        entity.setDeliveryDate(new Date());
        
        if (WebserviceConstants.NO_USER_WITH_ACCESS_TOKEN.equals(e.getError().getCode())){
            SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = new SOSShiroCurrentUserAnswer();
            sosShiroCurrentUserAnswer.setHasRole(false); 
            sosShiroCurrentUserAnswer.setIsAuthenticated(false);
            sosShiroCurrentUserAnswer.setIsPermitted(false);
            sosShiroCurrentUserAnswer.setMessage(entity.getError().getMessage());
            return responseStatus440(sosShiroCurrentUserAnswer, mediaType);
        } else {
            LOGGER.error(errorMsg, e);
            return responseStatus420(entity, mediaType);
        }
    }
    
    public static JOCDefaultResponse responseStatusJSError(JocException e) {
        return responseStatusJSError(e, MediaType.APPLICATION_JSON);
    }
    
    public static JOCDefaultResponse responseHTMLStatusJSError(JocException e) {
        return responseStatusJSError(e, MediaType.TEXT_HTML + "; charset=UTF-8");
    }
    
    public static JOCDefaultResponse responseStatusJSError(JocError e) {
        if (!"".equals(e.printMetaInfo())) {
            LOGGER.info(e.printMetaInfo());
        }
        Err420 entity = new Err420();
        entity.setError(e);
        entity.setSurveyDate(new Date());
        entity.setDeliveryDate(new Date());
        LOGGER.error(e.getMessage());
        return responseStatus420(entity);
    }
    
    public static JOCDefaultResponse responseStatusJSError(Exception e, String mediaType) {
        if (e instanceof JocException) {
            return responseStatusJSError((JocException) e);
        }
        if (e.getCause() != null && e.getCause() instanceof JocException) {
            return responseStatusJSError((JocException) e.getCause());
        }
        Err420 entity = new Err420();
        String errorMsg = ((e.getCause() != null) ? e.getCause().toString() : e.getClass().getSimpleName()) + ": " + e.getMessage();
        entity.setError(new JocError(errorMsg));
        entity.setSurveyDate(new Date());
        entity.setDeliveryDate(new Date());
        LOGGER.error(errorMsg, e);
        return responseStatus420(entity, mediaType);
    }
    
    public static JOCDefaultResponse responseStatusJSError(Exception e) {
        return responseStatusJSError(e, MediaType.APPLICATION_JSON);
    }
    
    public static JOCDefaultResponse responseHTMLStatusJSError(Exception e) {
        return responseStatusJSError(e, MediaType.TEXT_HTML + "; charset=UTF-8");
    }
    
    public static JOCDefaultResponse responseStatusJSError(Exception e, JocError err, String mediaType) {
        if (!"".equals(err.printMetaInfo())) {
            LOGGER.info(err.printMetaInfo());
        }
        if (e instanceof JocException) {
            return responseStatusJSError((JocException) e);
        }
        if (e.getCause() != null && e.getCause() instanceof JocException) {
            return responseStatusJSError((JocException) e.getCause());
        }
        Err420 entity = new Err420();
        String errorMsg = ((e.getCause() != null) ? e.getCause().toString() : e.getClass().getSimpleName()) + ": " + e.getMessage();
        entity.setError(new JocError(errorMsg));
        entity.setSurveyDate(new Date());
        entity.setDeliveryDate(new Date());
        LOGGER.error(errorMsg, e);
        return responseStatus420(entity, mediaType);
    }
    
    public static JOCDefaultResponse responseStatusJSError(Exception e, JocError err) {
        return responseStatusJSError(e, err, MediaType.APPLICATION_JSON);
    }
    
    public static JOCDefaultResponse responseHTMLStatusJSError(Exception e, JocError err) {
        return responseStatusJSError(e, err, MediaType.TEXT_HTML + "; charset=UTF-8");
    }
    
    public static JOCDefaultResponse responseStatus420(Err420 entity, String mediaType) {
        Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", mediaType);
        if (mediaType.contains(MediaType.TEXT_HTML)) {
            String entityStr = String.format(ERROR_HTML, entity.getError().getCode(), StringEscapeUtils.escapeHtml4(entity.getError().getMessage()));
            responseBuilder.entity(entityStr);
        } else {
            responseBuilder.entity(entity);
        }
        return new JOCDefaultResponse(responseBuilder.build());
    }

    public static JOCDefaultResponse responseStatus420(Err420 entity) {
        return responseStatus420(entity, MediaType.APPLICATION_JSON);
    }
    
    public static JOCDefaultResponse responseHTMLStatus420(Err420 entity) {
        return responseStatus420(entity, MediaType.TEXT_HTML + "; charset=UTF-8");
    }
    
    public static JOCDefaultResponse responseHTMLStatus420(String entity) {
        entity = String.format(ERROR_HTML, "JOC-420", StringEscapeUtils.escapeHtml4(entity));
        Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", MediaType.TEXT_HTML + "; charset=UTF-8");
        responseBuilder.entity(entity);
        return new JOCDefaultResponse(responseBuilder.build());
    }
    
    public static JOCDefaultResponse responseStatus419(List<Err419> listOfErrors) {
        Errs errors = new Errs();
        errors.setErrors(listOfErrors);

        Response.ResponseBuilder responseBuilder = Response.status(419).header("Content-Type", MediaType.APPLICATION_JSON);
        responseBuilder.entity(errors);
        return new JOCDefaultResponse(responseBuilder.build());
    }
    
    public static JOCDefaultResponse responseStatus419(List<Err419> listOfErrors, JocError err) {
        if (!"".equals(err.printMetaInfo())) {
            LOGGER.info(err.printMetaInfo());
        }
        Errs errors = new Errs();
        errors.setErrors(listOfErrors);

        Response.ResponseBuilder responseBuilder = Response.status(419).header("Content-Type", MediaType.APPLICATION_JSON);
        responseBuilder.entity(errors);
        return new JOCDefaultResponse(responseBuilder.build());
    }

    public static JOCDefaultResponse responseStatus401(SOSShiroCurrentUserAnswer entity) {
        Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", MediaType.APPLICATION_JSON);
        responseBuilder.entity(entity);
        return new JOCDefaultResponse(responseBuilder.build());
    }

    public static JOCDefaultResponse responseStatus403(SOSShiroCurrentUserAnswer entity, String mediaType) {
        Response.ResponseBuilder responseBuilder = Response.status(403).header("Content-Type", mediaType);
        LOGGER.error(entity.getMessage());
        if (mediaType.contains(MediaType.TEXT_HTML)) {
            String entityStr = String.format(ERROR_HTML, "JOC-403", StringEscapeUtils.escapeHtml4(entity.getMessage()));
            responseBuilder.entity(entityStr);
        } else {
            responseBuilder.entity(entity);
        }
        return new JOCDefaultResponse(responseBuilder.build());
    }
    
    public static JOCDefaultResponse responseStatus403(SOSShiroCurrentUserAnswer entity) {
        return responseStatus403(entity, MediaType.APPLICATION_JSON);
    }

    public static JOCDefaultResponse responseStatus440(SOSShiroCurrentUserAnswer entity, String mediaType) {
        Response.ResponseBuilder responseBuilder = Response.status(440).header("Content-Type", mediaType);
        LOGGER.error(entity.getMessage());
        if (mediaType.contains(MediaType.TEXT_HTML)) {
            String entityStr = String.format(ERROR_HTML, "JOC-440", StringEscapeUtils.escapeHtml4(entity.getMessage()));
            responseBuilder.entity(entityStr);
        } else {
            responseBuilder.entity(entity);
        }
        return new JOCDefaultResponse(responseBuilder.build());
    }

    public static JOCDefaultResponse responseStatus440(SOSShiroCurrentUserAnswer entity) {
        return responseStatus440(entity, MediaType.APPLICATION_JSON);
    }
    
    public static JOCDefaultResponse responseHTMLStatus440(SOSShiroCurrentUserAnswer entity) {
        return responseStatus440(entity, MediaType.TEXT_HTML + "; charset=UTF-8");
    }

    public static SOSShiroCurrentUserAnswer getError401Schema(JobSchedulerUser sosJobschedulerUser,String message) {
        SOSShiroCurrentUserAnswer entity = new SOSShiroCurrentUserAnswer();
        entity.setAccessToken(sosJobschedulerUser.getSosShiroCurrentUser().getAccessToken());
        entity.setHasRole(false);
        entity.setIsPermitted(false);
        entity.setIsAuthenticated(false);
        entity.setUser(sosJobschedulerUser.getSosShiroCurrentUser().getUsername());
        if ("".equals(message)){
            message = "Authentication failure";
        }
        LOGGER.error(message);
        entity.setMessage(message);
        return entity;
    }

}
