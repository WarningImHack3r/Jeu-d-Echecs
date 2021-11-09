package code;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * Echec
 */
public class Echec {
    // Classe principale
    // Liée à Echiquier et CouleurPiece

    static int etat = 0;
    static Case caseDep;
    static Case caseArr;
    static Echiquier unEchiquier;
    static Fenetre f; // fenetre principale
    static CouleurPiece joueur = CouleurPiece.BLANC; // Le joueur blanc commence la partie
    public static final Border ligneNoir = BorderFactory.createLineBorder(Color.black, 5, false); // bord de l'échiquier
    public static final Border ligneRouge = BorderFactory.createLineBorder(Color.red, 5, false); // case actuelle
    public static final Border ligneVert = BorderFactory.createLineBorder(Color.green, 5, false); // cases vides
                                                                                                  // déplaceables
    public static final Border ligneMagenta = BorderFactory.createLineBorder(Color.magenta, 5, false); // echec et echec
                                                                                                       // et mat
    public static final Border ligneCyan = BorderFactory.createLineBorder(Color.cyan, 5, false); // ennemi mangeable

    public static int abs(int x) {
        return (x < 0 ? -x : x);
    }

    public static CouleurPiece ennemi() {
        return Echec.joueur == CouleurPiece.BLANC ? CouleurPiece.NOIR : CouleurPiece.BLANC;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> f = new Fenetre("Jeu d'Échecs"));
    }
}