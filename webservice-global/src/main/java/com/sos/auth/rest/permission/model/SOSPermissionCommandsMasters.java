//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2019.11.29 um 05:34:57 PM CET 
//


package com.sos.auth.rest.permission.model;

import java.util.ArrayList;
import java.util.List;
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
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element ref="{}SOSPermissionCommandsMaster"/>
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
    "sosPermissionCommandsMaster"
})
@XmlRootElement(name = "SOSPermissionCommandsMasters")
public class SOSPermissionCommandsMasters {

    @XmlElement(name = "SOSPermissionCommandsMaster", required = true)
    protected List<SOSPermissionCommandsMaster> sosPermissionCommandsMaster;

    /**
     * Gets the value of the sosPermissionCommandsMaster property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sosPermissionCommandsMaster property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSOSPermissionCommandsMaster().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SOSPermissionCommandsMaster }
     * 
     * 
     */
    public List<SOSPermissionCommandsMaster> getSOSPermissionCommandsMaster() {
        if (sosPermissionCommandsMaster == null) {
            sosPermissionCommandsMaster = new ArrayList<SOSPermissionCommandsMaster>();
        }
        return this.sosPermissionCommandsMaster;
    }

}
