package com.sos.annotator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsonschema2pojo.AbstractAnnotator;
import org.jsonschema2pojo.Annotator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sun.codemodel.JAnnotatable;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JType;

public class JacksonXMLJoeConfigurationAnnotator extends AbstractAnnotator implements Annotator {

    private Map<String, String> xmlPropertyNames = new HashMap<String, String>();
    private Map<String, Boolean> xmlPropertyAttributes = new HashMap<String, Boolean>();

    @Override
    public void propertyInclusion(JDefinedClass clazz, JsonNode schema) {
        if (schema.has("xmlElement")) {
            clazz.annotate(JacksonXmlRootElement.class).param("localName", schema.get("xmlElement").asText());
        } else {
            clazz.annotate(JacksonXmlRootElement.class).param("localName", camelCaseToLowerUnderscore(clazz.name()));
        }
    }

    @Override
    public void propertyField(JFieldVar field, JDefinedClass clazz, String propertyName, JsonNode propertyNode) {
        xmlPropertyNames.put(propertyName, camelCaseToLowerUnderscore(propertyName));
        xmlPropertyAttributes.put(propertyName, isAttribute(field));

        if (isCollection(field)) {
            if (propertyNode.has("xmlElement")) {
                xmlPropertyNames.put(propertyName, propertyNode.get("xmlElement").asText());
            }
            field.annotate(JacksonXmlElementWrapper.class).param("useWrapping", false);
        }
        setLocalNameXMLProperty(field, propertyName);

    }

    @Override
    public void propertyGetter(JMethod getter, String propertyName) {
        setLocalNameXMLProperty(getter, propertyName);
    }

    @Override
    public void propertySetter(JMethod setter, String propertyName) {
        setLocalNameXMLProperty(setter, propertyName);
    }

    private void setLocalNameXMLProperty(JAnnotatable annotator, String propertyName) {
        annotator.annotate(JacksonXmlProperty.class).param("localName", xmlPropertyNames.get(propertyName)).param("isAttribute", xmlPropertyAttributes
                .get(propertyName));
    }

    private String camelCaseToLowerUnderscore(String propertyName) {
        return propertyName.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    private Boolean isAttribute(JFieldVar field) {
        JType jtype = field.type().erasure();
        JCodeModel jModel = field.type().owner();
        JClass jClassString = jModel.ref(String.class);
        JClass jClassInteger = jModel.ref(Integer.class);
        JClass jClassLong = jModel.ref(Long.class);
        JClass jClassBoolean = jModel.ref(Boolean.class);
        JClass jClassDate = jModel.ref(Date.class);
        return jtype.equals(jClassString) || jtype.equals(jClassInteger) || jtype.equals(jClassLong) || jtype.equals(jClassBoolean) || jtype.equals(
                jClassDate);
    }

    private Boolean isCollection(JFieldVar field) {
        JType jtype = field.type().erasure();
        JCodeModel jModel = field.type().owner();
        return jtype.equals(jModel.ref(List.class)) || jtype.equals(jModel.ref(Set.class));
    }

}
