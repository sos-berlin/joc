//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.07.03 um 04:47:04 PM CEST 
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
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element ref="{}SOSPermissionJocCockpitMaster"/>
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
    "sosPermissionJocCockpitMaster"
})
@XmlRootElement(name = "SOSPermissionJocCockpitMasters")
public class SOSPermissionJocCockpitMasters
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "SOSPermissionJocCockpitMaster", required = true)
    protected List<SOSPermissionJocCockpitMaster> sosPermissionJocCockpitMaster;

    /**
     * Gets the value of the sosPermissionJocCockpitMaster property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sosPermissionJocCockpitMaster property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSOSPermissionJocCockpitMaster().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SOSPermissionJocCockpitMaster }
     * 
     * 
     */
    public List<SOSPermissionJocCockpitMaster> getSOSPermissionJocCockpitMaster() {
        if (sosPermissionJocCockpitMaster == null) {
            sosPermissionJocCockpitMaster = new ArrayList<SOSPermissionJocCockpitMaster>();
        }
        return this.sosPermissionJocCockpitMaster;
    }

    public boolean isSetSOSPermissionJocCockpitMaster() {
        return ((this.sosPermissionJocCockpitMaster!= null)&&(!this.sosPermissionJocCockpitMaster.isEmpty()));
    }

    public void unsetSOSPermissionJocCockpitMaster() {
        this.sosPermissionJocCockpitMaster = null;
    }

}
