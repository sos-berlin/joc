//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.03.03 um 12:06:46 PM CET 
//


package com.sos.auth.rest.permission.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="JobSchedulerMaster" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{}SOSPermissionCommands"/>
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
    "jobSchedulerMaster",
    "sosPermissionCommands"
})
@XmlRootElement(name = "SOSPermissionCommandsMaster")
public class SOSPermissionCommandsMaster
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "JobSchedulerMaster", required = true)
    protected String jobSchedulerMaster;
    @XmlElement(name = "SOSPermissionCommands", required = true)
    protected SOSPermissionCommands sosPermissionCommands;

    /**
     * Ruft den Wert der jobSchedulerMaster-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobSchedulerMaster() {
        return jobSchedulerMaster;
    }

    /**
     * Legt den Wert der jobSchedulerMaster-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobSchedulerMaster(String value) {
        this.jobSchedulerMaster = value;
    }

    public boolean isSetJobSchedulerMaster() {
        return (this.jobSchedulerMaster!= null);
    }

    /**
     * Ruft den Wert der sosPermissionCommands-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionCommands }
     *     
     */
    public SOSPermissionCommands getSOSPermissionCommands() {
        return sosPermissionCommands;
    }

    /**
     * Legt den Wert der sosPermissionCommands-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionCommands }
     *     
     */
    public void setSOSPermissionCommands(SOSPermissionCommands value) {
        this.sosPermissionCommands = value;
    }

    public boolean isSetSOSPermissionCommands() {
        return (this.sosPermissionCommands!= null);
    }

}
