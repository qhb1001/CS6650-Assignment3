package handler;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import consumer.Consumer;
import util.SkierServletPostRequest;

import java.io.IOException;

public class PostRequestHandler implements Runnable {
    private static int basicQos = Integer.parseInt(System.getProperty("basicQos"));
    private static String exchangeName = "post";
    private Connection connection;
    private String queueName;
    private Consumer consumer;

    public PostRequestHandler(Connection connection, String queueName, Consumer consumer) {
        this.connection = connection;
        this.queueName = queueName;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        try {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, "fanout");
            channel.basicQos(basicQos);
            channel.queueBind(queueName, exchangeName, "");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                SkierServletPostRequest request = new Gson().fromJson(message, SkierServletPostRequest.class);
                consumer.consume(request);
//                System.out.println(" [x] Received '" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
