module com.example.pathree2022 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pathree2022 to javafx.fxml;
    exports com.example.pathree2022;
    exports com.example.pathree2022.Controller;
    opens com.example.pathree2022.Controller to javafx.fxml;
}