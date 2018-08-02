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

@Path("order")
public class OrderLogResourceImpl extends JOCResourceImpl implements IOrderLogResource {

    private static final String API_CALL = "./order/log";

    @Override
    public JOCDefaultResponse postOrderLog(String xAccessToken, String accessToken, OrderHistoryFilter orderHistoryFilter) throws Exception {
        return postOrderLog(getAccessToken(xAccessToken, accessToken), orderHistoryFilter);
    }

    public JOCDefaultResponse postOrderLog(String accessToken, OrderHistoryFilter orderHistoryFilter) throws Exception {
        return execute(API_CALL, accessToken, orderHistoryFilter);
    }

    @Override
    public JOCDefaultResponse getOrderLogHtml(String xAccessToken, String accessToken, String queryAccessToken, String jobschedulerId, String orderId,
            String jobChain, String historyId, String filename) throws Exception {
        return getOrderLogHtml(getAccessToken(xAccessToken, accessToken), queryAccessToken, jobschedulerId, orderId, jobChain, historyId, filename);
    }

    public JOCDefaultResponse getOrderLogHtml(String accessToken, String queryAccessToken, String jobschedulerId, String orderId, String jobChain,
            String historyId, String filename) throws Exception {
        if (accessToken == null) {
            accessToken = queryAccessToken;
        }
        OrderHistoryFilter orderHistoryFilter = setOrderHistoryFilter(jobschedulerId, orderId, jobChain, historyId, filename, LogMime.HTML);
        return execute(API_CALL + "/html", accessToken, orderHistoryFilter);
    }

    @Override
    public JOCDefaultResponse downloadOrderLog(String xAccessToken, String accessToken, String queryAccessToken, String jobschedulerId,
            String orderId, String jobChain, String historyId, String filename) throws Exception {
        return downloadOrderLog(getAccessToken(xAccessToken, accessToken), queryAccessToken, jobschedulerId, orderId, jobChain, historyId, filename);
    }

    public JOCDefaultResponse downloadOrderLog(String accessToken, String queryAccessToken, String jobschedulerId, String orderId, String jobChain,
            String historyId, String filename) throws Exception {
        if (accessToken == null) {
            accessToken = queryAccessToken;
        }
        OrderHistoryFilter orderHistoryFilter = setOrderHistoryFilter(jobschedulerId, orderId, jobChain, historyId, filename, LogMime.PLAIN);
        return downloadOrderLog(queryAccessToken, orderHistoryFilter);
    }

    @Override
    public JOCDefaultResponse downloadOrderLog(String xAccessToken, String accessToken, OrderHistoryFilter orderHistoryFilter) throws Exception {
        return downloadOrderLog(getAccessToken(xAccessToken, accessToken), orderHistoryFilter);
    }

    public JOCDefaultResponse downloadOrderLog(String accessToken, OrderHistoryFilter orderHistoryFilter) throws Exception {
        orderHistoryFilter.setMime(LogMime.PLAIN);
        return execute(API_CALL + "/download", accessToken, orderHistoryFilter);
    }

    @Override
    public JOCDefaultResponse getLogInfo(String xAccessToken, String accessToken, OrderHistoryFilter orderHistoryFilter) throws Exception {
        return getLogInfo(getAccessToken(xAccessToken, accessToken), orderHistoryFilter);
    }

    public JOCDefaultResponse getLogInfo(String accessToken, OrderHistoryFilter orderHistoryFilter) throws Exception {
        try {
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

    private JOCDefaultResponse execute(String apiCall, String accessToken, OrderHistoryFilter orderHistoryFilter) {
        try {
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

    private OrderHistoryFilter setOrderHistoryFilter(String jobschedulerId, String orderId, String jobChain, String historyId, String filename,
            LogMime mime) {
        OrderHistoryFilter orderHistoryFilter = new OrderHistoryFilter();
        orderHistoryFilter.setHistoryId(historyId);
        orderHistoryFilter.setJobChain(normalizePath(jobChain));
        orderHistoryFilter.setOrderId(orderId);
        orderHistoryFilter.setJobschedulerId(jobschedulerId);
        orderHistoryFilter.setFilename(filename);
        orderHistoryFilter.setMime(mime);
        return orderHistoryFilter;
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
