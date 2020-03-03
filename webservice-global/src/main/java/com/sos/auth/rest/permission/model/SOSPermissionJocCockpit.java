//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.03.03 um 05:35:10 PM CET 
//


package com.sos.auth.rest.permission.model;

import java.io.Serializable;
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
 *                             &lt;element name="mainlog" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="parameter" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="execute">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="restart">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                       &lt;element name="abort" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="pause" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="continue" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="abort" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="administration">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="removeOldInstances" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="manageCategories" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="editPermissions" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="editMainSection" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                             &lt;element name="configurations">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="view" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                       &lt;element name="edit" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                       &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                       &lt;element name="deploy">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="job" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                                 &lt;element name="jobChain" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                                 &lt;element name="order" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                                 &lt;element name="processClass" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                                 &lt;element name="schedule" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                                 &lt;element name="lock" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                                 &lt;element name="monitor" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                                 &lt;element name="xmlEditor" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
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
 *         &lt;element name="JobschedulerMasterCluster">
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
 *                   &lt;element name="execute">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="restart" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="terminateFailSafe" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *         &lt;element name="JobschedulerUniversalAgent">
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
 *                   &lt;element name="execute">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="restart">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                       &lt;element name="abort" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="abort" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                             &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="orderLog" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                             &lt;element name="startAndEndNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="timeForAdhocOrder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="runTime" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="parameter" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="delete">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="temporary" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="permanent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="execute">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="update" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="suspend" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="resume" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="reset" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="removeSetback" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="assignDocumentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                             &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="history" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="execute">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="stop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="unstop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="addOrder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="skipJobChainNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="processJobChainNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="stopJobChainNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="assignDocumentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                             &lt;element name="taskLog" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="history" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                             &lt;element name="runTime" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="execute">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="stop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="unstop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="kill" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="endAllTasks" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="suspendAllTasks" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="continueAllTasks" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="assignDocumentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                             &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="assignDocumentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                             &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                             &lt;element name="addSubstitute" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="editContent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="assignDocumentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                             &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="assignDocumentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Event">
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
 *                   &lt;element name="execute">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="add" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *         &lt;element name="HolidayCalendar">
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
 *         &lt;element name="MaintenanceWindow">
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
 *                   &lt;element name="enableDisableMaintenanceWindow" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="AuditLog">
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
 *         &lt;element name="JOCConfigurations">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="share">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="change">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="editContent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                       &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                       &lt;element name="sharedStatus">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="makePrivate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                                 &lt;element name="makeShared" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="view">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
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
 *         &lt;element ref="{}SOSPermissionRoles"/>
 *         &lt;element name="YADE">
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
 *                             &lt;element name="files" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="execute">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="transferStart" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *         &lt;element name="Calendar">
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
 *                             &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="edit">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="create" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="change" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="assign">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="runtime" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                       &lt;element name="nonworking" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                       &lt;element name="change" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="assignDocumentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Runtime">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="execute">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="editXml" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *         &lt;element name="Joc">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="view">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="log" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *         &lt;element name="Documentation">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="view" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="import" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="export" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="JobStream">
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
 *                             &lt;element name="graph" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="eventlist" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                             &lt;element name="conditions" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="jobStream" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="events">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="add" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                       &lt;element name="remove" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
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
 *       &lt;/sequence>
 *       &lt;attribute name="isAuthenticated" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="user" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accessToken" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="jobschedulerId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="precedence" type="{http://www.w3.org/2001/XMLSchema}int" />
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
    "jobschedulerUniversalAgent",
    "dailyPlan",
    "history",
    "order",
    "jobChain",
    "job",
    "processClass",
    "schedule",
    "lock",
    "event",
    "holidayCalendar",
    "maintenanceWindow",
    "auditLog",
    "jocConfigurations",
    "sosPermissionRoles",
    "yade",
    "calendar",
    "runtime",
    "joc",
    "documentation",
    "jobStream"
})
@XmlRootElement(name = "SOSPermissionJocCockpit")
public class SOSPermissionJocCockpit
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "JobschedulerMaster", required = true)
    protected SOSPermissionJocCockpit.JobschedulerMaster jobschedulerMaster;
    @XmlElement(name = "JobschedulerMasterCluster", required = true)
    protected SOSPermissionJocCockpit.JobschedulerMasterCluster jobschedulerMasterCluster;
    @XmlElement(name = "JobschedulerUniversalAgent", required = true)
    protected SOSPermissionJocCockpit.JobschedulerUniversalAgent jobschedulerUniversalAgent;
    @XmlElement(name = "DailyPlan", required = true)
    protected SOSPermissionJocCockpit.DailyPlan dailyPlan;
    @XmlElement(name = "History", required = true)
    protected SOSPermissionJocCockpit.History history;
    @XmlElement(name = "Order", required = true)
    protected SOSPermissionJocCockpit.Order order;
    @XmlElement(name = "JobChain", required = true)
    protected SOSPermissionJocCockpit.JobChain jobChain;
    @XmlElement(name = "Job", required = true)
    protected SOSPermissionJocCockpit.Job job;
    @XmlElement(name = "ProcessClass", required = true)
    protected SOSPermissionJocCockpit.ProcessClass processClass;
    @XmlElement(name = "Schedule", required = true)
    protected SOSPermissionJocCockpit.Schedule schedule;
    @XmlElement(name = "Lock", required = true)
    protected SOSPermissionJocCockpit.Lock lock;
    @XmlElement(name = "Event", required = true)
    protected SOSPermissionJocCockpit.Event event;
    @XmlElement(name = "HolidayCalendar", required = true)
    protected SOSPermissionJocCockpit.HolidayCalendar holidayCalendar;
    @XmlElement(name = "MaintenanceWindow", required = true)
    protected SOSPermissionJocCockpit.MaintenanceWindow maintenanceWindow;
    @XmlElement(name = "AuditLog", required = true)
    protected SOSPermissionJocCockpit.AuditLog auditLog;
    @XmlElement(name = "JOCConfigurations", required = true)
    protected SOSPermissionJocCockpit.JOCConfigurations jocConfigurations;
    @XmlElement(name = "SOSPermissionRoles", required = true)
    protected SOSPermissionRoles sosPermissionRoles;
    @XmlElement(name = "YADE", required = true)
    protected SOSPermissionJocCockpit.YADE yade;
    @XmlElement(name = "Calendar", required = true)
    protected SOSPermissionJocCockpit.Calendar calendar;
    @XmlElement(name = "Runtime", required = true)
    protected SOSPermissionJocCockpit.Runtime runtime;
    @XmlElement(name = "Joc", required = true)
    protected SOSPermissionJocCockpit.Joc joc;
    @XmlElement(name = "Documentation", required = true)
    protected SOSPermissionJocCockpit.Documentation documentation;
    @XmlElement(name = "JobStream", required = true)
    protected SOSPermissionJocCockpit.JobStream jobStream;
    @XmlAttribute(name = "isAuthenticated")
    protected Boolean isAuthenticated;
    @XmlAttribute(name = "user")
    protected String user;
    @XmlAttribute(name = "accessToken")
    protected String accessToken;
    @XmlAttribute(name = "jobschedulerId")
    protected String jobschedulerId;
    @XmlAttribute(name = "precedence")
    protected Integer precedence;

    /**
     * Ruft den Wert der jobschedulerMaster-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.JobschedulerMaster }
     *     
     */
    public SOSPermissionJocCockpit.JobschedulerMaster getJobschedulerMaster() {
        return jobschedulerMaster;
    }

    /**
     * Legt den Wert der jobschedulerMaster-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.JobschedulerMaster }
     *     
     */
    public void setJobschedulerMaster(SOSPermissionJocCockpit.JobschedulerMaster value) {
        this.jobschedulerMaster = value;
    }

    public boolean isSetJobschedulerMaster() {
        return (this.jobschedulerMaster!= null);
    }

    /**
     * Ruft den Wert der jobschedulerMasterCluster-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.JobschedulerMasterCluster }
     *     
     */
    public SOSPermissionJocCockpit.JobschedulerMasterCluster getJobschedulerMasterCluster() {
        return jobschedulerMasterCluster;
    }

    /**
     * Legt den Wert der jobschedulerMasterCluster-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.JobschedulerMasterCluster }
     *     
     */
    public void setJobschedulerMasterCluster(SOSPermissionJocCockpit.JobschedulerMasterCluster value) {
        this.jobschedulerMasterCluster = value;
    }

    public boolean isSetJobschedulerMasterCluster() {
        return (this.jobschedulerMasterCluster!= null);
    }

    /**
     * Ruft den Wert der jobschedulerUniversalAgent-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.JobschedulerUniversalAgent }
     *     
     */
    public SOSPermissionJocCockpit.JobschedulerUniversalAgent getJobschedulerUniversalAgent() {
        return jobschedulerUniversalAgent;
    }

    /**
     * Legt den Wert der jobschedulerUniversalAgent-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.JobschedulerUniversalAgent }
     *     
     */
    public void setJobschedulerUniversalAgent(SOSPermissionJocCockpit.JobschedulerUniversalAgent value) {
        this.jobschedulerUniversalAgent = value;
    }

    public boolean isSetJobschedulerUniversalAgent() {
        return (this.jobschedulerUniversalAgent!= null);
    }

    /**
     * Ruft den Wert der dailyPlan-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.DailyPlan }
     *     
     */
    public SOSPermissionJocCockpit.DailyPlan getDailyPlan() {
        return dailyPlan;
    }

    /**
     * Legt den Wert der dailyPlan-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.DailyPlan }
     *     
     */
    public void setDailyPlan(SOSPermissionJocCockpit.DailyPlan value) {
        this.dailyPlan = value;
    }

    public boolean isSetDailyPlan() {
        return (this.dailyPlan!= null);
    }

    /**
     * Ruft den Wert der history-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.History }
     *     
     */
    public SOSPermissionJocCockpit.History getHistory() {
        return history;
    }

    /**
     * Legt den Wert der history-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.History }
     *     
     */
    public void setHistory(SOSPermissionJocCockpit.History value) {
        this.history = value;
    }

    public boolean isSetHistory() {
        return (this.history!= null);
    }

    /**
     * Ruft den Wert der order-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.Order }
     *     
     */
    public SOSPermissionJocCockpit.Order getOrder() {
        return order;
    }

    /**
     * Legt den Wert der order-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.Order }
     *     
     */
    public void setOrder(SOSPermissionJocCockpit.Order value) {
        this.order = value;
    }

    public boolean isSetOrder() {
        return (this.order!= null);
    }

    /**
     * Ruft den Wert der jobChain-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.JobChain }
     *     
     */
    public SOSPermissionJocCockpit.JobChain getJobChain() {
        return jobChain;
    }

    /**
     * Legt den Wert der jobChain-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.JobChain }
     *     
     */
    public void setJobChain(SOSPermissionJocCockpit.JobChain value) {
        this.jobChain = value;
    }

    public boolean isSetJobChain() {
        return (this.jobChain!= null);
    }

    /**
     * Ruft den Wert der job-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.Job }
     *     
     */
    public SOSPermissionJocCockpit.Job getJob() {
        return job;
    }

    /**
     * Legt den Wert der job-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.Job }
     *     
     */
    public void setJob(SOSPermissionJocCockpit.Job value) {
        this.job = value;
    }

    public boolean isSetJob() {
        return (this.job!= null);
    }

    /**
     * Ruft den Wert der processClass-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.ProcessClass }
     *     
     */
    public SOSPermissionJocCockpit.ProcessClass getProcessClass() {
        return processClass;
    }

    /**
     * Legt den Wert der processClass-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.ProcessClass }
     *     
     */
    public void setProcessClass(SOSPermissionJocCockpit.ProcessClass value) {
        this.processClass = value;
    }

    public boolean isSetProcessClass() {
        return (this.processClass!= null);
    }

    /**
     * Ruft den Wert der schedule-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.Schedule }
     *     
     */
    public SOSPermissionJocCockpit.Schedule getSchedule() {
        return schedule;
    }

    /**
     * Legt den Wert der schedule-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.Schedule }
     *     
     */
    public void setSchedule(SOSPermissionJocCockpit.Schedule value) {
        this.schedule = value;
    }

    public boolean isSetSchedule() {
        return (this.schedule!= null);
    }

    /**
     * Ruft den Wert der lock-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.Lock }
     *     
     */
    public SOSPermissionJocCockpit.Lock getLock() {
        return lock;
    }

    /**
     * Legt den Wert der lock-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.Lock }
     *     
     */
    public void setLock(SOSPermissionJocCockpit.Lock value) {
        this.lock = value;
    }

    public boolean isSetLock() {
        return (this.lock!= null);
    }

    /**
     * Ruft den Wert der event-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.Event }
     *     
     */
    public SOSPermissionJocCockpit.Event getEvent() {
        return event;
    }

    /**
     * Legt den Wert der event-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.Event }
     *     
     */
    public void setEvent(SOSPermissionJocCockpit.Event value) {
        this.event = value;
    }

    public boolean isSetEvent() {
        return (this.event!= null);
    }

    /**
     * Ruft den Wert der holidayCalendar-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.HolidayCalendar }
     *     
     */
    public SOSPermissionJocCockpit.HolidayCalendar getHolidayCalendar() {
        return holidayCalendar;
    }

    /**
     * Legt den Wert der holidayCalendar-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.HolidayCalendar }
     *     
     */
    public void setHolidayCalendar(SOSPermissionJocCockpit.HolidayCalendar value) {
        this.holidayCalendar = value;
    }

    public boolean isSetHolidayCalendar() {
        return (this.holidayCalendar!= null);
    }

    /**
     * Ruft den Wert der maintenanceWindow-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.MaintenanceWindow }
     *     
     */
    public SOSPermissionJocCockpit.MaintenanceWindow getMaintenanceWindow() {
        return maintenanceWindow;
    }

    /**
     * Legt den Wert der maintenanceWindow-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.MaintenanceWindow }
     *     
     */
    public void setMaintenanceWindow(SOSPermissionJocCockpit.MaintenanceWindow value) {
        this.maintenanceWindow = value;
    }

    public boolean isSetMaintenanceWindow() {
        return (this.maintenanceWindow!= null);
    }

    /**
     * Ruft den Wert der auditLog-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.AuditLog }
     *     
     */
    public SOSPermissionJocCockpit.AuditLog getAuditLog() {
        return auditLog;
    }

    /**
     * Legt den Wert der auditLog-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.AuditLog }
     *     
     */
    public void setAuditLog(SOSPermissionJocCockpit.AuditLog value) {
        this.auditLog = value;
    }

    public boolean isSetAuditLog() {
        return (this.auditLog!= null);
    }

    /**
     * Ruft den Wert der jocConfigurations-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.JOCConfigurations }
     *     
     */
    public SOSPermissionJocCockpit.JOCConfigurations getJOCConfigurations() {
        return jocConfigurations;
    }

    /**
     * Legt den Wert der jocConfigurations-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.JOCConfigurations }
     *     
     */
    public void setJOCConfigurations(SOSPermissionJocCockpit.JOCConfigurations value) {
        this.jocConfigurations = value;
    }

    public boolean isSetJOCConfigurations() {
        return (this.jocConfigurations!= null);
    }

    /**
     * Ruft den Wert der sosPermissionRoles-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionRoles }
     *     
     */
    public SOSPermissionRoles getSOSPermissionRoles() {
        return sosPermissionRoles;
    }

    /**
     * Legt den Wert der sosPermissionRoles-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionRoles }
     *     
     */
    public void setSOSPermissionRoles(SOSPermissionRoles value) {
        this.sosPermissionRoles = value;
    }

    public boolean isSetSOSPermissionRoles() {
        return (this.sosPermissionRoles!= null);
    }

    /**
     * Ruft den Wert der yade-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.YADE }
     *     
     */
    public SOSPermissionJocCockpit.YADE getYADE() {
        return yade;
    }

    /**
     * Legt den Wert der yade-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.YADE }
     *     
     */
    public void setYADE(SOSPermissionJocCockpit.YADE value) {
        this.yade = value;
    }

    public boolean isSetYADE() {
        return (this.yade!= null);
    }

    /**
     * Ruft den Wert der calendar-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.Calendar }
     *     
     */
    public SOSPermissionJocCockpit.Calendar getCalendar() {
        return calendar;
    }

    /**
     * Legt den Wert der calendar-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.Calendar }
     *     
     */
    public void setCalendar(SOSPermissionJocCockpit.Calendar value) {
        this.calendar = value;
    }

    public boolean isSetCalendar() {
        return (this.calendar!= null);
    }

    /**
     * Ruft den Wert der runtime-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.Runtime }
     *     
     */
    public SOSPermissionJocCockpit.Runtime getRuntime() {
        return runtime;
    }

    /**
     * Legt den Wert der runtime-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.Runtime }
     *     
     */
    public void setRuntime(SOSPermissionJocCockpit.Runtime value) {
        this.runtime = value;
    }

    public boolean isSetRuntime() {
        return (this.runtime!= null);
    }

    /**
     * Ruft den Wert der joc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.Joc }
     *     
     */
    public SOSPermissionJocCockpit.Joc getJoc() {
        return joc;
    }

    /**
     * Legt den Wert der joc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.Joc }
     *     
     */
    public void setJoc(SOSPermissionJocCockpit.Joc value) {
        this.joc = value;
    }

    public boolean isSetJoc() {
        return (this.joc!= null);
    }

    /**
     * Ruft den Wert der documentation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.Documentation }
     *     
     */
    public SOSPermissionJocCockpit.Documentation getDocumentation() {
        return documentation;
    }

    /**
     * Legt den Wert der documentation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.Documentation }
     *     
     */
    public void setDocumentation(SOSPermissionJocCockpit.Documentation value) {
        this.documentation = value;
    }

    public boolean isSetDocumentation() {
        return (this.documentation!= null);
    }

    /**
     * Ruft den Wert der jobStream-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.JobStream }
     *     
     */
    public SOSPermissionJocCockpit.JobStream getJobStream() {
        return jobStream;
    }

    /**
     * Legt den Wert der jobStream-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.JobStream }
     *     
     */
    public void setJobStream(SOSPermissionJocCockpit.JobStream value) {
        this.jobStream = value;
    }

    public boolean isSetJobStream() {
        return (this.jobStream!= null);
    }

    /**
     * Ruft den Wert der isAuthenticated-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIsAuthenticated() {
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
    public void setIsAuthenticated(boolean value) {
        this.isAuthenticated = value;
    }

    public boolean isSetIsAuthenticated() {
        return (this.isAuthenticated!= null);
    }

    public void unsetIsAuthenticated() {
        this.isAuthenticated = null;
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

    public boolean isSetUser() {
        return (this.user!= null);
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

    public boolean isSetAccessToken() {
        return (this.accessToken!= null);
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

    public boolean isSetJobschedulerId() {
        return (this.jobschedulerId!= null);
    }

    /**
     * Ruft den Wert der precedence-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getPrecedence() {
        return precedence;
    }

    /**
     * Legt den Wert der precedence-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPrecedence(int value) {
        this.precedence = value;
    }

    public boolean isSetPrecedence() {
        return (this.precedence!= null);
    }

    public void unsetPrecedence() {
        this.precedence = null;
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
    public static class AuditLog
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.AuditLog.View view;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.AuditLog.View }
         *     
         */
        public SOSPermissionJocCockpit.AuditLog.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.AuditLog.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.AuditLog.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
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
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
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

            public boolean isSetStatus() {
                return true;
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
     *                   &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="edit">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="create" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="change" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="assign">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="runtime" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                             &lt;element name="nonworking" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                             &lt;element name="change" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
     *         &lt;element name="assignDocumentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "edit",
        "assignDocumentation"
    })
    public static class Calendar
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Calendar.View view;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Calendar.Edit edit;
        protected boolean assignDocumentation;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.Calendar.View }
         *     
         */
        public SOSPermissionJocCockpit.Calendar.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.Calendar.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.Calendar.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
        }

        /**
         * Ruft den Wert der edit-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.Calendar.Edit }
         *     
         */
        public SOSPermissionJocCockpit.Calendar.Edit getEdit() {
            return edit;
        }

        /**
         * Legt den Wert der edit-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.Calendar.Edit }
         *     
         */
        public void setEdit(SOSPermissionJocCockpit.Calendar.Edit value) {
            this.edit = value;
        }

        public boolean isSetEdit() {
            return (this.edit!= null);
        }

        /**
         * Ruft den Wert der assignDocumentation-Eigenschaft ab.
         * 
         */
        public boolean isAssignDocumentation() {
            return assignDocumentation;
        }

        /**
         * Legt den Wert der assignDocumentation-Eigenschaft fest.
         * 
         */
        public void setAssignDocumentation(boolean value) {
            this.assignDocumentation = value;
        }

        public boolean isSetAssignDocumentation() {
            return true;
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
         *         &lt;element name="create" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="change" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="assign">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="runtime" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                   &lt;element name="nonworking" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                   &lt;element name="change" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "create",
            "change",
            "delete",
            "assign"
        })
        public static class Edit
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean create;
            protected boolean change;
            protected boolean delete;
            @XmlElement(required = true)
            protected SOSPermissionJocCockpit.Calendar.Edit.Assign assign;

            /**
             * Ruft den Wert der create-Eigenschaft ab.
             * 
             */
            public boolean isCreate() {
                return create;
            }

            /**
             * Legt den Wert der create-Eigenschaft fest.
             * 
             */
            public void setCreate(boolean value) {
                this.create = value;
            }

            public boolean isSetCreate() {
                return true;
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

            public boolean isSetChange() {
                return true;
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

            public boolean isSetDelete() {
                return true;
            }

            /**
             * Ruft den Wert der assign-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link SOSPermissionJocCockpit.Calendar.Edit.Assign }
             *     
             */
            public SOSPermissionJocCockpit.Calendar.Edit.Assign getAssign() {
                return assign;
            }

            /**
             * Legt den Wert der assign-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link SOSPermissionJocCockpit.Calendar.Edit.Assign }
             *     
             */
            public void setAssign(SOSPermissionJocCockpit.Calendar.Edit.Assign value) {
                this.assign = value;
            }

            public boolean isSetAssign() {
                return (this.assign!= null);
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
             *         &lt;element name="runtime" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *         &lt;element name="nonworking" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *         &lt;element name="change" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
                "runtime",
                "nonworking",
                "change"
            })
            public static class Assign
                implements Serializable
            {

                private final static long serialVersionUID = 12343L;
                protected boolean runtime;
                protected boolean nonworking;
                protected boolean change;

                /**
                 * Ruft den Wert der runtime-Eigenschaft ab.
                 * 
                 */
                public boolean isRuntime() {
                    return runtime;
                }

                /**
                 * Legt den Wert der runtime-Eigenschaft fest.
                 * 
                 */
                public void setRuntime(boolean value) {
                    this.runtime = value;
                }

                public boolean isSetRuntime() {
                    return true;
                }

                /**
                 * Ruft den Wert der nonworking-Eigenschaft ab.
                 * 
                 */
                public boolean isNonworking() {
                    return nonworking;
                }

                /**
                 * Legt den Wert der nonworking-Eigenschaft fest.
                 * 
                 */
                public void setNonworking(boolean value) {
                    this.nonworking = value;
                }

                public boolean isSetNonworking() {
                    return true;
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

                public boolean isSetChange() {
                    return true;
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
         *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "documentation"
        })
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean status;
            protected boolean documentation;

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

            public boolean isSetStatus() {
                return true;
            }

            /**
             * Ruft den Wert der documentation-Eigenschaft ab.
             * 
             */
            public boolean isDocumentation() {
                return documentation;
            }

            /**
             * Legt den Wert der documentation-Eigenschaft fest.
             * 
             */
            public void setDocumentation(boolean value) {
                this.documentation = value;
            }

            public boolean isSetDocumentation() {
                return true;
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
    public static class DailyPlan
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.DailyPlan.View view;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.DailyPlan.View }
         *     
         */
        public SOSPermissionJocCockpit.DailyPlan.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.DailyPlan.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.DailyPlan.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
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
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
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

            public boolean isSetStatus() {
                return true;
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
     *         &lt;element name="import" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="export" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "_import",
        "export",
        "delete"
    })
    public static class Documentation
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        protected boolean view;
        @XmlElement(name = "import")
        protected boolean _import;
        protected boolean export;
        protected boolean delete;

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

        /**
         * Ruft den Wert der import-Eigenschaft ab.
         * 
         */
        public boolean isImport() {
            return _import;
        }

        /**
         * Legt den Wert der import-Eigenschaft fest.
         * 
         */
        public void setImport(boolean value) {
            this._import = value;
        }

        public boolean isSetImport() {
            return true;
        }

        /**
         * Ruft den Wert der export-Eigenschaft ab.
         * 
         */
        public boolean isExport() {
            return export;
        }

        /**
         * Legt den Wert der export-Eigenschaft fest.
         * 
         */
        public void setExport(boolean value) {
            this.export = value;
        }

        public boolean isSetExport() {
            return true;
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

        public boolean isSetDelete() {
            return true;
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
     *         &lt;element name="execute">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="add" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "view",
        "execute"
    })
    public static class Event
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Event.View view;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Event.Execute execute;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.Event.View }
         *     
         */
        public SOSPermissionJocCockpit.Event.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.Event.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.Event.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
        }

        /**
         * Ruft den Wert der execute-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.Event.Execute }
         *     
         */
        public SOSPermissionJocCockpit.Event.Execute getExecute() {
            return execute;
        }

        /**
         * Legt den Wert der execute-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.Event.Execute }
         *     
         */
        public void setExecute(SOSPermissionJocCockpit.Event.Execute value) {
            this.execute = value;
        }

        public boolean isSetExecute() {
            return (this.execute!= null);
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
         *         &lt;element name="add" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "add",
            "delete"
        })
        public static class Execute
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean add;
            protected boolean delete;

            /**
             * Ruft den Wert der add-Eigenschaft ab.
             * 
             */
            public boolean isAdd() {
                return add;
            }

            /**
             * Legt den Wert der add-Eigenschaft fest.
             * 
             */
            public void setAdd(boolean value) {
                this.add = value;
            }

            public boolean isSetAdd() {
                return true;
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

            public boolean isSetDelete() {
                return true;
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
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
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

            public boolean isSetStatus() {
                return true;
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
    public static class History
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.History.View view;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.History.View }
         *     
         */
        public SOSPermissionJocCockpit.History.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.History.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.History.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
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
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
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

            public boolean isSetStatus() {
                return true;
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
    public static class HolidayCalendar
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.HolidayCalendar.View view;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.HolidayCalendar.View }
         *     
         */
        public SOSPermissionJocCockpit.HolidayCalendar.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.HolidayCalendar.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.HolidayCalendar.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
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
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
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

            public boolean isSetStatus() {
                return true;
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
     *         &lt;element name="share">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="change">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="editContent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                             &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                             &lt;element name="sharedStatus">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="makePrivate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                                       &lt;element name="makeShared" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
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
        "share"
    })
    public static class JOCConfigurations
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.JOCConfigurations.Share share;

        /**
         * Ruft den Wert der share-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.JOCConfigurations.Share }
         *     
         */
        public SOSPermissionJocCockpit.JOCConfigurations.Share getShare() {
            return share;
        }

        /**
         * Legt den Wert der share-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.JOCConfigurations.Share }
         *     
         */
        public void setShare(SOSPermissionJocCockpit.JOCConfigurations.Share value) {
            this.share = value;
        }

        public boolean isSetShare() {
            return (this.share!= null);
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
         *         &lt;element name="change">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="editContent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                   &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                   &lt;element name="sharedStatus">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="makePrivate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                             &lt;element name="makeShared" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "change",
            "view"
        })
        public static class Share
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            @XmlElement(required = true)
            protected SOSPermissionJocCockpit.JOCConfigurations.Share.Change change;
            @XmlElement(required = true)
            protected SOSPermissionJocCockpit.JOCConfigurations.Share.View view;

            /**
             * Ruft den Wert der change-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link SOSPermissionJocCockpit.JOCConfigurations.Share.Change }
             *     
             */
            public SOSPermissionJocCockpit.JOCConfigurations.Share.Change getChange() {
                return change;
            }

            /**
             * Legt den Wert der change-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link SOSPermissionJocCockpit.JOCConfigurations.Share.Change }
             *     
             */
            public void setChange(SOSPermissionJocCockpit.JOCConfigurations.Share.Change value) {
                this.change = value;
            }

            public boolean isSetChange() {
                return (this.change!= null);
            }

            /**
             * Ruft den Wert der view-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link SOSPermissionJocCockpit.JOCConfigurations.Share.View }
             *     
             */
            public SOSPermissionJocCockpit.JOCConfigurations.Share.View getView() {
                return view;
            }

            /**
             * Legt den Wert der view-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link SOSPermissionJocCockpit.JOCConfigurations.Share.View }
             *     
             */
            public void setView(SOSPermissionJocCockpit.JOCConfigurations.Share.View value) {
                this.view = value;
            }

            public boolean isSetView() {
                return (this.view!= null);
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
             *         &lt;element name="editContent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *         &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *         &lt;element name="sharedStatus">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="makePrivate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *                   &lt;element name="makeShared" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
                "editContent",
                "delete",
                "sharedStatus"
            })
            public static class Change
                implements Serializable
            {

                private final static long serialVersionUID = 12343L;
                protected boolean editContent;
                protected boolean delete;
                @XmlElement(required = true)
                protected SOSPermissionJocCockpit.JOCConfigurations.Share.Change.SharedStatus sharedStatus;

                /**
                 * Ruft den Wert der editContent-Eigenschaft ab.
                 * 
                 */
                public boolean isEditContent() {
                    return editContent;
                }

                /**
                 * Legt den Wert der editContent-Eigenschaft fest.
                 * 
                 */
                public void setEditContent(boolean value) {
                    this.editContent = value;
                }

                public boolean isSetEditContent() {
                    return true;
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

                public boolean isSetDelete() {
                    return true;
                }

                /**
                 * Ruft den Wert der sharedStatus-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link SOSPermissionJocCockpit.JOCConfigurations.Share.Change.SharedStatus }
                 *     
                 */
                public SOSPermissionJocCockpit.JOCConfigurations.Share.Change.SharedStatus getSharedStatus() {
                    return sharedStatus;
                }

                /**
                 * Legt den Wert der sharedStatus-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link SOSPermissionJocCockpit.JOCConfigurations.Share.Change.SharedStatus }
                 *     
                 */
                public void setSharedStatus(SOSPermissionJocCockpit.JOCConfigurations.Share.Change.SharedStatus value) {
                    this.sharedStatus = value;
                }

                public boolean isSetSharedStatus() {
                    return (this.sharedStatus!= null);
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
                 *         &lt;element name="makePrivate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
                 *         &lt;element name="makeShared" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
                    "makePrivate",
                    "makeShared"
                })
                public static class SharedStatus
                    implements Serializable
                {

                    private final static long serialVersionUID = 12343L;
                    protected boolean makePrivate;
                    protected boolean makeShared;

                    /**
                     * Ruft den Wert der makePrivate-Eigenschaft ab.
                     * 
                     */
                    public boolean isMakePrivate() {
                        return makePrivate;
                    }

                    /**
                     * Legt den Wert der makePrivate-Eigenschaft fest.
                     * 
                     */
                    public void setMakePrivate(boolean value) {
                        this.makePrivate = value;
                    }

                    public boolean isSetMakePrivate() {
                        return true;
                    }

                    /**
                     * Ruft den Wert der makeShared-Eigenschaft ab.
                     * 
                     */
                    public boolean isMakeShared() {
                        return makeShared;
                    }

                    /**
                     * Legt den Wert der makeShared-Eigenschaft fest.
                     * 
                     */
                    public void setMakeShared(boolean value) {
                        this.makeShared = value;
                    }

                    public boolean isSetMakeShared() {
                        return true;
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
            public static class View
                implements Serializable
            {

                private final static long serialVersionUID = 12343L;
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

                public boolean isSetStatus() {
                    return true;
                }

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
     *                   &lt;element name="taskLog" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="history" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
     *                   &lt;element name="runTime" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="execute">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="stop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="unstop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="kill" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="endAllTasks" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="suspendAllTasks" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="continueAllTasks" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="assignDocumentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "execute",
        "assignDocumentation"
    })
    public static class Job
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Job.View view;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Job.Change change;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Job.Execute execute;
        protected boolean assignDocumentation;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.Job.View }
         *     
         */
        public SOSPermissionJocCockpit.Job.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.Job.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.Job.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
        }

        /**
         * Ruft den Wert der change-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.Job.Change }
         *     
         */
        public SOSPermissionJocCockpit.Job.Change getChange() {
            return change;
        }

        /**
         * Legt den Wert der change-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.Job.Change }
         *     
         */
        public void setChange(SOSPermissionJocCockpit.Job.Change value) {
            this.change = value;
        }

        public boolean isSetChange() {
            return (this.change!= null);
        }

        /**
         * Ruft den Wert der execute-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.Job.Execute }
         *     
         */
        public SOSPermissionJocCockpit.Job.Execute getExecute() {
            return execute;
        }

        /**
         * Legt den Wert der execute-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.Job.Execute }
         *     
         */
        public void setExecute(SOSPermissionJocCockpit.Job.Execute value) {
            this.execute = value;
        }

        public boolean isSetExecute() {
            return (this.execute!= null);
        }

        /**
         * Ruft den Wert der assignDocumentation-Eigenschaft ab.
         * 
         */
        public boolean isAssignDocumentation() {
            return assignDocumentation;
        }

        /**
         * Legt den Wert der assignDocumentation-Eigenschaft fest.
         * 
         */
        public void setAssignDocumentation(boolean value) {
            this.assignDocumentation = value;
        }

        public boolean isSetAssignDocumentation() {
            return true;
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
         *         &lt;element name="runTime" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "runTime"
        })
        public static class Change
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean runTime;

            /**
             * Ruft den Wert der runTime-Eigenschaft ab.
             * 
             */
            public boolean isRunTime() {
                return runTime;
            }

            /**
             * Legt den Wert der runTime-Eigenschaft fest.
             * 
             */
            public void setRunTime(boolean value) {
                this.runTime = value;
            }

            public boolean isSetRunTime() {
                return true;
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
         *         &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="stop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="unstop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="kill" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="endAllTasks" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="suspendAllTasks" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="continueAllTasks" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "start",
            "stop",
            "unstop",
            "terminate",
            "kill",
            "endAllTasks",
            "suspendAllTasks",
            "continueAllTasks"
        })
        public static class Execute
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean start;
            protected boolean stop;
            protected boolean unstop;
            protected boolean terminate;
            protected boolean kill;
            protected boolean endAllTasks;
            protected boolean suspendAllTasks;
            protected boolean continueAllTasks;

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

            public boolean isSetStart() {
                return true;
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

            public boolean isSetStop() {
                return true;
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

            public boolean isSetUnstop() {
                return true;
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

            public boolean isSetTerminate() {
                return true;
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

            public boolean isSetKill() {
                return true;
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

            public boolean isSetEndAllTasks() {
                return true;
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

            public boolean isSetSuspendAllTasks() {
                return true;
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

            public boolean isSetContinueAllTasks() {
                return true;
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
         *         &lt;element name="taskLog" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="history" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "taskLog",
            "configuration",
            "history",
            "documentation"
        })
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean status;
            protected boolean taskLog;
            protected boolean configuration;
            protected boolean history;
            protected boolean documentation;

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

            public boolean isSetStatus() {
                return true;
            }

            /**
             * Ruft den Wert der taskLog-Eigenschaft ab.
             * 
             */
            public boolean isTaskLog() {
                return taskLog;
            }

            /**
             * Legt den Wert der taskLog-Eigenschaft fest.
             * 
             */
            public void setTaskLog(boolean value) {
                this.taskLog = value;
            }

            public boolean isSetTaskLog() {
                return true;
            }

            /**
             * Ruft den Wert der configuration-Eigenschaft ab.
             * 
             */
            public boolean isConfiguration() {
                return configuration;
            }

            /**
             * Legt den Wert der configuration-Eigenschaft fest.
             * 
             */
            public void setConfiguration(boolean value) {
                this.configuration = value;
            }

            public boolean isSetConfiguration() {
                return true;
            }

            /**
             * Ruft den Wert der history-Eigenschaft ab.
             * 
             */
            public boolean isHistory() {
                return history;
            }

            /**
             * Legt den Wert der history-Eigenschaft fest.
             * 
             */
            public void setHistory(boolean value) {
                this.history = value;
            }

            public boolean isSetHistory() {
                return true;
            }

            /**
             * Ruft den Wert der documentation-Eigenschaft ab.
             * 
             */
            public boolean isDocumentation() {
                return documentation;
            }

            /**
             * Legt den Wert der documentation-Eigenschaft fest.
             * 
             */
            public void setDocumentation(boolean value) {
                this.documentation = value;
            }

            public boolean isSetDocumentation() {
                return true;
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
     *                   &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="history" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="execute">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="stop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="unstop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="addOrder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="skipJobChainNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="processJobChainNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="stopJobChainNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="assignDocumentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "execute",
        "assignDocumentation"
    })
    public static class JobChain
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.JobChain.View view;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.JobChain.Execute execute;
        protected boolean assignDocumentation;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.JobChain.View }
         *     
         */
        public SOSPermissionJocCockpit.JobChain.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.JobChain.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.JobChain.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
        }

        /**
         * Ruft den Wert der execute-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.JobChain.Execute }
         *     
         */
        public SOSPermissionJocCockpit.JobChain.Execute getExecute() {
            return execute;
        }

        /**
         * Legt den Wert der execute-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.JobChain.Execute }
         *     
         */
        public void setExecute(SOSPermissionJocCockpit.JobChain.Execute value) {
            this.execute = value;
        }

        public boolean isSetExecute() {
            return (this.execute!= null);
        }

        /**
         * Ruft den Wert der assignDocumentation-Eigenschaft ab.
         * 
         */
        public boolean isAssignDocumentation() {
            return assignDocumentation;
        }

        /**
         * Legt den Wert der assignDocumentation-Eigenschaft fest.
         * 
         */
        public void setAssignDocumentation(boolean value) {
            this.assignDocumentation = value;
        }

        public boolean isSetAssignDocumentation() {
            return true;
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
         *         &lt;element name="stop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="unstop" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="addOrder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="skipJobChainNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="processJobChainNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="stopJobChainNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "stop",
            "unstop",
            "addOrder",
            "skipJobChainNode",
            "processJobChainNode",
            "stopJobChainNode"
        })
        public static class Execute
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean stop;
            protected boolean unstop;
            protected boolean addOrder;
            protected boolean skipJobChainNode;
            protected boolean processJobChainNode;
            protected boolean stopJobChainNode;

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

            public boolean isSetStop() {
                return true;
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

            public boolean isSetUnstop() {
                return true;
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

            public boolean isSetAddOrder() {
                return true;
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

            public boolean isSetSkipJobChainNode() {
                return true;
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

            public boolean isSetProcessJobChainNode() {
                return true;
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

            public boolean isSetStopJobChainNode() {
                return true;
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
         *         &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="history" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "configuration",
            "history",
            "status",
            "documentation"
        })
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean configuration;
            protected boolean history;
            protected boolean status;
            protected boolean documentation;

            /**
             * Ruft den Wert der configuration-Eigenschaft ab.
             * 
             */
            public boolean isConfiguration() {
                return configuration;
            }

            /**
             * Legt den Wert der configuration-Eigenschaft fest.
             * 
             */
            public void setConfiguration(boolean value) {
                this.configuration = value;
            }

            public boolean isSetConfiguration() {
                return true;
            }

            /**
             * Ruft den Wert der history-Eigenschaft ab.
             * 
             */
            public boolean isHistory() {
                return history;
            }

            /**
             * Legt den Wert der history-Eigenschaft fest.
             * 
             */
            public void setHistory(boolean value) {
                this.history = value;
            }

            public boolean isSetHistory() {
                return true;
            }

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

            public boolean isSetStatus() {
                return true;
            }

            /**
             * Ruft den Wert der documentation-Eigenschaft ab.
             * 
             */
            public boolean isDocumentation() {
                return documentation;
            }

            /**
             * Legt den Wert der documentation-Eigenschaft fest.
             * 
             */
            public void setDocumentation(boolean value) {
                this.documentation = value;
            }

            public boolean isSetDocumentation() {
                return true;
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
     *                   &lt;element name="graph" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="eventlist" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
     *                   &lt;element name="conditions" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="jobStream" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="events">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="add" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                             &lt;element name="remove" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "change"
    })
    public static class JobStream
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.JobStream.View view;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.JobStream.Change change;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.JobStream.View }
         *     
         */
        public SOSPermissionJocCockpit.JobStream.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.JobStream.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.JobStream.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
        }

        /**
         * Ruft den Wert der change-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.JobStream.Change }
         *     
         */
        public SOSPermissionJocCockpit.JobStream.Change getChange() {
            return change;
        }

        /**
         * Legt den Wert der change-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.JobStream.Change }
         *     
         */
        public void setChange(SOSPermissionJocCockpit.JobStream.Change value) {
            this.change = value;
        }

        public boolean isSetChange() {
            return (this.change!= null);
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
         *         &lt;element name="conditions" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="jobStream" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="events">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="add" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                   &lt;element name="remove" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "conditions",
            "jobStream",
            "events"
        })
        public static class Change
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean conditions;
            protected boolean jobStream;
            @XmlElement(required = true)
            protected SOSPermissionJocCockpit.JobStream.Change.Events events;

            /**
             * Ruft den Wert der conditions-Eigenschaft ab.
             * 
             */
            public boolean isConditions() {
                return conditions;
            }

            /**
             * Legt den Wert der conditions-Eigenschaft fest.
             * 
             */
            public void setConditions(boolean value) {
                this.conditions = value;
            }

            public boolean isSetConditions() {
                return true;
            }

            /**
             * Ruft den Wert der jobStream-Eigenschaft ab.
             * 
             */
            public boolean isJobStream() {
                return jobStream;
            }

            /**
             * Legt den Wert der jobStream-Eigenschaft fest.
             * 
             */
            public void setJobStream(boolean value) {
                this.jobStream = value;
            }

            public boolean isSetJobStream() {
                return true;
            }

            /**
             * Ruft den Wert der events-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link SOSPermissionJocCockpit.JobStream.Change.Events }
             *     
             */
            public SOSPermissionJocCockpit.JobStream.Change.Events getEvents() {
                return events;
            }

            /**
             * Legt den Wert der events-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link SOSPermissionJocCockpit.JobStream.Change.Events }
             *     
             */
            public void setEvents(SOSPermissionJocCockpit.JobStream.Change.Events value) {
                this.events = value;
            }

            public boolean isSetEvents() {
                return (this.events!= null);
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
             *         &lt;element name="add" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *         &lt;element name="remove" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
                "add",
                "remove"
            })
            public static class Events
                implements Serializable
            {

                private final static long serialVersionUID = 12343L;
                protected boolean add;
                protected boolean remove;

                /**
                 * Ruft den Wert der add-Eigenschaft ab.
                 * 
                 */
                public boolean isAdd() {
                    return add;
                }

                /**
                 * Legt den Wert der add-Eigenschaft fest.
                 * 
                 */
                public void setAdd(boolean value) {
                    this.add = value;
                }

                public boolean isSetAdd() {
                    return true;
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

                public boolean isSetRemove() {
                    return true;
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
         *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="graph" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="eventlist" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "graph",
            "eventlist"
        })
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean status;
            protected boolean graph;
            protected boolean eventlist;

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

            public boolean isSetStatus() {
                return true;
            }

            /**
             * Ruft den Wert der graph-Eigenschaft ab.
             * 
             */
            public boolean isGraph() {
                return graph;
            }

            /**
             * Legt den Wert der graph-Eigenschaft fest.
             * 
             */
            public void setGraph(boolean value) {
                this.graph = value;
            }

            public boolean isSetGraph() {
                return true;
            }

            /**
             * Ruft den Wert der eventlist-Eigenschaft ab.
             * 
             */
            public boolean isEventlist() {
                return eventlist;
            }

            /**
             * Legt den Wert der eventlist-Eigenschaft fest.
             * 
             */
            public void setEventlist(boolean value) {
                this.eventlist = value;
            }

            public boolean isSetEventlist() {
                return true;
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
     *                   &lt;element name="mainlog" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="parameter" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="execute">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
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
     *                   &lt;element name="pause" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="continue" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="abort" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="administration">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="removeOldInstances" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="manageCategories" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="editPermissions" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="editMainSection" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
     *                   &lt;element name="configurations">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="view" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                             &lt;element name="edit" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                             &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                             &lt;element name="deploy">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="job" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                                       &lt;element name="jobChain" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                                       &lt;element name="order" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                                       &lt;element name="processClass" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                                       &lt;element name="schedule" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                                       &lt;element name="lock" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                                       &lt;element name="monitor" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                                       &lt;element name="xmlEditor" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
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
        "execute",
        "administration"
    })
    public static class JobschedulerMaster
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.JobschedulerMaster.View view;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.JobschedulerMaster.Execute execute;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.JobschedulerMaster.Administration administration;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.JobschedulerMaster.View }
         *     
         */
        public SOSPermissionJocCockpit.JobschedulerMaster.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.JobschedulerMaster.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.JobschedulerMaster.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
        }

        /**
         * Ruft den Wert der execute-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.JobschedulerMaster.Execute }
         *     
         */
        public SOSPermissionJocCockpit.JobschedulerMaster.Execute getExecute() {
            return execute;
        }

        /**
         * Legt den Wert der execute-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.JobschedulerMaster.Execute }
         *     
         */
        public void setExecute(SOSPermissionJocCockpit.JobschedulerMaster.Execute value) {
            this.execute = value;
        }

        public boolean isSetExecute() {
            return (this.execute!= null);
        }

        /**
         * Ruft den Wert der administration-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.JobschedulerMaster.Administration }
         *     
         */
        public SOSPermissionJocCockpit.JobschedulerMaster.Administration getAdministration() {
            return administration;
        }

        /**
         * Legt den Wert der administration-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.JobschedulerMaster.Administration }
         *     
         */
        public void setAdministration(SOSPermissionJocCockpit.JobschedulerMaster.Administration value) {
            this.administration = value;
        }

        public boolean isSetAdministration() {
            return (this.administration!= null);
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
         *         &lt;element name="removeOldInstances" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="manageCategories" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="editPermissions" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="editMainSection" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
         *         &lt;element name="configurations">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="view" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                   &lt;element name="edit" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                   &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                   &lt;element name="deploy">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="job" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                             &lt;element name="jobChain" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                             &lt;element name="order" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                             &lt;element name="processClass" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                             &lt;element name="schedule" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                             &lt;element name="lock" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                             &lt;element name="monitor" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                             &lt;element name="xmlEditor" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "removeOldInstances",
            "manageCategories",
            "editPermissions",
            "editMainSection",
            "configurations"
        })
        public static class Administration
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean removeOldInstances;
            protected boolean manageCategories;
            protected boolean editPermissions;
            @XmlElement(required = true)
            protected Object editMainSection;
            @XmlElement(required = true)
            protected SOSPermissionJocCockpit.JobschedulerMaster.Administration.Configurations configurations;

            /**
             * Ruft den Wert der removeOldInstances-Eigenschaft ab.
             * 
             */
            public boolean isRemoveOldInstances() {
                return removeOldInstances;
            }

            /**
             * Legt den Wert der removeOldInstances-Eigenschaft fest.
             * 
             */
            public void setRemoveOldInstances(boolean value) {
                this.removeOldInstances = value;
            }

            public boolean isSetRemoveOldInstances() {
                return true;
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

            public boolean isSetManageCategories() {
                return true;
            }

            /**
             * Ruft den Wert der editPermissions-Eigenschaft ab.
             * 
             */
            public boolean isEditPermissions() {
                return editPermissions;
            }

            /**
             * Legt den Wert der editPermissions-Eigenschaft fest.
             * 
             */
            public void setEditPermissions(boolean value) {
                this.editPermissions = value;
            }

            public boolean isSetEditPermissions() {
                return true;
            }

            /**
             * Ruft den Wert der editMainSection-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Object }
             *     
             */
            public Object getEditMainSection() {
                return editMainSection;
            }

            /**
             * Legt den Wert der editMainSection-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Object }
             *     
             */
            public void setEditMainSection(Object value) {
                this.editMainSection = value;
            }

            public boolean isSetEditMainSection() {
                return (this.editMainSection!= null);
            }

            /**
             * Ruft den Wert der configurations-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link SOSPermissionJocCockpit.JobschedulerMaster.Administration.Configurations }
             *     
             */
            public SOSPermissionJocCockpit.JobschedulerMaster.Administration.Configurations getConfigurations() {
                return configurations;
            }

            /**
             * Legt den Wert der configurations-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link SOSPermissionJocCockpit.JobschedulerMaster.Administration.Configurations }
             *     
             */
            public void setConfigurations(SOSPermissionJocCockpit.JobschedulerMaster.Administration.Configurations value) {
                this.configurations = value;
            }

            public boolean isSetConfigurations() {
                return (this.configurations!= null);
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
             *         &lt;element name="edit" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *         &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *         &lt;element name="deploy">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="job" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *                   &lt;element name="jobChain" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *                   &lt;element name="order" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *                   &lt;element name="processClass" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *                   &lt;element name="schedule" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *                   &lt;element name="lock" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *                   &lt;element name="monitor" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *                   &lt;element name="xmlEditor" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
                "view",
                "edit",
                "delete",
                "deploy"
            })
            public static class Configurations
                implements Serializable
            {

                private final static long serialVersionUID = 12343L;
                protected boolean view;
                protected boolean edit;
                protected boolean delete;
                @XmlElement(required = true)
                protected SOSPermissionJocCockpit.JobschedulerMaster.Administration.Configurations.Deploy deploy;

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

                /**
                 * Ruft den Wert der edit-Eigenschaft ab.
                 * 
                 */
                public boolean isEdit() {
                    return edit;
                }

                /**
                 * Legt den Wert der edit-Eigenschaft fest.
                 * 
                 */
                public void setEdit(boolean value) {
                    this.edit = value;
                }

                public boolean isSetEdit() {
                    return true;
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

                public boolean isSetDelete() {
                    return true;
                }

                /**
                 * Ruft den Wert der deploy-Eigenschaft ab.
                 * 
                 * @return
                 *     possible object is
                 *     {@link SOSPermissionJocCockpit.JobschedulerMaster.Administration.Configurations.Deploy }
                 *     
                 */
                public SOSPermissionJocCockpit.JobschedulerMaster.Administration.Configurations.Deploy getDeploy() {
                    return deploy;
                }

                /**
                 * Legt den Wert der deploy-Eigenschaft fest.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link SOSPermissionJocCockpit.JobschedulerMaster.Administration.Configurations.Deploy }
                 *     
                 */
                public void setDeploy(SOSPermissionJocCockpit.JobschedulerMaster.Administration.Configurations.Deploy value) {
                    this.deploy = value;
                }

                public boolean isSetDeploy() {
                    return (this.deploy!= null);
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
                 *         &lt;element name="job" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
                 *         &lt;element name="jobChain" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
                 *         &lt;element name="order" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
                 *         &lt;element name="processClass" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
                 *         &lt;element name="schedule" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
                 *         &lt;element name="lock" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
                 *         &lt;element name="monitor" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
                 *         &lt;element name="xmlEditor" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
                    "job",
                    "jobChain",
                    "order",
                    "processClass",
                    "schedule",
                    "lock",
                    "monitor",
                    "xmlEditor"
                })
                public static class Deploy
                    implements Serializable
                {

                    private final static long serialVersionUID = 12343L;
                    protected boolean job;
                    protected boolean jobChain;
                    protected boolean order;
                    protected boolean processClass;
                    protected boolean schedule;
                    protected boolean lock;
                    protected boolean monitor;
                    protected boolean xmlEditor;

                    /**
                     * Ruft den Wert der job-Eigenschaft ab.
                     * 
                     */
                    public boolean isJob() {
                        return job;
                    }

                    /**
                     * Legt den Wert der job-Eigenschaft fest.
                     * 
                     */
                    public void setJob(boolean value) {
                        this.job = value;
                    }

                    public boolean isSetJob() {
                        return true;
                    }

                    /**
                     * Ruft den Wert der jobChain-Eigenschaft ab.
                     * 
                     */
                    public boolean isJobChain() {
                        return jobChain;
                    }

                    /**
                     * Legt den Wert der jobChain-Eigenschaft fest.
                     * 
                     */
                    public void setJobChain(boolean value) {
                        this.jobChain = value;
                    }

                    public boolean isSetJobChain() {
                        return true;
                    }

                    /**
                     * Ruft den Wert der order-Eigenschaft ab.
                     * 
                     */
                    public boolean isOrder() {
                        return order;
                    }

                    /**
                     * Legt den Wert der order-Eigenschaft fest.
                     * 
                     */
                    public void setOrder(boolean value) {
                        this.order = value;
                    }

                    public boolean isSetOrder() {
                        return true;
                    }

                    /**
                     * Ruft den Wert der processClass-Eigenschaft ab.
                     * 
                     */
                    public boolean isProcessClass() {
                        return processClass;
                    }

                    /**
                     * Legt den Wert der processClass-Eigenschaft fest.
                     * 
                     */
                    public void setProcessClass(boolean value) {
                        this.processClass = value;
                    }

                    public boolean isSetProcessClass() {
                        return true;
                    }

                    /**
                     * Ruft den Wert der schedule-Eigenschaft ab.
                     * 
                     */
                    public boolean isSchedule() {
                        return schedule;
                    }

                    /**
                     * Legt den Wert der schedule-Eigenschaft fest.
                     * 
                     */
                    public void setSchedule(boolean value) {
                        this.schedule = value;
                    }

                    public boolean isSetSchedule() {
                        return true;
                    }

                    /**
                     * Ruft den Wert der lock-Eigenschaft ab.
                     * 
                     */
                    public boolean isLock() {
                        return lock;
                    }

                    /**
                     * Legt den Wert der lock-Eigenschaft fest.
                     * 
                     */
                    public void setLock(boolean value) {
                        this.lock = value;
                    }

                    public boolean isSetLock() {
                        return true;
                    }

                    /**
                     * Ruft den Wert der monitor-Eigenschaft ab.
                     * 
                     */
                    public boolean isMonitor() {
                        return monitor;
                    }

                    /**
                     * Legt den Wert der monitor-Eigenschaft fest.
                     * 
                     */
                    public void setMonitor(boolean value) {
                        this.monitor = value;
                    }

                    public boolean isSetMonitor() {
                        return true;
                    }

                    /**
                     * Ruft den Wert der xmlEditor-Eigenschaft ab.
                     * 
                     */
                    public boolean isXmlEditor() {
                        return xmlEditor;
                    }

                    /**
                     * Legt den Wert der xmlEditor-Eigenschaft fest.
                     * 
                     */
                    public void setXmlEditor(boolean value) {
                        this.xmlEditor = value;
                    }

                    public boolean isSetXmlEditor() {
                        return true;
                    }

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
         *         &lt;element name="pause" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="continue" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "restart",
            "pause",
            "_continue",
            "terminate",
            "abort"
        })
        public static class Execute
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            @XmlElement(required = true)
            protected SOSPermissionJocCockpit.JobschedulerMaster.Execute.Restart restart;
            protected boolean pause;
            @XmlElement(name = "continue")
            protected boolean _continue;
            protected boolean terminate;
            protected boolean abort;

            /**
             * Ruft den Wert der restart-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link SOSPermissionJocCockpit.JobschedulerMaster.Execute.Restart }
             *     
             */
            public SOSPermissionJocCockpit.JobschedulerMaster.Execute.Restart getRestart() {
                return restart;
            }

            /**
             * Legt den Wert der restart-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link SOSPermissionJocCockpit.JobschedulerMaster.Execute.Restart }
             *     
             */
            public void setRestart(SOSPermissionJocCockpit.JobschedulerMaster.Execute.Restart value) {
                this.restart = value;
            }

            public boolean isSetRestart() {
                return (this.restart!= null);
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

            public boolean isSetPause() {
                return true;
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

            public boolean isSetContinue() {
                return true;
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

            public boolean isSetTerminate() {
                return true;
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

            public boolean isSetAbort() {
                return true;
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
            public static class Restart
                implements Serializable
            {

                private final static long serialVersionUID = 12343L;
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

                public boolean isSetTerminate() {
                    return true;
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

                public boolean isSetAbort() {
                    return true;
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
         *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="mainlog" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "mainlog",
            "parameter"
        })
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean status;
            protected boolean mainlog;
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

            public boolean isSetStatus() {
                return true;
            }

            /**
             * Ruft den Wert der mainlog-Eigenschaft ab.
             * 
             */
            public boolean isMainlog() {
                return mainlog;
            }

            /**
             * Legt den Wert der mainlog-Eigenschaft fest.
             * 
             */
            public void setMainlog(boolean value) {
                this.mainlog = value;
            }

            public boolean isSetMainlog() {
                return true;
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

            public boolean isSetParameter() {
                return true;
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
     *         &lt;element name="execute">
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
        "execute"
    })
    public static class JobschedulerMasterCluster
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.JobschedulerMasterCluster.View view;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.JobschedulerMasterCluster.Execute execute;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.JobschedulerMasterCluster.View }
         *     
         */
        public SOSPermissionJocCockpit.JobschedulerMasterCluster.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.JobschedulerMasterCluster.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.JobschedulerMasterCluster.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
        }

        /**
         * Ruft den Wert der execute-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.JobschedulerMasterCluster.Execute }
         *     
         */
        public SOSPermissionJocCockpit.JobschedulerMasterCluster.Execute getExecute() {
            return execute;
        }

        /**
         * Legt den Wert der execute-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.JobschedulerMasterCluster.Execute }
         *     
         */
        public void setExecute(SOSPermissionJocCockpit.JobschedulerMasterCluster.Execute value) {
            this.execute = value;
        }

        public boolean isSetExecute() {
            return (this.execute!= null);
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
        public static class Execute
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
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

            public boolean isSetRestart() {
                return true;
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

            public boolean isSetTerminate() {
                return true;
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

            public boolean isSetTerminateFailSafe() {
                return true;
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
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
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

            public boolean isSetStatus() {
                return true;
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
     *         &lt;element name="execute">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
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
     *                   &lt;element name="terminate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="abort" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "view",
        "execute"
    })
    public static class JobschedulerUniversalAgent
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.JobschedulerUniversalAgent.View view;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.JobschedulerUniversalAgent.Execute execute;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.JobschedulerUniversalAgent.View }
         *     
         */
        public SOSPermissionJocCockpit.JobschedulerUniversalAgent.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.JobschedulerUniversalAgent.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.JobschedulerUniversalAgent.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
        }

        /**
         * Ruft den Wert der execute-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.JobschedulerUniversalAgent.Execute }
         *     
         */
        public SOSPermissionJocCockpit.JobschedulerUniversalAgent.Execute getExecute() {
            return execute;
        }

        /**
         * Legt den Wert der execute-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.JobschedulerUniversalAgent.Execute }
         *     
         */
        public void setExecute(SOSPermissionJocCockpit.JobschedulerUniversalAgent.Execute value) {
            this.execute = value;
        }

        public boolean isSetExecute() {
            return (this.execute!= null);
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
            "restart",
            "terminate",
            "abort"
        })
        public static class Execute
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            @XmlElement(required = true)
            protected SOSPermissionJocCockpit.JobschedulerUniversalAgent.Execute.Restart restart;
            protected boolean terminate;
            protected boolean abort;

            /**
             * Ruft den Wert der restart-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link SOSPermissionJocCockpit.JobschedulerUniversalAgent.Execute.Restart }
             *     
             */
            public SOSPermissionJocCockpit.JobschedulerUniversalAgent.Execute.Restart getRestart() {
                return restart;
            }

            /**
             * Legt den Wert der restart-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link SOSPermissionJocCockpit.JobschedulerUniversalAgent.Execute.Restart }
             *     
             */
            public void setRestart(SOSPermissionJocCockpit.JobschedulerUniversalAgent.Execute.Restart value) {
                this.restart = value;
            }

            public boolean isSetRestart() {
                return (this.restart!= null);
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

            public boolean isSetTerminate() {
                return true;
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

            public boolean isSetAbort() {
                return true;
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
            public static class Restart
                implements Serializable
            {

                private final static long serialVersionUID = 12343L;
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

                public boolean isSetTerminate() {
                    return true;
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

                public boolean isSetAbort() {
                    return true;
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
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
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

            public boolean isSetStatus() {
                return true;
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
     *                   &lt;element name="log" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    public static class Joc
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Joc.View view;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.Joc.View }
         *     
         */
        public SOSPermissionJocCockpit.Joc.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.Joc.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.Joc.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
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
         *         &lt;element name="log" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "log"
        })
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean log;

            /**
             * Ruft den Wert der log-Eigenschaft ab.
             * 
             */
            public boolean isLog() {
                return log;
            }

            /**
             * Legt den Wert der log-Eigenschaft fest.
             * 
             */
            public void setLog(boolean value) {
                this.log = value;
            }

            public boolean isSetLog() {
                return true;
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
     *                   &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="assignDocumentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "assignDocumentation"
    })
    public static class Lock
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Lock.View view;
        protected boolean assignDocumentation;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.Lock.View }
         *     
         */
        public SOSPermissionJocCockpit.Lock.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.Lock.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.Lock.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
        }

        /**
         * Ruft den Wert der assignDocumentation-Eigenschaft ab.
         * 
         */
        public boolean isAssignDocumentation() {
            return assignDocumentation;
        }

        /**
         * Legt den Wert der assignDocumentation-Eigenschaft fest.
         * 
         */
        public void setAssignDocumentation(boolean value) {
            this.assignDocumentation = value;
        }

        public boolean isSetAssignDocumentation() {
            return true;
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
         *         &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "configuration",
            "documentation"
        })
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean status;
            protected boolean configuration;
            protected boolean documentation;

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

            public boolean isSetStatus() {
                return true;
            }

            /**
             * Ruft den Wert der configuration-Eigenschaft ab.
             * 
             */
            public boolean isConfiguration() {
                return configuration;
            }

            /**
             * Legt den Wert der configuration-Eigenschaft fest.
             * 
             */
            public void setConfiguration(boolean value) {
                this.configuration = value;
            }

            public boolean isSetConfiguration() {
                return true;
            }

            /**
             * Ruft den Wert der documentation-Eigenschaft ab.
             * 
             */
            public boolean isDocumentation() {
                return documentation;
            }

            /**
             * Legt den Wert der documentation-Eigenschaft fest.
             * 
             */
            public void setDocumentation(boolean value) {
                this.documentation = value;
            }

            public boolean isSetDocumentation() {
                return true;
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
     *         &lt;element name="enableDisableMaintenanceWindow" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "enableDisableMaintenanceWindow"
    })
    public static class MaintenanceWindow
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.MaintenanceWindow.View view;
        protected boolean enableDisableMaintenanceWindow;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.MaintenanceWindow.View }
         *     
         */
        public SOSPermissionJocCockpit.MaintenanceWindow.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.MaintenanceWindow.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.MaintenanceWindow.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
        }

        /**
         * Ruft den Wert der enableDisableMaintenanceWindow-Eigenschaft ab.
         * 
         */
        public boolean isEnableDisableMaintenanceWindow() {
            return enableDisableMaintenanceWindow;
        }

        /**
         * Legt den Wert der enableDisableMaintenanceWindow-Eigenschaft fest.
         * 
         */
        public void setEnableDisableMaintenanceWindow(boolean value) {
            this.enableDisableMaintenanceWindow = value;
        }

        public boolean isSetEnableDisableMaintenanceWindow() {
            return true;
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
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
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

            public boolean isSetStatus() {
                return true;
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
     *                   &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="orderLog" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
     *                   &lt;element name="startAndEndNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="timeForAdhocOrder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="runTime" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="parameter" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="delete">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="temporary" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="permanent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="execute">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="update" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="suspend" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="resume" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="reset" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="removeSetback" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="assignDocumentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "delete",
        "execute",
        "assignDocumentation"
    })
    public static class Order
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Order.View view;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Order.Change change;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Order.Delete delete;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Order.Execute execute;
        protected boolean assignDocumentation;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.Order.View }
         *     
         */
        public SOSPermissionJocCockpit.Order.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.Order.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.Order.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
        }

        /**
         * Ruft den Wert der change-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.Order.Change }
         *     
         */
        public SOSPermissionJocCockpit.Order.Change getChange() {
            return change;
        }

        /**
         * Legt den Wert der change-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.Order.Change }
         *     
         */
        public void setChange(SOSPermissionJocCockpit.Order.Change value) {
            this.change = value;
        }

        public boolean isSetChange() {
            return (this.change!= null);
        }

        /**
         * Ruft den Wert der delete-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.Order.Delete }
         *     
         */
        public SOSPermissionJocCockpit.Order.Delete getDelete() {
            return delete;
        }

        /**
         * Legt den Wert der delete-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.Order.Delete }
         *     
         */
        public void setDelete(SOSPermissionJocCockpit.Order.Delete value) {
            this.delete = value;
        }

        public boolean isSetDelete() {
            return (this.delete!= null);
        }

        /**
         * Ruft den Wert der execute-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.Order.Execute }
         *     
         */
        public SOSPermissionJocCockpit.Order.Execute getExecute() {
            return execute;
        }

        /**
         * Legt den Wert der execute-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.Order.Execute }
         *     
         */
        public void setExecute(SOSPermissionJocCockpit.Order.Execute value) {
            this.execute = value;
        }

        public boolean isSetExecute() {
            return (this.execute!= null);
        }

        /**
         * Ruft den Wert der assignDocumentation-Eigenschaft ab.
         * 
         */
        public boolean isAssignDocumentation() {
            return assignDocumentation;
        }

        /**
         * Legt den Wert der assignDocumentation-Eigenschaft fest.
         * 
         */
        public void setAssignDocumentation(boolean value) {
            this.assignDocumentation = value;
        }

        public boolean isSetAssignDocumentation() {
            return true;
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
         *         &lt;element name="startAndEndNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="timeForAdhocOrder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="runTime" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "startAndEndNode",
            "timeForAdhocOrder",
            "runTime",
            "state",
            "parameter"
        })
        public static class Change
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean startAndEndNode;
            protected boolean timeForAdhocOrder;
            protected boolean runTime;
            protected boolean state;
            protected boolean parameter;

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

            public boolean isSetStartAndEndNode() {
                return true;
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

            public boolean isSetTimeForAdhocOrder() {
                return true;
            }

            /**
             * Ruft den Wert der runTime-Eigenschaft ab.
             * 
             */
            public boolean isRunTime() {
                return runTime;
            }

            /**
             * Legt den Wert der runTime-Eigenschaft fest.
             * 
             */
            public void setRunTime(boolean value) {
                this.runTime = value;
            }

            public boolean isSetRunTime() {
                return true;
            }

            /**
             * Ruft den Wert der state-Eigenschaft ab.
             * 
             */
            public boolean isState() {
                return state;
            }

            /**
             * Legt den Wert der state-Eigenschaft fest.
             * 
             */
            public void setState(boolean value) {
                this.state = value;
            }

            public boolean isSetState() {
                return true;
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

            public boolean isSetParameter() {
                return true;
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
         *         &lt;element name="temporary" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="permanent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "temporary",
            "permanent"
        })
        public static class Delete
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean temporary;
            protected boolean permanent;

            /**
             * Ruft den Wert der temporary-Eigenschaft ab.
             * 
             */
            public boolean isTemporary() {
                return temporary;
            }

            /**
             * Legt den Wert der temporary-Eigenschaft fest.
             * 
             */
            public void setTemporary(boolean value) {
                this.temporary = value;
            }

            public boolean isSetTemporary() {
                return true;
            }

            /**
             * Ruft den Wert der permanent-Eigenschaft ab.
             * 
             */
            public boolean isPermanent() {
                return permanent;
            }

            /**
             * Legt den Wert der permanent-Eigenschaft fest.
             * 
             */
            public void setPermanent(boolean value) {
                this.permanent = value;
            }

            public boolean isSetPermanent() {
                return true;
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
         *         &lt;element name="start" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="update" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="suspend" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="resume" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="reset" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="removeSetback" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "start",
            "update",
            "suspend",
            "resume",
            "reset",
            "removeSetback"
        })
        public static class Execute
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean start;
            protected boolean update;
            protected boolean suspend;
            protected boolean resume;
            protected boolean reset;
            protected boolean removeSetback;

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

            public boolean isSetStart() {
                return true;
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

            public boolean isSetUpdate() {
                return true;
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

            public boolean isSetSuspend() {
                return true;
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

            public boolean isSetResume() {
                return true;
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

            public boolean isSetReset() {
                return true;
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

            public boolean isSetRemoveSetback() {
                return true;
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
         *         &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="orderLog" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "configuration",
            "orderLog",
            "status",
            "documentation"
        })
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean configuration;
            protected boolean orderLog;
            protected boolean status;
            protected boolean documentation;

            /**
             * Ruft den Wert der configuration-Eigenschaft ab.
             * 
             */
            public boolean isConfiguration() {
                return configuration;
            }

            /**
             * Legt den Wert der configuration-Eigenschaft fest.
             * 
             */
            public void setConfiguration(boolean value) {
                this.configuration = value;
            }

            public boolean isSetConfiguration() {
                return true;
            }

            /**
             * Ruft den Wert der orderLog-Eigenschaft ab.
             * 
             */
            public boolean isOrderLog() {
                return orderLog;
            }

            /**
             * Legt den Wert der orderLog-Eigenschaft fest.
             * 
             */
            public void setOrderLog(boolean value) {
                this.orderLog = value;
            }

            public boolean isSetOrderLog() {
                return true;
            }

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

            public boolean isSetStatus() {
                return true;
            }

            /**
             * Ruft den Wert der documentation-Eigenschaft ab.
             * 
             */
            public boolean isDocumentation() {
                return documentation;
            }

            /**
             * Legt den Wert der documentation-Eigenschaft fest.
             * 
             */
            public void setDocumentation(boolean value) {
                this.documentation = value;
            }

            public boolean isSetDocumentation() {
                return true;
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
     *                   &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="assignDocumentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "assignDocumentation"
    })
    public static class ProcessClass
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.ProcessClass.View view;
        protected boolean assignDocumentation;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.ProcessClass.View }
         *     
         */
        public SOSPermissionJocCockpit.ProcessClass.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.ProcessClass.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.ProcessClass.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
        }

        /**
         * Ruft den Wert der assignDocumentation-Eigenschaft ab.
         * 
         */
        public boolean isAssignDocumentation() {
            return assignDocumentation;
        }

        /**
         * Legt den Wert der assignDocumentation-Eigenschaft fest.
         * 
         */
        public void setAssignDocumentation(boolean value) {
            this.assignDocumentation = value;
        }

        public boolean isSetAssignDocumentation() {
            return true;
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
         *         &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "configuration",
            "documentation"
        })
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean status;
            protected boolean configuration;
            protected boolean documentation;

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

            public boolean isSetStatus() {
                return true;
            }

            /**
             * Ruft den Wert der configuration-Eigenschaft ab.
             * 
             */
            public boolean isConfiguration() {
                return configuration;
            }

            /**
             * Legt den Wert der configuration-Eigenschaft fest.
             * 
             */
            public void setConfiguration(boolean value) {
                this.configuration = value;
            }

            public boolean isSetConfiguration() {
                return true;
            }

            /**
             * Ruft den Wert der documentation-Eigenschaft ab.
             * 
             */
            public boolean isDocumentation() {
                return documentation;
            }

            /**
             * Legt den Wert der documentation-Eigenschaft fest.
             * 
             */
            public void setDocumentation(boolean value) {
                this.documentation = value;
            }

            public boolean isSetDocumentation() {
                return true;
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
     *         &lt;element name="execute">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="editXml" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "execute"
    })
    public static class Runtime
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Runtime.Execute execute;

        /**
         * Ruft den Wert der execute-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.Runtime.Execute }
         *     
         */
        public SOSPermissionJocCockpit.Runtime.Execute getExecute() {
            return execute;
        }

        /**
         * Legt den Wert der execute-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.Runtime.Execute }
         *     
         */
        public void setExecute(SOSPermissionJocCockpit.Runtime.Execute value) {
            this.execute = value;
        }

        public boolean isSetExecute() {
            return (this.execute!= null);
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
         *         &lt;element name="editXml" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "editXml"
        })
        public static class Execute
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean editXml;

            /**
             * Ruft den Wert der editXml-Eigenschaft ab.
             * 
             */
            public boolean isEditXml() {
                return editXml;
            }

            /**
             * Legt den Wert der editXml-Eigenschaft fest.
             * 
             */
            public void setEditXml(boolean value) {
                this.editXml = value;
            }

            public boolean isSetEditXml() {
                return true;
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
     *                   &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
     *                   &lt;element name="addSubstitute" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="editContent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="assignDocumentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "assignDocumentation"
    })
    public static class Schedule
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Schedule.View view;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Schedule.Change change;
        protected boolean assignDocumentation;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.Schedule.View }
         *     
         */
        public SOSPermissionJocCockpit.Schedule.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.Schedule.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.Schedule.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
        }

        /**
         * Ruft den Wert der change-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.Schedule.Change }
         *     
         */
        public SOSPermissionJocCockpit.Schedule.Change getChange() {
            return change;
        }

        /**
         * Legt den Wert der change-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.Schedule.Change }
         *     
         */
        public void setChange(SOSPermissionJocCockpit.Schedule.Change value) {
            this.change = value;
        }

        public boolean isSetChange() {
            return (this.change!= null);
        }

        /**
         * Ruft den Wert der assignDocumentation-Eigenschaft ab.
         * 
         */
        public boolean isAssignDocumentation() {
            return assignDocumentation;
        }

        /**
         * Legt den Wert der assignDocumentation-Eigenschaft fest.
         * 
         */
        public void setAssignDocumentation(boolean value) {
            this.assignDocumentation = value;
        }

        public boolean isSetAssignDocumentation() {
            return true;
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
         *         &lt;element name="addSubstitute" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="editContent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "addSubstitute",
            "editContent"
        })
        public static class Change
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean addSubstitute;
            protected boolean editContent;

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

            public boolean isSetAddSubstitute() {
                return true;
            }

            /**
             * Ruft den Wert der editContent-Eigenschaft ab.
             * 
             */
            public boolean isEditContent() {
                return editContent;
            }

            /**
             * Legt den Wert der editContent-Eigenschaft fest.
             * 
             */
            public void setEditContent(boolean value) {
                this.editContent = value;
            }

            public boolean isSetEditContent() {
                return true;
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
         *         &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *         &lt;element name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "configuration",
            "documentation"
        })
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean status;
            protected boolean configuration;
            protected boolean documentation;

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

            public boolean isSetStatus() {
                return true;
            }

            /**
             * Ruft den Wert der configuration-Eigenschaft ab.
             * 
             */
            public boolean isConfiguration() {
                return configuration;
            }

            /**
             * Legt den Wert der configuration-Eigenschaft fest.
             * 
             */
            public void setConfiguration(boolean value) {
                this.configuration = value;
            }

            public boolean isSetConfiguration() {
                return true;
            }

            /**
             * Ruft den Wert der documentation-Eigenschaft ab.
             * 
             */
            public boolean isDocumentation() {
                return documentation;
            }

            /**
             * Legt den Wert der documentation-Eigenschaft fest.
             * 
             */
            public void setDocumentation(boolean value) {
                this.documentation = value;
            }

            public boolean isSetDocumentation() {
                return true;
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
     *                   &lt;element name="files" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="execute">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="transferStart" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "view",
        "execute"
    })
    public static class YADE
        implements Serializable
    {

        private final static long serialVersionUID = 12343L;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.YADE.View view;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.YADE.Execute execute;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.YADE.View }
         *     
         */
        public SOSPermissionJocCockpit.YADE.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.YADE.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.YADE.View value) {
            this.view = value;
        }

        public boolean isSetView() {
            return (this.view!= null);
        }

        /**
         * Ruft den Wert der execute-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.YADE.Execute }
         *     
         */
        public SOSPermissionJocCockpit.YADE.Execute getExecute() {
            return execute;
        }

        /**
         * Legt den Wert der execute-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.YADE.Execute }
         *     
         */
        public void setExecute(SOSPermissionJocCockpit.YADE.Execute value) {
            this.execute = value;
        }

        public boolean isSetExecute() {
            return (this.execute!= null);
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
         *         &lt;element name="transferStart" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "transferStart"
        })
        public static class Execute
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean transferStart;

            /**
             * Ruft den Wert der transferStart-Eigenschaft ab.
             * 
             */
            public boolean isTransferStart() {
                return transferStart;
            }

            /**
             * Legt den Wert der transferStart-Eigenschaft fest.
             * 
             */
            public void setTransferStart(boolean value) {
                this.transferStart = value;
            }

            public boolean isSetTransferStart() {
                return true;
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
         *         &lt;element name="files" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "files"
        })
        public static class View
            implements Serializable
        {

            private final static long serialVersionUID = 12343L;
            protected boolean status;
            protected boolean files;

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

            public boolean isSetStatus() {
                return true;
            }

            /**
             * Ruft den Wert der files-Eigenschaft ab.
             * 
             */
            public boolean isFiles() {
                return files;
            }

            /**
             * Legt den Wert der files-Eigenschaft fest.
             * 
             */
            public void setFiles(boolean value) {
                this.files = value;
            }

            public boolean isSetFiles() {
                return true;
            }

        }

    }

}
