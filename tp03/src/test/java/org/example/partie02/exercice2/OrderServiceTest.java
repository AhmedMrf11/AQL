package org.example.partie02.exercice2;

import org.example.partie01.exercice2.Order;
import org.example.partie01.exercice2.OrderController;
import org.example.partie01.exercice2.OrderService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class OrderServiceTest {

    @Container
    public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.33")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static Connection connection;
    private OrderController orderController;

    @BeforeAll
    public static void beforeAll() throws Exception {
        connection = DriverManager.getConnection(mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword());
        
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE orders (id VARCHAR(255) PRIMARY KEY)");
        }
    }

    @AfterAll
    public static void afterAll() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        try (Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE orders");
        }
        
        OrderDaoJdbcImpl orderDao = new OrderDaoJdbcImpl(connection);
        OrderService orderService = new OrderService(orderDao);
        orderController = new OrderController(orderService);
    }

    @Test
    public void testCreateOrder_Integration() throws Exception {
        Order order = new Order("ORD-12345");
        
        // Simuler un appel depuis le controller (la chaîne complète s'exécute)
        orderController.createOrder(order);
        
        // Vérifier dans la base de données que la commande a été persistée
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM orders WHERE id = 'ORD-12345'");
            assertTrue(resultSet.next(), "L'ordre devrait exister dans la base de données");
            assertEquals("ORD-12345", resultSet.getString("id"));
        }
    }
}
