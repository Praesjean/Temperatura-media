import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CorrelacaoCombiner
        extends Reducer<Text, EstatisticasWritable, Text, EstatisticasWritable> {

    @Override
    protected void reduce(
            Text chave,
            Iterable<EstatisticasWritable> valores,
            Context contexto
    ) throws IOException, InterruptedException {

        EstatisticasWritable acumulado = new EstatisticasWritable();

        // Soma as estatísticas parciais de cada coluna.
        for (EstatisticasWritable valor : valores) {
            acumulado.somar(valor);
        }

        // Envia os valores acumulados para a próxima etapa.
        contexto.write(chave, acumulado);
    }
}