/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jarredweb.jetty.sample.app;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;

public class AppServerTest {
    
    public static void main(String...args) throws Exception {
        HttpClient httpClient = new HttpClient();
        httpClient.setFollowRedirects(true);
        httpClient.start();
        
        ContentResponse response = httpClient.GET("http://localhost:8080/hello/");
        //System.out.format("", args)
    }
}
