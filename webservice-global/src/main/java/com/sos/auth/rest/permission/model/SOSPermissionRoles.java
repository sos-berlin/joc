//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.07.24 at 11:34:46 AM CEST 
//


package com.sos.auth.rest.permission.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{}SOSPermissionRole" maxOccurs="unbounded"/>
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
    "sosPermissionRole"
})
@XmlRootElement(name = "SOSPermissionRoles")
public class SOSPermissionRoles
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "SOSPermissionRole", required = true)
    protected List<String> sosPermissionRole;

    /**
     * Gets the value of the sosPermissionRole property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sosPermissionRole property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSOSPermissionRole().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSOSPermissionRole() {
        if (sosPermissionRole == null) {
            sosPermissionRole = new ArrayList<String>();
        }
        return this.sosPermissionRole;
    }

    public boolean isSetSOSPermissionRole() {
        return ((this.sosPermissionRole!= null)&&(!this.sosPermissionRole.isEmpty()));
    }

    public void unsetSOSPermissionRole() {
        this.sosPermissionRole = null;
    }

}
