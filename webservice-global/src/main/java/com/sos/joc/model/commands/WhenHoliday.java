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
 * <p>Java-Klasse für When_holiday.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="When_holiday">
 *   &lt;restriction base="{}String">
 *     &lt;enumeration value="suppress"/>
 *     &lt;enumeration value="ignore_holiday"/>
 *     &lt;enumeration value="previous_non_holiday"/>
 *     &lt;enumeration value="next_non_holiday"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "When_holiday")
@XmlEnum
public enum WhenHoliday {

    @XmlEnumValue("suppress")
    SUPPRESS("suppress"),
    @XmlEnumValue("ignore_holiday")
    IGNORE_HOLIDAY("ignore_holiday"),
    @XmlEnumValue("previous_non_holiday")
    PREVIOUS_NON_HOLIDAY("previous_non_holiday"),
    @XmlEnumValue("next_non_holiday")
    NEXT_NON_HOLIDAY("next_non_holiday");
    private final String value;

    WhenHoliday(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static WhenHoliday fromValue(String v) {
        for (WhenHoliday c: WhenHoliday.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
