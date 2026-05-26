
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Classe responsável por gerenciar o laço principal do jogo (Game Loop) e interagir com o usuário via console.
public class Jogo_II {

    private List<EstatisticaAlgoritmo_VI> estatisticasAlgoritmos;
    private Scanner scanner;

    public Jogo_II() {
        this.estatisticasAlgoritmos = new ArrayList<>();
        // Único Scanner instanciado no projeto para evitar conflitos de fechamento de System.in
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println("Bem-vindo ao Space Invaders!");
        System.out.print("Digite o número de linhas do campo de batalha: ");
        int linhas = scanner.nextInt();
        System.out.print("Digite o número de colunas do campo de batalha: ");
        int colunas = scanner.nextInt();

        Nave_IV naveIV = new Nave_IV();

        // Laço de validação: garante que o usuário não digite uma linha fora do mapa
        int linhaInicial = -1;
        while (linhaInicial < 0 || linhaInicial >= linhas) {
            System.out.print("Defina a linha inicial da Nave (0 a " + (linhas - 1) + "): ");
            linhaInicial = scanner.nextInt();
            if (linhaInicial >= 0 && linhaInicial < linhas) {
                naveIV.setLinhaAtual(linhaInicial);
                System.out.println("Nave posicionada com sucesso na linha " + linhaInicial + ".");
            } else {
                System.out.println("Posição inválida. Tente novamente.");
            }
        }

        // Pergunta quantas rodadas o jogador precisa sobreviver para vencer
        System.out.print("Quantas rodadas deseja sobreviver para vencer? ");
        int rodadasParaVencer = scanner.nextInt();

        // Campo passando as dimensões, a nave configurada e o objetivo de rodadas.
        CampoBatalha_III campo = new CampoBatalha_III(linhas, colunas, naveIV, rodadasParaVencer);
        boolean jogoAtivo = true;

        boolean primeiraRodada = true;

        // --- GAME LOOP ---
        // Continua rodando até o usuário sair (0) ou o jogo decidir que acabou (vitória/derrota)
        while (jogoAtivo) {
            campo.exibirCampo();
            System.out.println("\n--- Menu --- (Rodada " + campo.getRodadaAtual() + "/" + campo.getRodadasParaVencer() + ")");
            System.out.println("1. Subir ");
            System.out.println("2. Descer ");
            System.out.println("3. Usar módulo especial");
            System.out.println("4. Atacar linha atual");

            if (primeiraRodada) {
                System.out.println("5. Começar o contra ataque");
            } else {
                System.out.println("5. Continue o ataque");
            }

            System.out.println("6. Mostrar estatísticas dos módulos usados");
            System.out.println("0. Sair do jogo");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    campo.getNave().subirLinha();
                    break;
                case 2:
                    campo.getNave().descerLinha(linhas);
                    break;
                case 3:
                    System.out.println("\n--- Módulos Especiais ---");
                    // O operador ternário ( ? : ) deixa a exibição do (USADO) mais elegante
                    System.out.println("0: Scanner 'SS' " + (campo.getNave().isModuloOrdenacaoUsado(0) ? " (USADO) " : ""));
                    System.out.println("1: Reorganização 'IS' " + (campo.getNave().isModuloOrdenacaoUsado(1) ? " (USADO)" : ""));
                    System.out.println("2: Pulso 'BS' " + (campo.getNave().isModuloOrdenacaoUsado(2) ? " (USADO)" : ""));
                    System.out.println("3: Divisão 'MS' " + (campo.getNave().isModuloOrdenacaoUsado(3) ? " (USADO)" : ""));
                    System.out.println("4: Ataque 'QS' " + (campo.getNave().isModuloOrdenacaoUsado(4) ? " (USADO)" : ""));
                    System.out.println("5: Dominio 'HS' " + (campo.getNave().isModuloOrdenacaoUsado(5) ? " (USADO)" : ""));
                    System.out.print("Escolha o módulo: ");

                    int tipoModulo = scanner.nextInt();
                    EstatisticaAlgoritmo_VI estatistica = campo.usarModuloEspecial(tipoModulo);

                    if (estatistica != null) {
                        estatisticasAlgoritmos.add(estatistica);

                        System.out.println("Módulo de ordenação aplicado com sucesso!");
                    }
                    break;
                case 4:
                    campo.atacarLinhaAtual();
                    System.out.println("Rodada avançada automaticamente após ataque.");
                    jogoAtivo = campo.avancarRodada();

                    primeiraRodada = false;
                    break;
                case 5:
                    jogoAtivo = campo.avancarRodada();

                    primeiraRodada = false;

                    if (!jogoAtivo) {
                        System.out.println("Fim de jogo!");
                    }
                    break;
                case 6:
                    if (estatisticasAlgoritmos.isEmpty()) {
                        System.out.println("Nenhum módulo de ordenação foi usado ainda.");
                    } else {
                        System.out.println("\n--- Estatísticas dos Módulos Usados ---");
                        for (EstatisticaAlgoritmo_VI stats : estatisticasAlgoritmos) {
                            stats.exibirEstatistica();
                        }
                    }
                    break;
                case 0:
                    jogoAtivo = false;
                    System.out.println("Saindo do jogo.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        exibirRelatorioFinal(campo);
        scanner.close(); // Fechamos o scanner apenas no final da execução.
    }

    private void exibirRelatorioFinal(CampoBatalha_III campo) {
        System.out.println("\n--- Relatório Final ---");
        System.out.println("Quantidade total de invasores eliminados: " + campo.getNave().getInimigosEliminados());

        if (estatisticasAlgoritmos.isEmpty()) {
            System.out.println("Nenhum módulo de ordenação foi usado durante o jogo.");
        } else {
            System.out.println("\n--- Ranking de Algoritmos Usados (Mais rápidos primeiro) ---");

            // Aqui chamamos o nosso próprio algoritmo utilitário através de Sobrecarga,
            // respeitando a restrição de não usar bibliotecas prontas!
            Ordenadores_V.bubbleSort(estatisticasAlgoritmos);

            for (EstatisticaAlgoritmo_VI stats : estatisticasAlgoritmos) {
                stats.exibirEstatistica();
            }
        }
    }
}
