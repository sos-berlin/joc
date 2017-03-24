//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.03.24 um 02:41:35 PM CET 
//


package com.sos.joc.model.commands;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;


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
 *         &lt;element ref="{}extensions" minOccurs="0"/>
 *         &lt;element ref="{}note" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="job_chain_node.job_chain" type="{}job_chain_node.job_chain"/>
 *             &lt;choice maxOccurs="unbounded">
 *               &lt;element name="job_chain_node.job_chain" type="{}job_chain_node.job_chain"/>
 *               &lt;element name="job_chain_node.end" type="{}job_chain_node.end"/>
 *             &lt;/choice>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element name="file_order_source" maxOccurs="unbounded" minOccurs="0">
 *               &lt;complexType>
 *                 &lt;complexContent>
 *                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                     &lt;attribute name="directory" use="required" type="{}String" />
 *                     &lt;attribute name="regex" type="{}String" />
 *                     &lt;attribute name="delay_after_error" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *                     &lt;attribute name="repeat">
 *                       &lt;simpleType>
 *                         &lt;union>
 *                           &lt;simpleType>
 *                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *                               &lt;enumeration value="no"/>
 *                             &lt;/restriction>
 *                           &lt;/simpleType>
 *                           &lt;simpleType>
 *                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}positiveInteger">
 *                             &lt;/restriction>
 *                           &lt;/simpleType>
 *                         &lt;/union>
 *                       &lt;/simpleType>
 *                     &lt;/attribute>
 *                     &lt;attribute name="max" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *                     &lt;attribute name="next_state" type="{}String" />
 *                     &lt;attribute name="alert_when_directory_missing" type="{}Yes_no" />
 *                   &lt;/restriction>
 *                 &lt;/complexContent>
 *               &lt;/complexType>
 *             &lt;/element>
 *             &lt;choice maxOccurs="unbounded">
 *               &lt;element name="job_chain_node">
 *                 &lt;complexType>
 *                   &lt;complexContent>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                       &lt;sequence>
 *                         &lt;element ref="{}extensions" minOccurs="0"/>
 *                         &lt;any processContents='lax' namespace='##other' minOccurs="0"/>
 *                         &lt;element name="on_return_codes" minOccurs="0">
 *                           &lt;complexType>
 *                             &lt;complexContent>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                 &lt;sequence maxOccurs="unbounded">
 *                                   &lt;element name="on_return_code">
 *                                     &lt;complexType>
 *                                       &lt;complexContent>
 *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                           &lt;sequence>
 *                                             &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *                                             &lt;element name="to_state" minOccurs="0">
 *                                               &lt;complexType>
 *                                                 &lt;complexContent>
 *                                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                     &lt;attribute name="state" type="{}String" />
 *                                                   &lt;/restriction>
 *                                                 &lt;/complexContent>
 *                                               &lt;/complexType>
 *                                             &lt;/element>
 *                                           &lt;/sequence>
 *                                           &lt;attribute name="return_code" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                         &lt;/restriction>
 *                                       &lt;/complexContent>
 *                                     &lt;/complexType>
 *                                   &lt;/element>
 *                                 &lt;/sequence>
 *                               &lt;/restriction>
 *                             &lt;/complexContent>
 *                           &lt;/complexType>
 *                         &lt;/element>
 *                       &lt;/sequence>
 *                       &lt;attribute name="state" use="required" type="{}String" />
 *                       &lt;attribute name="job" type="{}Path" />
 *                       &lt;attribute name="next_state" type="{}String" />
 *                       &lt;attribute name="error_state" type="{}String" />
 *                       &lt;attribute name="on_error">
 *                         &lt;simpleType>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *                             &lt;enumeration value="setback"/>
 *                             &lt;enumeration value="suspend"/>
 *                           &lt;/restriction>
 *                         &lt;/simpleType>
 *                       &lt;/attribute>
 *                       &lt;attribute name="suspend" type="{}Yes_no" />
 *                       &lt;attribute name="delay" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *                       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *                     &lt;/restriction>
 *                   &lt;/complexContent>
 *                 &lt;/complexType>
 *               &lt;/element>
 *               &lt;element name="file_order_sink">
 *                 &lt;complexType>
 *                   &lt;complexContent>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                       &lt;attribute name="state" use="required" type="{}String" />
 *                       &lt;attribute name="remove" type="{}Yes_no" />
 *                       &lt;attribute name="move_to" type="{}File" />
 *                       &lt;attribute name="delay" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *                     &lt;/restriction>
 *                   &lt;/complexContent>
 *                 &lt;/complexType>
 *               &lt;/element>
 *               &lt;element name="job_chain_node.end" type="{}job_chain_node.end"/>
 *             &lt;/choice>
 *           &lt;/sequence>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{}Name" />
 *       &lt;attribute name="replace" type="{}Yes_no" />
 *       &lt;attribute name="visible" type="{}Yes_no_never" />
 *       &lt;attribute name="orders_recoverable" type="{}Yes_no" />
 *       &lt;attribute name="distributed" type="{}Yes_no" />
 *       &lt;attribute name="title" type="{}String" />
 *       &lt;attribute name="max_orders" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="process_class" type="{}Path" />
 *       &lt;attribute name="file_watching_process_class" type="{}Path" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "extensions",
    "note",
    "jobChainNodeJobChain",
    "jobChainNodeJobChainOrJobChainNodeEnd",
    "fileOrderSource",
    "jobChainNodeOrFileOrderSinkOrJobChainNodeEnd"
})
@XmlRootElement(name = "job_chain")
public class JobChain {

