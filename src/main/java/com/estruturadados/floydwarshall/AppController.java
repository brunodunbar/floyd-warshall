package com.estruturadados.floydwarshall;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class AppController {

    @FXML
    private Grafo grafo;


    public void handleAbrir(ActionEvent actionEvent) {

        No testNo = new No();
        testNo.setLayoutX(30);
        testNo.setLayoutY(50);
        testNo.setLabel("Test Nó");

        grafo.getNos().addAll(testNo);

        grafo.setNoFinal(testNo);

        System.out.println("Abrir...");
    }

    public void handleSalvar(ActionEvent actionEvent) {
        System.out.println("Salvar...");

    }
}
