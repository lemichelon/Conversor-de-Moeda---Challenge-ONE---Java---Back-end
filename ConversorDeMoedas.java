import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConversorDeMoedas {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/5820892f2d65ef5834ae2b24/latest/USD";

    // Função para buscar as taxas de conversão a partir da API
    private static JsonObject obterTaxasDeConversao() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Usando Gson para fazer o parsing do JSON
            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            return jsonResponse.getAsJsonObject("conversion_rates");
        } catch (Exception e) {
            System.out.println("Erro ao obter taxas de conversão: " + e.getMessage());
            return null;
        }
    }

    // Função para exibir o menu e obter a escolha do usuário
    private static void exibirMenu() {
        System.out.println("\nConversor de Moedas de Alexander Michelon");
        System.out.println("Escolha uma opção:");
        System.out.println("1. USD para EUR (Euro)");
        System.out.println("2. USD para GBP (Libra Esterlina)");
        System.out.println("3. USD para JPY (Yen Japonês)");
        System.out.println("4. USD para BRL (Real Brasileiro)");
        System.out.println("5. USD para CAD (Dólar Canadense)");
        System.out.println("6. USD para AUD (Dólar Australiano)");
        System.out.println("0. Sair");
    }

    // Função para fazer a conversão de moeda
    private static void realizarConversao(JsonObject taxas, int opcao) {
        if (taxas == null) {
            System.out.println("Não foi possível obter as taxas de conversão.");
            return;
        }

        System.out.print("Informe o valor em USD: ");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            double valorUSD = Double.parseDouble(reader.readLine());

            String moedaDestino = "";
            double taxaConversao = 0;

            switch (opcao) {
                case 1: // USD para EUR
                    moedaDestino = "EUR";
                    break;
                case 2: // USD para GBP
                    moedaDestino = "GBP";
                    break;
                case 3: // USD para JPY
                    moedaDestino = "JPY";
                    break;
                case 4: // USD para BRL
                    moedaDestino = "BRL";
                    break;
                case 5: // USD para CAD
                    moedaDestino = "CAD";
                    break;
                case 6: // USD para AUD
                    moedaDestino = "AUD";
                    break;
                default:
                    System.out.println("Opção inválida.");
                    return;
            }

            // Obtendo a taxa de conversão para a moeda escolhida
            if (taxas.has(moedaDestino)) {
                taxaConversao = taxas.get(moedaDestino).getAsDouble();
                double valorConvertido = valorUSD * taxaConversao;
                System.out.printf("Valor convertido: %.2f %s%n", valorConvertido, moedaDestino);
            } else {
                System.out.println("Taxa de conversão não disponível para a moeda selecionada.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao realizar a conversão: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        JsonObject taxas = obterTaxasDeConversao();
        if (taxas == null) {
            System.out.println("Falha ao obter as taxas de conversão. Encerrando o programa.");
            return;
        }

        while (true) {
            exibirMenu();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int opcao = Integer.parseInt(reader.readLine());

                if (opcao == 0) {
                    System.out.println("Saindo...");
                    break;
                }

                realizarConversao(taxas, opcao);
            } catch (Exception e) {
                System.out.println("Erro ao processar a entrada: " + e.getMessage());
            }
        }
    }
}
