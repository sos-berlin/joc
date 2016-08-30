package com.sos.joc.classes.runtime;

import java.util.Date;

import com.sos.joc.model.common.Runtime200Schema;
import com.sos.joc.model.common.RuntimeSchema;

public class RunTimeEntity {
    
  public Runtime200Schema getEntity (){
        Runtime200Schema entity = new Runtime200Schema();

        entity.setDeliveryDate(new Date());

        RuntimeSchema runtimeSchema = new RuntimeSchema();
        runtimeSchema.setRunTime("myRuntime");
        runtimeSchema.setSurveyDate(new Date());
        entity.setRunTime(runtimeSchema);
        return entity;
    }
}
