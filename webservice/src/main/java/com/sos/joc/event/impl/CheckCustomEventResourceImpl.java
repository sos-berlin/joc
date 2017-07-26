package com.sos.joc.event.impl;

import java.sql.Date;
import java.time.Instant;

import javax.ws.rs.Path;

import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.NodeList;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.event.resource.ICheckCustomResource;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.event.custom.CheckEvent;
import com.sos.joc.model.event.custom.CheckResult;

import sos.xml.SOSXMLXPath;

@Path("events")
public class CheckCustomEventResourceImpl extends JOCResourceImpl implements ICheckCustomResource {

    private static final String API_CALL = "./events/custom/check";
    private String xPath = "";
    
    @Override
    public JOCDefaultResponse checkEvent(String accessToken, CheckEvent checkEvent) {
        SOSHibernateSession connection = null;
        
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL, checkEvent, accessToken, checkEvent.getJobschedulerId(),
                    getPermissonsCommands(accessToken).getJobschedulerMaster().getView().isParameter());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            DBItemInventoryInstance dbItemInventorySupervisorInstance = null;
            connection = Globals.createSosHibernateStatelessConnection(API_CALL);
            Long supervisorId = dbItemInventoryInstance.getSupervisorId();
            if (supervisorId != DBLayer.DEFAULT_ID) {
                InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(connection);
                dbItemInventorySupervisorInstance = dbLayer.getInventoryInstanceByKey(supervisorId);
            }
            if (dbItemInventorySupervisorInstance == null) {
                dbItemInventorySupervisorInstance = dbItemInventoryInstance;
            }
            
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventorySupervisorInstance);
            jocXmlCommand.executePost("<param.get name=\"JobSchedulerEventJob.events\"/>", accessToken);
            
            CheckResult entity = new CheckResult();
            entity.setCount(0);
            entity.setDeliveryDate(Date.from(Instant.now()));
            
            String eventParamValue = jocXmlCommand.getSosxml().selectSingleNodeValue("//param[@name='JobSchedulerEventJob.events']/@value");
            
            if (eventParamValue != null) {
                eventParamValue = eventParamValue.replaceAll("(\\uC3BE|þ|Ã¾)", "<").replaceAll("(\\uC3BF|ÿ|Ã¿)", ">");
                eventParamValue = StringEscapeUtils.unescapeHtml4(eventParamValue);
                SOSXMLXPath dom = new SOSXMLXPath(new StringBuffer(eventParamValue));
                
                if (checkEvent.getXPath() != null && !checkEvent.getXPath().isEmpty()) {
                    NodeList nl = dom.selectNodeList(checkEvent.getXPath());
                    if (nl != null) {
                        entity.setCount(nl.getLength());
                    }
                } else {
                    if (checkEvent.getEventClass() != null && !checkEvent.getEventClass().isEmpty()) {
                        buildXPath("event_class", checkEvent.getEventClass());
                    }
                    if (checkEvent.getEventId() != null && !checkEvent.getEventId().isEmpty()) {
                        buildXPath("event_id", checkEvent.getEventId());
                    }
                    if (checkEvent.getExitCode() != null) {
                        buildXPath("exit_code", checkEvent.getExitCode()+"");
                    }
                    if (!xPath.isEmpty()) {
                        xPath = "//events/event[" + xPath + "]";
                    } else {
                        xPath = "//events/event";
                    }
                    NodeList nl = dom.selectNodeList(xPath);
                    if (nl != null) {
                        entity.setCount(nl.getLength());
                    }
                }
            }
            
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
        
    }
    
    private void buildXPath(String attr, String value) {
        String[] values = value.trim().split("\\s+");
        String xPathOr = "";
        for (int i = 0; i < values.length; i++) {
           if (i == 0) {
               xPathOr += "@"+attr+"='"+values[i]+"'";
           } else {
               xPathOr += "or @"+attr+"='"+values[i]+"'";
           }
        }
        if (values.length > 1) {
            xPathOr = "(" + xPathOr + ")";
        }
        if (values.length > 0) {
            if (!xPath.isEmpty()) {
                xPathOr = " and " + xPathOr;
            }
            xPath += xPathOr;
        }
    }

}
