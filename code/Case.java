package code;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Case
 */
public class Case extends JButton implements ActionListener {
    // Agit sur chaque case de la grille du jeu
    // Liée à Echiquier, TypePiece et Piece

    private static final long serialVersionUID = 1L;

    private Piece piece; // classe Piece, les caractéristiques
    private Color couleurFond; // couleur de la case
    private int abscisse;
    private int ordonnee; // abscisse et ordonnée de la pièce
    boolean hasBorders;

    static ImageIcon roiBlanc = new ImageIcon("code/icons/Roiblanc.png");
    static ImageIcon roiNoir = new ImageIcon("code/icons/Roinoir.png");
    static ImageIcon reineBlanc = new ImageIcon("code/icons/Reineblanc.png");
    static ImageIcon reineNoir = new ImageIcon("code/icons/Reinenoir.png");
    static ImageIcon cavalierBlanc = new ImageIcon("code/icons/Cavalierblanc.png");
    static ImageIcon cavalierNoir = new ImageIcon("code/icons/Cavaliernoir.png");
    static ImageIcon tourBlanc = new ImageIcon("code/icons/Tourblanc.png");
    static ImageIcon tourNoir = new ImageIcon("code/icons/Tournoir.png");
    static ImageIcon fouBlanc = new ImageIcon("code/icons/Foublanc.png");
    static ImageIcon fouNoir = new ImageIcon("code/icons/Founoir.png");
    static ImageIcon pionBlanc = new ImageIcon("code/icons/Pionblanc.png");
    static ImageIcon pionNoir = new ImageIcon("code/icons/Pionnoir.png");

    public Case(Color couleur, int abs, int ord) {
        this.couleurFond = couleur;
        this.abscisse = abs;
        this.ordonnee = ord;
        this.hasBorders = false;
        this.setBorder(null);
        this.setBackground(couleurFond);
        this.addActionListener(this);
        this.setPreferredSize(new Dimension(75, 75));
    }

    /**
     * @return the piece
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * @param piece the piece to set
     */
    public void setPiece(Piece p) {
        this.piece = p;
        if (p.getCouleur() == CouleurPiece.BLANC) {
            if (p.getType() == TypePiece.PION) {
                this.setIcon(pionBlanc);
                this.setDisabledIcon(pionBlanc);
            } else if (p.getType() == TypePiece.TOUR) {
                this.setIcon(tourBlanc);
                this.setDisabledIcon(tourBlanc);
            } else if (p.getType() == TypePiece.CAVALIER) {
                this.setIcon(cavalierBlanc);
                this.setDisabledIcon(cavalierBlanc);
            } else if (p.getType() == TypePiece.FOU) {
                this.setIcon(fouBlanc);
                this.setDisabledIcon(fouBlanc);
            } else if (p.getType() == TypePiece.ROI) {
                this.setIcon(roiBlanc);
                this.setDisabledIcon(roiBlanc);
            } else if (p.getType() == TypePiece.REINE) {
                this.setIcon(reineBlanc);
                this.setDisabledIcon(reineBlanc);
            }
        } else if (p.getCouleur() == CouleurPiece.NOIR) {
            if (p.getType() == TypePiece.PION) {
                this.setIcon(pionNoir);
                this.setDisabledIcon(pionNoir);
            } else if (p.getType() == TypePiece.TOUR) {
                this.setIcon(tourNoir);
                this.setDisabledIcon(tourNoir);
            } else if (p.getType() == TypePiece.CAVALIER) {
                this.setIcon(cavalierNoir);
                this.setDisabledIcon(cavalierNoir);
            } else if (p.getType() == TypePiece.FOU) {
                this.setIcon(fouNoir);
                this.setDisabledIcon(fouNoir);
            } else if (p.getType() == TypePiece.ROI) {
                this.setIcon(roiNoir);
                this.setDisabledIcon(roiNoir);
            } else if (p.getType() == TypePiece.REINE) {
                this.setIcon(reineNoir);
                this.setDisabledIcon(reineNoir);
            }
        } else if (!this.isOccupe()) {
            this.setIcon(null);
            this.setDisabledIcon(null);
        }
    }

    /**
     * @return the abscisse
     */
    public int getAbscisse() {
        return abscisse;
    }

    /**
     * @return the ordonnee
     */
    public int getOrdonnee() {
        return ordonnee;
    }

    /**
     * @return the couleurFond
     */
    public Color getCouleurFond() {
        return couleurFond;
    }

    public boolean isOccupe() {
        return this.getPiece().getType() != TypePiece.VIDE && this.getPiece().getCouleur() != CouleurPiece.VIDE;
    }

    /**
     * Listener de toutes les cases ; voir graphe d'état page 4
     */
    public void actionPerformed(ActionEvent e) {
        if (Echec.etat == 0) {
            Echec.caseDep = (Case) e.getSource();
            if (Echec.caseDep.getPiece().getCouleur() == Echec.joueur) {
                Echec.caseDep.setBorder(Echec.ligneRouge);
                if (Echec.caseDep.isOccupe()) {
                    Echec.unEchiquier.prediction(Echec.caseDep);
                    Echec.unEchiquier.coupSansEchec();
                    Echec.etat = 1;
                }
            }
            Echec.unEchiquier.cancelBtnIsClickable = false;
            Echec.f.cancelBtn.setEnabled(false);
        } else {
            if (this.hasBorders && this.getBorder() != Echec.ligneRouge) { // si dans les prédictions
                Echec.caseArr = (Case) e.getSource();
                if (Echec.unEchiquier.coupValideSansEchec(Echec.caseDep, Echec.caseArr)) {
                    if (Echec.unEchiquier.finJeu(Echec.joueur)) {
                        return;
                    }
                    if (Echec.unEchiquier.echec(Echec.joueur)) {
                        System.out.println("Les " + Echec.joueur + " ont mis les " + Echec.ennemi() + " en échec");
                    } else {
                        if (!Echec.unEchiquier.echec(Echec.joueur)) {
                            for (int i = 0; i < 8; i++) {
                                for (int j = 0; j < 8; j++) {
                                    Echec.unEchiquier.monEchiquier[i][j].setBorder(null);
                                    Echec.unEchiquier.monEchiquier[i][j].hasBorders = false;
                                }
                            }
                        }
                        Echec.unEchiquier.cancelBtnIsClickable = true;
                        Echec.f.cancelBtn.setEnabled(true);
                    }
                    // Changement de joueur et d'état
                    Echec.joueur = Echec.ennemi();
                    Echec.etat = 0;
                }
            } else { // si selection case vide ou même case
                Echec.etat = 0;
                // nettoyer les bordures
                Echec.unEchiquier.removeNonMagentaBorders();
                // copie modifiée de l'état 0
                if (this.getPiece().getCouleur() == Echec.joueur && this.getAbscisse() != Echec.caseDep.getAbscisse()
                        || this.getOrdonnee() != Echec.caseDep.getOrdonnee()) { // si sélection d'une case de la même
                                                                                // couleur, resélectionner directement
                    this.setBorder(Echec.ligneRouge);
                    Echec.caseDep = this;
                    if (this.isOccupe()) {
                        Echec.unEchiquier.prediction(this);
                        Echec.unEchiquier.coupSansEchec();
                        Echec.etat = 1;
                    }
                }
                Echec.unEchiquier.cancelBtnIsClickable = false;
                Echec.f.cancelBtn.setEnabled(false);
            }
        }
    }
}