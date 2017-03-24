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
 *       &lt;attribute name="all_schedulers" type="{}Yes_no" />
 *       &lt;attribute name="restart" type="{}Yes_no" />
 *       &lt;attribute name="timeout" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="continue_exclusive_operation" type="{}Yes_no" />
 *       &lt;attribute name="cluster_member_id" type="{}String" />
 *       &lt;attribute name="delete_dead_entry" type="{}Yes_no" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "terminate")
public class Terminate {

    @XmlAttribute(name = "all_schedulers")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String allSchedulers;
    @XmlAttribute(name = "restart")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String restart;
    @XmlAttribute(name = "timeout")
    protected BigInteger timeout;
    @XmlAttribute(name = "continue_exclusive_operation")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String continueExclusiveOperation;
    @XmlAttribute(name = "cluster_member_id")
    protected String clusterMemberId;
    @XmlAttribute(name = "delete_dead_entry")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String deleteDeadEntry;

    /**
     * Ruft den Wert der allSchedulers-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAllSchedulers() {
        return allSchedulers;
    }

    /**
     * Legt den Wert der allSchedulers-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAllSchedulers(String value) {
        this.allSchedulers = value;
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
     * Ruft den Wert der continueExclusiveOperation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContinueExclusiveOperation() {
        return continueExclusiveOperation;
    }

    /**
     * Legt den Wert der continueExclusiveOperation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContinueExclusiveOperation(String value) {
        this.continueExclusiveOperation = value;
    }

    /**
     * Ruft den Wert der clusterMemberId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClusterMemberId() {
        return clusterMemberId;
    }

    /**
     * Legt den Wert der clusterMemberId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClusterMemberId(String value) {
        this.clusterMemberId = value;
    }

    /**
     * Ruft den Wert der deleteDeadEntry-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeleteDeadEntry() {
        return deleteDeadEntry;
    }

    /**
     * Legt den Wert der deleteDeadEntry-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeleteDeadEntry(String value) {
        this.deleteDeadEntry = value;
    }

}
