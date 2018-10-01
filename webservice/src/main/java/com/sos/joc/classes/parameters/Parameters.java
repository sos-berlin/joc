package com.sos.joc.classes.parameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.json.JsonObject;
import javax.xml.transform.TransformerException;

import org.apache.xpath.CachedXPathAPI;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.model.common.NameValuePair;

public class Parameters {
    
    public static void sortParameters(List<NameValuePair> parameters) {
        Collections.sort(parameters, new Comparator<NameValuePair>() {
            @Override
            public int compare(NameValuePair param1, NameValuePair param2)
            {
                return  param1.getName().compareTo(param2.getName());
            }
        });
    }
        

    public static List<NameValuePair> getParameters(Element elem) throws TransformerException {
        CachedXPathAPI xPath = new CachedXPathAPI();
        NodeList paramList = xPath.selectNodeList(elem, "params/param");
        if (paramList != null && paramList.getLength() > 0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (int paramsCount = 0; paramsCount < paramList.getLength(); paramsCount++) {
                NameValuePair param = new NameValuePair();
                Element paramElement = (Element) paramList.item(paramsCount);
                param.setName(paramElement.getAttribute(WebserviceConstants.NAME));
                param.setValue(paramElement.getAttribute(WebserviceConstants.VALUE));
                params.add(param);
            }
            sortParameters(params);
            return params;
        } else {
            return null;
        }
    }
    
    public static List<NameValuePair> getParameters(JsonObject elem) {
        return getParameters(elem, "variables");
    }
    
    public static List<NameValuePair> getParameters(JsonObject elem, String elemName) {
        JsonObject paramList = elem.getJsonObject(elemName);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (paramList != null && !paramList.isEmpty()) {
            for (String key : paramList.keySet()) {
                NameValuePair param = new NameValuePair();
                param.setName(key);
                param.setValue(paramList.getString(key, ""));
                params.add(param);
            }
            sortParameters(params);
            return params;
        } else {
            return null;
        }
    }

}
