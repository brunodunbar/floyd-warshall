package com.estruturadados.floydwarshall.ui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class GrafoUI extends Pane {

    public GrafoUI() {
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                NoUI noUI = new NoUI();
                noUI.setLayoutX(event.getSceneX());
                noUI.setLayoutY(event.getSceneY());

                noUI.setLabel("Nó " + (getChildren().size() + 1));

                getChildren().add(noUI);
            }
        });
    }
}
