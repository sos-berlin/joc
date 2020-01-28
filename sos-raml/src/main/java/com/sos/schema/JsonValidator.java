package com.sos.schema;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonMetaSchema;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaException;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.NonValidationKeyword;
import com.networknt.schema.SchemaValidatorsConfig;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.sos.exception.SOSInvalidDataException;
import com.sos.schema.exception.SOSJsonSchemaException;

public class JsonValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonValidator.class);
    private static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final SpecVersion.VersionFlag JSONDRAFT = SpecVersion.VersionFlag.V4;
    // private static final Path RESOURCE_DIR = Paths.get("classpath:raml/schema", "schemas");
    private static final List<NonValidationKeyword> NON_VALIDATION_KEYS = Arrays.asList(new NonValidationKeyword("javaType"),
            new NonValidationKeyword("javaInterfaces"), new NonValidationKeyword("extends"), new NonValidationKeyword("xmlElement"),
            new NonValidationKeyword("isXmlCData"), new NonValidationKeyword("isXmlAttribute"));
    private static final JsonSchemaFactory FACTORY_V4 = JsonSchemaFactory.builder(JsonSchemaFactory.getInstance(JSONDRAFT)).addMetaSchema(
            JsonMetaSchema.builder(JsonMetaSchema.getV4().getUri(), JsonMetaSchema.getV4()).addKeywords(NON_VALIDATION_KEYS).build()).build();

    private static final Map<String, String> CLASS_URI_MAPPING = Collections.unmodifiableMap(new HashMap<String, String>() {

        private static final long serialVersionUID = 1L;

        {
            put("ConfigurationEdit", "joe/agentCluster/agentConfigurationEdit-schema.json");
            put("AuditLogFilter", "audit/auditLogFilter-schema.json");
            put("AuditParams", "audit/auditParams-schema.json");

            put("CalendarDatesFilter", "calendar/calendarDatesFilter-schema.json");
            put("CalendarObjectFilter", "calendar/calendarObjectFilter-schema.json");
            put("CalendarRenameFilter", "calendar/calendarRenameFilter-schema.json");
            put("CalendarDocuFilter", "calendar/calendarDocuFilter-schema.json");
            put("CalendarId", "calendar/calendarId-schema.json");
            put("CalendarImportFilter", "calendar/calendarImportFilter-schema.json");
            put("CalendarsFilter", "calendar/calendarsFilter-schema.json");

            put("Configuration", "configuration/configuration-schema.json");
            put("ConfigurationsFilter", "configuration/configurationsFilter-schema.json");
            put("ConfigurationsDeleteFilter", "configuration/configurationsDeleteFilter-schema.json");

            put("DocumentationShowFilter", "docu/documentationShow-schema.json");
            put("DocumentationFilter", "docu/documentationFilter-schema.json");
            put("DocumentationsFilter", "docu/documentationsFilter-schema.json");
            put("DocumentationImport", "docu/documentationImport-schema.json");

            put("CheckEvent", "event/checkCustomEvent-schema.json");
            put("EventsFilter", "event/customEventsFilter-schema.json");
            put("RegisterEvent", "event/register-schema.json");
            put("ModifyEvent", "event/modifyCustomEvent-schema.json");
            put("EventIdsFilter", "event/customDeleteEventsFilter-schema.json");

            put("JobFilter", "job/jobFilter-schema.json");
            put("JobConfigurationFilter", "job/jobConfigurationFilter-schema.json");
            put("JobDocuFilter", "job/jobDocuFilter-schema.json");
            put("TaskHistoryFilter", "job/jobHistoryFilter-schema.json");
            put("JobsFilter", "job/jobsFilter-schema.json");
            put("ModifyJobs", "job/modifyJobs-schema.json");
            put("StartJobs", "job/startJobs-schema.json");
            put("TaskFilter", "job/taskFilter-schema.json");
            put("ModifyTasks", "job/modifyTasks-schema.json");

            put("JobChainFilter", "jobChain/jobChainFilter-schema.json");
            put("JobChainConfigurationFilter", "jobChain/jobChainConfigurationFilter-schema.json");
            put("JobChainDocuFilter", "jobChain/jobChainDocuFilter-schema.json");
            put("JobChainHistoryFilter", "jobChain/jobChainHistoryFilter-schema.json");
            put("JobChainsFilter", "jobChain/jobChainsFilter-schema.json");
            put("ModifyJobChains", "jobChain/modifyJobChains-schema.json");
            put("ModifyJobChainNodes", "jobChain/modifyNode-schema.json");

            put("LockConfigurationFilter", "lock/lockConfigurationFilter-schema.json");
            put("LockDocuFilter", "lock/lockDocuFilter-schema.json");
            put("LocksFilter", "lock/locksFilter-schema.json");

            put("ProcessClassConfigurationFilter", "processClass/processClassConfigurationFilter-schema.json");
            put("ProcessClassDocuFilter", "processClass/processClassDocuFilter-schema.json");
            put("ProcessClassesFilter", "processClass/processClassesFilter-schema.json");

            put("ScheduleConfigurationFilter", "schedule/scheduleConfigurationFilter-schema.json"); // check
            put("ScheduleDocuFilter", "schedule/scheduleDocuFilter-schema.json");
            put("ScheduleFilter", "schedule/scheduleFilter-schema.json");
            put("ModifyRunTime", "schedule/modifyRuntime-schema.json");
            put("SchedulesFilter", "schedule/schedulesFilter-schema.json");

            put("OrderConfigurationFilter", "order/orderConfigurationFilter-schema.json");
            put("OrderDocuFilter", "order/orderDocuFilter-schema.json");
            put("OrderFilter", "order/orderFilter-schema.json");
            put("OrderHistoryFilter", "order/orderFilterWithHistoryId-schema.json");
            put("ModifyOrders", "order/modifyOrders-schema.json");
            put("OrdersFilter", "order/ordersFilter-schema.json");

            put("FileFilter", "yade/fileFilter-schema.json");
            put("FilesFilter", "yade/filesFilter-schema.json");
            put("TransferFilter", "yade/transferFilter-schema.json");
            put("ModifyTransfer", "yade/modifyTransfer-schema.json");
            put("ModifyTransfers", "yade/modifyTransfers-schema.json");

            put("HostPortParameter", "jobscheduler/urlParam-schema.json");
            put("TimeoutParameter", "jobscheduler/timeoutParam-schema.json");
            put("HostPortTimeOutParameter", "jobscheduler/urlTimeoutParam-schema.json");
            put("AgentClusterFilter", "jobscheduler/agentClusterFilter-schema.json");
            put("AgentFilter", "jobscheduler/agentFilter-schema.json");

            put("JobSchedulerId", "common/jobSchedulerId-schema.json");
            put("JOClog", "joc/filename-schema.json");
            put("TreeFilter", "tree/treeFilter-schema.json");
            put("SecurityConfiguration", "security/security-configuration-schema.json");
            put("AgentsFilter", "report/agentsFilter-schema.json");
            put("RunTimePlanFilter", "plan/runTimePlanFilter-schema.json");
            put("PlanFilter", "plan/planFilter-schema.json");

            put("ConditionEventsFilter", "jobstreams/conditionEventsFilter-schema.json");
            put("ConditionEvent", "jobstreams/conditionEvent-schema.json");
            put("InConditions", "jobstreams/inconditions-schema.json");
            put("OutConditions", "jobstreams/outconditions-schema.json");
            put("JobStreams", "jobstreams/jobStreams-schema.json");
            put("JobStreamFilter", "jobstreams/jobstreamFilter-schema.json");
            put("ResetJobStream", "jobstreams/reset_jobstream-schema.json");

            put("ApplyConfiguration", "xmleditor/apply/apply-configuration-schema.json");
            put("AssignSchemaConfiguration", "xmleditor/schema/assign/schema-assign-configuration-schema.json");
            put("DeleteAll", "xmleditor/delete/all/delete-all-schema.json");
            put("DeleteDraft", "xmleditor/delete/delete-draft-schema.json");
            put("DeployConfiguration", "xmleditor/deploy/deploy-configuration-schema.json");
            put("ReadConfiguration", "xmleditor/read/read-configuration-schema.json");
            put("ReassignSchemaConfiguration", "xmleditor/schema/reassign/schema-reassign-configuration-schema.json");
            put("RenameConfiguration", "xmleditor/rename/rename-configuration-schema.json");
            put("StoreConfiguration", "xmleditor/store/store-configuration-schema.json");
            put("ValidateConfiguration", "xmleditor/validate/validate-configuration-schema.json");
            put("Xml2JsonConfiguration", "xmleditor/xml2json/xml2json-configuration-schema.json");

            put("FilterDeploy", "joe/common/filter-deploy-schema.json");
            put("Filter", "joe/common/filter-schema.json");
            put("JSObjectEdit", "joe/common/jsObjectEdit-schema.json");
            put("JobsWizardFilter", "joe/wizard/jobsFilter-schema.json");
            put("JobWizardFilter", "joe/wizard/jobFilter-schema.json");
            put("LockFilter", "joe/lock/lockFilter-schema.json");
        }
    });

    /** Validation which raises all errors
     * 
     * @param json
     * @param schemaPath - path relative to ./resources/raml/schemas directory
     * @throws IOException
     * @throws SOSInvalidDataException */
    public static void validate(byte[] json, String schemaPath) throws IOException, SOSJsonSchemaException {
        if (schemaPath != null) {
            validate(json, URI.create("classpath:/raml/schemas/" + schemaPath), false);
        }
    }

    /** Validation which raises all errors
     * 
     * @param json
     * @param clazz
     * @throws IOException
     * @throws SOSInvalidDataException */
    public static void validate(byte[] json, Class<?> clazz) throws IOException, SOSJsonSchemaException {
        validate(json, getSchemaPath(clazz));
    }

    /** Validation which raises all errors
     * 
     * @param json
     * @param schemaUri
     * @throws IOException
     * @throws SOSInvalidDataException */
    public static void validate(byte[] json, URI schemaUri) throws IOException, SOSJsonSchemaException {
        if (schemaUri != null) {
            validate(json, schemaUri, false);
        }
    }

    /** Fast validation which stops after first error
     * 
     * @param json
     * @param schemaPath - path relative to ./resources/raml/schemas directory
     * @throws IOException
     * @throws SOSInvalidDataException */
    public static void validateFailFast(byte[] json, String schemaPath) throws IOException, SOSJsonSchemaException {
        if (schemaPath != null) {
            validate(json, URI.create("classpath:/raml/schemas/" + schemaPath), true);
        }
    }

    /** Fast validation which stops after first error
     * 
     * @param json
     * @param clazz
     * @throws IOException
     * @throws SOSInvalidDataException */
    public static void validateFailFast(byte[] json, Class<?> clazz) throws IOException, SOSJsonSchemaException {
        validateFailFast(json, getSchemaPath(clazz));
    }

    /** Fast validation which stops after first error
     * 
     * @param json
     * @param schemaUri
     * @throws IOException
     * @throws SOSInvalidDataException */
    public static void validateFailFast(byte[] json, URI schemaUri) throws IOException, SOSJsonSchemaException {
        if (schemaUri != null) {
            validate(json, schemaUri, true);
        }
    }

    // for testing
    protected static Map<String, String> getClassUriMap() {
        return CLASS_URI_MAPPING;
    }

    // protected for testing
    protected static JsonSchema getSchema(URI schemaUri, boolean failFast) {
        SchemaValidatorsConfig config = new SchemaValidatorsConfig();
        config.setTypeLoose(true);
        if (failFast) {
            config.setFailFast(true);
        }
        return FACTORY_V4.getSchema(schemaUri, config);
    }

    private static String getSchemaPath(Class<?> clazz) {
        String schemaPath = CLASS_URI_MAPPING.get(clazz.getSimpleName());
        if (schemaPath == null) {
            LOGGER.warn("JSON Validation impossible: no schema specified for " + clazz.getName());
            return null;
        } else {
            return schemaPath;
        }
    }

    private static void validate(byte[] json, URI schemaUri, boolean failFast) throws IOException, SOSJsonSchemaException {
        JsonSchema schema = getSchema(schemaUri, failFast);
        Set<ValidationMessage> errors;
        try {
            errors = schema.validate(MAPPER.readTree(json));
            if (errors != null && !errors.isEmpty()) {
                throw new SOSJsonSchemaException(errors.toString());
            }
        } catch (JsonParseException e) {
            throw e;
        } catch (JsonSchemaException e) {
            if (e.getCause() == null || e.getCause().getClass().isInstance(e)) {
                throw new SOSJsonSchemaException(e.getMessage());
            }
            LOGGER.warn("JSON Validation impossible: " + e.toString());
        }
    }

}
