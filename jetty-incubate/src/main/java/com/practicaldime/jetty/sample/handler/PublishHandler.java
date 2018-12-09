package com.practicaldime.jetty.sample.handler;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeoutException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishHandler extends AbstractHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PublishHandler.class);
    private final static String QUEUE_NAME = "hello";
    private final ConnectionFactory factory;

    public PublishHandler() {
        super();
        factory = new ConnectionFactory();
        factory.setHost("localhost");
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {

        String message = null;
        if (request.getMethod().equalsIgnoreCase("POST")) {
            message = request.getParameter("message");
            try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println(" [x] Sent '" + message + "'");
            } catch (IOException | TimeoutException ex) {
                LOG.error("Error while publishing mq message", ex);
            }
        }

        //prepare response
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();

        StringBuilder form = new StringBuilder("<form action='/message/' method='post'>");
        form.append("<textarea name='message' rows='5' cols='20'></textarea><br/>");
        form.append("<input type='submit' value='Send Message'/><br/>");
        form.append("</form><br/>");
        form.append("<h1>").append(message != null ? message : "").append("</h1>");

        out.println(form.toString());

        baseRequest.setHandled(true);
    }
}
