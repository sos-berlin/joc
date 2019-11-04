package com.sos.joc.classes.yade;

public class JocYade {

    public static final String APPLICATION_PATH = "yade";

    public static String getResourceImplPath(final String path) {
        return String.format("./%s/%s", APPLICATION_PATH, path);
    }
}
