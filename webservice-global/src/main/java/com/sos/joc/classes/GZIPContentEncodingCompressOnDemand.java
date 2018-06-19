package com.sos.joc.classes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import com.sos.joc.annotation.CompressOnDemand;

@Provider
@CompressOnDemand
public class GZIPContentEncodingCompressOnDemand implements WriterInterceptor {

    private HttpHeaders httpHeaders;

    public GZIPContentEncodingCompressOnDemand(@Context HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {

        if (httpHeaders != null) {
            MultivaluedMap<String, String> requestHeaders = httpHeaders.getRequestHeaders();
            if (requestHeaders != null) {
                List<String> acceptEncoding = requestHeaders.get(HttpHeaders.ACCEPT_ENCODING);
                if (acceptEncoding != null) {
                    for (String s : acceptEncoding) {
                        if (s.contains("gzip")) {
                            MultivaluedMap<String, Object> headers = context.getHeaders();
                            headers.add(HttpHeaders.CONTENT_ENCODING, "gzip");

                            final OutputStream outputStream = context.getOutputStream();
                            context.setOutputStream(new GZIPOutputStream(outputStream));

                            break;
                        }
                    }
                }
            }
        }
        context.proceed();
    }

}
