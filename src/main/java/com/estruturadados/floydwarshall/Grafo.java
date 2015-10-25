package com.estruturadados.floydwarshall;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Grafo extends GridPane {

    private double actionX, actionY;

    private No noSelecionado;

    @FXML
    private Pane grafoPane;

    private List<No> nos = new ArrayList<>();
    private List<Vertice> vertices = new ArrayList<>();

    private final ContextMenu contextMenu;

    public Grafo() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Grafo.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }


        contextMenu = new ContextMenu();
        MenuItem novoNo = new MenuItem("Novo Nó");
        novoNo.setOnAction(e -> {
            No no = new No();

            no.setLayoutX(actionX);
            no.setLayoutY(actionY);

            no.setLabel("Nó " + (grafoPane.getChildren().size() + 1));

            nos.add(no);
            grafoPane.getChildren().add(makeDraggable(no));
        });
        contextMenu.getItems().addAll(novoNo);

        grafoPane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                actionX = event.getX();
                actionY = event.getY();

                contextMenu.show(Grafo.this, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
    }

    private Node makeDraggable(final No no) {
        final Delta dragDelta = new Delta();
        final Group wrapGroup = new Group(no);

        wrapGroup.addEventFilter(MouseEvent.ANY, Event::consume);

        wrapGroup.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                if (noSelecionado != null && noSelecionado != no) {
                    if (!possuiVertice(noSelecionado, no)) {
                        Vertice vertice = new Vertice(noSelecionado, no);
                        vertices.add(vertice);
                        grafoPane.getChildren().add(vertice);
                    }
                }
            }
        });

        wrapGroup.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {

                if (noSelecionado != null) {
                    noSelecionado.setSelecionado(false);
                }
                noSelecionado = no;
                noSelecionado.setSelecionado(true);

                dragDelta.x = no.getLayoutX() - mouseEvent.getX();
                dragDelta.y = no.getLayoutY() - mouseEvent.getY();
                getScene().setCursor(Cursor.MOVE);
            }

            contextMenu.hide();
        });

        wrapGroup.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                getScene().setCursor(Cursor.HAND);
            }
        });

        wrapGroup.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                double newX = mouseEvent.getX() + dragDelta.x;
                if (newX > 0 && newX < getScene().getWidth() - no.getWidth()) {
                    no.setLayoutX(newX);
                }
                double newY = mouseEvent.getY() + dragDelta.y;
                if (newY > 0 && newY < getScene().getHeight() - no.getHeight()) {
                    no.setLayoutY(newY);
                }
            }
        });

        wrapGroup.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.HAND);
            }
        });

        wrapGroup.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });

        return wrapGroup;
    }

    private boolean possuiVertice(No no1, No no2) {
        return vertices.stream().anyMatch(vertice -> vertice.getDe().equals(no1) && vertice.getPara().equals(no2) ||
                vertice.getDe().equals(no2) && vertice.getPara().equals(no1));
    }

    private class Delta {
        double x, y;
    }
}
