package com.sos.joc.classes.common;

import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.Globals;

public class DeleteTempFile implements Runnable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteTempFile.class);
    private final Path path;
    private final Long delay;
    
    public DeleteTempFile(Path path) {
        this.path = path;
        this.delay = null;
    }
    
    public DeleteTempFile(Path path, long delay) {
        this.path = path;
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            if (delay == null) {
                Thread.sleep(Globals.timeoutToDeleteTempFiles); 
            } else {
                Thread.sleep(delay);
            }
            Files.deleteIfExists(path);
        } catch (Exception e) {
            LOGGER.error("", e); 
        }
    }

}
