package com.jarredweb.jesty.route;

public interface BodyReader<T> {
    
    T transform(String type, byte[] bytes);
}
