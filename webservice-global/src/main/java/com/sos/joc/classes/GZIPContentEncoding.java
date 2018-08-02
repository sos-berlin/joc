package com.sos.joc.classes;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import com.sos.joc.annotation.CompressedAlready;

@Provider
@CompressedAlready
public class GZIPContentEncoding implements WriterInterceptor {
    
    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        
        MultivaluedMap<String, Object> headers = context.getHeaders();
        if (headers.containsKey("X-Uncompressed-Length")) {
            headers.add(HttpHeaders.CONTENT_ENCODING, "gzip");
        }  
        context.proceed();
    }

}
