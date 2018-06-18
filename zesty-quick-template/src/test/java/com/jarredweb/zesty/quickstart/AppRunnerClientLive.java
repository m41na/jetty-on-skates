package com.jarredweb.zesty.quickstart;

import com.jarredweb.zesty.setup.ZestyLive;
import com.jarredweb.zesty.setup.UseProvider;
import java.net.URI;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

@RunWith(ZestyLive.class)
@UseProvider(AppRunnerProvider.class)
public class AppRunnerClientLive {

    @Test
    public void testGETHelloText() throws Exception {
        // prepare HttpClient
        HttpClient httpClient = new HttpClient();
        httpClient.setFollowRedirects(true);
        httpClient.start();

        //fire up request
        URI uri = URI.create("http://localhost:8082/ws");
        String request = uri.toString();
        ContentResponse response = httpClient.newRequest(request)
                .method(HttpMethod.GET)
                .agent("functional-test")
                .send();

        //assert result
        String actual = response.getContentAsString();
        assertEquals("You got this for real!!", actual);
        httpClient.stop();
    }
    
    @Test
    public void testGETHelloText2() throws Exception  {
        HttpClient httpClient = new HttpClient();
        httpClient.setFollowRedirects(true);
        httpClient.start();

        //fire up request
        URI uri = URI.create("http://localhost:8082/ws");
        String request = uri.toString();
        ContentResponse response = httpClient.newRequest(request)
                .method(HttpMethod.GET)
                .agent("functional-test")
                .send();

        //assert result
        String actual = response.getContentAsString();
        assertEquals("You got this for real!!", actual);
        httpClient.stop();
    }
}
