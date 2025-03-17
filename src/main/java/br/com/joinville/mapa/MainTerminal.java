package br.com.joinville.mapa;

import java.util.List;
import java.util.Scanner;

public class MainTerminal {
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
            while (!scanner.hasNextInt()) {
                System.out.println("⚠️ Entrada inválida! Digite um número de 0 a 5.");
                scanner.next();
            }

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
                    System.out.println(
                            "\n✅ Saindo do sistema... Obrigado por usar o Mapeamento de Áreas Verdes de Joinville! 🌿");
                    System.out.println("------------------------------------------------------");
                    break;
                default:
                    System.out.println("⚠️ Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);

        scanner.close();
    }

    private static void listarAreasVerdes() {
        List<AreaVerde> areas = AreaVerdeRepository.listarAreasVerdes();
        if (areas.isEmpty()) {
            System.out.println("\n----------------------------------");
            System.out.println("⚠️ Nenhuma área verde cadastrada.");
            System.out.println("----------------------------------\n");
        } else {
            System.out.println("\n📍 Áreas Verdes Cadastradas:");
            for (AreaVerde area : areas) {
                System.out.println(area);
            }
        }
    }

    private static void avaliarAreaVerde(Scanner scanner) {
        int idArea = obterIdValido(scanner, "Digite o ID da área verde que deseja avaliar");

        List<AreaVerde> areas = AreaVerdeRepository.listarAreasVerdes();
        boolean existe = areas.stream().anyMatch(a -> a.getId() == idArea);

        if (!existe) {
            System.out.println("⚠️ Área verde não encontrada.");
            return;
        }

        System.out.println("\n🌿 Avaliação da Área Verde - Notas de 1 a 5:");

        int qtdArvores = obterNotaValida(scanner, "Quantidade de árvores");
        int qualidadeAr = obterNotaValida(scanner, "Qualidade do ar");
        int poluicaoSonora = obterNotaValida(scanner, "Ausência de poluição sonora");
        int coletaResiduos = obterNotaValida(scanner, "Coleta de resíduos");
        int transportePublico = obterNotaValida(scanner, "Facilidade de transporte público");

        AvaliacaoRepository.adicionarAvaliacao(idArea, qtdArvores, qualidadeAr,
                poluicaoSonora, coletaResiduos, transportePublico);
        System.out.println("✅ Avaliação registrada com sucesso!");
    }

    // Método para visualizar detalhes de uma área verde
    private static void verDetalheAreaVerde(Scanner scanner) {
        int idArea = obterIdValido(scanner, "Digite o ID da área verde que deseja ver os detalhes");

        List<AreaVerde> areas = AreaVerdeRepository.listarAreasVerdes();
        AreaVerde areaSelecionada = areas.stream()
                .filter(a -> a.getId() == idArea)
                .findFirst()
                .orElse(null);

        if (areaSelecionada == null) {
            System.out.println("⚠️ Área verde não encontrada. Tente novamente.");
            return;
        }

        System.out.println("\n📍 Detalhes da Área Verde:");
        System.out.println("ID: " + areaSelecionada.getId());
        System.out.println("Nome: " + areaSelecionada.getNome());
        System.out.println("Tipo de Vegetação: " + areaSelecionada.getTipoVegetacao());
        System.out.println("Horário de Funcionamento: " + areaSelecionada.getHorarioFuncionamento());

        List<Avaliacao> avaliacoes = AvaliacaoRepository.listarAvaliacoes();
        double mediaAvaliacoes = avaliacoes.stream()
                .filter(a -> a.getIdAreaVerde() == idArea)
                .mapToDouble(Avaliacao::calcularMedia)
                .average()
                .orElse(0.0);

        System.out.printf("Média das Avaliações: %.2f\n", mediaAvaliacoes);
    }

