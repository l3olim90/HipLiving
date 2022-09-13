package com.example.pathree2022.Model;

public class Customer {
    private String customerID;
    private String name;
    private String contactNo;
    private String address;
    private String postalCode;

    public Customer(){
        this.customerID = "000Z";
        this.name = null;
        this.contactNo = "99999999";
        this.address = null;
        this.postalCode = "123456";
    }

    public Customer(String customerID, String name, String contactNo, String address, String postalCode){
        if (!Customer.checkCustomerID(customerID))
            throw new IllegalArgumentException("Invalid Customer ID.");
        else if (!Customer.checkContactNo(contactNo))
            throw new IllegalArgumentException("Invalid Handphone No.");
        else if (!Customer.checkPostalCode(postalCode))
            throw new IllegalArgumentException("Invalid Postal Code.");
        else {
            this.customerID = customerID;
            this.name = name;
            this.contactNo = contactNo;
            this.address = address;
            this.postalCode = postalCode;
        }
    }

    public static boolean checkCustomerID(String customerID){
        if (customerID.length() != 4) return false;
        for (int i = 0; i < 3; i++){
            if (!Character.isDigit(customerID.charAt(i))) return false;
        }
        return Character.isLetter(customerID.charAt(3)) && Character.isUpperCase(customerID.charAt(3));
    }
    public static boolean checkContactNo(String contactNo){
        if (contactNo.length() != 8) return false;
        for (int i = 0; i < 8; i++){
            if (!Character.isDigit(contactNo.charAt(i))) return false;
        }
        return contactNo.charAt(0) == '8' || contactNo.charAt(0) == '9';
    }
    public static boolean checkPostalCode(String postalCode){
        if (postalCode.length() != 6) return false;
        for (int i = 0; i < 6; i++){
            if (!Character.isDigit(postalCode.charAt(i))) return false;
        }
        return true;
    }

    public String getCustomerID() { return customerID; }
    public void setCustomerID(String customerID) {
        if (!Customer.checkCustomerID(customerID))
            throw new IllegalArgumentException("Invalid Customer ID.");
        this.customerID = customerID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }
    public void setContactNo(String contactNo) {
        if (!Customer.checkContactNo(contactNo))
            throw new IllegalArgumentException("Invalid Handphone No.");
        this.contactNo = contactNo;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) { this.address = address; }

    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        if (!Customer.checkPostalCode(postalCode))
            throw new IllegalArgumentException("Invalid Postal Code.");
        this.postalCode = postalCode;
    }

    @Override
    public String toString(){
        return "(" + customerID + ") " + name + "; Tel:" + contactNo + "; " + address + " S(" + postalCode + ")";
    }

}
