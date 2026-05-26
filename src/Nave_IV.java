
/**
 * Representa a entidade Nave controlada pelo jogador.
 * Aqui controlamos onde ela está, quantos inimigos abateu e quais módulos já usou.
 */
public class Nave_IV {

    private int linhaAtual;
    private int inimigosEliminados;
    private boolean[] modulosOrdenacaoUsados;

    public Nave_IV() {
        this.linhaAtual = 0;
        this.inimigosEliminados = 0;
        // Vetor de 6 posições (uma para cada algoritmo).
        // Em Java, ele já nasce preenchido com 'false', significando que nenhum foi usado ainda.
        this.modulosOrdenacaoUsados = new boolean[6];
    }

    public int getLinhaAtual() {
        return linhaAtual;
    }

    public int getInimigosEliminados() {
        return inimigosEliminados;
    }

    public boolean[] getModulosOrdenacaoUsados() {
        return modulosOrdenacaoUsados;
    }

    public void setLinhaAtual(int linhaAtual) {
        this.linhaAtual = linhaAtual;
    }

    public void setInimigosEliminados(int inimigosEliminados) {
        this.inimigosEliminados = inimigosEliminados;
    }

    // As funções de movimentação garantem que a nave não tente ir para fora da matriz.
    // Isso evita o erro 'ArrayIndexOutOfBoundsException'.
    public void subirLinha() {
        if (this.linhaAtual > 0) {
            this.linhaAtual--;
        } else {
            System.out.println("A nave já está no limite superior!");
        }
    }

    public void descerLinha(int maxLinhas) {
        if (this.linhaAtual < maxLinhas - 1) {
            this.linhaAtual++;
        } else {
            System.out.println("A nave já está no limite inferior!");
        }
    }

    // Marca um poder/algoritmo como usado para que não seja acionado novamente na partida
    public void usarModuloOrdenacao(int indiceDoModulo) {
        if (indiceDoModulo >= 0 && indiceDoModulo < modulosOrdenacaoUsados.length) {
            this.modulosOrdenacaoUsados[indiceDoModulo] = true;
        }
    }

    // Verifica se um poder específico já foi gasto
    public boolean isModuloOrdenacaoUsado(int indiceDoModulo) {
        if (indiceDoModulo >= 0 && indiceDoModulo < modulosOrdenacaoUsados.length) {
            return this.modulosOrdenacaoUsados[indiceDoModulo];
        }
        return false;
    }

    public void RestaurarModulosUsados(int qtdChances) {
        java.util.List<Integer> indicesUsados = new java.util.ArrayList<>();
        for (int i = 0; i < modulosOrdenacaoUsados.length; i++) {
            if (modulosOrdenacaoUsados[i]) {
                indicesUsados.add(i);
            }
        }

        if (indicesUsados.isEmpty()) {
            System.out.println("Nenhum módulo foi gasto ainda para ser restaurado. Macaco");
            return;
        }

        java.util.Random rand = new java.util.Random();
        int restaurados = 0;

        while (restaurados < qtdChances && !indicesUsados.isEmpty()) {
            int idxAleatorio = rand.nextInt(indicesUsados.size());
            int moduloParaRestaurar = indicesUsados.remove(idxAleatorio);

            modulosOrdenacaoUsados[moduloParaRestaurar] = false;
            restaurados++;

            String nomeModulo = "";
            switch (moduloParaRestaurar) {
                case 0:
                    nomeModulo = "Scanner 'SS'";
                    break;
                case 1:
                    nomeModulo = "Reorganização 'IS'";
                    break;
                case 2:
                    nomeModulo = "Pulso 'BS'";
                    break;
                case 3:
                    nomeModulo = "Divisão 'MS'";
                    break;
                case 4:
                    nomeModulo = "Ataque 'QS'";
                    break;
                case 5:
                    nomeModulo = "Dominio 'HS'";
                    break;
            }
            System.out.println("\u001B[35m[POWER-UP ACTIVATED] O módulo <" + nomeModulo + "> foi recarregado e pode ser reutilizado!\u001B[0m");
        }
    }

}
