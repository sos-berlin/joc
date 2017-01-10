//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.01.10 um 05:11:36 PM CET 
//


package com.sos.joc.model.commands;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für jobscheduler_commands complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="jobscheduler_commands">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.sos-berlin.com/scheduler}jobscheduler_command" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="url" type="{}String" />
 *       &lt;attribute name="jobschedulerId" type="{}String" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "jobscheduler_commands", namespace = "http://www.sos-berlin.com/scheduler", propOrder = {
    "jobschedulerCommand"
})
public class JobschedulerCommands {

    @XmlElement(name = "jobscheduler_command", namespace = "http://www.sos-berlin.com/scheduler", required = true)
    protected List<JobschedulerCommand> jobschedulerCommand;
    @XmlAttribute(name = "url")
    protected String url;
    @XmlAttribute(name = "jobschedulerId")
    protected String jobschedulerId;

    /**
     * Gets the value of the jobschedulerCommand property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the jobschedulerCommand property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJobschedulerCommand().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JobschedulerCommand }
     * 
     * 
     */
    public List<JobschedulerCommand> getJobschedulerCommand() {
        if (jobschedulerCommand == null) {
            jobschedulerCommand = new ArrayList<JobschedulerCommand>();
        }
        return this.jobschedulerCommand;
    }

    /**
     * Ruft den Wert der url-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Legt den Wert der url-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Ruft den Wert der jobschedulerId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobschedulerId() {
        return jobschedulerId;
    }

    /**
     * Legt den Wert der jobschedulerId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobschedulerId(String value) {
        this.jobschedulerId = value;
    }

}
