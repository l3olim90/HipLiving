package com.example.pathree2022.Model;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class Database {
    private ArrayList<Customer> customerDB;
    private ArrayList<Furniture> furnitureDB;
    private ArrayList<String[]> courierDB;

    public Database(String custFile, String furnitureFile, String courierFile){
        customerDB = new ArrayList<>();
        furnitureDB = new ArrayList<>();
        courierDB = new ArrayList<>();
        loadCustDB(custFile);
        loadFurnitureDB(furnitureFile);
        loadCourierDB(courierFile);
    }

    private void loadCustDB(String filename){
        BufferedReader br = null;
        try{
            String line;
            br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                customerDB.add(new Customer(data[0], data[1], data[2], data[3], data[4]));
            }
            br.close();
        }catch (IOException e) { e.printStackTrace();
        }finally{ try { br.close(); } catch (Exception e) { e.printStackTrace(); } }
    }

    private void loadFurnitureDB(String filename){
        BufferedReader br = null;
        try{
            String line;
            br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4)
                    furnitureDB.add(new Furniture(data[0], data[1], data[2], Double.parseDouble(data[3])));
                else
                    furnitureDB.add(new Furniture(data[0], data[1], data[2], Double.parseDouble(data[3]),
                            data[4], data[5], data[6].charAt(0)));
            }
            br.close();
        }catch (IOException | ParseException e) { e.printStackTrace();
        }finally{ try { br.close(); } catch (Exception e) { e.printStackTrace(); } }
    }

    private void loadCourierDB(String filename){
        BufferedReader br = null;
        try{
            String line;
            br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine()) != null) {
                courierDB.add(line.split(","));
            }
            br.close();
        }catch (IOException e) { e.printStackTrace();
        }finally{ try { br.close(); } catch (Exception e) { e.printStackTrace(); } }
    }

    public Customer getCustomer(String custID) {
        if (!Customer.checkCustomerID(custID)) throw new IllegalArgumentException("Invalid Customer ID!");
        for (Customer c : customerDB){
            if (c.getCustomerID().equals(custID))
                return c;
        }
        return null;
    }

    public Furniture getFurniture(String furnitureID) {
        for (Furniture f : furnitureDB){
            if (f.getFurnitureID().equals(furnitureID))
                return f;
        }
        return null;
    }

    public String[] getNextCourier(){
        return courierDB.get(Mover.getMoverCount());
    }

    public String setFurnitureOrdered(Furniture furniture, String custID, Date date, char warrantyLevel){
        if (!Customer.checkCustomerID(custID)) throw new IllegalArgumentException("Invalid Customer ID!");
        if (!Furniture.checkWarrantyLevel(warrantyLevel, furniture.getCost())) throw new IllegalArgumentException("Invalid Warranty Level!");
        if (furniture.isOrdered()) return "Item already ordered!";
        furniture.setOrdered(true);
        furniture.setCustomerID(custID);
        furniture.setOrderDate(date);
        furniture.setWarrantyLevel(warrantyLevel);
        return "Order success!";
    }

    public String setFurnitureUnordered(Furniture furniture){
        furniture.setOrdered(false);
        furniture.setCustomerID("000Z");
        furniture.setOrderDate(null);
        furniture.setWarrantyLevel('N');
        return "Order status reset!";
    }

    public ArrayList<String> getFurnitureCategories(){
        ArrayList<String> temp = new ArrayList<>();
        for (Furniture f : furnitureDB){
            if (!temp.contains(f.getCategory()))
                temp.add(f.getCategory());
        }
        return temp;
    }

    public ArrayList<Furniture> getUnorderedFurnitureList(String category, ArrayList<Furniture> shoppingCart) {
        ArrayList<String> categories = getFurnitureCategories();
        ArrayList<Furniture> result = new ArrayList<>();
        if (!categories.contains(category))
            return result;
        for (Furniture f : furnitureDB){
            boolean rented = false;
            for (Furniture rental : shoppingCart) {
                if (f.getFurnitureID().equals(rental.getFurnitureID())) {
                    rented = true;
                    break;
                }
            }
            if (f.getCategory().equals(category) && !f.isOrdered() && !rented)
                result.add(f);
        }
        return result;
    }

    public void writeFurniture(String filename){
        int item = 1;
        try{
            PrintWriter output = new PrintWriter(new FileOutputStream(filename));

            for (Furniture f: furnitureDB){
                if (!f.isOrdered()){
                    output.println(f.getFurnitureID()+","
                            + f.getName() + ","
                            + f.getCategory() + ","
                            + f.getCost());
                }
                else{
                    output.println(f.getFurnitureID()+","
                            + f.getName() + ","
                            + f.getCategory() + ","
                            + f.getCost() + ","
                            + f.getOrderDate() + ","
                            + f.getCustomerID() + ","
                            + f.getWarrantyLevel());
                }
                item++;
            }
            output.close();
        } catch(Exception e){System.out.println("Error at record: " + item); }
    }
}
