
import java.util.List;

public class Ordenadores_V {

    // Move zeros e valores negativos (power-ups) para o início do array,
    // garantindo que não participem da ordenação dos inimigos.
    private static int moverNaoCombatentesParaOInicio(int[] linha, long[] metricas) {
        int k = 0; // Aponta para a posição onde o próximo não-combatente deve ser colocado
        int n = linha.length;
        for (int i = 0; i < n; i++) {
            metricas[0]++; // Contabiliza comparação
            if (linha[i] <= 0) { // Zero (vazio) ou negativo (power-up)
                if (i != k) {
                    // Troca os valores para empurrar o não-combatente para o começo
                    int temp = linha[i];
                    linha[i] = linha[k];
                    linha[k] = temp;
                    metricas[1]++; // Contabiliza a troca
                }
                k++;
            }
        }
        return k; // Retorna onde a ordenação real dos inimigos deve começar
    }

    public static EstatisticaAlgoritmo_VI selectionSort(int[] linha, int indexLinha) {
        long[] metricas = {0, 0}; // Posição 0: comparações | Posição 1: trocas
        long tempoInicio = System.nanoTime();

        int k = moverNaoCombatentesParaOInicio(linha, metricas);
        int n = linha.length;

        for (int i = k; i < n - 1; i++) {
            int indiceMinimo = i; // Assume que o primeiro elemento da parte não ordenada é o menor
            for (int j = i + 1; j < n; j++) {
                metricas[0]++;
                if (linha[j] < linha[indiceMinimo]) {
                    indiceMinimo = j; // Achou alguém menor
                }
            }
            if (indiceMinimo != i) {
                // Ao final da busca, faz o swap (troca)
                int temp = linha[indiceMinimo];
                linha[indiceMinimo] = linha[i];
                linha[i] = temp;
                metricas[1]++;
            }
        }

        long tempoFim = System.nanoTime();
        return new EstatisticaAlgoritmo_VI("Selection Sort", indexLinha, metricas[0], metricas[1], tempoFim - tempoInicio);
    }

    public static EstatisticaAlgoritmo_VI insertionSort(int[] linha, int indexLinha) {
        long[] metricas = {0, 0};
        long tempoInicio = System.nanoTime();

        int k = moverNaoCombatentesParaOInicio(linha, metricas);
        int n = linha.length;

        for (int i = k + 1; i < n; i++) {
            int chave = linha[i];
            int j = i - 1;

            // Arraste para a direita os elementos maiores que a chave
            while (j >= k && linha[j] > chave) {
                metricas[0]++;
                linha[j + 1] = linha[j];
                metricas[1]++;
                j = j - 1;
            }
            if (j >= k) {
                metricas[0]++; // Conta a comparação do while que deu falso
            }
            if (linha[j + 1] != chave) { // Insere a chave na posição correta
                linha[j + 1] = chave;
                metricas[1]++;
            }
        }

        long tempoFim = System.nanoTime();
        return new EstatisticaAlgoritmo_VI("Insertion Sort", indexLinha, metricas[0], metricas[1], tempoFim - tempoInicio);
    }

    public static EstatisticaAlgoritmo_VI bubbleSort(int[] linha, int indexLinha) {
        long[] metricas = {0, 0};
        long tempoInicio = System.nanoTime();

        int k = moverNaoCombatentesParaOInicio(linha, metricas);
        int n = linha.length;

        for (int i = k; i < n - 1; i++) {
            // O limite final do loop diminui a cada iteração (n - 1 - (i - k))
            // porque o maior elemento já "borbulhou" para o final.
            for (int j = k; j < n - 1 - (i - k); j++) {
                metricas[0]++;
                if (linha[j] > linha[j + 1]) {
                    int temp = linha[j];
                    linha[j] = linha[j + 1];
                    linha[j + 1] = temp;
                    metricas[1]++;
                }
            }
        }

        long tempoFim = System.nanoTime();
        return new EstatisticaAlgoritmo_VI("Bubble Sort", indexLinha, metricas[0], metricas[1], tempoFim - tempoInicio);
    }

