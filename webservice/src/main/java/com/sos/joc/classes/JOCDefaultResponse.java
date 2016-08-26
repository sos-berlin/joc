package com.sos.joc.classes;

import java.util.Date;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.Error420Schema;
import com.sos.joc.model.common.ErrorSchema;
import com.sos.joc.model.common.OkSchema;

public class JOCDefaultResponse extends com.sos.joc.support.ResponseWrapper {

    private JOCDefaultResponse(Response delegate) {
        super(delegate);
    }

    public static JOCDefaultResponse responseStatus200(Object entity) {
        Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON);
        responseBuilder.entity(entity);
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
        Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", MediaType.APPLICATION_JSON);
        Error420Schema entity = new Error420Schema();
        entity.setDeliveryDate(new Date());
         
        ErrorSchema errorSchema = new ErrorSchema();
        errorSchema.setCode("420");
        errorSchema.setMessage(message);
        entity.setError(errorSchema);

        responseBuilder.entity(entity);
        return new JOCDefaultResponse(responseBuilder.build());

    }

    public static JOCDefaultResponse responseStatus420(Error420Schema entity) {
        Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", MediaType.APPLICATION_JSON);
        responseBuilder.entity(entity);
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
