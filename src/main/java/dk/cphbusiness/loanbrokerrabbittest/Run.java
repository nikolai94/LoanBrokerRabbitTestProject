/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.loanbrokerrabbittest;

import com.google.gson.Gson;
import dto.RequestLoanDto;
import entity.Bank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author nikolai
 */
public class Run {

    public static void main(String[] args) {
        Gson g = new Gson();
        String json = "{\"creditScore\":\"100\", \"SNN\":\"9875\", \"amount\":\"10.10\", \"toDate\":\"31-10-2016\", "
        + "\"banks\":[{\"id\":\"20\", \"name\":\"test\", \"minCreditScore\":\"110\", \"queue\":\"test\"}, "
                + "{\"id\":\"201\", \"name\":\"test11\", \"minCreditScore\":\"789\", \"queue\":\"testabc\"}] }";
        
        /*String json = "[{\"id\": \"113\", \"name\": \"testName\", \"minCreditScore\": \"1\", \"queue\": \"testQueue1\"},"
                + "{\"id\": \"445\", \"name\": \"testName11\", \"minCreditScore\": \"124\", \"queue\": \"testQueue2\"}]";
        
        Bank[] b = g.fromJson(json, Bank[].class);
        ArrayList<Bank> bankList = new ArrayList<Bank>(Arrays.asList(b));
        System.out.println("size: "+bankList.size());
        for (Bank bank : bankList) {
            System.out.println("Name: "+bank.getName());
        }*/
        
        RequestLoanDto rDto = g.fromJson(json, RequestLoanDto.class);
        
        ArrayList<Bank> bank = rDto.getBanksFromRealFormat();
        System.out.println("Size: "+rDto.getBanks().size());
        
        for (Bank bankRow : bank) {
            System.out.println("Name: "+ bankRow.getName());
        }
        
        
        
        
        
        
    }

}
