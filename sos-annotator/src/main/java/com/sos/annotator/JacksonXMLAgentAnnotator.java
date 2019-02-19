package com.sos.annotator;

import java.util.List;

import org.jsonschema2pojo.AbstractAnnotator;
import org.jsonschema2pojo.Annotator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;

public class JacksonXMLAgentAnnotator extends AbstractAnnotator implements Annotator {

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
        String xmlPropertyName = camelCaseToLowerUnderscore(propertyName);
        if (field.type().erasure().equals(field.type().owner().ref(List.class))) {
            field.annotate(JacksonXmlProperty.class).param("localName", xmlPropertyName);
            field.annotate(JacksonXmlElementWrapper.class).param("useWrapping", true).param("localName", xmlPropertyName);
        } else {
            field.annotate(JacksonXmlProperty.class).param("localName", xmlPropertyName).param("isAttribute", true);
        }
    }

    @Override
    public void propertyGetter(JMethod getter, String propertyName) {
        if (getter.type().erasure().equals(getter.type().owner().ref(List.class))) {
            getter.annotate(JacksonXmlProperty.class).param("localName", camelCaseToLowerUnderscore(propertyName));
        } else {
            getter.annotate(JacksonXmlProperty.class).param("localName", camelCaseToLowerUnderscore(propertyName)).param("isAttribute", true);
        }
    }

    @Override
    public void propertySetter(JMethod setter, String propertyName) {
        if (setter.listParamTypes()[0].erasure().equals(setter.listParamTypes()[0].owner().ref(List.class))) {
            setter.annotate(JacksonXmlProperty.class).param("localName", camelCaseToLowerUnderscore(propertyName));
        } else {
            setter.annotate(JacksonXmlProperty.class).param("localName", camelCaseToLowerUnderscore(propertyName)).param("isAttribute", true);
        }
    }

    private String camelCaseToLowerUnderscore(String propertyName) {
        return propertyName.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

}
