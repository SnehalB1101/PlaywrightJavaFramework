package tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class APITesting {
 @Test
 public void e2eApiTest()
         //Login
    {
        HashMap<Object,Object> loginPayload=new HashMap<>();
        loginPayload.put("email","rahulshetty1@yahoo.com");
        loginPayload.put("password","Magiclife1!");


        Playwright playwright=Playwright.create();
        APIRequestContext apiRequest=playwright.request().newContext();
        APIResponse loginResponse=apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                RequestOptions.create().setData(loginPayload));

        //System.out.println(loginResponse.text());
        //System.out.println(loginResponse.status());
        //System.out.println(loginResponse.statusText());
        Assert.assertTrue(loginResponse.ok());
        System.out.println(loginResponse.text());
        String token=JsonPath.read(loginResponse.text(),"$.token");
        System.out.println("Login Success "+token);

        //Create Event
        String eventTitle="Playwright API Testing";
        HashMap<Object,Object> createEventPayload=new HashMap<>();
        createEventPayload.put("title",eventTitle);
        createEventPayload.put("description","API Testing Details");
        createEventPayload.put("category","Sports");
        createEventPayload.put("venue","Test Venue1");
        createEventPayload.put("city","Pune");
        createEventPayload.put("eventDate","2026-11-26T13:01:00.000Z");
        createEventPayload.put("price",100);
        createEventPayload.put("totalSeats",10);

        APIResponse eventResponse=apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events",
        RequestOptions.create().setHeader("Authorization","Bearer "+token)
        .setData(createEventPayload));
        //Assert.assertTrue(eventResponse.ok(),"Create Event API should succeed");
        Assert.assertTrue(
                eventResponse.ok(),
                "Create Event Failed\nStatus = " + eventResponse.status()
                        + "\nBody = " + eventResponse.text());
        int eventID=JsonPath.read(eventResponse.text(),"$.data.id");
        System.out.println("Event created & its ID is "+eventID);

        //Get Event
        APIResponse retrieveEvents=apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
        RequestOptions.create().setQueryParam("page",1).setQueryParam("limit",12)
                .setHeader("Authorization","Bearer "+token));
        Assert.assertTrue(retrieveEvents.ok(), "Event API Retrieval should be succeed");
        System.out.println(retrieveEvents.text());
        List<Integer> allEventIds=JsonPath.read(retrieveEvents.text(),"$.data[*].id");
        Assert.assertTrue(allEventIds.contains(eventID),"Created event should be visible in the events list");

      //Delete Event

        APIResponse deleteResponse=apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/"+eventID,
                RequestOptions.create().setHeader("Authorization","Bearer "+token));
        Assert.assertTrue(deleteResponse.ok());

        // Verify deletion is success
        APIResponse verifyRes= apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create()
                        .setHeader("Authorization","Bearer "+token)
                        .setQueryParam("page",1)
                        .setQueryParam("Limit",12));
        Assert.assertTrue(verifyRes.ok(),"Post-delete events list call should succeed");
        List<String>idsAfterDelete=JsonPath.read(verifyRes.text(),"$.data[*].id");
        Assert.assertFalse(idsAfterDelete.contains(eventID),
                "Deleted event must no longer appear in the event List");
        System.out.println("Deletion verified:event no longer in list.");






    }


}
