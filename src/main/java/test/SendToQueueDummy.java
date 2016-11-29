/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import config.RabbitConnection;
import config.RoutingKeys;
import entity.Message;
import java.io.IOException;

/**
 *
 * @author nikolai
 */
public class SendToQueueDummy {
    public static void main(String[] args) {
        Gson gson = new Gson();
        String msg = gson.toJson(new Message("111222333", 120, 30.2, 22));
        RabbitConnection rabbitConnection = new RabbitConnection();
        Channel channel = rabbitConnection.makeConnection();
        try {
            channel.queueDeclare(RoutingKeys.RecipientListInput, false, false, false, null);
            channel.basicPublish("", RoutingKeys.RecipientListInput, null, msg.getBytes("UTF-8"));
            rabbitConnection.closeChannelAndConnection();
            System.out.println(" [x] Sent :" + msg + "");
        } catch (IOException ex) {
            System.out.println("Error in RecipientList class - sendToTranslator()");
            System.out.println(ex.getStackTrace());
        }

    }
}
