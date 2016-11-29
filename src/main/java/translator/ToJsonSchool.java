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
import config.RoutingKeys;
import entity.Bank;
import entity.Message;
import entity.RequestLoan;
import java.io.IOException;
import testBanks.DtoJsonBank;

/**
 *
 * @author nikolai
 */
public class ToJsonSchool {

    //use replyQueueName as ' BasicProperties props' for the school rabbitmq  https://www.rabbitmq.com/tutorials/tutorial-six-java.html
    public static void main(String[] args) throws Exception {
        final String replyQueueName = "replyFromBanks";
        final String EXCHANGE_NAME = "cphbusiness.bankJSON";
        String queueName = "translatorJsonSchool";
        
        RabbitConnection rabbitConnection = new RabbitConnection();

        //Channel channel = connectToChannel("localhost", "", "");
        Channel channel = rabbitConnection.makeConnection();
        channel.queueDeclare(queueName, false, false, false, null);

        //get banks from queue. "Get banks" component
        QueueingConsumer consumer = new QueueingConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                Message messageFromJson = getFromJson(message);
                sendMsgToBank(messageFromJson, properties.getCorrelationId(), EXCHANGE_NAME, replyQueueName);
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

     private static Message getFromJson(String json) {
        Gson g = new Gson();
        return g.fromJson(json, Message.class);
    }


    private static void sendMsgToBank(Message msg, String corrId, String exchangeName, String replyQueueName){
        Gson gson = new Gson();
        RabbitConnection rabbitConnection = new RabbitConnection();
        Channel channel = rabbitConnection.makeConnection();
        try {
            channel.exchangeDeclare(exchangeName, "fanout");
            
            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .correlationId("cphbusiness.bankJSON#"+corrId)
                    .replyTo(replyQueueName)
                    .build();
            

            //String message = gson.toJson(new DtoJsonBank(1605789787, 598, 10.0, 360));
            String message = gson.toJson(new DtoJsonBank(msg.getSsn(), msg.getCreditScore(), msg.getLoanAmount(), msg.getLoanDuration()));
            channel.basicPublish(exchangeName, "", props, message.getBytes());
            rabbitConnection.closeChannelAndConnection();
            System.out.println(" [x] Sent :" + msg.toString() + "");
        } catch (IOException ex) {
            System.out.println("Error in RecipientList class - sendToTranslator()");
            System.out.println(ex.getStackTrace());
        }
    }
}
