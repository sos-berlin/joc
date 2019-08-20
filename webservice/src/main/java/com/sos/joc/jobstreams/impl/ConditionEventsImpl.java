package com.sos.joc.jobstreams.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sos.classes.CustomEventsUtil;
import com.sos.eventhandlerservice.db.DBItemEvent;
import com.sos.eventhandlerservice.db.DBItemOutCondition;
import com.sos.eventhandlerservice.db.DBLayerEvents;
import com.sos.eventhandlerservice.db.DBLayerOutConditions;
import com.sos.eventhandlerservice.db.FilterEvents;
import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.jobstreams.resource.IConditionEventsResource;
import com.sos.joc.model.jobstreams.ConditionEvent;
import com.sos.joc.model.jobstreams.ConditionEvents;
import com.sos.joc.model.jobstreams.ConditionEventsFilter;
import com.sos.joc.exceptions.JocException;

@Path("conditions")
public class ConditionEventsImpl extends JOCResourceImpl implements IConditionEventsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConditionEventsImpl.class);
    private static final String API_CALL_EVENTLIST = "./conditions/eventlist";
    private static final String API_CALL_ADD_EVENT = "./conditions/add_event";
    private static final String API_CALL_DELETE_EVENT = "./conditions/delete_event";

    @Override
    public JOCDefaultResponse getEvents(String accessToken, ConditionEventsFilter conditionEventsFilter) throws Exception {
        SOSHibernateSession sosHibernateSession = null;

        try {

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_EVENTLIST, conditionEventsFilter, accessToken, conditionEventsFilter.getJobschedulerId(),
                    getPermissonsJocCockpit(conditionEventsFilter.getJobschedulerId(), accessToken).getJobStream().getView().isEventlist());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            sosHibernateSession = Globals.createSosHibernateStatelessConnection(API_CALL_EVENTLIST);

            DBLayerEvents dbLayerEvents = new DBLayerEvents(sosHibernateSession);
            DBLayerOutConditions dbLayerOutConditions = new DBLayerOutConditions(sosHibernateSession);
            FilterEvents filter = new FilterEvents();
            filter.setOutConditionId(conditionEventsFilter.getOutConditionId());

            if (conditionEventsFilter.getSession() == null || conditionEventsFilter.getSession().isEmpty()) {
                filter.setSession(com.sos.eventhandlerservice.classes.Constants.getSession());
            } else {
                filter.setSession(conditionEventsFilter.getSession());

            }

            filter.setJobStream(conditionEventsFilter.getJobStream());
            List<DBItemEvent> listOfEvents = dbLayerEvents.getEventsList(filter, conditionEventsFilter.getLimit());
            ConditionEvents conditionEvents = new ConditionEvents();
            conditionEvents.setDeliveryDate(new Date());
            conditionEvents.setSession(filter.getSession());

            for (DBItemEvent dbItemEvent : listOfEvents) {
                ConditionEvent conditionEvent = new ConditionEvent();
                DBItemOutCondition dbItemOutCondition = dbLayerOutConditions.getOutConditionsDbItem(dbItemEvent.getOutConditionId());
                if (dbItemOutCondition != null) {
                    conditionEvent.setEvent(dbItemEvent.getEvent());
                    conditionEvent.setOutConditionId(dbItemEvent.getOutConditionId());
                    conditionEvent.setSession(dbItemEvent.getSession());
                    conditionEvent.setJobStream(dbItemEvent.getJobStream());
                    conditionEvent.setPath(dbItemOutCondition.getPath());
                    if (conditionEventsFilter.getPath() == null || conditionEventsFilter.getPath().isEmpty() || dbItemOutCondition.getPath().equals(
                            conditionEventsFilter.getPath())) {
                        conditionEvents.getConditionEvents().add(conditionEvent);
                    }
                }
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

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_ADD_EVENT, conditionEvent, accessToken, conditionEvent.getJobschedulerId(), getPermissonsJocCockpit(
                    conditionEvent.getJobschedulerId(), accessToken).getJobStream().getChange().getEvents().isAdd());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            if (conditionEvent.getSession() == null || conditionEvent.getSession().isEmpty()) {
                conditionEvent.setSession(com.sos.eventhandlerservice.classes.Constants.getSession());
            } else {
                conditionEvent.setSession(conditionEvent.getSession());
            }

            this.checkRequiredParameter("jobStream", conditionEvent.getJobStream());
            this.checkRequiredParameter("outConditionId", conditionEvent.getOutConditionId());
            this.checkRequiredParameter("event", conditionEvent.getEvent());

            FilterEvents filter = new FilterEvents();
            filter.setEvent(conditionEvent.getEvent());
            filter.setSession(conditionEvent.getSession());
            filter.setJobStream(conditionEvent.getJobStream());
            filter.setOutConditionId(conditionEvent.getOutConditionId());

            notifyEventHandler(accessToken, "AddEvent", filter);
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

            JOCDefaultResponse jocDefaultResponse = init(API_CALL_DELETE_EVENT, conditionEvent, accessToken, conditionEvent.getJobschedulerId(), getPermissonsJocCockpit(
                    conditionEvent.getJobschedulerId(), accessToken).getJobStream().getChange().getEvents().isAdd());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            this.checkRequiredParameter("jobStream", conditionEvent.getJobStream());
            this.checkRequiredParameter("outConditionId", conditionEvent.getOutConditionId());
            this.checkRequiredParameter("event", conditionEvent.getEvent());

            if (conditionEvent.getSession() == null || conditionEvent.getSession().isEmpty()) {
                conditionEvent.setSession(com.sos.eventhandlerservice.classes.Constants.getSession());
            } else {
                conditionEvent.setSession(conditionEvent.getSession());
            }

            FilterEvents filter = new FilterEvents();
            filter.setEvent(conditionEvent.getEvent());
            filter.setSession(conditionEvent.getSession());
            filter.setJobStream(conditionEvent.getJobStream());

            notifyEventHandler(accessToken, "RemoveEvent", filter);
            return JOCDefaultResponse.responseStatus200(conditionEvent);

        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(sosHibernateSession);
        }
    }

    private void notifyEventHandler(String accessToken, String eventKey, FilterEvents filter) throws JsonProcessingException, JocException {
        CustomEventsUtil customEventsUtil = new CustomEventsUtil(ConditionEventsImpl.class.getName());
        Map<String, String> parameters = new HashMap<String, String>();
        if (filter != null) {
            parameters.put("outConditionId", String.valueOf(filter.getOutConditionId()));
            parameters.put("event", filter.getEvent());
            parameters.put("session", filter.getSession());
            parameters.put("jobStream", filter.getJobStream());
        }
        customEventsUtil.addEvent(eventKey, parameters);
        String notifyCommand = customEventsUtil.getEventCommandAsXml();
        com.sos.joc.classes.JOCXmlCommand jocXmlCommand = new com.sos.joc.classes.JOCXmlCommand(dbItemInventoryInstance);
        jocXmlCommand.executePost(notifyCommand, accessToken);
    }
}