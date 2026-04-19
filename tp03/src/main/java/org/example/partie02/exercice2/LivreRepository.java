package org.example.partie02.exercice2;

public interface LivreRepository {
    Livre findByTitre(String titre);
    void delete(Livre livre);
}
