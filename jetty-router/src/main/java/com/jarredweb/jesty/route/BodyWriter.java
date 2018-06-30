package com.jarredweb.jesty.route;

public interface BodyWriter<T> {
    
    byte[] transform(T object);
}
