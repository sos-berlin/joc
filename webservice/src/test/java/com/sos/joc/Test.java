package com.sos.joc;

import static org.junit.Assert.*;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.sos.jitl.restclient.JobSchedulerRestClient;

public class Test {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void test() throws Exception {
        JobSchedulerRestClient.headers.put("Authorization", "Basic U09TMDE6c29zMDE=");
        JobSchedulerRestClient.headers.put("Content-Type", "application/json");

        String response = JobSchedulerRestClient.executeRestServiceCommand("post", "http://localhost:8080/rest/security/login");
        System.out.println(response);

        JsonReader rdr = Json.createReader(new StringReader(response));
        JsonObject obj = rdr.readObject();
        System.out.println(obj.getJsonString("accessToken"));

    }

}
