package com.sos.joc.joc.impl;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocError;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.joc.resource.IAboutResource;
import com.sos.joc.model.Version;

@javax.ws.rs.Path("")
public class AboutImpl extends JOCResourceImpl implements IAboutResource {
    
    @Override
    public JOCDefaultResponse postVersion(String accept) {
        return getAbout(accept, "./version");
    }

    @Override
    public JOCDefaultResponse getVersion(String accept) {
        return getAbout(accept, "./version");
    }

    @Override
    public JOCDefaultResponse postAbout(String accept) {
        return getAbout(accept, "./about");
    }

    @Override
    public JOCDefaultResponse getAbout(String accept) {
        return getAbout(accept, "./about");
    }

    public JOCDefaultResponse getAbout(String accept, String apiCall) {
        String mediaType = MediaType.TEXT_PLAIN;
        if (MediaType.APPLICATION_JSON.equalsIgnoreCase(accept)) {
            mediaType = MediaType.APPLICATION_JSON;
        }
        try {
            initLogging(apiCall, null);
            return JOCDefaultResponse.responseStatus200(readVersion(mediaType), mediaType);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e, mediaType);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError(), mediaType);
        }
    }

    private Object readVersion(String mediaType) throws JocException {
        InputStream stream = null;
        String versionFile = "/version.json";
        try {
            stream = this.getClass().getClassLoader().getResourceAsStream(versionFile);
            if (stream != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                Version v = objectMapper.readValue(stream, Version.class);
                return MediaType.TEXT_PLAIN.equals(mediaType) ? versionClassToString(v) : v;
            } else {
                throw new JocException(new JocError("JOC-002", String.format("Version file %1$s not found in classpath", versionFile)));
            }
        } catch (JocException e) {
            throw e;
        } catch (Exception e) {
            throw new JocException(new JocError("JOC-002", String.format("Error while reading %1$s from classpath: ", versionFile)), e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception e) {
            }
        }
    }
    
    private String versionClassToString(Version v) {
        return String.format("version: %s%ngitHash: %s%ndate: %s%n", v.getVersion(), v.getGitHash(), v.getDate());
    }

}
