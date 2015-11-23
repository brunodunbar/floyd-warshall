package com.estruturadados.floydwarshall;

/*Defini��o
O algoritmo de Floyd-Warshall recebe como entrada uma matriz de adjac�ncia que 
representa um grafo (V, E) orientado e valorado. O valor de um caminho entre dois 
v�rtices � a soma dos valores de todas as arestas ao longo desse caminho. 
As arestas E do grafo podem ter valores negativos, mas o grafo n�o pode conter 
nenhum ciclo de valor negativo. O algoritmo calcula, para cada par de v�rtices, 
o menor de todos os caminhos entre os v�rtices. Por exemplo, o caminho de menor custo.
Sua ordem de complexidade � \theta(|V|^3).

O algoritmo se baseia nos passos abaixo:

Assumindo que os v�rtices de um grafo orientado G s�o V = {1,2,3,\ldots,n}, considere um subconjunto {1,2,3\ldots,k};
Para qualquer par de v�rtices (i, j) em V, considere todos os caminhos de i a j cujos v�rtices interm�dios pertencem ao subconjunto {1,2,3\ldots,k}, e p como o mais curto de todos eles;
O algoritmo explora um relacionamento entre o caminho p e os caminhos mais curtos de i a j com todos os v�rtices interm�dios em {1,2,3\ldots,k-1};
O relacionamento depende de k ser ou n�o um v�rtice interm�dio do caminho p.
link da DEFINI��O https://pt.wikipedia.org/wiki/Algoritmo_de_Floyd-Warshall
    ESPERO TER AJUDADO
*/

public class FloydWarshallOld {
    static int[][] P;
    static final int N = 4;

    public static void main(String[] args) {
        //public static void Start() {
        int[][] M = {
                {0, 5, 999, 999},
                {50, 0, 15, 5},
                {30, 99, 0, 15},
                {15, 99, 5, 0}
        };

        P = new int[N][N];
        System.out.println("Matris para encontrar o caminho mais curto.");
        printMatris(M);
        System.out.println("Matris de caminho mais curto.");
        printMatris(FloydAlgo(M));
        System.out.println("caminho da matris.");
        printMatris(P);
    }

    //link do algoritmo: https://www.youtube.com/watch?v=K6rI0umX-28
    //link explica��o: https://www.youtube.com/watch?v=DfgaBkp02HY
    public static int[][] FloydAlgo(int[][] M) {
        for (int k = 0; k < N; k++) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    // Para manter o controle
                    if (M[i][k] + M[k][j] < M[i][j]) { // se for menor entao o menor caminho � M[i][k] + M[k][j]
                        M[i][j] = M[i][k] + M[k][j];
                        P[i][j] = k;
                    }
                    // Retorna o menor caminho caso M[i][k] + M[k][j] n�o for <  M[i][j]
                    M[i][j] = min(M[i][j], M[i][k] + M[k][j]);
                }
            }
        }
        return M;
    }

    /*se o caminho i for maior que j, entao retorna j, caso contrario retorna i
    isso serve para que o menor caminho seja gravado, diferente do dijkstra que grava
    o caminho maior*/
    public static int min(int i, int j) {
        if (i > j) {
            return j;
        }
        return i;
    }

    public static void printMatris(int[][] Matris) {
        System.out.println("\n\t");
        for (int j = 0; j < N; j++) {   // imprime os indices das colunas

            System.out.print("\t" + "Nó " + j);
        }
        System.out.println();
        for (int j = 0; j < 35; j++) {  //imprime  a linha tracejada
            System.out.print("-");
        }
        System.out.println();
        for (int i = 0; i < N; i++) {   //Imprime a matriz inteira
            System.out.print("Nó " + i + " |\t");
            for (int j = 0; j < N; j++) {
                System.out.print(Matris[i][j]);
                System.out.print("\t");
            }
            System.out.println("\n");
        }
        System.out.println("\n");
    }

}