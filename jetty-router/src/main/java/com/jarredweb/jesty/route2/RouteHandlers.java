package com.jarredweb.jesty.route2;

import com.jarredweb.jesty.route.AppRoute;
import com.jarredweb.jesty.route.AppRoutes;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.annotation.ManagedObject;

/**
 * Matches handler to route to process the request
 *
 * @author mainas
 */
@ManagedObject("Route Handlers Collection")
public class RouteHandlers extends HandlerCollection {

    private final AppRoutes routes;

    public RouteHandlers(AppRoutes routes) {
        this.routes = routes;
    }

    public RouteHandlers(AppRoutes routes, Handler... handlers) {
        super(handlers);
        this.routes = routes;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Handler[] handlers = getHandlers();

        if (handlers != null && isStarted()) {
            AppRoute route = routes.search(request);
            if (route != null) {
                for (Handler handler1 : handlers) {
                    RouteHandler handler = (RouteHandler) handler1;
                    handler.handle(target, baseRequest, request, response);
                    if (baseRequest.isHandled()) {
                        return;
                    }
                }
            }
        }
    }
}
