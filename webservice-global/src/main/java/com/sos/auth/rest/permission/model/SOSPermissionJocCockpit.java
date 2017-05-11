//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.05.11 um 02:07:24 PM CEST 
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
 *                             &lt;element name="mainlog" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="parameter" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Execute">
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
 *                             &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="orderLog" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                             &lt;element name="startAndEndNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="timeForAdhocOrder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="runTime" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="parameter" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="hotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                   &lt;element name="change">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="hotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                             &lt;element name="hotfolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                             &lt;element name="hotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                             &lt;element name="hotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                             &lt;element name="editContent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                             &lt;element name="hotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                   &lt;element name="delete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="EventAction">
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
 *                   &lt;element name="createEventsManually" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
 *                             &lt;element name="view" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "eventAction",
    "holidayCalendar",
    "maintenanceWindow",
    "auditLog",
    "jocConfigurations",
    "sosPermissionRoles"
})
@XmlRootElement(name = "SOSPermissionJocCockpit")
public class SOSPermissionJocCockpit {

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
    @XmlElement(name = "EventAction", required = true)
    protected SOSPermissionJocCockpit.EventAction eventAction;
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

    /**
     * Ruft den Wert der eventAction-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SOSPermissionJocCockpit.EventAction }
     *     
     */
    public SOSPermissionJocCockpit.EventAction getEventAction() {
        return eventAction;
    }

