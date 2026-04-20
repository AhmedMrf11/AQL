package org.example.partie02.exercice3;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.partie01.exercice3.Product;
import org.example.partie01.exercice3.ProductApiClient;
import org.example.partie01.exercice3.ProductApiException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ProductApiClientHttpImpl implements ProductApiClient {

    private final HttpClient httpClient;
    private final String baseUrl;
    private final ObjectMapper objectMapper;

    public ProductApiClientHttpImpl(String baseUrl) {
        this.httpClient = HttpClient.newHttpClient();
        this.baseUrl = baseUrl;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Product getProduct(String productId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/products/" + productId))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), Product.class);
            } else if (response.statusCode() >= 500) {
                throw new ProductApiException("Erreur serveur lors de l'appel API");
            } else {
                throw new IllegalArgumentException("Format de réponse ou statut inattendu : " + response.statusCode());
            }

        } catch (ProductApiException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new ProductApiException("Erreur réseau");
        }
    }
}
