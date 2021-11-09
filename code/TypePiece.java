package code;
/**
 * TypePiece
 */
public enum TypePiece{
    // Définir le type de pièce
    // Liée avec Case et Piece

    Pion("Pion"), 
    Roi("Roi"), 
    Reine("Reine"), 
    Cavalier("Cavalier"), 
    Fou("Fou"), 
    Tour("Tour"), 
    Vide("Vide");

    public String name = "";

    TypePiece(String name){
        this.name = name;
    }
}