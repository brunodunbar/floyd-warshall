package com.estruturadados.floydwarshall;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;

public class No extends VBox {

    private Label label;
    private BooleanProperty selecionado = new SimpleBooleanProperty();

    public No() {
        label = new Label();
        getChildren().add(label);

        getStyleClass().add("no");

        selecionado.addListener(observable -> {
            if(selecionado.get()) {
                getStyleClass().add("selecionado");
            } else {
                getStyleClass().remove("selecionado");
            }
        });
    }

    public void setLabel(String value) {
        label.setText(value);
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado.set(selecionado);
    }

    public boolean isSelecionado() {
        return selecionado.get();
    }
}
