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
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;


public class ExecutePatch {
    
    private static final String JOC_WAR_FILE_NAME = "joc.war";
    private static final String ARCHIVE_DIR_NAME = "archive";
    private static final String PATCHES_DIR= "patchesDir";
    private static final String ARCHIVES_DIR= "archivesDir";
    private static final String WEBAPP_DIR= "webappDir";
    private static final String TEMP_DIR= "tempDir";
    private static final String ROLLBACK= "rollback";
    private static CopyOption[] COPYOPTIONS = new StandardCopyOption[] { 
        StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING };

    public static void main(String[] args) throws Exception {
        Path patchDir = null;
        Path archivePath = null;
        Path webAppDir = null;
        Path tempDir = null;
        Path workDir = Paths.get(System.getProperty("user.dir"));
        Path executable = null;
        executable = workDir.resolve(System.getProperty("java.class.path"));
        boolean rollback = false;
        if (args != null && args.length == 1) {
            if (args[0].equalsIgnoreCase("-?") || args[0].equalsIgnoreCase("-h") || args[0].equalsIgnoreCase("--help")) {
                printUsage();
                System.exit(0);
                return;
            } else if (args[0].startsWith(ROLLBACK)) {
                rollback = true;
                System.out.println("processing rollback...");
            }
        }
        if (args != null && args.length > 1) {
            for(int i = 0; i < args.length; i++) {
                if (args[i].startsWith(PATCHES_DIR)) {
                    String[] split = args[i].split("=", 2);
                    patchDir = Paths.get(split[1]);
                    System.out.println("Path of the patches directory = " + patchDir);
                }
                if (args[i].startsWith(ARCHIVES_DIR)) {
                    String[] split = args[i].split("=", 2);
                    archivePath = Paths.get(split[1]);
                    System.out.println("Path of the archives directory = " + archivePath);
                }
                if (args[i].startsWith(WEBAPP_DIR)) {
                    String[] split = args[i].split("=", 2);
                    webAppDir = Paths.get(split[1]);
                    System.out.println("Path of the webapp directory = " + webAppDir);
                }
                if (args[i].startsWith(TEMP_DIR)) {
                    String[] split = args[i].split("=", 2);
                    tempDir = Paths.get(split[1]);
                    System.out.println("Path of the temp directory = " + tempDir);
                }
            }
            if (patchDir == null || archivePath == null || webAppDir == null || tempDir == null) {
                System.err.println("One or more parameters missing! Please check if all four parameters for the paths are set.");
            }
        } else {
            patchDir = executable.getParent().getParent();
            System.out.println("Path of the patches directory = " + patchDir);
            archivePath = patchDir.getParent().resolve(ARCHIVE_DIR_NAME);
            System.out.println("Path of the archives directory = " + archivePath);
            webAppDir = patchDir.getParent().resolve("webapps");
            System.out.println("Path of the webapp directory = " + webAppDir);
            tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
            System.out.println("Path of the temp directory = " + tempDir);
        }
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
        File patchesDirectory = new File(patchDir.toString());
        String[] files = patchesDirectory.list();

        Comparator<String> fileNameComparator = new Comparator<String>() {
            
            @Override
            public int compare(String o1, String o2) {
                String timestamp1 = o1.replaceFirst("(\\d{8})", "$1");
                String timestamp2 = o2.replaceFirst("(\\d{8})", "$1");
                return timestamp1.compareTo(timestamp2);
            }
        };

        Set<String> patchFiles = new TreeSet<String>(fileNameComparator);
        
        for(int i = 0; i < files.length; i++) {
            File file = new File(files[i]);
            if (!file.isDirectory() && files[i].endsWith(".zip")) {
                patchFiles.add(files[i]);
            }
        }
        // process a new zip-file-system for each zip-file in patches folder
        try {
            if ((patchFiles.isEmpty() && Files.exists(archivePath.resolve(JOC_WAR_FILE_NAME)))
                    || rollback) {
                rollbackPatch(archivePath.resolve(JOC_WAR_FILE_NAME), webAppJocWarPath);
            } else {
                for (String patchFile : patchFiles) {
                    System.out.println(patchFile);
                    FileSystem sourceFileSystem = null;
                    sourceFileSystem = FileSystems.newFileSystem(Paths.get(patchFile), null);
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

    private static void printUsage(){
        System.out.println();
        System.out.println("Executes one or more patches on JOC from the patches folder.\n"
                + "The paths of the needed folders are resolved automatically.\n"
                + "If you want to set the paths differently use the options below.\n"
                + "If you choose so, all four path options are required.");
        System.out.println();
        System.out.println("patch-executor [Option]");
        System.out.println();
        System.out.println("  -? | -h | --help\tshows this help page, this option is exclusive and has no value");
        System.out.println("  " + ROLLBACK + "\t\trollback of already executed patches to the original joc.war, this option is exclusive and has no value");
        System.out.println();
        System.out.println("    OR");
        System.out.println();
        System.out.println("patch-executor [Option]=[Value] [Option]=[Value] [Option]=[Value] [Option]=[Value]");
        System.out.println();
        System.out.println("  " + PATCHES_DIR + "\t\tPath of JOCs patches directory");
        System.out.println("  " + ARCHIVES_DIR + "\t\tPath of JOCs archives directory");
        System.out.println("  " + WEBAPP_DIR + "\t\tPath of JOCs webapp directory");
        System.out.println("  " + TEMP_DIR + "\t\tPath of a temp directory, this is the working directory to actually patch the JOC web application");
    }

}
