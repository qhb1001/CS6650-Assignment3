package rabbitmq;

import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQDriver {
    private static String ipAddress = System.getProperty("rabbitmq_ip_address");
    private static String userName = System.getProperty("rabbitmq_user_name");
    private static String passwd = System.getProperty("rabbitmq_passwd");
    private static String vhost = System.getProperty("rabbitmq_vhost");

    public static ConnectionFactory getConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(ipAddress);
        factory.setUsername(userName);
        factory.setPassword(passwd);
        factory.setVirtualHost(vhost);
        factory.setPort(5672);

        return factory;
    }
}
