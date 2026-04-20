package pl.rengreen.taskmanager.service;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import pl.rengreen.taskmanager.model.Task;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Tests d'intégration avec Testcontainers Java.
 * Démarre un conteneur MySQL réel via Docker pour tester TaskService
 * sans dépendre d'une base de données en mémoire (H2).
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = {TaskServiceIntegrationTest.Initializer.class})
public class TaskServiceIntegrationTest {

    /**
     * Conteneur MySQL géré automatiquement par Testcontainers.
     * @ClassRule garantit qu'un seul conteneur est partagé entre tous les tests.
     */
    @ClassRule
    public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.30")
            .withDatabaseName("taskdb")
            .withUsername("root")
            .withPassword("password")
            .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(120)));

    /**
     * Injecte dynamiquement les propriétés de connexion au contexte Spring
     * en utilisant l'URL/port fournis par le conteneur Testcontainers.
     */
    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext ctx) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + mysql.getJdbcUrl(),
                    "spring.datasource.username=" + mysql.getUsername(),
                    "spring.datasource.password=" + mysql.getPassword(),
                    "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
                    "spring.jpa.hibernate.ddl-auto=update"
            ).applyTo(ctx.getEnvironment());
        }
    }

    @Autowired
    private TaskService taskService;

    /**
     * Test 1 : Vérifier qu'une tâche peut être créée et récupérée depuis MySQL.
     */
    @Test
    public void testCreateTask() {
        Task task = new Task("Integration Task", "Description testcontainers", LocalDate.now(), false, "Creator");
        taskService.createTask(task);

        Long id = task.getId();
        assertNotNull("L'ID ne doit pas être null après la sauvegarde", id);

        Task retrieved = taskService.getTaskById(id);
        assertNotNull("La tâche doit exister en base de données MySQL", retrieved);
        assertEquals("Integration Task", retrieved.getName());
        assertEquals("Description testcontainers", retrieved.getDescription());
    }

    /**
     * Test 2 : Vérifier qu'une tâche peut être supprimée de MySQL.
     */
    @Test
    public void testDeleteTask() {
        Task task = new Task("Task à Supprimer", "Description suppression", LocalDate.now(), false, "Creator");
        taskService.createTask(task);
        Long id = task.getId();
        assertNotNull("La tâche doit exister avant suppression", id);

        taskService.deleteTask(id);

        Task retrieved = taskService.getTaskById(id);
        assertNull("La tâche doit être null après suppression de MySQL", retrieved);
    }
}