    /**
     * Legt den Wert der eventAction-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SOSPermissionJocCockpit.EventAction }
     *     
     */
    public void setEventAction(SOSPermissionJocCockpit.EventAction value) {
        this.eventAction = value;
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
    public static class AuditLog {

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
        "delete"
    })
    public static class Event {

        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Event.View view;
        protected boolean delete;

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
     *         &lt;element name="createEventsManually" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "createEventsManually"
    })
    public static class EventAction {

        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.EventAction.View view;
        protected boolean createEventsManually;

        /**
         * Ruft den Wert der view-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.EventAction.View }
         *     
         */
        public SOSPermissionJocCockpit.EventAction.View getView() {
            return view;
        }

        /**
         * Legt den Wert der view-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.EventAction.View }
         *     
         */
        public void setView(SOSPermissionJocCockpit.EventAction.View value) {
            this.view = value;
        }

        /**
         * Ruft den Wert der createEventsManually-Eigenschaft ab.
         * 
         */
        public boolean isCreateEventsManually() {
            return createEventsManually;
        }

        /**
         * Legt den Wert der createEventsManually-Eigenschaft fest.
         * 
         */
        public void setCreateEventsManually(boolean value) {
            this.createEventsManually = value;
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
    public static class HolidayCalendar {

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
     *                   &lt;element name="view" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    public static class JOCConfigurations {

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
            "change",
            "view"
        })
        public static class Share {

            @XmlElement(required = true)
            protected SOSPermissionJocCockpit.JOCConfigurations.Share.Change change;
            protected boolean view;

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
            public static class Change {

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
                public static class SharedStatus {

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
     *                   &lt;element name="hotfolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "execute"
    })
    public static class Job {

        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Job.View view;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Job.Change change;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Job.Execute execute;

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
         *         &lt;element name="hotfolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "runTime",
            "hotfolder"
        })
        public static class Change {

            protected boolean runTime;
            protected boolean hotfolder;

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

            /**
             * Ruft den Wert der hotfolder-Eigenschaft ab.
             * 
             */
            public boolean isHotfolder() {
                return hotfolder;
            }

            /**
             * Legt den Wert der hotfolder-Eigenschaft fest.
             * 
             */
            public void setHotfolder(boolean value) {
                this.hotfolder = value;
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
        public static class Execute {

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
            "history"
        })
        public static class View {

            protected boolean status;
            protected boolean taskLog;
            protected boolean configuration;
            protected boolean history;

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
     *         &lt;element name="change">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="hotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "change"
    })
    public static class JobChain {

        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.JobChain.View view;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.JobChain.Execute execute;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.JobChain.Change change;

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

        /**
         * Ruft den Wert der change-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.JobChain.Change }
         *     
         */
        public SOSPermissionJocCockpit.JobChain.Change getChange() {
            return change;
        }

        /**
         * Legt den Wert der change-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.JobChain.Change }
         *     
         */
        public void setChange(SOSPermissionJocCockpit.JobChain.Change value) {
            this.change = value;
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
         *         &lt;element name="hotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "hotFolder"
        })
        public static class Change {

            protected boolean hotFolder;

            /**
             * Ruft den Wert der hotFolder-Eigenschaft ab.
             * 
             */
            public boolean isHotFolder() {
                return hotFolder;
            }

            /**
             * Legt den Wert der hotFolder-Eigenschaft fest.
             * 
             */
            public void setHotFolder(boolean value) {
                this.hotFolder = value;
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
        public static class Execute {

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
            "status"
        })
        public static class View {

            protected boolean configuration;
            protected boolean history;
            protected boolean status;

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
     *                   &lt;element name="mainlog" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="parameter" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Execute">
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
    public static class JobschedulerMaster {

        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.JobschedulerMaster.View view;
        @XmlElement(name = "Execute", required = true)
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
            "editPermissions"
        })
        public static class Administration {

            protected boolean removeOldInstances;
            protected boolean manageCategories;
            protected boolean editPermissions;

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
        public static class Execute {

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
        public static class View {

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
    public static class JobschedulerMasterCluster {

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
        public static class Execute {

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
    public static class JobschedulerUniversalAgent {

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
        public static class Execute {

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
     *                   &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
     *                   &lt;element name="hotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    public static class Lock {

        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Lock.View view;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Lock.Change change;

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

        /**
         * Ruft den Wert der change-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.Lock.Change }
         *     
         */
        public SOSPermissionJocCockpit.Lock.Change getChange() {
            return change;
        }

        /**
         * Legt den Wert der change-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.Lock.Change }
         *     
         */
        public void setChange(SOSPermissionJocCockpit.Lock.Change value) {
            this.change = value;
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
         *         &lt;element name="hotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "hotFolder"
        })
        public static class Change {

            protected boolean hotFolder;

            /**
             * Ruft den Wert der hotFolder-Eigenschaft ab.
             * 
             */
            public boolean isHotFolder() {
                return hotFolder;
            }

            /**
             * Legt den Wert der hotFolder-Eigenschaft fest.
             * 
             */
            public void setHotFolder(boolean value) {
                this.hotFolder = value;
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
            "configuration"
        })
        public static class View {

            protected boolean status;
            protected boolean configuration;

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
    public static class MaintenanceWindow {

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
     *                   &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="orderLog" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
     *                   &lt;element name="startAndEndNode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="timeForAdhocOrder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="runTime" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="parameter" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="hotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "execute"
    })
    public static class Order {

        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Order.View view;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Order.Change change;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Order.Delete delete;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Order.Execute execute;

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
         *         &lt;element name="hotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "parameter",
            "hotFolder"
        })
        public static class Change {

            protected boolean startAndEndNode;
            protected boolean timeForAdhocOrder;
            protected boolean runTime;
            protected boolean state;
            protected boolean parameter;
            protected boolean hotFolder;

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
             * Ruft den Wert der hotFolder-Eigenschaft ab.
             * 
             */
            public boolean isHotFolder() {
                return hotFolder;
            }

            /**
             * Legt den Wert der hotFolder-Eigenschaft fest.
             * 
             */
            public void setHotFolder(boolean value) {
                this.hotFolder = value;
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
        public static class Delete {

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
        public static class Execute {

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
            "status"
        })
        public static class View {

            protected boolean configuration;
            protected boolean orderLog;
            protected boolean status;

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
     *                   &lt;element name="configuration" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
     *                   &lt;element name="hotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    public static class ProcessClass {

        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.ProcessClass.View view;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.ProcessClass.Change change;

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

        /**
         * Ruft den Wert der change-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link SOSPermissionJocCockpit.ProcessClass.Change }
         *     
         */
        public SOSPermissionJocCockpit.ProcessClass.Change getChange() {
            return change;
        }

        /**
         * Legt den Wert der change-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link SOSPermissionJocCockpit.ProcessClass.Change }
         *     
         */
        public void setChange(SOSPermissionJocCockpit.ProcessClass.Change value) {
            this.change = value;
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
         *         &lt;element name="hotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "hotFolder"
        })
        public static class Change {

            protected boolean hotFolder;

            /**
             * Ruft den Wert der hotFolder-Eigenschaft ab.
             * 
             */
            public boolean isHotFolder() {
                return hotFolder;
            }

            /**
             * Legt den Wert der hotFolder-Eigenschaft fest.
             * 
             */
            public void setHotFolder(boolean value) {
                this.hotFolder = value;
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
            "configuration"
        })
        public static class View {

            protected boolean status;
            protected boolean configuration;

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
     *                   &lt;element name="hotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                   &lt;element name="editContent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    public static class Schedule {

        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Schedule.View view;
        @XmlElement(required = true)
        protected SOSPermissionJocCockpit.Schedule.Change change;

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
         *         &lt;element name="hotFolder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
            "hotFolder",
            "editContent"
        })
        public static class Change {

            protected boolean addSubstitute;
            protected boolean hotFolder;
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

            /**
             * Ruft den Wert der hotFolder-Eigenschaft ab.
             * 
             */
            public boolean isHotFolder() {
                return hotFolder;
            }

            /**
             * Legt den Wert der hotFolder-Eigenschaft fest.
             * 
             */
            public void setHotFolder(boolean value) {
                this.hotFolder = value;
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
            "configuration"
        })
        public static class View {

            protected boolean status;
            protected boolean configuration;

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

        }

    }

}
