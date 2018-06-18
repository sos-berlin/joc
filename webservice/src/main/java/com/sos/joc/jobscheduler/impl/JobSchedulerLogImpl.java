package com.sos.joc.jobscheduler.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;
import javax.ws.rs.core.StreamingOutput;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.common.DeleteTempFile;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerLogResource;
import com.sos.joc.model.common.LogInfo;
import com.sos.joc.model.common.LogInfo200;
import com.sos.joc.model.jobscheduler.HostPortParameter;

@Path("jobscheduler")
public class JobSchedulerLogImpl extends JOCResourceImpl implements IJobSchedulerLogResource {

    private static final String API_CALL = "./jobscheduler/log";

    @Override
    public JOCDefaultResponse getMainLog(String xAccessToken, String accessToken, String queryAccessToken, String jobschedulerId, String host,
            Integer port, String filename) throws Exception {
        return getMainLog(getAccessToken(xAccessToken, accessToken), queryAccessToken, jobschedulerId, host, port, filename);
    }

    public JOCDefaultResponse getMainLog(String accessToken, String queryAccessToken, String jobschedulerId, String host, Integer port,
            String filename) throws Exception {

        HostPortParameter hostPortParams = new HostPortParameter();
        hostPortParams.setJobschedulerId(jobschedulerId);
        hostPortParams.setHost(host);
        hostPortParams.setPort(port);
        hostPortParams.setFilename(filename);

        if (accessToken == null) {
            accessToken = queryAccessToken;
        }

        return getMainLog(accessToken, hostPortParams);
    }

    @Override
    public JOCDefaultResponse getMainLog(String xAccessToken, String accessToken, HostPortParameter hostPortParamSchema) throws Exception {
        return getMainLog(getAccessToken(xAccessToken, accessToken), hostPortParamSchema);
    }

    public JOCDefaultResponse getMainLog(String accessToken, HostPortParameter hostPortParamSchema) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, hostPortParamSchema, accessToken, hostPortParamSchema.getJobschedulerId(),
                    getPermissonsJocCockpit(hostPortParamSchema.getJobschedulerId(), accessToken).getJobschedulerMaster().getView().isMainlog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            final java.nio.file.Path responseEntity = getLogPath(accessToken, hostPortParamSchema, true);

            StreamingOutput fileStream = new StreamingOutput() {

                @Override
                public void write(OutputStream output) throws IOException {
                    InputStream in = null;
                    try {
                        in = Files.newInputStream(responseEntity);
                        byte[] buffer = new byte[4096];
                        int length;
                        while ((length = in.read(buffer)) > 0) {
                            output.write(buffer, 0, length);
                        }
                        output.flush();
                    } finally {
                        try {
                            output.close();
                        } catch (Exception e) {
                        }
                        if (in != null) {
                            try {
                                in.close();
                            } catch (Exception e) {
                            }
                        }
                        try {
                            Files.delete(responseEntity);
                        } catch (Exception e) {
                        }
                    }
                }
            };

            return JOCDefaultResponse.responseOctetStreamDownloadStatus200(fileStream, hostPortParamSchema.getFilename());
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } catch (OutOfMemoryError e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    @Override
    public JOCDefaultResponse getLogInfo(String xAccessToken, String accessToken, HostPortParameter hostPortParamSchema) throws Exception {
        return getLogInfo(getAccessToken(xAccessToken, accessToken), hostPortParamSchema);
    }

    public JOCDefaultResponse getLogInfo(String accessToken, HostPortParameter hostPortParamSchema) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "/html", hostPortParamSchema, accessToken, hostPortParamSchema.getJobschedulerId(),
                    getPermissonsJocCockpit(hostPortParamSchema.getJobschedulerId(), accessToken).getJobschedulerMaster().getView().isMainlog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            LogInfo200 entity = new LogInfo200();
            entity.setSurveyDate(Date.from(Instant.now()));

            final java.nio.file.Path responseEntity = getLogPath(accessToken, hostPortParamSchema, false);

            entity.setDeliveryDate(Date.from(Instant.now()));
            LogInfo logInfo = new LogInfo();
            logInfo.setDownload(Boolean.TRUE);
            logInfo.setFilename(responseEntity.getFileName().toString());
            logInfo.setSize(0L);
            try {
                if (Files.exists(responseEntity)) {
                    logInfo.setSize(Files.size(responseEntity));
                }
            } catch (Exception e) {
            }
            entity.setLog(logInfo);

            DeleteTempFile runnable = new DeleteTempFile(responseEntity);
            new Thread(runnable).start();

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } catch (OutOfMemoryError e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private java.nio.file.Path getLogPath(String accessToken, HostPortParameter hostPortParamSchema, boolean withFilenameCheck) throws Exception {

        if (withFilenameCheck) {
            if (hostPortParamSchema.getFilename() != null && !hostPortParamSchema.getFilename().isEmpty()) {
                java.nio.file.Path path = Paths.get(System.getProperty("java.io.tmpdir"), hostPortParamSchema.getFilename());
                if (Files.exists(path)) {
                    return path;
                }
            }
        }
        checkRequiredParameter("jobschedulerId", hostPortParamSchema.getJobschedulerId());
        getJobSchedulerInstanceByHostPort(hostPortParamSchema.getHost(), hostPortParamSchema.getPort(), hostPortParamSchema.getJobschedulerId());
        JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
        String xmlCommand = jocXmlCommand.getShowStateCommand("folder", "folders no_subfolders", "/does/not/exist");
        jocXmlCommand.executePostWithThrowBadRequestAfterRetry(xmlCommand, accessToken);
        String logFilename = jocXmlCommand.getSosxml().selectSingleNodeValue("/spooler/answer/state/@log_file", null);
        if (logFilename == null) {
            throw new JobSchedulerBadRequestException("could not determine logfile name");
        }
        logFilename = Paths.get(logFilename).getFileName().toString();
        hostPortParamSchema.setFilename(logFilename);

        // increase timeout for large log files
        int socketTimeout = Math.max(Globals.httpSocketTimeout, 30000);
        JOCJsonCommand jocJsonCommand = new JOCJsonCommand(this);
        jocJsonCommand.setSocketTimeout(socketTimeout);
        jocJsonCommand.setUriBuilderForMainLog(logFilename);
        // final byte[] responseEntity = jocJsonCommand.getByteArrayFromGet(jocJsonCommand.getURI(), accessToken, "text/plain,application/octet-stream");
        return jocJsonCommand.getFilePathFromGet(jocJsonCommand.getURI(), accessToken, "text/plain,application/octet-stream");
    }

}
