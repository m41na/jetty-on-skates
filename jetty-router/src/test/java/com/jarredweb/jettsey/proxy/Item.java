package com.jarredweb.jettsey.proxy;

public interface Item {
    
    void setId(Long id);
    
    Long getId();
    
    void setName(String name);
    
    String getName();
    
    void setValid(boolean valid);
    
    boolean isValid();
}
