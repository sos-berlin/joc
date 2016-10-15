package com.sos.joc.classes;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

 
import org.junit.Test;

public class JOCResourceImplTest {

    

   @Test
    public void getDateFromStringTest() {
       JOCResourceImpl jocResourceImpl  = new JOCResourceImpl();
       Date date = jocResourceImpl.getDateFromString("2016-08-22T08:15:48.760Z");
       int offset = Calendar.getInstance().getTimeZone().getOffset(date.getTime());
       date.setTime(date.getTime()-offset);
       assertEquals("getDateFromStringTest", 8, date.getHours());
       date = jocResourceImpl.getDateFromString("2016-08-22 08:15:48.760Z");
       date.setTime(date.getTime()-offset);
       assertEquals("getDateFromStringTest", 8, date.getHours());

   }
       
   @Test
   public void getDateFromTimestampTest(){
       JOCResourceImpl jocResourceImpl  = new JOCResourceImpl();
       Date date = jocResourceImpl.getDateFromTimestamp(new Long("1471854067629000"));
       int offset = Calendar.getInstance().getTimeZone().getOffset(date.getTime());
       date.setTime(date.getTime()-offset);
       assertEquals("getDateFromStringTest", 8, date.getHours());
   }

   @Test
   public void normalizePathTest(){
       JOCResourceImpl jocResourceImpl  = new JOCResourceImpl();
       assertEquals("normalizePathTest", "/1/2/3",jocResourceImpl.normalizePath("1/2/3") );
       assertEquals("normalizePathTest", "/1/2/3",jocResourceImpl.normalizePath("/1/2/3") );
       assertEquals("normalizePathTest", "/1",jocResourceImpl.normalizePath("1") );
       assertEquals("normalizePathTest", "/1",jocResourceImpl.normalizePath("/1") );
   }

   @Test
   public void getParentTest(){
       JOCResourceImpl jocResourceImpl  = new JOCResourceImpl();
       assertEquals("normalizePathTest", "/1/2",jocResourceImpl.getParent("/1/2//3") );
       assertEquals("normalizePathTest", "/1/2",jocResourceImpl.getParent("/1/2/3/") );
       assertEquals("normalizePathTest", null,jocResourceImpl.getParent("/") );
       assertEquals("normalizePathTest", "/",jocResourceImpl.getParent("/1") );
   }   
}
