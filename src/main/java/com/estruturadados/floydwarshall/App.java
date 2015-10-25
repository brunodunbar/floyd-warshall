package com.estruturadados.floydwarshall;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Grafo grafo = new Grafo();

        Scene scene = new Scene(grafo);
        scene.getStylesheets().add("/app.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Floyd Warshall");
        primaryStage.setWidth(500);
        primaryStage.setHeight(400);
        primaryStage.show();
    }
}
