/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import config.RabbitConnection;
import config.RoutingKeys;
import entity.Bank;
import entity.Message;
import java.io.IOException;

/**
 *
 * @author nikolai
 */
public class ReceiveQueueDummy {

    public static void main(String[] args) throws IOException {
        RabbitConnection rabbitConnection = new RabbitConnection();

        Channel channel = rabbitConnection.makeConnection();
        channel.queueDeclare("FromRecipientToAggregator", false, false, false, null);
        System.out.println("Wait...");
        QueueingConsumer consumer = new QueueingConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("message : " + message);
                System.out.println("CorrelationId: "+ properties.getCorrelationId());
            }
        };
        channel.basicConsume("FromRecipientToAggregator", true, consumer);
    }
}
