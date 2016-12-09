//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.12.08 um 05:17:49 PM CET 
//


package com.sos.joc.model.commands;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für command complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="command">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{}add_order"/>
 *         &lt;element name="check_folders" type="{}check_folders"/>
 *         &lt;element ref="{}kill_task"/>
 *         &lt;element name="lock" type="{}lock"/>
 *         &lt;element name="lock.remove" type="{}lock.remove"/>
 *         &lt;element ref="{}modify_job"/>
 *         &lt;element ref="{}modify_order"/>
 *         &lt;element ref="{}modify_spooler"/>
 *         &lt;element ref="{}modify_hot_folder"/>
 *         &lt;element ref="{}job_chain.modify"/>
 *         &lt;element ref="{}job_chain_node.modify"/>
 *         &lt;element name="order" type="{}order"/>
 *         &lt;element ref="{}param.get"/>
 *         &lt;element ref="{}params.get"/>
 *         &lt;element ref="{}plugin.command"/>
 *         &lt;element name="process_class" type="{}process_class"/>
 *         &lt;element name="process_class.remove" type="{}process_class.remove"/>
 *         &lt;element ref="{}job_chain.check_distributed"/>
 *         &lt;element ref="{}register_remote_scheduler"/>
 *         &lt;element ref="{}remove_job_chain"/>
 *         &lt;element ref="{}remove_order"/>
 *         &lt;element name="run_time" type="{}run_time"/>
 *         &lt;element ref="{}setting.set"/>
 *         &lt;element ref="{}scheduler_log.log_categories.reset"/>
 *         &lt;element ref="{}scheduler_log.log_categories.set"/>
 *         &lt;element ref="{}scheduler_log.log_categories.show"/>
 *         &lt;element ref="{}show_calendar"/>
 *         &lt;element ref="{}show_history"/>
 *         &lt;element ref="{}show_job"/>
 *         &lt;element ref="{}show_jobs"/>
 *         &lt;element ref="{}show_job_chains"/>
 *         &lt;element ref="{}show_job_chain"/>
 *         &lt;element ref="{}show_order"/>
 *         &lt;element ref="{}s"/>
 *         &lt;element ref="{}show_schedulers"/>
 *         &lt;element ref="{}show_state"/>
 *         &lt;element ref="{}show_task"/>
 *         &lt;element ref="{}service_request"/>
 *         &lt;element ref="{}start_job"/>
 *         &lt;element ref="{}subsystem.show"/>
 *         &lt;element ref="{}remote_scheduler.start_remote_task"/>
 *         &lt;element ref="{}remote_scheduler.remote_task.close"/>
 *         &lt;element ref="{}remote_scheduler.remote_task.kill"/>
 *         &lt;element ref="{}terminate"/>
 *       &lt;/choice>
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
@XmlType(name = "command", namespace = "http://www.sos-berlin.com/scheduler", propOrder = {
    "addOrder",
    "checkFolders",
    "killTask",
    "lock",
    "lockRemove",
    "modifyJob",
    "modifyOrder",
    "modifySpooler",
    "modifyHotFolder",
    "jobChainModify",
    "jobChainNodeModify",
    "order",
    "paramGet",
    "paramsGet",
    "pluginCommand",
    "processClass",
    "processClassRemove",
    "jobChainCheckDistributed",
    "registerRemoteScheduler",
    "removeJobChain",
    "removeOrder",
    "runTime",
    "settingSet",
    "schedulerLogLogCategoriesReset",
    "schedulerLogLogCategoriesSet",
    "schedulerLogLogCategoriesShow",
    "showCalendar",
    "showHistory",
    "showJob",
    "showJobs",
    "showJobChains",
    "showJobChain",
    "showOrder",
    "s",
    "showSchedulers",
    "showState",
    "showTask",
    "serviceRequest",
    "startJob",
    "subsystemShow",
    "remoteSchedulerStartRemoteTask",
    "remoteSchedulerRemoteTaskClose",
    "remoteSchedulerRemoteTaskKill",
    "terminate"
})
public class Command {

