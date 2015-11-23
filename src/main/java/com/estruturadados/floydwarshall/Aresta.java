package com.estruturadados.floydwarshall;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Aresta extends Group {

    private No de;
    private No para;

    @FXML
    private Line line;

    @FXML
    private Label label;

    IntegerProperty distancia = new SimpleIntegerProperty();

    public Aresta(No de, No para) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Aresta.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        label.textProperty().bind(distancia.asString());

        this.de = de;
        this.para = para;

        de.layoutXProperty().addListener(observable -> draw());
        de.layoutYProperty().addListener(observable -> draw());

        para.layoutYProperty().addListener(observable -> draw());
        para.layoutXProperty().addListener(observable -> draw());

        draw();
    }

    @FXML
    private void handleMouseClicked(MouseEvent event) {

        if(event.getClickCount() == 2) {

            TextInputDialog dialog = new TextInputDialog(distancia.asString().getValue());
            dialog.setTitle("Distancia da aresta");
            dialog.setContentText("Digite a distancia da aresta:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(valor -> {
                distancia.set(Integer.valueOf(valor));
            });
        }
    }

    private void draw() {
        Bounds deBoundsInParent = de.getBoundsInParent();
        Bounds paraBoundsInParent = para.getBoundsInParent();

        Point2D deCenter = new Point2D(deBoundsInParent.getMinX() + deBoundsInParent.getWidth() / 2,
                deBoundsInParent.getMinY() + deBoundsInParent.getHeight() / 2);

        Point2D paraCenter = new Point2D(paraBoundsInParent.getMinX() + paraBoundsInParent.getWidth() / 2,
                paraBoundsInParent.getMinY() + paraBoundsInParent.getHeight() / 2);

        Point2D dePosition = sortest(deBoundsInParent, paraCenter);
        Point2D paraPosition = sortest(paraBoundsInParent, dePosition);

        line.setStartX(dePosition.getX());
        line.setStartY(dePosition.getY());
        line.setEndX(paraPosition.getX());
        line.setEndY(paraPosition.getY());

        label.setLayoutX(dePosition.getX() + ((paraPosition.getX() - dePosition.getX()) / 2));
        label.setLayoutY(dePosition.getY() + ((paraPosition.getY() - dePosition.getY()) / 2));
    }

    private Point2D sortest(Bounds bounds, Point2D center) {

        List<Point2D> point2Ds = Arrays.asList(
                new Point2D(bounds.getMinX(), bounds.getMinY() + bounds.getHeight() / 2),
                new Point2D(bounds.getMinX() + bounds.getWidth() / 2, bounds.getMinY()),
                new Point2D(bounds.getMinX() + bounds.getWidth() / 2, bounds.getMaxY()),
                new Point2D(bounds.getMaxX(), bounds.getMinY() + bounds.getHeight() / 2)
        );

        Point2D position = null;
        double smallestDistance = Double.MAX_VALUE;
        for (Point2D point2D : point2Ds) {
            double distance = center.distance(point2D);
            if (distance < smallestDistance) {
                position = point2D;
                smallestDistance = distance;
            }
        }

        return position;
    }

    public No getDe() {
        return de;
    }

    public No getPara() {
        return para;
    }

    public int getDistancia() {
        return distancia.get();
    }

    public IntegerProperty distanciaProperty() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia.set(distancia);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Aresta aresta = (Aresta) o;

        if (de != null ? !de.equals(aresta.de) : aresta.de != null) return false;
        return !(para != null ? !para.equals(aresta.para) : aresta.para != null);

    }

    @Override
    public int hashCode() {
        int result = de != null ? de.hashCode() : 0;
        result = 31 * result + (para != null ? para.hashCode() : 0);
        return result;
    }
}