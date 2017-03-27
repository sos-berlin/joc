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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *       &lt;choice>
 *         &lt;element name="config" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{}extensions" minOccurs="0"/>
 *                   &lt;element name="base" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="file" use="required" type="{}File" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element ref="{}params" minOccurs="0"/>
 *                   &lt;element ref="{}security" minOccurs="0"/>
 *                   &lt;element name="plugins" type="{}Plugins" minOccurs="0"/>
 *                   &lt;element ref="{}cluster" minOccurs="0"/>
 *                   &lt;element ref="{}process_classes" minOccurs="0"/>
 *                   &lt;element name="schedules" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element ref="{}extensions" minOccurs="0"/>
 *                             &lt;element name="schedule" type="{}run_time" maxOccurs="unbounded" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="locks" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element ref="{}extensions" minOccurs="0"/>
 *                             &lt;element name="lock" type="{}lock" maxOccurs="unbounded" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;choice>
 *                     &lt;element ref="{}script" minOccurs="0"/>
 *                     &lt;element name="scheduler_script" type="{}scheduler_script" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;/choice>
 *                   &lt;choice minOccurs="0">
 *                     &lt;element ref="{}http_server"/>
 *                     &lt;element ref="{}web_services"/>
 *                   &lt;/choice>
 *                   &lt;choice>
 *                     &lt;element ref="{}holidays"/>
 *                     &lt;element ref="{}holiday" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;/choice>
 *                   &lt;element ref="{}jobs" minOccurs="0"/>
 *                   &lt;element ref="{}job_chains" minOccurs="0"/>
 *                   &lt;element ref="{}commands" minOccurs="0"/>
 *                   &lt;element ref="{}monitors" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="spooler_id" type="{}Name" />
 *                 &lt;attribute name="port" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *                 &lt;attribute name="http_port" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *                 &lt;attribute name="tcp_port" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *                 &lt;attribute name="udp_port" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *                 &lt;attribute name="ip_address" type="{}String" />
 *                 &lt;attribute name="param" type="{}String" />
 *                 &lt;attribute name="log_dir" type="{}File" />
 *                 &lt;attribute name="priority_max" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *                 &lt;attribute name="include_path" type="{}File" />
 *                 &lt;attribute name="main_scheduler" type="{}String" />
 *                 &lt;attribute name="mail_xslt_stylesheet" type="{}File" />
 *                 &lt;attribute name="supervisor" type="{}String" />
 *                 &lt;attribute name="configuration_directory" type="{}File" />
 *                 &lt;attribute name="central_configuration_directory" type="{}File" />
 *                 &lt;attribute name="configuration_add_event" type="{}Path" />
 *                 &lt;attribute name="configuration_modify_event" type="{}Path" />
 *                 &lt;attribute name="configuration_delete_event" type="{}Path" />
 *                 &lt;attribute name="time_zone" type="{}String" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{}commands"/>
 *         &lt;element name="command" type="{}Commands"/>
 *         &lt;element name="answer">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{}add_jobs"/>
 *         &lt;element ref="{}add_order"/>
 *         &lt;element name="check_folders" type="{}check_folders"/>
 *         &lt;element ref="{}job.why"/>
 *         &lt;element ref="{}kill_task"/>
 *         &lt;element ref="{}modify_job"/>
 *         &lt;element ref="{}modify_order"/>
 *         &lt;element ref="{}modify_spooler"/>
 *         &lt;element ref="{}job_chain.check_distributed"/>
 *         &lt;element ref="{}register_remote_scheduler"/>
 *         &lt;element ref="{}remove_job_chain"/>
 *         &lt;element ref="{}remove_order"/>
 *         &lt;element name="run_time" type="{}run_time"/>
 *         &lt;element ref="{}show_calendar"/>
 *         &lt;element ref="{}show_history"/>
 *         &lt;element ref="{}show_job"/>
 *         &lt;element ref="{}show_jobs"/>
 *         &lt;element ref="{}show_job_chains"/>
 *         &lt;element ref="{}show_job_chain"/>
 *         &lt;element ref="{}show_order"/>
 *         &lt;element ref="{}show_process_classes"/>
 *         &lt;element ref="{}s"/>
 *         &lt;element ref="{}show_schedulers"/>
 *         &lt;element ref="{}show_state"/>
 *         &lt;element ref="{}show_task"/>
 *         &lt;element ref="{}service_request"/>
 *         &lt;element ref="{}start_job"/>
 *         &lt;element ref="{}subsystem.show"/>
 *         &lt;element ref="{}terminate"/>
 *         &lt;element ref="{}events.get"/>
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
    "config",
    "commands",
    "command",
    "answer",
    "addJobs",
    "addOrder",
    "checkFolders",
    "jobWhy",
    "killTask",
    "modifyJob",
    "modifyOrder",
    "modifySpooler",
    "jobChainCheckDistributed",
    "registerRemoteScheduler",
    "removeJobChain",
    "removeOrder",
    "runTime",
    "showCalendar",
    "showHistory",
    "showJob",
    "showJobs",
    "showJobChains",
    "showJobChain",
    "showOrder",
    "showProcessClasses",
    "s",
    "showSchedulers",
    "showState",
    "showTask",
    "serviceRequest",
    "startJob",
    "subsystemShow",
    "terminate",
    "eventsGet"
})
@XmlRootElement(name = "spooler")
public class Spooler {

