
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Main {

    public static void main(String[] args) throws Exception {

        // Configura o Hadoop para executar localmente, sem HDFS ou YARN.
        Configuration configuracao = new Configuration();
        configuracao.set("mapreduce.framework.name", "local");
        configuracao.set("fs.defaultFS", "file:///");

        Job job = Job.getInstance(configuracao, "Correlacao Temperatura");

        job.setJarByClass(Main.class);

        // Define as classes responsáveis por cada etapa do MapReduce.
        job.setMapperClass(CorrelacaoMapper.class);
        job.setCombinerClass(CorrelacaoCombiner.class);
        job.setReducerClass(CorrelacaoReducer.class);

        // Tipos dos dados intermediários produzidos pelo Mapper.
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(EstatisticasWritable.class);

        // Tipos dos dados finais produzidos pelo Reducer.
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // Utiliza um único Reducer para reunir as correlações na mesma saída.
        job.setNumReduceTasks(1);

        // Caminhos de entrada e saída do processamento.
        FileInputFormat.addInputPath(
                job,
                new Path("datasets/forestfireinput.csv")
        );

        FileOutputFormat.setOutputPath(
                job,
                new Path("resultado-correlacoes")
        );

        // Executa o job e informa se terminou com sucesso.
        boolean sucesso = job.waitForCompletion(true);

        if (sucesso) {
            System.out.println("Correlacoes calculadas com sucesso!");
            System.out.println("Resultados na pasta: resultado-correlacoes");
        } else {
            System.err.println("Erro ao executar o MapReduce.");
        }

        System.exit(sucesso ? 0 : 1);
    }
}
