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
    private JocConfigurationDbItem oldJocConfigurationDbItem;

    private void init(Configuration configuration) throws Exception {
        jocConfigurationDbItem = new JocConfigurationDbItem();
        jocConfigurationDBLayer = new JocConfigurationDbLayer(connection);

        List<JocConfigurationDbItem> l = jocConfigurationDBLayer.getJocConfigurationList(1);
        if (l.size() > 0) {
            oldJocConfigurationDbItem = l.get(0);
        }

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

        jocConfigurationDbItem.setShared(configuration.getShared());
        jocConfigurationDbItem.setConfigurationItem(configuration.getConfigurationItem());
        jocConfigurationDbItem.setConfigurationType(configurationType);
        jocConfigurationDbItem.setAccount(configuration.getAccount());

        jocConfigurationDBLayer.getFilter().setConfigurationType(configuration.getConfigurationType().name());
        jocConfigurationDBLayer.getFilter().setAccount(configuration.getAccount());

    }

    @Override
    public JOCDefaultResponse postSaveConfiguration(String accessToken, Configuration configuration) throws Exception {

        try {
            boolean editShare = getPermissonsJocCockpit(accessToken).getJOCConfigurations().getShare().isEditContent();
            boolean editPrivate = getPermissonsJocCockpit(accessToken).getJOCConfigurations().getPrivate().isEditContent();
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_SAVE, configuration, accessToken, configuration.getJobschedulerId(), 
                    (editShare || editPrivate));
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            connection = Globals.createSosHibernateStatelessConnection("saveConfiguration");
            init(configuration);

            if (oldJocConfigurationDbItem != null && oldJocConfigurationDbItem.getShared()) {
                if (!editShare) {
                    return accessDeniedResponse();
                }
            } else {
                if (!editPrivate) {
                    return accessDeniedResponse();
                }
            }
            checkRequiredParameter("configurationItem", configuration.getConfigurationItem());

            jocConfigurationDbItem.setInstanceId(dbItemInventoryInstance.getId());
            jocConfigurationDBLayer.getFilter().setInstanceId(dbItemInventoryInstance.getId());
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
    public JOCDefaultResponse postReadConfiguration(String accessToken, Configuration configuration) throws Exception {
        try {
            boolean viewShare = getPermissonsJocCockpit(accessToken).getJOCConfigurations().getShare().isView();
            boolean viewPrivate = getPermissonsJocCockpit(accessToken).getJOCConfigurations().getPrivate().isView();
            
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_READ, configuration, accessToken, configuration.getJobschedulerId(), 
                    (viewShare || viewPrivate));
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection("readConfiguration");
            init(configuration);

            if (oldJocConfigurationDbItem != null && oldJocConfigurationDbItem.getShared()) {
                if (!viewShare) {
                    return accessDeniedResponse();
                }
            } else {
                if (!viewPrivate) {
                    return accessDeniedResponse();
                }
            }

            jocConfigurationDbItem.setInstanceId(dbItemInventoryInstance.getId());
            jocConfigurationDBLayer.getFilter().setInstanceId(dbItemInventoryInstance.getId());

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
            boolean deleteShare = getPermissonsJocCockpit(accessToken).getJOCConfigurations().getShare().isDelete();
            boolean deletePrivate = getPermissonsJocCockpit(accessToken).getJOCConfigurations().getPrivate().isDelete();
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_DELETE, configuration, accessToken, configuration.getJobschedulerId(), 
                    (deleteShare || deletePrivate));
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            connection = Globals.createSosHibernateStatelessConnection("deleteConfiguration");
            init(configuration);

            if (oldJocConfigurationDbItem != null && oldJocConfigurationDbItem.getShared()) {
                if (!deleteShare) {
                    return accessDeniedResponse();
                }
            } else {
                if (!deletePrivate) {
                    return accessDeniedResponse();
                }
            }

            jocConfigurationDbItem.setInstanceId(dbItemInventoryInstance.getId());
            jocConfigurationDBLayer.getFilter().setInstanceId(dbItemInventoryInstance.getId());

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

        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_SHARE, configuration, accessToken, configuration.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getJOCConfigurations().getPrivate().isMakeShared());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection("saveConfiguration");
            init(configuration);

            jocConfigurationDbItem.setInstanceId(dbItemInventoryInstance.getId());
            jocConfigurationDBLayer.getFilter().setInstanceId(dbItemInventoryInstance.getId());

            jocConfigurationDBLayer.saveConfiguration(jocConfigurationDbItem, true, configuration.getConfigurationItem());

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
    public JOCDefaultResponse postMakePrivate(String accessToken, Configuration configuration) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_PRIVATE, configuration, accessToken, configuration.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getJOCConfigurations().getShare().isMakePrivate());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection("saveConfiguration");
            init(configuration);
            jocConfigurationDbItem.setInstanceId(dbItemInventoryInstance.getId());
            jocConfigurationDBLayer.getFilter().setInstanceId(dbItemInventoryInstance.getId());
            jocConfigurationDBLayer.saveConfiguration(jocConfigurationDbItem, false, configuration.getConfigurationItem());

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
}