    protected List<Spooler.Config> config;
    protected Commands commands;
    protected Commands command;
    protected Spooler.Answer answer;
    @XmlElement(name = "add_jobs")
    protected AddJobs addJobs;
    @XmlElement(name = "add_order")
    protected Order addOrder;
    @XmlElement(name = "check_folders")
    protected CheckFolders checkFolders;
    @XmlElement(name = "job.why")
    protected JobWhy jobWhy;
    @XmlElement(name = "kill_task")
    protected KillTask killTask;
    @XmlElement(name = "modify_job")
    protected ModifyJob modifyJob;
    @XmlElement(name = "modify_order")
    protected ModifyOrder modifyOrder;
    @XmlElement(name = "modify_spooler")
    protected ModifySpooler modifySpooler;
    @XmlElement(name = "job_chain.check_distributed")
    protected JobChainCheckDistributed jobChainCheckDistributed;
    @XmlElement(name = "register_remote_scheduler")
    protected RegisterRemoteScheduler registerRemoteScheduler;
    @XmlElement(name = "remove_job_chain")
    protected RemoveJobChain removeJobChain;
    @XmlElement(name = "remove_order")
    protected RemoveOrder removeOrder;
    @XmlElement(name = "run_time")
    protected RunTime runTime;
    @XmlElement(name = "show_calendar")
    protected ShowCalendar showCalendar;
    @XmlElement(name = "show_history")
    protected ShowHistory showHistory;
    @XmlElement(name = "show_job")
    protected ShowJob showJob;
    @XmlElement(name = "show_jobs")
    protected ShowJobs showJobs;
    @XmlElement(name = "show_job_chains")
    protected ShowJobChains showJobChains;
    @XmlElement(name = "show_job_chain")
    protected ShowJobChain showJobChain;
    @XmlElement(name = "show_order")
    protected ShowOrder showOrder;
    @XmlElement(name = "show_process_classes")
    protected ShowProcessClasses showProcessClasses;
    protected ShowState s;
    @XmlElement(name = "show_schedulers")
    protected ShowState showSchedulers;
    @XmlElement(name = "show_state")
    protected ShowState showState;
    @XmlElement(name = "show_task")
    protected ShowTask showTask;
    @XmlElement(name = "service_request")
    protected ServiceRequest serviceRequest;
    @XmlElement(name = "start_job")
    protected StartJob startJob;
    @XmlElement(name = "subsystem.show")
    protected SubsystemShow subsystemShow;
    protected Terminate terminate;
    @XmlElement(name = "events.get")
    protected EventsGet eventsGet;

    /**
     * Gets the value of the config property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the config property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConfig().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Spooler.Config }
     * 
     * 
     */
    public List<Spooler.Config> getConfig() {
        if (config == null) {
            config = new ArrayList<Spooler.Config>();
        }
        return this.config;
    }

    /**
     * Ruft den Wert der commands-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Commands }
     *     
     */
    public Commands getCommands() {
        return commands;
    }

    /**
     * Legt den Wert der commands-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Commands }
     *     
     */
    public void setCommands(Commands value) {
        this.commands = value;
    }

    /**
     * Ruft den Wert der command-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Commands }
     *     
     */
    public Commands getCommand() {
        return command;
    }

    /**
     * Legt den Wert der command-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Commands }
     *     
     */
    public void setCommand(Commands value) {
        this.command = value;
    }

    /**
     * Ruft den Wert der answer-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Spooler.Answer }
     *     
     */
    public Spooler.Answer getAnswer() {
        return answer;
    }

