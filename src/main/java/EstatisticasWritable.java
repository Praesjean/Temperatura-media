import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class EstatisticasWritable implements Writable {

    // Estatísticas necessárias para calcular a correlação de Pearson.
    private long quantidade;
    private double somaX;
    private double somaY;
    private double somaXQuadrado;
    private double somaYQuadrado;
    private double somaXY;

    public EstatisticasWritable() {
    }

    // Recebe dois valores da mesma linha do CSV.
    public EstatisticasWritable(double x, double y) {
        this.quantidade = 1;
        this.somaX = x;
        this.somaY = y;
        this.somaXQuadrado = x * x;
        this.somaYQuadrado = y * y;
        this.somaXY = x * y;
    }

    // Acumula os resultados parciais sem calcular a correlação antes da hora.
    public void somar(EstatisticasWritable outra) {
        this.quantidade += outra.quantidade;
        this.somaX += outra.somaX;
        this.somaY += outra.somaY;
        this.somaXQuadrado += outra.somaXQuadrado;
        this.somaYQuadrado += outra.somaYQuadrado;
        this.somaXY += outra.somaXY;
    }

    public long getQuantidade() {
        return quantidade;
    }

    public double getSomaX() {
        return somaX;
    }

    public double getSomaY() {
        return somaY;
    }

    public double getSomaXQuadrado() {
        return somaXQuadrado;
    }

    public double getSomaYQuadrado() {
        return somaYQuadrado;
    }

    public double getSomaXY() {
        return somaXY;
    }

    // Serializa os dados para transferência entre as etapas do Hadoop.
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(quantidade);
        out.writeDouble(somaX);
        out.writeDouble(somaY);
        out.writeDouble(somaXQuadrado);
        out.writeDouble(somaYQuadrado);
        out.writeDouble(somaXY);
    }

    // Reconstrói os dados na mesma ordem em que foram gravados.
    @Override
    public void readFields(DataInput in) throws IOException {
        quantidade = in.readLong();
        somaX = in.readDouble();
        somaY = in.readDouble();
        somaXQuadrado = in.readDouble();
        somaYQuadrado = in.readDouble();
        somaXY = in.readDouble();
    }
}