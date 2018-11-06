package com.sos.joc;

import static org.junit.Assert.*;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

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
    @Ignore
    public void test() throws Exception {
        JobSchedulerRestClient.headers.put("Authorization", "Basic U09TMDE6c29zMDE=");
        JobSchedulerRestClient.headers.put("Content-Type", "application/json");

        String response = JobSchedulerRestClient.executeRestServiceCommand("post", "http://localhost:8080/rest/security/login");
        System.out.println(response);

        JsonReader rdr = Json.createReader(new StringReader(response));
        JsonObject obj = rdr.readObject();
        System.out.println(obj.getJsonString("accessToken"));

    }

    @org.junit.Test
    @Ignore
    public void jobSchedulerWebSservice() throws Exception {
        JobSchedulerRestClient.headers.put("Authorization", "Basic U09TMDE6c29zMDE=");
        JobSchedulerRestClient.headers.put("Content-Type", "application/json");

        String response = JobSchedulerRestClient.executeRestServiceCommand("get", "http://localhost:4404/jobscheduler/master/api/order/?return=OrdersComplemented");
        System.out.println(response);

        JsonReader rdr = Json.createReader(new StringReader(response));
        JsonObject obj = rdr.readObject();
        System.out.println(obj.getJsonNumber("eventId"));

        JsonArray results = obj.getJsonArray("orders");
        for (JsonObject result : results.getValuesAs(JsonObject.class)) {
            System.out.println(result.getString("nodeId", ""));
            System.out.println(result.getString("orderSourceType", ""));
            System.out.println(result.getString("path", ""));
            System.out.println(result.getString("nextStepAt", ""));
            System.out.println(result.getString("fileBasedState", ""));
            try {
                System.out.println(result.getJsonString("processingState").getString());
            } catch (Exception e) {

                System.out.println(result.getJsonObject("processingState").getString("at"));
                System.out.println(result.getJsonObject("processingState").getString("TYPE"));

            }
            JsonArray obstacles = result.getJsonArray("obstacles");
            if (obstacles != null) {
                for (JsonValue obstacle : obstacles) {
                    System.out.println(obstacle.toString());
                }
            }

            System.out.println("-----------");
        }

    }

}
