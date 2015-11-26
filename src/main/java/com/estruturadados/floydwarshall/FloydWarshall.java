package com.estruturadados.floydwarshall;

import javafx.collections.ObservableList;

public class FloydWarshall {

    private Grafo grafo;

    private double[][] distTo;
    private Aresta[][] edgeTo;
    private boolean hasNegativeCycle;

    public FloydWarshall(Grafo grafo) {
        this.grafo = grafo;


    }

    public void calcular() {

        hasNegativeCycle = false;
        ObservableList<No> nos = grafo.getNos();

        int size = nos.size();
        distTo = new double[size][size];
        edgeTo = new Aresta[size][size];

        for (int v = 0; v < size; v++) {
            for (int w = 0; w < size; w++) {
                distTo[v][w] = Double.POSITIVE_INFINITY;
            }
        }

        for (int v = 0; v < size; v++) {
            final int from = v;
            grafo.buscaArestas(nos.get(v))
                    .forEach(aresta -> {

                        int to = nos.indexOf(aresta.getPara());
                        if (aresta.getPara().equals(nos.get(from))) {
                            to = nos.indexOf(aresta.getDe());
                        }

                        distTo[from][to] = aresta.getDistancia();
                        edgeTo[from][to] = aresta;

                        if (distTo[from][from] >= 0.0) {
                            distTo[from][from] = 0.0;
                            edgeTo[from][from] = null;
                        }

                    });
        }

        // Floyd-Warshall updates
        for (int i = 0; i < size; i++) {
            // compute shortest paths using only 0, 1, ..., i as intermediate vertices
            for (int v = 0; v < size; v++) {
                if (edgeTo[v][i] == null) continue;  // optimization
                for (int w = 0; w < size; w++) {
                    if (distTo[v][w] > distTo[v][i] + distTo[i][w]) {
                        distTo[v][w] = distTo[v][i] + distTo[i][w];
                        edgeTo[v][w] = edgeTo[i][w];
                    }
                }
                // check for negative cycle
                if (distTo[v][v] < 0.0) {
                    hasNegativeCycle = true;
                    return;
                }
            }
        }

        mostrarCaminho();
    }

    public void mostrarCaminho() {

        for (Aresta aresta : grafo.getArestas()) {
            aresta.setCaminho(false);
        }

        if (hasNegativeCycle) {
            return;
        }

        No noInicial = grafo.getNoInicial();
        No noFinal = grafo.getNoFinal();

        if (noInicial == null || noFinal == null) {
            return;
        }


        ObservableList<No> nos = grafo.getNos();
        int inicio = nos.indexOf(noInicial);
        int fim = nos.indexOf(noFinal);


        if (!temCaminho(inicio, fim)) {
            return;
        }

        for (Aresta e = edgeTo[inicio][fim]; e != null; ) {
            e.setCaminho(true);

            int indexOf = nos.indexOf(e.getDe());
            if (indexOf == inicio) {
                indexOf = nos.indexOf(e.getPara());
            }

            e = edgeTo[inicio][indexOf];

        }
    }

    public boolean temCaminho(int inicio, int fim) {
        return distTo[inicio][fim] < Double.POSITIVE_INFINITY;
    }


}
