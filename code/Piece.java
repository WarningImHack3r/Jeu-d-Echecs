package code;
/**
 * Piece
 */
public class Piece {
    // Permet d'obtenir les caractéristiques de la pièce
    // Liée à TypePiece, CouleurPiece et Case

    private CouleurPiece couleur; // couleur de la pièce (noire ou blanche)
    private TypePiece type; // type de piece

    public Piece(TypePiece type, CouleurPiece couleur) {
        this.type = type;
        this.couleur = couleur;
    }

    /**
     * @return the couleur
     */
    public CouleurPiece getCouleur() {
        return couleur;
    }

    /**
     * @return the type
     */
    public TypePiece getType() {
        return type;
    }
}