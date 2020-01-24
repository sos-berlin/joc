package com.sos.schema;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ValidatorTest {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorTest.class);

    @Test
    public void test() {
        String s = "{\"jobschedulerId\":\"joc_40444\", \"calendar\":{\"path\":\"/../\\\"/> <script>alert(1)</script> \",\"title\":\"\\\"/><b>test2</b>\",\"category\":\"test\",\"type\":\"WORKING_DAYS\",\"from\":\"2019-12-02\",\"to\":\"2020-1-25\"}}";
        boolean check = false;
        try {
            JsonValidator.validateFailFast(s.getBytes(), "calendar/calendarObjectFilter-schema.json");
            check = true;
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        assertFalse(check);
    }
    
    @Test 
    public void testTypeLoose() {
        String s = "{\"jobschedulerId\":\"joc_40444\", \"id\": \"1\"}";
        boolean check = false;
        try {
            JsonValidator.validateFailFast(s.getBytes(), "calendar/calendarId-schema.json");
            check = true;
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        assertTrue(check);
    }
    
    @Test
    public void testLoadingSchemas() {
        List<String> classNames = new ArrayList<String>();
        for(Entry<String, String> entry : JsonValidator.getClassUriMap().entrySet()) {
            try {
                URI schemaUri = URI.create("classpath:/raml/schemas/" + entry.getValue());
                JsonValidator.getSchema(schemaUri, false);
            } catch (Exception e) {
                //LOGGER.error(e.getMessage());
                classNames.add(entry.getKey());
            }
        }
        assertTrue("Schemas not found for: " + classNames.toString(), classNames.isEmpty());
    }

}
