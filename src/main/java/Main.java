import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String arquivo = "datasets/forestfireinput.csv";

        Map map = new Map();
        Reduce reduce = new Reduce();

        int numeroBlocos = 3;

        // Cria um Combiner para cada bloco
        List<Combiner> combiners = new ArrayList<>();

        for (int i = 0; i < numeroBlocos; i++) {
            combiners.add(new Combiner());
        }

        try {
            List<String> linhas = Files.readAllLines(Path.of(arquivo));

            int registrosValidos = 0;

            // Etapa MAP + COMBINER
            for (String linha : linhas) {

                Double temperatura = map.processar(linha);

                if (temperatura != null) {

                    // Distribui os registros entre os blocos
                    int bloco = registrosValidos % numeroBlocos;

                    // Combiner acumula a soma e a quantidade
                    combiners.get(bloco).combinar(temperatura);

                    registrosValidos++;
                }
            }

            // Exibe os resultados parciais
            System.out.println("=== RESULTADOS DOS COMBINERS ===");

            for (int i = 0; i < combiners.size(); i++) {

                Combiner combiner = combiners.get(i);

                System.out.println(
                    "Bloco " + (i + 1) +
                    " | Soma: " + combiner.getSoma() +
                    " | Quantidade: " + combiner.getQuantidade()
                );
            }

            // Etapa REDUCE
            double media = reduce.calcularMedia(combiners);

            System.out.println();
            System.out.println("=== RESULTADO FINAL ===");
            System.out.println("Quantidade de temperaturas: " + registrosValidos);
            System.out.printf("Media das temperaturas: %.2f °C%n", media);

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }
}