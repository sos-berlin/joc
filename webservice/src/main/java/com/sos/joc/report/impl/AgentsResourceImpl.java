package com.sos.joc.report.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.report.Agent;
import com.sos.joc.model.report.Agents;
import com.sos.joc.model.report.AgentsFilter;
import com.sos.joc.report.resource.IAgentsResource;

@Path("report")
public class AgentsResourceImpl extends JOCResourceImpl implements IAgentsResource {

    private static final String API_CALL = "./report/agents";

    @Override
    public JOCDefaultResponse postAgentsReport(String accessToken, AgentsFilter agentsFilter) throws Exception {
        SOSHibernateSession connection = null;

        try {
            //TODO permission
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, agentsFilter, accessToken, "", true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            List<Agent> agents = new ArrayList<Agent>();
            
            //TODO set agents
            
            Agents entity = new Agents();
            entity.setAgents(agents);
            entity.setDeliveryDate(Date.from(Instant.now()));
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

}