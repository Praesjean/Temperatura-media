# Análise de Correlação de Pearson com Hadoop MapReduce

## Descrição

Projeto desenvolvido em Java com Maven e Apache Hadoop para calcular a correlação de Pearson entre a temperatura e as demais variáveis numéricas do dataset `forestfireinput.csv`.

A temperatura está localizada na coluna 9 do arquivo CSV (índice 8 no Java).

O processamento utiliza as etapas Mapper, Combiner e Reducer do Hadoop MapReduce, permitindo calcular correlações a partir de estatísticas parciais sem perder o relacionamento entre os valores de cada registro.

O projeto é executado em modo local no Ubuntu (WSL2), utilizando a API real do Apache Hadoop MapReduce, sem necessidade de configurar HDFS ou YARN.

## Estrutura do projeto

```text
Temperatura-media/
├── datasets/
│   └── forestfireinput.csv
├── src/
│   └── main/
│       └── java/
│           ├── Main.java
│           ├── CorrelacaoMapper.java
│           ├── CorrelacaoCombiner.java
│           ├── CorrelacaoReducer.java
│           └── EstatisticasWritable.java
├── pom.xml
└── README.md
```

A pasta `resultado-correlacoes` é criada automaticamente após a execução do processamento.

## Funcionamento

O processamento é dividido em quatro etapas principais.

### 1. Mapper

O Mapper lê os registros do arquivo CSV e identifica a temperatura de cada linha.

Em seguida, relaciona a temperatura com cada uma das outras colunas numéricas, ignorando as colunas categóricas `month` e `day`.

Para cada par válido, gera as estatísticas necessárias ao cálculo da correlação de Pearson.

Os valores são extraídos da mesma linha do CSV, preservando a correspondência entre as variáveis.

### 2. EstatisticasWritable

A classe `EstatisticasWritable` implementa a interface `Writable` do Hadoop.

Ela armazena seis estatísticas:

- Quantidade de pares válidos (`n`);
- Soma dos valores da variável analisada (`somaX`);
- Soma das temperaturas (`somaY`);
- Soma dos quadrados da variável (`somaXQuadrado`);
- Soma dos quadrados das temperaturas (`somaYQuadrado`);
- Soma dos produtos entre as duas variáveis (`somaXY`).

Essas informações são suficientes para calcular a correlação sem precisar armazenar todos os pares de valores até a etapa final.

### 3. Combiner

O Combiner realiza uma agregação local das estatísticas produzidas pelo Mapper.

Ele soma as estatísticas parciais de cada coluna, reduzindo a quantidade de dados intermediários enviados ao Reducer.

O Combiner não calcula correlações individuais. Ele apenas acumula as informações necessárias para o cálculo global.

Isso evita o erro de calcular uma média de correlações parciais.

### 4. Reducer

O Reducer recebe as estatísticas agrupadas por coluna e soma os resultados parciais.

Em seguida, aplica a fórmula de correlação de Pearson:

```text
                 n × Σxy - (Σx × Σy)
r = ------------------------------------------------
    √[(n × Σx² - (Σx)²) × (n × Σy² - (Σy)²)]
```

O coeficiente `r` varia entre -1 e +1:

- Próximo de +1: correlação linear positiva forte;
- Próximo de -1: correlação linear negativa forte;
- Próximo de 0: correlação linear fraca ou inexistente.

O Reducer também verifica se existem registros suficientes e variação nos valores para evitar divisões por zero.

## Integridade dos dados

Para preservar a integridade dos dados durante o processamento, o Mapper extrai os valores de cada par a partir da mesma linha do CSV.

Cada par gera suas próprias estatísticas, que podem ser acumuladas independentemente da divisão dos registros entre os Mappers.

O Combiner e o Reducer somam essas estatísticas, permitindo calcular a correlação global sem combinar incorretamente registros diferentes.

Essa abordagem é adequada ao modelo de processamento distribuído do Hadoop, embora a execução atual do projeto utilize o modo local.

## Resultado

O dataset possui 517 registros.

O programa calcula a correlação entre a temperatura (`temp`) e as outras dez colunas numéricas:

- X
- Y
- FFMC
- DMC
- DC
- ISI
- RH
- wind
- rain
- area

Os resultados são armazenados no arquivo:

```text
resultado-correlacoes/part-r-00000
```

Cada linha apresenta o nome da variável, seu coeficiente de correlação com a temperatura e a quantidade de pares válidos utilizados no cálculo.

Valores positivos indicam que as variáveis tendem a aumentar juntas. Valores negativos indicam que uma variável tende a diminuir quando a outra aumenta. Correlação não implica causalidade.

## Tecnologias utilizadas

- Java 17;
- Apache Hadoop 3.4.3;
- Apache Maven;
- Ubuntu no WSL2;
- Visual Studio Code.

## Execução

### 1. Abrir o Ubuntu (WSL)

No terminal do VS Code, execute:

```powershell
wsl
```

### 2. Acessar a pasta do projeto

```bash
cd /mnt/c/Users/Jean/OneDrive/Documentos/GitHub/Temperatura-media
```

### 3. Compilar e executar

```bash
mvn compile exec:java -Dexec.mainClass=Main
```

O Hadoop executará o Mapper, o Combiner (quando aplicável) e o Reducer.

### 4. Consultar os resultados

```bash
cat resultado-correlacoes/part-r-00000
```

**Importante:** o Hadoop exige que o diretório de saída não exista antes da execução. Para executar novamente, exclua apenas a pasta de resultados:

```bash
rm -rf resultado-correlacoes
```

Depois, execute novamente o comando Maven.

## Conclusão

O projeto demonstra a utilização do Apache Hadoop MapReduce para calcular a correlação de Pearson entre a temperatura e diferentes variáveis numéricas.

O Mapper preserva o relacionamento entre os valores de cada registro, o Combiner realiza agregações parciais e o Reducer calcula as correlações finais.

A utilização de estatísticas acumuláveis permite processar os registros em diferentes blocos sem precisar reunir todos os dados originais em memória.

A implementação utiliza a API real do Hadoop e foi executada em modo local no Ubuntu (WSL2). O código segue o modelo de processamento distribuído do MapReduce, mas não foi validado em um cluster com múltiplos nós.