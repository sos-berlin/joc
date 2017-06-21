package com.sos.joc.classes.schedule;

import java.time.Instant;
import java.util.Date;

import org.w3c.dom.Element;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBItemInventorySchedule;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.db.inventory.schedules.InventorySchedulesDBLayer;
import com.sos.joc.exceptions.DBMissingDataException;
import com.sos.joc.model.common.ConfigurationState;
import com.sos.joc.model.common.ConfigurationStateText;
import com.sos.joc.model.schedule.ScheduleState;
import com.sos.joc.model.schedule.ScheduleStateText;
import com.sos.joc.model.schedule.ScheduleV;

public class ScheduleVolatile extends ScheduleV {

    public ScheduleVolatile(Date surveyDate, Element scheduleElement, InventorySchedulesDBLayer dbLayer,
            DBItemInventoryInstance dbItemInventoryInstance) {
        setSurveyDate(surveyDate);
        setName(scheduleElement.getAttribute("name"));
        setPath(scheduleElement.getAttribute("path"));
        ConfigurationState confState = ConfigurationStatus.getConfigurationStatus(scheduleElement);
        ScheduleState state = new ScheduleState();
        state.setSeverity(0);
        state.set_text(ScheduleStateText.ACTIVE);
        Date now = Date.from(Instant.now());
        try {
            if (dbLayer != null) {
                DBItemInventorySchedule scheduleFromDb = dbLayer.getSchedule(getPath(), dbItemInventoryInstance.getId());
                if (scheduleFromDb == null) {
                    throw new DBMissingDataException(String.format("Schedule '%1$s' doesn't exit in the invemtory.", getPath()));
                }
                if (scheduleFromDb.getSubstituteId() != DBLayer.DEFAULT_ID) {
                    if (scheduleFromDb.getSubstituteValidFrom().after(now) || scheduleFromDb.getSubstituteValidTo().compareTo(now) <= 0) {
                        state.setSeverity(1);
                        state.set_text(ScheduleStateText.INACTIVE);
                    }
                    if (confState == null) {
                        String overlapedSchedule = dbLayer.hasOverlapedSubstitutedBySchedule(scheduleFromDb.getId(), scheduleFromDb.getSubstituteId(), scheduleFromDb.getSubstituteValidFrom(), scheduleFromDb.getSubstituteValidTo());
                        if (overlapedSchedule != null) {
                            confState = new ConfigurationState();
                            confState.setSeverity(2);
                            confState.set_text(ConfigurationStateText.ERROR_IN_CONFIGURATION_FILE);
                            confState.setMessage(String.format("Schedule '%1$s' overlaps schedule '%2$s'", scheduleFromDb.getName(), overlapedSchedule));
                        }
                    }
                } else {
                    if (dbLayer.hasActiveSubstitutedBySchedule(scheduleFromDb.getId())) {
                        state.setSeverity(1);
                        state.set_text(ScheduleStateText.INACTIVE);
                    }
                }
            }
        } catch (Exception e) {
            state.setSeverity(3);
            state.set_text(ScheduleStateText.UNKNOWN);
        }
        if (confState != null && confState.getSeverity() == 2) {
            state.setSeverity(2); 
        }
        setConfigurationStatus(confState);
        setState(state);
    }
}
