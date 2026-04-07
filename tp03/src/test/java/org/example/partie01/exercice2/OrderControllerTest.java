package org.example.partie01.exercice2;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

/**
 * Integration test for the OrderController → OrderService → OrderDao chain.
 * Exercise 2 – Interaction avec une Base de Données avec des Mocks
 */
public class OrderControllerTest {

    @Test
    void testCreateOrder_callsServiceAndDao() {
        // Arrange – mocks for service and DAO
        OrderService mockService = mock(OrderService.class);
        OrderDao mockDao = mock(OrderDao.class);

        Order order = new Order("ORD-001");

        // Stub service (no-op by default for void methods, but explicit for clarity)
        doNothing().when(mockService).createOrder(order);

        // Wire controller with mock service
        OrderController controller = new OrderController(mockService);

        // Act
        controller.createOrder(order);

        // Verify OrderService.createOrder was called with the correct order
        verify(mockService).createOrder(order);

        // Note: OrderDao is mocked separately to confirm the full call chain.
        // To verify DAO is called, wire a real service with mock DAO:
        OrderService realService = new OrderService(mockDao);
        realService.createOrder(order);
        verify(mockDao).saveOrder(order);
    }
}