    /**
     * Legt den Wert der answer-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Spooler.Answer }
     *     
     */
    public void setAnswer(Spooler.Answer value) {
        this.answer = value;
    }

    /**
     * Ruft den Wert der addJobs-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AddJobs }
     *     
     */
    public AddJobs getAddJobs() {
        return addJobs;
    }

    /**
     * Legt den Wert der addJobs-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AddJobs }
     *     
     */
    public void setAddJobs(AddJobs value) {
        this.addJobs = value;
    }

    /**
     * Ruft den Wert der addOrder-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Order }
     *     
     */
    public Order getAddOrder() {
        return addOrder;
    }

    /**
     * Legt den Wert der addOrder-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Order }
     *     
     */
    public void setAddOrder(Order value) {
        this.addOrder = value;
    }

    /**
     * Ruft den Wert der checkFolders-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CheckFolders }
     *     
     */
    public CheckFolders getCheckFolders() {
        return checkFolders;
    }

    /**
     * Legt den Wert der checkFolders-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CheckFolders }
     *     
     */
    public void setCheckFolders(CheckFolders value) {
        this.checkFolders = value;
    }

    /**
     * Ruft den Wert der jobWhy-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JobWhy }
     *     
     */
    public JobWhy getJobWhy() {
        return jobWhy;
    }

    /**
     * Legt den Wert der jobWhy-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JobWhy }
     *     
     */
    public void setJobWhy(JobWhy value) {
        this.jobWhy = value;
    }

    /**
     * Ruft den Wert der killTask-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KillTask }
     *     
     */
    public KillTask getKillTask() {
        return killTask;
    }

    /**
     * Legt den Wert der killTask-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KillTask }
     *     
     */
    public void setKillTask(KillTask value) {
        this.killTask = value;
    }

    /**
     * Ruft den Wert der modifyJob-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ModifyJob }
     *     
     */
    public ModifyJob getModifyJob() {
        return modifyJob;
    }

    /**
     * Legt den Wert der modifyJob-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ModifyJob }
     *     
     */
    public void setModifyJob(ModifyJob value) {
        this.modifyJob = value;
    }

    /**
     * Ruft den Wert der modifyOrder-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ModifyOrder }
     *     
     */
    public ModifyOrder getModifyOrder() {
        return modifyOrder;
    }

    /**
     * Legt den Wert der modifyOrder-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ModifyOrder }
     *     
     */
    public void setModifyOrder(ModifyOrder value) {
        this.modifyOrder = value;
    }

    /**
     * Ruft den Wert der modifySpooler-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ModifySpooler }
     *     
     */
    public ModifySpooler getModifySpooler() {
        return modifySpooler;
    }

    /**
     * Legt den Wert der modifySpooler-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ModifySpooler }
     *     
     */
    public void setModifySpooler(ModifySpooler value) {
        this.modifySpooler = value;
    }

    /**
     * Ruft den Wert der jobChainCheckDistributed-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JobChainCheckDistributed }
     *     
     */
    public JobChainCheckDistributed getJobChainCheckDistributed() {
        return jobChainCheckDistributed;
    }

    /**
     * Legt den Wert der jobChainCheckDistributed-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JobChainCheckDistributed }
     *     
     */
    public void setJobChainCheckDistributed(JobChainCheckDistributed value) {
        this.jobChainCheckDistributed = value;
    }

    /**
     * Ruft den Wert der registerRemoteScheduler-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RegisterRemoteScheduler }
     *     
     */
    public RegisterRemoteScheduler getRegisterRemoteScheduler() {
        return registerRemoteScheduler;
    }

    /**
     * Legt den Wert der registerRemoteScheduler-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RegisterRemoteScheduler }
     *     
     */
    public void setRegisterRemoteScheduler(RegisterRemoteScheduler value) {
        this.registerRemoteScheduler = value;
    }

    /**
     * Ruft den Wert der removeJobChain-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RemoveJobChain }
     *     
     */
    public RemoveJobChain getRemoveJobChain() {
        return removeJobChain;
    }

    /**
     * Legt den Wert der removeJobChain-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RemoveJobChain }
     *     
     */
    public void setRemoveJobChain(RemoveJobChain value) {
        this.removeJobChain = value;
    }

    /**
     * Ruft den Wert der removeOrder-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RemoveOrder }
     *     
     */
    public RemoveOrder getRemoveOrder() {
        return removeOrder;
    }

