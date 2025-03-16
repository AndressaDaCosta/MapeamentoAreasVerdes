package br.com.joinville.mapa;

public class TesteAvaliacao {
    public static void main(String[] args) {
        System.out.println("🔍 Teste do Módulo Avaliação\n");

        // Adicionando avaliações para diferentes áreas verdes
        AvaliacaoRepository.adicionarAvaliacao(1, 5, 4, 3, 5, 4);
        AvaliacaoRepository.adicionarAvaliacao(2, 3, 3, 4, 2, 5);
        AvaliacaoRepository.adicionarAvaliacao(3, 4, 5, 5, 4, 4);

        // Listando todas as avaliações cadastradas
        System.out.println("📊 Lista de Avaliações:");
        for (Avaliacao av : AvaliacaoRepository.listarAvaliacoes()) {
            System.out.println(av);
        }

        // Teste: Cálculo da média das avaliações
        System.out.println("\n🎯 Testando cálculo da média de uma avaliação...");
        Avaliacao primeiraAvaliacao = AvaliacaoRepository.listarAvaliacoes().get(0);
        System.out.printf("Área Verde ID %d - Média das Notas: %.2f\n",
                primeiraAvaliacao.getIdAreaVerde(), primeiraAvaliacao.calcularMedia());

        System.out.println("\n✅ Teste Finalizado!");
    }
}
