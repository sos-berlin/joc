//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.12.14 um 03:42:09 PM CET 
//


package com.sos.auth.rest.permission.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element name="JobschedulerMaster">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="view">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="parameter" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="restart">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="abort" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="manageCategories" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="abort" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="reload" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="pause" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="continue" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="stop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="JobschedulerMasterCluster">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="restart" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="terminateFailSafe" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="DailyPlan">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="view">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="History">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="view" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Order">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="view">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="change">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="parameter" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="timeForAdhocOrder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="startAndEndNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="other" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="update" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="suspend" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="resume" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="removeSetback" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="setRunTime" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="setState" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="reset" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="modifyHotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="JobChain">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="view">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="remove" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="stop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="addOrder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="skipJobChainNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="stopJobChainNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="processJobChainNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="unstop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="modifyHotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Job">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="view">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="setRunTime" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="stop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="unstop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="suspendAllTasks" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="endAllTasks" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="kill" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="continueAllTasks" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="modifyHotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ProcessClass">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="view">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="change" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="remove" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="modifyHotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Schedule">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="view">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="change" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="remove" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="addSubstitute" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="modifyHotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Lock">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="view">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="remove" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="modifyHotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="isAuthenticated" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="user" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accessToken" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "jobschedulerMaster",
    "jobschedulerMasterCluster",
    "dailyPlan",
    "history",
    "order",
    "jobChain",
    "job",
    "processClass",
    "schedule",
    "lock"
})
@XmlRootElement(name = "SOSPermissionCommands")
public class SOSPermissionCommands {

    @XmlElement(name = "JobschedulerMaster", required = true)
    protected SOSPermissionCommands.JobschedulerMaster jobschedulerMaster;
    @XmlElement(name = "JobschedulerMasterCluster", required = true)
    protected SOSPermissionCommands.JobschedulerMasterCluster jobschedulerMasterCluster;
    @XmlElement(name = "DailyPlan", required = true)
    protected SOSPermissionCommands.DailyPlan dailyPlan;
    @XmlElement(name = "History", required = true)
    protected SOSPermissionCommands.History history;
    @XmlElement(name = "Order", required = true)
    protected SOSPermissionCommands.Order order;
    @XmlElement(name = "JobChain", required = true)
    protected SOSPermissionCommands.JobChain jobChain;
    @XmlElement(name = "Job", required = true)
    protected SOSPermissionCommands.Job job;
    @XmlElement(name = "ProcessClass", required = true)
    protected SOSPermissionCommands.ProcessClass processClass;
    @XmlElement(name = "Schedule", required = true)
    protected SOSPermissionCommands.Schedule schedule;
    @XmlElement(name = "Lock", required = true)
    protected SOSPermissionCommands.Lock lock;
    @XmlAttribute(name = "isAuthenticated")
    protected Boolean isAuthenticated;
    @XmlAttribute(name = "user")
    protected String user;
    @XmlAttribute(name = "accessToken")
    protected String accessToken;

    /**
     * Ruft den Wert der jobschedulerMaster-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionCommands.JobschedulerMaster }
     *     
     */
    public SOSPermissionCommands.JobschedulerMaster getJobschedulerMaster() {
        return jobschedulerMaster;
    }

    /**
     * Legt den Wert der jobschedulerMaster-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionCommands.JobschedulerMaster }
     *     
     */
    public void setJobschedulerMaster(SOSPermissionCommands.JobschedulerMaster value) {
        this.jobschedulerMaster = value;
    }

    /**
     * Ruft den Wert der jobschedulerMasterCluster-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionCommands.JobschedulerMasterCluster }
     *     
     */
    public SOSPermissionCommands.JobschedulerMasterCluster getJobschedulerMasterCluster() {
        return jobschedulerMasterCluster;
    }

    /**
     * Legt den Wert der jobschedulerMasterCluster-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionCommands.JobschedulerMasterCluster }
     *     
     */
    public void setJobschedulerMasterCluster(SOSPermissionCommands.JobschedulerMasterCluster value) {
        this.jobschedulerMasterCluster = value;
    }

    /**
     * Ruft den Wert der dailyPlan-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionCommands.DailyPlan }
     *     
     */
    public SOSPermissionCommands.DailyPlan getDailyPlan() {
        return dailyPlan;
    }

    /**
     * Legt den Wert der dailyPlan-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionCommands.DailyPlan }
     *     
     */
    public void setDailyPlan(SOSPermissionCommands.DailyPlan value) {
        this.dailyPlan = value;
    }

    /**
     * Ruft den Wert der history-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionCommands.History }
     *     
     */
    public SOSPermissionCommands.History getHistory() {
        return history;
    }

