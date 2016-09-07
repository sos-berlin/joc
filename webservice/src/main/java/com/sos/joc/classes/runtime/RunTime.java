package com.sos.joc.classes.runtime;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

import com.sos.joc.Globals;
import com.sos.joc.model.job.JobFilterSchema;
import com.sos.joc.model.order.OrderFilterSchema;
import com.sos.scheduler.model.commands.JSCmdShowJob;
import com.sos.scheduler.model.commands.JSCmdShowOrder;


public class RunTime {

    public static String getRuntimeXmlString(Node runtimeNode) throws Exception {
        Source source = new DOMSource(runtimeNode);
        StringWriter writer = new StringWriter();
        Result result = new StreamResult(writer);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        transformer.transform(source, result);
        return writer.toString();
    }

    public static String createJobRuntimePostCommand(final JobFilterSchema body) {
        if (!body.getJob().isEmpty()) {
            JSCmdShowJob showJob = Globals.schedulerObjectFactory.createShowJob();
            showJob.setWhat("run_time");
            showJob.setJob(body.getJob());
            return Globals.schedulerObjectFactory.toXMLString(showJob);
        }
        return null;
    }

    public static String createOrderRuntimePostCommand(final OrderFilterSchema body) {
        if (!body.getOrderId().isEmpty()) {
            JSCmdShowOrder showOrder = Globals.schedulerObjectFactory.createShowOrder();
            showOrder.setWhat("run_time");
            showOrder.setOrder(body.getOrderId());
            return Globals.schedulerObjectFactory.toXMLString(showOrder);
        }
        return null;
    }

}
