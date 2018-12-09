package com.practicaldime.jesty.route2;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.practicaldime.zesty.route.AppRoute;


public class RouteHandler extends AbstractHandler{
    
    private final AppRoute route;

    public RouteHandler(AppRoute route) {
        this.route = route;
    }

    public AppRoute getRoute() {
        return route;
    }
    
    public String param(String key) {
        return route.pathParams.get(key);
    }
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();

        out.println("<h1>" + param(":lang") + "</h1>");
        
        baseRequest.setHandled(true);
    }
}
