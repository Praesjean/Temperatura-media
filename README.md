# Análise de Temperatura Média com MapReduce

Projeto desenvolvido para a disciplina de Big Data com o objetivo de calcular a temperatura média presente no arquivo `forestfireinput.csv`, utilizando a lógica de MapReduce.

## Objetivo

O programa realiza a leitura do dataset e utiliza a coluna de temperatura, localizada no índice 8 do arquivo CSV.

O cálculo da média é realizado utilizando:

**Média = Soma das temperaturas / Quantidade de temperaturas**

Dessa forma, evita-se o cálculo incorreto de média das médias.

## Estrutura do Projeto

```text
Temperatura-media/
├── datasets/
│   └── forestfireinput.csv
├── src/
│   └── main/
│       └── java/
│           ├── Main.java
│           ├── Map.java
│           └── Reduce.java
├── pom.xml
└── README.md
```

## Classes

### Main.java

Responsável por:

- Ler o arquivo `forestfireinput.csv`;
- Executar a etapa Map;
- Enviar os resultados para a etapa Reduce;
- Exibir o resultado final.

### Map.java

Responsável por:

- Processar cada linha do dataset;
- Ignorar o cabeçalho;
- Obter a temperatura localizada no índice 8;
- Somar as temperaturas;
- Contabilizar a quantidade de temperaturas processadas.

### Reduce.java

Responsável por receber a soma e a quantidade obtidas na etapa Map e calcular a média final:

```text
media = soma / quantidade
```

## Tecnologias utilizadas

- Java 25
- Apache Maven

## Execução

Para compilar o projeto:

```bash
mvn compile
```

Para executar:

```bash
mvn exec:java "-Dexec.mainClass=Main"
```

## Resultado

O dataset possui 517 temperaturas válidas.

Resultado obtido:

```text
Quantidade de temperaturas: 517
Soma das temperaturas: 9765,70
Media das temperaturas: 18,89
```

Portanto, a temperatura média encontrada foi de aproximadamente **18,89 °C**.