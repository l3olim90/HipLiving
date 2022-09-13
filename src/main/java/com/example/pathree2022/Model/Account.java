package com.example.pathree2022.Model;

public class Account {
    private String loginID;
    private String password;
    private String customerID;
    private double points;

    public Account(String loginID, String password, String customerID, double points){
        if (!Customer.checkCustomerID(customerID))
            throw new IllegalArgumentException("Unable to create Account. Invalid Customer ID.");
        else {
            this.loginID = loginID;
            this.password = password;
            this.customerID = customerID;
            this.points = points;
        }
    }

    public String getLoginID() {
        return loginID;
    }
    public String getPassword() {
        return password;
    }
    public String getCustomerID() {
        return customerID;
    }
    public double getPoints() {
        return points;
    }

    // Maximum 999 points, Minimum 0 points
    // Free Delivery: Deduct 50 points
    // Every purchase: Add 0.1*Amount paid
    public String addPoints(double amt){
        if (points + amt >= 999) {
            points = 999;
            return "You have reached the maximum amount. Topped up to only 999";
        }
        else {
            points += amt;
            return String.format("%.2f", amt) + " was added to account";
        }
    }
    public String deductPoints(double amt){
        if (points - amt <= 0) {
            return "Insufficient points available. You only have " + amt + " points left.";
        }
        else {
            points -= amt;
            return String.format("%.2f", amt) + " was deducted from account";
        }
    }

    // Formula:
    /*
     * 20 to 40 points: Flat rate 5% discount
     * >40 to 70 points: 5 + 0.4*remaining
     * >70 to 100 points: 17 + 0.2*remaining
     */
    // Max points can be redeemed per purchase: 100 points
    // Min points can be redeemed per purchase: 20 points
    public static double calcPercentDiscount(double points){
        if (points > 100)
            return 23;
        else if (points > 70)
            return 17 + 0.2*(points - 70);
        else if (points > 40)
            return 5 + 0.4*(points - 40);
        else if (points >= 20)
            return 5;
        else
            return 0;
    }


}

