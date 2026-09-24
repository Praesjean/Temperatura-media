public class Reduce {

    public double calcularMedia(
            double soma,
            int quantidade
    ) {

        if (quantidade == 0) {
            throw new IllegalArgumentException(
                "Nenhuma temperatura valida encontrada."
            );
        }

        return soma / quantidade;
    }
}