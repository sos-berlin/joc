package com.sos.joc.classes.schedule;

import org.w3c.dom.Element;

import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.configuration.ConfigurationStatus;
import com.sos.joc.model.schedule.ScheduleState;
import com.sos.joc.model.schedule.ScheduleStateText;
import com.sos.joc.model.schedule.ScheduleV;

public class ScheduleVolatile extends ScheduleV {

    private static final String ATTRIBUTE_ACTIVE = "active";
    private static final String ATTRIBUTE_NOW_COVERED_BY_SCHEDULE = "now_covered_by_schedule";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_PATH = "path";

    private JOCXmlCommand jocXmlCommand;
    private Element scheduleElement;

    public ScheduleVolatile(JOCXmlCommand jocXmlCommand, Element scheduleElement) {
        super();
        this.jocXmlCommand = jocXmlCommand;
        this.scheduleElement = scheduleElement;
    }

    public void setValues() {
//        String path = jocXmlCommand.getAttribute(ATTRIBUTE_PATH);
//        String activeValue = jocXmlCommand.getAttribute(ATTRIBUTE_ACTIVE);
        String path = scheduleElement.getAttribute(ATTRIBUTE_PATH);
        String activeValue = scheduleElement.getAttribute(ATTRIBUTE_ACTIVE);
        setSurveyDate(jocXmlCommand.getSurveyDate());
        setConfigurationStatus(ConfigurationStatus.getConfigurationStatus(scheduleElement));
//        setName(jocXmlCommand.getAttribute(ATTRIBUTE_NAME));
        setName(scheduleElement.getAttribute(ATTRIBUTE_NAME));
        setPath(path);
        ScheduleState state = new ScheduleState();
        if ("yes".equals(activeValue)) {
            state.setSeverity(0);
            state.set_text(ScheduleStateText.ACTIVE);
        } else {
            state.setSeverity(1);
            state.set_text(ScheduleStateText.INACTIVE);
        }
        setState(state);
        // TODO: ERROR NPE when schedule doesn´t have a substitute, some checks on NULL could be helpfull, but where? 
//        setSubstitutedBy(jocXmlCommand.getAttribute(ATTRIBUTE_NOW_COVERED_BY_SCHEDULE));
    }
}
