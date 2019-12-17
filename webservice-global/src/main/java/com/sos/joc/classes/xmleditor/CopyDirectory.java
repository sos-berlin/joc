package com.sos.joc.classes.xmleditor;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CopyDirectory extends SimpleFileVisitor<Path> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CopyDirectory.class);

    private Path source;
    private Path target;

    public CopyDirectory(Path sourceDir, Path targetDir) {
        source = sourceDir;
        target = targetDir;
    }

    public void copy() throws Exception {
        Files.walkFileTree(source, this);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {

        try {
            Path targetFile = target.resolve(source.relativize(file));
            Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (Throwable ex) {
            LOGGER.error(ex.toString(), ex);
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) {
        try {
            Path targetDir = target.resolve(source.relativize(dir));
            Files.createDirectories(targetDir);
        } catch (Throwable ex) {
            LOGGER.error(ex.toString(), ex);
        }

        return FileVisitResult.CONTINUE;
    }
}
