package com.sos.patch;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;


public class ExecutePatch {
    
    private static final String JOC_WAR_FILE_NAME = "joc.war";
    private static final String PATCH_FILE_NAME = "joc-patch.zip";
    private static final String ARCHIVE_DIR_NAME = "archive";
    private static CopyOption[] COPYOPTIONS = new StandardCopyOption[] { 
        StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING };

    public static void main(String[] args) throws Exception {
        Path workDir = Paths.get(System.getProperty("user.dir"));
        Path executable = workDir.resolve(System.getProperty("java.class.path"));
        System.out.println("executable = " + executable);
        Path patchDir = executable.getParent().getParent();
        System.out.println("patchDir = " + patchDir);
        Path archivePath = patchDir.getParent().resolve(ARCHIVE_DIR_NAME);
        System.out.println("archivePath = " + archivePath);
        Path patchFilePath = patchDir.resolve(PATCH_FILE_NAME);
        Path webAppDir = patchDir.getParent().resolve("webapps");
        System.out.println("webAppDir = " + webAppDir);
        Path webAppJocWarPath = webAppDir.resolve(JOC_WAR_FILE_NAME);
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
        System.out.println("tempDir = " + tempDir);
        // copy joc.war to tempDir for further working on
        Path copiedPath = Files.copy(webAppJocWarPath, tempDir.resolve(JOC_WAR_FILE_NAME), COPYOPTIONS);
        // archive only once if the original file not already exists in archive directory
        if (Files.exists(archivePath.resolve(JOC_WAR_FILE_NAME))) {
            System.out.println("joc.war not archived, because an archive file already exists!");
        } else {
            Path copiedArchivePath = Files.copy(webAppJocWarPath, archivePath.resolve(JOC_WAR_FILE_NAME), COPYOPTIONS); 
            System.out.println("copiedArchivePath = " + copiedArchivePath);
        }
        
        // Source
        FileSystem sourceFileSystem = FileSystems.newFileSystem(patchFilePath, null);
        // Target
        FileSystem targetFileSystem = FileSystems.newFileSystem(copiedPath, null);
        Files.walkFileTree(sourceFileSystem.getPath("/"), new FileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = targetFileSystem.getPath("/");
                try {
                    Path p = Files.copy(dir, targetDir.resolve(dir), COPYOPTIONS);
                    System.out.println("creating directory tree '" + p.toString() + "'");
                } catch (FileAlreadyExistsException | DirectoryNotEmptyException e) {
                } catch (IOException e) {
                    System.out.println("error at creating directory tree '" + targetDir + "'" + e.getMessage());
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path targetFile = targetFileSystem.getPath("/").resolve(file);
                Path p = Files.copy(file, targetFile, COPYOPTIONS);
                System.out.println(String.format("Copy file '%1$s -- %2$s' -> '%3$s -- %4$s'", 
                        PATCH_FILE_NAME, file.toString(), JOC_WAR_FILE_NAME, p.toString()));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
        sourceFileSystem.close();
        targetFileSystem.close();
    }
}
