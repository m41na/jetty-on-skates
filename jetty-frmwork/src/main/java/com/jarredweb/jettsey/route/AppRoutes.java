package com.jarredweb.jettsey.route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.jetty.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppRoutes {

    private static final Logger LOG = LoggerFactory.getLogger(AppRoutes.class);
    private final List<AppRoute> routes = new ArrayList<>();

    public void addRoute(AppRoute route) {
        this.routes.add(route);
    }

    public AppRoute match(Request input) {
        for (int i = 0; i < routes.size(); i++) {
            AppRoute route = routes.get(i);

            String method = input.getMethod();
            if (route.method != null && method != null) {
                if (!method.toLowerCase().equals(route.method.toLowerCase())) {
                    continue;
                }
            }
            String accepts = input.getHeader("Accepts");
            if (route.accepts != null && accepts != null) {
                if (!accepts.contains(route.accepts)) {
                    continue;
                }
            }
            String contentType = input.getHeader("Content-Type");
            if (route.contentType != null && contentType != null) {
                if (!contentType.contains(route.contentType)) {
                    continue;
                }
            }
            if (route.headers.size() > 0) {
                boolean contains = true;
                for (String key : route.headers.keySet()) {
                    String inputHeader = input.getHeader(key);
                    if (inputHeader == null || !inputHeader.contains(route.headers.get(key))) {
                        contains = false;
                        break;
                    }
                }
                if (!contains) {
                    continue;
                }
            }
            String path = input.getRequestURI();
            if (route.path != null && path != null) {
                if (pathMatch(route, path)) {
                    return route;
                }
            }
        }
        return null;
    }

    /* Example values
    * routePath - /book/:isbn/author/:name
    * inputPath - /book/12345/author/steve
     */
    private boolean pathMatch(AppRoute route, String inputPath) {
        LOG.info("matching route [{}] against input [{}]", route.path, inputPath);
        Map<String, String> params = route.pathParams;
        String paramRegex = "(:.*?)/";
        Pattern paramPattern = Pattern.compile(paramRegex);
        Matcher paramMatcher = paramPattern.matcher(route.path);

        String routeRegex = route.path.replaceAll(paramRegex, "(.*?)/");
        Pattern routePattern = Pattern.compile("^" + routeRegex + "$");
        Matcher routeMatcher = routePattern.matcher(inputPath);

        LOG.info("matching pattern [{}] against input [{}]", routePattern.pattern(), inputPath);
        if (routeMatcher.matches()) {
            int index = 1;
            while (paramMatcher.find()) {
                String param = paramMatcher.group(1);
                String value = routeMatcher.group(index++);
                params.put(param, value);
            }
            return true;
        }
        return false;
    }
}
