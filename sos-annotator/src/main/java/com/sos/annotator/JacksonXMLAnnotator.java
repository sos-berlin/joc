package com.sos.annotator;

import java.util.List;

import org.jsonschema2pojo.AbstractAnnotator;
import org.jsonschema2pojo.Annotator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;

public class JacksonXMLAnnotator extends AbstractAnnotator implements Annotator {

    @Override
    public void propertyField(JFieldVar field, JDefinedClass clazz, String propertyName, JsonNode propertyNode) {
        if (field.type().erasure().equals(field.type().owner().ref(List.class))) {
            field.annotate(JacksonXmlProperty.class).param("localName", getSingularPropertyName(propertyName));
            field.annotate(JacksonXmlElementWrapper.class).param("useWrapping", true).param("localName", propertyName);
        } else {
            field.annotate(JacksonXmlProperty.class).param("localName", propertyName);
        }
    }

    @Override
    public void propertyGetter(JMethod getter, String propertyName) {
        if (getter.type().erasure().equals(getter.type().owner().ref(List.class))) {
            getter.annotate(JacksonXmlProperty.class).param("localName", getSingularPropertyName(propertyName));
        } else {
            getter.annotate(JacksonXmlProperty.class).param("localName", propertyName);
        }
    }

    @Override
    public void propertySetter(JMethod setter, String propertyName) {
        if (setter.listParamTypes()[0].erasure().equals(setter.listParamTypes()[0].owner().ref(List.class))) {
            setter.annotate(JacksonXmlProperty.class).param("localName", getSingularPropertyName(propertyName));
        } else {
            setter.annotate(JacksonXmlProperty.class).param("localName", propertyName);
        }
    }

    private String getSingularPropertyName(String propertyName) {
        if (propertyName.endsWith("ses")) {
            return propertyName.replaceFirst("ses$", "s");
        }
        if (propertyName.endsWith("ies")) {
            return propertyName.replaceFirst("ies$", "y");
        }
        if (propertyName.endsWith("s")) {
            return propertyName.replaceFirst("s$", "");
        }
        return propertyName;
    }

}
