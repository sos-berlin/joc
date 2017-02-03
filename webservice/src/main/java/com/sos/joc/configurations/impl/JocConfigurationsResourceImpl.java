package com.sos.joc.configurations.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.joc.db.JocConfigurationDbItem;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.configurations.resource.IJocConfigurationsResource;
import com.sos.joc.db.configuration.JocConfigurationDbLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.configuration.Configuration;
import com.sos.joc.model.configuration.ConfigurationObjectType;
import com.sos.joc.model.configuration.ConfigurationType;
import com.sos.joc.model.configuration.Configurations;
import com.sos.joc.model.configuration.ConfigurationsFilter;

@Path("configurations")
public class JocConfigurationsResourceImpl extends JOCResourceImpl implements IJocConfigurationsResource {

    private static final String API_CALL = "./configurations";
    private SOSHibernateConnection connection = null;

    @Override
    public JOCDefaultResponse postConfigurations(String accessToken, ConfigurationsFilter configurationsFilter) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, configurationsFilter, accessToken, configurationsFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getJOCConfigurations().getPrivate().isView());

            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            connection = Globals.createSosHibernateStatelessConnection("API_CALL");
            Globals.beginTransaction(connection);

            String objectType = "";

            if (configurationsFilter.getObjectType() == null) {
                objectType = null;
            } else {
                objectType = configurationsFilter.getObjectType().name();
            }

            String configurationType = "";

            if (configurationsFilter.getConfigurationType() == null) {
                configurationType = null;
            } else {
                configurationType = configurationsFilter.getConfigurationType().name();
            }

            checkRequiredParameter("jobschedulerId", configurationsFilter.getJobschedulerId());

            JocConfigurationDbLayer jocConfigurationDBLayer = new JocConfigurationDbLayer(connection);

            jocConfigurationDBLayer.getFilter().setObjectType(objectType);
            jocConfigurationDBLayer.getFilter().setInstanceId(dbItemInventoryInstance.getId());
            jocConfigurationDBLayer.getFilter().setConfigurationType(configurationType);
            jocConfigurationDBLayer.getFilter().setAccount(configurationsFilter.getAccount());
            jocConfigurationDBLayer.getFilter().setShared(configurationsFilter.getShared());

            List<JocConfigurationDbItem> listOfJocConfigurationDbItem = jocConfigurationDBLayer.getJocConfigurationList(0);
            Configurations configurations = new Configurations();
            ArrayList<Configuration> listOfConfigurations = new ArrayList<Configuration>();

            for (JocConfigurationDbItem jocConfigurationDbItem : listOfJocConfigurationDbItem) {
                Configuration configuration = new Configuration();
                configuration.setAccount(jocConfigurationDbItem.getAccount());
                configuration.setConfigurationType(ConfigurationType.fromValue(jocConfigurationDbItem.getConfigurationType()));
                configuration.setJobschedulerId(configurationsFilter.getJobschedulerId());
                configuration.setName(jocConfigurationDbItem.getName());
                configuration.setObjectType(ConfigurationObjectType.fromValue(jocConfigurationDbItem.getObjectType()));
                configuration.setShared(jocConfigurationDbItem.getShared());
                listOfConfigurations.add(configuration);
            }

            configurations.setDeliveryDate(new Date());
            configurations.setConfigurations(listOfConfigurations);

            return JOCDefaultResponse.responseStatus200(configurations);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }

    }

}