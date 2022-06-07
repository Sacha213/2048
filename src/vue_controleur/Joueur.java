package vue_controleur;

import modele.Case;
import modele.Jeu;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Classe gérant le jeu et l'affichage de la grille de jeu
 */
public class Joueur implements Observer{
    private static final int PIXEL_PER_SQUARE = 90;
    public Jeu jeu;
    private JLabel[][] tabC;
    private JLabel scoreLabel;
    private JPanel jPanel;
    private Swing2048 swing2048;

    /**
     * Constructeur de la classe Joueur
     * @param nbCases nombre de cases de la grille de jeu
     * @param swing référence sur la vue Swing
     */
    public Joueur(int nbCases, Swing2048 swing){
        jeu = new Jeu(nbCases);
        jeu.addObserver(this);
        tabC = new JLabel[jeu.getSize()][jeu.getSize()];
        scoreLabel = new JLabel("0");
        swing2048 = swing;
    }

    /**
     * Crée le JPanel qui permet d'afficher le score et la grille de jeu
     * @return JPanel du joueur
     */
    public JPanel afficherJoueur(){
        jPanel = new JPanel(new BorderLayout());

        //Affichage des informations
        JPanel informationContainer = new JPanel();


        //affichage du score
        JPanel scoreContainer = new JPanel();
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BorderLayout());
        scorePanel.setPreferredSize(new Dimension(90, 40));
        scorePanel.setBackground(Color.lightGray);

        JLabel scoreTitle = new JLabel("Score");
        scoreTitle.setFont(new Font("Serif", Font.PLAIN, 15));
        scoreTitle.setForeground(Color.white);
        scoreTitle.setHorizontalAlignment(SwingConstants.CENTER);
        scorePanel.add(scoreTitle, BorderLayout.NORTH);
        scoreLabel.setFont(new Font("Serif", Font.BOLD, 20));
        scoreLabel.setForeground(Color.white);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scorePanel.add(scoreLabel, BorderLayout.SOUTH);
        scoreContainer.add(scorePanel);

        informationContainer.add(scoreContainer);

        if(swing2048.playType==1){
            //Affichage du record
            JPanel recordContainer = new JPanel();
            recordContainer.setLayout(new BorderLayout());
            recordContainer.setPreferredSize(new Dimension(90, 40));
            recordContainer.setBackground(Color.gray);

            JLabel titleRecord = new JLabel("Record");
            titleRecord.setFont(new Font("Serif", Font.PLAIN, 15));
            titleRecord.setForeground(Color.white);
            titleRecord.setHorizontalAlignment(SwingConstants.CENTER);
            recordContainer.add(titleRecord, BorderLayout.NORTH);

            JLabel recordLabel = new JLabel(String.valueOf(jeu.highScore));
            recordLabel.setFont(new Font("Serif", Font.BOLD, 20));
            recordLabel.setForeground(Color.white);
            recordLabel.setHorizontalAlignment(SwingConstants.CENTER);
            recordContainer.add(recordLabel, BorderLayout.SOUTH);
            informationContainer.add(recordContainer);
        }


        jPanel.add(informationContainer, BorderLayout.NORTH);

        JPanel grilleContainer = new JPanel();
        JPanel grillePanel = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize(), -3, -3));
        grillePanel.setPreferredSize(new Dimension(4*PIXEL_PER_SQUARE, 4*PIXEL_PER_SQUARE));
        //affichage de la grille
        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(Color.darkGray, 3);
                tabC[i][j] = new JLabel();
                tabC[i][j].setBorder(border);
                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tabC[i][j].setOpaque(true);
                tabC[i][j].setForeground(new Color(255, 255, 255));
                tabC[i][j].setFont(new Font("", Font.PLAIN, 23));

                grillePanel.add(tabC[i][j]);
            }
        }
        grilleContainer.add(grillePanel);
        jPanel.add(grilleContainer, BorderLayout.CENTER);
        rafraichir();


        return jPanel;
    }

    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     */
    public void rafraichir()  {
        //si la partie n'est pas finie
        if (jeu.gameRunning) {
            // demande au processus graphique de réaliser le traitement
            SwingUtilities.invokeLater(() -> {
                for (int i = 0; i < jeu.getSize(); i++) {
                    for (int j = 0; j < jeu.getSize(); j++) {
                        Case c = jeu.getCase(i, j);
                        int val = c.getValeur();

                        if (val == 0) {
                            tabC[i][j].setText("");
                        } else {
                            tabC[i][j].setText(val + "");
                        }
                        tabC[i][j].setBackground(swing2048.colorMap.get(val));


                    }
                }
                //On met à jour le score
                scoreLabel.setText(String.valueOf(jeu.score));

            });
        }
        //si la partie est finie
        else {
            swing2048.gameOver();
            jeu.gameRunning = true;
        }
    }

    /**
     * Fonction de base d'un observer, permet de rafraichir la vue
     * @param o     the observable object.
     * @param arg   an argument passed to the {@code notifyObservers}
     *                 method.
     */
    @Override
    public void update(Observable o, Object arg) {
        rafraichir();
    }
}
