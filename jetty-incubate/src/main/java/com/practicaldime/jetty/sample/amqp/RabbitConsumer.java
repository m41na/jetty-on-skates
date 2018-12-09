package com.practicaldime.jetty.sample.amqp;

import com.practicaldime.zesty.tasklet.AbstractTasklet;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RabbitConsumer extends AbstractTasklet {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitConsumer.class);
    private final static String QUEUE_NAME = "hello";
    private ConnectionFactory factory;
    private Connection connection;

    public void handleMessage(String message) {
        System.out.println(" [x] Received '" + message + "'");
    }

    @Override
    public void deploy(Future<?> future) {
        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    handleMessage(message);
                }
            };
            channel.basicConsume(QUEUE_NAME, true, consumer);

        } catch (IOException | TimeoutException ex) {
            LOG.error("Error while consuming amqp messages", ex);
        }
    }

    @Override
    public void init() {
        if (factory == null) {
            try {
                factory = new ConnectionFactory();
                factory.setHost("localhost");
                connection = factory.newConnection();
            } catch (IOException | TimeoutException ex) {
                LOG.error("Error while starting message consumer", ex);
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println("destroying consumer");
        try {
            connection.close();
            factory = null;
        } catch (IOException ex) {
            LOG.error("Error while stopping message consumer", ex);
        }
    }
}
