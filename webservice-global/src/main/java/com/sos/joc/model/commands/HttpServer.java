//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.03.24 um 02:41:35 PM CET 
//


package com.sos.joc.model.commands;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="http.authentication" type="{}http.authentication"/>
 *         &lt;element name="web_service" type="{}web_service"/>
 *         &lt;element name="http_directory" type="{}http_directory"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "httpAuthenticationOrWebServiceOrHttpDirectory"
})
@XmlRootElement(name = "http_server")
public class HttpServer {

    @XmlElements({
        @XmlElement(name = "http.authentication", type = HttpAuthentication.class),
        @XmlElement(name = "web_service", type = WebService.class),
        @XmlElement(name = "http_directory", type = HttpDirectory.class)
    })
    protected List<Object> httpAuthenticationOrWebServiceOrHttpDirectory;

    /**
     * Gets the value of the httpAuthenticationOrWebServiceOrHttpDirectory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the httpAuthenticationOrWebServiceOrHttpDirectory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHttpAuthenticationOrWebServiceOrHttpDirectory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HttpAuthentication }
     * {@link WebService }
     * {@link HttpDirectory }
     * 
     * 
     */
    public List<Object> getHttpAuthenticationOrWebServiceOrHttpDirectory() {
        if (httpAuthenticationOrWebServiceOrHttpDirectory == null) {
            httpAuthenticationOrWebServiceOrHttpDirectory = new ArrayList<Object>();
        }
        return this.httpAuthenticationOrWebServiceOrHttpDirectory;
    }

}
