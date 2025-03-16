package br.com.joinville.mapa;

import java.util.ArrayList;
import java.util.List;

public class LocalizacaoRepository {
    private static List<Localizacao> LOCALIZACOES = new ArrayList<>();
    private static int contadorId = 1;

    public static void adicionarLocalizacao(double latitude, double longitude, int idAreaVerde) {
        LOCALIZACOES.add(new Localizacao(contadorId++, latitude, longitude, idAreaVerde));
    }

    public static List<Localizacao> listarLocalizacoes() {
        return LOCALIZACOES;
    }
}
