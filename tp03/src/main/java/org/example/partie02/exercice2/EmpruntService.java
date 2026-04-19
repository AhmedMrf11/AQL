package org.example.partie02.exercice2;

public class EmpruntService {
    private final LivreRepository livreRepository;

    public EmpruntService(LivreRepository livreRepository) {
        this.livreRepository = livreRepository;
    }

    public void emprunterLivre(String titre) {
        Livre livre = livreRepository.findByTitre(titre);
        if (livre == null) {
            throw new IllegalArgumentException("Livre non trouvé");
        }
        livreRepository.delete(livre);
    }
}