    /**
     * Legt den Wert der removeOrder-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RemoveOrder }
     *     
     */
    public void setRemoveOrder(RemoveOrder value) {
        this.removeOrder = value;
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
     * Ruft den Wert der showCalendar-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ShowCalendar }
     *     
     */
    public ShowCalendar getShowCalendar() {
        return showCalendar;
    }

    /**
     * Legt den Wert der showCalendar-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ShowCalendar }
     *     
     */
    public void setShowCalendar(ShowCalendar value) {
        this.showCalendar = value;
    }

    /**
     * Ruft den Wert der showHistory-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ShowHistory }
     *     
     */
    public ShowHistory getShowHistory() {
        return showHistory;
    }

    /**
     * Legt den Wert der showHistory-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ShowHistory }
     *     
     */
    public void setShowHistory(ShowHistory value) {
        this.showHistory = value;
    }

    /**
     * Ruft den Wert der showJob-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ShowJob }
     *     
     */
    public ShowJob getShowJob() {
        return showJob;
    }

    /**
     * Legt den Wert der showJob-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ShowJob }
     *     
     */
    public void setShowJob(ShowJob value) {
        this.showJob = value;
    }

    /**
     * Ruft den Wert der showJobs-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ShowJobs }
     *     
     */
    public ShowJobs getShowJobs() {
        return showJobs;
    }

    /**
     * Legt den Wert der showJobs-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ShowJobs }
     *     
     */
    public void setShowJobs(ShowJobs value) {
        this.showJobs = value;
    }

    /**
     * Ruft den Wert der showJobChains-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ShowJobChains }
     *     
     */
    public ShowJobChains getShowJobChains() {
        return showJobChains;
    }

    /**
     * Legt den Wert der showJobChains-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ShowJobChains }
     *     
     */
    public void setShowJobChains(ShowJobChains value) {
        this.showJobChains = value;
    }

    /**
     * Ruft den Wert der showJobChain-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ShowJobChain }
     *     
     */
    public ShowJobChain getShowJobChain() {
        return showJobChain;
    }

    /**
     * Legt den Wert der showJobChain-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ShowJobChain }
     *     
     */
    public void setShowJobChain(ShowJobChain value) {
        this.showJobChain = value;
    }

    /**
     * Ruft den Wert der showOrder-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ShowOrder }
     *     
     */
    public ShowOrder getShowOrder() {
        return showOrder;
    }

    /**
     * Legt den Wert der showOrder-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ShowOrder }
     *     
     */
    public void setShowOrder(ShowOrder value) {
        this.showOrder = value;
    }

    /**
     * Ruft den Wert der showProcessClasses-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ShowProcessClasses }
     *     
     */
    public ShowProcessClasses getShowProcessClasses() {
        return showProcessClasses;
    }

    /**
     * Legt den Wert der showProcessClasses-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ShowProcessClasses }
     *     
     */
    public void setShowProcessClasses(ShowProcessClasses value) {
        this.showProcessClasses = value;
    }

    /**
     * Ruft den Wert der s-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ShowState }
     *     
     */
    public ShowState getS() {
        return s;
    }

    /**
     * Legt den Wert der s-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ShowState }
     *     
     */
    public void setS(ShowState value) {
        this.s = value;
    }

    /**
     * Ruft den Wert der showSchedulers-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ShowState }
     *     
     */
    public ShowState getShowSchedulers() {
        return showSchedulers;
    }

    /**
     * Legt den Wert der showSchedulers-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ShowState }
     *     
     */
    public void setShowSchedulers(ShowState value) {
        this.showSchedulers = value;
    }

    /**
     * Ruft den Wert der showState-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ShowState }
     *     
     */
    public ShowState getShowState() {
        return showState;
    }

    /**
     * Legt den Wert der showState-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ShowState }
     *     
     */
    public void setShowState(ShowState value) {
        this.showState = value;
    }

    /**
     * Ruft den Wert der showTask-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ShowTask }
     *     
     */
    public ShowTask getShowTask() {
        return showTask;
    }

    /**
     * Legt den Wert der showTask-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ShowTask }
     *     
     */
    public void setShowTask(ShowTask value) {
        this.showTask = value;
    }

    /**
     * Ruft den Wert der serviceRequest-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ServiceRequest }
     *     
     */
    public ServiceRequest getServiceRequest() {
        return serviceRequest;
    }

