package com.sos.joc.classes.calendar;

import java.time.Instant;
import java.util.List;

import com.sos.joc.model.calendar.RepetitionText;

public class FrequencyRepetitionResolver {

    public static List<String> resolve(RepetitionText repetition, Integer step, Instant from, Instant to) {
        switch (repetition) {
        case DAILY:
            //TODO
            break;
        case MONTHLY:
            //TODO
            break;
        case WEEKLY:
            //TODO
            break;
        }
        return null;
    }

}
