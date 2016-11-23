package entity;

/**
 *
 * @author nikolai
 */
public class Bank {
    
   private String id;
   private String name;
   //minimum credit score
   private int minCreditScore;
   private String routingKey;

    public Bank(String id, String name, int minCreditScore, String channelName) {
        this.id = id;
        this.name = name;
        this.minCreditScore = minCreditScore;
        this.routingKey = channelName;
    }

   
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMinCreditScore() {
        return minCreditScore;
    }

    public void setMinCreditScore(int minCreditScore) {
        this.minCreditScore = minCreditScore;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