    /**
     * Legt den Wert der serviceRequest-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceRequest }
     *     
     */
    public void setServiceRequest(ServiceRequest value) {
        this.serviceRequest = value;
    }

    /**
     * Ruft den Wert der startJob-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link StartJob }
     *     
     */
    public StartJob getStartJob() {
        return startJob;
    }

    /**
     * Legt den Wert der startJob-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link StartJob }
     *     
     */
    public void setStartJob(StartJob value) {
        this.startJob = value;
    }

    /**
     * Ruft den Wert der subsystemShow-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SubsystemShow }
     *     
     */
    public SubsystemShow getSubsystemShow() {
        return subsystemShow;
    }

    /**
     * Legt den Wert der subsystemShow-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SubsystemShow }
     *     
     */
    public void setSubsystemShow(SubsystemShow value) {
        this.subsystemShow = value;
    }

    /**
     * Ruft den Wert der terminate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Terminate }
     *     
     */
    public Terminate getTerminate() {
        return terminate;
    }

    /**
     * Legt den Wert der terminate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Terminate }
     *     
     */
    public void setTerminate(Terminate value) {
        this.terminate = value;
    }

    /**
     * Ruft den Wert der eventsGet-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link EventsGet }
     *     
     */
    public EventsGet getEventsGet() {
        return eventsGet;
    }

    /**
     * Legt den Wert der eventsGet-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link EventsGet }
     *     
     */
    public void setEventsGet(EventsGet value) {
        this.eventsGet = value;
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
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Answer {


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
     *         &lt;element ref="{}extensions" minOccurs="0"/>
     *         &lt;element name="base" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="file" use="required" type="{}File" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element ref="{}params" minOccurs="0"/>
     *         &lt;element ref="{}security" minOccurs="0"/>
     *         &lt;element name="plugins" type="{}Plugins" minOccurs="0"/>
     *         &lt;element ref="{}cluster" minOccurs="0"/>
     *         &lt;element ref="{}process_classes" minOccurs="0"/>
     *         &lt;element name="schedules" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{}extensions" minOccurs="0"/>
     *                   &lt;element name="schedule" type="{}run_time" maxOccurs="unbounded" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="locks" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{}extensions" minOccurs="0"/>
     *                   &lt;element name="lock" type="{}lock" maxOccurs="unbounded" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;choice>
     *           &lt;element ref="{}script" minOccurs="0"/>
     *           &lt;element name="scheduler_script" type="{}scheduler_script" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;/choice>
     *         &lt;choice minOccurs="0">
     *           &lt;element ref="{}http_server"/>
     *           &lt;element ref="{}web_services"/>
     *         &lt;/choice>
     *         &lt;choice>
     *           &lt;element ref="{}holidays"/>
     *           &lt;element ref="{}holiday" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;/choice>
     *         &lt;element ref="{}jobs" minOccurs="0"/>
     *         &lt;element ref="{}job_chains" minOccurs="0"/>
     *         &lt;element ref="{}commands" minOccurs="0"/>
     *         &lt;element ref="{}monitors" minOccurs="0"/>
     *       &lt;/sequence>
     *       &lt;attribute name="spooler_id" type="{}Name" />
     *       &lt;attribute name="port" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
     *       &lt;attribute name="http_port" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
     *       &lt;attribute name="tcp_port" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
     *       &lt;attribute name="udp_port" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
     *       &lt;attribute name="ip_address" type="{}String" />
     *       &lt;attribute name="param" type="{}String" />
     *       &lt;attribute name="log_dir" type="{}File" />
     *       &lt;attribute name="priority_max" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
     *       &lt;attribute name="include_path" type="{}File" />
     *       &lt;attribute name="main_scheduler" type="{}String" />
     *       &lt;attribute name="mail_xslt_stylesheet" type="{}File" />
     *       &lt;attribute name="supervisor" type="{}String" />
     *       &lt;attribute name="configuration_directory" type="{}File" />
     *       &lt;attribute name="central_configuration_directory" type="{}File" />
     *       &lt;attribute name="configuration_add_event" type="{}Path" />
     *       &lt;attribute name="configuration_modify_event" type="{}Path" />
     *       &lt;attribute name="configuration_delete_event" type="{}Path" />
     *       &lt;attribute name="time_zone" type="{}String" />
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
        "base",
        "params",
        "security",
        "plugins",
        "cluster",
        "processClasses",
        "schedules",
        "locks",
        "script",
        "schedulerScript",
        "httpServer",
        "webServices",
        "holidays",
        "holiday",
        "jobs",
        "jobChains",
        "commands",
        "monitors"
    })
    public static class Config {

