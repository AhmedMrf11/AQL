package org.example.partie02.exercice2;

public class Livre {
    private String titre;
    private String auteur;

    public Livre(String titre, String auteur) {
        this.titre = titre;
        this.auteur = auteur;
    }

    public String getTitre() { return titre; }
    public String getAuteur() { return auteur; }
}
