/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipientList;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import config.RabbitConnection;
import config.RoutingKeys;
import java.io.IOException;

/**
 *
 * @author nikolai
 */
public class Receive {
    public static void main(String[] args) throws IOException {
        final String exchangeName = "TeamFirebug";
        
        RabbitConnection rabbitConnection = new RabbitConnection();
        
        Channel channel = rabbitConnection.makeConnection();
        channel.exchangeDeclare(exchangeName, "direct");
        
        String queueName = channel.queueDeclare().getQueue();
        
        //routingKey = RoutingKeys.RecipientListInput
        channel.queueBind(queueName, exchangeName, "testRoutingKey");
         
        //get banks from queue. "Get banks" component
        QueueingConsumer consumer = new QueueingConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                //send to Aggregator first
                System.out.println("Msg: "+ message);
              
                
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}
