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
 * <p>Java-Klasse für Job_chain_node.Action.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="Job_chain_node.Action">
 *   &lt;restriction base="{}String">
 *     &lt;enumeration value="process"/>
 *     &lt;enumeration value="stop"/>
 *     &lt;enumeration value="next_state"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Job_chain_node.Action")
@XmlEnum
public enum JobChainNodeAction {

    @XmlEnumValue("process")
    PROCESS("process"),
    @XmlEnumValue("stop")
    STOP("stop"),
    @XmlEnumValue("next_state")
    NEXT_STATE("next_state");
    private final String value;

    JobChainNodeAction(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static JobChainNodeAction fromValue(String v) {
        for (JobChainNodeAction c: JobChainNodeAction.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