        protected Extensions extensions;
        protected List<Spooler.Config.Base> base;
        protected Params params;
        protected Security security;
        protected Plugins plugins;
        protected Cluster cluster;
        @XmlElement(name = "process_classes")
        protected ProcessClasses processClasses;
        protected Spooler.Config.Schedules schedules;
        protected Spooler.Config.Locks locks;
        protected Script script;
        @XmlElement(name = "scheduler_script")
        protected List<SchedulerScript> schedulerScript;
        @XmlElement(name = "http_server")
        protected HttpServer httpServer;
        @XmlElement(name = "web_services")
        protected WebServices webServices;
        protected Holidays holidays;
        protected List<Holiday> holiday;
        protected Jobs jobs;
        @XmlElement(name = "job_chains")
        protected JobChains jobChains;
        protected Commands commands;
        protected Monitors monitors;
        @XmlAttribute(name = "spooler_id")
        protected String spoolerId;
        @XmlAttribute(name = "port")
        @XmlSchemaType(name = "nonNegativeInteger")
        protected BigInteger port;
        @XmlAttribute(name = "http_port")
        @XmlSchemaType(name = "nonNegativeInteger")
        protected BigInteger httpPort;
        @XmlAttribute(name = "tcp_port")
        @XmlSchemaType(name = "nonNegativeInteger")
        protected BigInteger tcpPort;
        @XmlAttribute(name = "udp_port")
        @XmlSchemaType(name = "nonNegativeInteger")
        protected BigInteger udpPort;
        @XmlAttribute(name = "ip_address")
        protected String ipAddress;
        @XmlAttribute(name = "param")
        protected String param;
        @XmlAttribute(name = "log_dir")
        protected String logDir;
        @XmlAttribute(name = "priority_max")
        @XmlSchemaType(name = "nonNegativeInteger")
        protected BigInteger priorityMax;
        @XmlAttribute(name = "include_path")
        protected String includePath;
        @XmlAttribute(name = "main_scheduler")
        protected String mainScheduler;
        @XmlAttribute(name = "mail_xslt_stylesheet")
        protected String mailXsltStylesheet;
        @XmlAttribute(name = "supervisor")
        protected String supervisor;
        @XmlAttribute(name = "configuration_directory")
        protected String configurationDirectory;
        @XmlAttribute(name = "central_configuration_directory")
        protected String centralConfigurationDirectory;
        @XmlAttribute(name = "configuration_add_event")
        protected String configurationAddEvent;
        @XmlAttribute(name = "configuration_modify_event")
        protected String configurationModifyEvent;
        @XmlAttribute(name = "configuration_delete_event")
        protected String configurationDeleteEvent;
        @XmlAttribute(name = "time_zone")
        protected String timeZone;

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
         * Gets the value of the base property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the base property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getBase().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Spooler.Config.Base }
         * 
         * 
         */
        public List<Spooler.Config.Base> getBase() {
            if (base == null) {
                base = new ArrayList<Spooler.Config.Base>();
            }
            return this.base;
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
         * Ruft den Wert der security-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Security }
         *     
         */
        public Security getSecurity() {
            return security;
        }

        /**
         * Legt den Wert der security-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Security }
         *     
         */
        public void setSecurity(Security value) {
            this.security = value;
        }

        /**
         * Ruft den Wert der plugins-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Plugins }
         *     
         */
        public Plugins getPlugins() {
            return plugins;
        }

        /**
         * Legt den Wert der plugins-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Plugins }
         *     
         */
        public void setPlugins(Plugins value) {
            this.plugins = value;
        }

        /**
         * Ruft den Wert der cluster-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Cluster }
         *     
         */
        public Cluster getCluster() {
            return cluster;
        }

        /**
         * Legt den Wert der cluster-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Cluster }
         *     
         */
        public void setCluster(Cluster value) {
            this.cluster = value;
        }

        /**
         * Ruft den Wert der processClasses-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link ProcessClasses }
         *     
         */
        public ProcessClasses getProcessClasses() {
            return processClasses;
        }

        /**
         * Legt den Wert der processClasses-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link ProcessClasses }
         *     
         */
        public void setProcessClasses(ProcessClasses value) {
            this.processClasses = value;
        }

        /**
         * Ruft den Wert der schedules-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Spooler.Config.Schedules }
         *     
         */
        public Spooler.Config.Schedules getSchedules() {
            return schedules;
        }

