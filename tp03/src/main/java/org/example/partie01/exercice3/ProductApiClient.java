package org.example.partie01.exercice3;

/**
 * Client interface for the external Product API.
 * Implementations communicate with a remote service (REST, gRPC, etc.)
 * to retrieve product information.
 */
public interface ProductApiClient {
    /**
     * Retrieves a product by its identifier.
     *
     * @param productId the unique identifier of the product
     * @return the {@link Product} object with full details
     * @throws ProductApiException if the API call fails (network error, HTTP 5xx, etc.)
     * @throws IllegalArgumentException if the response cannot be parsed / has unexpected format
     */
    Product getProduct(String productId);
}
