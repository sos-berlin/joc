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
import javax.xml.bind.annotation.XmlElement;
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
 *       &lt;sequence>
 *         &lt;element ref="{}params" minOccurs="0"/>
 *         &lt;element name="xml_payload" type="{}Xml_payload" minOccurs="0"/>
 *         &lt;element name="run_time" type="{}run_time" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="job_chain" use="required" type="{}Path" />
 *       &lt;attribute name="order" use="required" type="{}Order_id" />
 *       &lt;attribute name="priority" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="title" type="{}String" />
 *       &lt;attribute name="state" type="{}String" />
 *       &lt;attribute name="setback" type="{}No" />
 *       &lt;attribute name="suspended" type="{}Yes_no" />
 *       &lt;attribute name="at" type="{}Date_time_with_now" />
 *       &lt;attribute name="end_state" type="{}String" />
 *       &lt;attribute name="action">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="reset"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "params",
    "xmlPayload",
    "runTime"
})
@XmlRootElement(name = "modify_order")
public class ModifyOrder {

    protected Params params;
    @XmlElement(name = "xml_payload")
    protected XmlPayload xmlPayload;
    @XmlElement(name = "run_time")
    protected RunTime runTime;
    @XmlAttribute(name = "job_chain", required = true)
    protected String jobChain;
    @XmlAttribute(name = "order", required = true)
    protected String order;
    @XmlAttribute(name = "priority")
    protected BigInteger priority;
    @XmlAttribute(name = "title")
    protected String title;
    @XmlAttribute(name = "state")
    protected String state;
    @XmlAttribute(name = "setback")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String setback;
    @XmlAttribute(name = "suspended")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String suspended;
    @XmlAttribute(name = "at")
    protected String at;
    @XmlAttribute(name = "end_state")
    protected String endState;
    @XmlAttribute(name = "action")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String action;

    /**
     * Ruft den Wert der params-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Params }
     *     
     */
    public Params getParams() {
        return params;
    }

    /**
     * Legt den Wert der params-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Params }
     *     
     */
    public void setParams(Params value) {
        this.params = value;
    }

    /**
     * Ruft den Wert der xmlPayload-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlPayload }
     *     
     */
    public XmlPayload getXmlPayload() {
        return xmlPayload;
    }

    /**
     * Legt den Wert der xmlPayload-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlPayload }
     *     
     */
    public void setXmlPayload(XmlPayload value) {
        this.xmlPayload = value;
    }

    /**
     * Ruft den Wert der runTime-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RunTime }
     *     
     */
    public RunTime getRunTime() {
        return runTime;
    }

    /**
     * Legt den Wert der runTime-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RunTime }
     *     
     */
    public void setRunTime(RunTime value) {
        this.runTime = value;
    }

    /**
     * Ruft den Wert der jobChain-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobChain() {
        return jobChain;
    }

    /**
     * Legt den Wert der jobChain-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobChain(String value) {
        this.jobChain = value;
    }

    /**
     * Ruft den Wert der order-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrder() {
        return order;
    }

    /**
     * Legt den Wert der order-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrder(String value) {
        this.order = value;
    }

    /**
     * Ruft den Wert der priority-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPriority() {
        return priority;
    }

    /**
     * Legt den Wert der priority-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPriority(BigInteger value) {
        this.priority = value;
    }

    /**
     * Ruft den Wert der title-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Legt den Wert der title-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Ruft den Wert der state-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Legt den Wert der state-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Ruft den Wert der setback-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetback() {
        return setback;
    }

    /**
     * Legt den Wert der setback-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetback(String value) {
        this.setback = value;
    }

    /**
     * Ruft den Wert der suspended-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSuspended() {
        return suspended;
    }

    /**
     * Legt den Wert der suspended-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSuspended(String value) {
        this.suspended = value;
    }

    /**
     * Ruft den Wert der at-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAt() {
        return at;
    }

    /**
     * Legt den Wert der at-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAt(String value) {
        this.at = value;
    }

    /**
     * Ruft den Wert der endState-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndState() {
        return endState;
    }

    /**
     * Legt den Wert der endState-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndState(String value) {
        this.endState = value;
    }

    /**
     * Ruft den Wert der action-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Legt den Wert der action-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
    }

}
