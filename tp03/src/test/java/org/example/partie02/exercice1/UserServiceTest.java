package org.example.partie02.exercice1;

import org.example.partie01.exercice1.User;
import org.example.partie01.exercice1.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserServiceIntegrationTest {

    @Container
    public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.33")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static Connection connection;
    private UserService userService;

    @BeforeAll
    public static void beforeAll() throws Exception {
        connection = DriverManager.getConnection(mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword());
        
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE users (id BIGINT PRIMARY KEY, name VARCHAR(255))");
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
        // Nettoyage de la table avant chaque test
        try (Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE users");
        }
        
        UserRepositoryJdbcImpl userRepository = new UserRepositoryJdbcImpl(connection);
        userService = new UserService(userRepository);
    }

    @Test
    public void testCreateAndGetUser() {
        User newUser = new User(1L, "Integration Test User");
        
        // Tester la création
        userService.createUser(newUser);
        
        // Tester la récupération (vérification de la BD)
        User retrievedUser = userService.getUserById(1L);
        assertNotNull(retrievedUser);
        assertEquals(1L, retrievedUser.getId());
        assertEquals("Integration Test User", retrievedUser.getName());
    }

    @Test
    public void testGetUser_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(2L));
    }

    @Test
    public void testCreateUser_AlreadyExists() {
        User user = new User(3L, "Existing User");
        userService.createUser(user);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
    }
}
