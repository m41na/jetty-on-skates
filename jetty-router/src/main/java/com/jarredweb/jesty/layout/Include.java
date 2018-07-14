package com.jarredweb.jesty.layout;

import java.util.LinkedHashMap;
import java.util.Map;

public class Include {
    
    public String markup;
    public String[] hscripts;
    public String[] bscripts;
    public Map<String, Object> model;
    public Map<String, Include> includes = new LinkedHashMap<>();
}
