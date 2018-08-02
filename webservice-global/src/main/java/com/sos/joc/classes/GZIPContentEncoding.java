package com.sos.joc.classes;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import com.sos.joc.annotation.CompressedAlready;
import com.sos.joc.model.common.Err420;

@Provider
@CompressedAlready
public class GZIPContentEncoding implements WriterInterceptor {
    
    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {

        if (context.getEntity() instanceof Err420) {
            //
        } else {
            MultivaluedMap<String, Object> headers = context.getHeaders();
            headers.add(HttpHeaders.CONTENT_ENCODING, "gzip");
        }   
        context.proceed();
    }

}
