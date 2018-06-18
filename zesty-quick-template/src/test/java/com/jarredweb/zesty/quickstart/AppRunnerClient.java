package com.jarredweb.zesty.quickstart;

import java.net.URI;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.Test;
import static org.junit.Assert.*;

public class AppRunnerClient {

    @Test
    public void testGETHelloText() throws Exception {
        AppRunner runner = new AppRunner() {

            @Override
            public void onServerStarted(int port, String host) {
                //fire up client
                try {
                    // prepare HttpClient
                    HttpClient httpClient = new HttpClient();
                    httpClient.setFollowRedirects(true);
                    httpClient.start();

                    //fire up request
                    URI uri = URI.create("http://" + host + ":" + port + "/ws");
                    String request = uri.toString();
                    ContentResponse response = httpClient.newRequest(request)
                            .method(HttpMethod.GET)
                            .agent("functional-test")
                            .send();

                    //assert result
                    String actual = response.getContentAsString();
                    assertEquals("You got this for real!!", actual);
                    httpClient.stop();
                    //stop server
                    shutdownServer();
                } catch (Exception e) {
                    fail(String.format("unexpected failure -> %s", e.getMessage()));
                }
            }
        };
        
        runner.startApplication();
    }
}
