package com.sos.joc.classes;

import static org.junit.Assert.*;

 
import java.util.Date;

 
import org.junit.Test;

public class JOCResourceImplTest {

    

   @Test
    public void getDateFromStringTest() {
       JOCResourceImpl jocResourceImpl  = new JOCResourceImpl();
       Date date = jocResourceImpl.getDateFromString("2016-08-22T08:15:48.760Z");
       assertEquals("getDateFromStringTest", 8, date.getHours());
       date = jocResourceImpl.getDateFromString("2016-08-22 08:15:48.760Z");
       assertEquals("getDateFromStringTest", 8, date.getHours());

   }
       
   @Test
   public void getDateFromTimestampTest(){
       JOCResourceImpl jocResourceImpl  = new JOCResourceImpl();
       Date date = jocResourceImpl.getDateFromTimestamp(new Long("1471854067629000"));
       assertEquals("getDateFromStringTest", 8, date.getHours());
   }

   
}
