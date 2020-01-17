package com.sos.schema;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.joc.model.calendar.CalendarObjectFilter;


public class ValidatorTest {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorTest.class);

    @Test
    public void test() {
        String s = "{\"jobschedulerId\":\"joc_40444\",\"calendar\":{\"path\":\"/../\\\"/> <script>alert(1)</script> \",\"title\":\"\\\"/><b>test2</b>\",\"category\":\"test\",\"type\":\"WORKING_DAYS\",\"from\":\"2019-12-02\",\"to\":\"2019-12-25\"}}";
        boolean check = false;
        try {
            JsonValidator.validateFailFast(s.getBytes(), CalendarObjectFilter.class);
            check = true;
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        assertFalse(check);
    }

}
