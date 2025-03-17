package br.com.joinville.mapa;

import static spark.Spark.*;
import java.util.List;
import java.util.Optional;

public class Main {

        public static void main(String[] args) {
                port(8080);

                get("/", (req, res) -> {
                        StringBuilder html = new StringBuilder();
                        html.append("<html><head><title>Áreas Verdes</title>");
                        html.append("<meta charset='UTF-8'>");
                        html.append("<style>body { font-family: Arial, sans-serif; text-align: center; }");
                        html.append(
                                        "button { padding: 10px; background-color: #4CAF50; color: white; border: none; cursor: pointer; }</style>");
                        html.append("</head><body>");

                        html.append("<h1>🌿 Mapeamento de Áreas Verdes</h1>");

                        // Formulário de cadastro
                        html.append("<h2>📍 Cadastrar Nova Área Verde</h2>");
                        html.append("<form action='/adicionar' method='post'>");
                        html.append(
                                        "Nome: <input type='text' name='nome' pattern='[A-Za-zÀ-ÿ ]{3,}' title='Apenas letras, mínimo 3 caracteres' required><br>");
                        html.append(
                                        "Tipo de Vegetação: <input type='text' name='tipo' pattern='[A-Za-zÀ-ÿ ]{3,}' title='Apenas letras, mínimo 3 caracteres' required><br>");
                        html.append(
                                        "Horário (HH:mm - HH:mm): <input type='text' name='horario' pattern='\\d{2}:\\d{2} - \\d{2}:\\d{2}' title='Exemplo: 08:00 - 18:00' required><br>");
                        html.append(
                                        "Latitude: <input type='text' name='latitude' pattern='-?\\d{1,2}\\.\\d{1,6}' title='Exemplo: -26.304400' required><br>");
                        html.append(
                                        "Longitude: <input type='text' name='longitude' pattern='-?\\d{1,3}\\.\\d{1,6}' title='Exemplo: -48.848900' required><br>");
                        html.append("<button type='submit'>Adicionar</button></form>");

                        // Listar áreas cadastradas
                        html.append("<h2>📋 Áreas Verdes Cadastradas</h2>");
                        List<AreaVerde> areas = AreaVerdeRepository.listarAreasVerdes();
                        if (areas.isEmpty()) {
                                html.append("<p>⚠️ Nenhuma área cadastrada.</p>");
                        } else {
                                html.append("<ul>");
                                for (AreaVerde area : areas) {
                                        html.append("<li><b>").append(area.getNome()).append("</b> - ")
                                                        .append(area.getTipoVegetacao()).append(" (")
                                                        .append(area.getHorarioFuncionamento()).append(") ")
                                                        .append("<a href='/detalhe?id=").append(area.getId())
                                                        .append("'>🔍 Ver Detalhes</a> | ")
                                                        .append("<a href='/avaliar?id=").append(area.getId())
                                                        .append("'>🌟 Avaliar</a>")
                                                        .append("</li>");
                                }
                                html.append("</ul>");
                        }

                        html.append("</body></html>");
                        return html.toString();
                });

                // Cadastrar nova área verde
                post("/adicionar", (req, res) -> {
                        try {
                                String nome = validarTexto(req.queryParams("nome"), "Nome");
                                String tipo = validarTexto(req.queryParams("tipo"), "Tipo de Vegetação");
                                String horario = validarHorario(req.queryParams("horario"));
                                double latitude = validarCoordenada(req.queryParams("latitude"), -90, 90, "Latitude");
                                double longitude = validarCoordenada(req.queryParams("longitude"), -180, 180,
                                                "Longitude");

                                AreaVerdeRepository.adicionarAreaVerde(nome, tipo, horario);
                                int idNovaArea = AreaVerdeRepository.listarAreasVerdes().size();
                                LocalizacaoRepository.adicionarLocalizacao(latitude, longitude, idNovaArea);

                                res.redirect("/");
                        } catch (Exception e) {
                                return "<script>alert('" + e.getMessage() + "'); window.history.back();</script>";
                        }
                        return null;
                });

                // Detalhes de uma área verde
                get("/detalhe", (req, res) -> {
                        int id = Integer.parseInt(req.queryParams("id"));
                        Optional<AreaVerde> areaOpt = AreaVerdeRepository.listarAreasVerdes().stream()
                                        .filter(a -> a.getId() == id)
                                        .findFirst();

                        // Buscar a localização correspondente pelo ID da área verde
                        Optional<Localizacao> localizacaoOpt = LocalizacaoRepository.listarLocalizacoes()
                                        .stream().filter(l -> l.getIdAreaVerde() == id).findFirst();

                        if (areaOpt.isEmpty()) {
                                return "<h1>⚠️ Área não encontrada!</h1><br><a href='/'>Voltar</a>";
                        }

                        AreaVerde area = areaOpt.get();
                        Localizacao localizacao = localizacaoOpt.orElse(null);

                        List<Avaliacao> avaliacoes = AvaliacaoRepository.listarAvaliacoes().stream()
                                        .filter(a -> a.getIdAreaVerde() == id).toList();

                        StringBuilder html = new StringBuilder();
                        html.append("<h1>").append(area.getNome()).append("</h1>");
                        html.append("<p><b>Tipo de Vegetação:</b> ").append(area.getTipoVegetacao()).append("</p>");
                        html.append("<p><b>Horário:</b> ").append(area.getHorarioFuncionamento()).append("</p>");

                        // Exibir a localização, caso exista
                        if (localizacao != null) {
                                html.append("<p><b>Localização:</b> Latitude ")
                                                .append(localizacao.getLatitude()).append(", Longitude ")
                                                .append(localizacao.getLongitude()).append("</p>");
                        } else {
                                html.append("<p><b>Localização:</b> ⚠️ Não disponível</p>");
                        }

                        html.append("<h3>🌟 Avaliações:</h3>");
                        if (avaliacoes.isEmpty()) {
                                html.append("<p>⚠️ Nenhuma avaliação disponível.</p>");
                        } else {
                                double media = avaliacoes.stream().mapToDouble(Avaliacao::calcularMedia).average()
                                                .orElse(0.0);
                                html.append("<p><b>Média das Avaliações:</b> ").append(String.format("%.2f", media))
                                                .append("</p>");
                        }

                        html.append("<br><a href='/'>Voltar</a>");
                        return html.toString();
                });

                // Rota para exibir o formulário de avaliação de uma área verde
                get("/avaliar", (req, res) -> {
                        int id = Integer.parseInt(req.queryParams("id"));
                        Optional<AreaVerde> areaOpt = AreaVerdeRepository.listarAreasVerdes()
                                        .stream().filter(a -> a.getId() == id).findFirst();

                        if (areaOpt.isEmpty()) {
                                return "<h1>⚠️ Área Verde não encontrada!</h1><br><a href='/'>Voltar</a>";
                        }

                        AreaVerde area = areaOpt.get();
                        StringBuilder html = new StringBuilder();
                        html.append("<html><head><title>Avaliação</title></head><body>");
                        html.append("<h1>🌟 Avaliação de ").append(area.getNome()).append("</h1>");
                        html.append("<form action='/avaliar' method='post'>");
                        html.append("<input type='hidden' name='id' value='").append(id).append("'>");
                        html.append(
                                        "<p>Quantidade de Árvores (1-5): <input type='number' name='arvores' min='1' max='5' required></p>");
                        html.append("<p>Qualidade do Ar (1-5): <input type='number' name='ar' min='1' max='5' required></p>");
                        html.append(
                                        "<p>Ausência de Poluição Sonora (1-5): <input type='number' name='sonora' min='1' max='5' required></p>");
                        html.append(
                                        "<p>Coleta de Resíduos (1-5): <input type='number' name='residuos' min='1' max='5' required></p>");
                        html.append(
                                        "<p>Facilidade de Transporte Público (1-5): <input type='number' name='transporte' min='1' max='5' required></p>");
                        html.append("<button type='submit'>Enviar Avaliação</button>");
                        html.append("</form><br><a href='/'>Voltar</a>");
                        html.append("</body></html>");

                        return html.toString();
                });

                // Rota para processar a avaliação enviada
                post("/avaliar", (req, res) -> {
                        int id = Integer.parseInt(req.queryParams("id"));
                        int qtdArvores = Integer.parseInt(req.queryParams("arvores"));
                        int qualidadeAr = Integer.parseInt(req.queryParams("ar"));
                        int sonora = Integer.parseInt(req.queryParams("sonora"));
                        int residuos = Integer.parseInt(req.queryParams("residuos"));
                        int transporte = Integer.parseInt(req.queryParams("transporte"));

                        if (!validarNota(qtdArvores) || !validarNota(qualidadeAr) || !validarNota(sonora)
                                        || !validarNota(residuos) || !validarNota(transporte)) {
                                return "<script>alert('⚠️ Todas as notas devem ser entre 1 e 5!'); window.history.back();</script>";
                        }

                        AvaliacaoRepository.adicionarAvaliacao(id, qtdArvores, qualidadeAr, sonora, residuos,
                                        transporte);
                        res.redirect("/detalhe?id=" + id);
                        return null;
                });

        }

