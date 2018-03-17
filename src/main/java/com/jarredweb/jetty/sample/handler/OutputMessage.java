package com.jarredweb.jetty.sample.handler;

public class OutputMessage {
    
    public String from;
    public String text;
    public String time;

    public OutputMessage(String from, String text, String time) {
        this.from = from;
        this.text = text;
        this.time = time;
    }
}
