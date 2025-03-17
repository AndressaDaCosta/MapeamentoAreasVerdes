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
                        html.append("<meta name='viewport' content='width=device-width, initial-scale=1'>");
                        html.append("<style>");
                        html.append(
                                        "body { font-family: Arial, sans-serif; text-align: center; margin: 0; padding: 0; background: #f4f4f4; }");
                        html.append("h1 { color: #2e7d32; margin: 20px 0; }");
                        html.append("h2 { color: #388e3c; }");
                        html.append(
                                        "form { background: white; padding: 20px; border-radius: 10px; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); max-width: 500px; margin: auto; margin-bottom: 30px; }");
                        html.append(
                                        "input, button { width: 100%; padding: 10px; margin: 10px 0; font-size: 16px; border: 1px solid #ccc; border-radius: 5px; }");
                        html.append(
                                        "button { background-color: #4caf50; color: white; border: none; cursor: pointer; font-size: 18px; }");
                        html.append("button:hover { background-color: #45a049; }");
                        html.append(
                                        ".lista { text-align: left; max-width: 600px; margin: auto; padding: 20px; background: white; border-radius: 10px; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); }");
                        html.append(
                                        ".item { background: #e8f5e9; padding: 10px; margin: 5px 0; border-radius: 5px; display: flex; justify-content: space-between; align-items: center; }");
                        html.append("a { text-decoration: none; color: #2e7d32; font-weight: bold; }");
                        html.append("a:hover { text-decoration: underline; }");
                        html.append("</style></head><body>");

                        html.append("<h1>🌿 Mapeamento de Áreas Verdes</h1>");

                        // Formulário de cadastro
                        html.append("<h2>📍 Cadastrar Nova Área Verde</h2>");
                        html.append("<form action='/adicionar' method='post'>");
                        html.append(
                                        "<input type='text' name='nome' pattern='[A-Za-zÀ-ÿ ]{3,}' title='Apenas letras, mínimo 3 caracteres' placeholder='Nome da Área Verde' required><br>");
                        html.append(
                                        "<input type='text' name='tipo' pattern='[A-Za-zÀ-ÿ ]{3,}' title='Apenas letras, mínimo 3 caracteres' placeholder='Tipo de Vegetação' required><br>");
                        html.append(
                                        "<input type='text' name='horario' pattern='\\d{2}:\\d{2} - \\d{2}:\\d{2}' title='Exemplo: 08:00 - 18:00' placeholder='Horário (HH:mm - HH:mm)' required><br>");
                        html.append(
                                        "<input type='text' name='latitude' pattern='-?\\d{1,2}\\.\\d{1,6}' title='Exemplo: -26.304400' placeholder='Latitude' required><br>");
                        html.append(
                                        "<input type='text' name='longitude' pattern='-?\\d{1,3}\\.\\d{1,6}' title='Exemplo: -48.848900' placeholder='Longitude' required><br>");
                        html.append("<button type='submit'>Adicionar</button></form>");

                        // Lista de áreas cadastradas
                        html.append("<div class='lista'><h2>📋 Áreas Verdes Cadastradas</h2>");
                        List<AreaVerde> areas = AreaVerdeRepository.listarAreasVerdes();
                        if (areas.isEmpty()) {
                                html.append("<p>⚠️ Nenhuma área cadastrada.</p>");
                        } else {
                                html.append("<ul style='list-style:none; padding: 0;'>");
                                for (AreaVerde area : areas) {
                                        html.append("<li class='item'><b>").append(area.getNome())
                                                        // .append("</b> - ")
                                                        .append("</b> ")
                                                        // .append(area.getTipoVegetacao())
                                                        .append(" (")
                                                        .append(area.getHorarioFuncionamento()).append(") ")
                                                        .append("<a href='/detalhe?id=").append(area.getId())
                                                        .append("'>🔍 Ver Detalhes</a>")
                                                        .append(" | <a href='/avaliar?id=").append(area.getId())
                                                        .append("'>🌟 Avaliar</a>")
                                                        .append("</li>");
                                }
                                html.append("</ul>");
                        }
                        html.append("</div>");

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
                                        .filter(a -> a.getId() == id).findFirst();

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
                        html.append("<html><head><title>Detalhes da Área Verde</title>");
                        html.append("<meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                        html.append("<style>");
                        html.append(
                                        "body { font-family: Arial, sans-serif; text-align: center; background: #f4f4f4; margin: 0; padding: 0; }");
                        html.append(
                                        ".container { max-width: 600px; margin: 20px auto; padding: 20px; background: white; border-radius: 10px; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); }");
                        html.append("h1 { color: #2e7d32; }");
                        html.append("p { font-size: 16px; line-height: 1.5; }");
                        html.append(".info { background: #e8f5e9; padding: 10px; border-radius: 5px; margin-bottom: 10px; }");
                        html.append(
                                        "a { display: inline-block; margin-top: 10px; color: white; background: #4caf50; padding: 10px 15px; text-decoration: none; border-radius: 5px; }");
                        html.append("a:hover { background: #388e3c; }");
                        html.append("</style></head><body>");

                        html.append("<div class='container'>");
                        html.append("<h1>📍 ").append(area.getNome()).append("</h1>");
                        html.append("<p class='info'><b>Tipo de Vegetação:</b> ").append(area.getTipoVegetacao())
                                        .append("</p>");
                        html.append("<p class='info'><b>Horário:</b> ").append(area.getHorarioFuncionamento())
                                        .append("</p>");

                        if (localizacao != null) {
                                html.append("<p class='info'><b>Localização:</b> Latitude ")
                                                .append(localizacao.getLatitude())
                                                .append(", Longitude ").append(localizacao.getLongitude())
                                                .append("</p>");
                        } else {
                                html.append("<p class='info'><b>Localização:</b> ⚠️ Não disponível</p>");
                        }

                        html.append("<h3>🌟 Avaliações:</h3>");
                        if (avaliacoes.isEmpty()) {
                                html.append("<p>⚠️ Nenhuma avaliação disponível.</p>");
                        } else {
                                double media = avaliacoes.stream().mapToDouble(Avaliacao::calcularMedia).average()
                                                .orElse(0.0);
                                html.append("<p class='info'><b>Média das Avaliações:</b> ")
                                                .append(String.format("%.2f", media))
                                                .append("</p>");
                        }

                        html.append("<a href='/'>🔙 Voltar</a>");
                        html.append("</div>");

                        html.append("</body></html>");
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
                        html.append("<html><head><title>Avaliação</title>");
                        html.append("<meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                        html.append("<style>");
                        html.append(
                                        "body { font-family: Arial, sans-serif; text-align: center; background: #f4f4f4; margin: 0; padding: 0; }");
                        html.append(
                                        ".container { max-width: 500px; margin: 20px auto; padding: 20px; background: white; border-radius: 10px; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); }");
                        html.append("h1 { color: #2e7d32; }");
                        html.append("form { display: flex; flex-direction: column; align-items: center; }");
                        html.append(
                                        "input, button { width: 100%; padding: 10px; margin: 8px 0; font-size: 16px; border: 1px solid #ccc; border-radius: 5px; }");
                        html.append(
                                        "button { background-color: #4caf50; color: white; border: none; cursor: pointer; font-size: 18px; }");
                        html.append("button:hover { background-color: #388e3c; }");
                        html.append(
                                        "a { display: inline-block; margin-top: 10px; color: white; background: #4caf50; padding: 10px 15px; text-decoration: none; border-radius: 5px; }");
                        html.append("a:hover { background: #388e3c; }");
                        html.append("</style></head><body>");

                        html.append("<div class='container'>");
                        html.append("<h1>🌟 Avaliação de ").append(area.getNome()).append("</h1>");
                        html.append("<form action='/avaliar' method='post'>");
                        html.append("<input type='hidden' name='id' value='").append(id).append("'>");
                        html.append(
                                        "<p>🌳 Quantidade de Árvores (1-5): <input type='number' name='arvores' min='1' max='5' required></p>");
                        html.append("<p>💨 Qualidade do Ar (1-5): <input type='number' name='ar' min='1' max='5' required></p>");
                        html.append(
                                        "<p>🔇 Ausência de Poluição Sonora (1-5): <input type='number' name='sonora' min='1' max='5' required></p>");
                        html.append(
                                        "<p>🗑️ Coleta de Resíduos (1-5): <input type='number' name='residuos' min='1' max='5' required></p>");
                        html.append(
                                        "<p>🚌 Facilidade de Transporte Público (1-5): <input type='number' name='transporte' min='1' max='5' required></p>");
                        html.append("<button type='submit'>✅ Enviar Avaliação</button>");
                        html.append("</form>");
                        html.append("<a href='/'>🔙 Voltar</a>");
                        html.append("</div>");

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
