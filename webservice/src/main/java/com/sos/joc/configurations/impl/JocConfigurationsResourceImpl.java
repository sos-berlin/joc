package com.sos.joc.configurations.impl;

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
	private SOSHibernateSession connection = null;

	@Override
	public JOCDefaultResponse postConfigurations(String xAccessToken, String accessToken,
			ConfigurationsFilter configurationsFilter) throws Exception {
		return postConfigurations(getAccessToken(xAccessToken, accessToken), configurationsFilter);
	}

	public JOCDefaultResponse postConfigurations(String accessToken, ConfigurationsFilter configurationsFilter)
			throws Exception {

		try {

			JOCDefaultResponse jocDefaultResponse = init(API_CALL, configurationsFilter, accessToken,
					configurationsFilter.getJobschedulerId(), true);

			if (jocDefaultResponse != null) {
				return jocDefaultResponse;
			}

			connection = Globals.createSosHibernateStatelessConnection(API_CALL);
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
			jocConfigurationDBLayer.getFilter().setSchedulerId(configurationsFilter.getJobschedulerId());
			jocConfigurationDBLayer.getFilter().setConfigurationType(configurationType);
			jocConfigurationDBLayer.getFilter().setAccount(configurationsFilter.getAccount());
			jocConfigurationDBLayer.getFilter().setShared(configurationsFilter.getShared());

			List<JocConfigurationDbItem> listOfJocConfigurationDbItem = jocConfigurationDBLayer
					.getJocConfigurationList(0);
			Configurations configurations = new Configurations();
			ArrayList<Configuration> listOfConfigurations = new ArrayList<Configuration>();
			// cleanup wrongfully duplicated Profile entries
			listOfJocConfigurationDbItem = cleanupProfileDuplicates(listOfJocConfigurationDbItem);
			for (JocConfigurationDbItem jocConfigurationDbItem : listOfJocConfigurationDbItem) {
				Configuration configuration = new Configuration();
				configuration.setAccount(jocConfigurationDbItem.getAccount());
				configuration.setConfigurationType(
						ConfigurationType.fromValue(jocConfigurationDbItem.getConfigurationType()));
				configuration.setJobschedulerId(configurationsFilter.getJobschedulerId());
				configuration.setName(jocConfigurationDbItem.getName());
				if (jocConfigurationDbItem.getObjectType() != null) {
					configuration
							.setObjectType(ConfigurationObjectType.fromValue(jocConfigurationDbItem.getObjectType()));
				}
				configuration.setShared(jocConfigurationDbItem.getShared());
				configuration.setId(jocConfigurationDbItem.getId());
				if (jocConfigurationDbItem.getConfigurationItem() != null
						&& !jocConfigurationDbItem.getConfigurationItem().isEmpty()) {
					configuration.setConfigurationItem(jocConfigurationDbItem.getConfigurationItem());
				}
				if (!jocConfigurationDbItem.getShared()
						|| getPermissonsJocCockpit(configurationsFilter.getJobschedulerId(), accessToken)
								.getJOCConfigurations().getShare().getView().isStatus()) {
					listOfConfigurations.add(configuration);
				}
			}

			configurations.setDeliveryDate(new Date());
			configurations.setConfigurations(listOfConfigurations);

			return JOCDefaultResponse.responseStatus200(configurations);
		} catch (

		JocException e) {
			e.addErrorMetaInfo(getJocError());
			return JOCDefaultResponse.responseStatusJSError(e);
		} catch (Exception e) {
			return JOCDefaultResponse.responseStatusJSError(e, getJocError());
		} finally {
			Globals.disconnect(connection);
		}
	}

	private List<JocConfigurationDbItem> cleanupProfileDuplicates(List<JocConfigurationDbItem> configurationDbItems) {
		Comparator<JocConfigurationDbItem> itemComparator = new Comparator<JocConfigurationDbItem>() {
			@Override
			public int compare(JocConfigurationDbItem o1, JocConfigurationDbItem o2) {
				if (o1.getId() < o2.getId()) {
					return -1;
				} else if (o1.getId() == o2.getId()) {
					return 0;
				} else {
					return 1;
				}
			}
		};
		configurationDbItems.sort(itemComparator);
		Iterator<JocConfigurationDbItem> iterator = configurationDbItems.iterator();
		int count = 0;
		while (iterator.hasNext()) {
			if (iterator.next().getConfigurationType().equals(ConfigurationType.PROFILE.name())) {
				if (count == 0) {
					count++;
				} else {
					iterator.remove();
				}
			}
		}
		return configurationDbItems;
	}

}