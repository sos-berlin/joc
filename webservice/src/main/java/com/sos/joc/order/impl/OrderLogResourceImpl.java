package com.sos.joc.order.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Path;
import javax.ws.rs.core.StreamingOutput;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.LogOrderContent;
import com.sos.joc.classes.common.DeleteTempFile;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.LogInfo;
import com.sos.joc.model.common.LogInfo200;
import com.sos.joc.model.common.LogMime;
import com.sos.joc.model.order.OrderHistoryFilter;
import com.sos.joc.order.resource.IOrderLogResource;
import com.sos.schema.JsonValidator;

@Path("order")
public class OrderLogResourceImpl extends JOCResourceImpl implements IOrderLogResource {

    private static final String API_CALL = "./order/log";

    @Override
    public JOCDefaultResponse postOrderLog(String accessToken, byte[] orderHistoryFilter) {
        return execute(API_CALL, accessToken, orderHistoryFilter, null);
    }

    @Override
    public JOCDefaultResponse getOrderLogHtml(String accessToken, String queryAccessToken, String jobschedulerId, String orderId, String jobChain,
            String historyId, String filename) {
        if (accessToken == null) {
            accessToken = queryAccessToken;
        }
        byte[] orderHistoryFilter = setOrderHistoryFilter(jobschedulerId, orderId, jobChain, historyId, filename);
        return execute(API_CALL + "/html", accessToken, orderHistoryFilter, LogMime.HTML);
    }

    @Override
    public JOCDefaultResponse downloadOrderLog(String accessToken, String queryAccessToken, String jobschedulerId, String orderId, String jobChain,
            String historyId, String filename) {
        if (accessToken == null) {
            accessToken = queryAccessToken;
        }
        byte[] orderHistoryFilter = setOrderHistoryFilter(jobschedulerId, orderId, jobChain, historyId, filename);
        return execute(API_CALL + "/download", accessToken, orderHistoryFilter, LogMime.PLAIN);
    }

    @Override
    public JOCDefaultResponse downloadOrderLog(String accessToken, byte[] orderHistoryFilter) {
        return execute(API_CALL + "/download", accessToken, orderHistoryFilter, LogMime.PLAIN);
    }

