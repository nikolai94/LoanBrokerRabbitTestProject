/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.loanbrokerrabbittest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 *
 * @author nikolai
 */
public class Test {
    public static void main(String[] args) {
       //String testJson = "{\"jsonTest\": \"this value\"}";
       String testJson = "{\"jsonTest\": [{\"thisKey\" :\"this value\"}], \"jsonRoot2\" : {\"thisKey2\" : \"this vaule 22\"}, \"departure\": \"tesst\" }";
        System.out.println(testJson);
       Gson gson = new Gson();
       //gson.
       JsonObject jsonObject = gson.fromJson( testJson, JsonObject.class);
       System.out.println(jsonObject.get("departure"));
    }
}
