import java.io.IOException;
import java.util.Locale;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CorrelacaoReducer
        extends Reducer<Text, EstatisticasWritable, Text, Text> {

    @Override
    protected void reduce(
            Text chave,
            Iterable<EstatisticasWritable> valores,
            Context contexto
    ) throws IOException, InterruptedException {

        EstatisticasWritable total = new EstatisticasWritable();

        // Reúne as estatísticas de todos os registros da coluna.
        for (EstatisticasWritable valor : valores) {
            total.somar(valor);
        }

        long n = total.getQuantidade();

        double somaX = total.getSomaX();
        double somaY = total.getSomaY();
        double somaXQuadrado = total.getSomaXQuadrado();
        double somaYQuadrado = total.getSomaYQuadrado();
        double somaXY = total.getSomaXY();

        // Componentes da fórmula de correlação de Pearson.
        double numerador = n * somaXY - somaX * somaY;

        double denominadorX =
                n * somaXQuadrado - somaX * somaX;

        double denominadorY =
                n * somaYQuadrado - somaY * somaY;

        // Evita divisão por zero quando não existe variação nos dados.
        if (n < 2 || denominadorX <= 0 || denominadorY <= 0) {
            contexto.write(
                    chave,
                    new Text("Correlacao indefinida (n=" + n + ")")
            );
            return;
        }

        double denominador =
                Math.sqrt(denominadorX * denominadorY);

        double correlacao = numerador / denominador;

        // Corrige pequenas diferenças de arredondamento.
        correlacao = Math.max(-1.0, Math.min(1.0, correlacao));

        String resultado = String.format(
                Locale.US,
                "%.6f (n=%d)",
                correlacao,
                n
        );

        contexto.write(chave, new Text(resultado));
    }
}