//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.07.03 um 04:47:04 PM CEST 
//


package com.sos.auth.rest.permission.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="view" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "view"
})
@XmlRootElement(name = "SOSDocumentation")
public class SOSDocumentation
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    protected boolean view;

    /**
     * Ruft den Wert der view-Eigenschaft ab.
     * 
     */
    public boolean isView() {
        return view;
    }

    /**
     * Legt den Wert der view-Eigenschaft fest.
     * 
     */
    public void setView(boolean value) {
        this.view = value;
    }

    public boolean isSetView() {
        return true;
    }

}
