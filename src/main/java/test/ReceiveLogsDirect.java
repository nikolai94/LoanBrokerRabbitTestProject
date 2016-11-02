/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.rabbitmq.client.*;
import config.RabbitConnection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiveLogsDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        RabbitConnection rabbitConnection = new RabbitConnection();
        
        final Channel channel = rabbitConnection.makeConnection();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, "routingKeyTest");
        //final Channel channelSendMsg = channel;
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        QueueingConsumer consumer = new QueueingConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                    AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
                sendToTranslator(channel, "testKeyReceiveDummy", "exchangeeceiveDummy", message);
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    private static void sendToTranslator(Channel channel,String routingKey, String exchangeName, String msg) {

        try {
            channel.exchangeDeclare(exchangeName, "direct");
            
            channel.basicPublish(exchangeName, routingKey, null, msg.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + routingKey + "':'" + msg + "'");

        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }

    }

}
