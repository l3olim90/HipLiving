package com.example.pathree2022.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Furniture {
    private String furnitureID;
    private String name;
    private String category;
    private double cost;

    private boolean isOrdered;
    private Date orderDate;
    private String customerID;
    private char warrantyLevel; //L = 1 year warranty, M = 5 year warranty, H = 10 year warranty, N = None
    //L only for >=$100; M only for >=$250; H only for >=$500

    public Furniture(String furnitureID, String name, String category, double cost){
        this.furnitureID = furnitureID;
        this.name = name;
        this.category = category;
        this.cost = cost;
        this.isOrdered = false;
        this.orderDate = null;
        this.customerID = "000Z";
        this.warrantyLevel = 'N';
    }

    public Furniture(String furnitureID, String name, String category, double cost, String orderDate, String customerID,
                     char warrantyLevel) throws ParseException {
        this(furnitureID, name, category, cost);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (!checkWarrantyLevel(warrantyLevel, this.cost))
            throw new IllegalArgumentException("Invalid warranty level.");
        else if (!Customer.checkCustomerID(customerID))
            throw new IllegalArgumentException("Invalid Customer ID.");
        else {
            this.isOrdered = true;
            this.orderDate = format.parse(orderDate);
            this.customerID = customerID;
            this.warrantyLevel = warrantyLevel;
        }
    }

    public Furniture(Furniture f){
        this.furnitureID = f.furnitureID;
        this.name = f.name;
        this.category = f.category;
        this.cost = f.cost;
        this.isOrdered = f.isOrdered;
        this.orderDate = f.orderDate;
        this.customerID = f.customerID;
        this.warrantyLevel = f.warrantyLevel;
    }

    public static boolean checkWarrantyLevel(char warrantyLevel, double cost){
        if (!(warrantyLevel == 'L' || warrantyLevel == 'M' || warrantyLevel == 'H' || warrantyLevel == 'N'))
            return false;
        if (warrantyLevel == 'L' && cost < 100)
            return false;
        if (warrantyLevel == 'M' && cost < 250)
            return false;
        if (warrantyLevel == 'H' && cost < 500)
            return false;
        return true;
    }

    public String getFurnitureID() {  return furnitureID;  }
    public String getName() { return name;  }
    public String getCategory() { return category;  }
    public double getCost() { return cost;  }

    public boolean isOrdered() {  return isOrdered; }
    public void setOrdered(boolean ordered) {  isOrdered = ordered; }

    public String getOrderDate() {
        if (orderDate == null) return null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(orderDate);
    }
    public void setOrderDate(Date orderDate) {  this.orderDate = orderDate; }

    public String getCustomerID() { return customerID;  }
    public void setCustomerID(String customerID) {  this.customerID = customerID; }

    public char getWarrantyLevel() {  return warrantyLevel; }
    public void setWarrantyLevel(char warrantyLevel) {
        if (!checkWarrantyLevel(warrantyLevel, this.cost))
            throw new IllegalArgumentException("Invalid warranty level.");
        this.warrantyLevel = warrantyLevel;
    }

    @Override
    public String toString(){
        String warrantyStr = "";
        if (warrantyLevel == 'L') warrantyStr += "1 year warranty";
        else if (warrantyLevel == 'M') warrantyStr += "5 year warranty";
        else if (warrantyLevel == 'H') warrantyStr += "10 year warranty";
        else warrantyStr += "No warranty";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String avail;
        if (isOrdered) {
            Calendar c = Calendar.getInstance();
            c.setTime(orderDate);
            c.add(Calendar.DAY_OF_MONTH, 3);
            avail = "Ordered by " + customerID + " (" + warrantyStr + "), deliver by " + format.format(c.getTime());
        }
        else
            avail = "Available, Cost = $" + cost;
        return "(" + furnitureID + ") "+ name + "; " + avail;
    }
}
