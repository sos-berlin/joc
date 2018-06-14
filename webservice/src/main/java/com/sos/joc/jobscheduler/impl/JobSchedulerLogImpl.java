package com.sos.joc.jobscheduler.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.Path;
import javax.ws.rs.core.StreamingOutput;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCJsonCommand;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.jobscheduler.resource.IJobSchedulerLogResource;
import com.sos.joc.model.jobscheduler.HostPortTimeOutParameter;

@Path("jobscheduler")
public class JobSchedulerLogImpl extends JOCResourceImpl implements IJobSchedulerLogResource {

    private static final String API_CALL = "./jobscheduler/log";

	@Override
	public JOCDefaultResponse getMainLog(String xAccessToken, String accessToken, String queryAccessToken,
			String jobschedulerId, String host, Integer port) throws Exception {
		return getMainLog(getAccessToken(xAccessToken, accessToken), queryAccessToken, jobschedulerId, host, port);
	}

	public JOCDefaultResponse getMainLog(String accessToken, String queryAccessToken, String jobschedulerId,
			String host, Integer port) throws Exception {

		HostPortTimeOutParameter hostPortParams = new HostPortTimeOutParameter();
		hostPortParams.setJobschedulerId(jobschedulerId);
		hostPortParams.setHost(host);
		hostPortParams.setPort(port);

		if (accessToken == null) {
			accessToken = queryAccessToken;
		}

		return getMainLog(accessToken, hostPortParams);
	}

	@Override
	public JOCDefaultResponse getMainLog(String xAccessToken, String accessToken,
			HostPortTimeOutParameter hostPortParamSchema) throws Exception {
		return getMainLog(getAccessToken(xAccessToken, accessToken), hostPortParamSchema);
	}

	public JOCDefaultResponse getMainLog2(String accessToken, HostPortTimeOutParameter hostPortParamSchema)
			throws Exception {

		try {
			JOCDefaultResponse jocDefaultResponse = init(API_CALL, hostPortParamSchema, accessToken,
					hostPortParamSchema.getJobschedulerId(),
					getPermissonsJocCockpit(hostPortParamSchema.getJobschedulerId(), accessToken)
							.getJobschedulerMaster().getView().isMainlog());
			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			checkRequiredParameter("jobschedulerId", hostPortParamSchema.getJobschedulerId());
			getJobSchedulerInstanceByHostPort(hostPortParamSchema.getHost(), hostPortParamSchema.getPort(),
					hostPortParamSchema.getJobschedulerId());
			JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
			String xmlCommand = jocXmlCommand.getShowStateCommand("folder", "folders no_subfolders", "/does/not/exist");
			jocXmlCommand.executePostWithThrowBadRequestAfterRetry(xmlCommand, accessToken);
			String logFileName = jocXmlCommand.getSosxml().selectSingleNodeValue("/spooler/answer/state/@log_file",
					null);
			if (logFileName == null) {
				throw new JobSchedulerBadRequestException("could not determine logfile name");
			}
			logFileName = Paths.get(logFileName).getFileName().toString();
			JOCJsonCommand jocJsonCommand = new JOCJsonCommand(this);
			// increase timeout for large log files
			int socketTimeout = Math.max(Globals.httpSocketTimeout, 10000);
			jocJsonCommand.setSocketTimeout(socketTimeout);
			jocJsonCommand.setUriBuilderForMainLog(logFileName);
			String logTxt = jocJsonCommand.getAcceptTypeFromGet(jocJsonCommand.getURI(), accessToken,
					"application/octet-stream,text/plain");
			return JOCDefaultResponse.responseOctetStreamDownloadStatus200(logTxt, logFileName);
		} catch (JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		}
	}

    public JOCDefaultResponse getMainLog(String accessToken, HostPortTimeOutParameter hostPortParamSchema)
            throws Exception {
        JOCJsonCommand jocJsonCommand = new JOCJsonCommand(this);
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, hostPortParamSchema, accessToken,
                    hostPortParamSchema.getJobschedulerId(),
                    getPermissonsJocCockpit(hostPortParamSchema.getJobschedulerId(), accessToken)
                            .getJobschedulerMaster().getView().isMainlog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            checkRequiredParameter("jobschedulerId", hostPortParamSchema.getJobschedulerId());
            getJobSchedulerInstanceByHostPort(hostPortParamSchema.getHost(), hostPortParamSchema.getPort(),
                    hostPortParamSchema.getJobschedulerId());
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
            String xmlCommand = jocXmlCommand.getShowStateCommand("folder", "folders no_subfolders", "/does/not/exist");
            jocXmlCommand.executePostWithThrowBadRequestAfterRetry(xmlCommand, accessToken);
            String logFileName = jocXmlCommand.getSosxml().selectSingleNodeValue("/spooler/answer/state/@log_file",
                    null);
            if (logFileName == null) {
                throw new JobSchedulerBadRequestException("could not determine logfile name");
            }
            logFileName = Paths.get(logFileName).getFileName().toString();
            
            // increase timeout for large log files
            int socketTimeout = Math.max(Globals.httpSocketTimeout, 30000);
            jocJsonCommand.setSocketTimeout(socketTimeout);
            jocJsonCommand.setUriBuilderForMainLog(logFileName);
            //final byte[] responseEntity = jocJsonCommand.getByteArrayFromGet(jocJsonCommand.getURI(), accessToken, "text/plain,application/octet-stream");
            final java.nio.file.Path responseEntity = jocJsonCommand.getFilePathFromGet(jocJsonCommand.getURI(), accessToken, "text/plain,application/octet-stream");
            
            StreamingOutput fileStream = new StreamingOutput() {

                @Override
                public void write(OutputStream output) throws IOException {
                    InputStream in = null;
                    try {
                        in = Files.newInputStream(responseEntity);
                        byte[] buffer = new byte[4096];
                        int length;
                        while((length = in.read(buffer)) > 0) {
                            output.write(buffer, 0, length);
                        }
                        output.flush();
                    } finally {
                        try {
                            output.close();
                        } catch (Exception e) {}
                        if (in != null) {
                            try {
                                in.close();
                            } catch (Exception e) {}
                        }
                        try {
                            Files.delete(responseEntity);
                        } catch (Exception e) {}
                    }
                }
            };
            
            return JOCDefaultResponse.responseOctetStreamDownloadStatus200(fileStream, logFileName);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } catch (OutOfMemoryError e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

}
