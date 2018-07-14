package com.jarredweb.jesty.view;

import java.util.Map;

public interface ViewEngine {
    
    String merge(String template, Map<String, Object> model) throws Exception;
}
