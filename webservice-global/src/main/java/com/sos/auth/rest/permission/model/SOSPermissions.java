//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.11.29 um 04:03:44 PM CET 
//


package com.sos.auth.rest.permission.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
 *       &lt;sequence>
 *         &lt;element ref="{}SOSPermissionListCommands"/>
 *         &lt;element ref="{}SOSPermissionListJoc"/>
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
    "sosPermissionListCommands",
    "sosPermissionListJoc"
})
@XmlRootElement(name = "SOSPermissions")
public class SOSPermissions {

    @XmlElement(name = "SOSPermissionListCommands", required = true)
    protected SOSPermissionListCommands sosPermissionListCommands;
    @XmlElement(name = "SOSPermissionListJoc", required = true)
    protected SOSPermissionListJoc sosPermissionListJoc;

    /**
     * Ruft den Wert der sosPermissionListCommands-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionListCommands }
     *     
     */
    public SOSPermissionListCommands getSOSPermissionListCommands() {
        return sosPermissionListCommands;
    }

    /**
     * Legt den Wert der sosPermissionListCommands-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionListCommands }
     *     
     */
    public void setSOSPermissionListCommands(SOSPermissionListCommands value) {
        this.sosPermissionListCommands = value;
    }

    /**
     * Ruft den Wert der sosPermissionListJoc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionListJoc }
     *     
     */
    public SOSPermissionListJoc getSOSPermissionListJoc() {
        return sosPermissionListJoc;
    }

    /**
     * Legt den Wert der sosPermissionListJoc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionListJoc }
     *     
     */
    public void setSOSPermissionListJoc(SOSPermissionListJoc value) {
        this.sosPermissionListJoc = value;
    }

}
