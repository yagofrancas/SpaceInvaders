
import java.util.Random;

// O CampoBatalha é o centro da regra de negócios do jogo.
// Ele gerencia a matriz onde os inimigos ficam e as condições de vitória/derrota.
public class CampoBatalha_III {

    private int[][] matriz;
    private Nave_IV naveIV;
    // Sistema de rodadas para condição de vitória
    private int rodadaAtual;
    private final int rodadasParaVencer;
    private boolean vitoria; // Armazena se o jogador venceu ou perdeu
    // Sistema de aleatoriedade real para power-ups:
    // Após um power-up surgir, há um cooldown mínimo de rodadas antes do próximo.
    // Depois do cooldown, a probabilidade vai crescendo a cada rodada até estourar.
    private int rodadasDesdeUltimoPowerUp;
    private static final int COOLDOWN_POWERUP = 3;       // rodadas mínimas entre power-ups
    private static final int CHANCE_BASE_POWERUP = 5;     // % inicial após o cooldown
    private static final int INCREMENTO_CHANCE = 5;       // % a mais por rodada de espera
    private static final int CHANCE_MAX_POWERUP = 30;     // teto máximo de %
    String ANSI_Reset = "\u001B[0m";
    String ANSI_Negrito = "\u001B[1m";

    public CampoBatalha_III(int linhas, int colunas, Nave_IV naveIV, int rodadasParaVencer) {
        this.matriz = new int[linhas][colunas];
        this.naveIV = naveIV;
        this.rodadaAtual = 0;
        this.rodadasParaVencer = rodadasParaVencer;

        // Inicializa o campo de batalha vazio (tudo com 0)
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                this.matriz[i][j] = 0;
            }
        }

        this.rodadasDesdeUltimoPowerUp = 0;
        this.vitoria = false;

        System.out.println(ANSI_Negrito + "Alerta do Setor /!/ : Inimigos se aproximando em sua direção!! " + ANSI_Reset);
        System.out.println("Objetivo: Sobreviva " + rodadasParaVencer + " rodadas para vencer!");
    }

    public int[][] getMatriz() {
        return matriz;
    }

    public Nave_IV getNave() {
        return naveIV;
    }

    public int getRodadaAtual() {
        return rodadaAtual;
    }

    public int getRodadasParaVencer() {
        return rodadasParaVencer;
    }

    public boolean isVitoria() {
        return vitoria;
    }

    public void setMatriz(int[][] matriz) {
        this.matriz = matriz;
    }

    public void setNave(Nave_IV naveIV) {
        this.naveIV = naveIV;
    }

    public int contarInvasores() {
        return contarInimigosRecursivo(0, 0);
    }


    private int contarInimigosRecursivo(int linha, int coluna) {

        // CASO BASE 1: Se a linha atual for maior ou igual ao total de linhas,
        if (linha >= matriz.length) {
            return 0;
        }

        // CASO BASE 2 (Quebra de linha): Se a coluna estourar o limite da linha atual,
        // chamamos a recursão para a PRIMEIRA coluna (0) da PRÓXIMA linha (linha + 1).
        if (coluna >= matriz[linha].length) {
            return contarInimigosRecursivo(linha + 1, 0);
        }

        // PASSO RECURSIVO: Conta se há invasor na célula atual
        int contador = (matriz[linha][coluna] > 0) ? 1 : 0;

        // Soma o resultado da célula atual com o resto da matriz
        return contador + contarInimigosRecursivo(linha, coluna + 1);
    }

    // Desenha a matriz na tela do console de forma alinhada.
    public void exibirCampo() {

        String ANSI_Laranja = "\u001B[33m";
        String ANSI_Roxo = "\u001B[35m";

        System.out.println("--- Campo de Batalha ---  |  Rodada: " + rodadaAtual + "/" + rodadasParaVencer + "  |  Ativos: " + contarInvasores() + "  |  Eliminados: " + naveIV.getInimigosEliminados());
        System.out.println("Legenda: Números = Inimigos (vida) | " + ANSI_Roxo + "[\u2605]" + ANSI_Reset + " = Power-Up (ataque para coletar!) | 0 = Vazio");
        System.out.println();
        for (int i = 0; i < matriz.length; i++) {
            StringBuilder linhaFormatada = new StringBuilder();
            linhaFormatada.append("[");

            for (int j = 0; j < matriz[i].length; j++) {

                if (matriz[i][j] < 0) {
                    // Power-Up: exibe como [★] em roxo, ocupando o mesmo espaço de 3 caracteres
                    linhaFormatada.append(ANSI_Roxo).append("[\u2605]").append(ANSI_Reset);
                } else if (matriz[i][j] > 50) {
                    // Inimigo forte (vida > 50): destaque em laranja
                    // String.format("%3d") faz os numeros ocuparem espaços de 3, garantindo que as colunas fiquem retinhas, mesmo misturando números de 1 e 2 dígitos.
                    linhaFormatada.append(ANSI_Laranja).append(String.format("%3d", matriz[i][j])).append(ANSI_Reset);
                } else {
                    // Inimigo normal ou espaço vazio (0)
                    linhaFormatada.append(String.format("%3d", matriz[i][j]));
                }
                if (j < matriz[i].length - 1) {
                    linhaFormatada.append(",");
                }
            }
            linhaFormatada.append(" ]");

            // Indica onde a nave está apontada visualmente
            if (i == naveIV.getLinhaAtual()) {
                System.out.println("Nave ->" + linhaFormatada.toString());
            } else {
                System.out.println("       " + linhaFormatada.toString());
            }
        }
        System.out.println("------------------------");
    }

    public void atacarLinhaAtual() {
        int linhaDoAtaque = naveIV.getLinhaAtual();

        if (linhaDoAtaque >= 0 && linhaDoAtaque < matriz.length) {
            for (int j = 0; j < matriz[linhaDoAtaque].length; j++) {

                if (matriz[linhaDoAtaque][j] != 0) {

                    if (matriz[linhaDoAtaque][j] < 0) {
                        System.out.println("Caixinha de Power_Chance coletada na linha " + linhaDoAtaque + ", coluna " + j + "!");

                        // Sistema de recompensa com 3 tiers de probabilidade:
                        // 70% = recarrega 1 módulo | 25% = recarrega 2 módulos | 5% = recarrega TODOS
                        Random random = new Random();
                        int sorte = random.nextInt(100);
                        int chancesConcedidas;

                        if (sorte < 5) { // 5% de chance - JACKPOT: recarrega TODOS os módulos
                            chancesConcedidas = 6; // Total de módulos existentes
                            System.out.println("\u001B[33m\u001B[1m\u2605\u2605\u2605 JACKPOT! Todos os módulos foram recarregados! \u2605\u2605\u2605\u001B[0m");
                        } else if (sorte < 30) { // 25% de chance - recarrega 2 módulos
                            chancesConcedidas = 2;
                            System.out.println("\u001B[35m\u2605\u2605 Sorte! 2 módulos serão recarregados! \u2605\u2605\u001B[0m");
                        } else { // 70% de chance - recarrega 1 módulo
                            chancesConcedidas = 1;
                            System.out.println("\u001B[35m\u2605 1 módulo será recarregado. \u2605\u001B[0m");
                        }

                        naveIV.RestaurarModulosUsados(chancesConcedidas);

                        matriz[linhaDoAtaque][j] = 0; // Remove a caixinha do mapa após coletar
                        return;
                    }
                }

                if (matriz[linhaDoAtaque][j] > 0) {

                    matriz[linhaDoAtaque][j] -= 15;

                    if (matriz[linhaDoAtaque][j] <= 0) {
                        matriz[linhaDoAtaque][j] = 0; // Se a vida zerar, volta a ser espaço vazio (0)
                        naveIV.setInimigosEliminados(naveIV.getInimigosEliminados() + 1);
                        System.out.println("Invasor eliminado na linha " + linhaDoAtaque + ", coluna " + j + "!");
                    } else {
                        System.out.println("Invasor na linha " + linhaDoAtaque + ", coluna " + j + " atingido! Vida restante: " + matriz[linhaDoAtaque][j]);
                    }
                    return; // O break sai do laço para não atirar varando todos os inimigos da linha
                }
            }
            System.out.println("Nenhum invasor encontrado na linha " + linhaDoAtaque + " para atacar.");
        }
    }

    // Aciona a classe utilitária estática Ordenadores baseado na escolha do jogador.
    public EstatisticaAlgoritmo_VI usarModuloEspecial(int tipoModulo) {
        if (tipoModulo < 0 || tipoModulo >= 6) {
            System.out.println("Tipo de módulo inválido.");
            return null;
        }

        if (naveIV.isModuloOrdenacaoUsado(tipoModulo)) {
            System.out.println("Módulo de ordenação " + tipoModulo + " já foi usado. Cada módulo tem uso único!");
            return null;
        }

        int linhaAtualNave = naveIV.getLinhaAtual();
        int[] linhaParaOrdenar = matriz[linhaAtualNave];
        EstatisticaAlgoritmo_VI estatistica = null;

        // O switch funciona como um roteador para os métodos estáticos da classe de algoritmos
        switch (tipoModulo) {
            case 0:
                estatistica = Ordenadores_V.selectionSort(linhaParaOrdenar, linhaAtualNave);
                break;
            case 1:
                estatistica = Ordenadores_V.insertionSort(linhaParaOrdenar, linhaAtualNave);
                break;
            case 2:
                estatistica = Ordenadores_V.bubbleSort(linhaParaOrdenar, linhaAtualNave);
                break;
            case 3:
                estatistica = Ordenadores_V.mergeSort(linhaParaOrdenar, linhaAtualNave);
                break;
            case 4:
                estatistica = Ordenadores_V.quickSort(linhaParaOrdenar, linhaAtualNave);
                break;
            case 5:
                estatistica = Ordenadores_V.heapSort(linhaParaOrdenar, linhaAtualNave);
                break;
            default:
                estatistica = null;
                break;
        }

        if (estatistica != null) {
            naveIV.usarModuloOrdenacao(tipoModulo);
            System.out.println("Módulo de ordenação " + estatistica.getNomeAlgoritmo() + " aplicado na linha " + linhaAtualNave + ".");
        }
        return estatistica;
    }

    // Calcula a chance (%) de um power-up surgir nesta rodada, usando distribuição progressiva
    private int calcularChancePowerUp() {
        if (rodadasDesdeUltimoPowerUp < COOLDOWN_POWERUP) {
            return 0; // Ainda está no cooldown, sem chance nenhuma
        }
        // Após o cooldown, chance cresce linearmente a cada rodada extra de espera
        int rodadasAposCooldown = rodadasDesdeUltimoPowerUp - COOLDOWN_POWERUP;
        int chance = CHANCE_BASE_POWERUP + (rodadasAposCooldown * INCREMENTO_CHANCE);
        return Math.min(chance, CHANCE_MAX_POWERUP); // Limita ao teto máximo
    }

    //Aqui é onde os inimigos vão se movendo pela matriz e lógico, avançando rodadas.
    public boolean avancarRodada() {
        // CHECAGEM DE VITÓRIA: se todos os invasores foram eliminados (somente após o jogo ter iniciado)
        if (rodadaAtual > 0 && contarInvasores() == 0) {
            System.out.println(ANSI_Negrito + "\n=== VITÓRIA ABSOLUTA! ===" + ANSI_Reset);
            System.out.println("Todos os invasores foram completamente eliminados do setor!");
            this.vitoria = true;
            return false; // Termina o jogo
        }

        // 1. CHECAGEM GLOBAL DE DERROTA INICIAL: se qualquer inimigo ja esta na coluna 0 antes do avanco, derrota imediata
        for (int i = 0; i < matriz.length; i++) {
            if (matriz[i][0] >= 1) {
                System.out.println("Derrota! O inimigo superou as defesas da sua base!");
                System.out.println(ANSI_Negrito + "Nossas linhas de defesa caíram. O setor foi perdido!" + ANSI_Reset);
                this.vitoria = false;
                return false;
            }
        }

        Random random = new Random();
        boolean powerUpSurgiuNestaRodada = false;

        // Processa o avanço linha por linha
        for (int i = 0; i < matriz.length; i++) {
            int ultimaColuna = matriz[i].length - 1;

            // 50% de chance de ativar a linha nesta rodada
            if (random.nextBoolean()) {

                // 20% de chance dos inimigos desta linha ficarem paralisados (não se movem)
                if (random.nextInt(100) < 20) {
                    System.out.println("\u001B[36mInvasores na linha " + i + " ficaram paralisados nesta rodada!\u001B[0m");
                    continue; // Pula para a próxima linha sem mover nem spawnar
                }

                // Desloca todos os inimigos dessa linha um bloco para a esquerda
                for (int j = 0; j < ultimaColuna; j++) {
                    matriz[i][j] = matriz[i][j + 1];
                }

                // Inimigos surgem infinitamente, sem limite
                int novaEnergia = random.nextInt(90) + 10; // Energia entre 10 e 99

                // Sistema de power-up com aleatoriedade real e distribuída
                int chancePowerUp = calcularChancePowerUp();
                if (chancePowerUp > 0 && random.nextInt(100) < chancePowerUp) {
                    matriz[i][ultimaColuna] = -novaEnergia; // Valor negativo indica power-up
                    System.out.println("\u001B[35m\u2605 POWER-UP! Caixinha [\u2605] apareceu na linha " + i + ", coluna " + ultimaColuna + "! Ataque-a para coletar!\u001B[0m");
                    powerUpSurgiuNestaRodada = true;
                } else {
                    matriz[i][ultimaColuna] = novaEnergia; //Inimigo normal
                    System.out.println("Novo invasor surgiu na linha " + i + ", coluna " + ultimaColuna + " com energia " + matriz[i][ultimaColuna] + ".");
                }
            }
        }

        // Atualiza o contador de rodadas para o sistema de power-up
        if (powerUpSurgiuNestaRodada) {
            rodadasDesdeUltimoPowerUp = 0; // Reseta o cooldown
        } else {
            rodadasDesdeUltimoPowerUp++;
        }

        // Incrementa o contador de rodadas
        rodadaAtual++;

        // 2. CHECAGEM GLOBAL DE DERROTA FINAL: se algum inimigo chegou na coluna 0 apos o movimento desta rodada
        for (int i = 0; i < matriz.length; i++) {
            if (matriz[i][0] >= 1) {
                System.out.println("Derrota! O inimigo superou as defesas da sua base!");
                System.out.println(ANSI_Negrito + "Nossas linhas de defesa caíram. O setor foi perdido!" + ANSI_Reset);
                this.vitoria = false;
                return false;
            }
        }


        // Condição de Vitória: o jogador sobreviveu o número de rodadas necessário!
        if (rodadaAtual >= rodadasParaVencer) {
            System.out.println(ANSI_Negrito + "\n=== GRANDE VITÓRIA! ==="  + ANSI_Reset);
            System.out.println("Você sobreviveu todas as " + rodadasParaVencer + " rodadas!");
            System.out.println(ANSI_Negrito + "A humanidade está salva graças às suas táticas de defesa!" + ANSI_Reset);
            this.vitoria = true;
            return false;
        }

        // Se chegou até aqui, o jogo segue
        return true;
    }
}
