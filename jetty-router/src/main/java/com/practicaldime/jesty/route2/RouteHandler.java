package com.practicaldime.jesty.route2;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.practicaldime.zesty.router.Route;


public class RouteHandler extends AbstractHandler{
    
    private final Route route;

    public RouteHandler(Route route) {
        this.route = route;
    }

    public Route getRoute() {
        return route;
    }
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();

        out.println("<h1>".concat(route.method) + ":" + route.path.concat("</h1>"));
        
        baseRequest.setHandled(true);
    }
}
