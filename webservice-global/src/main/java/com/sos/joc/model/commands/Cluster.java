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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="heart_beat_timeout" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *       &lt;attribute name="heart_beat_own_timeout">
 *         &lt;simpleType>
 *           &lt;union memberTypes=" {http://www.w3.org/2001/XMLSchema}positiveInteger">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *                 &lt;enumeration value="never"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/union>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="heart_beat_warn_timeout" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "cluster")
public class Cluster {

    @XmlAttribute(name = "heart_beat_timeout")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger heartBeatTimeout;
    @XmlAttribute(name = "heart_beat_own_timeout")
    protected String heartBeatOwnTimeout;
    @XmlAttribute(name = "heart_beat_warn_timeout")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger heartBeatWarnTimeout;

    /**
     * Ruft den Wert der heartBeatTimeout-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getHeartBeatTimeout() {
        return heartBeatTimeout;
    }

    /**
     * Legt den Wert der heartBeatTimeout-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setHeartBeatTimeout(BigInteger value) {
        this.heartBeatTimeout = value;
    }

    /**
     * Ruft den Wert der heartBeatOwnTimeout-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHeartBeatOwnTimeout() {
        return heartBeatOwnTimeout;
    }

    /**
     * Legt den Wert der heartBeatOwnTimeout-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHeartBeatOwnTimeout(String value) {
        this.heartBeatOwnTimeout = value;
    }

    /**
     * Ruft den Wert der heartBeatWarnTimeout-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getHeartBeatWarnTimeout() {
        return heartBeatWarnTimeout;
    }

    /**
     * Legt den Wert der heartBeatWarnTimeout-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setHeartBeatWarnTimeout(BigInteger value) {
        this.heartBeatWarnTimeout = value;
    }

}