    protected Extensions extensions;
    protected List<Note> note;
    @XmlElement(name = "job_chain_node.job_chain")
    protected JobChainNodeJobChain jobChainNodeJobChain;
    @XmlElements({
        @XmlElement(name = "job_chain_node.job_chain", type = JobChainNodeJobChain.class),
        @XmlElement(name = "job_chain_node.end", type = JobChainNodeEnd.class)
    })
    protected List<Object> jobChainNodeJobChainOrJobChainNodeEnd;
    @XmlElement(name = "file_order_source")
    protected List<JobChain.FileOrderSource> fileOrderSource;
    @XmlElements({
        @XmlElement(name = "job_chain_node", type = JobChain.JobChainNode.class),
        @XmlElement(name = "file_order_sink", type = JobChain.FileOrderSink.class),
        @XmlElement(name = "job_chain_node.end", type = JobChainNodeEnd.class)
    })
    protected List<Object> jobChainNodeOrFileOrderSinkOrJobChainNodeEnd;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "replace")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String replace;
    @XmlAttribute(name = "visible")
    protected String visible;
    @XmlAttribute(name = "orders_recoverable")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String ordersRecoverable;
    @XmlAttribute(name = "distributed")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String distributed;
    @XmlAttribute(name = "title")
    protected String title;
    @XmlAttribute(name = "max_orders")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger maxOrders;
    @XmlAttribute(name = "process_class")
    protected String processClass;
    @XmlAttribute(name = "file_watching_process_class")
    protected String fileWatchingProcessClass;

    /**
     * Ruft den Wert der extensions-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Extensions }
     *     
     */
    public Extensions getExtensions() {
        return extensions;
    }

    /**
     * Legt den Wert der extensions-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Extensions }
     *     
     */
    public void setExtensions(Extensions value) {
        this.extensions = value;
    }

    /**
     * Gets the value of the note property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the note property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNote().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Note }
     * 
     * 
     */
    public List<Note> getNote() {
        if (note == null) {
            note = new ArrayList<Note>();
        }
        return this.note;
    }

    /**
     * Ruft den Wert der jobChainNodeJobChain-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JobChainNodeJobChain }
     *     
     */
    public JobChainNodeJobChain getJobChainNodeJobChain() {
        return jobChainNodeJobChain;
    }

    /**
     * Legt den Wert der jobChainNodeJobChain-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JobChainNodeJobChain }
     *     
     */
    public void setJobChainNodeJobChain(JobChainNodeJobChain value) {
        this.jobChainNodeJobChain = value;
    }

    /**
     * Gets the value of the jobChainNodeJobChainOrJobChainNodeEnd property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the jobChainNodeJobChainOrJobChainNodeEnd property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJobChainNodeJobChainOrJobChainNodeEnd().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JobChainNodeJobChain }
     * {@link JobChainNodeEnd }
     * 
     * 
     */
    public List<Object> getJobChainNodeJobChainOrJobChainNodeEnd() {
        if (jobChainNodeJobChainOrJobChainNodeEnd == null) {
            jobChainNodeJobChainOrJobChainNodeEnd = new ArrayList<Object>();
        }
        return this.jobChainNodeJobChainOrJobChainNodeEnd;
    }

    /**
     * Gets the value of the fileOrderSource property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fileOrderSource property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFileOrderSource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JobChain.FileOrderSource }
     * 
     * 
     */
    public List<JobChain.FileOrderSource> getFileOrderSource() {
        if (fileOrderSource == null) {
            fileOrderSource = new ArrayList<JobChain.FileOrderSource>();
        }
        return this.fileOrderSource;
    }

    /**
     * Gets the value of the jobChainNodeOrFileOrderSinkOrJobChainNodeEnd property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the jobChainNodeOrFileOrderSinkOrJobChainNodeEnd property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJobChainNodeOrFileOrderSinkOrJobChainNodeEnd().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JobChain.JobChainNode }
     * {@link JobChain.FileOrderSink }
     * {@link JobChainNodeEnd }
     * 
     * 
     */
    public List<Object> getJobChainNodeOrFileOrderSinkOrJobChainNodeEnd() {
        if (jobChainNodeOrFileOrderSinkOrJobChainNodeEnd == null) {
            jobChainNodeOrFileOrderSinkOrJobChainNodeEnd = new ArrayList<Object>();
        }
        return this.jobChainNodeOrFileOrderSinkOrJobChainNodeEnd;
    }

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Ruft den Wert der replace-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReplace() {
        return replace;
    }

    /**
     * Legt den Wert der replace-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReplace(String value) {
        this.replace = value;
    }

    /**
     * Ruft den Wert der visible-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVisible() {
        return visible;
    }

    /**
     * Legt den Wert der visible-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVisible(String value) {
        this.visible = value;
    }

    /**
     * Ruft den Wert der ordersRecoverable-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrdersRecoverable() {
        return ordersRecoverable;
    }

    /**
     * Legt den Wert der ordersRecoverable-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrdersRecoverable(String value) {
        this.ordersRecoverable = value;
    }

    /**
     * Ruft den Wert der distributed-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDistributed() {
        return distributed;
    }

    /**
     * Legt den Wert der distributed-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDistributed(String value) {
        this.distributed = value;
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
     * Ruft den Wert der maxOrders-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxOrders() {
        return maxOrders;
    }

    /**
     * Legt den Wert der maxOrders-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxOrders(BigInteger value) {
        this.maxOrders = value;
    }

    /**
     * Ruft den Wert der processClass-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessClass() {
        return processClass;
    }

    /**
     * Legt den Wert der processClass-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessClass(String value) {
        this.processClass = value;
    }

    /**
     * Ruft den Wert der fileWatchingProcessClass-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileWatchingProcessClass() {
        return fileWatchingProcessClass;
    }

    /**
     * Legt den Wert der fileWatchingProcessClass-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileWatchingProcessClass(String value) {
        this.fileWatchingProcessClass = value;
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
     *       &lt;attribute name="state" use="required" type="{}String" />
     *       &lt;attribute name="remove" type="{}Yes_no" />
     *       &lt;attribute name="move_to" type="{}File" />
     *       &lt;attribute name="delay" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class FileOrderSink {

        @XmlAttribute(name = "state", required = true)
        protected String state;
        @XmlAttribute(name = "remove")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        protected String remove;
        @XmlAttribute(name = "move_to")
        protected String moveTo;
        @XmlAttribute(name = "delay")
        @XmlSchemaType(name = "nonNegativeInteger")
        protected BigInteger delay;

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
         * Ruft den Wert der remove-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRemove() {
            return remove;
        }

        /**
         * Legt den Wert der remove-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRemove(String value) {
            this.remove = value;
        }

        /**
         * Ruft den Wert der moveTo-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMoveTo() {
            return moveTo;
        }

        /**
         * Legt den Wert der moveTo-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMoveTo(String value) {
            this.moveTo = value;
        }

        /**
         * Ruft den Wert der delay-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getDelay() {
            return delay;
        }

        /**
         * Legt den Wert der delay-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setDelay(BigInteger value) {
            this.delay = value;
        }

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
     *       &lt;attribute name="directory" use="required" type="{}String" />
     *       &lt;attribute name="regex" type="{}String" />
     *       &lt;attribute name="delay_after_error" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
     *       &lt;attribute name="repeat">
     *         &lt;simpleType>
     *           &lt;union>
     *             &lt;simpleType>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
     *                 &lt;enumeration value="no"/>
     *               &lt;/restriction>
     *             &lt;/simpleType>
     *             &lt;simpleType>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}positiveInteger">
     *               &lt;/restriction>
     *             &lt;/simpleType>
     *           &lt;/union>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="max" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
     *       &lt;attribute name="next_state" type="{}String" />
     *       &lt;attribute name="alert_when_directory_missing" type="{}Yes_no" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class FileOrderSource {

        @XmlAttribute(name = "directory", required = true)
        protected String directory;
        @XmlAttribute(name = "regex")
        protected String regex;
        @XmlAttribute(name = "delay_after_error")
        @XmlSchemaType(name = "positiveInteger")
        protected BigInteger delayAfterError;
        @XmlAttribute(name = "repeat")
        protected String repeat;
        @XmlAttribute(name = "max")
        @XmlSchemaType(name = "positiveInteger")
        protected BigInteger max;
        @XmlAttribute(name = "next_state")
        protected String nextState;
        @XmlAttribute(name = "alert_when_directory_missing")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        protected String alertWhenDirectoryMissing;

        /**
         * Ruft den Wert der directory-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDirectory() {
            return directory;
        }

        /**
         * Legt den Wert der directory-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDirectory(String value) {
            this.directory = value;
        }

        /**
         * Ruft den Wert der regex-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRegex() {
            return regex;
        }

        /**
         * Legt den Wert der regex-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRegex(String value) {
            this.regex = value;
        }

        /**
         * Ruft den Wert der delayAfterError-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getDelayAfterError() {
            return delayAfterError;
        }

        /**
         * Legt den Wert der delayAfterError-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setDelayAfterError(BigInteger value) {
            this.delayAfterError = value;
        }

        /**
         * Ruft den Wert der repeat-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRepeat() {
            return repeat;
        }

        /**
         * Legt den Wert der repeat-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRepeat(String value) {
            this.repeat = value;
        }

        /**
         * Ruft den Wert der max-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getMax() {
            return max;
        }

        /**
         * Legt den Wert der max-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setMax(BigInteger value) {
            this.max = value;
        }

        /**
         * Ruft den Wert der nextState-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNextState() {
            return nextState;
        }

        /**
         * Legt den Wert der nextState-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNextState(String value) {
            this.nextState = value;
        }

        /**
         * Ruft den Wert der alertWhenDirectoryMissing-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAlertWhenDirectoryMissing() {
            return alertWhenDirectoryMissing;
        }

        /**
         * Legt den Wert der alertWhenDirectoryMissing-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAlertWhenDirectoryMissing(String value) {
            this.alertWhenDirectoryMissing = value;
        }

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
     *       &lt;sequence>
     *         &lt;element ref="{}extensions" minOccurs="0"/>
     *         &lt;any processContents='lax' namespace='##other' minOccurs="0"/>
     *         &lt;element name="on_return_codes" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence maxOccurs="unbounded">
     *                   &lt;element name="on_return_code">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
     *                             &lt;element name="to_state" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;attribute name="state" type="{}String" />
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                           &lt;attribute name="return_code" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="state" use="required" type="{}String" />
     *       &lt;attribute name="job" type="{}Path" />
     *       &lt;attribute name="next_state" type="{}String" />
     *       &lt;attribute name="error_state" type="{}String" />
     *       &lt;attribute name="on_error">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
     *             &lt;enumeration value="setback"/>
     *             &lt;enumeration value="suspend"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="suspend" type="{}Yes_no" />
     *       &lt;attribute name="delay" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
     *       &lt;anyAttribute processContents='lax' namespace='##other'/>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "extensions",
        "any",
        "onReturnCodes"
    })
    public static class JobChainNode {

        protected Extensions extensions;
        @XmlAnyElement(lax = true)
        protected Object any;
        @XmlElement(name = "on_return_codes")
        protected JobChain.JobChainNode.OnReturnCodes onReturnCodes;
        @XmlAttribute(name = "state", required = true)
        protected String state;
        @XmlAttribute(name = "job")
        protected String job;
        @XmlAttribute(name = "next_state")
        protected String nextState;
        @XmlAttribute(name = "error_state")
        protected String errorState;
        @XmlAttribute(name = "on_error")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        protected String onError;
        @XmlAttribute(name = "suspend")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        protected String suspend;
        @XmlAttribute(name = "delay")
        @XmlSchemaType(name = "nonNegativeInteger")
        protected BigInteger delay;
        @XmlAnyAttribute
        private Map<QName, String> otherAttributes = new HashMap<QName, String>();

        /**
         * Ruft den Wert der extensions-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Extensions }
         *     
         */
        public Extensions getExtensions() {
            return extensions;
        }

        /**
         * Legt den Wert der extensions-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Extensions }
         *     
         */
        public void setExtensions(Extensions value) {
            this.extensions = value;
        }

        /**
         * Ruft den Wert der any-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     {@link Element }
         *     
         */
        public Object getAny() {
            return any;
        }

        /**
         * Legt den Wert der any-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     {@link Element }
         *     
         */
        public void setAny(Object value) {
            this.any = value;
        }

        /**
         * Ruft den Wert der onReturnCodes-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link JobChain.JobChainNode.OnReturnCodes }
         *     
         */
        public JobChain.JobChainNode.OnReturnCodes getOnReturnCodes() {
            return onReturnCodes;
        }

        /**
         * Legt den Wert der onReturnCodes-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link JobChain.JobChainNode.OnReturnCodes }
         *     
         */
        public void setOnReturnCodes(JobChain.JobChainNode.OnReturnCodes value) {
            this.onReturnCodes = value;
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
         * Ruft den Wert der job-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getJob() {
            return job;
        }

        /**
         * Legt den Wert der job-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setJob(String value) {
            this.job = value;
        }

        /**
         * Ruft den Wert der nextState-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNextState() {
            return nextState;
        }

        /**
         * Legt den Wert der nextState-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNextState(String value) {
            this.nextState = value;
        }

        /**
         * Ruft den Wert der errorState-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getErrorState() {
            return errorState;
        }

        /**
         * Legt den Wert der errorState-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setErrorState(String value) {
            this.errorState = value;
        }

        /**
         * Ruft den Wert der onError-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOnError() {
            return onError;
        }

        /**
         * Legt den Wert der onError-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOnError(String value) {
            this.onError = value;
        }

        /**
         * Ruft den Wert der suspend-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSuspend() {
            return suspend;
        }

        /**
         * Legt den Wert der suspend-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSuspend(String value) {
            this.suspend = value;
        }

        /**
         * Ruft den Wert der delay-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getDelay() {
            return delay;
        }

        /**
         * Legt den Wert der delay-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setDelay(BigInteger value) {
            this.delay = value;
        }

        /**
         * Gets a map that contains attributes that aren't bound to any typed property on this class.
         * 
         * <p>
         * the map is keyed by the name of the attribute and 
         * the value is the string value of the attribute.
         * 
         * the map returned by this method is live, and you can add new attribute
         * by updating the map directly. Because of this design, there's no setter.
         * 
         * 
         * @return
         *     always non-null
         */
        public Map<QName, String> getOtherAttributes() {
            return otherAttributes;
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
         *       &lt;sequence maxOccurs="unbounded">
         *         &lt;element name="on_return_code">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
         *                   &lt;element name="to_state" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;attribute name="state" type="{}String" />
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *                 &lt;attribute name="return_code" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "onReturnCode"
        })
        public static class OnReturnCodes {

            @XmlElement(name = "on_return_code", required = true)
            protected List<JobChain.JobChainNode.OnReturnCodes.OnReturnCode> onReturnCode;

            /**
             * Gets the value of the onReturnCode property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the onReturnCode property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getOnReturnCode().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link JobChain.JobChainNode.OnReturnCodes.OnReturnCode }
             * 
             * 
             */
            public List<JobChain.JobChainNode.OnReturnCodes.OnReturnCode> getOnReturnCode() {
                if (onReturnCode == null) {
                    onReturnCode = new ArrayList<JobChain.JobChainNode.OnReturnCodes.OnReturnCode>();
                }
                return this.onReturnCode;
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
             *       &lt;sequence>
             *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
             *         &lt;element name="to_state" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="state" type="{}String" />
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *       &lt;attribute name="return_code" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "any",
                "toState"
            })
            public static class OnReturnCode {

                @XmlAnyElement(lax = true)
                protected List<Object> any;
                @XmlElement(name = "to_state")
                protected JobChain.JobChainNode.OnReturnCodes.OnReturnCode.ToState toState;
                @XmlAttribute(name = "return_code")
                @XmlSchemaType(name = "anySimpleType")
                protected String returnCode;

                /**
                 * Gets the value of the any property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the any property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getAny().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Object }
                 * {@link Element }
                 * 
                 * 
                 */
                public List<Object> getAny() {
                    if (any == null) {
                        any = new ArrayList<Object>();
                    }
                    return this.any;
                }

                /**
                 * Ruft den Wert der toState-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link JobChain.JobChainNode.OnReturnCodes.OnReturnCode.ToState }
                 *     
                 */
                public JobChain.JobChainNode.OnReturnCodes.OnReturnCode.ToState getToState() {
                    return toState;
                }

                /**
                 * Legt den Wert der toState-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link JobChain.JobChainNode.OnReturnCodes.OnReturnCode.ToState }
                 *     
                 */
                public void setToState(JobChain.JobChainNode.OnReturnCodes.OnReturnCode.ToState value) {
                    this.toState = value;
                }

                /**
                 * Ruft den Wert der returnCode-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getReturnCode() {
                    return returnCode;
                }

                /**
                 * Legt den Wert der returnCode-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setReturnCode(String value) {
                    this.returnCode = value;
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
                 *       &lt;attribute name="state" type="{}String" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class ToState {

                    @XmlAttribute(name = "state")
                    protected String state;

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

                }

            }

        }

    }

}
