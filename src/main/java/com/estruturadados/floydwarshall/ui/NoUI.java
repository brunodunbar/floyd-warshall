package com.estruturadados.floydwarshall.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class NoUI extends VBox {

    public NoUI() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/NoUI.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
