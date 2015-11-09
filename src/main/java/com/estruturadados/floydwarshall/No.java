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
    private final BooleanProperty selecionado = new SimpleBooleanProperty();
    private final BooleanProperty inicial = new SimpleBooleanProperty();
    private final BooleanProperty _final = new SimpleBooleanProperty();

    public No() {

        label = new Label();
        getChildren().add(label);

        getStyleClass().add("no");

        selecionado.addListener(observable -> {
            if (selecionado.get()) {
                getStyleClass().add("selecionado");
            } else {
                getStyleClass().remove("selecionado");
            }
        });

        inicial.addListener(observable -> {
            if (inicial.get()) {
                getStyleClass().add("inicial");
            } else {
                getStyleClass().remove("inicial");
            }
        });

        _final.addListener(observable -> {
            if (_final.get()) {
                getStyleClass().add("final");
            } else {
                getStyleClass().remove("final");
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

    public boolean getInicial() {
        return inicial.get();
    }

    public BooleanProperty inicialProperty() {
        return inicial;
    }

    public void setInicial(boolean inicial) {
        this.inicial.set(inicial);
    }

    public boolean getFinal() {
        return _final.get();
    }

    public BooleanProperty finalProperty() {
        return _final;
    }

    public void setFinal(boolean _final) {
        this._final.set(_final);
    }
}
