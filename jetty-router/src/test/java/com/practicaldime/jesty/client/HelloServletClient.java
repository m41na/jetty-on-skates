package com.practicaldime.jesty.client;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import static org.junit.Assert.*;
import org.junit.Test;

public class HelloServletClient {

    private final boolean ready = false;

    @Test
    public void testAsyncServlet() throws Exception {
        if (ready) {
            String url = "http://localhost:8080/hello/steve/yes";
            HttpClient httpClient = new HttpClient();
            // Configure HttpClient, for example:
            httpClient.setFollowRedirects(true);

            // Start HttpClient
            httpClient.start();
            ContentResponse response = httpClient.GET(url);

            assertEquals(response.getStatus(), 200);
            String responseContent = response.getContentAsString();
            assertEquals(responseContent, "<h1>Hello from Async HelloServlet</h1>");
        }
    }
}
