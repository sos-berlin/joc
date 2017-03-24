//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.03.24 um 02:41:35 PM CET 
//


package com.sos.joc.model.commands;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element ref="{}extensions" minOccurs="0"/>
 *         &lt;element name="settings" type="{}Job.Settings" minOccurs="0"/>
 *         &lt;element name="description" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element ref="{}include"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="lock.use" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="lock" use="required" type="{}String" />
 *                 &lt;attribute name="exclusive" type="{}Yes_no" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{}params" minOccurs="0"/>
 *         &lt;element name="environment" type="{}environment" minOccurs="0"/>
 *         &lt;element name="login" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="password.plain">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="password" use="required" type="{}String" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/choice>
 *                 &lt;attribute name="user" use="required" type="{}String" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{}script" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{}monitor"/>
 *           &lt;element name="monitor.use">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="monitor" use="required" type="{}Path" />
 *                   &lt;attribute name="ordering" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
 *         &lt;element name="start_when_directory_changed" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="directory" use="required" type="{}File" />
 *                 &lt;attribute name="regex" type="{}String" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="delay_after_error" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="error_count" use="required" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *                 &lt;attribute name="delay" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{}String">
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="delay_order_after_setback" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="setback_count" use="required" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *                 &lt;attribute name="delay" type="{}Duration" />
 *                 &lt;attribute name="is_maximum" type="{}Yes_no" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="run_time" type="{}run_time" minOccurs="0"/>
 *         &lt;element name="commands" type="{}Commands" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{}Name" />
 *       &lt;attribute name="visible" type="{}Yes_no_never" />
 *       &lt;attribute name="priority" type="{}Process_priority" />
 *       &lt;attribute name="temporary" type="{}Yes_no" />
 *       &lt;attribute name="spooler_id" type="{}Name" />
 *       &lt;attribute name="title" type="{}String" />
 *       &lt;attribute name="log_append" type="{}Yes_no" />
 *       &lt;attribute name="order" type="{}Yes_no" />
 *       &lt;attribute name="separate_process" type="{}Yes_no" />
 *       &lt;attribute name="tasks" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="min_tasks" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *       &lt;attribute name="timeout" type="{}Duration" />
 *       &lt;attribute name="idle_timeout" type="{}Duration_or_never" />
 *       &lt;attribute name="force_idle_timeout" type="{}Yes_no" />
 *       &lt;attribute name="process_class" type="{}Path" />
 *       &lt;attribute name="mail_xslt_stylesheet" type="{}File" />
 *       &lt;attribute name="java_options" type="{}String" />
 *       &lt;attribute name="stop_on_error" type="{}Yes_no" />
 *       &lt;attribute name="ignore_signals">
 *         &lt;simpleType>
 *           &lt;union>
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *                 &lt;enumeration value="all"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *             &lt;simpleType>
 *               &lt;list itemType="{}Unix_signal" />
 *             &lt;/simpleType>
 *           &lt;/union>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="replace" type="{}Yes_no" />
 *       &lt;attribute name="warn_if_longer_than" type="{}Duration_or_percentage" />
 *       &lt;attribute name="warn_if_shorter_than" type="{}Duration_or_percentage" />
 *       &lt;attribute name="enabled" type="{}Yes_no" />
 *       &lt;attribute name="stderr_log_level" type="{}Log_level" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "extensions",
    "settings",
    "description",
    "lockUse",
    "params",
    "environment",
    "login",
    "script",
    "monitorOrMonitorUse",
    "startWhenDirectoryChanged",
    "delayAfterError",
    "delayOrderAfterSetback",
    "runTime",
    "commands"
})
@XmlRootElement(name = "job")
public class Job {

