import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CorrelacaoMapper
        extends Mapper<LongWritable, Text, Text, EstatisticasWritable> {

    // Coluna 9 do CSV: temperatura (índice 8 no Java).
    private static final int COLUNA_TEMPERATURA = 8;

    private static final String[] COLUNAS = {
        "X", "Y", "month", "day", "FFMC", "DMC", "DC",
        "ISI", "temp", "RH", "wind", "rain", "area"
    };

    @Override
    protected void map(LongWritable chave, Text valor, Context contexto)
            throws IOException, InterruptedException {

        String linha = valor.toString().trim();

        if (linha.isEmpty() || linha.startsWith("X,Y,month,day")) {
            return;
        }

        String[] dados = linha.split(",", -1);

        // Descarta linhas incompletas para não associar valores incorretos.
        if (dados.length != COLUNAS.length) {
            return;
        }

        double temperatura;

        try {
            temperatura = Double.parseDouble(dados[COLUNA_TEMPERATURA].trim());
        } catch (NumberFormatException e) {
            return;
        }

        if (!Double.isFinite(temperatura)) {
            return;
        }

        for (int i = 0; i < dados.length; i++) {

            // Ignora a própria temperatura e as colunas categóricas.
            if (i == COLUNA_TEMPERATURA || i == 2 || i == 3) {
                continue;
            }

            try {
                double valorColuna = Double.parseDouble(dados[i].trim());

                if (!Double.isFinite(valorColuna)) {
                    continue;
                }

                // Mantém os dois valores da mesma linha do dataset.
                EstatisticasWritable estatisticas =
                        new EstatisticasWritable(valorColuna, temperatura);

                contexto.write(new Text(COLUNAS[i]), estatisticas);

            } catch (NumberFormatException e) {
                // Ignora apenas o par inválido, preservando os demais.
            }
        }
    }
}