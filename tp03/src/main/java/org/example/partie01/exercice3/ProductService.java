package org.example.partie01.exercice3;

/**
 * Service layer that retrieves product information from an external API.
 * Delegates the actual HTTP call to a {@link ProductApiClient}.
 */
public class ProductService {

    private final ProductApiClient productApiClient;

    public ProductService(ProductApiClient productApiClient) {
        this.productApiClient = productApiClient;
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId the unique identifier of the product
     * @return the {@link Product} details
     * @throws ProductApiException if the underlying API call fails
     * @throws IllegalArgumentException if the API returns incompatible/malformed data
     */
    public Product getProduct(String productId) {
        return productApiClient.getProduct(productId);
    }
}
