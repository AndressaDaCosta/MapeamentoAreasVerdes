package br.com.joinville.mapa;

public class AreaVerde {
    private int id;
    private String nome;
    private String tipoVegetacao;
    private String horarioFuncionamento;

    public AreaVerde(int id, String nome, String tipoVegetacao, String horarioFuncionamento) {
        this.id = id;
        this.nome = nome;
        this.tipoVegetacao = tipoVegetacao;
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTipoVegetacao() {
        return tipoVegetacao;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    @Override
    public String toString() {
        return "Área Verde [ID: " + id + ", Nome: " + nome + ", Vegetação: " + tipoVegetacao +
                ", Horário: " + horarioFuncionamento + "]";
    }
}
