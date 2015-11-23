package com.estruturadados.floydwarshall;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import javafx.stage.FileChooser;

public class AppController {

    @FXML
    private Grafo grafo;
    private FileChooser salvarFileChooser;
    private FileChooser abrirFileChooser;

    public void handleAbrir(ActionEvent actionEvent) {
//
//        No testNo = new No();
//        testNo.setLayoutX(30);
//        testNo.setLayoutY(50);
//        testNo.setLabel("Test Nó");
//
//        grafo.getNos().addAll(testNo);
//
//        grafo.setNoFinal(testNo);
//
//        System.out.println("Abrir...");

        if (abrirFileChooser == null) {
            abrirFileChooser = new FileChooser();
            abrirFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json", "*.json"));
            abrirFileChooser.setTitle("Abrir...");
        }

        File file = abrirFileChooser.showOpenDialog(grafo.getScene().getWindow());

        if (file != null && file.exists()) {
            try (JsonReader reader = new JsonReader(new FileReader(file))) {

                grafo.clear();

                reader.beginObject();

                while (reader.hasNext()) {

                    switch (reader.nextName()) {
                        case "nos":

                            reader.beginArray();

                            while (reader.hasNext()) {

                                reader.beginObject();

                                No no = new No();

                                while (reader.hasNext()) {
                                    switch (reader.nextName()) {
                                        case "label":
                                            no.setLabel(reader.nextString());
                                            break;
                                        case "x":
                                            no.setLayoutX(reader.nextDouble());
                                            break;
                                        case "y":
                                            no.setLayoutY(reader.nextDouble());
                                            break;
                                        case "inical":
                                            no.setInicial(reader.nextBoolean());
                                            break;
                                        case "final":
                                            no.setFinal(reader.nextBoolean());
                                            break;
                                        default:
                                            reader.skipValue();
                                    }
                                }

                                grafo.getNos().add(no);
                                reader.endObject();
                            }

                            reader.endArray();

                            break;
                        case "arestas":

                            reader.beginArray();

                            while (reader.hasNext()) {

                                reader.beginObject();

                                No de = null, para = null;
                                int distancia = 0;

                                while (reader.hasNext()) {
                                    switch (reader.nextName()) {
                                        case "de":
                                            de = grafo.buscaNoPorLabel(reader.nextString());
                                            break;
                                        case "para":
                                            para = grafo.buscaNoPorLabel(reader.nextString());
                                            break;
                                        case "distancia":
                                            distancia = reader.nextInt();
                                            break;
                                        default:
                                            reader.skipValue();
                                    }
                                }

                                Aresta aresta = new Aresta(de, para);
                                aresta.setDistancia(distancia);

                                grafo.getArestas().add(aresta);

                                reader.endObject();
                            }
                            reader.endArray();
                            break;
                        default:
                            reader.skipValue();
                    }
                }

                reader.endObject();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
