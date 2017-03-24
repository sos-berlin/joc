//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.03.24 um 02:41:35 PM CET 
//


package com.sos.joc.model.commands;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="terminate">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="timeout" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                 &lt;attribute name="restart" type="{}Yes_no" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "terminate"
})
@XmlRootElement(name = "cluster_member_command")
public class ClusterMemberCommand {

    protected ClusterMemberCommand.Terminate terminate;

    /**
     * Ruft den Wert der terminate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ClusterMemberCommand.Terminate }
     *     
     */
    public ClusterMemberCommand.Terminate getTerminate() {
        return terminate;
    }

    /**
     * Legt den Wert der terminate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ClusterMemberCommand.Terminate }
     *     
     */
    public void setTerminate(ClusterMemberCommand.Terminate value) {
        this.terminate = value;
    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="timeout" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *       &lt;attribute name="restart" type="{}Yes_no" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Terminate {

        @XmlAttribute(name = "timeout")
        protected BigInteger timeout;
        @XmlAttribute(name = "restart")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        protected String restart;

        /**
         * Ruft den Wert der timeout-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getTimeout() {
            return timeout;
        }

        /**
         * Legt den Wert der timeout-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setTimeout(BigInteger value) {
            this.timeout = value;
        }

        /**
         * Ruft den Wert der restart-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRestart() {
            return restart;
        }

        /**
         * Legt den Wert der restart-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRestart(String value) {
            this.restart = value;
        }

    }

}
