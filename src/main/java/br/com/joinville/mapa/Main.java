package br.com.joinville.mapa;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n🌳 Bem-vindo ao Mapeamento de Áreas Verdes de Joinville! 🌿");
            System.out.println("Digite a opção que deseja acessar:");
            System.out.println("1- Listar Áreas Verdes");
            System.out.println("2- Avaliar Área Verde");
            System.out.println("3- Ver detalhe de uma Área Verde");
            System.out.println("4- Cadastrar nova Área Verde");
            System.out.println("5- Listar Localizações");
            System.out.println("0- Sair");
            System.out.print("Opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    listarAreasVerdes();
                    break;
                case 2:
                    avaliarAreaVerde(scanner);
                    break;
                case 3:
                    verDetalheAreaVerde(scanner);
                    break;
                case 4:
                    cadastrarAreaVerde(scanner);
                    break;
                case 5:
                    listarLocalizacoes();
                    break;
                case 0:
                    System.out.println("✅ Saindo do sistema...");
                    break;
                default:
                    System.out.println("⚠️ Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);

        scanner.close();
    }

    // Método para listar todas as áreas verdes
    private static void listarAreasVerdes() {
        List<AreaVerde> areas = AreaVerdeRepository.listarAreasVerdes();
        if (areas.isEmpty()) {
            System.out.println("⚠️ Nenhuma área verde cadastrada.");
        } else {
            System.out.println("\n📍 Áreas Verdes Cadastradas:");
            for (AreaVerde area : areas) {
                System.out.println(area);
            }
        }
    }

    // Método para avaliar uma área verde
    private static void avaliarAreaVerde(Scanner scanner) {
        System.out.print("\n🔍 Digite o ID da área verde que deseja avaliar: ");
        int idArea = scanner.nextInt();

        List<AreaVerde> areas = AreaVerdeRepository.listarAreasVerdes();
        boolean existe = areas.stream().anyMatch(a -> a.getId() == idArea);

        if (!existe) {
            System.out.println("⚠️ Área verde não encontrada.");
            return;
        }

        System.out.println("\n🌿 Avaliação da Área Verde - Notas de 1 a 5:");
        System.out.print("Quantidade de árvores: ");
        int qtdArvores = scanner.nextInt();
        System.out.print("Qualidade do ar: ");
        int qualidadeAr = scanner.nextInt();
        System.out.print("Ausência de poluição sonora: ");
        int poluicaoSonora = scanner.nextInt();
        System.out.print("Coleta de resíduos: ");
        int coletaResiduos = scanner.nextInt();
        System.out.print("Facilidade de transporte público: ");
        int transportePublico = scanner.nextInt();

        AvaliacaoRepository.adicionarAvaliacao(idArea, qtdArvores, qualidadeAr, poluicaoSonora, coletaResiduos,
                transportePublico);
        System.out.println("✅ Avaliação registrada com sucesso!");
    }

    // Método para visualizar detalhes de uma área verde
    private static void verDetalheAreaVerde(Scanner scanner) {
        System.out.print("\n🔍 Digite o ID da área verde que deseja ver os detalhes: ");
        int idArea = scanner.nextInt();

        List<AreaVerde> areas = AreaVerdeRepository.listarAreasVerdes();
        AreaVerde areaSelecionada = null;

        for (AreaVerde area : areas) {
            if (area.getId() == idArea) {
                areaSelecionada = area;
                break;
            }
        }

        if (areaSelecionada == null) {
            System.out.println("⚠️ Área verde não encontrada.");
            return;
        }

        System.out.println("\n📍 Detalhes da Área Verde:");
        System.out.println("ID: " + areaSelecionada.getId());
        System.out.println("Nome: " + areaSelecionada.getNome());
        System.out.println("Tipo de Vegetação: " + areaSelecionada.getTipoVegetacao());
        System.out.println("Horário de Funcionamento: " + areaSelecionada.getHorarioFuncionamento());

        // Buscar avaliações dessa área verde
        List<Avaliacao> avaliacoes = AvaliacaoRepository.listarAvaliacoes();
        double mediaAvaliacoes = avaliacoes.stream()
                .filter(a -> a.getIdAreaVerde() == idArea)
                .mapToDouble(Avaliacao::calcularMedia)
                .average()
                .orElse(0.0);

        System.out.printf("Média das Avaliações: %.2f\n", mediaAvaliacoes);
    }

    // Método para cadastrar uma nova área verde
    private static void cadastrarAreaVerde(Scanner scanner) {
        System.out.println("\n📍 Cadastro de Nova Área Verde:");
        System.out.print("Nome da área verde: ");
        String nome = scanner.nextLine();
        System.out.print("Tipo de vegetação (Árvores, Arbustos, Gramado, etc.): ");
        String tipoVegetacao = scanner.nextLine();
        System.out.print("Horário de funcionamento: ");
        String horarioFuncionamento = scanner.nextLine();
        System.out.print("Latitude: ");
        double latitude = scanner.nextDouble();
        System.out.print("Longitude: ");
        double longitude = scanner.nextDouble();

        AreaVerdeRepository.adicionarAreaVerde(nome, tipoVegetacao, horarioFuncionamento);
        int idNovaArea = AreaVerdeRepository.listarAreasVerdes().size();
        LocalizacaoRepository.adicionarLocalizacao(latitude, longitude, idNovaArea);

        System.out.println("✅ Área verde cadastrada com sucesso!");
    }

    // Método para listar todas as localizações
    private static void listarLocalizacoes() {
        List<Localizacao> localizacoes = LocalizacaoRepository.listarLocalizacoes();
        if (localizacoes.isEmpty()) {
            System.out.println("⚠️ Nenhuma localização cadastrada.");
        } else {
            System.out.println("\n📍 Localizações das Áreas Verdes:");
            for (Localizacao loc : localizacoes) {
                System.out.println(loc);
            }
        }
    }
}
