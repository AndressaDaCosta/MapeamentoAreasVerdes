package br.com.joinville.mapa;

public class TesteAreaVerde {
    public static void main(String[] args) {
        System.out.println("🔍 Teste do Módulo Área Verde\n");

        // Adicionando áreas verdes
        AreaVerdeRepository.adicionarAreaVerde(
                "Parque Expoville",
                "Árvores e gramado",
                "06:00 - 22:00"
        );
        AreaVerdeRepository.adicionarAreaVerde(
                "Parque Porta do Mar",
                "Gramado e arbustos",
                "07:00 - 20:00"
        );
        AreaVerdeRepository.adicionarAreaVerde(
                "Parque Morro do Finder",
                "Floresta nativa",
                "06:00 - 18:00"
        );

        System.out.println("🌿 Lista de Áreas Verdes:");
        for (AreaVerde av : AreaVerdeRepository.listarAreasVerdes()) {
            System.out.println(av);
        }
        System.out.println("\n✅ Teste Finalizado!");
    }
}
