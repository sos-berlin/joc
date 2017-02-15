package com.sos.joc.jobscheduler.impl;

import java.nio.file.Paths;

import javax.ws.rs.Path;

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
    public JOCDefaultResponse getMainLog(String accessToken, HostPortTimeOutParameter hostPortParamSchema) throws Exception {

//        InputStream in = null;
//        InputStreamReader inReader = null;
//        BufferedReader inBufferedReader = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, hostPortParamSchema, accessToken, hostPortParamSchema.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getJobschedulerMaster().getView().isMainlog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobschedulerId", hostPortParamSchema.getJobschedulerId());
            getJobSchedulerInstanceByHostPort(hostPortParamSchema.getHost(), hostPortParamSchema.getPort(), hostPortParamSchema.getJobschedulerId());
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventoryInstance);
            String xmlCommand = jocXmlCommand.getShowStateCommand("folder", "folders no_subfolders", "/does/not/exist");
            jocXmlCommand.executePostWithThrowBadRequestAfterRetry(xmlCommand, accessToken);
            String logFileName = jocXmlCommand.getSosxml().selectSingleNodeValue("/spooler/answer/state/@log_file", null);
            if (logFileName == null) {
                throw new JobSchedulerBadRequestException("could not determine logfile name");
            }
            logFileName = Paths.get(logFileName).getFileName().toString();
            JOCJsonCommand jocJsonCommand = new JOCJsonCommand(this);
            jocJsonCommand.setUriBuilderForMainLog(logFileName);
            String logTxt = jocJsonCommand.getAcceptTypeFromGet(jocJsonCommand.getURI(), accessToken, "application/octet-stream,text/plain");

//            JOCJsonCommand jocJsonCommand = new JOCJsonCommand(this);
//            jocJsonCommand.setUriBuilderForMainLog();
//            HttpEntity htmlLog = jocJsonCommand.getInCompleteHtmlEntityFromGet(accessToken);
//            Header[] header = jocJsonCommand.getHttpResponse().getAllHeaders();
//            for (int i = 0; i < header.length; i++){
//                LOGGER.info(header[i].getName() + "=" + header[i].getValue());
//            }
//            LOGGER.info(htmlLog.getContentLength()+"");
//            LOGGER.info(htmlLog.getContentType().toString());
//            LOGGER.info(htmlLog.isChunked()+"");
//            
//            in = htmlLog.getContent();
//            StringBuilder strBuilder = new StringBuilder();
//            byte[] tmp = new byte[1024];
//            while (in.available() > 0) {
//                int i = in.read(tmp, 0, 1024);
//                if (i < 0) {
//                    break;
//                }
//                strBuilder.append(new String(tmp, 0, i));
//            }
//            String str = strBuilder.toString();
//            LOGGER.info(str);
//            str = str.replaceFirst("^.*\n<pre class='log'>", "").replaceAll("<span class='[^']+'>(\\d{4}-\\d{2}-\\d{2})", "$1").replaceAll("</span>(\r?\n)","$1");
//            LOGGER.info(str);
//            inReader = new InputStreamReader(in, "UTF-8");
//            inBufferedReader = new BufferedReader(inReader);
//            String str = "";
//            StringBuilder strBuilder = new StringBuilder();
//            Pattern p = Pattern.compile("^<span\\s+class\\s+=\\s+'[^']+'\\s+>(.*)</span>$");
//            Matcher m = null;
//            while ((str = inBufferedReader.readLine()) != null) {
//                m = p.matcher(str.trim());
//                if (m.find()) {
//                    strBuilder.append(m.group(1)+"\n");  
//                }
//            }
            return JOCDefaultResponse.responseTxtDownloadStatus200(logTxt, logFileName);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
//        } finally {
//            try {
//                if (inBufferedReader != null) {
//                    inBufferedReader.close();
//                } 
//            } catch (Exception ee) {
//            }
//            try {
//                if (inReader != null) {
//                    inReader.close();
//                } 
//            } catch (Exception ee) {
//            }
//            try {
//                if (in != null) {
//                    in.close();
//                } 
//            } catch (Exception ee) {
//            }
        }
    }
}
