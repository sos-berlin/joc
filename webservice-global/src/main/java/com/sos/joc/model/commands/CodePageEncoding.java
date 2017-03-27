//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.03.24 um 02:41:35 PM CET 
//


package com.sos.joc.model.commands;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für Code_page_encoding.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="Code_page_encoding">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="oem"/>
 *     &lt;enumeration value="cp437"/>
 *     &lt;enumeration value="cp850"/>
 *     &lt;enumeration value="latin1"/>
 *     &lt;enumeration value="none"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Code_page_encoding")
@XmlEnum
public enum CodePageEncoding {

    @XmlEnumValue("oem")
    OEM("oem"),
    @XmlEnumValue("cp437")
    CP_437("cp437"),
    @XmlEnumValue("cp850")
    CP_850("cp850"),
    @XmlEnumValue("latin1")
    LATIN_1("latin1"),
    @XmlEnumValue("none")
    NONE("none");
    private final String value;

    CodePageEncoding(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CodePageEncoding fromValue(String v) {
        for (CodePageEncoding c: CodePageEncoding.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
