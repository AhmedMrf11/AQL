package org.example.partie02.exercice1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import org.example.partie01.exercice1.User;
import org.example.partie01.exercice1.UserRepository;
import org.example.partie01.exercice1.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    public void testGetUserById_Success() {
        User mockUser = new User(1L, "Alice");
        when(userRepository.findById(1L)).thenReturn(mockUser);

        User result = userService.getUserById(1L);

        assertEquals("Alice", result.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetUserById_NotFound() {
        when(userRepository.findById(2L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserById(2L);
        });

        assertEquals("Utilisateur non trouve", exception.getMessage());
    }

    @Test
    public void testCreateUser_Success() {
        User newUser = new User(3L, "Bob");
        when(userRepository.findById(3L)).thenReturn(null);

        userService.createUser(newUser);

        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    public void testCreateUser_Exists() {
        User existingUser = new User(4L, "Charlie");
        when(userRepository.findById(4L)).thenReturn(existingUser);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(existingUser);
        });

        verify(userRepository, never()).save(any(User.class));
    }
}
