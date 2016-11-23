/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package translator;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import config.RabbitConnection;
import entity.Bank;
import entity.RequestLoan;
import java.io.IOException;

/**
 *
 * @author nikolai
 */
public class ToJsonSchool {
    
    //use replyQueueName as ' BasicProperties props' for the school rabbitmq  https://www.rabbitmq.com/tutorials/tutorial-six-java.html
    
    public static void main(String[] args) throws Exception {
        final String replyQueueName = "replyFromBanks";
        
        //make handleDelivery from the RecipientList queue
        
        //send to school rabbitmq server
        
         RabbitConnection rabbitConnection = new RabbitConnection();
         
           //rabbit connect
        final String exchangeName = "Banks";
        //Channel channel = connectToChannel("localhost", "", "");
        final Channel channel = rabbitConnection.makeConnection();
        channel.exchangeDeclare(exchangeName, "direct");

        String queueName = channel.queueDeclare().getQueue();
        //banks = routingKey
        channel.queueBind(queueName, exchangeName, "translatorJsonSchool");

        //get banks from queue. "Get banks" component
        QueueingConsumer consumer = new QueueingConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                RequestLoan request = getFromJson(message);
                sendMsgToBank(channel, "ourRabbitBank", exchangeName, message, replyQueueName);
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
    
    private static RequestLoan getFromJson(String json) {
        Gson g = new Gson();
        return g.fromJson(json, RequestLoan.class);
    }
    
    private static void sendMsgToBank(Channel channel, String routingKey, String exchangeName, String msg, String replyQueueName){
        BasicProperties props = new BasicProperties
                                .Builder()
                                //.correlationId(corrId)
                                .replyTo(replyQueueName)
                                .build(); 
        
        try {
            channel.exchangeDeclare(exchangeName, "direct");
            channel.basicPublish(exchangeName, routingKey, props, msg.getBytes("UTF-8"));
            
            System.out.println(" [x] Sent '" + routingKey + "':'" + msg + "'");
        } catch (IOException ex) {
            System.out.println("Error in ToJsonSchool class - sendMsgToBank()");
            System.out.println(ex.getStackTrace());
        }
    } 
}
