package com.jarredweb.jesty.view;

public class ViewException extends RuntimeException{

    public ViewException(String message) {
        super(message);
    }

    public ViewException(Throwable cause) {
        super(cause);
    }

    public ViewException(String message, Throwable cause) {
        super(message, cause);
    }    
}
