//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.07.24 at 11:34:46 AM CEST 
//


package com.sos.auth.rest.permission.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
@XmlRootElement(name = "SOSPermissionJocCockpitMaster")
public class SOSPermissionJocCockpitMaster
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "JobSchedulerMaster", required = true)
    protected String jobSchedulerMaster;
    @XmlElement(name = "SOSPermissionJocCockpit", required = true)
    protected SOSPermissionJocCockpit sosPermissionJocCockpit;

    /**
     * Gets the value of the jobSchedulerMaster property.
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
     * Sets the value of the jobSchedulerMaster property.
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
     * Gets the value of the sosPermissionJocCockpit property.
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
     * Sets the value of the sosPermissionJocCockpit property.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit }
     *     
     */
    public void setSOSPermissionJocCockpit(SOSPermissionJocCockpit value) {
        this.sosPermissionJocCockpit = value;
    }

    public boolean isSetSOSPermissionJocCockpit() {
        return (this.sosPermissionJocCockpit!= null);
    }

}
