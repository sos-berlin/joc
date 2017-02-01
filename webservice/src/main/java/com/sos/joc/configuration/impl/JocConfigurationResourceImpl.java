package com.sos.joc.configuration.impl;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;
import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.joc.db.JocConfigurationDbItem;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.configuration.resource.IJocConfigurationResource;
import com.sos.joc.db.configuration.JocConfigurationDbLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.configuration.Configuration;
import com.sos.joc.model.configuration.Configuration200;
import com.sos.joc.model.configuration.ConfigurationType;

@Path("configuration")
public class JocConfigurationResourceImpl extends JOCResourceImpl implements IJocConfigurationResource {

    private static final String API_CALL_READ = "./configuration";
    private static final String API_CALL_SAVE = "./configuration/save_configuration";
    private static final String API_CALL_DELETE = "./configuration/delete_configuration";
    private static final String API_CALL_SHARE = "./configuration/share";
    private static final String API_CALL_PRIVATE = "./configuration/make_private";
    private SOSHibernateConnection connection = null;
    private JocConfigurationDbLayer jocConfigurationDBLayer;
    private JocConfigurationDbItem jocConfigurationDbItem;

    private void init(Configuration configuration) throws Exception {
        jocConfigurationDBLayer = new JocConfigurationDbLayer(connection);
        jocConfigurationDbItem = new JocConfigurationDbItem();
        String objectType = "";

        if (configuration.getObjectType() == null) {
            objectType = null;
        } else {
            objectType = configuration.getObjectType().name();
        }

        String configurationType = "";

        if (configuration.getConfigurationType() == null) {
            configurationType = null;
        } else {
            configurationType = configuration.getConfigurationType().name();
        }

        checkRequiredParameter("jobschedulerId", configuration.getJobschedulerId());
        checkRequiredParameter("account", configuration.getAccount());

        checkRequiredParameter("configurationType", configurationType);

        if (configuration.getConfigurationType().name().equals(ConfigurationType.CUSTOMIZATION.name())) {
            checkRequiredParameter("name", configuration.getName());
            checkRequiredParameter("objectType", objectType);

            jocConfigurationDBLayer.getFilter().setObjectType(objectType);
            jocConfigurationDBLayer.getFilter().setName(configuration.getName());

            jocConfigurationDbItem.setName(configuration.getName());
            jocConfigurationDbItem.setObjectType(objectType);
        }

        jocConfigurationDbItem.setInstanceId(dbItemInventoryInstance.getId());
        jocConfigurationDbItem.setShared(configuration.getShared());
        jocConfigurationDbItem.setConfigurationItem(configuration.getConfigurationItem());
        jocConfigurationDbItem.setConfigurationType(configurationType);
        jocConfigurationDbItem.setAccount(configuration.getAccount());

        jocConfigurationDBLayer.getFilter().setInstanceId(dbItemInventoryInstance.getId());
        jocConfigurationDBLayer.getFilter().setConfigurationType(configuration.getConfigurationType().name());
        jocConfigurationDBLayer.getFilter().setAccount(configuration.getAccount());

    }

    private JOCDefaultResponse updateConfiguration(String accessToken, Configuration configuration) {
        try {
            connection = Globals.createSosHibernateStatelessConnection("saveConfiguration");

            JOCDefaultResponse jocDefaultResponse = init(accessToken, configuration.getJobschedulerId(), getPermissonsJocCockpit(accessToken).getJobschedulerMaster().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            init(configuration);

            jocConfigurationDBLayer.saveConfiguration(jocConfigurationDbItem, configuration.getShared(), configuration.getConfigurationItem());


            return JOCDefaultResponse.responseStatusJSOk(new Date());
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

    @Override
    public JOCDefaultResponse postSaveConfiguration(String accessToken, Configuration configuration) throws Exception {
        initLogging(API_CALL_SAVE, configuration);
        checkRequiredParameter("configurationItem", configuration.getConfigurationItem());
        return updateConfiguration(accessToken, configuration);
    }

    @Override
    public JOCDefaultResponse postReadConfiguration(String accessToken, Configuration configuration) throws Exception {
        initLogging(API_CALL_READ, configuration);
        try {
            connection = Globals.createSosHibernateStatelessConnection("readConfiguration");

            JOCDefaultResponse jocDefaultResponse = init(accessToken, configuration.getJobschedulerId(), getPermissonsJocCockpit(accessToken).getJobschedulerMaster().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            init(configuration);
            List<JocConfigurationDbItem> l = jocConfigurationDBLayer.getJocConfigurationList(1);
            if (l.size() > 0) {
                jocConfigurationDbItem = l.get(0);
                configuration.setConfigurationItem(jocConfigurationDbItem.getConfigurationItem());
                configuration.setShared(jocConfigurationDbItem.getShared());

            } else {
                configuration = new Configuration();
            }

            Configuration200 entity = new Configuration200();
            entity.setDeliveryDate(new Date());
            entity.setConfiguration(configuration);

            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }

    }

    @Override
    public JOCDefaultResponse postDeleteConfiguration(String accessToken, Configuration configuration) throws Exception {

        try {
            connection = Globals.createSosHibernateStatelessConnection("deleteConfiguration");
            initLogging(API_CALL_DELETE, configuration);
            JOCDefaultResponse jocDefaultResponse = init(accessToken, configuration.getJobschedulerId(), getPermissonsJocCockpit(accessToken).getJobschedulerMaster().getView()
                    .isStatus());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            init(configuration);
            jocConfigurationDBLayer.deleteConfiguration();
            return JOCDefaultResponse.responseStatusJSOk(new Date());
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

    @Override
    public JOCDefaultResponse postShareConfiguration(String accessToken, Configuration configuration) throws Exception {
        initLogging(API_CALL_SHARE, configuration);
        configuration.setShared(true);
        return updateConfiguration(accessToken, configuration);
    }

    @Override
    public JOCDefaultResponse postMakePrivate(String accessToken, Configuration configuration) throws Exception {
        initLogging(API_CALL_PRIVATE, configuration);
        configuration.setShared(false);
        return updateConfiguration(accessToken, configuration);
    }

}