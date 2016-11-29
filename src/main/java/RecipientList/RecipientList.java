package RecipientList;

import com.google.gson.Gson;
import entity.Bank;

import com.rabbitmq.client.*;
import java.io.IOException;

import com.rabbitmq.client.*;
import config.*;
import entity.Message;
import java.util.concurrent.TimeoutException;

/*TO DO!!!!
-Remember to make a string in class RoutingKeys to the queue between RecipientList and Aggregator and replace the hardcode string
-Make a list of banks queue names
*/

/**
 *
 * @author nikolai
 */
public class RecipientList {

    //Rabbit mq make function send to all banks to their channel (direct)
    public static void main(String[] args) throws Exception {
        //make correlationId for Aggregator
        final String corrId = java.util.UUID.randomUUID().toString();
        
        RabbitConnection rabbitConnection = new RabbitConnection();
        
        Channel channel = rabbitConnection.makeConnection();
        channel.queueDeclare(RoutingKeys.RecipientListInput, false, false, false, null);
        
        //get banks from queue. "Get banks" component
        QueueingConsumer consumer = new QueueingConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                //send to Aggregator first
                sendMessage("FromRecipientToAggregator",message, corrId);
                
                //send to translator
                Message request = getFromJson(message);
                if (request.getBanks() != null) {
                    for (Bank bank : request.getBanks()) {
                        sendMessage(bank.getRoutingKey(), message, corrId);
                        //remember that the component "Get banks" must choose which banks we need to send to(according to credit score)
                        //send to translator queue here, make send method
                        //sendMessage(channel, bank.getRoutingKey(), "Banks", message);
                    }
                }
            }
        };
        channel.basicConsume(RoutingKeys.RecipientListInput, true, consumer);

    }

    private static Message getFromJson(String json) {
        Gson g = new Gson();
        return g.fromJson(json, Message.class);
    }

    //can be used for sending message to Translator and Aggregator
    private static void sendMessage(String queueName, String msg, String corrId) {
        RabbitConnection rabbitConnection = new RabbitConnection();
        Channel channel = rabbitConnection.makeConnection();
        try {
            //set correlationId for Aggregator
            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .correlationId(corrId)
                    .build();

            
            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicPublish("", queueName, props, msg.getBytes("UTF-8"));
            rabbitConnection.closeChannelAndConnection();
            System.out.println(" [x] Sent :" + msg + "");
        } catch (IOException ex) {
            System.out.println("Error in RecipientList class - sendToTranslator()");
            System.out.println(ex.getStackTrace());
        }

    }
    

}