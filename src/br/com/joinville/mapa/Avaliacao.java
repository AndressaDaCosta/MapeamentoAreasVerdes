package br.com.joinville.mapa;

public class Avaliacao {
    private int id;
    private int idAreaVerde;
    private int qtdArvores;
    private int qualidadeAr;
    private int poluicaoSonora;
    private int coletaResiduos;
    private int transportePublico;

    public Avaliacao(int id, int idAreaVerde, int qtdArvores, int qualidadeAr, int poluicaoSonora, int coletaResiduos,
            int transportePublico) {
        this.id = id;
        this.idAreaVerde = idAreaVerde;
        this.qtdArvores = qtdArvores;
        this.qualidadeAr = qualidadeAr;
        this.poluicaoSonora = poluicaoSonora;
        this.coletaResiduos = coletaResiduos;
        this.transportePublico = transportePublico;
    }

    public int getIdAreaVerde() {
        return idAreaVerde;
    }

    public double calcularMedia() {
        return (qtdArvores + qualidadeAr + poluicaoSonora + coletaResiduos + transportePublico) / 5.0;
    }

    @Override
    public String toString() {
        return "Avaliação [ID: " + id + ", Área Verde: " + idAreaVerde + ", Média: " + calcularMedia() + "]";
    }
}
