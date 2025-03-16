package br.com.joinville.mapa;

public class TesteLocalizacao {
    public static void main(String[] args) {
        System.out.println("🔍 Teste do Módulo Localização\n");

        // Adicionando localizações
        LocalizacaoRepository.adicionarLocalizacao(-26.3051, -48.8461, 1);
        LocalizacaoRepository.adicionarLocalizacao(-26.3121, -48.8610, 2);
        LocalizacaoRepository.adicionarLocalizacao(-26.2925, -48.8775, 3);

        // Listando todas as localizações cadastradas
        System.out.println("📍 Lista de Localizações:");
        for (Localizacao loc : LocalizacaoRepository.listarLocalizacoes()) {
            System.out.println(loc);
        }

        // Teste: Busca de localização específica
        System.out.println("\n✅ Teste Finalizado!");
    }
}
