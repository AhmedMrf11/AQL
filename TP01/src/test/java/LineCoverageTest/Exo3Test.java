package LineCoverageTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.example.BinarySearch; // adapte selon ton package

public class Exo3Test {

    @Test
    void testElementFound() {
        int[] arr = {1, 2, 3, 4, 5};
        assertEquals(2, BinarySearch.binarySearch(arr, 3)); // 3 est à l'index 2
        assertEquals(0, BinarySearch.binarySearch(arr, 1)); // premier élément
        assertEquals(4, BinarySearch.binarySearch(arr, 5)); // dernier élément
    }

    @Test
    void testElementNotFound() {
        int[] arr = {1, 2, 3, 4, 5};
        assertEquals(-1, BinarySearch.binarySearch(arr, 10)); // élément absent
        assertEquals(-1, BinarySearch.binarySearch(arr, 0));  // élément absent
    }

    @Test
    void testEmptyArray() {
        int[] arr = {};
        assertEquals(-1, BinarySearch.binarySearch(arr, 5)); // tableau vide
    }

    @Test
    void testNullArray() {
        assertThrows(NullPointerException.class, () -> {
            BinarySearch.binarySearch(null, 5);
        });
    }
    @Test
    void testLastElementBug() {
        int[] arr = {1, 2, 3, 4, 5};
        // Avec while(low < high), la recherche du dernier élément pourrait échouer
        assertEquals(4, BinarySearch.binarySearch(arr, 5));
    }

    @Test
    void testNearLastElementBug() {
        int[] arr = {10, 20, 30, 40, 50, 60};
        // Avec le bug, certains éléments proches de la fin ne sont jamais trouvés
        assertEquals(5, BinarySearch.binarySearch(arr, 60));
    }
    @Test
    void testArrayHasOneElement() {
        int[] arr = {1};
        int result = BinarySearch.binarySearch(arr,1);
        assertEquals(0,result);
    }
}