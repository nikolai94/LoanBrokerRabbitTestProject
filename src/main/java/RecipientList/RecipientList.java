package RecipientList;

import com.google.gson.Gson;
import entity.Bank;

import com.rabbitmq.client.*;
import java.io.IOException;

import com.rabbitmq.client.*;
import dto.RequestLoanDto;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author nikolai
 */
public class RecipientList {

    //Rabbit mq make function send to all banks to their channel (direct)
    public static void main(String[] args) throws Exception {
        //rabbit connect
        String exchangeName = "Banks";
        Channel channel = null;
        channel = connectToChannel("localhost", "", "", exchangeName);
        channel.exchangeDeclare(exchangeName, "direct");

        String queueName = channel.queueDeclare().getQueue();
        //banks = routingKey
        channel.queueBind(queueName, exchangeName, "banksQueue");

        //get banks from queue. "Get banks" component
        QueueingConsumer consumer = new QueueingConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                RequestLoanDto requestDto = getDtoFromJson(message);
                if (requestDto.getBanks() != null) {
                    for (Bank bank : requestDto.getBanksFromRealFormat()) {
                        //remember that the component "Get banks" must choose which banks we need to send to(according to credit score)
                        //send to translator queue here, make send method
                        sendToTranslator();
                    }
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);

    }

    //Make a connection to channel.
    //Returns a Channel. Returns null if error
    private static Channel connectToChannel(String host, String username, String cph, String exchangeName) {
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setUsername(username);
            factory.setPassword(cph);
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException | TimeoutException exc) {
            System.out.println("Error in RecipientList class - connectToChannel()");
            System.out.println(exc.getStackTrace());
            return null;
        }

        return channel;
    }

    private static RequestLoanDto getDtoFromJson(String json) {
        Gson g = new Gson();
        return g.fromJson(json, RequestLoanDto.class);
    }

    private static void sendToTranslator() {
        //connectToChannel();
    }

}
