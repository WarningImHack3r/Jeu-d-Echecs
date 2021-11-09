import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Echiquier
 */
public class Echiquier extends JPanel {
    // Panel de 8x8 pour l'échiquier -> COMMENCE PAR BLANC
    // Liée à Fenetre, Echec et Case

    private static final long serialVersionUID = 1L;

    /**
     * Échiquier
     */
    public Case[][] monEchiquier;

    /**
     * Permet la gestion de l'activation du bouton Annuler le coup
     */
    public boolean cancelBtnIsClickable = false;
    /**
     * Garde en mémoire la pièce mangée pour la replacer en cas d'annulation
     */
    private Piece tempMange = null;
    /**
     * Tableau permettant le fonctionnement de coupSansEchec()
     */
    private boolean[][] tabSansEchec = new boolean[8][8];
    
    /**
     * Constructeur ; initialise la forme de l'échiquier
     */
    public Echiquier() {
        this.setLayout(new GridLayout(8,8));
        monEchiquier = new Case[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i%2 == 0 && j%2 == 0) {
                    monEchiquier[i][j] = new Case(Color.PINK, i, j);
                }
                if (i%2 == 0 && j%2 != 0) {
                    monEchiquier[i][j] = new Case(Color.LIGHT_GRAY, i, j);
                }
                if (i%2 != 0 && j%2 != 0) {
                    monEchiquier[i][j] = new Case(Color.PINK, i, j);
                }
                if (i%2 != 0 && j%2 == 0) {
                    monEchiquier[i][j] = new Case(Color.LIGHT_GRAY, i, j);
                }
                monEchiquier[i][j].setFocusPainted(false);
                this.add(monEchiquier[i][j]);
            }
        }
        initialiser();
    }
    
    /**
     * Encadre les cases de différentes couleurs en fonction des coups possibles
     */
    public void prediction(Case dep) {
        int depx = dep.getAbscisse();
        int depy = dep.getOrdonnee();

        if (Echec.joueur == dep.getPiece().getCouleur()) {
            if (dep.getPiece().getType() == TypePiece.Pion) { // PION : déplacement d'une ou deux cases vers l'avant, diagonal pour manger
                if (Echec.joueur == CouleurPiece.Blanc) { // if pion blanc else if pion noir
                    // if noir { i = -i, 1--} idee pr fusionner les deux
                    if (!monEchiquier[depx+1][depy].isOccupe()) {
                        monEchiquier[depx+1][depy].setBorder(Echec.ligneVert);
                    }
                    if (dep.getOrdonnee() >= 0 && dep.getOrdonnee() < 7 && monEchiquier[depx+1][depy+1].getPiece().getCouleur() == CouleurPiece.Noir) {
                        monEchiquier[depx+1][depy+1].setBorder(Echec.ligneCyan);
                    }
                    if (dep.getOrdonnee() > 0 && dep.getOrdonnee() < 8 && monEchiquier[depx+1][depy-1].getPiece().getCouleur() == CouleurPiece.Noir) {
                        monEchiquier[depx+1][depy-1].setBorder(Echec.ligneCyan);
                    }
                    if (dep.getAbscisse() == 1 && !monEchiquier[depx+1][depy].isOccupe()&& !monEchiquier[depx+2][depy].isOccupe()) {
                        monEchiquier[depx+2][depy].setBorder(Echec.ligneVert);
                    }
                } else if (Echec.joueur == CouleurPiece.Noir) {
                    if (!monEchiquier[depx-1][depy].isOccupe()) {
                        monEchiquier[depx-1][depy].setBorder(Echec.ligneVert);
                    }
                    if (dep.getOrdonnee() >= 0 && dep.getOrdonnee() < 7 && monEchiquier[depx-1][depy+1].getPiece().getCouleur() == CouleurPiece.Blanc) {
                        monEchiquier[depx-1][depy+1].setBorder(Echec.ligneCyan);
                    }
                    if (dep.getOrdonnee() > 0 && dep.getOrdonnee() < 8 && monEchiquier[depx-1][depy-1].getPiece().getCouleur() == CouleurPiece.Blanc) {
                        monEchiquier[depx-1][depy-1].setBorder(Echec.ligneCyan);
                    }
                    if (dep.getAbscisse() == 6 && !monEchiquier[depx-1][depy].isOccupe() && !monEchiquier[depx-2][depy].isOccupe()) {
                        monEchiquier[depx-2][depy].setBorder(Echec.ligneVert);
                    }
                }
            }

            if (dep.getPiece().getType() == TypePiece.Tour) { // TOUR : déplacement quelconque en ligne droite, verticalement ou horizontalement
                predictionsHV(dep);
            }
            
            if (dep.getPiece().getType() == TypePiece.Fou) { // FOU : déplacement quelconque en diagonale
                predictionsDiagonal(dep);
            }
            
            if (dep.getPiece().getType() == TypePiece.Cavalier) { // CAVALIER : en ligne droite, 2 case puis 1 dans une autre direction
                int[] caseX = {-1, -2, -2, -1, 1, 2, 2, 1};
                int[] caseY = {-2, -1, 1, 2, 2, 1, -1, -2};

                for (int i = 0; i < 8; i++) {
                    int abs = depx + caseX[i], ord = depy + caseY[i];
                    if ((abs >= 0 && abs < 8) && (ord >= 0 && ord < 8)) {
                        if (monEchiquier[abs][ord].isOccupe() && monEchiquier[abs][ord].getPiece().getCouleur() != dep.getPiece().getCouleur()) {
                            monEchiquier[abs][ord].setBorder(Echec.ligneCyan);
                        }
                        if (!monEchiquier[abs][ord].isOccupe()) {
                            monEchiquier[abs][ord].setBorder(Echec.ligneVert);
                        }
                    }
                }
            }
            
            if (dep.getPiece().getType() == TypePiece.Roi) { // ROI : 1 seule case dans n'importe quelle direction
                int[] caseX = {-1, -1, -1, 0, 0, 1, 1, 1};
                int[] caseY = {0, -1, 1, -1, 1, -1, 0, 1};

                for (int i = 0; i < 8; i++) {
                    int abs = depx + caseX[i], ord = depy + caseY[i];
                    if ((abs >= 0 && abs < 8) && (ord >= 0 && ord < 8)) {
                        if (monEchiquier[abs][ord].isOccupe() && monEchiquier[abs][ord].getPiece().getCouleur() != dep.getPiece().getCouleur()) {
                            monEchiquier[abs][ord].setBorder(Echec.ligneCyan);
                        }
                        if (!monEchiquier[abs][ord].isOccupe()) {
                            monEchiquier[abs][ord].setBorder(Echec.ligneVert);
                        }
                    }
                }
            }
            
            if (dep.getPiece().getType() == TypePiece.Reine) { // REINE : déplacement quelconque dans n'importe quelle direction
                predictionsDiagonal(dep);
                predictionsHV(dep);
            }
        }
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (monEchiquier[i][j].getBorder() != null) {
                    monEchiquier[i][j].hasBorders = true;
                    cancelBtnIsClickable = false;
                    Echec.f.cancelBtn.setEnabled(false);
                }
            }
        }
    }
    
    /**
     * Dépendance de prediction(Case dep), elle affiche les prédictions diagonales (Fou et Dame)
     */
    private void predictionsDiagonal(Case dep) {
        int depx = dep.getAbscisse();
        int depy = dep.getOrdonnee();

        // BAS
        int i = depy + 1, j = depx + 1;
        while (i < 8 && j < 8) { // Bas droite
            if (monEchiquier[j][i].isOccupe() && monEchiquier[j][i].getPiece().getCouleur() != dep.getPiece().getCouleur()) {
                monEchiquier[j][i].setBorder(Echec.ligneCyan);
                break;
            } else if (!monEchiquier[j][i].isOccupe()) {
                monEchiquier[j][i].setBorder(Echec.ligneVert);
                i++;
                j++;
            } else break;
        }
        int k = depy - 1, l = depx + 1;
        while (k >= 0 && l < 8) { // Bas gauche
            if (monEchiquier[l][k].isOccupe() && monEchiquier[l][k].getPiece().getCouleur() != dep.getPiece().getCouleur()) {
                monEchiquier[l][k].setBorder(Echec.ligneCyan);
                break;
            } else if (!monEchiquier[l][k].isOccupe()) {
                monEchiquier[l][k].setBorder(Echec.ligneVert);
                k--;
                l++;
            } else break;
            
        }
        // HAUT
        int m = depx - 1, n = depy + 1;
        while (m >= 0 && n < 8) { // Haut droite
            if (monEchiquier[m][n].isOccupe() && monEchiquier[m][n].getPiece().getCouleur() != dep.getPiece().getCouleur()) { 
                monEchiquier[m][n].setBorder(Echec.ligneCyan);
                break;
            } else if (!monEchiquier[m][n].isOccupe()) {
                monEchiquier[m][n].setBorder(Echec.ligneVert);
                n++;
                m--;
            } else break;
            
        }
        int o = depx - 1, p = depy - 1;
        while (o >= 0 && p >= 0) { // Haut gauche
            if (monEchiquier[o][p].isOccupe() && monEchiquier[o][p].getPiece().getCouleur() != dep.getPiece().getCouleur()) { 
                monEchiquier[o][p].setBorder(Echec.ligneCyan);
                break;
            } else if (!monEchiquier[o][p].isOccupe()) {
                monEchiquier[o][p].setBorder(Echec.ligneVert);
                o--;
                p--;
            } else break;
        }
    }
    
    /**
     * Dépendance de prediction(Case dep), elle affiche les prédictions horizontales et verticales (Tour et Dame)
     */
    private void predictionsHV(Case dep) {
        int depx = dep.getAbscisse();
        int depy = dep.getOrdonnee();
        for (int i = depx+1; i < 8; i++) { // Bas
            if(monEchiquier[i][depy].isOccupe() && monEchiquier[i][depy].getPiece().getCouleur() != dep.getPiece().getCouleur()){
                monEchiquier[i][depy].setBorder(Echec.ligneCyan);
                break;
            }
            if(!monEchiquier[i][depy].isOccupe()) {
                monEchiquier[i][depy].setBorder(Echec.ligneVert);
            }else break;
        }
        for (int j = depx-1; j >= 0; j--) { // Haut
            if(monEchiquier[j][depy].isOccupe() && monEchiquier[j][depy].getPiece().getCouleur() != dep.getPiece().getCouleur()){
                monEchiquier[j][depy].setBorder(Echec.ligneCyan);
                break;
            }
            if(!monEchiquier[j][depy].isOccupe()) {
                monEchiquier[j][depy].setBorder(Echec.ligneVert);
            }else break;
        }
        for (int k = depy+1; k < 8; k++) { // Droite
            if(monEchiquier[depx][k].isOccupe() && monEchiquier[depx][k].getPiece().getCouleur() != dep.getPiece().getCouleur()){
                monEchiquier[depx][k].setBorder(Echec.ligneCyan);
                break;
            }
            if(!monEchiquier[depx][k].isOccupe()) {
                monEchiquier[depx][k].setBorder(Echec.ligneVert);
            }else break;
        }
        for (int l = depy-1; l >= 0; l--) { // Gauche
            if(monEchiquier[depx][l].isOccupe() && monEchiquier[depx][l].getPiece().getCouleur() != dep.getPiece().getCouleur()){
                monEchiquier[depx][l].setBorder(Echec.ligneCyan);
                break;
            }
            if(!monEchiquier[depx][l].isOccupe()) {
                monEchiquier[depx][l].setBorder(Echec.ligneVert);
            }else break;
        }
    }

    /**
     * Sert à supprimer les bordures non magenta de l'échiquier
     */
    public void removeNonMagentaBorders() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (monEchiquier[i][j].hasBorders && monEchiquier[i][j].getBorder() != Echec.ligneMagenta) {
                    monEchiquier[i][j].setBorder(null);
                    monEchiquier[i][j].hasBorders = false;
                }
            }
        }
        cancelBtnIsClickable = true;
        Echec.f.cancelBtn.setEnabled(true);
    }

    /**
     * Message popup d'arrivée du pion en bout de ligne ; choix de la nouvelle pièce
     */
    private Piece popupChangePion(CouleurPiece couleur) {
        String[] options = {"Fou", "Cavalier", "Tour", "Reine"};
        JFrame popup = new JFrame();
        int change = JOptionPane.showOptionDialog(popup, "Vous êtes arrivé au bout du camp ennemi. En quel pion voulez-vous vous changer ?", "Jeu d'échecs", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (change == 0) {
            return new Piece(TypePiece.Fou, couleur);
        }
        if (change == 1) {
            return new Piece(TypePiece.Cavalier, couleur);
        }
        if (change == 2) {
            return new Piece(TypePiece.Tour, couleur);
        }
        if (change == 3) {
            return new Piece(TypePiece.Reine, couleur);
        }
        return popupChangePion(couleur);
    }

    /**
     * Autorise ou non le déplacement d'une pièce
     */
    private boolean deplacementValide(Case dep, Case arr) {
        if ((arr.hasBorders && arr.getBorder() != Echec.ligneRouge) && (dep.getPiece().getCouleur() != arr.getPiece().getCouleur())) {
            return true;
        }
        return false;
    }

    /**
     * Définit le coup comme valide ou non
     */
    private boolean coupValide(Case dep, Case arr) {
        if (deplacementValide(dep, arr)) {
            if (arr.isOccupe() && arr.getPiece().getCouleur() != Echec.joueur) {
                tempMange = arr.getPiece();
                Echec.f.updtScores(Echec.joueur, 1);
                Echec.f.addPieceMangee(arr.getPiece());
            } else {
                tempMange = null;
            }
            jouerCoup(dep, arr);
            removeNonMagentaBorders();
            return true;
        }
        return false;
    }

    /**
     * Déplace la pièce
     */
    private void jouerCoup(Case dep, Case arr) {
        if (dep.getPiece().getType() == TypePiece.Pion && (arr.getAbscisse() == 0 || arr.getAbscisse() == 7)) { // Gestion de l'arrivée d'un pion en bout de ligne
            if (arr.getAbscisse() == 0) {
                arr.setPiece(popupChangePion(CouleurPiece.Noir));
            }
            if (arr.getAbscisse() == 7) {
                arr.setPiece(popupChangePion(CouleurPiece.Blanc));
            }
        } else {
            arr.setPiece(dep.getPiece());
        }
        dep.setPiece(new Piece(TypePiece.Vide, CouleurPiece.Vide));
        if (!cancelBtnIsClickable) {
            cancelBtnIsClickable = true;
            Echec.f.cancelBtn.setEnabled(true);
        }
    }
    
    /**
     * Trouve et retourne le roi ennemi
     */
    private Case trouverRoi(CouleurPiece couleur) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (monEchiquier[i][j].getPiece().getType() == TypePiece.Roi && monEchiquier[i][j].getPiece().getCouleur() != couleur) {
                    return monEchiquier[i][j];
                }
            }
        }
        return null;
    }
    
    /**
     * Renvoie true si l'ennemi de _couleur_ est mis en échec
     */
    public boolean echec(CouleurPiece couleur) {
        Case caseRoi = trouverRoi(couleur); // trouver le roi ennemi
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                prediction(monEchiquier[i][j]); // prédictions de toutes les cases
                if ((caseRoi.getBorder() == Echec.ligneCyan || caseRoi.getBorder() == Echec.ligneMagenta) && couleur == Echec.joueur) { // si le roi ennemi est mangeable...
                    caseRoi.setBorder(Echec.ligneMagenta); // ...l'encadrer

                    // Ajout des coordonnées entre le roi et la pièce qui le met en échec dans le tableau
                    for (int k = 0; k < 8; k++) {
                        for (int l = 0; l < 8; l++) {
                            tabSansEchec[k][l] = false; // (ré)initialisation du tableau
                        }
                    }
                    
                    int absRoi = caseRoi.getAbscisse();
                    int ordRoi = caseRoi.getOrdonnee();
                    CouleurPiece coulRoi = monEchiquier[absRoi][ordRoi].getPiece().getCouleur();
                    Case caseDepEchecRoi = monEchiquier[i][j];
                    int caseDepAbs = caseDepEchecRoi.getAbscisse();
                    int caseDepOrd = caseDepEchecRoi.getOrdonnee();
                    TypePiece typeCaseDep = caseDepEchecRoi.getPiece().getType();

                    // true sur les cases entre le roi ennemi et le pion allié (inclus) qui le met en échec
                    for (int m = 1; m < 8; m++) {
                        if (absRoi+m < 8 && monEchiquier[absRoi+m][ordRoi].getPiece().getType() == typeCaseDep && monEchiquier[absRoi+m][ordRoi].getPiece().getCouleur() != coulRoi) { // Bas
                            for (int n = 1; n <= caseDepAbs; n++) {
                                if (absRoi+n < 8) {
                                    tabSansEchec[absRoi+n][ordRoi] = true;
                                }
                            }
                        } else if (absRoi+m < 8 && ordRoi+m < 8 && monEchiquier[absRoi+m][ordRoi+m].getPiece().getType() == typeCaseDep && monEchiquier[absRoi+m][ordRoi+m].getPiece().getCouleur() != coulRoi) { // Bas droite
                            int a = 1, b = 1;
                            while (absRoi+a != caseDepAbs && ordRoi+b != caseDepOrd) {
                                tabSansEchec[absRoi+a][ordRoi+b] = true;
                                a++;
                                b++;
                            }
                        } else if (absRoi+m < 8 && ordRoi-m >= 0 && monEchiquier[absRoi+m][ordRoi-m].getPiece().getType() == typeCaseDep && monEchiquier[absRoi+m][ordRoi-m].getPiece().getCouleur() != coulRoi) { // Bas gauche
                            int a = 1, b = 1;
                            while (absRoi+a != caseDepAbs+1 && ordRoi-b != caseDepOrd-1) {
                                tabSansEchec[absRoi+a][ordRoi-b] = true;
                                a++;
                                b++;
                            }
                        } else if (absRoi-m >= 0 && monEchiquier[absRoi-m][ordRoi].getPiece().getType() == typeCaseDep && monEchiquier[absRoi-m][ordRoi].getPiece().getCouleur() != coulRoi) { // Haut
                            for (int n = 1; n <= caseDepAbs; n++) {
                                if (absRoi-n >= 0) {
                                    tabSansEchec[absRoi-n][ordRoi] = true;
                                }
                            }
                        } else if (absRoi-m >= 0 && ordRoi+m < 8 && monEchiquier[absRoi-m][ordRoi+m].getPiece().getType() == typeCaseDep && monEchiquier[absRoi-m][ordRoi+m].getPiece().getCouleur() != coulRoi) { // Haut droite
                            int a = 1, b = 1;
                            while (absRoi-a != caseDepAbs && ordRoi+b != caseDepOrd) {
                                tabSansEchec[absRoi-a][ordRoi+b] = true;
                                a++;
                                b++;
                            }
                        } else if (absRoi-m >= 0 && ordRoi-m >= 0 && monEchiquier[absRoi-m][ordRoi-m].getPiece().getType() == typeCaseDep && monEchiquier[absRoi-m][ordRoi-m].getPiece().getCouleur() != coulRoi) { // Haut gauche
                            int a = 1, b = 1;
                            while (absRoi-a != caseDepAbs && ordRoi-b != caseDepOrd) {
                                tabSansEchec[absRoi-a][ordRoi-b] = true;
                                a++;
                                b++;
                            } 
                        } else if (ordRoi+m < 8 && monEchiquier[absRoi][ordRoi+m].getPiece().getType() == typeCaseDep && monEchiquier[absRoi][ordRoi+m].getPiece().getCouleur() != coulRoi) { // Droite

                            for (int n = 1; n <= caseDepAbs; n++) {
                                if (ordRoi+n < 8) {
                                    tabSansEchec[absRoi][ordRoi+n] = true;
                                }
                            }
                        } else if (ordRoi-m >= 0 && monEchiquier[absRoi][ordRoi-m].getPiece().getType() == typeCaseDep && monEchiquier[absRoi][ordRoi-m].getPiece().getCouleur() != coulRoi) { // Gauche
                            for (int n = 1; n <= caseDepAbs; n++) {
                                if (ordRoi-n >= 0) {
                                    tabSansEchec[absRoi][ordRoi-n] = true;
                                }
                            }
                        } else {
                            int[] caseX = {-1, -2, -2, -1, 1, 2, 2, 1}, caseY = {-2, -1, 1, 2, 2, 1, -1, -2};
                            for (int n = 0; n < 8; n++) {
                                int abs = absRoi + caseX[n], ord = ordRoi + caseY[n];
                                if ((abs >= 0 && abs < 8) && (ord >= 0 && ord < 8)) {
                                    if (monEchiquier[abs][ord].getPiece().getType() == typeCaseDep && monEchiquier[abs][ord].getPiece().getCouleur() != coulRoi) {
                                        tabSansEchec[abs][ord] = true;
                                    }
                                }
                            }
                        }
                    }
                    removeNonMagentaBorders();
                    return true;
                } else {
                    for (int k = 0; k < 8; k++) {
                        for (int l = 0; l < 8; l++) {
                            tabSansEchec[k][l] = true;
                        }
                    }
                }
                removeNonMagentaBorders();
            }
        }
        return false;
    }
    
    /**
     * Enlève toutes les prédictions qui ne permettent pas aux pièces alliées de sauver leur roi
     */
    public void coupSansEchec() {
        int cmptFalse = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tabSansEchec[i][j] == false) {
                    cmptFalse++;
                }
            }
        }
        if (cmptFalse > 0) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if ((monEchiquier[i][j].getBorder() == Echec.ligneVert || monEchiquier[i][j].getBorder() == Echec.ligneCyan) && tabSansEchec[i][j] == false) { // enlever les prédictions hors du tableau tabSansEchec
                        monEchiquier[i][j].setBorder(null);
                        monEchiquier[i][j].hasBorders = false;
                    } else if (monEchiquier[i][j].getPiece().getType() == TypePiece.Roi && monEchiquier[i][j].getPiece().getCouleur() == Echec.joueur) {
                        if (monEchiquier[i][j].getBorder() == Echec.ligneRouge) {
                            int[] caseX = {-1, -1, -1, 0, 0, 1, 1, 1};
                            int[] caseY = {0, -1, 1, -1, 1, -1, 0, 1};
                            
                            for (int k = 0; k < 8; k++) {
                                int abs = monEchiquier[i][j].getAbscisse() + caseX[k], ord = monEchiquier[i][j].getOrdonnee() + caseY[k];
                                if ((abs >= 0 && abs < 8) && (ord >= 0 && ord < 8)) {
                                    if (tabSansEchec[abs][ord] == true) {
                                        monEchiquier[abs][ord].setBorder(null);
                                        monEchiquier[abs][ord].hasBorders = false;
                                    } else {
                                        if (monEchiquier[abs][ord].getPiece().getCouleur() == Echec.ennemi()) {
                                            monEchiquier[abs][ord].setBorder(Echec.ligneCyan);
                                        } else if (monEchiquier[abs][ord].getPiece().getCouleur() == CouleurPiece.Vide) {
                                            monEchiquier[abs][ord].setBorder(Echec.ligneVert);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Empêche une pièce de mettre son roi en échec
     */
    public boolean coupValideSansEchec(Case dep, Case arr) {
        if (coupValide(dep, arr)) {
            
            return true;
        }
        return false;
    }

    /**
     * Retourne true si le joueur de la couleur _couleur_ est en échec et mat
     */
    public boolean echecEtMat(CouleurPiece couleur) {
        int casesJouables = 0;
        if (echec(Echec.ennemi())) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (monEchiquier[i][j].isOccupe() && monEchiquier[i][j].getPiece().getCouleur() != couleur) {
                        Echec.joueur = Echec.ennemi();
                        monEchiquier[1][1].setBorder(Echec.ligneRouge);
                        prediction(monEchiquier[1][1]);
                        Echec.joueur = Echec.ennemi();
                    }
                }
            }
            for (int k = 0; k < 8; k++) {
                for (int l = 0; l < 8; l++) {
                    if (tabSansEchec[k][l] == true && (monEchiquier[k][l].getBorder() == Echec.ligneVert || monEchiquier[k][l].getBorder() == Echec.ligneCyan)) {
                        casesJouables++;
                    }
                }
            }
            removeNonMagentaBorders();
            if (casesJouables == 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Retourne true si le roi de la couleur _couleur_ ne peut pas bouger sans se mettre en echec
     */
    public boolean pat(CouleurPiece couleur) {
        Case roiEnnemi = trouverRoi(Echec.joueur); // retourne le roi ennemi
        int nbBorder = 0, autourRoi = 0, dansEchiquier = 8, nbPiece = 0;
        int[] caseX = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] caseY = {0, -1, 1, -1, 1, -1, 0, 1};
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) { // parcourt l'échiquier pour trouver les cases de notre couleur
                prediction(monEchiquier[i][j]);
            }
        }
        for (int m = 0; m < 8; m++) { // Teste si le roi ennemie est entouré de cases vertes, ce qui veut dire que nous l'empêchons de bouger
            int abs = roiEnnemi.getAbscisse() + caseX[m], ord = roiEnnemi.getOrdonnee() + caseY[m];
            if ((abs >= 0 && abs < 8) && (ord >= 0 && ord < 8)) {
                if (monEchiquier[abs][ord].getBorder() == Echec.ligneVert) {
                    autourRoi++;
                }
            } else {
                dansEchiquier--;
            }
        }
        if (autourRoi == dansEchiquier) { // Le roi ennemi ne peut pas bouger
            removeNonMagentaBorders();
            for (int m = 0; m < 8; m++) {
                for (int n = 0; n < 8; n++) {
                    if (monEchiquier[m][n].getPiece().getType() != TypePiece.Roi && monEchiquier[m][n].getPiece().getCouleur() == Echec.ennemi()) { // teste si les pions ennemis peuvent bouger
                        nbPiece++;
                        prediction(monEchiquier[m][n]);
                        for (int o = 0; o < 8; o++) {
                            for (int p = 0; p < 8; p++) {
                                if (monEchiquier[o][p].getBorder() == Echec.ligneVert || monEchiquier[o][p].getBorder() == Echec.ligneCyan) {
                                    nbBorder++;
                                }
                            }
                        }
                        if (nbPiece != 0 && nbBorder == 0) { // Les pièces ennemies ne peuvent pas bouger
                            return true;
                        }
                    }
                    if (nbPiece == 0) { // Pas de pièces ennemies
                        return true;
                    }
                }
            }
            removeNonMagentaBorders();
        } else { // Le roi peut bouger donc pas Pat                          
            return false;
        }
        return false;
    }
    
    /**
     * Arrête le jeu si l'une des deux fonctions au dessus est true
     */
    public boolean finJeu(CouleurPiece couleur) {
        String stringFin = "";
        int isFin = 0;
        if (echecEtMat(couleur)) {
            stringFin = "Échec et mat ! Les " + couleur + " ont gagné !";
            isFin = 1;
        } else if (pat(Echec.ennemi())) {
            stringFin = "Pat ! Fin de partie.";
        }
        
        if (isFin == 1) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    monEchiquier[i][j].setEnabled(false);
                }
            }
            cancelBtnIsClickable = false;
            Echec.f.cancelBtn.setEnabled(false);
            // popup nouvelle partie ou quitter
            String[] options = {"Nouvelle partie", "Quitter le jeu"};
            JFrame finJeuWD = new JFrame();
            int finJeu = JOptionPane.showOptionDialog(finJeuWD, stringFin + " Que voulez-vous faire ?", "Jeu d'échecs", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (finJeu == 0) {
                reinitialiser();
            } else if (finJeu == 1) {
                leaveGame();
            } else {
                finJeuWD.dispose();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Initialise le jeu
     */
    private void initialiser() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                monEchiquier[i][j].setPiece(new Piece(TypePiece.Vide, CouleurPiece.Vide));
                monEchiquier[1][j].setPiece(new Piece(TypePiece.Pion, CouleurPiece.Blanc));
                monEchiquier[6][j].setPiece(new Piece(TypePiece.Pion, CouleurPiece.Noir));
                tabSansEchec[i][j] = true; // (ré)initialisation du tableau
            }
        }

        monEchiquier[0][0].setPiece(new Piece(TypePiece.Tour, CouleurPiece.Blanc));
        monEchiquier[0][1].setPiece(new Piece(TypePiece.Cavalier, CouleurPiece.Blanc));
        monEchiquier[0][2].setPiece(new Piece(TypePiece.Fou, CouleurPiece.Blanc));
        monEchiquier[0][3].setPiece(new Piece(TypePiece.Reine, CouleurPiece.Blanc));
        monEchiquier[0][4].setPiece(new Piece(TypePiece.Roi, CouleurPiece.Blanc));
        monEchiquier[0][5].setPiece(new Piece(TypePiece.Fou, CouleurPiece.Blanc));
        monEchiquier[0][6].setPiece(new Piece(TypePiece.Cavalier, CouleurPiece.Blanc));
        monEchiquier[0][7].setPiece(new Piece(TypePiece.Tour, CouleurPiece.Blanc));

        monEchiquier[7][0].setPiece(new Piece(TypePiece.Tour, CouleurPiece.Noir));
        monEchiquier[7][1].setPiece(new Piece(TypePiece.Cavalier, CouleurPiece.Noir));
        monEchiquier[7][2].setPiece(new Piece(TypePiece.Fou, CouleurPiece.Noir));
        monEchiquier[7][3].setPiece(new Piece(TypePiece.Reine, CouleurPiece.Noir));
        monEchiquier[7][4].setPiece(new Piece(TypePiece.Roi, CouleurPiece.Noir));
        monEchiquier[7][5].setPiece(new Piece(TypePiece.Fou, CouleurPiece.Noir));
        monEchiquier[7][6].setPiece(new Piece(TypePiece.Cavalier, CouleurPiece.Noir));
        monEchiquier[7][7].setPiece(new Piece(TypePiece.Tour, CouleurPiece.Noir));
        
        Echec.joueur = CouleurPiece.Blanc;
    }

    /**
     * Remet le jeu à 0
     */
    public void reinitialiser() {
        String[] options = {"Oui", "Non"};
        JFrame resetWD = new JFrame();
        int reset = JOptionPane.showOptionDialog(resetWD, "Voulez-vous vraiment recommencer la partie ?", "Jeu d'échecs", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (reset == 0) {
            initialiser();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    monEchiquier[i][j].setBorder(null);
                    monEchiquier[i][j].hasBorders = false;
                    tabSansEchec[i][j] = true;
                }
            }
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    Echec.f.tabMangeBlanc[i][j].setPiece(new Piece(TypePiece.Vide, CouleurPiece.Vide));
                    Echec.f.tabMangeNoir[i][j].setPiece(new Piece(TypePiece.Vide, CouleurPiece.Vide));
                }
            }
            cancelBtnIsClickable = false;
            Echec.f.cancelBtn.setEnabled(false);
            Echec.f.scoreBlancLabel.setText("0");
            Echec.f.scoreNoirLabel.setText("0");
            tempMange = null;
        } else {
            resetWD.dispose();
        }
    }
    
    /**
     * Permet au joueur d'annuler son coup lors de son tour
     */
    public void annulerCoup(Case dep, Case arr) {
        if (dep.getPiece().getCouleur() != Echec.joueur || arr.getPiece().getCouleur() != Echec.joueur) {
            if (Echec.joueur == CouleurPiece.Blanc) {
                if (arr.getAbscisse() == 0) {
                    arr.setPiece(new Piece(TypePiece.Pion, CouleurPiece.Noir));
                }
                jouerCoup(arr, dep);
                Echec.joueur = CouleurPiece.Noir;
            } else if (Echec.joueur == CouleurPiece.Noir) {
                if (arr.getAbscisse() == 7) {
                    arr.setPiece(new Piece(TypePiece.Pion, CouleurPiece.Blanc));
                }
                jouerCoup(arr, dep);
                Echec.joueur = CouleurPiece.Blanc;
            }
            if (tempMange != null) {
                if (dep.getPiece().getCouleur() != arr.getPiece().getCouleur()) {
                    arr.setPiece(tempMange);
                    tempMange = null;
                    Echec.f.updtScores(Echec.joueur, -1);
                    Echec.f.delPieceMangee(Echec.joueur);
                }
            }
            echec(Echec.ennemi());
            cancelBtnIsClickable = false;
            Echec.f.cancelBtn.setEnabled(false);
        }
    }

    /**
     * Permet de quitter le jeu
     */
    public void leaveGame() {
        String[] options = {"Oui", "Non"};
        JFrame leaveWD = new JFrame();
        int leave = JOptionPane.showOptionDialog(leaveWD, "Voulez-vous vraiment quitter le jeu ?", "Jeu d'échecs", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (leave == 0) {
            System.exit(0);
        } else {
            leaveWD.dispose();
        }
    }
}