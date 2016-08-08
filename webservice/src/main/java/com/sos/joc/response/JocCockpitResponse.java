package com.sos.joc.response;

import java.util.Date;
import javax.ws.rs.core.Response;

import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.classes.JobSchedulerUser;
import com.sos.joc.model.common.Error420Schema;
import com.sos.joc.model.common.ErrorSchema;
import com.sos.joc.model.common.OkSchema;

public class JocCockpitResponse extends com.sos.joc.support.ResponseWrapper {

    private JocCockpitResponse(Response delegate) {
        super(delegate);
    }

    public static Error420Schema getError420Schema(String message, Date surveyDate) {
        Error420Schema entity = new Error420Schema();
        entity.setDeliveryDate(new Date());
        if (surveyDate != null) {
            entity.setSurveyDate(surveyDate);
        }
        ErrorSchema errorSchema = new ErrorSchema();
        errorSchema.setCode("420");
        errorSchema.setMessage(message);
        entity.setError(errorSchema);

        return entity;
    }

    public static Error420Schema getError420Schema(String message) {
        return getError420Schema(message, null);
    }

    public static SOSShiroCurrentUserAnswer getError401Schema(JobSchedulerUser sosJobschedulerUser) {
        SOSShiroCurrentUserAnswer entity = new SOSShiroCurrentUserAnswer();
        entity.setAccessToken(sosJobschedulerUser.getSosShiroCurrentUser().getAccessToken());
        entity.setHasRole(false);
        entity.setIsPermitted(false);
        entity.setIsAuthenticated(false);
        entity.setUser(sosJobschedulerUser.getSosShiroCurrentUser().getUsername());
        return entity;
    }

    public static SOSShiroCurrentUserAnswer getError401Schema(String accessToken) {
        SOSShiroCurrentUserAnswer entity = new SOSShiroCurrentUserAnswer();
        entity.setAccessToken(accessToken);
        entity.setHasRole(false);
        entity.setIsPermitted(false);
        entity.setIsAuthenticated(false);
        entity.setUser("");
        return entity;
    }

    public static JocCockpitResponse responseStatus200(Date surveyDate) {
        Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", "application/json");
        OkSchema entity = new OkSchema();

        if (surveyDate != null) {
            entity.setSurveyDate(surveyDate);
        }

        entity.setDeliveryDate(new Date());
        entity.setOk(true);
        responseBuilder.entity(entity);
        return new JocCockpitResponse(responseBuilder.build());

    }

    public static JocCockpitResponse responseStatus420(String message, Date surveyDate) {
        Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", "application/json");
        responseBuilder.entity(getError420Schema(message, surveyDate));
        return new JocCockpitResponse(responseBuilder.build());
    }

    public static JocCockpitResponse responseStatus420(String message) {
        return JocCockpitResponse.responseStatus420(message, null);
    }

    public static JocCockpitResponse responseStatus401(String accessToken) {
        Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", "application/json");
        responseBuilder.entity(getError401Schema(accessToken));
        return new JocCockpitResponse(responseBuilder.build());
    }

    public static JocCockpitResponse responseStatus403(JobSchedulerUser sosJobschedulerUser) {
        Response.ResponseBuilder responseBuilder = Response.status(403).header("Content-Type", "application/json");
        responseBuilder.entity(getError401Schema(sosJobschedulerUser));
        return new JocCockpitResponse(responseBuilder.build());
    }

    public static JocCockpitResponse responseStatus440(JobSchedulerUser sosJobschedulerUser) {
        Response.ResponseBuilder responseBuilder = Response.status(440).header("Content-Type", "application/json");
        responseBuilder.entity(getError401Schema(sosJobschedulerUser));
        return new JocCockpitResponse(responseBuilder.build());
    }
}