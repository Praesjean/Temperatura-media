import java.util.List;

public class Reduce {

    public double calcularMedia(List<Combiner> combiners) {

        double somaTotal = 0.0;
        int quantidadeTotal = 0;

        // Junta os resultados parciais de todos os Combiners
        for (Combiner combiner : combiners) {
            somaTotal += combiner.getSoma();
            quantidadeTotal += combiner.getQuantidade();
        }

        // Evita divisão por zero
        if (quantidadeTotal == 0) {
            throw new IllegalArgumentException(
                "Nenhuma temperatura valida encontrada."
            );
        }

        // Calcula a média global
        return somaTotal / quantidadeTotal;
    }
}