import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String arquivo =
            "datasets/forestfireinput.csv";

        Map map = new Map();
        Reduce reduce = new Reduce();

        try {

            List<String> linhas =
                Files.readAllLines(Path.of(arquivo));

            for (String linha : linhas) {
                map.processar(linha);
            }

            double media = reduce.calcularMedia(
                map.getSoma(),
                map.getQuantidade()
            );

            System.out.println(
                "Quantidade de temperaturas: "
                + map.getQuantidade()
            );

            System.out.println(
                "Soma das temperaturas: %.2f%n"
                + map.getSoma()
            );

            System.out.println(
                "Media das temperaturas: %.2f%n"
                + media
            );

        } catch (IOException e) {

            System.out.println(
                "Erro ao ler o arquivo: "
                + e.getMessage()
            );
        }
    }
}