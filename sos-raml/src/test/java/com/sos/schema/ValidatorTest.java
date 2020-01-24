package com.sos.schema;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ValidatorTest {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorTest.class);
    private Path resourceDirectory = Paths.get("src","test","resources");

    @Test
    public void testPath() {
        String s = "{\"jobschedulerId\":\"joc_40444\", \"calendar\":{\"path\":\"/../\\\"/> <script>alert(1)</script> \",\"category\":\"test\",\"type\":\"WORKING_DAYS\",\"from\":\"2019-12-02\",\"to\":\"2020-01-25\"}}";
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
    public void testNoScriptPattern() {
        Pattern p = Pattern.compile("^(?sm)((?!<script( |>)|<svg/on).)*$");
        String testStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Configurations>\n    <Fragments>\n        <ProtocolFragments/>\n    </Fragments>\n    <Profiles/>\n</Configurations>";
        assertTrue(p.matcher(testStr).matches());
    }
    
    @Test
    public void testNoScript() {
        Path testFile = resourceDirectory.resolve("xmleditor-request.json");
        boolean check = false;
        try {
            JsonValidator.validateFailFast(Files.readAllBytes(testFile), "xmleditor/store/store-configuration-schema.json");
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
