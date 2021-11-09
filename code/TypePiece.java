package code;

/**
 * TypePiece
 */
public enum TypePiece {
    // Définir le type de pièce
    // Liée avec Case et Piece

    PION("Pion"), ROI("Roi"), REINE("Reine"), CAVALIER("Cavalier"), FOU("Fou"), TOUR("Tour"), VIDE("Vide");

    private String name = "";

    TypePiece(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}