    @XmlElement(name = "add_order")
    protected Order addOrder;
    @XmlElement(name = "check_folders", namespace = "http://www.sos-berlin.com/scheduler")
    protected CheckFolders checkFolders;
    @XmlElement(name = "kill_task")
    protected KillTask killTask;
    @XmlElement(namespace = "http://www.sos-berlin.com/scheduler")
    protected Lock lock;
    @XmlElement(name = "lock.remove", namespace = "http://www.sos-berlin.com/scheduler")
    protected LockRemove lockRemove;
    @XmlElement(name = "modify_job")
    protected ModifyJob modifyJob;
    @XmlElement(name = "modify_order")
    protected ModifyOrder modifyOrder;
    @XmlElement(name = "modify_spooler")
    protected ModifySpooler modifySpooler;
    @XmlElement(name = "modify_hot_folder")
    protected ModifyHotFolder modifyHotFolder;
    @XmlElement(name = "job_chain.modify")
    protected JobChainModify jobChainModify;
    @XmlElement(name = "job_chain_node.modify")
    protected JobChainNodeModify jobChainNodeModify;
    @XmlElement(namespace = "http://www.sos-berlin.com/scheduler")
    protected Order order;
    @XmlElement(name = "param.get")
    protected ParamGet paramGet;
    @XmlElement(name = "params.get")
    protected Object paramsGet;
    @XmlElement(name = "plugin.command")
    protected PluginCommand pluginCommand;
    @XmlElement(name = "process_class", namespace = "http://www.sos-berlin.com/scheduler")
    protected ProcessClass processClass;
    @XmlElement(name = "process_class.remove", namespace = "http://www.sos-berlin.com/scheduler")
    protected ProcessClassRemove processClassRemove;
    @XmlElement(name = "job_chain.check_distributed")
    protected JobChainCheckDistributed jobChainCheckDistributed;
    @XmlElement(name = "register_remote_scheduler")
    protected RegisterRemoteScheduler registerRemoteScheduler;
    @XmlElement(name = "remove_job_chain")
    protected RemoveJobChain removeJobChain;
    @XmlElement(name = "remove_order")
    protected RemoveOrder removeOrder;
    @XmlElement(name = "run_time", namespace = "http://www.sos-berlin.com/scheduler")
    protected RunTime runTime;
    @XmlElement(name = "setting.set")
    protected SettingSet settingSet;
    @XmlElement(name = "scheduler_log.log_categories.reset")
    protected SchedulerLogLogCategoriesReset schedulerLogLogCategoriesReset;
    @XmlElement(name = "scheduler_log.log_categories.set")
    protected SchedulerLogLogCategoriesSet schedulerLogLogCategoriesSet;
    @XmlElement(name = "scheduler_log.log_categories.show")
    protected Object schedulerLogLogCategoriesShow;
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
    @XmlElement(name = "remote_scheduler.start_remote_task")
    protected RemoteSchedulerStartRemoteTask remoteSchedulerStartRemoteTask;
    @XmlElement(name = "remote_scheduler.remote_task.close")
    protected RemoteSchedulerRemoteTaskClose remoteSchedulerRemoteTaskClose;
    @XmlElement(name = "remote_scheduler.remote_task.kill")
    protected RemoteSchedulerRemoteTaskKill remoteSchedulerRemoteTaskKill;
    protected Terminate terminate;
    @XmlAttribute(name = "url")
    protected String url;
    @XmlAttribute(name = "jobschedulerId")
    protected String jobschedulerId;

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
     * Ruft den Wert der lock-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Lock }
     *     
     */
    public Lock getLock() {
        return lock;
    }

    /**
     * Legt den Wert der lock-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Lock }
     *     
     */
    public void setLock(Lock value) {
        this.lock = value;
    }

    /**
     * Ruft den Wert der lockRemove-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link LockRemove }
     *     
     */
    public LockRemove getLockRemove() {
        return lockRemove;
    }

    /**
     * Legt den Wert der lockRemove-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link LockRemove }
     *     
     */
    public void setLockRemove(LockRemove value) {
        this.lockRemove = value;
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
     * Ruft den Wert der modifyHotFolder-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ModifyHotFolder }
     *     
     */
    public ModifyHotFolder getModifyHotFolder() {
        return modifyHotFolder;
    }

    /**
     * Legt den Wert der modifyHotFolder-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ModifyHotFolder }
     *     
     */
    public void setModifyHotFolder(ModifyHotFolder value) {
        this.modifyHotFolder = value;
    }

    /**
     * Ruft den Wert der jobChainModify-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JobChainModify }
     *     
     */
    public JobChainModify getJobChainModify() {
        return jobChainModify;
    }

    /**
     * Legt den Wert der jobChainModify-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JobChainModify }
     *     
     */
    public void setJobChainModify(JobChainModify value) {
        this.jobChainModify = value;
    }

    /**
     * Ruft den Wert der jobChainNodeModify-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link JobChainNodeModify }
     *     
     */
    public JobChainNodeModify getJobChainNodeModify() {
        return jobChainNodeModify;
    }

    /**
     * Legt den Wert der jobChainNodeModify-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link JobChainNodeModify }
     *     
     */
    public void setJobChainNodeModify(JobChainNodeModify value) {
        this.jobChainNodeModify = value;
    }

    /**
     * Ruft den Wert der order-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Order }
     *     
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Legt den Wert der order-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Order }
     *     
     */
    public void setOrder(Order value) {
        this.order = value;
    }

    /**
     * Ruft den Wert der paramGet-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ParamGet }
     *     
     */
    public ParamGet getParamGet() {
        return paramGet;
    }

    /**
     * Legt den Wert der paramGet-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ParamGet }
     *     
     */
    public void setParamGet(ParamGet value) {
        this.paramGet = value;
    }

    /**
     * Ruft den Wert der paramsGet-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getParamsGet() {
        return paramsGet;
    }

    /**
     * Legt den Wert der paramsGet-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setParamsGet(Object value) {
        this.paramsGet = value;
    }

    /**
     * Ruft den Wert der pluginCommand-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PluginCommand }
     *     
     */
    public PluginCommand getPluginCommand() {
        return pluginCommand;
    }

    /**
     * Legt den Wert der pluginCommand-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PluginCommand }
     *     
     */
    public void setPluginCommand(PluginCommand value) {
        this.pluginCommand = value;
    }

    /**
     * Ruft den Wert der processClass-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ProcessClass }
     *     
     */
    public ProcessClass getProcessClass() {
        return processClass;
    }

    /**
     * Legt den Wert der processClass-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcessClass }
     *     
     */
    public void setProcessClass(ProcessClass value) {
        this.processClass = value;
    }

    /**
     * Ruft den Wert der processClassRemove-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ProcessClassRemove }
     *     
     */
    public ProcessClassRemove getProcessClassRemove() {
        return processClassRemove;
    }

    /**
     * Legt den Wert der processClassRemove-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcessClassRemove }
     *     
     */
    public void setProcessClassRemove(ProcessClassRemove value) {
        this.processClassRemove = value;
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
     * Ruft den Wert der settingSet-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SettingSet }
     *     
     */
    public SettingSet getSettingSet() {
        return settingSet;
    }

    /**
     * Legt den Wert der settingSet-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SettingSet }
     *     
     */
    public void setSettingSet(SettingSet value) {
        this.settingSet = value;
    }

    /**
     * Ruft den Wert der schedulerLogLogCategoriesReset-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SchedulerLogLogCategoriesReset }
     *     
     */
    public SchedulerLogLogCategoriesReset getSchedulerLogLogCategoriesReset() {
        return schedulerLogLogCategoriesReset;
    }

    /**
     * Legt den Wert der schedulerLogLogCategoriesReset-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SchedulerLogLogCategoriesReset }
     *     
     */
    public void setSchedulerLogLogCategoriesReset(SchedulerLogLogCategoriesReset value) {
        this.schedulerLogLogCategoriesReset = value;
    }

    /**
     * Ruft den Wert der schedulerLogLogCategoriesSet-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SchedulerLogLogCategoriesSet }
     *     
     */
    public SchedulerLogLogCategoriesSet getSchedulerLogLogCategoriesSet() {
        return schedulerLogLogCategoriesSet;
    }

    /**
     * Legt den Wert der schedulerLogLogCategoriesSet-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SchedulerLogLogCategoriesSet }
     *     
     */
    public void setSchedulerLogLogCategoriesSet(SchedulerLogLogCategoriesSet value) {
        this.schedulerLogLogCategoriesSet = value;
    }

    /**
     * Ruft den Wert der schedulerLogLogCategoriesShow-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getSchedulerLogLogCategoriesShow() {
        return schedulerLogLogCategoriesShow;
    }

    /**
     * Legt den Wert der schedulerLogLogCategoriesShow-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setSchedulerLogLogCategoriesShow(Object value) {
        this.schedulerLogLogCategoriesShow = value;
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
     * Ruft den Wert der remoteSchedulerStartRemoteTask-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RemoteSchedulerStartRemoteTask }
     *     
     */
    public RemoteSchedulerStartRemoteTask getRemoteSchedulerStartRemoteTask() {
        return remoteSchedulerStartRemoteTask;
    }

    /**
     * Legt den Wert der remoteSchedulerStartRemoteTask-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RemoteSchedulerStartRemoteTask }
     *     
     */
    public void setRemoteSchedulerStartRemoteTask(RemoteSchedulerStartRemoteTask value) {
        this.remoteSchedulerStartRemoteTask = value;
    }

    /**
     * Ruft den Wert der remoteSchedulerRemoteTaskClose-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RemoteSchedulerRemoteTaskClose }
     *     
     */
    public RemoteSchedulerRemoteTaskClose getRemoteSchedulerRemoteTaskClose() {
        return remoteSchedulerRemoteTaskClose;
    }

    /**
     * Legt den Wert der remoteSchedulerRemoteTaskClose-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RemoteSchedulerRemoteTaskClose }
     *     
     */
    public void setRemoteSchedulerRemoteTaskClose(RemoteSchedulerRemoteTaskClose value) {
        this.remoteSchedulerRemoteTaskClose = value;
    }

    /**
     * Ruft den Wert der remoteSchedulerRemoteTaskKill-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link RemoteSchedulerRemoteTaskKill }
     *     
     */
    public RemoteSchedulerRemoteTaskKill getRemoteSchedulerRemoteTaskKill() {
        return remoteSchedulerRemoteTaskKill;
    }

    /**
     * Legt den Wert der remoteSchedulerRemoteTaskKill-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link RemoteSchedulerRemoteTaskKill }
     *     
     */
    public void setRemoteSchedulerRemoteTaskKill(RemoteSchedulerRemoteTaskKill value) {
        this.remoteSchedulerRemoteTaskKill = value;
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
