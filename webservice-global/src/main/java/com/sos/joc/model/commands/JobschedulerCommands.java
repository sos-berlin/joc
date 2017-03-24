//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.03.24 um 02:41:35 PM CET 
//


package com.sos.joc.model.commands;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
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
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element ref="{}add_order"/>
 *         &lt;element name="check_folders" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element ref="{}kill_task"/>
 *         &lt;element name="lock.remove" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element ref="{}modify_job"/>
 *         &lt;element ref="{}modify_order"/>
 *         &lt;element ref="{}modify_spooler"/>
 *         &lt;element ref="{}modify_hot_folder"/>
 *         &lt;element ref="{}job.why"/>
 *         &lt;element ref="{}job_chain.modify"/>
 *         &lt;element ref="{}job_chain_node.modify"/>
 *         &lt;element name="order" type="{}order"/>
 *         &lt;element ref="{}param.get"/>
 *         &lt;element ref="{}params.get"/>
 *         &lt;element ref="{}plugin.command"/>
 *         &lt;element name="process_class" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="process_class.remove" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element ref="{}job_chain.check_distributed"/>
 *         &lt;element ref="{}register_remote_scheduler"/>
 *         &lt;element ref="{}remove_job_chain"/>
 *         &lt;element ref="{}remove_order"/>
 *         &lt;element name="run_time" type="{}run_time"/>
 *         &lt;element ref="{}setting.set"/>
 *         &lt;element ref="{}schedule.remove"/>
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
 *         &lt;element ref="{}show_schedulers"/>
 *         &lt;element ref="{}s"/>
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
 *       &lt;attribute name="url" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="jobschedulerId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="comment" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="timeSpent" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="ticketLink" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "addOrderOrCheckFoldersOrKillTask"
})
@XmlRootElement(name = "jobscheduler_commands")
public class JobschedulerCommands {

    @XmlElementRefs({
        @XmlElementRef(name = "show_state", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "scheduler_log.log_categories.show", type = SchedulerLogLogCategoriesShow.class, required = false),
        @XmlElementRef(name = "check_folders", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "modify_hot_folder", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "process_class.remove", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "show_task", type = ShowTask.class, required = false),
        @XmlElementRef(name = "param.get", type = ParamGet.class, required = false),
        @XmlElementRef(name = "kill_task", type = KillTask.class, required = false),
        @XmlElementRef(name = "remove_job_chain", type = RemoveJobChain.class, required = false),
        @XmlElementRef(name = "scheduler_log.log_categories.reset", type = SchedulerLogLogCategoriesReset.class, required = false),
        @XmlElementRef(name = "process_class", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "show_calendar", type = ShowCalendar.class, required = false),
        @XmlElementRef(name = "remote_scheduler.start_remote_task", type = RemoteSchedulerStartRemoteTask.class, required = false),
        @XmlElementRef(name = "params.get", type = ParamsGet.class, required = false),
        @XmlElementRef(name = "remote_scheduler.remote_task.close", type = RemoteSchedulerRemoteTaskClose.class, required = false),
        @XmlElementRef(name = "terminate", type = Terminate.class, required = false),
        @XmlElementRef(name = "show_job", type = ShowJob.class, required = false),
        @XmlElementRef(name = "register_remote_scheduler", type = RegisterRemoteScheduler.class, required = false),
        @XmlElementRef(name = "job.why", type = JobWhy.class, required = false),
        @XmlElementRef(name = "job_chain_node.modify", type = JobChainNodeModify.class, required = false),
        @XmlElementRef(name = "job_chain.check_distributed", type = JobChainCheckDistributed.class, required = false),
        @XmlElementRef(name = "modify_spooler", type = ModifySpooler.class, required = false),
        @XmlElementRef(name = "job_chain.modify", type = JobChainModify.class, required = false),
        @XmlElementRef(name = "add_order", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "modify_order", type = ModifyOrder.class, required = false),
        @XmlElementRef(name = "schedule.remove", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "show_schedulers", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "remove_order", type = RemoveOrder.class, required = false),
        @XmlElementRef(name = "run_time", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "show_order", type = ShowOrder.class, required = false),
        @XmlElementRef(name = "order", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "lock.remove", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "setting.set", type = SettingSet.class, required = false),
        @XmlElementRef(name = "show_history", type = ShowHistory.class, required = false),
        @XmlElementRef(name = "remote_scheduler.remote_task.kill", type = RemoteSchedulerRemoteTaskKill.class, required = false),
        @XmlElementRef(name = "start_job", type = StartJob.class, required = false),
        @XmlElementRef(name = "modify_job", type = ModifyJob.class, required = false),
        @XmlElementRef(name = "show_jobs", type = ShowJobs.class, required = false),
        @XmlElementRef(name = "subsystem.show", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "s", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "scheduler_log.log_categories.set", type = SchedulerLogLogCategoriesSet.class, required = false),
        @XmlElementRef(name = "show_job_chains", type = ShowJobChains.class, required = false),
        @XmlElementRef(name = "plugin.command", type = PluginCommand.class, required = false),
        @XmlElementRef(name = "show_job_chain", type = ShowJobChain.class, required = false),
        @XmlElementRef(name = "service_request", type = ServiceRequest.class, required = false)
    })
    protected List<Object> addOrderOrCheckFoldersOrKillTask;
    @XmlAttribute(name = "url")
    protected String url;
    @XmlAttribute(name = "jobschedulerId")
    protected String jobschedulerId;
    @XmlAttribute(name = "comment")
    protected String comment;
    @XmlAttribute(name = "timeSpent")
    protected Integer timeSpent;
    @XmlAttribute(name = "ticketLink")
    protected String ticketLink;

