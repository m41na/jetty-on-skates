package com.jarredweb.jesty.servlet;

import com.jarredweb.jesty.route.AppRoute;
import com.jarredweb.jesty.route.AppRoutes;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReRouteFilter implements Filter {

    public static final Logger LOG = LoggerFactory.getLogger(ReRouteFilter.class);

    protected FilterConfig fConfig;
    private final AppRoutes routes;

    public ReRouteFilter(AppRoutes routes) {
        super();
        this.routes = routes;
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        this.fConfig = fConfig;
    }

    @Override
    public void destroy() {
        //nothing to clean up
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HandlerRequest httpRequest = new HandlerRequest((HttpServletRequest) request);
        HandlerResponse httpResponse = new HandlerResponse((HttpServletResponse)response);

        AppRoute route = routes.match(httpRequest);
        if (route != null) {
            httpRequest.route(route);
           httpRequest.getRequestDispatcher(route.pathId).forward(httpRequest, httpResponse);
        } else {
            chain.doFilter(httpRequest, httpResponse);
        }
    }    
}
