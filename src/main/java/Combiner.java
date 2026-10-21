public class Combiner {

    private double soma;
    private int quantidade;

    public void combinar(double temperatura) {
        soma += temperatura;
        quantidade++;
    }

    public double getSoma() {
        return soma;
    }

    public int getQuantidade() {
        return quantidade;
    }
}