/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.google.gson.Gson;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import entity.Bank;
import entity.RequestLoan;
import java.util.ArrayList;

public class EmitLogDirect {

  private static final String EXCHANGE_NAME = "direct_logs";

  public static void main(String[] argv) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, "direct");

    String severity = "routingKeyTest";
    String message = toJson();
    
    channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + severity + "':'" + message + "'");

    channel.close();
    connection.close();
  }
  
  private static String toJson(){
   Gson g = new Gson();
   
   //first make a requestLoan object
        ArrayList<Bank> bankNew = new ArrayList<>();
        bankNew.add(new Bank("123", "denne test", 876, "testQueue"));
        bankNew.add(new Bank("986", "test test", 123, "testQueue222"));
        RequestLoan loan = new RequestLoan(111, "234567", 100.0, "31-10-2016", bankNew);
        
        //convert to json string
        String json = g.toJson(loan);
        return json;
  }

}
