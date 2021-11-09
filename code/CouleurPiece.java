package code;
/**
 * CouleurPiece
 */
public enum CouleurPiece{
    // Définir la couleur de la pièce
    // Liée à Piece et Echec
    
    Blanc("Case blanche"),
    Noir("Case noir"),
    Vide("Case vide");

    String nom = "";

    CouleurPiece(String nom){
        this.nom = nom;
    }
}