    /**
     * Gets the value of the addOrderOrCheckFoldersOrKillTask property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addOrderOrCheckFoldersOrKillTask property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddOrderOrCheckFoldersOrKillTask().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ShowState }{@code >}
     * {@link SchedulerLogLogCategoriesShow }
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link ModifyHotFolder }{@code >}
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link ShowTask }
     * {@link ParamGet }
     * {@link KillTask }
     * {@link RemoveJobChain }
     * {@link SchedulerLogLogCategoriesReset }
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link ShowCalendar }
     * {@link RemoteSchedulerStartRemoteTask }
     * {@link ParamsGet }
     * {@link RemoteSchedulerRemoteTaskClose }
     * {@link Terminate }
     * {@link ShowJob }
     * {@link RegisterRemoteScheduler }
     * {@link JobWhy }
     * {@link JobChainNodeModify }
     * {@link JobChainCheckDistributed }
     * {@link ModifySpooler }
     * {@link JobChainModify }
     * {@link JAXBElement }{@code <}{@link Order }{@code >}
     * {@link ModifyOrder }
     * {@link JAXBElement }{@code <}{@link ScheduleRemove }{@code >}
     * {@link JAXBElement }{@code <}{@link ShowState }{@code >}
     * {@link RemoveOrder }
     * {@link JAXBElement }{@code <}{@link RunTime }{@code >}
     * {@link ShowOrder }
     * {@link JAXBElement }{@code <}{@link Order }{@code >}
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link SettingSet }
     * {@link ShowHistory }
     * {@link RemoteSchedulerRemoteTaskKill }
     * {@link StartJob }
     * {@link ModifyJob }
     * {@link ShowJobs }
     * {@link JAXBElement }{@code <}{@link SubsystemShow }{@code >}
     * {@link JAXBElement }{@code <}{@link ShowState }{@code >}
     * {@link SchedulerLogLogCategoriesSet }
     * {@link ShowJobChains }
     * {@link PluginCommand }
     * {@link ShowJobChain }
     * {@link ServiceRequest }
     * 
     * 
     */
    public List<Object> getAddOrderOrCheckFoldersOrKillTask() {
        if (addOrderOrCheckFoldersOrKillTask == null) {
            addOrderOrCheckFoldersOrKillTask = new ArrayList<Object>();
        }
        return this.addOrderOrCheckFoldersOrKillTask;
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

    /**
     * Ruft den Wert der comment-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Legt den Wert der comment-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Ruft den Wert der timeSpent-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTimeSpent() {
        return timeSpent;
    }

    /**
     * Legt den Wert der timeSpent-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTimeSpent(Integer value) {
        this.timeSpent = value;
    }

    /**
     * Ruft den Wert der ticketLink-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTicketLink() {
        return ticketLink;
    }

    /**
     * Legt den Wert der ticketLink-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTicketLink(String value) {
        this.ticketLink = value;
    }

}
