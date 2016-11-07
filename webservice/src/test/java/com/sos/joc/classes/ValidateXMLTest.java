package com.sos.joc.classes;

import org.junit.Assert;
import org.junit.Test;

import com.sos.joc.classes.jobscheduler.ValidateXML;

public class ValidateXMLTest {
    
    
    @Test
    public void runTimeTest() throws Exception{
        StringBuffer s = new StringBuffer();
        s.append("<run_time/>");
        boolean check = false;
        try {
            check = ValidateXML.validateRunTimeAgainstJobSchedulerSchema(s.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(check);
    }
    
}
