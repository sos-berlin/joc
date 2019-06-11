package com.sos.joc.conditions.impl;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.xalan.xsltc.compiler.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.eventhandlerservice.db.DBItemEvent;
import com.sos.eventhandlerservice.db.DBLayerEvents;
import com.sos.eventhandlerservice.db.FilterEvents;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.conditions.resource.IConditionEventsResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.conditions.ConditionEvent;
import com.sos.joc.model.conditions.ConditionEvents;
import com.sos.joc.model.conditions.ConditionEventsFilter;

@Path("conditions")
public class ConditionEventsImpl extends JOCResourceImpl implements IConditionEventsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConditionEventsImpl.class);
    private static final String API_CALL = "./conditions/edit/in_condition";

    @Override
    public JOCDefaultResponse getEvents(String accessToken, ConditionEventsFilter conditionEventsFilter) throws Exception {
        SOSHibernateSession sosHibernateSession = null;

        try {

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, conditionEventsFilter, accessToken, conditionEventsFilter.getMasterId(),
                    getPermissonsJocCockpit(conditionEventsFilter.getMasterId(), accessToken).getCondition().getView().isEventlist());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            DBLayerEvents dbLayerEvents = new DBLayerEvents(sosHibernateSession);
            FilterEvents filter = new FilterEvents();
            filter.setOutConditionId(conditionEventsFilter.getOutConditionId());

            if (conditionEventsFilter.getSession() == null || conditionEventsFilter.getSession().isEmpty()) {
                filter.setSession(com.sos.eventhandlerservice.classes.Constants.getSession());
            } else {
                filter.setSession(conditionEventsFilter.getSession());

            }

            filter.setWorkflow(conditionEventsFilter.getWorkflow());
            List<DBItemEvent> listOfEvents = dbLayerEvents.getEventsList(filter, conditionEventsFilter.getLimit());

            ConditionEvents conditionEvents = new ConditionEvents();
            conditionEvents.setDeliveryDate(new Date());
            conditionEvents.setSession(conditionEventsFilter.getSession());

            for (DBItemEvent dbItemEvent : listOfEvents) {
                ConditionEvent conditionEvent = new ConditionEvent();
                conditionEvent.setEvent(dbItemEvent.getEvent());
                conditionEvent.setOutConditionId(dbItemEvent.getOutConditionId());
                conditionEvent.setSession(dbItemEvent.getSession());
                conditionEvent.setWorkflow(dbItemEvent.getWorkflow());
                conditionEvents.getConditionEvents().add(conditionEvent);
            }
            return JOCDefaultResponse.responseStatus200(conditionEvents);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    @Override
    public JOCDefaultResponse addEvent(String accessToken, ConditionEvent conditionEvent) throws Exception {
        SOSHibernateSession sosHibernateSession = null;

        try {

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, conditionEvent, accessToken, conditionEvent.getMasterId(), getPermissonsJocCockpit(
                    conditionEvent.getMasterId(), accessToken).getCondition().getChange().getEvents().isAdd());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            if (conditionEvent.getSession() == null || conditionEvent.getSession().isEmpty()) {
                conditionEvent.setSession(com.sos.eventhandlerservice.classes.Constants.getSession());
            } else {
                conditionEvent.setSession(conditionEvent.getSession());
            }

            this.checkRequiredParameter("masterId", conditionEvent.getMasterId());
            this.checkRequiredParameter("workflow", conditionEvent.getWorkflow());
            this.checkRequiredParameter("outConditionId", conditionEvent.getOutConditionId());
            this.checkRequiredParameter("event", conditionEvent.getEvent());

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            DBLayerEvents dbLayerEvents = new DBLayerEvents(sosHibernateSession);
            sosHibernateSession.setAutoCommit(false);

            DBItemEvent itemEvent = new DBItemEvent();
            itemEvent.setCreated(new Date());
            itemEvent.setEvent(conditionEvent.getEvent());
            itemEvent.setSession(conditionEvent.getSession());
            itemEvent.setOutConditionId(conditionEvent.getOutConditionId());
            itemEvent.setWorkflow(conditionEvent.getWorkflow());

            sosHibernateSession.beginTransaction();
            dbLayerEvents.store(itemEvent);
            sosHibernateSession.commit();

            Globals.commit(sosHibernateSession);
            notifyEventHandler(accessToken);
            return JOCDefaultResponse.responseStatus200(conditionEvent);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    @Override
    public JOCDefaultResponse deleteEvent(String accessToken, ConditionEvent conditionEvent) throws Exception {
        SOSHibernateSession sosHibernateSession = null;

        try {

            JOCDefaultResponse jocDefaultResponse = init(API_CALL, conditionEvent, accessToken, conditionEvent.getMasterId(), getPermissonsJocCockpit(
                    conditionEvent.getMasterId(), accessToken).getCondition().getChange().getEvents().isAdd());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            this.checkRequiredParameter("masterId", conditionEvent.getMasterId());
            this.checkRequiredParameter("workflow", conditionEvent.getWorkflow());
            this.checkRequiredParameter("outConditionId", conditionEvent.getOutConditionId());
            this.checkRequiredParameter("event", conditionEvent.getEvent());

            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL);

            if (conditionEvent.getSession() == null || conditionEvent.getSession().isEmpty()) {
                conditionEvent.setSession(com.sos.eventhandlerservice.classes.Constants.getSession());
            } else {
                conditionEvent.setSession(conditionEvent.getSession());
            }

            DBLayerEvents dbLayerEvents = new DBLayerEvents(sosHibernateSession);
            FilterEvents filter = new FilterEvents();
            filter.setEvent(conditionEvent.getEvent());
            filter.setSession(conditionEvent.getSession());
            filter.setWorkflow(conditionEvent.getWorkflow());

            sosHibernateSession.setAutoCommit(false);

            sosHibernateSession.beginTransaction();
            dbLayerEvents.delete(filter);
            sosHibernateSession.commit();

            Globals.commit(sosHibernateSession);
            notifyEventHandler(accessToken);
            return JOCDefaultResponse.responseStatus200(conditionEvent);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    private void notifyEventHandler(String accessToken) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(EditOutConditionsImpl.class.getName());
        customEventsUtil.addEvent("InitConditionResolver");
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
    }
}