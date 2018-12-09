package com.practicaldime.jetty.sample.handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {
    
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage chatHandler(InputMessage message) throws Exception{
        Thread.sleep(1000l); //simulated delay
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.from, message.text, time);
    }
    
    @MessageMapping("/multichat")
    @SendTo("/topic/chatback")
    public OutputMessage multiChatHandler(InputMessage message) throws Exception{
        Thread.sleep(1000l); //simulated delay
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage("app", message.text, time);
    }
}
