package com.sos.auth.rest;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.jitl.joc.db.JocConfigurationDbItem;
import com.sos.joc.Globals;
import com.sos.joc.db.configuration.JocConfigurationDbLayer;
import com.sos.joc.exceptions.JocException;

public class SOSShiroIniShare {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSShiroIniShare.class);
    private String iniFileName;
    SOSHibernateSession sosHibernateSession;

    public SOSShiroIniShare(SOSHibernateSession sosHibernateSession) throws JocException {
        super();
        this.sosHibernateSession = sosHibernateSession;
    }

    public void provideIniFile() throws SOSHibernateException, JocException, IOException {
        iniFileName = Globals.getShiroIniInClassPath();
        if (!iniFileName.startsWith("file:")) {
            LOGGER.warn("can not provide shiro.ini file from filesystem");
        } else {
            iniFileName = iniFileName.replaceFirst("^file:", "");
        }

        String iniFileNameActive = Globals.getIniFileForShiro(iniFileName);
        File iniFileActive = new File(iniFileNameActive);

        checkForceFile();
        String inifileContent = getContentFromDatabase();
        if (inifileContent.isEmpty()) {
            File forceFile = new File(iniFileName);
            forceFile.delete();
            iniFileActive.renameTo(forceFile);
            checkForceFile();
            inifileContent = getContentFromDatabase();
        }

        createShiroIniFileFromDb(inifileContent, iniFileActive);

    }

    private void checkForceFile() throws SOSHibernateException, JocException, UnsupportedEncodingException, IOException {
        File forceFile = new File(iniFileName);

        if (forceFile.exists()) {
            LOGGER.debug(forceFile.getAbsoluteFile() + " found. Will be moved to database");
            copyFileToDb(forceFile);
            forceFile.delete();
            File iniFile = new File(Globals.getIniFileForShiro(iniFileName));
            File destinationFile = new File(iniFileName + ".backup");
            destinationFile.delete();
            iniFile.renameTo(destinationFile);
        }

    }

    public void copyFileToDb(File iniFile) throws SOSHibernateException, JocException, UnsupportedEncodingException, IOException {
        Globals.beginTransaction(sosHibernateSession);

        JocConfigurationDbItem jocConfigurationDbItem;
        JocConfigurationDbLayer jocConfigurationDBLayer = new JocConfigurationDbLayer(sosHibernateSession);
        jocConfigurationDBLayer.getFilter().setAccount(".");
        jocConfigurationDBLayer.getFilter().setConfigurationType("SHIRO");
        List<JocConfigurationDbItem> listOfConfigurtions = jocConfigurationDBLayer.getJocConfigurationList(0);
        if (listOfConfigurtions.size() > 0) {
            jocConfigurationDbItem = listOfConfigurtions.get(0);
        } else {
            jocConfigurationDbItem = new JocConfigurationDbItem();
            jocConfigurationDbItem.setId(null);
            jocConfigurationDbItem.setAccount(".");
            jocConfigurationDbItem.setConfigurationType("SHIRO");
            jocConfigurationDbItem.setName("shiro.ini");
            jocConfigurationDbItem.setShared(true);
            jocConfigurationDbItem.setInstanceId(0L);
            jocConfigurationDbItem.setSchedulerId("");
        }

        String content = new String(Files.readAllBytes(Paths.get(iniFile.getAbsolutePath())), "UTF-8");

        jocConfigurationDbItem.setConfigurationItem(content);
        Long id = jocConfigurationDBLayer.saveOrUpdateConfiguration(jocConfigurationDbItem);
        if (jocConfigurationDbItem.getId() == null) {
            jocConfigurationDbItem.setId(id);
        }
        Globals.commit(sosHibernateSession);
        ;
    }

    private void createShiroIniFileFromDb(String inifileContent, File iniFileActive) throws IOException {
        String contentIniFileActive = "*nothing";
        if (iniFileActive.exists()) {
            contentIniFileActive = new String(Files.readAllBytes(Paths.get(iniFileActive.getAbsolutePath())), "UTF-8");
        }

        if (!inifileContent.equals(contentIniFileActive)) {
            LOGGER.debug (iniFileActive.getAbsoluteFile() + " content changed. Will be updated from database");
            byte[] bytes = inifileContent.getBytes(StandardCharsets.UTF_8);
            Files.write(Paths.get(Globals.getIniFileForShiro(iniFileName)), bytes, java.nio.file.StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        }

    }

    private String getContentFromDatabase() throws SOSHibernateException {
        Globals.beginTransaction(sosHibernateSession);

        JocConfigurationDbItem jocConfigurationDbItem;
        JocConfigurationDbLayer jocConfigurationDBLayer = new JocConfigurationDbLayer(sosHibernateSession);
        jocConfigurationDBLayer.getFilter().setAccount(".");
        jocConfigurationDBLayer.getFilter().setConfigurationType("SHIRO");
        List<JocConfigurationDbItem> listOfConfigurtions = jocConfigurationDBLayer.getJocConfigurationList(0);
        Globals.commit(sosHibernateSession);

        if (listOfConfigurtions.size() > 0) {
            jocConfigurationDbItem = listOfConfigurtions.get(0);
            return jocConfigurationDbItem.getConfigurationItem();
        } else {
            return "";
        }

    }
}
