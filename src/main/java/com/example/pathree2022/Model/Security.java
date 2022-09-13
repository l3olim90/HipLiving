package com.example.pathree2022.Model;

import java.io.*;
import java.util.ArrayList;

public class Security {
    private ArrayList<Account> loginDB;
    private static String currCustomerLogin;

    public Security(String filename){
        loginDB = new ArrayList<Account>();
        loadLoginDB(filename);
    }

    public Account getAccount(String customerID){
        if (!Customer.checkCustomerID(customerID))
            throw new IllegalArgumentException("Invalid Customer ID!");

        for (Account a : loginDB){
            if(a.getCustomerID().equals(customerID))
                return a;
        }
        return null;
    }

    public void loadLoginDB(String filename){
        BufferedReader br = null;
        try{
            String line;
            br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                loginDB.add(new Account(data[0], data[1], data[2], Double.parseDouble(data[3])));
            }
            br.close();
        }catch (IOException e) { e.printStackTrace();
        }finally{ try { br.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }
    public static String getCurrCustomerLogin(){
        return currCustomerLogin;
    }

    public boolean login(String loginID, String password){
        for (Account a : loginDB){
            if (a.getLoginID().equals(loginID) && a.getPassword().equals(password)){
                currCustomerLogin = a.getCustomerID();
                return true;
            }
        }
        return false;
    }

    public void logout() {currCustomerLogin = null;}

    public void writeAccount(String filename){
        int item = 1;
        try{
            PrintWriter output = new PrintWriter(new FileOutputStream(filename));

            for (Account a: loginDB){
                output.println(a.getLoginID()+","
                        + a.getPassword() + ","
                        + a.getCustomerID() + ","
                        + a.getPoints() + ",");
                item++;
            }
            output.close();
        } catch(Exception e){
            System.out.println("Error at record " + item);
        }
    }
}
