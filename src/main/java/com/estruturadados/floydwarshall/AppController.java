package com.estruturadados.floydwarshall;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import javafx.stage.FileChooser;

public class AppController {

    @FXML
    private Grafo grafo;
    private FileChooser salvarFileChooser;

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
        if (salvarFileChooser == null) {
            salvarFileChooser = new FileChooser();
            salvarFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json", "*.json"));
            salvarFileChooser.setTitle("Salvar...");
        }

        File file = salvarFileChooser.showSaveDialog(grafo.getScene().getWindow());
        if (file != null) {

            try (JsonWriter writer = new JsonWriter(new FileWriter(file))) {
                writer.setIndent("  ");
                writer.beginObject();
                writer.name("nos");

                writer.beginArray();

                for (No no : grafo.getNos()) {

                    writer.beginObject();

                    writer.name("label").value(no.getLabel());
                    writer.name("x").value(no.getLayoutX());
                    writer.name("y").value(no.getLayoutY());
                    writer.name("inicial").value(no.getInicial());
                    writer.name("final").value(no.getFinal());
                    
                    writer.endObject();

                }
                writer.endArray();

                writer.name("arestas");
                writer.beginArray();
                for (Aresta aresta : grafo.getArestas()) {

                    writer.beginObject();
                    
                    writer.name("de").value(aresta.getDe().getLabel());
                    writer.name("para").value(aresta.getPara().getLabel());
                    writer.name("distancia").value(aresta.getLabel());

                    writer.endObject();
                }
                writer.endArray();

                writer.endObject();

                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
