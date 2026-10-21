# Análise de Temperatura Média com MapReduce e Combiner

## Descrição

Projeto desenvolvido em Java com Maven para calcular a temperatura média dos registros presentes no arquivo `forestfireinput.csv`, utilizando os conceitos de MapReduce e a otimização Combiner.

O Combiner realiza uma agregação parcial dos dados antes da etapa Reduce, diminuindo a quantidade de informações intermediárias que precisam ser processadas.

O projeto utiliza uma simulação didática das etapas MapReduce em Java, sem a execução distribuída do Apache Hadoop.

## Estrutura do projeto

```text
Temperatura-media/
├── datasets/
│   └── forestfireinput.csv
├── src/main/java/
│   ├── Main.java
│   ├── Map.java
│   ├── Combiner.java
│   └── Reduce.java
├── pom.xml
└── README.md
```

## Funcionamento

O processamento é dividido em quatro etapas:

**1. Map**

Lê os registros do arquivo CSV e extrai a temperatura, localizada na coluna de índice 8.

**2. Combiner**

Os registros são distribuídos entre três blocos. Cada bloco possui um Combiner responsável por calcular a soma parcial das temperaturas e a quantidade de registros.

**3. Agrupamento dos resultados**

Os resultados parciais dos três Combiners são reunidos para o processamento final.

**4. Reduce**

Recebe as somas e quantidades parciais, calcula a soma total e divide pela quantidade total de temperaturas.

A fórmula utilizada é:

Média = Soma total das temperaturas / Quantidade total de temperaturas

Essa abordagem evita o erro de calcular a média das médias parciais.

## Resultado

O arquivo contém 517 registros válidos de temperatura.

- Quantidade de temperaturas: 517
- Soma das temperaturas: 9765,70
- Temperatura média: **18,89 °C**

A utilização do Combiner mantém o resultado final da atividade anterior, mas reduz de 517 valores individuais para 3 resultados parciais encaminhados ao Reduce nesta simulação.

## Tecnologias utilizadas

- Java 25
- Apache Maven
- Visual Studio Code

## Execução

No terminal, dentro da pasta do projeto, execute:

```bash
mvn compile
```

Em seguida:

```bash
mvn exec:java "-Dexec.mainClass=Main"
```

## Conclusão

A implementação demonstra o funcionamento da otimização Combiner no modelo MapReduce.

Ao realizar agregações parciais antes da etapa Reduce, diminuímos a quantidade de dados intermediários processados, mantendo a precisão do cálculo da temperatura média.