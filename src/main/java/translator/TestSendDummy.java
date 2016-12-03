/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package translator;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import config.RabbitConnection;
import config.RoutingKeys;
import entity.Bank;
import entity.Message;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author nikolai
 */
public class TestSendDummy {
     public static void main(String[] args) throws IOException {
        final String corrId = java.util.UUID.randomUUID().toString();
        final String exchangeName = "TeamFirebug";
        
        //test data
        Gson g = new Gson();
        //Message testMsg = new Message("1605789787", 598, 10.0, "360");
        Message testMsg = new Message("1605789787", 598, 10.0, "1973-01-01 01:00:00.0 CET");
        ArrayList<Bank> banks = new ArrayList<Bank>();
        banks.add(new Bank("aa", "testname", 1, "testKeyHere"));
        testMsg.setBanks(banks);
        String msg = g.toJson(testMsg);
        
        
        RabbitConnection rabbitConnection = new RabbitConnection();
        Channel channel = rabbitConnection.makeConnection();
        try {
            //set correlationId for Aggregator
            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .correlationId(corrId)
                    .build();

            channel.exchangeDeclare(exchangeName, "direct");
            //channel.basicPublish(exchangeName, "keyBankJSON", null, msg.getBytes("UTF-8"));
            channel.basicPublish(exchangeName, "keyBankXML", null, msg.getBytes("UTF-8"));
            rabbitConnection.closeChannelAndConnection();
            System.out.println(" [x] Sent :" + msg + "");
        } catch (IOException ex) {
            System.out.println("Error in RecipientList class - sendToTranslator()");
            System.out.println(ex.getStackTrace());
        }
    }
}
