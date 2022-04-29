package com.example.lab4;

import com.example.lab4.view.View;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class RasterApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        new View().start(stage);
    }


    public static void main(String[] args) {
        launch();
    }
}