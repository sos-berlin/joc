package com.sos.joc.configurations.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.joc.db.JocConfigurationDbItem;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JocCockpitProperties;
import com.sos.joc.configurations.resource.IJocConfigurationsResource;
import com.sos.joc.db.configuration.JocConfigurationDbLayer;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.configuration.Configuration;
import com.sos.joc.model.configuration.ConfigurationObjectType;
import com.sos.joc.model.configuration.ConfigurationType;
import com.sos.joc.model.configuration.Configurations;
import com.sos.joc.model.configuration.ConfigurationsDeleteFilter;
import com.sos.joc.model.configuration.ConfigurationsFilter;

@Path("configurations")
public class JocConfigurationsResourceImpl extends JOCResourceImpl implements IJocConfigurationsResource {

    private static final String API_CALL = "./configurations";
    private static final String API_CALL_DELETE = "./configurations/delete";

    @Override
    public JOCDefaultResponse postConfigurations(String xAccessToken, String accessToken, ConfigurationsFilter configurationsFilter)
            throws Exception {
        return postConfigurations(getAccessToken(xAccessToken, accessToken), configurationsFilter);
    }
    
    @Override
    public JOCDefaultResponse postConfigurationsDelete(String xAccessToken, String accessToken, ConfigurationsDeleteFilter configurationsFilter)
            throws Exception {
        return postConfigurationsDelete(getAccessToken(xAccessToken, accessToken), configurationsFilter);
    }

