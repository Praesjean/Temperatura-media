public class Map {

    public Double processar(String linha) {

        // Ignora o cabeçalho do CSV
        if (linha.startsWith("X,Y,month,day")) {
            return null;
        }

        String[] campos = linha.split(",");

        // Verifica se existe a coluna de temperatura
        if (campos.length <= 8) {
            return null;
        }

        try {
            // Temperatura está na coluna de índice 8
            return Double.parseDouble(campos[8]);

        } catch (NumberFormatException e) {
            // Ignora temperaturas inválidas
            return null;
        }
    }
}