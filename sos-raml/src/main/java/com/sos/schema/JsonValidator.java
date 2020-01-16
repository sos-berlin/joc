package com.sos.schema;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaValidatorsConfig;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.sos.exception.SOSInvalidDataException;
import com.sos.joc.model.audit.AuditLogFilter;

public class JsonValidator {
    
    private static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final SpecVersion.VersionFlag JSONDRAFT = SpecVersion.VersionFlag.V4;
    private static final Path RESOURCE_DIR = Paths.get("src", "main", "resources", "raml", "schemas");
    
    private static final Map<Class<?>, URI> CLASS_URI_MAPPING = Collections.unmodifiableMap(new HashMap<Class<?>, URI>() {

        private static final long serialVersionUID = 1L;

        {
            put(AuditLogFilter.class, RESOURCE_DIR.resolve("audit/auditLogFilter-schema.json").toUri());
        }
    });

    
    /**
     * Validation which raises all errors 
     * 
     * @param json
     * @param schemaPath - path relative to ./resources/raml/schemas directory
     * @throws IOException
     * @throws SOSInvalidDataException
     */
    public static void validate(byte[] json, String schemaPath) throws IOException, SOSInvalidDataException {
        validate(json, RESOURCE_DIR.resolve(schemaPath).toUri(), false);
    }
    
    /**
     * Validation which raises all errors
     * 
     * @param json
     * @param clazz
     * @throws IOException
     * @throws SOSInvalidDataException
     */
    public static void validate(byte[] json, Class<?> clazz) throws IOException, SOSInvalidDataException {
        validate(json, mapUri(clazz), false);
    }
    
    /**
     * Validation which raises all errors
     * 
     * @param json
     * @param schemaUri
     * @throws IOException
     * @throws SOSInvalidDataException
     */
    public static void validate(byte[] json, URI schemaUri) throws IOException, SOSInvalidDataException {
        validate(json, schemaUri, false);
    }
    
    /**
     * Fast validation which stops after first error
     * 
     * @param json
     * @param schemaPath - path relative to ./resources/raml/schemas directory
     * @throws IOException
     * @throws SOSInvalidDataException
     */
    public static void validateFailFast(byte[] json, String schemaPath) throws IOException, SOSInvalidDataException {
        validate(json, RESOURCE_DIR.resolve(schemaPath).toUri(), true);
    }
    
    /**
     * Fast validation which stops after first error
     * 
     * @param json
     * @param clazz
     * @throws IOException
     * @throws SOSInvalidDataException
     */
    public static void validateFailFast(byte[] json, Class<?> clazz) throws IOException, SOSInvalidDataException {
        validate(json, mapUri(clazz), true);
    }
    
    /**
     * Fast validation which stops after first error
     * 
     * @param json
     * @param schemaUri
     * @throws IOException
     * @throws SOSInvalidDataException
     */
    public static void validateFailFast(byte[] json, URI schemaUri) throws IOException, SOSInvalidDataException {
        validate(json, schemaUri, true);
    }
    
    private static URI mapUri(Class<?> clazz) {
        URI schemaUri = CLASS_URI_MAPPING.get(clazz);
        if (schemaUri == null) {
            throw new IllegalArgumentException("Json Validation: Unkown class " + clazz.getSimpleName());
        }
        return schemaUri;
    }
    
    private static JsonSchema getSchema(URI schemaUri, boolean failFast) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(JSONDRAFT);
        if (failFast) {
            SchemaValidatorsConfig config = new SchemaValidatorsConfig();
            config.setFailFast(true);
            return factory.getSchema(schemaUri, config);
        } else {
            return factory.getSchema(schemaUri);
        }
    }
    
    private static void validate(byte[] json, URI schemaUri, boolean failFast) throws IOException, SOSInvalidDataException {
        JsonSchema schema = getSchema(schemaUri, failFast);
        Set<ValidationMessage> errors = schema.validate(MAPPER.readTree(json));
        if (errors != null && !errors.isEmpty()) {
            throw new SOSInvalidDataException(errors.toString());
        }
    }

}
