module com.example.lab4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lab4 to javafx.fxml;
    exports com.example.lab4;
    exports com.example.lab4.rasterizer;
    opens com.example.lab4.rasterizer to javafx.fxml;
}