package com.sos.joc.classes;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import com.sos.joc.model.commands.LockRemove;
import com.sos.joc.model.commands.ModifyHotFolder;
import com.sos.joc.model.commands.ObjectFactory;
import com.sos.joc.model.commands.Order;
import com.sos.joc.model.commands.ProcessClassRemove;
import com.sos.joc.model.commands.RunTime;
import com.sos.joc.model.commands.ScheduleRemove;
import com.sos.joc.model.commands.ShowState;

public class JobSchedulerCommandFactory {
    private ObjectFactory objectFactory;
    private JAXBElement<?> jaxbElement;

    public JobSchedulerCommandFactory() {
        super();
        objectFactory = new ObjectFactory();
        jaxbElement = null;
    }

    private String asXml(Object command, JAXBElement<?> j) throws JAXBException {
        String xml = "";
        JAXBContext jaxbContext = JAXBContext.newInstance(command.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter s = new StringWriter();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        if (j == null) {
            jaxbMarshaller.marshal(command, s);
        } else {
            jaxbMarshaller.marshal(j, s);
        }
        xml = s.toString();
        return xml;
    }
 

    public String getXml(Object command) throws JAXBException {
        jaxbElement = null;
        if (command instanceof JAXBElement) {
            jaxbElement = (JAXBElement<?>) command;

            System.out.println("--------->" + jaxbElement.getName().getLocalPart());

            if ("show_state".equals(jaxbElement.getName().toString())) {
                jaxbElement = objectFactory.createShowState((ShowState) jaxbElement.getValue());
                command = (ShowState)jaxbElement.getValue();
            }
            if ("add_order".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createAddOrder((Order) jaxbElement.getValue());
                command = (Order)jaxbElement.getValue();
            }
            if ("lock.remove".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createLockRemove((LockRemove) jaxbElement.getValue());
                command = (LockRemove)jaxbElement.getValue();
            }
            if ("modif_hot_folder".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createModifyHotFolder((ModifyHotFolder) jaxbElement.getValue());
                command = (ModifyHotFolder)jaxbElement.getValue();
            }
            if ("process_class.remove".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createProcessClassRemove((ProcessClassRemove) jaxbElement.getValue());
                command = (ProcessClassRemove)jaxbElement.getValue();
            }
            if ("s".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createS((ShowState) jaxbElement.getValue());
                command = (ShowState)jaxbElement.getValue();
            }
            if ("schedule.remove".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createScheduleRemove((ScheduleRemove) jaxbElement.getValue());
                command = (ScheduleRemove)jaxbElement.getValue();
            }
            if ("show_schedulers".equals(jaxbElement.getName().getLocalPart().toString())) {
                jaxbElement = objectFactory.createShowSchedulers((ShowState) jaxbElement.getValue());
                command = (ShowState)jaxbElement.getValue();
            }
             
        }

        return asXml(command, jaxbElement);
    }

}
