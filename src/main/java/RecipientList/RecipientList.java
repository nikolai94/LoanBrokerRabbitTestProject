/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecipientList;

import com.google.gson.Gson;
import entity.Bank;
import java.util.ArrayList;

import com.rabbitmq.client.*;
import java.io.IOException;

import com.rabbitmq.client.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author nikolai
 */
public class RecipientList {
    //To do's

    //Make a bank class
    //arraylist of all banks, only as an eksempel. remember that "Get banks" component make the banks and send the banks to you
    //Make function find banks for this customer. The bank class would have an attribut call 'overThisPoints', which will assess what banks we need to send a requst to
    //Rabbitmq receive message from 'getBanks' program.
    //Rabbit mq make function send to all banks to their channel (direct)
    public static void main(String[] args) throws Exception {
        String exchangeName = "Banks";
        Channel channel = null;
        try {
            channel = connectToChannel("localhost", "", "", exchangeName);
        } catch (IOException | TimeoutException exc) {
            System.out.println(exc.getStackTrace());
        }

        String queueName = channel.queueDeclare().getQueue();
        //banks = routingKey
        channel.queueBind(queueName, exchangeName, "banksQueue");
        //get banks from queue. "Get banks" component
        QueueingConsumer consumer = new QueueingConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                ArrayList<Bank> banks = getBanksFromJsonToArray(message);
                
                //send to translator queue
                for (Bank bank : banks) {
                    
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);

    }
    
    //Make a connection to channel.
    //Returns a Channel
    private static Channel connectToChannel(String host, String username, String cph, String exchangeName) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(username);
        factory.setPassword(cph);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName, "direct");
        return channel;
    }
    
    
    //Get a arraylist of banks from a json string array. Json array could look like e.g. 
    //"[{\"id\": \"113\", \"name\": \"testName\", \"minCreditScore\": \"1\", \"queue\": \"testQueue1\"},""{\"id\": \"445\", \"name\": \"testName11\", \"minCreditScore\": \"124\", \"queue\": \"testQueue2\"}]"
    private static ArrayList<Bank> getBanksFromJsonToArray(String banksInJson) {
        Gson g = new Gson();
        Bank[] b = g.fromJson(banksInJson, Bank[].class);
        //cast to arrayList
        ArrayList<Bank> bankList = new ArrayList<Bank>(Arrays.asList(b));
        return bankList;
    }

}
