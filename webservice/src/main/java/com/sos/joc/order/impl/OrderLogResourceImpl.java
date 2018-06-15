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

import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.LogOrderContent;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.LogContent;
import com.sos.joc.model.common.LogContent200;
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
            String jobChain, String historyId) throws Exception {
        return getOrderLogHtml(getAccessToken(xAccessToken, accessToken), queryAccessToken, jobschedulerId, orderId, jobChain, historyId);
    }

    public JOCDefaultResponse getOrderLogHtml(String accessToken, String queryAccessToken, String jobschedulerId, String orderId, String jobChain,
            String historyId) throws Exception {
        if (accessToken == null) {
            accessToken = queryAccessToken;
        }
        OrderHistoryFilter orderHistoryFilter = setOrderHistoryFilter(jobschedulerId, orderId, jobChain, historyId, LogMime.HTML);
        return execute(API_CALL + "/html", accessToken, orderHistoryFilter);
    }

    @Override
    public JOCDefaultResponse downloadOrderLog(String xAccessToken, String accessToken, String queryAccessToken, String jobschedulerId,
            String orderId, String jobChain, String historyId) throws Exception {
        return downloadOrderLog(getAccessToken(xAccessToken, accessToken), queryAccessToken, jobschedulerId, orderId, jobChain, historyId);
    }

    public JOCDefaultResponse downloadOrderLog(String accessToken, String queryAccessToken, String jobschedulerId, String orderId, String jobChain,
            String historyId) throws Exception {
        if (accessToken == null) {
            accessToken = queryAccessToken;
        }
        OrderHistoryFilter orderHistoryFilter = setOrderHistoryFilter(jobschedulerId, orderId, jobChain, historyId, LogMime.PLAIN);
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

    private JOCDefaultResponse execute(String apiCall, String accessToken, OrderHistoryFilter orderHistoryFilter) {
        try {
            JOCDefaultResponse jocDefaultResponse = init(apiCall, orderHistoryFilter, accessToken, orderHistoryFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(orderHistoryFilter.getJobschedulerId(), accessToken).getOrder().getView().isOrderLog());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobschedulerId", orderHistoryFilter.getJobschedulerId());
            checkRequiredParameter("jobChain", orderHistoryFilter.getJobChain());
            checkRequiredParameter("orderId", orderHistoryFilter.getOrderId());
            checkRequiredParameter("historyId", orderHistoryFilter.getHistoryId());

            LogOrderContent logOrderContent = new LogOrderContent(orderHistoryFilter, dbItemInventoryInstance, accessToken);

            switch (apiCall) {
            case API_CALL:
                LogContent200 entity = new LogContent200();
                entity.setSurveyDate(Date.from(Instant.now()));
                LogContent logContentSchema = new LogContent();

                if (orderHistoryFilter.getMime() != null && orderHistoryFilter.getMime() == LogMime.HTML) {
                    java.nio.file.Path path = null;
                    try {
                        path = logOrderContent.writeLogFile();
                        logContentSchema.setHtml(logOrderContent.htmlWithColouredLogContent(path));
                    } finally {
                        try {
                            if (path != null) {
                                Files.deleteIfExists(path);
                            }
                        } catch (Exception e2) {
                        }
                    }
                } else {
                    logContentSchema.setPlain(logOrderContent.getLog());
                }
                entity.setLog(logContentSchema);
                entity.setDeliveryDate(Date.from(Instant.now()));
                return JOCDefaultResponse.responseStatus200(entity);

            case API_CALL + "/html":
                return JOCDefaultResponse.responseHtmlStatus200(logOrderContent.htmlPageWithColouredLogContent(logOrderContent.getLog(), "Order "
                        + orderHistoryFilter.getOrderId()));

            default: //case API_CALL + "/download"
                java.nio.file.Path path = null;
                try {
                    path = logOrderContent.writeLogFile();
                } catch (Exception e1) {
                    try {
                        Files.deleteIfExists(path);
                    } catch (Exception e2) {
                    }
                    throw e1;
                }
                final java.nio.file.Path tmpPath = path;

                StreamingOutput fileStream = new StreamingOutput() {

                    @Override
                    public void write(OutputStream output) throws IOException {
                        InputStream in = null;
                        try {
                            in = Files.newInputStream(tmpPath);
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
                                Files.delete(tmpPath);
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

    private OrderHistoryFilter setOrderHistoryFilter(String jobschedulerId, String orderId, String jobChain, String historyId, LogMime mime) {
        OrderHistoryFilter orderHistoryFilter = new OrderHistoryFilter();
        orderHistoryFilter.setHistoryId(historyId);
        orderHistoryFilter.setJobChain(normalizePath(jobChain));
        orderHistoryFilter.setOrderId(orderId);
        orderHistoryFilter.setJobschedulerId(jobschedulerId);
        orderHistoryFilter.setMime(mime);
        return orderHistoryFilter;
    }

    private String getLogFilename(OrderHistoryFilter orderHistoryFilter) {
        return String.format("%s,%s.%s.order.log", Paths.get(orderHistoryFilter.getJobChain()).getFileName().toString(), orderHistoryFilter
                .getOrderId(), orderHistoryFilter.getHistoryId());
    }

}
