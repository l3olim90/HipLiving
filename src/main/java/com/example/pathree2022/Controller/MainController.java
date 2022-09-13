package com.example.pathree2022.Controller;

import com.example.pathree2022.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import com.example.pathree2022.Model.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class MainController {
    @FXML
    private VBox afterLoginPointsVBox;
    @FXML
    private VBox afterLoginVBox;
    @FXML
    private VBox pointsVBox;
    @FXML
    private VBox loginVBox;
    @FXML
    private VBox removeItemVBox;
    @FXML
    private VBox cartVBox;
    @FXML
    private TextField userLogin;
    @FXML
    private PasswordField userPassword;
    @FXML
    private Button loginBtn;
    @FXML
    private Label pointsLabel;
    @FXML
    private Button redeemBtn;
    @FXML
    private TextField pointsTxtField;
    @FXML
    private Button calcDistBtn;
    @FXML
    private Button abtProgBtn;
    @FXML
    private TextField removeChoice;
    @FXML
    private Button removeItemBtn;
    @FXML
    private Label helloLabel;
    @FXML
    private Label mainTitleLabel;
    @FXML
    private TextArea mainTxtField;
    @FXML
    private Label mainOptionLabel;
    @FXML
    private TextField mainOptionTxtField;
    @FXML
    private Button mainOptionBtn;
    @FXML
    private Label cartNumLabel;
    @FXML
    private TextArea shoppingTxtField;
    @FXML
    private Button removeOptionBtn;
    @FXML
    private Button checkoutBtn;

    private boolean currLogin;
    private Database db = new Database(System.getProperty("user.dir")+"/Customer.csv",
            System.getProperty("user.dir")+"/Furniture.csv",
            System.getProperty("user.dir")+"/Courier.csv");
    private Security secure = new Security(System.getProperty("user.dir")+"/Secure.csv");
    private ArrayList<Furniture> rentalCart = new ArrayList<>();
    private ArrayList<String> categories;
    private ArrayList<Furniture> catFurniture = new ArrayList<>();
    private boolean catFlag = true;
    private boolean addCartFlag = false;
    private boolean redeemFlag = false;
    private boolean warrantyFlag = false;
    private double perDist;
    private double pointsRedeem;
    private boolean freeDelivNoPoints;
    private int warrantyOptions;
    private boolean deliverySer = true;
    private ArrayList<Furniture> forMoving = new ArrayList<>();
    private ArrayList<Mover> moversUsed = new ArrayList<>();
    private int orderCount = -1;

    public MainController(){
        categories = db.getFurnitureCategories();
    }
    public void aboutProg(ActionEvent actionEvent) {
        Stage popupwindow = new Stage();
        popupwindow.setTitle("About Programmer");
        try {
            Parent root = FXMLLoader.load(MainApplication.class.getResource("View/abtProg.fxml"));
            popupwindow.setScene(new Scene(root));
            popupwindow.initModality(Modality.APPLICATION_MODAL);
            popupwindow.initOwner(abtProgBtn.getScene().getWindow());
            popupwindow.showAndWait();
        }catch(Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("About Programmer cannot be loaded!");
            alert.showAndWait();
        }
    }
    public void userLogin(ActionEvent actionEvent) {
        // If there is currently a login
        if (currLogin) {
            currLogin = false;
            loginBtn.setText("Login");
            loginVBox.setDisable(false);
            resetViews();
            userLogin.setText(""); userPassword.setText("");
            afterLoginPointsVBox.setVisible(false);
            afterLoginVBox.setVisible(false);
            rentalCart = new ArrayList<>();
            secure.logout();
        } else {
            String loginID = userLogin.getText();
            String password = userPassword.getText();
            if (!(secure.login(loginID, password))) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Invalid Login!");
                alert.setContentText("Please enter the correct username and password!");
                alert.showAndWait();
                return;
            }
            loginBtn.setText("Logout");
            currLogin = true;
            loginVBox.setDisable(true);
            afterLoginPointsVBox.setVisible(true);
            afterLoginVBox.setVisible(true);
            pointsVBox.setVisible(false);
            removeItemVBox.setVisible(false);
            helloLabel.setText("Hi " + db.getCustomer(Security.getCurrCustomerLogin()).getName()
                    + ", welcome to Hip Living!");
            pointsLabel.setText(String.format("You have %.2f points.",
                    secure.getAccount(Security.getCurrCustomerLogin()).getPoints()));
            cartNumLabel.setText("You have " + rentalCart.size() + " items in your shopping cart.");
            mainTxtField.setText(categoryMenu());
            mainTitleLabel.setText("Pick a category.");
            mainOptionLabel.setText("Category option:");
            mainOptionBtn.setText("Select category");
            catFlag = true;
            addCartFlag = false;
            redeemFlag = false;
            freeDelivNoPoints = false;
            warrantyFlag = false;
            deliverySer = true;
        }
    }

    public void redeemOptionClicked(ActionEvent actionEvent) {
        if (rentalCart.size() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Shopping Cart is Empty!");
            alert.setContentText("Please add items to the cart!");
            alert.showAndWait();
            return;
        } else pointsVBox.setVisible(true);
    }
    public void redeemPoints(ActionEvent actionEvent) {
        String userinput = pointsTxtField.getText();
        if (!checkNumInput(0, 100, userinput))
            return;
        pointsRedeem = Double.parseDouble(userinput);
        perDist = Account.calcPercentDiscount(pointsRedeem);
        double currPoints = secure.getAccount(Security.getCurrCustomerLogin()).getPoints();
        pointsLabel.setText(String.format("You have %.2f points.\nAfter redemption: %.2f points.", currPoints, (currPoints - pointsRedeem)));
        if (currPoints - pointsRedeem < 0) {
            redeemFlag = false;
            pointsLabel.setText(String.format("You have %.2f points.\nOnly can redeem up to %.2f points!", currPoints, currPoints));
        }else
            redeemFlag = true;
        shoppingTxtField.setText(viewCart());
    }

    public void mainOptionClick(ActionEvent actionEvent) {
        String userinput = mainOptionTxtField.getText();
        if (catFlag){
            if (!checkNumInput(1, categories.size(), userinput))
                return;
            String selection = categories.get(Integer.parseInt(userinput) - 1);
            catFurniture = db.getUnorderedFurnitureList(selection, rentalCart);
            String result = "";
            for (int i = 0; i < catFurniture.size(); i++)
                result += (i+1) + ") " + catFurniture.get(i) + "\n";
            result += (catFurniture.size()+1) + ") Back to main menu";
            mainTxtField.setText(result);
            mainTitleLabel.setText("Pick a " + selection + " furniture:");
            mainOptionLabel.setText("Select your furniture option:");
            mainOptionBtn.setText("Add to cart");
            mainOptionTxtField.setText("");
            catFlag = false;
            addCartFlag = true;
            warrantyFlag = false;
        } else if (addCartFlag){
            if (!checkNumInput(1, catFurniture.size() + 1, userinput))
                return;
            int choice = Integer.parseInt(userinput);
            if (choice != catFurniture.size()+1) {
                Furniture chosenFurniture = catFurniture.get(choice-1);
                rentalCart.add(chosenFurniture);
                shoppingTxtField.setText(viewCart());
            }
            mainTxtField.setText(categoryMenu());
            mainTitleLabel.setText("Pick a category.");
            mainOptionLabel.setText("Category option:");
            mainOptionBtn.setText("Select category");
            mainOptionTxtField.setText("");
            catFlag = true;
            addCartFlag = false;
            warrantyFlag = false;
        } else if (warrantyFlag){
            if (!checkNumInput(1, warrantyOptions, userinput))
                return;
            char warranty = getWarranty(Integer.parseInt(userinput));
            db.setFurnitureOrdered(rentalCart.get(orderCount), Security.getCurrCustomerLogin(), new Date(), warranty);
            forMoving.add(rentalCart.get(orderCount));
            mainOptionTxtField.setText("");
            if ((((orderCount + 1) % 5 == 0 && orderCount != 0) || orderCount == rentalCart.size()-1) && deliverySer){
                moversUsed.add(assignMover(forMoving, true));
                forMoving = new ArrayList<>();
            }
            if(orderCount == rentalCart.size()-1 && !deliverySer)
                moversUsed.add(assignMover(forMoving, false));
            System.out.println(moversUsed);
            if (orderCount == rentalCart.size()-1){
                Account account = secure.getAccount(Security.getCurrCustomerLogin());
                double cost = totalCost();
                account.deductPoints(pointsRedeem);
                account.addPoints(0.1*cost*(100-perDist)/100);
                writeTransaction("Transaction.txt", moversUsed);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Transaction completed");
                alert.setHeaderText("Please view your order");
                alert.setContentText("Click on the arrow (Show details) to see details");
                String deliverDetails = "";
                for (Mover m: moversUsed) deliverDetails += m.toString();

                TextArea textArea = new TextArea(deliverDetails);
                textArea.setEditable(false);
                textArea.setWrapText(true);

                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);

                GridPane moverContent = new GridPane();
                moverContent.setMaxWidth(Double.MAX_VALUE);
                moverContent.add(textArea, 0, 0);

                alert.getDialogPane().setExpandableContent(moverContent);
                alert.showAndWait();

                db.writeFurniture(System.getProperty("user.dir")+"/Furniture.csv");
                secure.writeAccount(System.getProperty("user.dir")+"/Secure.csv");

                moversUsed  = new ArrayList<>();
                rentalCart = new ArrayList<>();
                shoppingTxtField.setText(viewCart());
                mainTitleLabel.setText("Pick a category.");
                mainOptionLabel.setText("Category option:");
                mainOptionBtn.setText("Select category");
                pointsLabel.setText(String.format("You have %.2f points.",
                        secure.getAccount(Security.getCurrCustomerLogin()).getPoints()));
                catFlag = true;
                addCartFlag = false;
                freeDelivNoPoints = false;
                warrantyFlag = false;
                deliverySer = true;
                orderCount = -1;
                resetViews();
                cartVBox.setDisable(false);
            } else{
                warrantyMenu();
            }
        }
    }

    public void removeOptionClick(ActionEvent actionEvent) {
        if (rentalCart.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Shopping Cart is Empty!");
            alert.setContentText("Please add items to the cart!");
            alert.showAndWait();
        } else removeItemVBox.setVisible(true);
    }

    public void checkoutCart(ActionEvent actionEvent) {
        double points = secure.getAccount(Security.getCurrCustomerLogin()).getPoints();
        if (rentalCart.size() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Shopping Cart is Empty!");
            alert.setContentText("Please add items to the cart!");
            alert.showAndWait();
            return;
        }
        String contentTxt = "";
        if (points - pointsRedeem > 50)
            contentTxt += "50 points will be redeemed.";
        else
            contentTxt += "Additional $30 will be charged.";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Free Delivery?");
        alert.setHeaderText("Would you like free delivery service?");
        alert.setContentText(contentTxt);
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == okButton) {
            deliverySer = true;
            if (points - pointsRedeem >= 50) {
                pointsRedeem += 50;
                double currPoints = secure.getAccount(Security.getCurrCustomerLogin()).getPoints();
                pointsLabel.setText(String.format("You have %.2f points.\nAfter redemption: %.2f points.",
                        currPoints, (currPoints - pointsRedeem)));
            } else {
                freeDelivNoPoints = true;
                shoppingTxtField.setText(viewCart());
            }
        } else if (result.get() == noButton){
            deliverySer = false;
        }

        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Order?");
        alert.setHeaderText("Confirm Order?");
        alert.getButtonTypes().setAll(okButton, noButton);
        result = alert.showAndWait();
        if (result.get() == okButton) {
            cartVBox.setDisable(true);
            mainTitleLabel.setText("Select warranty type");
            mainOptionLabel.setText("Choose warranty option:");
            mainOptionBtn.setText("Select warranty");
            addCartFlag = false;
            catFlag = false;
            warrantyFlag = true;
            warrantyMenu();
        }
    }

    public void removeItem(ActionEvent actionEvent) {
        String userinput = removeChoice.getText();
        if (!checkNumInput(1, rentalCart.size(), userinput)) {
            removeItemVBox.setVisible(false);
            removeChoice.setText("");
            return;
        }
        Furniture removeF = rentalCart.get(Integer.parseInt(userinput) - 1);
        if (!rentalCart.contains(removeF)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Cannot remove furniture!");
            alert.setContentText("Furniture does not exist in cart.");
            alert.showAndWait();
            removeItemVBox.setVisible(false);
            removeChoice.setText("");
            return;
        }
        rentalCart.remove(removeF);
        shoppingTxtField.setText(viewCart());
        removeChoice.setText("");
        removeItemVBox.setVisible(false);
    }

    private void resetViews(){
        mainTxtField.setText("");
        shoppingTxtField.setText("");
        mainOptionTxtField.setText("");
        pointsTxtField.setText("");
    }
    private String categoryMenu(){
        String result = "";
        for (int i = 0; i < categories.size(); i++)
            result += (i+1) + ") " + categories.get(i) + "\n";
        return result;
    }
    private boolean checkNumInput(double lower, double upper, String input){
        if (input == null) return false;
        double num;
        try{
            num = Double.parseDouble(input);
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Invalid input!");
            alert.setContentText("Enter a number from " + (int)lower + " to " + (int)upper);
            alert.showAndWait();
            return false;
        }
        if (!(num >= lower && num <= upper)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Invalid number entered!");
            alert.setContentText("Enter a number from " + (int)lower + " to " + (int)upper);
            alert.showAndWait();
        }
        return num >= lower && num <= upper;
    }

    private String viewCart() {
        cartNumLabel.setText("You have " + rentalCart.size() + " items in your shopping cart.");
        double cost = totalCost();
        String result = "Your order: \n";
        for (int i = 0; i < rentalCart.size(); i++) {
            result += (i + 1) + ") " + rentalCart.get(i) + "\n";
        }
        result += "-------------------------------\n";
        result += String.format("Total cost: $%.2f\n", cost);
        if (redeemFlag) {
            result += "-------------------------------\n";
            result += String.format("Total cost after discount: $%.2f\n", cost*(100-perDist)/100);
        }
        result += "-------------------------------\n";
        return result;
    }
    private double totalCost(){
        double total = 0;
        for (Furniture f : rentalCart) total += f.getCost();
        if (freeDelivNoPoints) return total + 30;
        else return total;
    }
    private void warrantyMenu(){
        orderCount++;
        Furniture f = rentalCart.get(orderCount);
        String menu = f.toString() + "\n";
        warrantyOptions = 1;
        double cost = f.getCost();
        menu += "1) No warranty\n";
        if (cost >= 100) {
            warrantyOptions++;
            menu += warrantyOptions + ") Low Warranty\n";
        }
        if (cost >= 250) {
            warrantyOptions++;
            menu += warrantyOptions + ") Medium Warranty\n";
        }
        if (cost >= 500) {
            warrantyOptions++;
            menu += warrantyOptions + ") High Warranty\n";
        }
        mainTxtField.setText(menu);
    }
    private char getWarranty(int option){
        switch(option){
            case 1: return 'N';
            case 2: return 'L';
            case 3: return 'M';
            case 4: return 'H';
        }
        return 'N';
    }
    private Mover assignMover(ArrayList<Furniture> furnitureList, boolean deliveryService){
        if (furnitureList.size() > 5 && deliveryService)
            return null;
        String[] tokens = db.getNextCourier();
        Mover mover;
        if (deliveryService)
            mover = new Mover(tokens[0], tokens[1], tokens[2]);
        else
            mover = new Mover(furnitureList.size());
        Customer deliverCust = db.getCustomer(furnitureList.get(0).getCustomerID());
        mover.setCustomer(deliverCust);
        for (Furniture f : furnitureList){
            mover.addMoveItems(f);
        }
        return mover;
    }
    private void writeTransaction(String filename, ArrayList<Mover> movers){
        try{
            PrintWriter output = new PrintWriter(new FileOutputStream(filename, true));
            for (Mover m : movers){
                output.print(m.toString());
            }
            output.close();
        } catch(Exception e){e.printStackTrace(); }
    }
}