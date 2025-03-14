package br.com.joinville.mapa;

import java.util.ArrayList;
import java.util.List;

public class AreaVerdeRepository {
    private static List<AreaVerde> AREAS_VERDES = new ArrayList<>();
    private static int contadorId = 1;

    public static void adicionarAreaVerde(String nome, String tipoVegetacao, String horarioFuncionamento) {
        AREAS_VERDES.add(new AreaVerde(contadorId++, nome, tipoVegetacao, horarioFuncionamento));
    }

    public static List<AreaVerde> listarAreasVerdes() {
        return AREAS_VERDES;
    }
}
