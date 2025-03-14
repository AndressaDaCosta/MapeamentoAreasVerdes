package br.com.joinville.mapa;

import java.util.ArrayList;
import java.util.List;

public class AvaliacaoRepository {
    private static List<Avaliacao> AVALIACOES = new ArrayList<>();
    private static int contadorId = 1;

    public static void adicionarAvaliacao(int idAreaVerde, int qtdArvores, int qualidadeAr, int poluicaoSonora,
            int coletaResiduos, int transportePublico) {
        AVALIACOES.add(new Avaliacao(contadorId++, idAreaVerde, qtdArvores, qualidadeAr, poluicaoSonora, coletaResiduos,
                transportePublico));
    }

    public static List<Avaliacao> listarAvaliacoes() {
        return AVALIACOES;
    }
}
