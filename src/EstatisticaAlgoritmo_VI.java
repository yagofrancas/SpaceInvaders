
/**
 * Classe responsável por encapsular os dados de desempenho de cada algoritmo.
 * O uso dessa classe evita passar
 * múltiplas variáveis soltas de um lado para o outro.
 */
public class EstatisticaAlgoritmo_VI {

    private String nomeAlgoritmo;
    private int linhaAplicada;
    private long comparacoes;
    private long trocas;
    private long tempoExecucaoNano;

    // Construtor vazio inicializando com valores padrão por segurança
    public EstatisticaAlgoritmo_VI() {
        this.nomeAlgoritmo = "";
        this.linhaAplicada = 0;
        this.comparacoes = 0;
        this.trocas = 0;
        this.tempoExecucaoNano = 0;
    }

    // Construtor completo para facilitar a instanciação lá na classe Ordenadores
    public EstatisticaAlgoritmo_VI(String nomeAlgoritmo, int linhaAplicada, long comparacoes, long trocas, long tempoExecucaoNano) {
        this.nomeAlgoritmo = nomeAlgoritmo;
        this.linhaAplicada = linhaAplicada;
        this.comparacoes = comparacoes;
        this.trocas = trocas;
        this.tempoExecucaoNano = tempoExecucaoNano;
    }

    // --- GETTERS E SETTERS ---
    // Usados para manter o encapsulamento dos atributos privados
    public String getNomeAlgoritmo() {
        return nomeAlgoritmo;
    }

    public int getLinhaAplicada() {
        return linhaAplicada;
    }

    public long getComparacoes() {
        return comparacoes;
    }

    public long getTrocas() {
        return trocas;
    }

    public long getTempoExecucaoNano() {
        return tempoExecucaoNano;
    }

    public void setNomeAlgoritmo(String nomeAlgoritmo) {
        this.nomeAlgoritmo = nomeAlgoritmo;
    }

    public void setLinhaAplicada(int linhaAplicada) {
        this.linhaAplicada = linhaAplicada;
    }

    public void setComparacoes(long comparacoes) {
        this.comparacoes = comparacoes;
    }

    public void setTrocas(long trocas) {
        this.trocas = trocas;
    }

    public void setTempoExecucaoNano(long tempoExecucaoNano) {
        this.tempoExecucaoNano = tempoExecucaoNano;
    }

    //O cálculo para milissegundos é feito aqui na hora de exibir para manter a precisão original (nano) nos atributos da classe.
    public void exibirEstatistica() {
        System.out.println("--- Estatísticas do Algoritmo ---");
        System.out.println("Algoritmo: " + nomeAlgoritmo);
        System.out.println("Linha Aplicada: " + linhaAplicada);
        System.out.println("Comparações: " + comparacoes);
        System.out.println("Trocas: " + trocas);
        System.out.println("Tempo de Execução (ns): " + tempoExecucaoNano);
        System.out.printf("Tempo de Execução (ms): %.4f\n", (tempoExecucaoNano / 1_000_000.0));
        System.out.println("----------------------------------");
    }
}
