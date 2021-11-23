import com.rabbitmq.client.Channel;
import consumer.Consumer;
import consumer.ResortConsumer;
import consumer.SkierConsumer;
import handler.PostRequestHandler;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;


public class main {
    private static String ipAddress = System.getProperty("rabbitmq_ip_address");
    private static String userName = System.getProperty("rabbitmq_user_name");
    private static String passwd = System.getProperty("rabbitmq_passwd");
    private static String vhost = System.getProperty("rabbitmq_vhost");
    private static String exchangeName = System.getProperty("exchangeName");
    private static String consumerType = System.getProperty("consumerType");
    private static int NUMBER = Integer.parseInt(System.getProperty("threadNumber"));

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(ipAddress);
        factory.setUsername(userName);
        factory.setPassword(passwd);
        factory.setVirtualHost(vhost);

        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, exchangeName, "");
        Consumer consumer;
        if (consumerType.equals("skier")) {
            consumer = new SkierConsumer();
        } else {
            consumer = new ResortConsumer();
        }

        for (int i = 0; i < NUMBER; i++) {
            Thread t = new Thread(new PostRequestHandler(connection, queueName, consumer));
            t.start();
        }

        CountDownLatch countDownLatch = new CountDownLatch(3);
        countDownLatch.await();
    }

}
