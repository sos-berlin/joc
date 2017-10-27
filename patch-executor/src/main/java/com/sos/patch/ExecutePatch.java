package com.sos.patch;

import java.io.File;


public class ExecutePatch {

    public static void main(String[] args) throws Exception {
        File newFile = new File(".");
        System.out.println(newFile.getCanonicalPath());
        System.out.println(System.getProperty("user.dir"));
    }
}
