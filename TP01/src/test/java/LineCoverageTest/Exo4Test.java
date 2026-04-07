package LineCoverageTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.example.QuadraticEquation; // adapte selon ton package

public class Exo4Test {

    @Test
    void testTwoSolutions() {
        double[] result = QuadraticEquation.solve(1, -3, 2); // x^2 - 3x + 2 = 0
        assertEquals(2, result.length);
        assertArrayEquals(new double[]{2.0, 1.0}, result); // solutions : 1 et 2
    }

    @Test
    void testOneSolution() {
        double[] result = QuadraticEquation.solve(1, 2, 1); // x^2 + 2x + 1 = 0
        assertEquals(1, result.length);
        assertEquals(-1.0, result[0]);
    }

    @Test
    void testNoSolution() {
        double[] result = QuadraticEquation.solve(1, 0, 1); // x^2 + 1 = 0
        assertNull(result); // Δ < 0
    }

    @Test
    void testInvalidA() {
        assertThrows(IllegalArgumentException.class, () -> {
            QuadraticEquation.solve(0, 2, 3); // a = 0
        });
    }
}