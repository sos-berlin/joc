package com.sos.joc.annotation;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import javax.ws.rs.NameBinding;

/**
 * @CompressedAlready annotation is the name binding annotation
 *     for WriterInterceptor which set Content-Encoding=gzip
 *     and content is already compressed
 * @see com.sos.joc.classes.GZIPContentEncoding
 * @author oh
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface CompressedAlready {

}
