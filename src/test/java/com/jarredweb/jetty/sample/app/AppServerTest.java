/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jarredweb.jetty.sample.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;

public class AppServerTest {
    
    public static void main(String...args) throws Exception {
        HttpClient httpClient = new HttpClient();
        httpClient.setFollowRedirects(true);
        httpClient.start();
        
        ContentResponse response = httpClient.GET("http://localhost:8080/hello/");
        System.out.format("%d %s %s%n%s%n", response.getStatus(), response.getReason(), response.getVersion(), new String(response.getContent()));
        
        response = httpClient.newRequest("http://localhost:8080/form")
                .method(HttpMethod.POST)
                .param("message", SimpleDateFormat.getInstance().format(new Date()))
                .send();
        
        System.out.format("%d %s %s%n%s%n", response.getStatus(), response.getReason(), response.getVersion(), new String(response.getContent()));
        
        response = httpClient.newRequest("http://localhost:8080/rs/yo")
                .method(HttpMethod.GET)
                .send();
        
        System.out.format("%d %s %s%n%s%n", response.getStatus(), response.getReason(), response.getVersion(), new String(response.getContent()));
        
        httpClient.stop();
    }
}
