package com.jarredweb.jettsey.view;

import java.io.IOException;
import java.util.Map;
import org.eclipse.jetty.server.Response;
import org.jtwig.JtwigTemplate;
import org.jtwig.resource.reference.ResourceReference;

public interface ViewProcessor {
    
    void write(Response response, JtwigTemplate template, String view, String contentType, Map<String, Object> model) throws IOException;
    
    JtwigTemplate resolve(String templatePath, ResourceReference where) throws Exception;
}