    protected Extensions extensions;
    protected JobSettings settings;
    protected Job.Description description;
    @XmlElement(name = "lock.use")
    protected List<Job.LockUse> lockUse;
    protected Params params;
    protected Environment environment;
    protected Job.Login login;
    protected Script script;
    @XmlElements({
        @XmlElement(name = "monitor", type = Monitor.class),
        @XmlElement(name = "monitor.use", type = Job.MonitorUse.class)
    })
    protected List<Object> monitorOrMonitorUse;
    @XmlElement(name = "start_when_directory_changed")
    protected List<Job.StartWhenDirectoryChanged> startWhenDirectoryChanged;
    @XmlElement(name = "delay_after_error")
    protected List<Job.DelayAfterError> delayAfterError;
    @XmlElement(name = "delay_order_after_setback")
    protected List<Job.DelayOrderAfterSetback> delayOrderAfterSetback;
    @XmlElement(name = "run_time")
    protected RunTime runTime;
    protected List<Commands> commands;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "visible")
    protected String visible;
    @XmlAttribute(name = "priority")
    protected List<String> priority;
    @XmlAttribute(name = "temporary")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String temporary;
    @XmlAttribute(name = "spooler_id")
    protected String spoolerId;
    @XmlAttribute(name = "title")
    protected String title;
    @XmlAttribute(name = "log_append")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String logAppend;
    @XmlAttribute(name = "order")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String order;
    @XmlAttribute(name = "separate_process")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String separateProcess;
    @XmlAttribute(name = "tasks")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger tasks;
    @XmlAttribute(name = "min_tasks")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger minTasks;
    @XmlAttribute(name = "timeout")
    protected String timeout;
    @XmlAttribute(name = "idle_timeout")
    protected String idleTimeout;
    @XmlAttribute(name = "force_idle_timeout")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String forceIdleTimeout;
    @XmlAttribute(name = "process_class")
    protected String processClass;
    @XmlAttribute(name = "mail_xslt_stylesheet")
    protected String mailXsltStylesheet;
    @XmlAttribute(name = "java_options")
    protected String javaOptions;
    @XmlAttribute(name = "stop_on_error")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String stopOnError;
    @XmlAttribute(name = "ignore_signals")
    protected List<String> ignoreSignals;
    @XmlAttribute(name = "replace")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String replace;
    @XmlAttribute(name = "warn_if_longer_than")
    protected String warnIfLongerThan;
    @XmlAttribute(name = "warn_if_shorter_than")
    protected String warnIfShorterThan;
    @XmlAttribute(name = "enabled")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String enabled;
    @XmlAttribute(name = "stderr_log_level")
    protected LogLevel stderrLogLevel;

    /**
     * Ruft den Wert der extensions-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Extensions }
     *     
     */
    public Extensions getExtensions() {
        return extensions;
    }

    /**
     * Legt den Wert der extensions-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Extensions }
     *     
     */
    public void setExtensions(Extensions value) {
        this.extensions = value;
    }

    /**
     * Ruft den Wert der settings-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JobSettings }
     *     
     */
    public JobSettings getSettings() {
        return settings;
    }

    /**
     * Legt den Wert der settings-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JobSettings }
     *     
     */
    public void setSettings(JobSettings value) {
        this.settings = value;
    }

    /**
     * Ruft den Wert der description-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Job.Description }
     *     
     */
    public Job.Description getDescription() {
        return description;
    }

    /**
     * Legt den Wert der description-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Job.Description }
     *     
     */
    public void setDescription(Job.Description value) {
        this.description = value;
    }

    /**
     * Gets the value of the lockUse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lockUse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLockUse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Job.LockUse }
     * 
     * 
     */
    public List<Job.LockUse> getLockUse() {
        if (lockUse == null) {
            lockUse = new ArrayList<Job.LockUse>();
        }
        return this.lockUse;
    }

    /**
     * Ruft den Wert der params-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Params }
     *     
     */
    public Params getParams() {
        return params;
    }

    /**
     * Legt den Wert der params-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Params }
     *     
     */
    public void setParams(Params value) {
        this.params = value;
    }

    /**
     * Ruft den Wert der environment-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Environment }
     *     
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Legt den Wert der environment-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Environment }
     *     
     */
    public void setEnvironment(Environment value) {
        this.environment = value;
    }

    /**
     * Ruft den Wert der login-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Job.Login }
     *     
     */
    public Job.Login getLogin() {
        return login;
    }

    /**
     * Legt den Wert der login-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Job.Login }
     *     
     */
    public void setLogin(Job.Login value) {
        this.login = value;
    }

    /**
     * Ruft den Wert der script-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Script }
     *     
     */
    public Script getScript() {
        return script;
    }

    /**
     * Legt den Wert der script-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Script }
     *     
     */
    public void setScript(Script value) {
        this.script = value;
    }

    /**
     * Gets the value of the monitorOrMonitorUse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the monitorOrMonitorUse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMonitorOrMonitorUse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Monitor }
     * {@link Job.MonitorUse }
     * 
     * 
     */
    public List<Object> getMonitorOrMonitorUse() {
        if (monitorOrMonitorUse == null) {
            monitorOrMonitorUse = new ArrayList<Object>();
        }
        return this.monitorOrMonitorUse;
    }

    /**
     * Gets the value of the startWhenDirectoryChanged property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the startWhenDirectoryChanged property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStartWhenDirectoryChanged().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Job.StartWhenDirectoryChanged }
     * 
     * 
     */
    public List<Job.StartWhenDirectoryChanged> getStartWhenDirectoryChanged() {
        if (startWhenDirectoryChanged == null) {
            startWhenDirectoryChanged = new ArrayList<Job.StartWhenDirectoryChanged>();
        }
        return this.startWhenDirectoryChanged;
    }

    /**
     * Gets the value of the delayAfterError property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the delayAfterError property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDelayAfterError().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Job.DelayAfterError }
     * 
     * 
     */
    public List<Job.DelayAfterError> getDelayAfterError() {
        if (delayAfterError == null) {
            delayAfterError = new ArrayList<Job.DelayAfterError>();
        }
        return this.delayAfterError;
    }

    /**
     * Gets the value of the delayOrderAfterSetback property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the delayOrderAfterSetback property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDelayOrderAfterSetback().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Job.DelayOrderAfterSetback }
     * 
     * 
     */
    public List<Job.DelayOrderAfterSetback> getDelayOrderAfterSetback() {
        if (delayOrderAfterSetback == null) {
            delayOrderAfterSetback = new ArrayList<Job.DelayOrderAfterSetback>();
        }
        return this.delayOrderAfterSetback;
    }

    /**
     * Ruft den Wert der runTime-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RunTime }
     *     
     */
    public RunTime getRunTime() {
        return runTime;
    }

    /**
     * Legt den Wert der runTime-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RunTime }
     *     
     */
    public void setRunTime(RunTime value) {
        this.runTime = value;
    }

    /**
     * Gets the value of the commands property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the commands property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCommands().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Commands }
     * 
     * 
     */
    public List<Commands> getCommands() {
        if (commands == null) {
            commands = new ArrayList<Commands>();
        }
        return this.commands;
    }

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Ruft den Wert der visible-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVisible() {
        return visible;
    }

    /**
     * Legt den Wert der visible-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVisible(String value) {
        this.visible = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the priority property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPriority().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPriority() {
        if (priority == null) {
            priority = new ArrayList<String>();
        }
        return this.priority;
    }

    /**
     * Ruft den Wert der temporary-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemporary() {
        return temporary;
    }

    /**
     * Legt den Wert der temporary-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemporary(String value) {
        this.temporary = value;
    }

    /**
     * Ruft den Wert der spoolerId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpoolerId() {
        return spoolerId;
    }

    /**
     * Legt den Wert der spoolerId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpoolerId(String value) {
        this.spoolerId = value;
    }

    /**
     * Ruft den Wert der title-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Legt den Wert der title-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Ruft den Wert der logAppend-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogAppend() {
        return logAppend;
    }

    /**
     * Legt den Wert der logAppend-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogAppend(String value) {
        this.logAppend = value;
    }

    /**
     * Ruft den Wert der order-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrder() {
        return order;
    }

    /**
     * Legt den Wert der order-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrder(String value) {
        this.order = value;
    }

    /**
     * Ruft den Wert der separateProcess-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeparateProcess() {
        return separateProcess;
    }

    /**
     * Legt den Wert der separateProcess-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeparateProcess(String value) {
        this.separateProcess = value;
    }

    /**
     * Ruft den Wert der tasks-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTasks() {
        return tasks;
    }

    /**
     * Legt den Wert der tasks-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTasks(BigInteger value) {
        this.tasks = value;
    }

    /**
     * Ruft den Wert der minTasks-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMinTasks() {
        return minTasks;
    }

    /**
     * Legt den Wert der minTasks-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMinTasks(BigInteger value) {
        this.minTasks = value;
    }

    /**
     * Ruft den Wert der timeout-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeout() {
        return timeout;
    }

    /**
     * Legt den Wert der timeout-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeout(String value) {
        this.timeout = value;
    }

    /**
     * Ruft den Wert der idleTimeout-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdleTimeout() {
        return idleTimeout;
    }

    /**
     * Legt den Wert der idleTimeout-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdleTimeout(String value) {
        this.idleTimeout = value;
    }

    /**
     * Ruft den Wert der forceIdleTimeout-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForceIdleTimeout() {
        return forceIdleTimeout;
    }

    /**
     * Legt den Wert der forceIdleTimeout-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForceIdleTimeout(String value) {
        this.forceIdleTimeout = value;
    }

    /**
     * Ruft den Wert der processClass-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessClass() {
        return processClass;
    }

    /**
     * Legt den Wert der processClass-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessClass(String value) {
        this.processClass = value;
    }

    /**
     * Ruft den Wert der mailXsltStylesheet-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailXsltStylesheet() {
        return mailXsltStylesheet;
    }

    /**
     * Legt den Wert der mailXsltStylesheet-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailXsltStylesheet(String value) {
        this.mailXsltStylesheet = value;
    }

    /**
     * Ruft den Wert der javaOptions-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJavaOptions() {
        return javaOptions;
    }

    /**
     * Legt den Wert der javaOptions-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJavaOptions(String value) {
        this.javaOptions = value;
    }

    /**
     * Ruft den Wert der stopOnError-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStopOnError() {
        return stopOnError;
    }

    /**
     * Legt den Wert der stopOnError-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStopOnError(String value) {
        this.stopOnError = value;
    }

    /**
     * Gets the value of the ignoreSignals property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ignoreSignals property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIgnoreSignals().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getIgnoreSignals() {
        if (ignoreSignals == null) {
            ignoreSignals = new ArrayList<String>();
        }
        return this.ignoreSignals;
    }

    /**
     * Ruft den Wert der replace-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReplace() {
        return replace;
    }

    /**
     * Legt den Wert der replace-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReplace(String value) {
        this.replace = value;
    }

    /**
     * Ruft den Wert der warnIfLongerThan-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarnIfLongerThan() {
        return warnIfLongerThan;
    }

    /**
     * Legt den Wert der warnIfLongerThan-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarnIfLongerThan(String value) {
        this.warnIfLongerThan = value;
    }

    /**
     * Ruft den Wert der warnIfShorterThan-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarnIfShorterThan() {
        return warnIfShorterThan;
    }

    /**
     * Legt den Wert der warnIfShorterThan-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarnIfShorterThan(String value) {
        this.warnIfShorterThan = value;
    }

    /**
     * Ruft den Wert der enabled-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnabled() {
        return enabled;
    }

    /**
     * Legt den Wert der enabled-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnabled(String value) {
        this.enabled = value;
    }

    /**
     * Ruft den Wert der stderrLogLevel-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LogLevel }
     *     
     */
    public LogLevel getStderrLogLevel() {
        return stderrLogLevel;
    }

    /**
     * Legt den Wert der stderrLogLevel-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LogLevel }
     *     
     */
    public void setStderrLogLevel(LogLevel value) {
        this.stderrLogLevel = value;
    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="error_count" use="required" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
     *       &lt;attribute name="delay" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{}String">
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class DelayAfterError {

        @XmlAttribute(name = "error_count", required = true)
        @XmlSchemaType(name = "positiveInteger")
        protected BigInteger errorCount;
        @XmlAttribute(name = "delay", required = true)
        protected String delay;

        /**
         * Ruft den Wert der errorCount-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getErrorCount() {
            return errorCount;
        }

        /**
         * Legt den Wert der errorCount-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setErrorCount(BigInteger value) {
            this.errorCount = value;
        }

        /**
         * Ruft den Wert der delay-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDelay() {
            return delay;
        }

        /**
         * Legt den Wert der delay-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDelay(String value) {
            this.delay = value;
        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="setback_count" use="required" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
     *       &lt;attribute name="delay" type="{}Duration" />
     *       &lt;attribute name="is_maximum" type="{}Yes_no" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class DelayOrderAfterSetback {

        @XmlAttribute(name = "setback_count", required = true)
        @XmlSchemaType(name = "positiveInteger")
        protected BigInteger setbackCount;
        @XmlAttribute(name = "delay")
        protected String delay;
        @XmlAttribute(name = "is_maximum")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        protected String isMaximum;

        /**
         * Ruft den Wert der setbackCount-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getSetbackCount() {
            return setbackCount;
        }

        /**
         * Legt den Wert der setbackCount-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setSetbackCount(BigInteger value) {
            this.setbackCount = value;
        }

        /**
         * Ruft den Wert der delay-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDelay() {
            return delay;
        }

        /**
         * Legt den Wert der delay-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDelay(String value) {
            this.delay = value;
        }

        /**
         * Ruft den Wert der isMaximum-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIsMaximum() {
            return isMaximum;
        }

        /**
         * Legt den Wert der isMaximum-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIsMaximum(String value) {
            this.isMaximum = value;
        }

    }


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
     *         &lt;element ref="{}include"/>
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
        "content"
    })
    public static class Description {

        @XmlElementRef(name = "include", type = Include.class, required = false)
        @XmlMixed
        protected List<Object> content;

        /**
         * Gets the value of the content property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the content property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getContent().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * {@link Include }
         * 
         * 
         */
        public List<Object> getContent() {
            if (content == null) {
                content = new ArrayList<Object>();
            }
            return this.content;
        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="lock" use="required" type="{}String" />
     *       &lt;attribute name="exclusive" type="{}Yes_no" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class LockUse {

        @XmlAttribute(name = "lock", required = true)
        protected String lock;
        @XmlAttribute(name = "exclusive")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        protected String exclusive;

        /**
         * Ruft den Wert der lock-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLock() {
            return lock;
        }

        /**
         * Legt den Wert der lock-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLock(String value) {
            this.lock = value;
        }

        /**
         * Ruft den Wert der exclusive-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getExclusive() {
            return exclusive;
        }

        /**
         * Legt den Wert der exclusive-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setExclusive(String value) {
            this.exclusive = value;
        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="password.plain">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="password" use="required" type="{}String" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/choice>
     *       &lt;attribute name="user" use="required" type="{}String" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "passwordPlain"
    })
    public static class Login {

        @XmlElement(name = "password.plain")
        protected Job.Login.PasswordPlain passwordPlain;
        @XmlAttribute(name = "user", required = true)
        protected String user;

        /**
         * Ruft den Wert der passwordPlain-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Job.Login.PasswordPlain }
         *     
         */
        public Job.Login.PasswordPlain getPasswordPlain() {
            return passwordPlain;
        }

        /**
         * Legt den Wert der passwordPlain-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Job.Login.PasswordPlain }
         *     
         */
        public void setPasswordPlain(Job.Login.PasswordPlain value) {
            this.passwordPlain = value;
        }

        /**
         * Ruft den Wert der user-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUser() {
            return user;
        }

        /**
         * Legt den Wert der user-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUser(String value) {
            this.user = value;
        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="password" use="required" type="{}String" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class PasswordPlain {

            @XmlAttribute(name = "password", required = true)
            protected String password;

            /**
             * Ruft den Wert der password-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPassword() {
                return password;
            }

            /**
             * Legt den Wert der password-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPassword(String value) {
                this.password = value;
            }

        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="monitor" use="required" type="{}Path" />
     *       &lt;attribute name="ordering" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class MonitorUse {

        @XmlAttribute(name = "monitor", required = true)
        protected String monitor;
        @XmlAttribute(name = "ordering")
        @XmlSchemaType(name = "nonNegativeInteger")
        protected BigInteger ordering;

        /**
         * Ruft den Wert der monitor-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMonitor() {
            return monitor;
        }

        /**
         * Legt den Wert der monitor-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMonitor(String value) {
            this.monitor = value;
        }

        /**
         * Ruft den Wert der ordering-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getOrdering() {
            return ordering;
        }

        /**
         * Legt den Wert der ordering-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setOrdering(BigInteger value) {
            this.ordering = value;
        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="directory" use="required" type="{}File" />
     *       &lt;attribute name="regex" type="{}String" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class StartWhenDirectoryChanged {

        @XmlAttribute(name = "directory", required = true)
        protected String directory;
        @XmlAttribute(name = "regex")
        protected String regex;

        /**
         * Ruft den Wert der directory-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDirectory() {
            return directory;
        }

        /**
         * Legt den Wert der directory-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDirectory(String value) {
            this.directory = value;
        }

        /**
         * Ruft den Wert der regex-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRegex() {
            return regex;
        }

        /**
         * Legt den Wert der regex-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRegex(String value) {
            this.regex = value;
        }

    }

}
