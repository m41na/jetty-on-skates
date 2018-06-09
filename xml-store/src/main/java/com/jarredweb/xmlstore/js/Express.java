package com.jarredweb.xmlstore.js;

public class Express {
    
    public Express(){
        System.out.println("creating new Express");
    }
    
    public <T,R>void listen(int port, Function func) {
        func.action();
    }
}
