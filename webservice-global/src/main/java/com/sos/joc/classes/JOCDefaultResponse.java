package com.sos.joc.classes;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Error;
import com.sos.joc.model.common.Error420Schema;
import com.sos.joc.model.common.ErrorSchema;
import com.sos.joc.model.common.ErrorsSchema;
import com.sos.joc.model.common.OkSchema;

public class JOCDefaultResponse extends com.sos.joc.classes.ResponseWrapper {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JOCDefaultResponse.class);
    private static final String TIMEOUT = "timeout";
    private static final String ACCESS_TOKEN = "access_token";

    private JOCDefaultResponse(Response delegate) {
        super(delegate);
    }

    public static JOCDefaultResponse responseStatus200(Object entity) {
        Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON);
        responseBuilder.entity(entity);
        return new JOCDefaultResponse(responseBuilder.build());
    }

    public static JOCDefaultResponse responseStatus200WithHeaders(Object entity,String accessToken, long timeout) {
        Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON);
        responseBuilder.entity(entity);
        responseBuilder.header(ACCESS_TOKEN, accessToken);
        responseBuilder.header(TIMEOUT, timeout);
        return new JOCDefaultResponse(responseBuilder.build());
    }

    public static JOCDefaultResponse responseStatusJSOk(Date surveyDate) {
        Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON);
        OkSchema entity = new OkSchema();

        if (surveyDate != null) {
            entity.setSurveyDate(surveyDate);
        }

        entity.setDeliveryDate(new Date());
        entity.setOk(true);
        responseBuilder.entity(entity);
        return new JOCDefaultResponse(responseBuilder.build());

    }

    public static JOCDefaultResponse responseStatusJSError(String message) {
        Error420Schema entity = new Error420Schema();
        entity.setDeliveryDate(new Date());
         
        ErrorSchema errorSchema = new ErrorSchema();
        errorSchema.setCode("JOC-420");
        errorSchema.setMessage(message);
        entity.setError(errorSchema);

        return responseStatus420(entity);

    }
    
    public static JOCDefaultResponse responseStatusJSError(JocException e) {
        String errorMsg = ((e.getCause() != null) ? e.getCause().toString() : e.getClass().getSimpleName()) + ": " + e.getError().getMessage();
        LOGGER.error(errorMsg, e);
        
        if (WebserviceConstants.NO_USER_WITH_ACCESS_TOKEN.equals(e.getError().getCode())){
            SOSShiroCurrentUserAnswer sosShiroCurrentUserAnswer = new SOSShiroCurrentUserAnswer();
            sosShiroCurrentUserAnswer.setHasRole(false); 
            sosShiroCurrentUserAnswer.setIsAuthenticated(false);
            sosShiroCurrentUserAnswer.setIsPermitted(false);
            sosShiroCurrentUserAnswer.setMessage(errorMsg);
            return responseStatus440(sosShiroCurrentUserAnswer);
        }else{
            
            Error420Schema entity = new Error420Schema();
            entity.setDeliveryDate(new Date());
             
            ErrorSchema errorSchema = new ErrorSchema();
            errorSchema.setCode(e.getError().getCode());
            errorSchema.setMessage(errorMsg);
            entity.setError(errorSchema);
            return responseStatus420(entity);
        }

    }
    
    public static JOCDefaultResponse responseStatusJSError(JocError e) {
        Error420Schema entity = new Error420Schema();
        entity.setDeliveryDate(new Date());
         
        ErrorSchema errorSchema = new ErrorSchema();
        errorSchema.setCode(e.getCode());
        String errorMsg = e.getMessage();
        LOGGER.error(errorMsg, e);
        errorSchema.setMessage(errorMsg);
        entity.setError(errorSchema);

        return responseStatus420(entity);
    }    
    public static JOCDefaultResponse responseStatusJSError(Exception e) {
        if (e instanceof JocException) {
            return responseStatusJSError((JocException) e);
        }
        Error420Schema entity = new Error420Schema();
        entity.setDeliveryDate(new Date());
        ErrorSchema errorSchema = new ErrorSchema();
        errorSchema.setCode("JOC-420");
        String errorMsg = ((e.getCause() != null) ? e.getCause().toString() : e.getClass().getSimpleName()) + ": " + e.getMessage();
        LOGGER.error(errorMsg, e);
        errorSchema.setMessage(errorMsg);
        entity.setError(errorSchema);

        return responseStatus420(entity);
    }

    public static JOCDefaultResponse responseStatus420(Error420Schema entity) {
        Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", MediaType.APPLICATION_JSON);
        responseBuilder.entity(entity);
        return new JOCDefaultResponse(responseBuilder.build());
    }
    
    public static JOCDefaultResponse responseStatus419(List<Error> listOfErrors) {
        ErrorsSchema errorsSchema = new ErrorsSchema();
        errorsSchema.setErrors(listOfErrors);

        Response.ResponseBuilder responseBuilder = Response.status(419).header("Content-Type", MediaType.APPLICATION_JSON);
        responseBuilder.entity(errorsSchema);
        return new JOCDefaultResponse(responseBuilder.build());
    }


    public static JOCDefaultResponse responseStatus401(SOSShiroCurrentUserAnswer entity) {
        Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", MediaType.APPLICATION_JSON);
        responseBuilder.entity(entity);
        return new JOCDefaultResponse(responseBuilder.build());
    }

    public static JOCDefaultResponse responseStatus403(SOSShiroCurrentUserAnswer entity) {
        Response.ResponseBuilder responseBuilder = Response.status(403).header("Content-Type", MediaType.APPLICATION_JSON);
        responseBuilder.entity(entity);
        return new JOCDefaultResponse(responseBuilder.build());
    }

    public static JOCDefaultResponse responseStatus440(SOSShiroCurrentUserAnswer entity) {
        Response.ResponseBuilder responseBuilder = Response.status(440).header("Content-Type", MediaType.APPLICATION_JSON);
        responseBuilder.entity(entity);
        return new JOCDefaultResponse(responseBuilder.build());
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
        entity.setMessage(message);
        return entity;
    }

}