        /**
         * Legt den Wert der schedules-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Spooler.Config.Schedules }
         *     
         */
        public void setSchedules(Spooler.Config.Schedules value) {
            this.schedules = value;
        }

        /**
         * Ruft den Wert der locks-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Spooler.Config.Locks }
         *     
         */
        public Spooler.Config.Locks getLocks() {
            return locks;
        }

        /**
         * Legt den Wert der locks-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Spooler.Config.Locks }
         *     
         */
        public void setLocks(Spooler.Config.Locks value) {
            this.locks = value;
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
         * Gets the value of the schedulerScript property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the schedulerScript property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSchedulerScript().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SchedulerScript }
         * 
         * 
         */
        public List<SchedulerScript> getSchedulerScript() {
            if (schedulerScript == null) {
                schedulerScript = new ArrayList<SchedulerScript>();
            }
            return this.schedulerScript;
        }

        /**
         * Ruft den Wert der httpServer-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link HttpServer }
         *     
         */
        public HttpServer getHttpServer() {
            return httpServer;
        }

        /**
         * Legt den Wert der httpServer-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link HttpServer }
         *     
         */
        public void setHttpServer(HttpServer value) {
            this.httpServer = value;
        }

        /**
         * Ruft den Wert der webServices-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link WebServices }
         *     
         */
        public WebServices getWebServices() {
            return webServices;
        }

        /**
         * Legt den Wert der webServices-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link WebServices }
         *     
         */
        public void setWebServices(WebServices value) {
            this.webServices = value;
        }

        /**
         * Ruft den Wert der holidays-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Holidays }
         *     
         */
        public Holidays getHolidays() {
            return holidays;
        }

        /**
         * Legt den Wert der holidays-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Holidays }
         *     
         */
        public void setHolidays(Holidays value) {
            this.holidays = value;
        }

        /**
         * Gets the value of the holiday property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the holiday property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getHoliday().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Holiday }
         * 
         * 
         */
        public List<Holiday> getHoliday() {
            if (holiday == null) {
                holiday = new ArrayList<Holiday>();
            }
            return this.holiday;
        }

        /**
         * Ruft den Wert der jobs-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Jobs }
         *     
         */
        public Jobs getJobs() {
            return jobs;
        }

        /**
         * Legt den Wert der jobs-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Jobs }
         *     
         */
        public void setJobs(Jobs value) {
            this.jobs = value;
        }

        /**
         * Ruft den Wert der jobChains-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link JobChains }
         *     
         */
        public JobChains getJobChains() {
            return jobChains;
        }

        /**
         * Legt den Wert der jobChains-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link JobChains }
         *     
         */
        public void setJobChains(JobChains value) {
            this.jobChains = value;
        }

        /**
         * Ruft den Wert der commands-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Commands }
         *     
         */
        public Commands getCommands() {
            return commands;
        }

        /**
         * Legt den Wert der commands-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Commands }
         *     
         */
        public void setCommands(Commands value) {
            this.commands = value;
        }

        /**
         * Ruft den Wert der monitors-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Monitors }
         *     
         */
        public Monitors getMonitors() {
            return monitors;
        }

