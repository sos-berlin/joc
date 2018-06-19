package com.sos.joc.annotation;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import javax.ws.rs.NameBinding;

/**
 * @Compress annotation is the name binding annotation
 *     for WriterInterceptor which set Content-Encoding=gzip
 *     and compress the content always
 * @see com.sos.joc.classes.GZIPContentEncodingCompress
 * @author oh
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Compress {

}
