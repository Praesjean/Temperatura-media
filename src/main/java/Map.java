public class Map {

    private double soma;
    private int quantidade;

    public Map() {
        this.soma = 0.0;
        this.quantidade = 0;
    }

    public void processar(String linha) {

        if (linha.startsWith("X,Y,month,day")) {
            return;
        }

        String[] campos = linha.split(",");

        if (campos.length > 8) {

            try {

                double temperatura =
                    Double.parseDouble(campos[8]);

                soma += temperatura;
                quantidade++;

            } catch (NumberFormatException e) {
            }
        }
    }

    public double getSoma() {
        return soma;
    }

    public int getQuantidade() {
        return quantidade;
    }
}