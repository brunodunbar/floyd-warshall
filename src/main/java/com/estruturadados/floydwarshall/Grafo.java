package com.estruturadados.floydwarshall;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
import sun.swing.MenuItemLayoutHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Grafo extends GridPane {

    private double actionX, actionY;

    private No actionNo;

    private No noSelecionado;

    private No noInicial;
    private No noFinal;

    @FXML
    private Pane grafoPane;

    private ObservableList<No> nos = FXCollections.observableArrayList();
    private ObservableList<Vertice> vertices = FXCollections.observableArrayList();

    private final ContextMenu contextMenu;

    private final ContextMenu noContextMenu;

    public Grafo() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Grafo.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        nos.addListener((ListChangeListener<No>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    grafoPane.getChildren().removeAll(c.getRemoved());
                }
                if (c.wasAdded()) {

                    List<Node> collect = c.getAddedSubList().stream()
                            .map(no -> makeDraggable(no))
                            .collect(Collectors.toList());

                    grafoPane.getChildren().addAll(collect);
                }
            }
        });

        vertices.addListener((ListChangeListener<Vertice>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    grafoPane.getChildren().removeAll(c.getRemoved());
                }
                if (c.wasAdded()) {
                    grafoPane.getChildren().addAll(c.getAddedSubList());
                }
            }
        });


        contextMenu = new ContextMenu();
        MenuItem novoNo = new MenuItem("Novo Nó");
        novoNo.setOnAction(e -> {
            No no = new No();

            no.setLayoutX(actionX);
            no.setLayoutY(actionY);

            no.setLabel("Nó " + (nos.size() + 1));

            nos.add(no);
        });
        contextMenu.getItems().addAll(novoNo);


        noContextMenu = new ContextMenu();

        MenuItem criarVertice = new MenuItem("Criar vertice");
        criarVertice.setOnAction(event -> {

            if (actionNo == null) {
                return;
            }

            if (noSelecionado != null && noSelecionado != actionNo) {
                if (!possuiVertice(noSelecionado, actionNo)) {
                    Vertice vertice = new Vertice(noSelecionado, actionNo);
                    vertices.add(vertice);
                }
            }
        });

        MenuItem definirInicial = new MenuItem("Definir como nó inicial");
        definirInicial.setOnAction(event -> {

            if (actionNo == null) {
                return;
            }

            setNoInicial(actionNo);
        });

        MenuItem definirFinal = new MenuItem("Definir como nó final");
        definirFinal.setOnAction(event -> {

            if (actionNo == null) {
                return;
            }

            setNoFinal(actionNo);

        });

        noContextMenu.getItems().addAll(criarVertice, definirInicial, definirFinal);

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
                actionNo = no;
                noContextMenu.show(no, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            } else {
                noContextMenu.hide();
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

    public ObservableList<No> getNos() {
        return nos;
    }

    public ObservableList<Vertice> getVertices() {
        return vertices;
    }

    public void clear() {
        grafoPane.getChildren().clear();
    }

    public void setNoInicial(No no) {
        if (noInicial != null) {
            noInicial.setInicial(false);
        }
        noInicial = no;
        noInicial.setInicial(true);
    }

    public void setNoFinal(No no) {
        if (noFinal != null) {
            noFinal.setFinal(false);
        }
        noFinal = no;
        noFinal.setFinal(true);
    }
}
