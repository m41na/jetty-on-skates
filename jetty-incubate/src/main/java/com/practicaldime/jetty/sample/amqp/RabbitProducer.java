package com.practicaldime.jetty.sample.amqp;

import com.practicaldime.zesty.tasklet.AbstractTasklet;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RabbitProducer extends AbstractTasklet {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitProducer.class);
    private final static String QUEUE_NAME = "hello";
    private ConnectionFactory factory;

    public void sendMessage(String message) {
        LOG.debug("message to send out -> " + message);
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            LOG.info(" [x] Sent '" + message + "'");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public void deploy(Future<?> future) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type message to send here... ");
        String input = scanner.nextLine();
        while (!input.equals("exit")) {
            sendMessage(input);

            System.out.println("Type next message... ");
            input = scanner.nextLine();
        }
        System.out.println("Exiting producer. Bye");
    }

    @Override
    public void init() {
        if (factory == null) {
            factory = new ConnectionFactory();
            factory.setHost("localhost");
        }
    }

    @Override
    public void destroy() {
        factory = null;
    }
}
