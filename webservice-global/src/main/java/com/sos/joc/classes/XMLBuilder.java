package com.sos.joc.classes;

 
import org.dom4j.DocumentHelper;

import com.sos.joc.exceptions.JobSchedulerBadRequestException;

public class XMLBuilder extends com.sos.xml.XMLBuilder{
    
   
    public XMLBuilder(String name) {
        super(name);
    }

    public static String getRootElementName(String xmlString) throws Exception {
        try {
            return DocumentHelper.parseText(xmlString).getRootElement().getName();
        } catch (Exception e) {
            throw new JobSchedulerBadRequestException(e);
        }
    }
    
     

}
