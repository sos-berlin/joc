package com.sos.joc.classes.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.joc.Globals;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.common.JobSchedulerObjectType;
import com.sos.joc.model.event.EventSnapshot;
import com.sos.joc.model.event.JobSchedulerEvent;

public class EventCallableJobSchedulerStateChanged extends EventCallable implements Callable<JobSchedulerEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventCallableJobSchedulerStateChanged.class);
    private final JobSchedulerEvent jobSchedulerEvent;
    private final Long mods;
    private SOSHibernateSession connection = null;

    public EventCallableJobSchedulerStateChanged(JobSchedulerEvent jobSchedulerEvent, Session session, Long mods) {
        super(null, jobSchedulerEvent, null, session, null);
        this.jobSchedulerEvent = jobSchedulerEvent;
        this.mods = mods;
    }

    @Override
    public JobSchedulerEvent call() throws JocException {
        try {
            setStartTime();
            jobSchedulerEvent.getEventSnapshots().addAll(getSchedulerStateChangedEvent());
        } catch (Exception e) {
            jobSchedulerEvent.setEventSnapshots(null);
            throw e;
        } finally {
            Globals.disconnect(connection);
        }
        return jobSchedulerEvent;
    }
    
    public boolean instancesHasChanged() {
        InventoryInstancesDBLayer instanceLayer = null;
        try {
            if (connection == null) {
                connection = Globals.createSosHibernateStatelessConnection("jobSchedulerStateChangedCallable");
                Globals.beginTransaction(connection);
            }
            instanceLayer = new InventoryInstancesDBLayer(connection);
            if (instanceLayer != null) {
                long mods = instanceLayer.getInventoryMods();
                if (this.mods != mods) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            LOGGER.warn(e.toString());
            return false;
        } finally {
            Globals.disconnect(connection);
            connection = null;
        }
    }
    
    private List<EventSnapshot> getSchedulerStateChangedEvent() throws JocException {
        List<EventSnapshot> eventSnapshots = new ArrayList<EventSnapshot>();
        checkTimeout();
        if (instancesHasChanged()) {
            EventSnapshot eventSnapshot = new EventSnapshot();
            eventSnapshot.setEventType("SchedulerStateChanged");
            eventSnapshot.setObjectType(JobSchedulerObjectType.JOBSCHEDULER);
            eventSnapshot.setPath(jobSchedulerEvent.getJobschedulerId());
            eventSnapshots.add(eventSnapshot);
        } else {
            try {
                int delay = Math.min(15000, new Long(getSessionTimeout()).intValue());
                Thread.sleep(delay);
            } catch (InterruptedException e1) {
            }
            eventSnapshots.addAll(getSchedulerStateChangedEvent());
        }
        return eventSnapshots;
    }
}
