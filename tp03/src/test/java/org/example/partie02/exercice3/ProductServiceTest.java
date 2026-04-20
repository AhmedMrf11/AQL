package org.example.partie02.exercice3;

import org.example.partie01.exercice3.Product;
import org.example.partie01.exercice3.ProductApiException;
import org.example.partie01.exercice3.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@Testcontainers
public class ProductServiceTest {

    public static final DockerImageName MOCKSERVER_IMAGE = DockerImageName
            .parse("mockserver/mockserver")
            .withTag("mockserver-" + MockServerClient.class.getPackage().getImplementationVersion());

    @Container
    public static MockServerContainer mockServer = new MockServerContainer(MOCKSERVER_IMAGE);

    private MockServerClient mockServerClient;
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        mockServerClient = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
        mockServerClient.reset();

        ProductApiClientHttpImpl apiClient = new ProductApiClientHttpImpl(mockServer.getEndpoint());
        productService = new ProductService(apiClient);
    }

    @Test
    public void testGetProduct_Success() {
        String productId = "P99";
        
        mockServerClient
                .when(request().withMethod("GET").withPath("/products/" + productId))
                .respond(response().withStatusCode(200).withBody("{\"id\":\"P99\",\"name\":\"Test Product\",\"price\":150.0}"));

        Product product = productService.getProduct(productId);

        assertNotNull(product);
        assertEquals("P99", product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(150.0, product.getPrice());
    }

    @Test
    public void testGetProduct_ApiError() {
        String productId = "P99";

        mockServerClient
                .when(request().withMethod("GET").withPath("/products/" + productId))
                .respond(response().withStatusCode(503));

        ProductApiException exception = assertThrows(ProductApiException.class, () -> {
            productService.getProduct(productId);
        });

        assertEquals("Erreur serveur lors de l'appel API", exception.getMessage());
    }

    @Test
    public void testGetProduct_InvalidFormat() {
        String productId = "P99";

        mockServerClient
                .when(request().withMethod("GET").withPath("/products/" + productId))
                .respond(response().withStatusCode(200).withBody("invalid-json-format"));

        assertThrows(ProductApiException.class, () -> {
            productService.getProduct(productId);
        });
    }
}
