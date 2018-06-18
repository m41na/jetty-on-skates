package com.jarredweb.editor.app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class BlogginHandler extends AbstractHandler{

    private final String destDir = "www/bloggin";
    private final String destFile = "books.xml";
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String method = baseRequest.getMethod();
        if(method.equalsIgnoreCase("POST")){
            try{
                //handle post resuest
                StringBuilder xml = new StringBuilder();
                String line;
                while((line = baseRequest.getReader().readLine()) != null){
                    xml.append(line);
                }
                System.out.println(xml.toString());
                saveToDisk(xml);
                
                //create response
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                response.setHeader("Location", "");
            }catch(Exception e){
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                
                PrintWriter out = response.getWriter();
                out.println(e.getMessage());
            }
        }
        else{
            response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                
                PrintWriter out = response.getWriter();
                out.println("Method handles only 'POST' requests");
        }
        
        //flag as done
        baseRequest.setHandled(true);
    }    

    private void saveToDisk(StringBuilder xml) throws IOException {
        try(FileWriter fout = new FileWriter(new File(destDir, destFile))){
            fout.write(xml.toString());
        }
    }
}