    private static void cadastrarAreaVerde(Scanner scanner) {
        System.out.println("\n📍 Cadastro de Nova Área Verde:");
        String nome = obterTextoValido(scanner, "Nome da área verde");
        String tipoVegetacao = obterTextoValido(scanner, "Tipo de vegetação (Árvores, Arbustos, Gramado, etc.)");
        String horarioFuncionamento = obterHorarioFuncionamentoValido(scanner);

        // Configura Locale para garantir que ponto seja usado como separador decimal
        scanner.useLocale(java.util.Locale.US);
        System.out.println("\n🌍 Exemplo de coordenadas válidas:");
        System.out.println("Joinville: -26.3044, -48.8489");
        System.out.println("São Paulo: -23.5505, -46.6333");

        // Validação de latitude e longitude
        double latitude = obterCoordenadaValida(scanner, "Latitude", -90, 90);
        double longitude = obterCoordenadaValida(scanner, "Longitude", -180, 180);

        AreaVerdeRepository.adicionarAreaVerde(nome, tipoVegetacao, horarioFuncionamento);
        int idNovaArea = AreaVerdeRepository.listarAreasVerdes().size();
        LocalizacaoRepository.adicionarLocalizacao(latitude, longitude, idNovaArea);
        System.out.println("✅ Área verde cadastrada com sucesso!");
    }

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

    /**
     * Métodos auxiliares -------------------------------------------
     */
    private static String obterTextoValido(Scanner scanner, String mensagem) {
        String texto;
        do {
            System.out.print(mensagem + ": ");
            texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("⚠️ Esse campo não pode estar vazio.");
            }
        } while (texto.isEmpty());
        return texto;
    }

    private static double obterCoordenadaValida(Scanner scanner, String tipo, double min, double max) {
        double valor;
        while (true) {
            System.out.print(tipo + " (" + min + " a " + max + "): ");
            if (scanner.hasNextDouble()) {
                valor = scanner.nextDouble();
                if (valor >= min && valor <= max) {
                    break;
                } else {
                    System.out.println(
                            "⚠️ Valor inválido! O valor de " + tipo + " deve estar entre " + min + " e " + max + ".");
                }
            } else {
                System.out.println("⚠️ Entrada inválida! Digite um número decimal válido para " + tipo + ".");
                scanner.next();
            }
        }
        scanner.nextLine();
        return valor;
    }

    private static int obterIdValido(Scanner scanner, String mensagem) {
        int id;
        do {
            System.out.print("\n🔍 " + mensagem + ": ");
            while (!scanner.hasNextInt()) {
                System.out.println("⚠️ Entrada inválida! Digite um número inteiro válido.");
                scanner.next();
            }
            id = scanner.nextInt();
        } while (id <= 0);
        return id;
    }

    private static int obterNotaValida(Scanner scanner, String criterio) {
        int nota;
        while (true) {
            System.out.print(criterio + " (1 a 5): ");
            if (scanner.hasNextInt()) {
                nota = scanner.nextInt();
                scanner.nextLine();
                if (nota >= 1 && nota <= 5) {
                    break;
                } else {
                    System.out.println("⚠️ Nota inválida! Digite um número entre 1 e 5.");
                }
            } else {
                System.out.println("⚠️ Entrada inválida! Digite um número inteiro entre 1 e 5.");
                scanner.next();
            }
        }
        return nota;
    }

    private static String obterHorarioFuncionamentoValido(Scanner scanner) {
        String horario;
        String regexHorario = "([01]\\d|2[0-3]):[0-5]\\d - ([01]\\d|2[0-3]):[0-5]\\d";

        do {
            System.out.print("Horário de funcionamento (exemplo: 08:00 - 20:00): ");
            horario = scanner.nextLine().trim();

            if (!horario.matches(regexHorario)) {
                System.out.println("⚠️ Formato inválido! Digite no formato correto (exemplo: 08:00 - 20:00).");
            }

        } while (!horario.matches(regexHorario));

        return horario;
    }
}
