
package entity;

import java.util.ArrayList;

/**
 *
 * @author nikolai
 */
public class RequestLoan {
    private int creditScore;
    private String SNN;
    private double amount;
    //how long before you pay
    private String toDate;
    private ArrayList<Bank> banks;

    public RequestLoan() {
    }
    
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

    public void setBanks(ArrayList<Bank> banks) {
        this.banks = banks;
    }
    
    
    
}
