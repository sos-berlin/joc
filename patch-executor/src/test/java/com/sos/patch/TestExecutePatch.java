package com.sos.patch;

import static java.nio.file.StandardCopyOption.*;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;

import org.junit.Test;


public class TestExecutePatch {

    private static final String JOC_WAR_FILE_NAME = "joc.war";
    private static final String PATCH_FILE_NAME = "joc-patch.zip";
    private static final String PATCHES_DIR= "patchesDir";
    private static final String ARCHIVES_DIR= "archivesDir";
    private static final String WEBAPP_DIR= "webappDir";
    private static final String TEMP_DIR= "tempDir";
    private static CopyOption[] COPYOPTIONS = new StandardCopyOption[] { 
        StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING };

    @Test
    public void testPaths(){
        Path warFilePath = Paths.get("C:/sp/joc-1.11.x-SNAPSHOT/base/webapps/joc.war");
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
        System.out.println("warFilePath = " + warFilePath);
        System.out.println("tempDir = " + tempDir);
        try {
            Path copiedPath = Files.copy(warFilePath, tempDir.resolve("joc.war"), REPLACE_EXISTING);
            System.out.println("copiedPath = " + copiedPath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testPathsFromZip(){
        Path warFilePath = Paths.get("C:/sp/joc-1.11.x-SNAPSHOT/base/webapps/joc.war");
        Path patchFilePath = Paths.get("C:/sp/joc-1.11.x-SNAPSHOT/base/patches/joc-patch.zip");
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
        System.out.println("warFilePath = " + warFilePath);
        System.out.println("tempDir = " + tempDir);
        try {
            Path copiedPath = Files.copy(warFilePath, tempDir.resolve("joc.war"), REPLACE_EXISTING);
            System.out.println("copiedPath = " + copiedPath);
            FileSystem sourceFileSystem = FileSystems.newFileSystem(patchFilePath, null);
            FileSystem targetFileSystem = FileSystems.newFileSystem(copiedPath, null);
            EnumSet<FileVisitOption> options = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
            Files.walkFileTree(sourceFileSystem.getPath("/"), options, Integer.MAX_VALUE, new FileVisitor<Path>() {

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    System.out.println("directoryPath = " + dir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println("filePath = " + file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                private void print(Path file) throws IOException{
                    final DateFormat df = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
                    final String modTime= df.format(new Date(Files.getLastModifiedTime(file).toMillis()));
                    System.out.printf("%d  %s  %s\n", Files.size(file), modTime, file);
                }
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void copyZipFileSystem() {
        Path warFilePath = Paths.get("C:/sp/joc-1.11.x-SNAPSHOT/base/webapps/joc.war");
        Path patchFilePath = Paths.get("C:/sp/joc-1.11.x-SNAPSHOT/base/patches/joc-patch.zip");
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
        System.out.println("warFilePath = " + warFilePath);
        System.out.println("tempDir = " + tempDir);
        try {
            Path copiedPath = Files.copy(warFilePath, tempDir.resolve("joc.war"), REPLACE_EXISTING);
            System.out.println("copiedPath = " + copiedPath);
            FileSystem sourceFileSystem = FileSystems.newFileSystem(patchFilePath, null);
            FileSystem targetFileSystem = FileSystems.newFileSystem(copiedPath, null);
            EnumSet<FileVisitOption> options = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
            Files.walkFileTree(sourceFileSystem.getPath("/"), options, Integer.MAX_VALUE, new FileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path target = null;
                    try {
                        target = targetFileSystem.getPath("/").resolve(file);
                    } catch (Exception e) {
                        return FileVisitResult.CONTINUE;
                    }
                    System.out.println(String.format("Copy file '%s' -> '%s'", file.toString(), target.toString()));
                    Path p = Files.copy(file, target, COPYOPTIONS);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    @Test
    public void testWalkFileTreeExceptions() throws IOException {
        EnumSet<FileVisitOption> options = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
        Files.walkFileTree(Paths.get("C:/Users/Santiago/AppData/Local/Temp"), options, Integer.MAX_VALUE, new FileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                System.out.println("Failed to visit file: " + file);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