    public static void bubbleSort(List<EstatisticaAlgoritmo_VI> lista) {
        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                EstatisticaAlgoritmo_VI atual = lista.get(j);
                EstatisticaAlgoritmo_VI proximo = lista.get(j + 1);

                // Compara o tempo de execução (do menor para o maior)
                if (atual.getTempoExecucaoNano() > proximo.getTempoExecucaoNano()) {
                    // Realiza a troca (swap) diretamente na lista
                    lista.set(j, proximo);
                    lista.set(j + 1, atual);
                }
            }
        }
    }

    /**
     * MERGE SORT (Função Principal): A "Divisão de Esquadrilha". Algoritmo O(N
     * log N) que divide o array recursivamente pela metade e depois intercala
     * (merge) ordenando as partes.
     */
    public static EstatisticaAlgoritmo_VI mergeSort(int[] linha, int indexLinha) {
        long[] metricas = {0, 0};
        long tempoInicio = System.nanoTime();

        int k = moverNaoCombatentesParaOInicio(linha, metricas);
        int n = linha.length;

        if (k < n) {
            mergeSortAux(linha, k, n - 1, metricas);
        }

        long tempoFim = System.nanoTime();
        return new EstatisticaAlgoritmo_VI("Merge Sort", indexLinha, metricas[0], metricas[1], tempoFim - tempoInicio);
    }

    // Parte recursiva do Merge Sort (Divide)
    private static void mergeSortAux(int[] linha, int inicio, int fim, long[] metrics) {
        if (inicio < fim) {
            int meio = (inicio + fim) / 2;
            mergeSortAux(linha, inicio, meio, metrics);
            mergeSortAux(linha, meio + 1, fim, metrics);
            merge(linha, inicio, meio, fim, metrics); // Conquista (junta as metades)
        }
    }

    // Parte de união do Merge Sort (Conquista)
    private static void merge(int[] linha, int inicio, int meio, int fim, long[] metrics) {
        int n1 = meio - inicio + 1;
        int n2 = fim - meio;

        // Arrays temporários para as duas metades
        int[] L = new int[n1];
        int[] R = new int[n2];

        // Copia os dados
        for (int i = 0; i < n1; ++i) {
            L[i] = linha[inicio + i];
        }
        for (int j = 0; j < n2; ++j) {
            R[j] = linha[meio + 1 + j];
        }

        int i = 0, j = 0, k = inicio;

        // Intercala comparando elemento a elemento
        while (i < n1 && j < n2) {
            metrics[0]++;
            if (L[i] <= R[j]) {
                linha[k] = L[i];
                i++;
            } else {
                linha[k] = R[j];
                j++;
                metrics[1]++; // Consideramos como troca de posição lógica
            }
            k++;
        }

        // Esvazia os arrays caso tenha sobrado algum elemento
        while (i < n1) {
            linha[k] = L[i];
            i++;
            k++;
        }
        while (j < n2) {
            linha[k] = R[j];
            j++;
            k++;
        }
    }

    /**
     * QUICK SORT (Função Principal): O "Ataque Hiperluz". Escolhe um pivô, joga
     * menores para um lado, maiores para o outro e repete recursivamente. Muito
     * rápido em média.
     */
    public static EstatisticaAlgoritmo_VI quickSort(int[] linha, int indexLinha) {
        long[] metricas = {0, 0};
        long tempoInicio = System.nanoTime();

        int k = moverNaoCombatentesParaOInicio(linha, metricas);
        int n = linha.length;

        if (k < n) {
            quickSortAux(linha, k, n - 1, metricas);
        }

        long tempoFim = System.nanoTime();
        return new EstatisticaAlgoritmo_VI("Quick Sort", indexLinha, metricas[0], metricas[1], tempoFim - tempoInicio);
    }

    // Parte recursiva do Quick Sort
    private static void quickSortAux(int[] linha, int baixo, int alto, long[] metrics) {
        if (baixo < alto) {
            // Particiona o array e retorna o índice onde o pivô parou
            int pi = particionar(linha, baixo, alto, metrics);
            quickSortAux(linha, baixo, pi - 1, metrics);
            quickSortAux(linha, pi + 1, alto, metrics);
        }
    }

    // Lógica de particionamento do Quick Sort
    private static int particionar(int[] linha, int baixo, int alto, long[] metrics) {
        int pivo = linha[alto]; // Escolhemos o último elemento como pivô
        int i = (baixo - 1);

        for (int j = baixo; j < alto; j++) {
            metrics[0]++;
            if (linha[j] < pivo) {
                i++;
                // Troca linha[i] com linha[j] para empurrar quem é menor que o pivô
                int temp = linha[i];
                linha[i] = linha[j];
                linha[j] = temp;
                metrics[1]++;
            }
        }

        // Coloca o pivô no meio do tiroteio (entre os menores e os maiores)
        int temp = linha[i + 1];
        linha[i + 1] = linha[alto];
        linha[alto] = temp;
        metrics[1]++;

        return i + 1;
    }

    /**
     * HEAP SORT (Função Principal): O "Domínio Orbital". Modela o array como
     * uma árvore binária (Heap Max) e repetidamente extrai o maior elemento
     * (raiz) colocando-o no fim do array.
     */
    public static EstatisticaAlgoritmo_VI heapSort(int[] linha, int indexLinha) {
        long[] metricas = {0, 0};
        long tempoInicio = System.nanoTime();

        int k = moverNaoCombatentesParaOInicio(linha, metricas);
        int n = linha.length;

        if (k < n) {
            int tamanhoHeap = n - k;

            // Passo 1: Constrói a Max-Heap estruturando o array inteiro
            for (int i = tamanhoHeap / 2 - 1; i >= 0; i--) {
                heapify(linha, k, tamanhoHeap, i, metricas);
            }

            // Passo 2: Extrai os elementos um a um
            for (int i = tamanhoHeap - 1; i > 0; i--) {
                // Move a raiz atual (maior elemento) para o final
                int temp = linha[k];
                linha[k] = linha[k + i];
                linha[k + i] = temp;
                metricas[1]++;

                // Reconstrói a árvore chamando heapify na heap reduzida
                heapify(linha, k, i, 0, metricas);
            }
        }

        long tempoFim = System.nanoTime();
        return new EstatisticaAlgoritmo_VI("Heap Sort", indexLinha, metricas[0], metricas[1], tempoFim - tempoInicio);
    }

    // Função que garante a propriedade da Max-Heap (o pai é maior que os filhos)
    private static void heapify(int[] linha, int offset, int tamanhoHeap, int i, long[] metrics) {
        int maior = i; // Inicializa a raiz atual como maior
        int esquerda = 2 * i + 1; // Posição do filho à esquerda
        int direita = 2 * i + 2;  // Posição do filho à direita

        // Compara com o filho da esquerda
        if (esquerda < tamanhoHeap) {
            metrics[0]++;
            if (linha[offset + esquerda] > linha[offset + maior]) {
                maior = esquerda;
            }
        }

        // Compara com o filho da direita
        if (direita < tamanhoHeap) {
            metrics[0]++;
            if (linha[offset + direita] > linha[offset + maior]) {
                maior = direita;
            }
        }

        // Se o maior não for mais a raiz, faz a troca e propaga para baixo (recursivo)
        if (maior != i) {
            int troca = linha[offset + i];
            linha[offset + i] = linha[offset + maior];
            linha[offset + maior] = troca;
            metrics[1]++;

            heapify(linha, offset, tamanhoHeap, maior, metrics);
        }
    }
}
