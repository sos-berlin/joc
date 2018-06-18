package com.sos.joc.order.impl;

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
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.LogOrderContent;
import com.sos.joc.classes.common.DeleteTempFile;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.LogContent;
import com.sos.joc.model.common.LogContent200;
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
        return execute(API_CALL + "/info", accessToken, orderHistoryFilter);
    }

    private JOCDefaultResponse execute(String apiCall, String accessToken, OrderHistoryFilter orderHistoryFilter) {
        try {
            JOCDefaultResponse jocDefaultResponse = init(apiCall, orderHistoryFilter, accessToken, orderHistoryFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(orderHistoryFilter.getJobschedulerId(), accessToken).getOrder().getView().isOrderLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            LogOrderContent logOrderContent = new LogOrderContent(orderHistoryFilter, dbItemInventoryInstance, accessToken);

            switch (apiCall) {
            case API_CALL:
                LogContent200 log = new LogContent200();
                log.setSurveyDate(Date.from(Instant.now()));
                final java.nio.file.Path path = getLogPath(logOrderContent, orderHistoryFilter, true);
                try {
                    if (Files.exists(path)) {
                        log.setSurveyDate(Date.from(Files.getLastModifiedTime(path).toInstant()));
                    }
                } catch (Exception e) {
                }
                try {
                    LogContent logContentSchema = new LogContent();
                    if (orderHistoryFilter.getMime() != null && orderHistoryFilter.getMime() == LogMime.HTML) {
                        logContentSchema.setHtml(logOrderContent.htmlWithColouredLogContent(path));
                    } else {
                        logContentSchema.setPlain(logOrderContent.getLogContent(path));
                    }
                    log.setLog(logContentSchema);
                    log.setDeliveryDate(Date.from(Instant.now()));
                    return JOCDefaultResponse.responseStatus200(log);
                } finally {
                    try {
                        if (path != null) {
                            Files.deleteIfExists(path);
                        }
                    } catch (Exception e1) {
                    }
                }

            case API_CALL + "/html":
                java.nio.file.Path path2 = getLogPath(logOrderContent, orderHistoryFilter, true);
                try {
                    return JOCDefaultResponse.responseHtmlStatus200(logOrderContent.htmlPageWithColouredLogContent(path2, "Order "
                            + orderHistoryFilter.getOrderId()));
                } finally {
                    try {
                        if (path2 != null) {
                            Files.deleteIfExists(path2);
                        }
                    } catch (Exception e1) {
                    }
                }

            case API_CALL + "/info":
                LogInfo200 logInfo200 = new LogInfo200();
                logInfo200.setSurveyDate(Date.from(Instant.now()));
                final java.nio.file.Path path3 = getLogPath(logOrderContent, orderHistoryFilter, false);
                logInfo200.setDeliveryDate(Date.from(Instant.now()));
                LogInfo logInfo = new LogInfo();
                logInfo.setFilename(path3.getFileName().toString());
                logInfo.setSize(0L);
                try {
                    if (Files.exists(path3)) {
                        logInfo.setSize(Files.size(path3));
                    }
                } catch (Exception e) {
                }
                logInfo.setDownload(logInfo.getSize() > Globals.maxSizeOfLogsToDisplay);
                logInfo200.setLog(logInfo);

                DeleteTempFile runnable = new DeleteTempFile(path3);
                new Thread(runnable).start();

                return JOCDefaultResponse.responseStatus200(logInfo200);

            default: // case API_CALL + "/download"
                final java.nio.file.Path path4 = getLogPath(logOrderContent, orderHistoryFilter, true);

                StreamingOutput fileStream = new StreamingOutput() {

                    @Override
                    public void write(OutputStream output) throws IOException {
                        InputStream in = null;
                        try {
                            in = Files.newInputStream(path4);
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
                                Files.delete(path4);
                            } catch (Exception e) {
                            }
                        }
                    }
                };

                return JOCDefaultResponse.responseOctetStreamDownloadStatus200(fileStream, getLogFilename(orderHistoryFilter));
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
                    return path;
                }
            }
        }
        checkRequiredParameter("jobschedulerId", orderHistoryFilter.getJobschedulerId());
        checkRequiredParameter("jobChain", orderHistoryFilter.getJobChain());
        checkRequiredParameter("orderId", orderHistoryFilter.getOrderId());
        checkRequiredParameter("historyId", orderHistoryFilter.getHistoryId());
        java.nio.file.Path path = null;
        try {
            path = logOrderContent.writeLogFile();
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

    private String getLogFilename(OrderHistoryFilter orderHistoryFilter) {
        return String.format("%s,%s.%s.order.log", Paths.get(orderHistoryFilter.getJobChain()).getFileName().toString(), orderHistoryFilter
                .getOrderId(), orderHistoryFilter.getHistoryId());
    }

}
