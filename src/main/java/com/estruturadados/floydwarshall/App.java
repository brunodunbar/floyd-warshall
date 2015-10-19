package com.estruturadados.floydwarshall;

import com.estruturadados.floydwarshall.ui.GrafoUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        GrafoUI grafoUI = new GrafoUI();

        primaryStage.setScene(new Scene(grafoUI));
        primaryStage.setTitle("Custom Control");
        primaryStage.setWidth(500);
        primaryStage.setHeight(400);
        primaryStage.show();
    }
}
