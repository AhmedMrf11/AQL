package LineCoverageTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.example.Anagram;

public class Exo2Test {

    @Test
    void testAnagramSimple() {
        assertTrue(Anagram.isAnagram("chien", "niche"));
    }

    @Test
    void testNotAnagram() {
        assertFalse(Anagram.isAnagram("java", "python"));
    }

    @Test
    void testDifferentLength() {
        assertFalse(Anagram.isAnagram("abc", "ab"));
    }

    @Test
    void testCaseInsensitive() {
        assertTrue(Anagram.isAnagram("Chien", "NICHE"));
    }

    @Test
    void testNull() {
        assertThrows(NullPointerException.class, () -> {
            Anagram.isAnagram(null, "test");
        });

        assertThrows(NullPointerException.class, () -> {
            Anagram.isAnagram("test", null);
        });
    }

    @Test
    void testAnagramWithDifferentLetters() {
        // Même longueur, mais pas anagramme → force return false dans for (c != 0)
        assertFalse(Anagram.isAnagram("abc", "abd"));
    }
    @Test
    void testAnagramFullCoverage() {
        // 1️⃣ vrai anagramme avec majuscules et minuscules
        assertTrue(Anagram.isAnagram("Listen", "Silent"));

        // 2️⃣ anagramme avec répétition de lettres
        assertTrue(Anagram.isAnagram("aabbcc", "baccab"));

        // 3️⃣ même longueur mais pas anagramme → force return false dans for (c != 0)
        assertFalse(Anagram.isAnagram("abcd", "abce"));

        // 4️⃣ longueur différente → force return false sur "if (s1.length() != s2.length())"
        assertFalse(Anagram.isAnagram("abc", "abcd"));

        // 5️⃣ lettres minuscules → déjà couvert
        assertTrue(Anagram.isAnagram("chien", "niche"));
    }
    @Test
    void testEmptyStrings() {
        // deux chaînes vides → sont considérées comme anagrammes
        assertTrue(Anagram.isAnagram("", ""));
    }

    @Test
    void testSingleCharacter() {
        // même caractère → anagramme
        assertTrue(Anagram.isAnagram("a", "a"));

        // caractères différents → pas anagramme
        assertFalse(Anagram.isAnagram("a", "b"));
    }

    @Test
    void testNonAlphabeticCharacters() {
        // on suppose que ton code ne gère que a-z, mais on peut tester chiffres ou ponctuation
        assertFalse(Anagram.isAnagram("a1b", "b1c"));  // même longueur, mais pas anagramme
    }

    @Test
    void testRepeatedCharacters() {
        // test pour plusieurs lettres répétées
        assertTrue(Anagram.isAnagram("aabbcc", "ccbbaa"));
        assertFalse(Anagram.isAnagram("aabbcc", "ccbbaaX")); // différent caractère → faux
    }

    @Test
    void testMixedCaseAndSpaces() {
        // si ton code ignore espaces et casse
        assertTrue(Anagram.isAnagram("A b C", "c B a")); // en supprimant les espaces, vrai
    }
}