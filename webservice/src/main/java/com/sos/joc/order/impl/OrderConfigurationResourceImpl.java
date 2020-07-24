package com.sos.joc.order.impl;

import javax.ws.rs.Path;

import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.configuration.JSObjectConfiguration;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.Configuration200;
import com.sos.joc.model.common.ConfigurationMime;
import com.sos.joc.model.order.OrderConfigurationFilter;
import com.sos.joc.order.resource.IOrderConfigurationResource;
import com.sos.schema.JsonValidator;

@Path("order")
public class OrderConfigurationResourceImpl extends JOCResourceImpl implements IOrderConfigurationResource {

    private static final String API_CALL = "./order/configuration";

    @Override
    public JOCDefaultResponse postOrderConfiguration(String accessToken, byte[] orderFilterBytes) {
        try {
            JsonValidator.validateFailFast(orderFilterBytes, OrderConfigurationFilter.class);
            OrderConfigurationFilter orderBody = Globals.objectMapper.readValue(orderFilterBytes, OrderConfigurationFilter.class);

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, orderBody, accessToken, orderBody.getJobschedulerId(), getPermissonsJocCockpit(
                    orderBody.getJobschedulerId(), accessToken).getOrder().getView().isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("orderId", orderBody.getOrderId());
            checkRequiredParameter("jobChain", orderBody.getJobChain());
            String jobChainPath = normalizePath(orderBody.getJobChain());
            checkFolderPermissions(jobChainPath);

            Configuration200 entity = new Configuration200();
            JSObjectConfiguration jocConfiguration = new JSObjectConfiguration(accessToken, versionIsOlderThan("1.13.1"));
            boolean responseInHtml = orderBody.getMime() == ConfigurationMime.HTML;
            entity = jocConfiguration.getOrderConfiguration(this, jobChainPath, orderBody.getOrderId(), responseInHtml);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        }
    }
}
