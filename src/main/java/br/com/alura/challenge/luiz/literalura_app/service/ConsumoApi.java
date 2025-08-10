package br.com.alura.challenge.luiz.literalura_app.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class ConsumoApi {

    private final HttpClient client;

    public ConsumoApi() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10)) // timeout de conexão
                .build();
    }

    public String obterDados(String endereco) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .timeout(Duration.ofSeconds(15)) // timeout para leitura da resposta
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();

            if (statusCode >= 200 && statusCode < 300) {
                return response.body();
            } else {
                throw new RuntimeException(
                        "Erro na requisição HTTP (" + statusCode + ") para URL: " + endereco
                );
            }
        } catch (HttpConnectTimeoutException e) {
            throw new RuntimeException("Tempo de conexão esgotado para a URL: " + endereco, e);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt(); // boa prática em InterruptedException
            throw new RuntimeException("Falha ao consumir a API: " + endereco + " - " + e.getMessage(), e);
        }
    }
}