        // =================== FUNÇÕES AUXILIARES ===================

        private static String validarTexto(String valor, String campo) throws Exception {
                if (valor == null || !valor.matches("[A-Za-zÀ-ÿ ]{3,}")) {
                        throw new Exception("⚠️ O campo '" + campo
                                        + "' deve conter apenas letras e no mínimo 3 caracteres.");
                }
                return valor.trim();
        }

        private static String validarHorario(String horario) throws Exception {
                if (horario == null || !horario.matches("\\d{2}:\\d{2} - \\d{2}:\\d{2}")) {
                        throw new Exception("⚠️ Formato de horário inválido! Use HH:mm - HH:mm (Ex: 08:00 - 18:00).");
                }
                return horario;
        }

        private static double validarCoordenada(String valor, double min, double max, String campo) throws Exception {
                if (valor == null || !valor.matches("-?\\d{1,3}\\.\\d{1,6}")) {
                        throw new Exception(
                                        "⚠️ O campo '" + campo + "' deve estar no formato correto, ex: -26.304400.");
                }
                double coord = Double.parseDouble(valor);
                if (coord < min || coord > max) {
                        throw new Exception("⚠️ O campo '" + campo + "' deve estar entre " + min + " e " + max + ".");
                }
                return coord;
        }

        private static boolean validarNota(int nota) {
                return nota >= 1 && nota <= 5;
        }

}