    @Override
    public JOCDefaultResponse getLogInfo(String accessToken, byte[] filterBytes) {
        try {
            JsonValidator.validateFailFast(filterBytes, OrderHistoryFilter.class);
            OrderHistoryFilter orderHistoryFilter = Globals.objectMapper.readValue(filterBytes, OrderHistoryFilter.class);
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + "/info", orderHistoryFilter, accessToken, orderHistoryFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(orderHistoryFilter.getJobschedulerId(), accessToken).getJob().getView().isTaskLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            LogOrderContent logOrderContent = new LogOrderContent(orderHistoryFilter, dbItemInventoryInstance, accessToken);

            LogInfo200 logInfo200 = new LogInfo200();
            logInfo200.setSurveyDate(Date.from(Instant.now()));
            java.nio.file.Path path = getLogPath(logOrderContent, orderHistoryFilter, false);
            LogInfo logInfo = new LogInfo();
            logInfo.setFilename(path.getFileName().toString());
            logInfo.setSize(null);
            try {
                if (Files.exists(path)) {
                    //logInfo.setSize(Files.size(path));
                    logInfo.setSize(getSize(path));
                    logInfo.setDownload(logInfo.getSize() > Globals.maxSizeOfLogsToDisplay);
                }
            } catch (Exception e) {
            }
            logInfo200.setLog(logInfo);
            logInfo200.setDeliveryDate(Date.from(Instant.now()));

            DeleteTempFile runnable = new DeleteTempFile(path);
            new Thread(runnable).start();

            return JOCDefaultResponse.responseStatus200(logInfo200);

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }

    private JOCDefaultResponse execute(String apiCall, String accessToken, byte[] filterBytes, LogMime mime) {
        try {
            JsonValidator.validateFailFast(filterBytes, OrderHistoryFilter.class);
            OrderHistoryFilter orderHistoryFilter = Globals.objectMapper.readValue(filterBytes, OrderHistoryFilter.class);
            if (mime != null) {
                orderHistoryFilter.setMime(mime);
            }
            
            JOCDefaultResponse jocDefaultResponse = init(apiCall, orderHistoryFilter, accessToken, orderHistoryFilter.getJobschedulerId(), getPermissonsJocCockpit(
                    orderHistoryFilter.getJobschedulerId(), accessToken).getJob().getView().isTaskLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            LogOrderContent logOrderContent = new LogOrderContent(orderHistoryFilter, dbItemInventoryInstance, accessToken);
            java.nio.file.Path path = getLogPath(logOrderContent, orderHistoryFilter, true);
//            boolean offerredAsDownload = false;
//            switch (apiCall) {
//            case API_CALL + "/download":
//                offerredAsDownload = true;
//                break;
//            default:
//                try {
//                    if (Files.exists(path) && getSize(path) > Globals.maxSizeOfLogsToDisplay) {
//                        offerredAsDownload = true;
//                    }
//                } catch (Exception e) {
//                }
//                break;
//            }
            boolean offerredAsDownload = (API_CALL + "/download").equals(apiCall);

            if ((API_CALL + "/html").equals(apiCall)) {
                path = logOrderContent.pathOfHtmlPageWithColouredGzipLogContent(path, "Order " + orderHistoryFilter.getHistoryId());
            } else if ((API_CALL).equals(apiCall) && orderHistoryFilter.getMime() != null && orderHistoryFilter.getMime() == LogMime.HTML) {
                path = logOrderContent.pathOfHtmlWithColouredGzipLogContent(path);
            }
            
            long unCompressedLength = getSize(path);

            final java.nio.file.Path downPath = path;

            StreamingOutput fileStream = new StreamingOutput() {

                @Override
                public void write(OutputStream output) throws IOException {
                    InputStream in = null;
                    try {
                        in = Files.newInputStream(downPath);
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
                            Files.deleteIfExists(downPath);
                        } catch (Exception e) {
                        }
                    }
                }
            };
            if (offerredAsDownload) {
                return JOCDefaultResponse.responseOctetStreamDownloadStatus200(fileStream, getFileName(path), unCompressedLength);
            } else {
                if ((API_CALL + "/html").equals(apiCall)) {
                    return JOCDefaultResponse.responseHtmlStatus200(fileStream, unCompressedLength);
                } else {
                    return JOCDefaultResponse.responsePlainStatus200(fileStream, unCompressedLength);
                }
            }

        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            if ((API_CALL + "/html").equals(apiCall)) {
                return JOCDefaultResponse.responseHTMLStatusJSError(e);
            } else {
                return JOCDefaultResponse.responseStatusJSError(e);
            }
        } catch (Exception e) {
            if ((API_CALL + "/html").equals(apiCall)) {
                return JOCDefaultResponse.responseHTMLStatusJSError(e, getJocError());
            } else {
                return JOCDefaultResponse.responseStatusJSError(e, getJocError());
            }
        }
    }

    private java.nio.file.Path getLogPath(LogOrderContent logOrderContent, OrderHistoryFilter orderHistoryFilter, boolean withFilenameCheck)
            throws Exception {

        if (withFilenameCheck) {
            if (orderHistoryFilter.getFilename() != null && !orderHistoryFilter.getFilename().isEmpty()) {
                java.nio.file.Path path = Paths.get(System.getProperty("java.io.tmpdir"), orderHistoryFilter.getFilename());
                if (Files.exists(path)) {
                    return Files.move(path, path.getParent().resolve(path.getFileName().toString()+".log"), StandardCopyOption.ATOMIC_MOVE);
                }
            }
        }
        checkRequiredParameter("jobschedulerId", orderHistoryFilter.getJobschedulerId());
        checkRequiredParameter("jobChain", orderHistoryFilter.getJobChain());
        checkRequiredParameter("orderId", orderHistoryFilter.getOrderId());
        checkRequiredParameter("historyId", orderHistoryFilter.getHistoryId());
        java.nio.file.Path path = null;
        try {
            path = logOrderContent.writeGzipLogFile();
            return path;
        } catch (Exception e) {
            try {
                if (path != null) {
                    Files.deleteIfExists(path);
                }
            } catch (Exception e1) {
            }
            throw e;
        }
    }

    private byte[] setOrderHistoryFilter(String jobschedulerId, String orderId, String jobChain, String historyId, String filename) {
        
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("jobschedulerId", jobschedulerId);
        if(historyId != null) {
            builder.add("historyId", historyId);
        }
        if(orderId != null) {
            builder.add("orderId", orderId);
        }
        if(jobChain != null) {
            builder.add("jobChain", jobChain);
        }
        if(filename != null) {
            builder.add("filename", filename);
        }
        return builder.build().toString().getBytes();
    }

    private String getFileName(java.nio.file.Path path) {
        return path.getFileName().toString().replaceFirst("^sos-(.*)-download-.+\\.tmp(\\.log)*$", "$1");
    }
    
    private long getSize(java.nio.file.Path path) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r");
        raf.seek(raf.length() - 4);
        int b4 = raf.read();
        int b3 = raf.read();
        int b2 = raf.read();
        int b1 = raf.read();
        raf.close();
        return ((long)b1 << 24) | ((long)b2 << 16) | ((long)b3 << 8) | (long)b4;
    }

}
