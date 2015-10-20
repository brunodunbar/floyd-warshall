package com.estruturadados.floydwarshall.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class NoUI extends VBox {

    @FXML
    private Label label;

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

    public void setLabel(String value) {
        label.setText(value);
    }
}
