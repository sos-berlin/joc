package com.sos.patch;

import java.io.File;
import java.io.FileFilter;
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
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;


public class ExecutePatch {
    
    private static final String JOC_WAR_FILE_NAME = "joc.war";
    private static final String ARCHIVE_DIR_NAME = "archive";
    private static final String PATCHES_DIR= "--patches-dir";
    private static final String ARCHIVES_DIR= "--archives-dir";
    private static final String WEBAPP_DIR= "--webapp-dir";
    private static final String TEMP_DIR= "--temp-dir";
    private static final String ROLLBACK= "--rollback";
    private static CopyOption[] COPYOPTIONS = new StandardCopyOption[] { 
        StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING };

    public static void main(String[] args) throws Exception {
        Path workDir = Paths.get(System.getProperty("user.dir"));
        Path executable = workDir.resolve(System.getProperty("java.class.path"));
        Path patchDir = executable.getParent().getParent();
        Path archivePath = patchDir.getParent().resolve(ARCHIVE_DIR_NAME);
        Path webAppDir = patchDir.getParent().resolve("webapps");
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
        boolean rollback = false;
        if (args != null && args.length == 1) {
            if (args[0].equalsIgnoreCase("-?") || args[0].equalsIgnoreCase("-h") || args[0].equalsIgnoreCase("--help")) {
                printUsage(patchDir);
                System.exit(0);
                return;
            } else if (args[0].startsWith(ROLLBACK)) {
                rollback = true;
                System.out.println("processing rollback...");
            }
        }
        if (args != null && args.length >= 1) {
            for(int i = 0; i < args.length; i++) {
                if (args[i].startsWith(PATCHES_DIR)) {
                    String[] split = args[i].split("=", 2);
                    patchDir = Paths.get(split[1]);
                }
                if (args[i].startsWith(ARCHIVES_DIR)) {
                    String[] split = args[i].split("=", 2);
                    archivePath = Paths.get(split[1]);
                }
                if (args[i].startsWith(WEBAPP_DIR)) {
                    String[] split = args[i].split("=", 2);
                    webAppDir = Paths.get(split[1]);
                }
                if (args[i].startsWith(TEMP_DIR)) {
                    String[] split = args[i].split("=", 2);
                    tempDir = Paths.get(split[1]);
                }
            }
        }
        System.out.println("Path of the patches directory = " + patchDir);
        System.out.println("Path of the archives directory = " + archivePath);
        System.out.println("Path of the webapp directory = " + webAppDir);
        System.out.println("Path of the temp directory = " + tempDir);
        Path webAppJocWarPath = webAppDir.resolve(JOC_WAR_FILE_NAME);
        // copy joc.war to tempDir for further working on
        Path copiedPath = null;
        // archive only once if the original file not already exists in archive directory
        if (Files.exists(archivePath.resolve(JOC_WAR_FILE_NAME))) {
            copiedPath = Files.copy(archivePath.resolve(JOC_WAR_FILE_NAME), tempDir.resolve(JOC_WAR_FILE_NAME), COPYOPTIONS);
            System.out.println("joc.war not archived, because an archive file of the original joc.war already exists!");
        } else {
            Path copiedArchivePath = Files.copy(webAppJocWarPath, archivePath.resolve(JOC_WAR_FILE_NAME), COPYOPTIONS); 
            System.out.println("joc.war was archived before processing the patch(es)!");
            copiedPath = Files.copy(copiedArchivePath, tempDir.resolve(JOC_WAR_FILE_NAME), COPYOPTIONS);
        }

        // Target
        FileSystem targetFileSystem = FileSystems.newFileSystem(copiedPath, null);
        // sort the patches ascending
        File[] files = patchDir.toFile().listFiles(new FileFilter() {
            
            @Override
            public boolean accept(File pathname) {
                return !pathname.isDirectory() && pathname.getAbsolutePath().matches(".*\\d{8}.*\\.zip$");
            }
        });

        Comparator<File> fileNameComparator = new Comparator<File>() {
            
            @Override
            public int compare(File o1, File o2) {
                String timestamp1 = o1.getName().replaceFirst("(\\d{8})", "$1");
                String timestamp2 = o2.getName().replaceFirst("(\\d{8})", "$1");
                return timestamp1.compareTo(timestamp2);
            }
        };

        Set<File> patchFiles = new TreeSet<File>(fileNameComparator);
        
        for(int i = 0; i < files.length; i++) {
            patchFiles.add(files[i]);
        }
        
        // process a new zip-file-system for each zip-file in patches folder
        try {
            if ((patchFiles.isEmpty() && Files.exists(archivePath.resolve(JOC_WAR_FILE_NAME)))
                    || rollback) {
                rollbackPatch(archivePath.resolve(JOC_WAR_FILE_NAME), webAppJocWarPath);
            } else {
                for (File patchFile : patchFiles) {
                    System.out.println(patchFile.getAbsolutePath());
                    FileSystem sourceFileSystem = null;
                    sourceFileSystem = FileSystems.newFileSystem(patchFile.toPath(), null);
                    processPatchZipFile(sourceFileSystem, targetFileSystem);
                }
                // After everything from patches folder is processed, copy back from temp directory
                targetFileSystem.close();
                Path copyBack = Files.copy(tempDir.resolve(JOC_WAR_FILE_NAME), webAppJocWarPath, COPYOPTIONS);
                if (copyBack != null && !copyBack.toString().isEmpty()) {
                    System.out.println(String.format("%1$s was updated successfully!", copyBack));
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(1);
        } finally {
            // close target file system after all patches are processed!
            if (targetFileSystem.isOpen()) {
                targetFileSystem.close();
            }
        }

    }
    
    private static void rollbackPatch (Path archiveFilePath, Path webappFolderPath) throws IOException {
        Path p = Files.copy(archiveFilePath, webappFolderPath, COPYOPTIONS);
        System.out.println(String.format("rollback processed on file '%1$s'", p.toString(), JOC_WAR_FILE_NAME));
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
                try {
                    Path p = Files.copy(file, targetFile, COPYOPTIONS);
                    if (alreadyExists) {
                        System.out.println(String.format("file '%1$s updated in %2$s'", p.toString(), JOC_WAR_FILE_NAME));
                    } else {
                        System.out.println(String.format("file '%1$s added to %2$s'", p.toString(), JOC_WAR_FILE_NAME));
                    }
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                    return FileVisitResult.TERMINATE;
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

    private static void printUsage(Path patchDir){
        System.out.println();
        System.out.println("Executes one or more patches on JOC from the patches folder.\n"
                + "The paths of the needed folders are resolved automatically.\n"
                + "If you want to set the paths differently use the options below.\n");
        System.out.println();
        System.out.println("patch-executor [Option]");
        System.out.println();
        System.out.printf("  %-29s | %s%n", "-?, -h, --help", "shows this help page, this option is exclusive and has no value");
        System.out.printf("  %-29s | %s%n", ROLLBACK, "rollback of already executed patches to the original joc.war, this option is exclusive and has no value");
        System.out.println();
        System.out.println("    OR");
        System.out.println();
        System.out.println("patch-executor [Options]");
        System.out.println();
        System.out.printf("  %-29s | %-31s (default: %s)%n", PATCHES_DIR + "=<Path>", "Path of JOCs patches directory", patchDir);
        System.out.printf("  %-29s | %-31s (default: %s)%n", ARCHIVES_DIR + "=<Path>", "Path of JOCs archives directory", patchDir.getParent().resolve(ARCHIVE_DIR_NAME));
        System.out.printf("  %-29s | %-31s (default: %s)%n", WEBAPP_DIR + "=<Path>", "Path of JOCs webapp directory", patchDir.getParent().resolve("webapps"));
        System.out.printf("  %-29s | %-31s (default: %s)%n", TEMP_DIR + "=<Path>", "Path of a temp directory", Paths.get(System.getProperty("java.io.tmpdir")));
    }

}
