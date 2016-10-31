/*
 * This DTO is for when we want to convert from json. Gson.fromJson
 */
package dto;

import entity.Bank;
import java.util.ArrayList;

/**
 *
 * @author nikolai
 */
public class RequestLoanDto {

    private int creditScore;
    private String SNN;
    private double amount;
    //how long before you pay
    private String toDate;
    private ArrayList<Bank> banks = new ArrayList<>();

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public String getSNN() {
        return SNN;
    }

    public void setSNN(String SNN) {
        this.SNN = SNN;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public ArrayList<Bank> getBanks() {
        return banks;
    }

    public void setBanks(String id, String name, int minCreditScore, String queue) {
        banks.add(new Bank(id, name, minCreditScore, queue));
    }

    //convert the static bank class, to a the right entity.Bank class
    public ArrayList<entity.Bank> getBanksFromRealFormat() {
        //if there is any banks
        if (banks != null && banks.size() != 0) {
            ArrayList<entity.Bank> banksReal = new ArrayList<>();
            for (Bank bankRow : banks) {
                banksReal.add(new entity.Bank(bankRow.id, bankRow.name, bankRow.minCreditScore, bankRow.queue));
            }
            return banksReal;
        }
        return null;
    }

    //for the from json convert
    public static class Bank {

        private String id;
        private String name;
        //minimum credit score
        private int minCreditScore;
        private String queue;

        public Bank(String id, String name, int minCreditScore, String queue) {
            this.id = id;
            this.name = name;
            this.minCreditScore = minCreditScore;
            this.queue = queue;
        }
    }

}
