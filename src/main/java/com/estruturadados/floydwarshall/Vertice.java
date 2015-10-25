package com.estruturadados.floydwarshall;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

import java.util.Arrays;
import java.util.List;

public class Vertice extends Group {

    private No de;
    private No para;

    private Line line = new Line();
    private Label label = new Label();

    int distancia;

    public Vertice(No de, No para) {

        this.de = de;
        this.para = para;

        line = new Line();

        line.setStrokeWidth(2);
        line.setStroke(Color.BLACK.deriveColor(0, 1, 1, 0.5));
        line.setStrokeLineCap(StrokeLineCap.BUTT);

        de.layoutXProperty().addListener(observable -> draw());
        de.layoutYProperty().addListener(observable -> draw());

        para.layoutYProperty().addListener(observable -> draw());
        para.layoutXProperty().addListener(observable -> draw());

        draw();

        getChildren().addAll(line, label);
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

        distancia = Double.valueOf(deCenter.distance(paraCenter)).intValue();

        label.setText(Integer.toString(distancia));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertice vertice = (Vertice) o;

        if (de != null ? !de.equals(vertice.de) : vertice.de != null) return false;
        return !(para != null ? !para.equals(vertice.para) : vertice.para != null);

    }

    @Override
    public int hashCode() {
        int result = de != null ? de.hashCode() : 0;
        result = 31 * result + (para != null ? para.hashCode() : 0);
        return result;
    }
}