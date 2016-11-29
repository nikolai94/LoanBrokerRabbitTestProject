/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testBanks;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author nikolai
 */
public class GetReply {

    private static final String EXCHANGE_NAME = "cphbusiness.bankJSON";
    private static final String RPC_QUEUE_NAME = "testQueue_reply";

    public static void main(String[] args) {
          try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("datdb.cphbusiness.dk");
            factory.setPort(5672);
            factory.setUsername("student");
            factory.setPassword("cph");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            //String queueName = channel.queueDeclare().getQueue();
            //channel.queueBind(queueName, EXCHANGE_NAME, "");   
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(RPC_QUEUE_NAME, true, consumer);
            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                System.out.println("CorrelationId: "+ delivery.getProperties().getCorrelationId());
                 
                String message = new String(delivery.getBody());
                
                System.out.println(" [x] Received '" + message + "'");
            }

        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
