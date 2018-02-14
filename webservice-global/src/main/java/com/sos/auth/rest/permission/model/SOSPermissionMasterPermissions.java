//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.02.12 um 11:46:11 AM CET 
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
 *         &lt;element name="JobSchedulerMaster" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{}SOSPermissionJocCockpit"/>
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
    "sosPermissionJocCockpit"
})
@XmlRootElement(name = "SOSPermissionMasterPermissions")
public class SOSPermissionMasterPermissions {

    @XmlElement(name = "JobSchedulerMaster", required = true)
    protected String jobSchedulerMaster;
    @XmlElement(name = "SOSPermissionJocCockpit", required = true)
    protected SOSPermissionJocCockpit sosPermissionJocCockpit;

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

    /**
     * Ruft den Wert der sosPermissionJocCockpit-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit }
     *     
     */
    public SOSPermissionJocCockpit getSOSPermissionJocCockpit() {
        return sosPermissionJocCockpit;
    }

    /**
     * Legt den Wert der sosPermissionJocCockpit-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit }
     *     
     */
    public void setSOSPermissionJocCockpit(SOSPermissionJocCockpit value) {
        this.sosPermissionJocCockpit = value;
    }

}
