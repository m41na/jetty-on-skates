package com.jarredweb.jettsey.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class FormHandler extends AbstractHandler{

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String message = request.getParameter("message");
        
        StringBuilder form = new StringBuilder();
        form.append("<form action='/form/' method='POST'>");
        form.append("<textarea rows='5' cols='40' name='message'></textarea><br/>");
        form.append("<input type='submit' value='Send'/><br/>");
        form.append("</form><br/>");
        form.append("<p>").append(message != null? message : "").append("</p>");
        
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        
        //write back response
        response.getWriter().println(form.toString());
        
        //inform jetty this request has been handled
        baseRequest.setHandled(true);
    }
}
