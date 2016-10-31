/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.loanbrokerrabbittest;

import com.google.gson.Gson;
import dto.RequestLoanDto;
import entity.Bank;
import entity.RequestLoan;
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
        
        /*String json = "[{\"id\": \"113\", \"name\": \"testName\", \"minCreditScore\": \"1\", \"queue\": \"testQueue1\"},"
                + "{\"id\": \"445\", \"name\": \"testName11\", \"minCreditScore\": \"124\", \"queue\": \"testQueue2\"}]";
        
        Bank[] b = g.fromJson(json, Bank[].class);
        ArrayList<Bank> bankList = new ArrayList<Bank>(Arrays.asList(b));
        System.out.println("size: "+bankList.size());
        for (Bank bank : bankList) {
            System.out.println("Name: "+bank.getName());
        }*/
        
        //-----------------------------------------------------To Json AND back from Json--------------------------------------------------------
        
        
        /*String json = "{\"creditScore\":\"100\", \"SNN\":\"9875\", \"amount\":\"10.10\", \"toDate\":\"31-10-2016\", "
        + "\"banks\":[{\"id\":\"20\", \"name\":\"test\", \"minCreditScore\":\"110\", \"queue\":\"test\"}, "
                + "{\"id\":\"201\", \"name\":\"test11\", \"minCreditScore\":\"789\", \"queue\":\"testabc\"}] }";*/
        
        //first make a requestLoan object
        ArrayList<Bank> bankNew = new ArrayList<>();
        bankNew.add(new Bank("123", "denne test", 876, "testQueue"));
        bankNew.add(new Bank("986", "test test", 123, "testQueue222"));
        RequestLoan loan = new RequestLoan(111, "234567", 100.0, "31-10-2016", bankNew);
        
        //convert to json string
        String json = g.toJson(loan);
        System.out.println(json);
        
        //decode from json back to RequestLoan DTO
        RequestLoanDto rDto = g.fromJson(json, RequestLoanDto.class);
        
        ArrayList<Bank> bank = rDto.getBanksFromRealFormat();
        System.out.println("Size: "+rDto.getBanks().size());
        
        
        for (Bank bankRow : bank) {
            System.out.println("Name: "+ bankRow.getName());
        }
        
        
        
        
        
        
    }
    
    
    
    //Get a arraylist of banks from a json string array. Json array could look like e.g. 
    //"[{\"id\": \"113\", \"name\": \"testName\", \"minCreditScore\": \"1\", \"queue\": \"testQueue1\"},""{\"id\": \"445\", \"name\": \"testName11\", \"minCreditScore\": \"124\", \"queue\": \"testQueue2\"}]"
    /*private static ArrayList<Bank> getBanksFromJsonToArray(String banksInJson) {
        Gson g = new Gson();
        Bank[] b = g.fromJson(banksInJson, Bank[].class);
        //cast to arrayList
        ArrayList<Bank> bankList = new ArrayList<Bank>(Arrays.asList(b));
        return bankList;
    }*/

}
