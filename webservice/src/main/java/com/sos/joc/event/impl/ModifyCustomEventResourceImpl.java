package com.sos.joc.event.impl;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import org.dom4j.Element;

import com.sos.hibernate.classes.SOSHibernateSession;
import com.sos.jitl.reporting.db.DBItemInventoryInstance;
import com.sos.jitl.reporting.db.DBLayer;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.classes.JOCXmlCommand;
import com.sos.joc.classes.JobSchedulerDate;
import com.sos.joc.classes.XMLBuilder;
import com.sos.joc.db.inventory.instances.InventoryInstancesDBLayer;
import com.sos.joc.event.resource.IModifyCustomEventResource;
import com.sos.joc.exceptions.JobSchedulerBadRequestException;
import com.sos.joc.exceptions.JocMissingRequiredParameterException;
import com.sos.joc.model.common.NameValuePair;
import com.sos.joc.model.order.ModifyOrder;
import com.sos.joc.model.order.ModifyOrders;

@Path("events")
public class ModifyCustomEventResourceImpl extends JOCResourceImpl implements IModifyCustomEventResource {

    private static final String API_CALL = "./events/custom/";
    
    @Override
    public JOCDefaultResponse addEvent(String accessToken, ModifyOrders modifyEvent) {
        return executeModifyEvent("add", modifyEvent, accessToken); 
    }

    @Override
    public JOCDefaultResponse removeEvent(String accessToken, ModifyOrders modifyEvent) {
        return executeModifyEvent("remove", modifyEvent, accessToken);
    }
    
    private JOCDefaultResponse executeModifyEvent(String request, ModifyOrders modifyEvent, String accessToken) {
        
        SOSHibernateSession connection = null;
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL + request, modifyEvent, accessToken, modifyEvent.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getJobChain().getExecute().isAddOrder());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            
            DBItemInventoryInstance dbItemInventorySupervisorInstance = null;
            connection = Globals.createSosHibernateStatelessConnection(API_CALL + request);
            Long supervisorId = dbItemInventoryInstance.getSupervisorId();
            if (supervisorId != DBLayer.DEFAULT_ID) {
                InventoryInstancesDBLayer dbLayer = new InventoryInstancesDBLayer(connection);
                dbItemInventorySupervisorInstance = dbLayer.getInventoryInstanceByKey(supervisorId);
            }
            if (dbItemInventorySupervisorInstance == null) {
                dbItemInventorySupervisorInstance = dbItemInventoryInstance;
            }
            
            if (modifyEvent.getOrders().size() == 0) {
                throw new JocMissingRequiredParameterException("undefined 'order'");
            }
            
            Map<String,Boolean> requiredParams = new HashMap<String,Boolean>();
            requiredParams.put("event_class", Boolean.FALSE);
            if ("add".equals(request)) {
                requiredParams.put("event_id", Boolean.FALSE);
                requiredParams.put("exit_code", Boolean.FALSE);
            }
            
            ModifyOrder order = modifyEvent.getOrders().get(0);
            checkRequiredParameter("jobChain", order.getJobChain());
            
            XMLBuilder xml = new XMLBuilder("add_order");
            xml.addAttribute("job_chain", normalizePath(order.getJobChain()));
            boolean createdFound = false;
            if (order.getParams() != null && !order.getParams().isEmpty()) {
                Element params = XMLBuilder.create("params");
                for (NameValuePair param : order.getParams()) {
                    if (requiredParams.containsKey(param.getName()) && !param.getValue().isEmpty()) {
                        requiredParams.put(param.getName(), Boolean.TRUE);
                    }
                    if ("created".equals(param.getName()) && !param.getValue().isEmpty()) {
                        createdFound = true;
                    }
                    params.addElement("param").addAttribute("name", param.getName()).addAttribute("value", param.getValue());
                }
                params.addElement("param").addAttribute("name", "action").addAttribute("value", request);
                if (!createdFound) {
                    params.addElement("param").addAttribute("name", "created").addAttribute("value", JobSchedulerDate.getNowInISO()); 
                }
                xml.add(params);
            }
            
            for (Map.Entry<String,Boolean> entry : requiredParams.entrySet()) {
               if (!entry.getValue()) {
                   throw new JocMissingRequiredParameterException("undefined " + entry.getKey());
               }
            }
            
            JOCXmlCommand jocXmlCommand = new JOCXmlCommand(dbItemInventorySupervisorInstance);
            jocXmlCommand.executePostWithThrowBadRequest(xml.asXML(), getAccessToken());
            
            return JOCDefaultResponse.responsePlainStatus200("JobScheduler response: OK");
        } catch (JobSchedulerBadRequestException e) {
            String errorOutput = "JobScheduler reports error: " + e.getError().getMessage();
            return JOCDefaultResponse.responsePlainStatus420(errorOutput);
        } catch (Exception e) {
            String errorOutput = e.getClass().getSimpleName() + ": " +((e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
            return JOCDefaultResponse.responsePlainStatus420(errorOutput);
        } finally {
            Globals.disconnect(connection);
        }
    }

}
