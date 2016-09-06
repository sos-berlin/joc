package com.sos.joc.classes.parameters;

import java.util.ArrayList;
import java.util.List;

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

}
