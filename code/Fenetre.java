package code;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

/**
 * Fenetre
 */
public class Fenetre extends JFrame {
    // Interface graphique
    // Liée avec Echiquier

    private static final long serialVersionUID = 1L;

    JButton resetBtn;
    JButton cancelBtn;
    JButton quitterBtn;
    JLabel scoreBlancLabel;
    JLabel scoreNoirLabel;
    Case[][] tabMangeBlanc;
    Case[][] tabMangeNoir;
    JPanel mangeBlanc;
    JPanel mangeNoir;
    JPanel panelNoir;
    JPanel panelBlanc;

    public Fenetre(String name) {
        super(name);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(1000, 700));
        this.changeLook();
        this.init();

        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void changeLook() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) // avoid misbehaviors if not Windows
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.getMessage();
        }
    }

    private void init() {
        JPanel panelHaut = new JPanel(); // Contient panel droite, jeu et gauche
        panelHaut.setLayout(new BoxLayout(panelHaut, BoxLayout.X_AXIS));
        panelHaut.setMaximumSize(new Dimension(1000, 620));

        panelBlanc = new JPanel(); // Panel à gauche du jeu
        panelBlanc.setLayout(new GridLayout(3, 1));
        JPanel panelJoueurBlanc = new JPanel();
        panelJoueurBlanc.setLayout(new GridLayout(2, 1));
        JLabel blancLogo = new JLabel(Case.roiBlanc);
        blancLogo.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel blancTxt = new JLabel("Joueur Blanc");
        blancTxt.setFont(blancTxt.getFont().deriveFont(24.0f));
        blancTxt.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel panelScoreBlanc = new JPanel();
        JLabel scoreLabel1 = new JLabel("Score :");
        scoreLabel1.setFont(scoreLabel1.getFont().deriveFont(24.0f));
        scoreLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        scoreBlancLabel = new JLabel("0");
        scoreBlancLabel.setFont(scoreBlancLabel.getFont().deriveFont(24.0f));
        scoreBlancLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mangeBlanc = new JPanel();
        mangeBlanc.setLayout(new GridLayout(4, 4));
        tabMangeBlanc = new Case[4][4];

        JPanel fEchec = new JPanel(); // Jeu
        Echec.unEchiquier = new Echiquier();
        Echec.unEchiquier.setBorder(Echec.ligneNoir);

        panelNoir = new JPanel(); // Panel à droite du jeu
        panelNoir.setLayout(new GridLayout(3, 1));
        JPanel panelJoueurNoir = new JPanel();
        panelJoueurNoir.setLayout(new GridLayout(2, 1));
        JLabel noirLogo = new JLabel(Case.roiNoir);
        noirLogo.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel noirTxt = new JLabel("Joueur Noir");
        noirTxt.setFont(noirTxt.getFont().deriveFont(24.0f));
        noirTxt.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel panelScoreNoir = new JPanel();
        JLabel scoreLabel2 = new JLabel("Score :");
        scoreLabel2.setFont(scoreLabel2.getFont().deriveFont(24.0f));
        scoreLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        scoreNoirLabel = new JLabel("0");
        scoreNoirLabel.setFont(scoreNoirLabel.getFont().deriveFont(24.0f));
        scoreNoirLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mangeNoir = new JPanel();
        mangeNoir.setLayout(new GridLayout(4, 4));
        tabMangeNoir = new Case[4][4];

        initTabs();

        JPanel panelBas = new JPanel(); // Panel à boutons en bas
        resetBtn = new JButton("Nouvelle partie");
        resetBtn.addActionListener(e -> Echec.unEchiquier.reinitialiser());
        cancelBtn = new JButton("Annuler coup");
        cancelBtn.setEnabled(false);
        cancelBtn.addActionListener(e -> Echec.unEchiquier.annulerCoup(Echec.caseDep, Echec.caseArr));
        quitterBtn = new JButton("Quitter le jeu");
        quitterBtn.addActionListener(e -> Echec.unEchiquier.leaveGame());

        panelJoueurBlanc.add(blancLogo);
        panelJoueurBlanc.add(blancTxt);
        panelBlanc.add(panelJoueurBlanc);
        panelScoreBlanc.add(scoreLabel1);
        panelScoreBlanc.add(scoreBlancLabel);
        panelBlanc.add(panelScoreBlanc);
        panelBlanc.add(mangeBlanc);
        panelJoueurNoir.add(noirLogo);
        panelJoueurNoir.add(noirTxt);
        panelNoir.add(panelJoueurNoir);
        panelScoreNoir.add(scoreLabel2);
        panelScoreNoir.add(scoreNoirLabel);
        panelNoir.add(panelScoreNoir);
        panelNoir.add(mangeNoir);
        panelHaut.add(panelBlanc);
        fEchec.add(Echec.unEchiquier);
        panelHaut.add(fEchec);
        panelHaut.add(panelNoir);
        panelBas.add(resetBtn);
        panelBas.add(cancelBtn);
        panelBas.add(quitterBtn);
        this.add(panelHaut);
        this.add(panelBas);
    }

    /**
     * Initialise les tableaux de pions mangés
     */
    private void initTabs() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tabMangeBlanc[i][j] = new Case(this.getBackground(), i, j);
                tabMangeNoir[i][j] = new Case(this.getBackground(), i, j);
                tabMangeBlanc[i][j].setPiece(new Piece(TypePiece.VIDE, CouleurPiece.VIDE));
                tabMangeNoir[i][j].setPiece(new Piece(TypePiece.VIDE, CouleurPiece.VIDE));
                tabMangeBlanc[i][j]
                        .setPreferredSize(new Dimension(panelBlanc.getWidth() / 4, panelBlanc.getWidth() / 4));
                tabMangeNoir[i][j].setPreferredSize(new Dimension(panelNoir.getWidth() / 4, panelNoir.getWidth() / 4));
                tabMangeBlanc[i][j].setEnabled(false);
                tabMangeNoir[i][j].setEnabled(false);
                mangeBlanc.add(tabMangeBlanc[i][j]);
                mangeNoir.add(tabMangeNoir[i][j]);
            }
        }
    }

    /**
     * Met à jour les scores des deux joueurs en fonction de la couleur
     * 
     * @param coul
     * @param score
     */
    public void updtScores(CouleurPiece coul, int score) {
        if (coul == CouleurPiece.BLANC) {
            scoreBlancLabel.setText(Integer.toString(Integer.parseInt(scoreBlancLabel.getText()) + score));
        } else if (coul == CouleurPiece.NOIR) {
            scoreNoirLabel.setText(Integer.toString(Integer.parseInt(scoreNoirLabel.getText()) + score));
        }
    }

    /**
     * Ajoute la pièce mangée au tableau correspondant
     * 
     * @param piece
     */
    public void addPieceMangee(Piece piece) {
        if (piece.getCouleur() == CouleurPiece.BLANC) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (!tabMangeNoir[i][j].isOccupe()) {
                        tabMangeNoir[i][j].setPiece(piece);
                        tabMangeNoir[i][j].setIcon(new ImageIcon(((ImageIcon) tabMangeNoir[i][j].getIcon()).getImage()
                                .getScaledInstance(tabMangeNoir[i][j].getHeight() - 5,
                                        tabMangeNoir[i][j].getHeight() - 5, java.awt.Image.SCALE_SMOOTH)));
                        tabMangeNoir[i][j].setDisabledIcon(new ImageIcon(((ImageIcon) tabMangeNoir[i][j].getIcon())
                                .getImage().getScaledInstance(tabMangeNoir[i][j].getHeight() - 5,
                                        tabMangeNoir[i][j].getHeight() - 5, java.awt.Image.SCALE_SMOOTH)));
                        return;
                    }
                }
            }
        } else if (piece.getCouleur() == CouleurPiece.NOIR) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (!tabMangeBlanc[i][j].isOccupe()) {
                        tabMangeBlanc[i][j].setPiece(piece);
                        tabMangeBlanc[i][j].setIcon(new ImageIcon(((ImageIcon) tabMangeBlanc[i][j].getIcon()).getImage()
                                .getScaledInstance(tabMangeBlanc[i][j].getHeight() - 5,
                                        tabMangeBlanc[i][j].getHeight() - 5, java.awt.Image.SCALE_SMOOTH)));
                        tabMangeBlanc[i][j].setDisabledIcon(new ImageIcon(((ImageIcon) tabMangeBlanc[i][j].getIcon())
                                .getImage().getScaledInstance(tabMangeBlanc[i][j].getHeight() - 5,
                                        tabMangeBlanc[i][j].getHeight() - 5, java.awt.Image.SCALE_SMOOTH)));
                        return;
                    }
                }
            }
        }
    }

    /**
     * Retire la pièce mangée du tableau correspondant
     * 
     * @param coul
     */
    public void delPieceMangee(CouleurPiece coul) {
        if (coul == CouleurPiece.BLANC) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (!tabMangeBlanc[i][j].isOccupe()) {
                        if (j == 0) {
                            tabMangeBlanc[i - 1][3].setPiece(new Piece(TypePiece.VIDE, CouleurPiece.VIDE));
                        } else if (j > 0) {
                            tabMangeBlanc[i][j - 1].setPiece(new Piece(TypePiece.VIDE, CouleurPiece.VIDE));
                        }
                        return;
                    }
                }
            }
        } else if (coul == CouleurPiece.NOIR) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (!tabMangeNoir[i][j].isOccupe()) {
                        if (j == 0) {
                            tabMangeNoir[i - 1][3].setPiece(new Piece(TypePiece.VIDE, CouleurPiece.VIDE));
                        } else if (j > 0) {
                            tabMangeNoir[i][j - 1].setPiece(new Piece(TypePiece.VIDE, CouleurPiece.VIDE));
                        }
                        return;
                    }
                }
            }
        }
    }
}