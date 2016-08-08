package com.sos.joc.response;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.Error420Schema;
import com.sos.joc.model.jobscheduler.Jobscheduler200PSchema;

public class JobSchedulerPResponse extends com.sos.joc.support.ResponseWrapper {
        
        private JobSchedulerPResponse(Response delegate) {
            super(delegate);
        }
        
        public static JobSchedulerPResponse responseStatus200(Jobscheduler200PSchema entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new JobSchedulerPResponse(responseBuilder.build());
        }
        
        public static JobSchedulerPResponse responseStatus420(Error420Schema entity) {
            Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new JobSchedulerPResponse(responseBuilder.build());
        }

        public static JobSchedulerPResponse responseStatus401(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new JobSchedulerPResponse(responseBuilder.build());
        }           

        public static JobSchedulerPResponse responseStatus403(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(403).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new JobSchedulerPResponse(responseBuilder.build());
        }           

        public static JobSchedulerPResponse responseStatus440(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(440).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new JobSchedulerPResponse(responseBuilder.build());
        }           
    }   
 