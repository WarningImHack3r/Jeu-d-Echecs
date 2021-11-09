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

    public static int etat = 0;
    public static CouleurPiece joueur = CouleurPiece.Blanc; // Le joueur blanc commence la partie
    public static Case caseDep, caseArr;
    public static Echiquier unEchiquier;
    public static Border ligneNoir = BorderFactory.createLineBorder(Color.black, 5, false); // bord de l'échiquier
    public static Border ligneRouge = BorderFactory.createLineBorder(Color.red, 5, false); // case actuelle
    public static Border ligneVert = BorderFactory.createLineBorder(Color.green, 5, false); // cases vides déplaceables
    public static Border ligneMagenta = BorderFactory.createLineBorder(Color.magenta, 5, false); // echec et echec et mat
    public static Border ligneCyan = BorderFactory.createLineBorder(Color.cyan, 5, false); // ennemi mangeable
    public static Fenetre f; // fenetre principale
    
    public static int abs(int x) {
        return (x < 0 ? -x : x);
    }

    public static CouleurPiece ennemi() {
        if (Echec.joueur == CouleurPiece.Blanc) {
            return CouleurPiece.Noir;
        } else {
            return CouleurPiece.Blanc;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                f = new Fenetre("Jeu d'Échecs");
            }
        });
    }
}