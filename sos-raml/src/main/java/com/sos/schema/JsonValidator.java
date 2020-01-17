package com.sos.schema;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonMetaSchema;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.NonValidationKeyword;
import com.networknt.schema.SchemaValidatorsConfig;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.sos.exception.SOSInvalidDataException;
import com.sos.joc.model.audit.AuditLogFilter;
import com.sos.joc.model.calendar.CalendarDatesFilter;
import com.sos.joc.model.calendar.CalendarDocuFilter;
import com.sos.joc.model.calendar.CalendarId;
import com.sos.joc.model.calendar.CalendarObjectFilter;
import com.sos.joc.model.calendar.CalendarRenameFilter;
import com.sos.schema.exception.SOSJsonSchemaException;

public class JsonValidator {

    private static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final SpecVersion.VersionFlag JSONDRAFT = SpecVersion.VersionFlag.V4;
    //private static final Path RESOURCE_DIR = Paths.get("classpath:raml/schema", "schemas");
    private static final List<NonValidationKeyword> NON_VALIDATION_KEYS = Arrays.asList(new NonValidationKeyword("javaType"),
            new NonValidationKeyword("xmlElement"), new NonValidationKeyword("isXmlCData"), new NonValidationKeyword("isXmlAttribute"));
    private static final JsonSchemaFactory FACTORY_V4 = JsonSchemaFactory.builder(JsonSchemaFactory.getInstance(JSONDRAFT)).addMetaSchema(
            JsonMetaSchema.builder(JsonMetaSchema.getV4().getUri(), JsonMetaSchema.getV4()).addKeywords(NON_VALIDATION_KEYS).build()).build();

    private static final Map<Class<?>, String> CLASS_URI_MAPPING = Collections.unmodifiableMap(new HashMap<Class<?>, String>() {

        private static final long serialVersionUID = 1L;

        {
            put(AuditLogFilter.class, "audit/auditLogFilter-schema.json");
            put(CalendarDatesFilter.class, "calendar/calendarDatesFilter-schema.json");
            put(CalendarObjectFilter.class, "calendar/calendarObjectFilter-schema.json");
            put(CalendarRenameFilter.class, "calendar/calendarRenameFilter-schema.json");
            put(CalendarDocuFilter.class, "calendar/calendarDocuFilter-schema.json");
            put(CalendarId.class, "calendar/calendarId-schema.json");
        }
    });

    /** Validation which raises all errors
     * 
     * @param json
     * @param schemaPath - path relative to ./resources/raml/schemas directory
     * @throws IOException
     * @throws URISyntaxException 
     * @throws SOSInvalidDataException */
    public static void validate(byte[] json, String schemaPath) throws IOException, SOSJsonSchemaException {
        validate(json, URI.create("classpath:/raml/schemas/" + schemaPath), false);
    }

    /** Validation which raises all errors
     * 
     * @param json
     * @param clazz
     * @throws IOException
     * @throws URISyntaxException 
     * @throws SOSInvalidDataException */
    public static void validate(byte[] json, Class<?> clazz) throws IOException, SOSJsonSchemaException {
        validate(json, URI.create("classpath:/raml/schemas/" + CLASS_URI_MAPPING.get(clazz)), false);
    }

    /** Validation which raises all errors
     * 
     * @param json
     * @param schemaUri
     * @throws IOException
     * @throws SOSInvalidDataException */
    public static void validate(byte[] json, URI schemaUri) throws IOException, SOSJsonSchemaException {
        validate(json, schemaUri, false);
    }

    /** Fast validation which stops after first error
     * 
     * @param json
     * @param schemaPath - path relative to ./resources/raml/schemas directory
     * @throws IOException
     * @throws URISyntaxException 
     * @throws SOSInvalidDataException */
    public static void validateFailFast(byte[] json, String schemaPath) throws IOException, SOSJsonSchemaException {
        validate(json, URI.create("classpath:/raml/schemas/" + schemaPath), true);
    }

    /** Fast validation which stops after first error
     * 
     * @param json
     * @param clazz
     * @throws IOException
     * @throws URISyntaxException 
     * @throws SOSInvalidDataException */
    public static void validateFailFast(byte[] json, Class<?> clazz) throws IOException, SOSJsonSchemaException {
        validate(json, URI.create("classpath:/raml/schemas/" + CLASS_URI_MAPPING.get(clazz)), true);
    }

    /** Fast validation which stops after first error
     * 
     * @param json
     * @param schemaUri
     * @throws IOException
     * @throws SOSInvalidDataException */
    public static void validateFailFast(byte[] json, URI schemaUri) throws IOException, SOSJsonSchemaException {
        validate(json, schemaUri, true);
    }

    private static JsonSchema getSchema(URI schemaUri, boolean failFast) {
        if (failFast) {
            SchemaValidatorsConfig config = new SchemaValidatorsConfig();
            config.setFailFast(true);
            return FACTORY_V4.getSchema(schemaUri, config);
        } else {
            return FACTORY_V4.getSchema(schemaUri);
        }
    }

    private static void validate(byte[] json, URI schemaUri, boolean failFast) throws IOException, SOSJsonSchemaException {
        JsonSchema schema = getSchema(schemaUri, failFast);
        Set<ValidationMessage> errors = schema.validate(MAPPER.readTree(json));
        if (errors != null && !errors.isEmpty()) {
            if (failFast) {
                throw new SOSJsonSchemaException(errors.iterator().next().toString());
            } else {
                throw new SOSJsonSchemaException(errors.toString());
            }
        }
    }

}
