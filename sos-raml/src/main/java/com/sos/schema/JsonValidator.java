package com.sos.schema;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaValidatorsConfig;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.sos.exception.SOSInvalidDataException;

public class JsonValidator {
    
    private static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final SpecVersion.VersionFlag JSONDRAFT = SpecVersion.VersionFlag.V4;
    private static final Path RESOURCE_DIR = Paths.get("src", "main", "resources", "raml", "schemas");

    
    /**
     * Validation which raises all errors 
     * 
     * @param json
     * @param schemaPath - path relative to ./resources/raml/schemas directory
     * @throws IOException
     * @throws SOSInvalidDataException
     */
    public static void validate(byte[] json, String schemaPath) throws IOException, SOSInvalidDataException {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(JSONDRAFT);
        JsonSchema schema = factory.getSchema(RESOURCE_DIR.resolve(schemaPath).toUri());
        Set<ValidationMessage> errors = schema.validate(MAPPER.readTree(json));
        if (errors != null && !errors.isEmpty()) {
            throw new SOSInvalidDataException(errors.toString());
        }
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
        SchemaValidatorsConfig config = new SchemaValidatorsConfig();
        config.setFailFast(true);
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(JSONDRAFT);
        JsonSchema schema = factory.getSchema(RESOURCE_DIR.resolve(schemaPath).toUri(), config);
        Set<ValidationMessage> errors = schema.validate(MAPPER.readTree(json));
        if (errors != null && !errors.isEmpty()) {
            throw new SOSInvalidDataException(errors.iterator().next().toString());
        }
    }

}
