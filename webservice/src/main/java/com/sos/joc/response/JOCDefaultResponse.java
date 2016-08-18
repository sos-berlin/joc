package com.sos.joc.response;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.sos.auth.rest.SOSShiroCurrentUserAnswer;
import com.sos.joc.model.common.Error420Schema;

public class JOCDefaultResponse extends com.sos.joc.support.ResponseWrapper {
        
        private JOCDefaultResponse(Response delegate) {
            super(delegate);
        }
        
        public static JOCDefaultResponse responseStatus200(Object entity) {
            Response.ResponseBuilder responseBuilder = Response.status(200).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new JOCDefaultResponse(responseBuilder.build());
        }
        
        public static JOCDefaultResponse responseStatus420(Error420Schema entity) {
            Response.ResponseBuilder responseBuilder = Response.status(420).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new JOCDefaultResponse(responseBuilder.build());
        }

        public static JOCDefaultResponse responseStatus401(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(401).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new JOCDefaultResponse(responseBuilder.build());
        }           

        public static JOCDefaultResponse responseStatus403(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(403).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new JOCDefaultResponse(responseBuilder.build());
        }           

        public static JOCDefaultResponse responseStatus440(SOSShiroCurrentUserAnswer entity) {
            Response.ResponseBuilder responseBuilder = Response.status(440).header("Content-Type", MediaType.APPLICATION_JSON );
            responseBuilder.entity(entity);
            return new JOCDefaultResponse(responseBuilder.build());
        }           
    
}