    public JOCDefaultResponse postConfigurations(String accessToken, ConfigurationsFilter configurationsFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, configurationsFilter, accessToken, configurationsFilter.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("jobschedulerId", configurationsFilter.getJobschedulerId());

            String objectType = null;
            if (configurationsFilter.getObjectType() != null) {
                objectType = configurationsFilter.getObjectType().name();
            }

            String configurationType = null;
            if (configurationsFilter.getConfigurationType() != null) {
                configurationType = configurationsFilter.getConfigurationType().name();
                if (configurationsFilter.getConfigurationType() == ConfigurationType.PROFILE) {
                    String userName = getJobschedulerUser(accessToken).getSosShiroCurrentUser().getUsername();
                    if (configurationsFilter.getAccount() == null || configurationsFilter.getAccount().isEmpty()) {
                        configurationsFilter.setAccount(userName);
                    } else if (!configurationsFilter.getAccount().equalsIgnoreCase(userName)) {
                        throw new JobSchedulerBadRequestException("You can only read your own profile.");
                    }
                }
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            JocConfigurationDbLayer jocConfigurationDBLayer = new JocConfigurationDbLayer(connection);

            jocConfigurationDBLayer.getFilter().setObjectType(objectType);
            jocConfigurationDBLayer.getFilter().setSchedulerId(configurationsFilter.getJobschedulerId());
            jocConfigurationDBLayer.getFilter().setConfigurationType(configurationType);
            jocConfigurationDBLayer.getFilter().setAccount(configurationsFilter.getAccount());
            jocConfigurationDBLayer.getFilter().setShared(configurationsFilter.getShared());

            List<JocConfigurationDbItem> listOfJocConfigurationDbItem = jocConfigurationDBLayer.getJocConfigurationList(0);
            Configurations configurations = new Configurations();
            List<Configuration> listOfConfigurations = new ArrayList<Configuration>();
            // cleanup wrongfully duplicated Profile entries
            listOfJocConfigurationDbItem = cleanupProfileDuplicates(listOfJocConfigurationDbItem);
            
            //if profile is new then try default_profile_account from joc.properties if exists
            if (configurationsFilter.getConfigurationType() == ConfigurationType.PROFILE && (listOfJocConfigurationDbItem == null
                    || listOfJocConfigurationDbItem.isEmpty() || listOfJocConfigurationDbItem.get(0).getConfigurationItem() == null
                    || listOfJocConfigurationDbItem.get(0).getConfigurationItem().isEmpty())) {
                if (Globals.sosShiroProperties == null) {
                    Globals.sosShiroProperties = new JocCockpitProperties();
                }
                String defaultProfileAccount = Globals.sosShiroProperties.getProperty("default_profile_account", "").trim();
                String currentAccount = configurationsFilter.getAccount();
                if (!defaultProfileAccount.isEmpty() && !defaultProfileAccount.equalsIgnoreCase(currentAccount)) {
                    jocConfigurationDBLayer.getFilter().setAccount(defaultProfileAccount);
                    listOfJocConfigurationDbItem = cleanupProfileDuplicates(jocConfigurationDBLayer.getJocConfigurationList(0));
                    //if default_profile_account profile exist then store it for new user
                    if (listOfJocConfigurationDbItem != null && !listOfJocConfigurationDbItem.isEmpty()) {
                        listOfJocConfigurationDbItem.get(0).setAccount(currentAccount);
                        listOfJocConfigurationDbItem.get(0).setId(null);
                        jocConfigurationDBLayer.saveOrUpdateConfiguration(listOfJocConfigurationDbItem.get(0));
                    }
                }
            }
            
            if (listOfJocConfigurationDbItem != null) {
                boolean sharePerm = getPermissonsJocCockpit(configurationsFilter.getJobschedulerId(), accessToken).getJOCConfigurations().getShare()
                        .getView().isStatus();
                for (JocConfigurationDbItem jocConfigurationDbItem : listOfJocConfigurationDbItem) {
                    Configuration configuration = new Configuration();
                    configuration.setAccount(jocConfigurationDbItem.getAccount());
                    configuration.setConfigurationType(ConfigurationType.fromValue(jocConfigurationDbItem.getConfigurationType()));
                    configuration.setJobschedulerId(configurationsFilter.getJobschedulerId());
                    configuration.setName(jocConfigurationDbItem.getName());
                    if (jocConfigurationDbItem.getObjectType() != null) {
                        configuration.setObjectType(ConfigurationObjectType.fromValue(jocConfigurationDbItem.getObjectType()));
                    }
                    configuration.setShared(jocConfigurationDbItem.getShared());
                    configuration.setId(jocConfigurationDbItem.getId());
                    if (jocConfigurationDbItem.getConfigurationItem() != null && !jocConfigurationDbItem.getConfigurationItem().isEmpty()) {
                        configuration.setConfigurationItem(jocConfigurationDbItem.getConfigurationItem());
                    }
                    if (!jocConfigurationDbItem.getShared() || sharePerm) {
                        listOfConfigurations.add(configuration);
                    }
                }
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
    
    public JOCDefaultResponse postConfigurationsDelete(String accessToken, ConfigurationsDeleteFilter configurationsFilter) throws Exception {
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_DELETE, configurationsFilter, accessToken, "", getPermissonsJocCockpit("",
                    accessToken).getJobschedulerMaster().getAdministration().isEditPermissions());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            checkRequiredParameter("accounts", configurationsFilter.getAccounts());
            connection = Globals.createSosHibernateStatelessConnection(API_CALL_DELETE);
            JocConfigurationDbLayer jocConfigurationDBLayer = new JocConfigurationDbLayer(connection);
            connection.setAutoCommit(false);
            Globals.beginTransaction(connection);
            jocConfigurationDBLayer.deleteConfigurations(configurationsFilter.getAccounts());
            Globals.commit(connection);

            return JOCDefaultResponse.responseStatusJSOk(Date.from(Instant.now()));
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

    private List<JocConfigurationDbItem> cleanupProfileDuplicates(List<JocConfigurationDbItem> configurationDbItems) {
        if (configurationDbItems == null) {
            return null;
        }
        Comparator<JocConfigurationDbItem> itemComparator = new Comparator<JocConfigurationDbItem>() {

            @Override
            public int compare(JocConfigurationDbItem o1, JocConfigurationDbItem o2) {
                return o1.getId().compareTo(o2.getId());
            }
        };
        configurationDbItems.sort(itemComparator);
        Iterator<JocConfigurationDbItem> iterator = configurationDbItems.iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            if (iterator.next().getConfigurationType().equals(ConfigurationType.PROFILE.name())) {
                if (!found) {
                    found = true;
                } else {
                    iterator.remove();
                }
            }
        }
        return configurationDbItems;
    }

}