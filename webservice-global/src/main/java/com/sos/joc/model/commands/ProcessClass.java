//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.03.24 um 02:41:35 PM CET 
//


package com.sos.joc.model.commands;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java-Klasse für process_class complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="process_class">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="remote_schedulers" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="remote_scheduler" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="remote_scheduler" type="{}String" />
 *                           &lt;attribute name="http_heartbeat_period" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *                           &lt;attribute name="http_heartbeat_timeout" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="select">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *                       &lt;enumeration value="first"/>
 *                       &lt;enumeration value="next"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{}Name" />
 *       &lt;attribute name="max_processes" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="remote_scheduler" type="{}String" />
 *       &lt;attribute name="spooler_id" type="{}Name" />
 *       &lt;attribute name="replace" type="{}Yes_no" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "process_class", propOrder = {
    "remoteSchedulers"
})
public class ProcessClass {

    @XmlElement(name = "remote_schedulers")
    protected ProcessClass.RemoteSchedulers remoteSchedulers;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "max_processes")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger maxProcesses;
    @XmlAttribute(name = "remote_scheduler")
    protected String remoteScheduler;
    @XmlAttribute(name = "spooler_id")
    protected String spoolerId;
    @XmlAttribute(name = "replace")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String replace;

    /**
     * Ruft den Wert der remoteSchedulers-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ProcessClass.RemoteSchedulers }
     *     
     */
    public ProcessClass.RemoteSchedulers getRemoteSchedulers() {
        return remoteSchedulers;
    }

    /**
     * Legt den Wert der remoteSchedulers-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcessClass.RemoteSchedulers }
     *     
     */
    public void setRemoteSchedulers(ProcessClass.RemoteSchedulers value) {
        this.remoteSchedulers = value;
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
     * Ruft den Wert der maxProcesses-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxProcesses() {
        return maxProcesses;
    }

    /**
     * Legt den Wert der maxProcesses-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxProcesses(BigInteger value) {
        this.maxProcesses = value;
    }

    /**
     * Ruft den Wert der remoteScheduler-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemoteScheduler() {
        return remoteScheduler;
    }

    /**
     * Legt den Wert der remoteScheduler-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemoteScheduler(String value) {
        this.remoteScheduler = value;
    }

    /**
     * Ruft den Wert der spoolerId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpoolerId() {
        return spoolerId;
    }

    /**
     * Legt den Wert der spoolerId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpoolerId(String value) {
        this.spoolerId = value;
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
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="remote_scheduler" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="remote_scheduler" type="{}String" />
     *                 &lt;attribute name="http_heartbeat_period" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
     *                 &lt;attribute name="http_heartbeat_timeout" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="select">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
     *             &lt;enumeration value="first"/>
     *             &lt;enumeration value="next"/>
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
        "remoteScheduler"
    })
    public static class RemoteSchedulers {

        @XmlElement(name = "remote_scheduler")
        protected List<ProcessClass.RemoteSchedulers.RemoteScheduler> remoteScheduler;
        @XmlAttribute(name = "select")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        protected String select;

        /**
         * Gets the value of the remoteScheduler property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the remoteScheduler property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRemoteScheduler().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ProcessClass.RemoteSchedulers.RemoteScheduler }
         * 
         * 
         */
        public List<ProcessClass.RemoteSchedulers.RemoteScheduler> getRemoteScheduler() {
            if (remoteScheduler == null) {
                remoteScheduler = new ArrayList<ProcessClass.RemoteSchedulers.RemoteScheduler>();
            }
            return this.remoteScheduler;
        }

        /**
         * Ruft den Wert der select-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSelect() {
            return select;
        }

        /**
         * Legt den Wert der select-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSelect(String value) {
            this.select = value;
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
         *       &lt;attribute name="remote_scheduler" type="{}String" />
         *       &lt;attribute name="http_heartbeat_period" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
         *       &lt;attribute name="http_heartbeat_timeout" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class RemoteScheduler {

            @XmlAttribute(name = "remote_scheduler")
            protected String remoteScheduler;
            @XmlAttribute(name = "http_heartbeat_period")
            @XmlSchemaType(name = "positiveInteger")
            protected BigInteger httpHeartbeatPeriod;
            @XmlAttribute(name = "http_heartbeat_timeout")
            @XmlSchemaType(name = "positiveInteger")
            protected BigInteger httpHeartbeatTimeout;

            /**
             * Ruft den Wert der remoteScheduler-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRemoteScheduler() {
                return remoteScheduler;
            }

            /**
             * Legt den Wert der remoteScheduler-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRemoteScheduler(String value) {
                this.remoteScheduler = value;
            }

            /**
             * Ruft den Wert der httpHeartbeatPeriod-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getHttpHeartbeatPeriod() {
                return httpHeartbeatPeriod;
            }

            /**
             * Legt den Wert der httpHeartbeatPeriod-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setHttpHeartbeatPeriod(BigInteger value) {
                this.httpHeartbeatPeriod = value;
            }

            /**
             * Ruft den Wert der httpHeartbeatTimeout-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getHttpHeartbeatTimeout() {
                return httpHeartbeatTimeout;
            }

            /**
             * Legt den Wert der httpHeartbeatTimeout-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setHttpHeartbeatTimeout(BigInteger value) {
                this.httpHeartbeatTimeout = value;
            }

        }

    }

}
