package com.sos.joc.classes.schedule;

import java.util.Date;

import org.w3c.dom.Element;

import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.model.schedule.ScheduleState;
import com.sos.joc.model.schedule.ScheduleStateText;
import com.sos.joc.model.schedule.ScheduleV;

public class ScheduleVolatile extends ScheduleV {

    public ScheduleVolatile(Date surveyDate, Element scheduleElement) {
        setSurveyDate(surveyDate);
        setName(scheduleElement.getAttribute("name"));
        setPath(scheduleElement.getAttribute("path"));
        setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(scheduleElement));
        String activeValue = scheduleElement.getAttribute("active");
        ScheduleState state = new ScheduleState();
        if ("yes".equals(activeValue)) {
            state.setSeverity(0);
            state.set_text(ScheduleStateText.ACTIVE);
        } else {
            state.setSeverity(1);
            state.set_text(ScheduleStateText.INACTIVE);
            String substitutedBy = scheduleElement.getAttribute("now_covered_by_schedule");
            if (substitutedBy != null && !substitutedBy.isEmpty()) {
                setSubstitutedBy(scheduleElement.getAttribute("now_covered_by_schedule"));
            }
        }
        setState(state);
    }
}
