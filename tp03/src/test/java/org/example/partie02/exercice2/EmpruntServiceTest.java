package org.example.partie02.exercice2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmpruntServiceTest {

    @Mock
    private LivreRepository livreRepository;

    private EmpruntService empruntService;

    @BeforeEach
    public void setUp() {
        empruntService = new EmpruntService(livreRepository);
    }

    @Test
    public void testEmprunterLivre_Success() {
        Livre mockLivre = new Livre("Harry Potter", "J.K. Rowling");
        when(livreRepository.findByTitre("Harry Potter")).thenReturn(mockLivre);

        empruntService.emprunterLivre("Harry Potter");

        verify(livreRepository, times(1)).delete(mockLivre);
    }

    @Test
    public void testEmprunterLivre_NotFound() {
        when(livreRepository.findByTitre("Unknown")).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            empruntService.emprunterLivre("Unknown");
        });

        assertEquals("Livre non trouvé", exception.getMessage());
        verify(livreRepository, never()).delete(any(Livre.class));
    }
}
