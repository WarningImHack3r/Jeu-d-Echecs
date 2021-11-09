package code;

/**
 * CouleurPiece
 */
public enum CouleurPiece {
    // Définir la couleur de la pièce
    // Liée à Piece et Echec

    BLANC("Case blanche"), NOIR("Case noir"), VIDE("Case vide");

    private String nom = "";

    CouleurPiece(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
}