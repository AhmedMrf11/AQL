package org.example.partie01.exercice3;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for ProductService ↔ ProductApiClient interaction.
 * Exercise 3 – Intégration d'API avec Mocking
 *
 * Three scenarios tested:
 *  1. Successful product retrieval
 *  2. Incompatible / malformed data format (IllegalArgumentException)
 *  3. API call failure (ProductApiException)
 */
public class ProductServiceTest {

    // ------------------------------------------------------------------ //
    // Scenario 1 – Successful retrieval
    // ------------------------------------------------------------------ //
    @Test
    void testGetProduct_success() {
        // Arrange
        ProductApiClient mockClient = mock(ProductApiClient.class);
        Product expected = new Product("P42", "Laptop", 1299.99);
        when(mockClient.getProduct("P42")).thenReturn(expected);

        ProductService service = new ProductService(mockClient);

        // Act
        Product result = service.getProduct("P42");

        // Assert – correct product was returned
        assertNotNull(result);
        assertEquals("P42", result.getId());
        assertEquals("Laptop", result.getName());
        assertEquals(1299.99, result.getPrice(), 0.001);

        // Verify the client was called with the right productId
        verify(mockClient).getProduct("P42");
    }

    // ------------------------------------------------------------------ //
    // Scenario 2 – Incompatible data format
    // The API client returns data that cannot be interpreted (e.g., price
    // is a raw string that fails conversion), modelled as an
    // IllegalArgumentException thrown by the client.
    // ------------------------------------------------------------------ //
    @Test
    void testGetProduct_incompatibleDataFormat_throwsIllegalArgumentException() {
        // Arrange
        ProductApiClient mockClient = mock(ProductApiClient.class);
        when(mockClient.getProduct("BAD-DATA"))
                .thenThrow(new IllegalArgumentException("Incompatible response format: price field is not a number"));

        ProductService service = new ProductService(mockClient);

        // Act & Assert – the service propagates the format error
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.getProduct("BAD-DATA")
        );
        assertTrue(ex.getMessage().contains("Incompatible response format"));

        // Verify the client was indeed called
        verify(mockClient).getProduct("BAD-DATA");
    }

    // ------------------------------------------------------------------ //
    // Scenario 3 – API call failure
    // Network timeout, HTTP 5xx, service unavailable, etc.
    // ------------------------------------------------------------------ //
    @Test
    void testGetProduct_apiCallFailure_throwsProductApiException() {
        // Arrange
        ProductApiClient mockClient = mock(ProductApiClient.class);
        when(mockClient.getProduct("P99"))
                .thenThrow(new ProductApiException("API call failed: service unavailable (HTTP 503)"));

        ProductService service = new ProductService(mockClient);

        // Act & Assert – the service propagates the API exception
        ProductApiException ex = assertThrows(
                ProductApiException.class,
                () -> service.getProduct("P99")
        );
        assertTrue(ex.getMessage().contains("HTTP 503"));

        // Verify the client was called with the correct productId
        verify(mockClient).getProduct("P99");
    }
}
