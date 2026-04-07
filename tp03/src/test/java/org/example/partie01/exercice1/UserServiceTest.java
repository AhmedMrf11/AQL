package org.example.partie01.exercice1;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for UserService ↔ UserRepository interaction.
 * Exercise 1 – Interaction Simple entre Modules
 */
public class UserServiceTest {

    @Test
    void testGetUserById_returnsCorrectUser() {
        // Arrange – create a mock for UserRepository
        UserRepository mockRepo = mock(UserRepository.class);
        User expectedUser = new User(1L, "Ahmed");
        when(mockRepo.findUserById(1L)).thenReturn(expectedUser);

        UserService service = new UserService(mockRepo);

        // Act
        User result = service.getUserById(1L);

        // Assert – correct user is returned
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Ahmed", result.getName());

        // Verify the mock was called with the right argument
        verify(mockRepo).findUserById(1L);
    }
}
