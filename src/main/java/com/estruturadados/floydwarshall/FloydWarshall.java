package com.estruturadados.floydwarshall;

import javafx.collections.ObservableList;

public class FloydWarshall {

    private Grafo grafo;

    private double[][] distTo;
    private GAresta[][] edgeTo;
    private boolean hasNegativeCycle;

    public FloydWarshall(Grafo grafo) {
        this.grafo = grafo;
    }

    public void calcular() {
        hasNegativeCycle = false;
        ObservableList<No> nos = grafo.getNos();

        int size = nos.size();
        distTo = new double[size][size];
        edgeTo = new GAresta[size][size];

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
                        edgeTo[from][to] = new GAresta(from, to, aresta);

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
            throw new IllegalStateException("O grafo possui ciclos negativos");
        }

        No noInicial = grafo.getNoInicial();
        No noFinal = grafo.getNoFinal();

        if (noInicial == null || noFinal == null) {
            throw new IllegalStateException("É necessário configurar o inicio e fim");
        }

        ObservableList<No> nos = grafo.getNos();
        int inicio = nos.indexOf(noInicial);
        int fim = nos.indexOf(noFinal);

        if (!temCaminho(inicio, fim)) {
            throw new IllegalStateException("Não existe nenhum caminho entre " + noInicial + " e " + noFinal);
        }

        for (GAresta e = edgeTo[inicio][fim]; e != null; ) {
            e.getAresta().setCaminho(true);
            e = edgeTo[inicio][e.getDe()];
        }
    }

    public boolean temCaminho(int inicio, int fim) {
        return distTo[inicio][fim] < Double.POSITIVE_INFINITY;
    }

    private static class GAresta {
        private int de, para;
        private Aresta aresta;

        public GAresta(int de, int para, Aresta aresta) {
            this.de = de;
            this.para = para;
            this.aresta = aresta;
        }

        public int getDe() {
            return de;
        }

        public int getPara() {
            return para;
        }

        public Aresta getAresta() {
            return aresta;
        }
    }

}