    /**
     * Legt den Wert der history-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionCommands.History }
     *     
     */
    public void setHistory(SOSPermissionCommands.History value) {
        this.history = value;
    }

    /**
     * Ruft den Wert der order-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionCommands.Order }
     *     
     */
    public SOSPermissionCommands.Order getOrder() {
        return order;
    }

    /**
     * Legt den Wert der order-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionCommands.Order }
     *     
     */
    public void setOrder(SOSPermissionCommands.Order value) {
        this.order = value;
    }

    /**
     * Ruft den Wert der jobChain-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionCommands.JobChain }
     *     
     */
    public SOSPermissionCommands.JobChain getJobChain() {
        return jobChain;
    }

    /**
     * Legt den Wert der jobChain-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionCommands.JobChain }
     *     
     */
    public void setJobChain(SOSPermissionCommands.JobChain value) {
        this.jobChain = value;
    }

    /**
     * Ruft den Wert der job-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionCommands.Job }
     *     
     */
    public SOSPermissionCommands.Job getJob() {
        return job;
    }

    /**
     * Legt den Wert der job-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionCommands.Job }
     *     
     */
    public void setJob(SOSPermissionCommands.Job value) {
        this.job = value;
    }

    /**
     * Ruft den Wert der processClass-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionCommands.ProcessClass }
     *     
     */
    public SOSPermissionCommands.ProcessClass getProcessClass() {
        return processClass;
    }

    /**
     * Legt den Wert der processClass-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionCommands.ProcessClass }
     *     
     */
    public void setProcessClass(SOSPermissionCommands.ProcessClass value) {
        this.processClass = value;
    }

    /**
     * Ruft den Wert der schedule-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionCommands.Schedule }
     *     
     */
    public SOSPermissionCommands.Schedule getSchedule() {
        return schedule;
    }

    /**
     * Legt den Wert der schedule-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionCommands.Schedule }
     *     
     */
    public void setSchedule(SOSPermissionCommands.Schedule value) {
        this.schedule = value;
    }

    /**
     * Ruft den Wert der lock-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionCommands.Lock }
     *     
     */
    public SOSPermissionCommands.Lock getLock() {
        return lock;
    }

    /**
     * Legt den Wert der lock-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionCommands.Lock }
     *     
     */
    public void setLock(SOSPermissionCommands.Lock value) {
        this.lock = value;
    }