        /**
         * Legt den Wert der monitors-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Monitors }
         *     
         */
        public void setMonitors(Monitors value) {
            this.monitors = value;
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
         * Ruft den Wert der port-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getPort() {
            return port;
        }

        /**
         * Legt den Wert der port-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setPort(BigInteger value) {
            this.port = value;
        }

        /**
         * Ruft den Wert der httpPort-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getHttpPort() {
            return httpPort;
        }

        /**
         * Legt den Wert der httpPort-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setHttpPort(BigInteger value) {
            this.httpPort = value;
        }

        /**
         * Ruft den Wert der tcpPort-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getTcpPort() {
            return tcpPort;
        }

        /**
         * Legt den Wert der tcpPort-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setTcpPort(BigInteger value) {
            this.tcpPort = value;
        }

        /**
         * Ruft den Wert der udpPort-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getUdpPort() {
            return udpPort;
        }

        /**
         * Legt den Wert der udpPort-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setUdpPort(BigInteger value) {
            this.udpPort = value;
        }

        /**
         * Ruft den Wert der ipAddress-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIpAddress() {
            return ipAddress;
        }

        /**
         * Legt den Wert der ipAddress-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIpAddress(String value) {
            this.ipAddress = value;
        }

        /**
         * Ruft den Wert der param-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getParam() {
            return param;
        }

        /**
         * Legt den Wert der param-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setParam(String value) {
            this.param = value;
        }

        /**
         * Ruft den Wert der logDir-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLogDir() {
            return logDir;
        }

        /**
         * Legt den Wert der logDir-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLogDir(String value) {
            this.logDir = value;
        }

        /**
         * Ruft den Wert der priorityMax-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getPriorityMax() {
            return priorityMax;
        }

        /**
         * Legt den Wert der priorityMax-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setPriorityMax(BigInteger value) {
            this.priorityMax = value;
        }

        /**
         * Ruft den Wert der includePath-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIncludePath() {
            return includePath;
        }

        /**
         * Legt den Wert der includePath-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIncludePath(String value) {
            this.includePath = value;
        }

        /**
         * Ruft den Wert der mainScheduler-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMainScheduler() {
            return mainScheduler;
        }

        /**
         * Legt den Wert der mainScheduler-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMainScheduler(String value) {
            this.mainScheduler = value;
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
         * Ruft den Wert der supervisor-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSupervisor() {
            return supervisor;
        }

        /**
         * Legt den Wert der supervisor-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSupervisor(String value) {
            this.supervisor = value;
        }

        /**
         * Ruft den Wert der configurationDirectory-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getConfigurationDirectory() {
            return configurationDirectory;
        }

        /**
         * Legt den Wert der configurationDirectory-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setConfigurationDirectory(String value) {
            this.configurationDirectory = value;
        }

        /**
         * Ruft den Wert der centralConfigurationDirectory-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCentralConfigurationDirectory() {
            return centralConfigurationDirectory;
        }

        /**
         * Legt den Wert der centralConfigurationDirectory-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCentralConfigurationDirectory(String value) {
            this.centralConfigurationDirectory = value;
        }

        /**
         * Ruft den Wert der configurationAddEvent-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getConfigurationAddEvent() {
            return configurationAddEvent;
        }

        /**
         * Legt den Wert der configurationAddEvent-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setConfigurationAddEvent(String value) {
            this.configurationAddEvent = value;
        }

        /**
         * Ruft den Wert der configurationModifyEvent-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getConfigurationModifyEvent() {
            return configurationModifyEvent;
        }

        /**
         * Legt den Wert der configurationModifyEvent-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setConfigurationModifyEvent(String value) {
            this.configurationModifyEvent = value;
        }

        /**
         * Ruft den Wert der configurationDeleteEvent-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getConfigurationDeleteEvent() {
            return configurationDeleteEvent;
        }

        /**
         * Legt den Wert der configurationDeleteEvent-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setConfigurationDeleteEvent(String value) {
            this.configurationDeleteEvent = value;
        }

        /**
         * Ruft den Wert der timeZone-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTimeZone() {
            return timeZone;
        }

        /**
         * Legt den Wert der timeZone-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTimeZone(String value) {
            this.timeZone = value;
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
         *       &lt;attribute name="file" use="required" type="{}File" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Base {

            @XmlAttribute(name = "file", required = true)
            protected String file;

            /**
             * Ruft den Wert der file-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getFile() {
                return file;
            }

            /**
             * Legt den Wert der file-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setFile(String value) {
                this.file = value;
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
         *         &lt;element ref="{}extensions" minOccurs="0"/>
         *         &lt;element name="lock" type="{}lock" maxOccurs="unbounded" minOccurs="0"/>
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
            "extensions",
            "lock"
        })
        public static class Locks {

            protected Extensions extensions;
            protected List<Lock> lock;

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
             * Gets the value of the lock property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the lock property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getLock().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Lock }
             * 
             * 
             */
            public List<Lock> getLock() {
                if (lock == null) {
                    lock = new ArrayList<Lock>();
                }
                return this.lock;
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
         *         &lt;element ref="{}extensions" minOccurs="0"/>
         *         &lt;element name="schedule" type="{}run_time" maxOccurs="unbounded" minOccurs="0"/>
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
            "extensions",
            "schedule"
        })
        public static class Schedules {

            protected Extensions extensions;
            protected List<RunTime> schedule;

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
             * Gets the value of the schedule property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the schedule property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getSchedule().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link RunTime }
             * 
             * 
             */
            public List<RunTime> getSchedule() {
                if (schedule == null) {
                    schedule = new ArrayList<RunTime>();
                }
                return this.schedule;
            }

        }

    }

}
