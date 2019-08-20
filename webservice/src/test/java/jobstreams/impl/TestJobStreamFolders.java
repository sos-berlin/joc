package jobstreams.impl;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import com.sos.eventhandlerservice.classes.Constants;
import com.sos.eventhandlerservice.db.DBItemInCondition;
import com.sos.eventhandlerservice.db.DBItemOutCondition;
import com.sos.eventhandlerservice.db.DBItemOutConditionWithEvent;
import com.sos.eventhandlerservice.db.DBLayerInConditions;
import com.sos.eventhandlerservice.db.DBLayerOutConditions;
import com.sos.eventhandlerservice.db.FilterInConditions;
import com.sos.eventhandlerservice.db.FilterOutConditions;
import com.sos.hibernate.classes.SOSHibernateFactory;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.hibernate.exceptions.SOSHibernateConfigurationException;
import com.sos.hibernate.exceptions.SOSHibernateException;
import com.sos.hibernate.exceptions.SOSHibernateFactoryBuildException;
import com.sos.hibernate.exceptions.SOSHibernateOpenSessionException;

public class TestJobStreamFolders {

	private SOSHibernateSession getSession(String confFile) throws SOSHibernateFactoryBuildException,
			SOSHibernateOpenSessionException, SOSHibernateConfigurationException {
		SOSHibernateFactory sosHibernateFactory = new SOSHibernateFactory(confFile);
		sosHibernateFactory.addClassMapping(Constants.getConditionsClassMapping());
		sosHibernateFactory.build();
		return sosHibernateFactory.openStatelessSession();
	}

	@Test
	@Ignore
	public void test() throws SOSHibernateException {
		SOSHibernateSession sosHibernateSession = getSession("src/test/resources/joc/hibernate.cfg.xml");
		Map<String, Set<String>> mapOfJobStream2Folders = new HashMap<String, Set<String>>();
		DBLayerInConditions dbLayerInConditions = new DBLayerInConditions(sosHibernateSession);
		FilterInConditions filterInConditions = new FilterInConditions();
		filterInConditions.setJobSchedulerId("scheduler_joc_cockpit");
		List<DBItemInCondition> listOfInConditions = dbLayerInConditions.getSimpleInConditionsList(filterInConditions,
				0);

		for (DBItemInCondition dbItemInCondition : listOfInConditions) {
			String jobStream = dbItemInCondition.getJobStream();
			if (mapOfJobStream2Folders.get(jobStream) == null) {
				Set<String> listOfFolders = new LinkedHashSet<String>();
				mapOfJobStream2Folders.put(jobStream, listOfFolders);
			}
			mapOfJobStream2Folders.get(jobStream).add(Paths.get(dbItemInCondition.getJob()).getParent().toString());
		}

		DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
		FilterOutConditions filterOutConditions = new FilterOutConditions();
		filterOutConditions.setJobSchedulerId("scheduler_joc_cockpit");
		List<DBItemOutCondition> listOfOutConditions = dbLayerOutConditions	.getSimpleOutConditionsList(filterOutConditions, 0);
		for (DBItemOutCondition dbItemOutCondition : listOfOutConditions) {
			String jobStream = dbItemOutCondition.getJobStream();
			if (mapOfJobStream2Folders.get(jobStream) == null) {
				Set<String> listOfFolders = new LinkedHashSet<String>();
				mapOfJobStream2Folders.put(jobStream, listOfFolders);
			}
			mapOfJobStream2Folders.get(jobStream).add(Paths.get(dbItemOutCondition.getJob()).getParent().toString());
		}
	}

}
