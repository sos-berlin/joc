package com.sos.patch;

import java.io.File;
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
    private static final String ARCHIVE_DIR_NAME = "archive";
    private static CopyOption[] COPYOPTIONS = new StandardCopyOption[] { 
        StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING };

    public static void main(String[] args) throws Exception {
        Path workDir = Paths.get(System.getProperty("user.dir"));
        Path executable = workDir.resolve(System.getProperty("java.class.path"));
        System.out.println("Path of the executable = " + executable);
        Path patchDir = executable.getParent().getParent();
        System.out.println("Path of the patches directory = " + patchDir);
        Path archivePath = patchDir.getParent().resolve(ARCHIVE_DIR_NAME);
        System.out.println("Path of the archives directory = " + archivePath);
        Path webAppDir = patchDir.getParent().resolve("webapps");
        System.out.println("Path of the webapp directory = " + webAppDir);
        Path webAppJocWarPath = webAppDir.resolve(JOC_WAR_FILE_NAME);
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
        System.out.println("Path of the temp directory = " + tempDir);
        // copy joc.war to tempDir for further working on
        Path copiedPath = Files.copy(webAppJocWarPath, tempDir.resolve(JOC_WAR_FILE_NAME), COPYOPTIONS);
        // archive only once if the original file not already exists in archive directory
        if (Files.exists(archivePath.resolve(JOC_WAR_FILE_NAME))) {
            System.out.println("joc.war not archived, because an archive file already exists!");
        } else {
            Path copiedArchivePath = Files.copy(webAppJocWarPath, archivePath.resolve(JOC_WAR_FILE_NAME), COPYOPTIONS); 
            System.out.println("working file to update the web app = " + copiedArchivePath);
        }

        // Target
        FileSystem targetFileSystem = FileSystems.newFileSystem(copiedPath, null);

        // process a new zip-file-system for each zip-file in patches folder
        Files.walkFileTree(patchDir, new FileVisitor<Path>() {

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.getFileName().toString().endsWith(".zip")) {
                    // Source
                    FileSystem sourceFileSystem = FileSystems.newFileSystem(file, null);
                    processPatchZipFile(sourceFileSystem, targetFileSystem);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

        });
        // close target file system after all patches are processed!
        targetFileSystem.close();

        // After everything from patches folder is processed, copy back from temp directory
        Path copyBack = Files.copy(copiedPath, webAppJocWarPath, COPYOPTIONS);
        if (copyBack != null && !copyBack.toString().isEmpty()) {
            System.out.println(String.format("%1$s was updated successfully!", copyBack.getFileName().toString()));
        }
    }
    
    private static void processPatchZipFile (FileSystem sourceFileSystem, FileSystem targetFileSystem) throws IOException {
        Path sourceRootPath = sourceFileSystem.getPath("/");
        Path targetRootPath = targetFileSystem.getPath("/");
        Files.walkFileTree(sourceFileSystem.getPath("/"), new FileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = targetRootPath.resolve(sourceRootPath.relativize(dir));
                try {
                    Path p = Files.copy(dir, targetDir.resolve(dir), COPYOPTIONS);
                    System.out.println("processing directory '" + p.toString() + "'");
                } catch (FileAlreadyExistsException | DirectoryNotEmptyException e) {
                } catch (IOException e) {
                    System.out.println("error processing directory tree '" + targetDir + "'" + e.getMessage());
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
                Path targetFile = targetRootPath.resolve(sourceRootPath.relativize(file));
                boolean alreadyExists = false;
                if (Files.exists(targetFile)) {
                    alreadyExists = true;
                }
                Path p = Files.copy(file, targetFile, COPYOPTIONS);
                if (alreadyExists) {
                    System.out.println(String.format("file '%1$s updated in %2$s'", p.toString(), JOC_WAR_FILE_NAME));
                } else {
                    System.out.println(String.format("file '%1$s added to %2$s'", p.toString(), JOC_WAR_FILE_NAME));
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
        sourceFileSystem.close();
    }

}
