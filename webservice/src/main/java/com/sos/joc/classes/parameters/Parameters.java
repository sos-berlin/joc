package com.sos.joc.classes.parameters;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sos.joc.classes.WebserviceConstants;
import com.sos.joc.model.common.NameValuePairsSchema;

public class Parameters {

    public static List<NameValuePairsSchema> getParameters(){
        List<NameValuePairsSchema> parameters = new ArrayList<NameValuePairsSchema>();
        NameValuePairsSchema param1 = new NameValuePairsSchema();
        NameValuePairsSchema param2 = new NameValuePairsSchema();
        param1.setName("param1");
        param1.setValue("value1");
        param2.setName("param2");
        param2.setValue("value2");
        parameters.add(param1);
        parameters.add(param1);
        return parameters;
    }

    public static List<NameValuePairsSchema> getParameters(NodeList paramList) {
        List<NameValuePairsSchema> params = new ArrayList<NameValuePairsSchema>();
        if (paramList != null && paramList.getLength() > 0) {
            for (int paramsCount = 0; paramsCount < paramList.getLength(); paramsCount++) {
                NameValuePairsSchema param = new NameValuePairsSchema();
                Element paramElement = (Element) paramList.item(paramsCount);
                param.setName(paramElement.getAttribute(WebserviceConstants.NAME));
                param.setValue(paramElement.getAttribute(WebserviceConstants.VALUE));
                params.add(param);
            }
            return params;
        } else {
            return null;
        }
    }

}
