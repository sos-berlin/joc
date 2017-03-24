//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.03.24 um 02:41:35 PM CET 
//


package com.sos.joc.model.commands;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java-Klasse für Job.Settings complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Job.Settings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mail_on_error" type="{}Yes_no" minOccurs="0"/>
 *         &lt;element name="mail_on_warning" type="{}Yes_no" minOccurs="0"/>
 *         &lt;element name="mail_on_success" type="{}Yes_no" minOccurs="0"/>
 *         &lt;element name="mail_on_process" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;union memberTypes=" {}Yes_no {http://www.w3.org/2001/XMLSchema}positiveInteger">
 *             &lt;/union>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="mail_on_delay_after_error" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *               &lt;enumeration value="all"/>
 *               &lt;enumeration value="first_only"/>
 *               &lt;enumeration value="last_only"/>
 *               &lt;enumeration value="first_and_last_only"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="log_mail_to" type="{}Non_empty_string" minOccurs="0"/>
 *         &lt;element name="log_mail_cc" type="{}Non_empty_string" minOccurs="0"/>
 *         &lt;element name="log_mail_bcc" type="{}Non_empty_string" minOccurs="0"/>
 *         &lt;element name="log_level" type="{}Log_level" minOccurs="0"/>
 *         &lt;element name="history" type="{}Yes_no" minOccurs="0"/>
 *         &lt;element name="history_on_process" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;union memberTypes=" {}Yes_no {http://www.w3.org/2001/XMLSchema}nonNegativeInteger">
 *             &lt;/union>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="history_with_log" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *               &lt;enumeration value="yes"/>
 *               &lt;enumeration value="no"/>
 *               &lt;enumeration value="gzip"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Job.Settings", propOrder = {
    "mailOnError",
    "mailOnWarning",
    "mailOnSuccess",
    "mailOnProcess",
    "mailOnDelayAfterError",
    "logMailTo",
    "logMailCc",
    "logMailBcc",
    "logLevel",
    "history",
    "historyOnProcess",
    "historyWithLog"
})
public class JobSettings {

    @XmlElement(name = "mail_on_error")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String mailOnError;
    @XmlElement(name = "mail_on_warning")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String mailOnWarning;
    @XmlElement(name = "mail_on_success")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String mailOnSuccess;
    @XmlElement(name = "mail_on_process")
    protected String mailOnProcess;
    @XmlElement(name = "mail_on_delay_after_error")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String mailOnDelayAfterError;
    @XmlElement(name = "log_mail_to")
    protected String logMailTo;
    @XmlElement(name = "log_mail_cc")
    protected String logMailCc;
    @XmlElement(name = "log_mail_bcc")
    protected String logMailBcc;
    @XmlElement(name = "log_level")
    @XmlSchemaType(name = "NMTOKEN")
    protected LogLevel logLevel;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String history;
    @XmlElement(name = "history_on_process")
    protected String historyOnProcess;
    @XmlElement(name = "history_with_log")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String historyWithLog;

    /**
     * Ruft den Wert der mailOnError-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailOnError() {
        return mailOnError;
    }

    /**
     * Legt den Wert der mailOnError-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailOnError(String value) {
        this.mailOnError = value;
    }

    /**
     * Ruft den Wert der mailOnWarning-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailOnWarning() {
        return mailOnWarning;
    }

    /**
     * Legt den Wert der mailOnWarning-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailOnWarning(String value) {
        this.mailOnWarning = value;
    }

    /**
     * Ruft den Wert der mailOnSuccess-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailOnSuccess() {
        return mailOnSuccess;
    }

    /**
     * Legt den Wert der mailOnSuccess-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailOnSuccess(String value) {
        this.mailOnSuccess = value;
    }

    /**
     * Ruft den Wert der mailOnProcess-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailOnProcess() {
        return mailOnProcess;
    }

    /**
     * Legt den Wert der mailOnProcess-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailOnProcess(String value) {
        this.mailOnProcess = value;
    }

    /**
     * Ruft den Wert der mailOnDelayAfterError-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailOnDelayAfterError() {
        return mailOnDelayAfterError;
    }

    /**
     * Legt den Wert der mailOnDelayAfterError-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailOnDelayAfterError(String value) {
        this.mailOnDelayAfterError = value;
    }

    /**
     * Ruft den Wert der logMailTo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogMailTo() {
        return logMailTo;
    }

    /**
     * Legt den Wert der logMailTo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogMailTo(String value) {
        this.logMailTo = value;
    }

    /**
     * Ruft den Wert der logMailCc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogMailCc() {
        return logMailCc;
    }

    /**
     * Legt den Wert der logMailCc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogMailCc(String value) {
        this.logMailCc = value;
    }

    /**
     * Ruft den Wert der logMailBcc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogMailBcc() {
        return logMailBcc;
    }

    /**
     * Legt den Wert der logMailBcc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogMailBcc(String value) {
        this.logMailBcc = value;
    }

    /**
     * Ruft den Wert der logLevel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LogLevel }
     *     
     */
    public LogLevel getLogLevel() {
        return logLevel;
    }

    /**
     * Legt den Wert der logLevel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LogLevel }
     *     
     */
    public void setLogLevel(LogLevel value) {
        this.logLevel = value;
    }

    /**
     * Ruft den Wert der history-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHistory() {
        return history;
    }

    /**
     * Legt den Wert der history-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHistory(String value) {
        this.history = value;
    }

    /**
     * Ruft den Wert der historyOnProcess-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHistoryOnProcess() {
        return historyOnProcess;
    }

    /**
     * Legt den Wert der historyOnProcess-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHistoryOnProcess(String value) {
        this.historyOnProcess = value;
    }

    /**
     * Ruft den Wert der historyWithLog-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHistoryWithLog() {
        return historyWithLog;
    }

    /**
     * Legt den Wert der historyWithLog-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHistoryWithLog(String value) {
        this.historyWithLog = value;
    }

}
