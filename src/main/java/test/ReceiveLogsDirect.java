/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.rabbitmq.client.*;

import java.io.IOException;

public class ReceiveLogsDirect {

  private static final String EXCHANGE_NAME = "direct_logs";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    
    channel.exchangeDeclare(EXCHANGE_NAME, "direct");
    String queueName = channel.queueDeclare().getQueue();
    
    if (argv.length == 0){
      System.err.println("Usage: ReceiveLogsDirect [info] [warning] [error]");
      channel.queueBind(queueName, EXCHANGE_NAME, "info");
    }
    
    /*for(String severity : argv){
      channel.queueBind(queueName, EXCHANGE_NAME, severity);
    }*/
    channel.queueBind(queueName, EXCHANGE_NAME, "routingKeyTest");
    
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
     QueueingConsumer consumer = new QueueingConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
                                 AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
      }
    };
    channel.basicConsume(queueName, true, consumer);
  }
}
