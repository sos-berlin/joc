package com.sos.joc.classes.schedule;

import org.w3c.dom.Element;

import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.model.schedule.Schedule_;
import com.sos.joc.model.schedule.State;

public class Schedule {

    private static final String ATTRIBUTE_ACTIVE = "active";
    private static final String ATTRIBUTE_NOW_COVERED_BY_SCHEDULE = "now_covered_by_schedule";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_PATH = "path";

    private JOCXmlCommand jocXmlCommand;
    private Element scheduleElement;
    private Schedule_ schedule;

    public Schedule(JOCXmlCommand jocXmlCommand, Element scheduleElement) {
        super();
        this.jocXmlCommand = jocXmlCommand;
        this.scheduleElement = scheduleElement;
    }

    private void getValues() {
        String path = jocXmlCommand.getAttribute(ATTRIBUTE_PATH);
        String activeValue = jocXmlCommand.getAttribute(ATTRIBUTE_ACTIVE);
        schedule = new Schedule_();
        schedule.setSurveyDate(jocXmlCommand.getSurveyDate());
        schedule.setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(scheduleElement));
        schedule.setName(jocXmlCommand.getAttribute(ATTRIBUTE_NAME));
        schedule.setPath(path);
        State state = new State();
        if ("yes".equals(activeValue)) {
            state.setSeverity(0);
            state.setText(State.Text.ACTIVE);
        } else {
            state.setSeverity(1);
            state.setText(State.Text.INACTIVE);
        }

        schedule.setState(state);
        schedule.setSubstitutedBy(jocXmlCommand.getAttribute(ATTRIBUTE_NOW_COVERED_BY_SCHEDULE));
    }

    public Schedule_ getSchedule() {
        if (scheduleElement != null) {
            getValues();
        }
        return schedule;
    }
}