    /**
     * Ruft den Wert der isAuthenticated-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsAuthenticated() {
        return isAuthenticated;
    }

    /**
     * Legt den Wert der isAuthenticated-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsAuthenticated(Boolean value) {
        this.isAuthenticated = value;
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
     * Ruft den Wert der accessToken-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Legt den Wert der accessToken-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccessToken(String value) {
        this.accessToken = value;
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
     *       &lt;sequence>
     *         &lt;element name="view">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
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
    @XmlType(name = "", propOrder = {
        "view"
    })
    public static class DailyPlan {

        @XmlElement(required = true)
        protected SOSPermissionCommands.DailyPlan.View view;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionCommands.DailyPlan.View }
         *     
         */
        public SOSPermissionCommands.DailyPlan.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionCommands.DailyPlan.View }
         *     
         */
        public void setView(SOSPermissionCommands.DailyPlan.View value) {
            this.view = value;
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
         *       &lt;sequence>
         *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "status"
        })
        public static class View {

            protected boolean status;

            /**
             * Ruft den Wert der status-Eigenschaft ab.
             * 
             */
            public boolean isStatus() {
                return status;
            }

            /**
             * Legt den Wert der status-Eigenschaft fest.
             * 
             */
            public void setStatus(boolean value) {
                this.status = value;
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
    public static class History {

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
     *       &lt;sequence>
     *         &lt;element name="view">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="setRunTime" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="stop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="unstop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="suspendAllTasks" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="endAllTasks" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="kill" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="continueAllTasks" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="modifyHotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "view",
        "setRunTime",
        "start",
        "stop",
        "unstop",
        "suspendAllTasks",
        "endAllTasks",
        "terminate",
        "kill",
        "continueAllTasks",
        "modifyHotFolder"
    })
    public static class Job {

        @XmlElement(required = true)
        protected SOSPermissionCommands.Job.View view;
        protected boolean setRunTime;
        protected boolean start;
        protected boolean stop;
        protected boolean unstop;
        protected boolean suspendAllTasks;
        protected boolean endAllTasks;
        protected boolean terminate;
        protected boolean kill;
        protected boolean continueAllTasks;
        protected boolean modifyHotFolder;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionCommands.Job.View }
         *     
         */
        public SOSPermissionCommands.Job.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionCommands.Job.View }
         *     
         */
        public void setView(SOSPermissionCommands.Job.View value) {
            this.view = value;
        }

        /**
         * Ruft den Wert der setRunTime-Eigenschaft ab.
         * 
         */
        public boolean isSetRunTime() {
            return setRunTime;
        }

        /**
         * Legt den Wert der setRunTime-Eigenschaft fest.
         * 
         */
        public void setSetRunTime(boolean value) {
            this.setRunTime = value;
        }

        /**
         * Ruft den Wert der start-Eigenschaft ab.
         * 
         */
        public boolean isStart() {
            return start;
        }

        /**
         * Legt den Wert der start-Eigenschaft fest.
         * 
         */
        public void setStart(boolean value) {
            this.start = value;
        }

        /**
         * Ruft den Wert der stop-Eigenschaft ab.
         * 
         */
        public boolean isStop() {
            return stop;
        }

        /**
         * Legt den Wert der stop-Eigenschaft fest.
         * 
         */
        public void setStop(boolean value) {
            this.stop = value;
        }

        /**
         * Ruft den Wert der unstop-Eigenschaft ab.
         * 
         */
        public boolean isUnstop() {
            return unstop;
        }

        /**
         * Legt den Wert der unstop-Eigenschaft fest.
         * 
         */
        public void setUnstop(boolean value) {
            this.unstop = value;
        }

        /**
         * Ruft den Wert der suspendAllTasks-Eigenschaft ab.
         * 
         */
        public boolean isSuspendAllTasks() {
            return suspendAllTasks;
        }

        /**
         * Legt den Wert der suspendAllTasks-Eigenschaft fest.
         * 
         */
        public void setSuspendAllTasks(boolean value) {
            this.suspendAllTasks = value;
        }

        /**
         * Ruft den Wert der endAllTasks-Eigenschaft ab.
         * 
         */
        public boolean isEndAllTasks() {
            return endAllTasks;
        }

        /**
         * Legt den Wert der endAllTasks-Eigenschaft fest.
         * 
         */
        public void setEndAllTasks(boolean value) {
            this.endAllTasks = value;
        }

        /**
         * Ruft den Wert der terminate-Eigenschaft ab.
         * 
         */
        public boolean isTerminate() {
            return terminate;
        }

        /**
         * Legt den Wert der terminate-Eigenschaft fest.
         * 
         */
        public void setTerminate(boolean value) {
            this.terminate = value;
        }

        /**
         * Ruft den Wert der kill-Eigenschaft ab.
         * 
         */
        public boolean isKill() {
            return kill;
        }

        /**
         * Legt den Wert der kill-Eigenschaft fest.
         * 
         */
        public void setKill(boolean value) {
            this.kill = value;
        }

        /**
         * Ruft den Wert der continueAllTasks-Eigenschaft ab.
         * 
         */
        public boolean isContinueAllTasks() {
            return continueAllTasks;
        }

        /**
         * Legt den Wert der continueAllTasks-Eigenschaft fest.
         * 
         */
        public void setContinueAllTasks(boolean value) {
            this.continueAllTasks = value;
        }

        /**
         * Ruft den Wert der modifyHotFolder-Eigenschaft ab.
         * 
         */
        public boolean isModifyHotFolder() {
            return modifyHotFolder;
        }

        /**
         * Legt den Wert der modifyHotFolder-Eigenschaft fest.
         * 
         */
        public void setModifyHotFolder(boolean value) {
            this.modifyHotFolder = value;
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
         *       &lt;sequence>
         *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "status"
        })
        public static class View {

            protected boolean status;

            /**
             * Ruft den Wert der status-Eigenschaft ab.
             * 
             */
            public boolean isStatus() {
                return status;
            }

            /**
             * Legt den Wert der status-Eigenschaft fest.
             * 
             */
            public void setStatus(boolean value) {
                this.status = value;
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
     *       &lt;sequence>
     *         &lt;element name="view">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="remove" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="stop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="addOrder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="skipJobChainNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="stopJobChainNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="processJobChainNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="unstop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="modifyHotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "view",
        "remove",
        "stop",
        "addOrder",
        "skipJobChainNode",
        "stopJobChainNode",
        "processJobChainNode",
        "unstop",
        "modifyHotFolder"
    })
    public static class JobChain {

        @XmlElement(required = true)
        protected SOSPermissionCommands.JobChain.View view;
        protected boolean remove;
        protected boolean stop;
        protected boolean addOrder;
        protected boolean skipJobChainNode;
        protected boolean stopJobChainNode;
        protected boolean processJobChainNode;
        protected boolean unstop;
        protected boolean modifyHotFolder;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionCommands.JobChain.View }
         *     
         */
        public SOSPermissionCommands.JobChain.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionCommands.JobChain.View }
         *     
         */
        public void setView(SOSPermissionCommands.JobChain.View value) {
            this.view = value;
        }

        /**
         * Ruft den Wert der remove-Eigenschaft ab.
         * 
         */
        public boolean isRemove() {
            return remove;
        }

        /**
         * Legt den Wert der remove-Eigenschaft fest.
         * 
         */
        public void setRemove(boolean value) {
            this.remove = value;
        }

        /**
         * Ruft den Wert der stop-Eigenschaft ab.
         * 
         */
        public boolean isStop() {
            return stop;
        }

        /**
         * Legt den Wert der stop-Eigenschaft fest.
         * 
         */
        public void setStop(boolean value) {
            this.stop = value;
        }

        /**
         * Ruft den Wert der addOrder-Eigenschaft ab.
         * 
         */
        public boolean isAddOrder() {
            return addOrder;
        }

        /**
         * Legt den Wert der addOrder-Eigenschaft fest.
         * 
         */
        public void setAddOrder(boolean value) {
            this.addOrder = value;
        }

        /**
         * Ruft den Wert der skipJobChainNode-Eigenschaft ab.
         * 
         */
        public boolean isSkipJobChainNode() {
            return skipJobChainNode;
        }

        /**
         * Legt den Wert der skipJobChainNode-Eigenschaft fest.
         * 
         */
        public void setSkipJobChainNode(boolean value) {
            this.skipJobChainNode = value;
        }

        /**
         * Ruft den Wert der stopJobChainNode-Eigenschaft ab.
         * 
         */
        public boolean isStopJobChainNode() {
            return stopJobChainNode;
        }

        /**
         * Legt den Wert der stopJobChainNode-Eigenschaft fest.
         * 
         */
        public void setStopJobChainNode(boolean value) {
            this.stopJobChainNode = value;
        }

        /**
         * Ruft den Wert der processJobChainNode-Eigenschaft ab.
         * 
         */
        public boolean isProcessJobChainNode() {
            return processJobChainNode;
        }

        /**
         * Legt den Wert der processJobChainNode-Eigenschaft fest.
         * 
         */
        public void setProcessJobChainNode(boolean value) {
            this.processJobChainNode = value;
        }

        /**
         * Ruft den Wert der unstop-Eigenschaft ab.
         * 
         */
        public boolean isUnstop() {
            return unstop;
        }

        /**
         * Legt den Wert der unstop-Eigenschaft fest.
         * 
         */
        public void setUnstop(boolean value) {
            this.unstop = value;
        }

        /**
         * Ruft den Wert der modifyHotFolder-Eigenschaft ab.
         * 
         */
        public boolean isModifyHotFolder() {
            return modifyHotFolder;
        }

        /**
         * Legt den Wert der modifyHotFolder-Eigenschaft fest.
         * 
         */
        public void setModifyHotFolder(boolean value) {
            this.modifyHotFolder = value;
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
         *       &lt;sequence>
         *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "status"
        })
        public static class View {

            protected boolean status;

            /**
             * Ruft den Wert der status-Eigenschaft ab.
             * 
             */
            public boolean isStatus() {
                return status;
            }

            /**
             * Legt den Wert der status-Eigenschaft fest.
             * 
             */
            public void setStatus(boolean value) {
                this.status = value;
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
     *       &lt;sequence>
     *         &lt;element name="view">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="parameter" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="restart">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="abort" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="manageCategories" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="abort" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="reload" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="pause" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="continue" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="stop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "view",
        "restart",
        "manageCategories",
        "abort",
        "terminate",
        "reload",
        "pause",
        "_continue",
        "stop"
    })
    public static class JobschedulerMaster {

        @XmlElement(required = true)
        protected SOSPermissionCommands.JobschedulerMaster.View view;
        @XmlElement(required = true)
        protected SOSPermissionCommands.JobschedulerMaster.Restart restart;
        protected boolean manageCategories;
        protected boolean abort;
        protected boolean terminate;
        protected boolean reload;
        protected boolean pause;
        @XmlElement(name = "continue")
        protected boolean _continue;
        protected boolean stop;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionCommands.JobschedulerMaster.View }
         *     
         */
        public SOSPermissionCommands.JobschedulerMaster.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionCommands.JobschedulerMaster.View }
         *     
         */
        public void setView(SOSPermissionCommands.JobschedulerMaster.View value) {
            this.view = value;
        }

        /**
         * Ruft den Wert der restart-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionCommands.JobschedulerMaster.Restart }
         *     
         */
        public SOSPermissionCommands.JobschedulerMaster.Restart getRestart() {
            return restart;
        }

        /**
         * Legt den Wert der restart-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionCommands.JobschedulerMaster.Restart }
         *     
         */
        public void setRestart(SOSPermissionCommands.JobschedulerMaster.Restart value) {
            this.restart = value;
        }

        /**
         * Ruft den Wert der manageCategories-Eigenschaft ab.
         * 
         */
        public boolean isManageCategories() {
            return manageCategories;
        }

        /**
         * Legt den Wert der manageCategories-Eigenschaft fest.
         * 
         */
        public void setManageCategories(boolean value) {
            this.manageCategories = value;
        }

        /**
         * Ruft den Wert der abort-Eigenschaft ab.
         * 
         */
        public boolean isAbort() {
            return abort;
        }

        /**
         * Legt den Wert der abort-Eigenschaft fest.
         * 
         */
        public void setAbort(boolean value) {
            this.abort = value;
        }

        /**
         * Ruft den Wert der terminate-Eigenschaft ab.
         * 
         */
        public boolean isTerminate() {
            return terminate;
        }

        /**
         * Legt den Wert der terminate-Eigenschaft fest.
         * 
         */
        public void setTerminate(boolean value) {
            this.terminate = value;
        }

        /**
         * Ruft den Wert der reload-Eigenschaft ab.
         * 
         */
        public boolean isReload() {
            return reload;
        }

        /**
         * Legt den Wert der reload-Eigenschaft fest.
         * 
         */
        public void setReload(boolean value) {
            this.reload = value;
        }

        /**
         * Ruft den Wert der pause-Eigenschaft ab.
         * 
         */
        public boolean isPause() {
            return pause;
        }

        /**
         * Legt den Wert der pause-Eigenschaft fest.
         * 
         */
        public void setPause(boolean value) {
            this.pause = value;
        }

        /**
         * Ruft den Wert der continue-Eigenschaft ab.
         * 
         */
        public boolean isContinue() {
            return _continue;
        }

        /**
         * Legt den Wert der continue-Eigenschaft fest.
         * 
         */
        public void setContinue(boolean value) {
            this._continue = value;
        }

        /**
         * Ruft den Wert der stop-Eigenschaft ab.
         * 
         */
        public boolean isStop() {
            return stop;
        }

        /**
         * Legt den Wert der stop-Eigenschaft fest.
         * 
         */
        public void setStop(boolean value) {
            this.stop = value;
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
         *       &lt;sequence>
         *         &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="abort" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "terminate",
            "abort"
        })
        public static class Restart {

            protected boolean terminate;
            protected boolean abort;

            /**
             * Ruft den Wert der terminate-Eigenschaft ab.
             * 
             */
            public boolean isTerminate() {
                return terminate;
            }

            /**
             * Legt den Wert der terminate-Eigenschaft fest.
             * 
             */
            public void setTerminate(boolean value) {
                this.terminate = value;
            }

            /**
             * Ruft den Wert der abort-Eigenschaft ab.
             * 
             */
            public boolean isAbort() {
                return abort;
            }

            /**
             * Legt den Wert der abort-Eigenschaft fest.
             * 
             */
            public void setAbort(boolean value) {
                this.abort = value;
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
         *       &lt;sequence>
         *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="parameter" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "status",
            "parameter"
        })
        public static class View {

            protected boolean status;
            protected boolean parameter;

            /**
             * Ruft den Wert der status-Eigenschaft ab.
             * 
             */
            public boolean isStatus() {
                return status;
            }

            /**
             * Legt den Wert der status-Eigenschaft fest.
             * 
             */
            public void setStatus(boolean value) {
                this.status = value;
            }

            /**
             * Ruft den Wert der parameter-Eigenschaft ab.
             * 
             */
            public boolean isParameter() {
                return parameter;
            }

            /**
             * Legt den Wert der parameter-Eigenschaft fest.
             * 
             */
            public void setParameter(boolean value) {
                this.parameter = value;
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
     *       &lt;sequence>
     *         &lt;element name="restart" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="terminateFailSafe" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "restart",
        "terminate",
        "terminateFailSafe"
    })
    public static class JobschedulerMasterCluster {

        protected boolean restart;
        protected boolean terminate;
        protected boolean terminateFailSafe;

        /**
         * Ruft den Wert der restart-Eigenschaft ab.
         * 
         */
        public boolean isRestart() {
            return restart;
        }

        /**
         * Legt den Wert der restart-Eigenschaft fest.
         * 
         */
        public void setRestart(boolean value) {
            this.restart = value;
        }

        /**
         * Ruft den Wert der terminate-Eigenschaft ab.
         * 
         */
        public boolean isTerminate() {
            return terminate;
        }

        /**
         * Legt den Wert der terminate-Eigenschaft fest.
         * 
         */
        public void setTerminate(boolean value) {
            this.terminate = value;
        }

        /**
         * Ruft den Wert der terminateFailSafe-Eigenschaft ab.
         * 
         */
        public boolean isTerminateFailSafe() {
            return terminateFailSafe;
        }

        /**
         * Legt den Wert der terminateFailSafe-Eigenschaft fest.
         * 
         */
        public void setTerminateFailSafe(boolean value) {
            this.terminateFailSafe = value;
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
     *       &lt;sequence>
     *         &lt;element name="view">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="remove" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="modifyHotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "view",
        "remove",
        "modifyHotFolder"
    })
    public static class Lock {

        @XmlElement(required = true)
        protected SOSPermissionCommands.Lock.View view;
        protected boolean remove;
        protected boolean modifyHotFolder;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionCommands.Lock.View }
         *     
         */
        public SOSPermissionCommands.Lock.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionCommands.Lock.View }
         *     
         */
        public void setView(SOSPermissionCommands.Lock.View value) {
            this.view = value;
        }

        /**
         * Ruft den Wert der remove-Eigenschaft ab.
         * 
         */
        public boolean isRemove() {
            return remove;
        }

        /**
         * Legt den Wert der remove-Eigenschaft fest.
         * 
         */
        public void setRemove(boolean value) {
            this.remove = value;
        }

        /**
         * Ruft den Wert der modifyHotFolder-Eigenschaft ab.
         * 
         */
        public boolean isModifyHotFolder() {
            return modifyHotFolder;
        }

        /**
         * Legt den Wert der modifyHotFolder-Eigenschaft fest.
         * 
         */
        public void setModifyHotFolder(boolean value) {
            this.modifyHotFolder = value;
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
         *       &lt;sequence>
         *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "status"
        })
        public static class View {

            protected boolean status;

            /**
             * Ruft den Wert der status-Eigenschaft ab.
             * 
             */
            public boolean isStatus() {
                return status;
            }

            /**
             * Legt den Wert der status-Eigenschaft fest.
             * 
             */
            public void setStatus(boolean value) {
                this.status = value;
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
     *       &lt;sequence>
     *         &lt;element name="view">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="change">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="parameter" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="timeForAdhocOrder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="startAndEndNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="other" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="update" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="suspend" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="resume" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="removeSetback" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="setRunTime" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="setState" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="reset" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="modifyHotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "view",
        "change",
        "start",
        "update",
        "suspend",
        "resume",
        "delete",
        "removeSetback",
        "setRunTime",
        "setState",
        "reset",
        "modifyHotFolder"
    })
    public static class Order {

        @XmlElement(required = true)
        protected SOSPermissionCommands.Order.View view;
        @XmlElement(required = true)
        protected SOSPermissionCommands.Order.Change change;
        protected boolean start;
        protected boolean update;
        protected boolean suspend;
        protected boolean resume;
        protected boolean delete;
        protected boolean removeSetback;
        protected boolean setRunTime;
        protected boolean setState;
        protected boolean reset;
        protected boolean modifyHotFolder;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionCommands.Order.View }
         *     
         */
        public SOSPermissionCommands.Order.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionCommands.Order.View }
         *     
         */
        public void setView(SOSPermissionCommands.Order.View value) {
            this.view = value;
        }

        /**
         * Ruft den Wert der change-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionCommands.Order.Change }
         *     
         */
        public SOSPermissionCommands.Order.Change getChange() {
            return change;
        }

        /**
         * Legt den Wert der change-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionCommands.Order.Change }
         *     
         */
        public void setChange(SOSPermissionCommands.Order.Change value) {
            this.change = value;
        }

        /**
         * Ruft den Wert der start-Eigenschaft ab.
         * 
         */
        public boolean isStart() {
            return start;
        }

        /**
         * Legt den Wert der start-Eigenschaft fest.
         * 
         */
        public void setStart(boolean value) {
            this.start = value;
        }

        /**
         * Ruft den Wert der update-Eigenschaft ab.
         * 
         */
        public boolean isUpdate() {
            return update;
        }

        /**
         * Legt den Wert der update-Eigenschaft fest.
         * 
         */
        public void setUpdate(boolean value) {
            this.update = value;
        }

        /**
         * Ruft den Wert der suspend-Eigenschaft ab.
         * 
         */
        public boolean isSuspend() {
            return suspend;
        }

        /**
         * Legt den Wert der suspend-Eigenschaft fest.
         * 
         */
        public void setSuspend(boolean value) {
            this.suspend = value;
        }

        /**
         * Ruft den Wert der resume-Eigenschaft ab.
         * 
         */
        public boolean isResume() {
            return resume;
        }

        /**
         * Legt den Wert der resume-Eigenschaft fest.
         * 
         */
        public void setResume(boolean value) {
            this.resume = value;
        }

        /**
         * Ruft den Wert der delete-Eigenschaft ab.
         * 
         */
        public boolean isDelete() {
            return delete;
        }

        /**
         * Legt den Wert der delete-Eigenschaft fest.
         * 
         */
        public void setDelete(boolean value) {
            this.delete = value;
        }

        /**
         * Ruft den Wert der removeSetback-Eigenschaft ab.
         * 
         */
        public boolean isRemoveSetback() {
            return removeSetback;
        }

        /**
         * Legt den Wert der removeSetback-Eigenschaft fest.
         * 
         */
        public void setRemoveSetback(boolean value) {
            this.removeSetback = value;
        }

        /**
         * Ruft den Wert der setRunTime-Eigenschaft ab.
         * 
         */
        public boolean isSetRunTime() {
            return setRunTime;
        }

        /**
         * Legt den Wert der setRunTime-Eigenschaft fest.
         * 
         */
        public void setSetRunTime(boolean value) {
            this.setRunTime = value;
        }

        /**
         * Ruft den Wert der setState-Eigenschaft ab.
         * 
         */
        public boolean isSetState() {
            return setState;
        }

        /**
         * Legt den Wert der setState-Eigenschaft fest.
         * 
         */
        public void setSetState(boolean value) {
            this.setState = value;
        }

        /**
         * Ruft den Wert der reset-Eigenschaft ab.
         * 
         */
        public boolean isReset() {
            return reset;
        }

        /**
         * Legt den Wert der reset-Eigenschaft fest.
         * 
         */
        public void setReset(boolean value) {
            this.reset = value;
        }

        /**
         * Ruft den Wert der modifyHotFolder-Eigenschaft ab.
         * 
         */
        public boolean isModifyHotFolder() {
            return modifyHotFolder;
        }

        /**
         * Legt den Wert der modifyHotFolder-Eigenschaft fest.
         * 
         */
        public void setModifyHotFolder(boolean value) {
            this.modifyHotFolder = value;
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
         *       &lt;sequence>
         *         &lt;element name="parameter" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="timeForAdhocOrder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="startAndEndNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="other" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "parameter",
            "timeForAdhocOrder",
            "startAndEndNode",
            "other"
        })
        public static class Change {

            protected boolean parameter;
            protected boolean timeForAdhocOrder;
            protected boolean startAndEndNode;
            protected boolean other;

            /**
             * Ruft den Wert der parameter-Eigenschaft ab.
             * 
             */
            public boolean isParameter() {
                return parameter;
            }

            /**
             * Legt den Wert der parameter-Eigenschaft fest.
             * 
             */
            public void setParameter(boolean value) {
                this.parameter = value;
            }

            /**
             * Ruft den Wert der timeForAdhocOrder-Eigenschaft ab.
             * 
             */
            public boolean isTimeForAdhocOrder() {
                return timeForAdhocOrder;
            }

            /**
             * Legt den Wert der timeForAdhocOrder-Eigenschaft fest.
             * 
             */
            public void setTimeForAdhocOrder(boolean value) {
                this.timeForAdhocOrder = value;
            }

            /**
             * Ruft den Wert der startAndEndNode-Eigenschaft ab.
             * 
             */
            public boolean isStartAndEndNode() {
                return startAndEndNode;
            }

            /**
             * Legt den Wert der startAndEndNode-Eigenschaft fest.
             * 
             */
            public void setStartAndEndNode(boolean value) {
                this.startAndEndNode = value;
            }

            /**
             * Ruft den Wert der other-Eigenschaft ab.
             * 
             */
            public boolean isOther() {
                return other;
            }

            /**
             * Legt den Wert der other-Eigenschaft fest.
             * 
             */
            public void setOther(boolean value) {
                this.other = value;
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
         *       &lt;sequence>
         *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "status"
        })
        public static class View {

            protected boolean status;

            /**
             * Ruft den Wert der status-Eigenschaft ab.
             * 
             */
            public boolean isStatus() {
                return status;
            }

            /**
             * Legt den Wert der status-Eigenschaft fest.
             * 
             */
            public void setStatus(boolean value) {
                this.status = value;
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
     *       &lt;sequence>
     *         &lt;element name="view">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="change" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="remove" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="modifyHotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "view",
        "change",
        "remove",
        "modifyHotFolder"
    })
    public static class ProcessClass {

        @XmlElement(required = true)
        protected SOSPermissionCommands.ProcessClass.View view;
        protected boolean change;
        protected boolean remove;
        protected boolean modifyHotFolder;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionCommands.ProcessClass.View }
         *     
         */
        public SOSPermissionCommands.ProcessClass.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionCommands.ProcessClass.View }
         *     
         */
        public void setView(SOSPermissionCommands.ProcessClass.View value) {
            this.view = value;
        }

        /**
         * Ruft den Wert der change-Eigenschaft ab.
         * 
         */
        public boolean isChange() {
            return change;
        }

        /**
         * Legt den Wert der change-Eigenschaft fest.
         * 
         */
        public void setChange(boolean value) {
            this.change = value;
        }

        /**
         * Ruft den Wert der remove-Eigenschaft ab.
         * 
         */
        public boolean isRemove() {
            return remove;
        }

        /**
         * Legt den Wert der remove-Eigenschaft fest.
         * 
         */
        public void setRemove(boolean value) {
            this.remove = value;
        }

        /**
         * Ruft den Wert der modifyHotFolder-Eigenschaft ab.
         * 
         */
        public boolean isModifyHotFolder() {
            return modifyHotFolder;
        }

        /**
         * Legt den Wert der modifyHotFolder-Eigenschaft fest.
         * 
         */
        public void setModifyHotFolder(boolean value) {
            this.modifyHotFolder = value;
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
         *       &lt;sequence>
         *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "status"
        })
        public static class View {

            protected boolean status;

            /**
             * Ruft den Wert der status-Eigenschaft ab.
             * 
             */
            public boolean isStatus() {
                return status;
            }

            /**
             * Legt den Wert der status-Eigenschaft fest.
             * 
             */
            public void setStatus(boolean value) {
                this.status = value;
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
     *       &lt;sequence>
     *         &lt;element name="view">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="change" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="remove" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="addSubstitute" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="modifyHotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "view",
        "change",
        "remove",
        "addSubstitute",
        "modifyHotFolder"
    })
    public static class Schedule {

        @XmlElement(required = true)
        protected SOSPermissionCommands.Schedule.View view;
        protected boolean change;
        protected boolean remove;
        protected boolean addSubstitute;
        protected boolean modifyHotFolder;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionCommands.Schedule.View }
         *     
         */
        public SOSPermissionCommands.Schedule.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionCommands.Schedule.View }
         *     
         */
        public void setView(SOSPermissionCommands.Schedule.View value) {
            this.view = value;
        }

        /**
         * Ruft den Wert der change-Eigenschaft ab.
         * 
         */
        public boolean isChange() {
            return change;
        }

        /**
         * Legt den Wert der change-Eigenschaft fest.
         * 
         */
        public void setChange(boolean value) {
            this.change = value;
        }

        /**
         * Ruft den Wert der remove-Eigenschaft ab.
         * 
         */
        public boolean isRemove() {
            return remove;
        }

        /**
         * Legt den Wert der remove-Eigenschaft fest.
         * 
         */
        public void setRemove(boolean value) {
            this.remove = value;
        }

        /**
         * Ruft den Wert der addSubstitute-Eigenschaft ab.
         * 
         */
        public boolean isAddSubstitute() {
            return addSubstitute;
        }

        /**
         * Legt den Wert der addSubstitute-Eigenschaft fest.
         * 
         */
        public void setAddSubstitute(boolean value) {
            this.addSubstitute = value;
        }

        /**
         * Ruft den Wert der modifyHotFolder-Eigenschaft ab.
         * 
         */
        public boolean isModifyHotFolder() {
            return modifyHotFolder;
        }

        /**
         * Legt den Wert der modifyHotFolder-Eigenschaft fest.
         * 
         */
        public void setModifyHotFolder(boolean value) {
            this.modifyHotFolder = value;
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
         *       &lt;sequence>
         *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "status"
        })
        public static class View {

            protected boolean status;

            /**
             * Ruft den Wert der status-Eigenschaft ab.
             * 
             */
            public boolean isStatus() {
                return status;
            }

            /**
             * Legt den Wert der status-Eigenschaft fest.
             * 
             */
            public void setStatus(boolean value) {
                this.status = value;
            }

        }

    }

}
