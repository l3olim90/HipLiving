package com.example.pathree2022.Model;

public class Mover {
    private String courierID;
    private String courierName;
    private String courierContact;
    private static int moverCount = 0;
    private Customer customer;
    private Furniture[] moveItems;
    private int numItems;

    public Mover(int numMoveItems){
        this.courierID = "No delivery";
        this.courierName = "Opted for Self-Collection";
        this.courierContact = "NIL";
        this.customer = null;
        this.moveItems = new Furniture[numMoveItems];
        this.numItems = 0;
    }

    public Mover(String courierID, String courierName, String courierContact){
        this.courierID = courierID;
        this.courierName = courierName;
        this.courierContact = courierContact;
        moverCount++;
        this.customer = null;
        this.moveItems = new Furniture[5];
        this.numItems = 0;
    }
    public String getCourierID(){ return courierID; }
    public String getCourierName(){ return courierName; }
    public String getCourierContact(){ return courierContact; }
    public static int getMoverCount() { return moverCount;  }

    public Customer getCustomer(){ return customer;}
    public void setCustomer(Customer c){
        customer = new Customer(c.getCustomerID(), c.getName(), c.getContactNo(), c.getAddress(), c.getPostalCode());
    }

    public Furniture[] getMoveItems(){
        Furniture[] temp = new Furniture[numItems];
        for (int i = 0; i < numItems; i++){
            temp[i] = new Furniture(moveItems[i]);
        }
        return temp;
    }
    public void addMoveItems(Furniture item){
        if (numItems < moveItems.length) {
            moveItems[numItems] = new Furniture(item);
            numItems++;
        } else
            System.out.println("Item cannot be added, please use another mover");
    }
    public Furniture removeLastMoveItem(){
        if (numItems == 0)
            System.out.println("No items with mover!");
        else{
            Furniture removed = moveItems[numItems-1];
            numItems--;
            return removed;
        }
        return null;
    }

    @Override
    public String toString(){
        String courierStr = "Courier: " + courierName + "(" + courierID + "); " + "Tel:" + courierContact;
        if (numItems == 0)
            return courierStr;
        else {
            String items = "";
            for (int i = 0; i < numItems; i++) {
                items += moveItems[i] + "\n";
            }
            return courierStr + "\n" + "Customer: " + customer.toString() + "\n" + items;
        }
    }
}
