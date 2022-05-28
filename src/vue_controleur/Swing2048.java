package vue_controleur;

import modele.Case;
import modele.Direction;
import modele.Jeu;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Classe gérant la version graphique du jeu 2048
 */
public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 90;
    // tableau de cases : i, j -> case graphique
    private JLabel[][] tabC;
    private Jeu jeu;
    private JLabel nowScore;
    private JLabel nowRecord;

    private HashMap<Integer, Color> colorMap;
    private KeyAdapter keyAdapter;


    /**
     * Constructeur de la classe
     * @param _jeu Référence sur la grille du jeu
     */
    public Swing2048(Jeu _jeu) {
        jeu = _jeu;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(jeu.getSize() * PIXEL_PER_SQUARE, (jeu.getSize() +1) * (PIXEL_PER_SQUARE));

        remplirColorMap ();
        afficherMenu();
        //Affiche l'icône personnalisée
        try {
            setIconImage(ImageIO.read(getClass().getResource("../resources/icon.png")));
        } catch (IOException e) {
            System.out.println("Impossible de charger l'icône " + e);
        }

    }

    /**
     * Fonction permettant de remplir la structure gérant les couleurs
     */
    private void remplirColorMap () {
        //Remplissage des couleurs
        colorMap = new HashMap<Integer, Color>();
        colorMap.put(0, new Color(255, 255, 255));
        colorMap.put(2, new Color(89, 216, 230));
        colorMap.put(4, new Color(0, 146, 178));
        colorMap.put(8, new Color(0, 103, 166));
        colorMap.put(16, new Color(53, 71, 140));
        colorMap.put(32, new Color(85, 50, 133));
        colorMap.put(64, new Color(123, 31, 162));
        colorMap.put(128, new Color(171, 71, 188));
        colorMap.put(256, new Color(239, 83, 80));
        colorMap.put(512, new Color(148, 9, 13));
        colorMap.put(1024, new Color( 69, 0, 3));
        colorMap.put(2048, new Color(198, 182, 23));
        colorMap.put(4096, new Color(21, 21, 21));
    }

    private void afficherMenu () {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(4, 0));

        contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));


        JPanel pTitle = new JPanel();
        JLabel title = new JLabel("Menu", SwingConstants.CENTER);
        title.setForeground(Color.darkGray);
        title.setFont(new Font("Serif", Font.BOLD, 40));
        pTitle.add(title);

        JButton buttonPlayEasy = new JButton("Facile");
        JButton buttonPlayMedium = new JButton("Moyen");
        JButton buttonPlayMulti = new JButton("Multijoueur");


        buttonPlayEasy.addActionListener(e ->{
            //On redonne le focus pour avoir les keyevents
            requestFocus();
            afficherJeuEasy();
            ajouterEcouteurClavier();
        });

        buttonPlayMulti.addActionListener(e ->{
            //On redonne le focus pour avoir les keyevents
            requestFocus();
            afficherJeuMulti();
            ajouterEcouteurClavier();
        });


        contentPane.add(title);
        contentPane.add(buttonPlayEasy);
        contentPane.add(buttonPlayMedium);
        contentPane.add(buttonPlayMulti);

        setContentPane(contentPane);
    }

    /**
     * Méthode permettant d'afficher le 2048
     */
    public void afficherJeuEasy(){
        //JPanel principale
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //JPanel pour les informations
        JPanel contentInfo = new JPanel();
        contentInfo.setPreferredSize(new Dimension(jeu.getSize() * PIXEL_PER_SQUARE, PIXEL_PER_SQUARE));
        contentInfo.setLayout(new BorderLayout());

        //Affichage du titre
        JPanel pTitle = new JPanel();
        JLabel title = new JLabel("2048");
        title.setForeground(Color.darkGray);
        title.setFont(new Font("Serif", Font.BOLD, 40));
        pTitle.add(title);

        contentInfo.add(pTitle, BorderLayout.WEST);

        JPanel nombre = new JPanel();

        //Affichage du score
        JPanel jScore = new JPanel();
        jScore.setLayout(new BorderLayout());
        jScore.setPreferredSize(new Dimension(90, 40));
        jScore.setBackground(Color.lightGray);


        JLabel titleScore = new JLabel("Score");
        titleScore.setFont(new Font("Serif", Font.PLAIN, 15));
        titleScore.setForeground(Color.white);
        titleScore.setHorizontalAlignment(SwingConstants.CENTER);
        jScore.add(titleScore, BorderLayout.NORTH);

        nowScore = new JLabel("0");
        nowScore.setFont(new Font("Serif", Font.BOLD, 20));
        nowScore.setForeground(Color.white);
        nowScore.setHorizontalAlignment(SwingConstants.CENTER);
        jScore.add(nowScore, BorderLayout.SOUTH);

        nombre.add(jScore);

        //Affichage du score
        JPanel jRecord = new JPanel();
        jRecord.setLayout(new BorderLayout());
        jRecord.setPreferredSize(new Dimension(90, 40));
        jRecord.setBackground(Color.gray);

        JLabel titleRecord = new JLabel("Record");
        titleRecord.setFont(new Font("Serif", Font.PLAIN, 15));
        titleRecord.setForeground(Color.white);
        titleRecord.setHorizontalAlignment(SwingConstants.CENTER);
        jRecord.add(titleRecord, BorderLayout.NORTH);

        //Modifier ici
        nowRecord = new JLabel(String.valueOf(jeu.highScore));
        nowRecord.setFont(new Font("Serif", Font.BOLD, 20));
        nowRecord.setForeground(Color.white);
        nowRecord.setHorizontalAlignment(SwingConstants.CENTER);
        jRecord.add(nowRecord, BorderLayout.SOUTH);

        nombre.add(jRecord);

        contentInfo.add(nombre, BorderLayout.EAST);
        contentPane.add(contentInfo, BorderLayout.NORTH);
        setTitle("2/0/4/8");
        tabC = new JLabel[jeu.getSize()][jeu.getSize()];

        JPanel contentGame = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize(), -3, -3));

        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(Color.darkGray, 3);
                tabC[i][j] = new JLabel();
                tabC[i][j].setBorder(border);
                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tabC[i][j].setOpaque(true);
                tabC[i][j].setForeground(new Color(255, 255, 255));
                tabC[i][j].setFont(new Font("", Font.PLAIN, 23));

                contentGame.add(tabC[i][j]);

            }
        }
        contentPane.add(contentGame, BorderLayout.CENTER);
        setContentPane(contentPane);
        rafraichir();
    }

    public void afficherJeuMulti(){ // a modifier pour le multi
        setSize(jeu.getSize() * PIXEL_PER_SQUARE * 2, (jeu.getSize() +1) * (PIXEL_PER_SQUARE));
        setTitle("2/0/4/8");

        //JPanel principale
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //JPanel pour les informations
        JPanel contentInfo = new JPanel();
        contentInfo.setPreferredSize(new Dimension(jeu.getSize() * PIXEL_PER_SQUARE * 2, PIXEL_PER_SQUARE));
        contentInfo.setLayout(new GridLayout(0, 3, -1, -1));

        //Affichage du titre
        JPanel pTitle = new JPanel();
        JLabel lTitle = new JLabel("2048");
        lTitle.setHorizontalAlignment(JLabel.CENTER);
        lTitle.setForeground(Color.darkGray);
        lTitle.setFont(new Font("Serif", Font.BOLD, 40));
        pTitle.add(lTitle);

        //Affichage des infos du joueur 1
        JPanel pJoueur1 = new JPanel();
        //Affichage du score
        JPanel pScore1 = new JPanel();
        pScore1.setLayout(new BorderLayout());
        pScore1.setPreferredSize(new Dimension(90, 40));
        pScore1.setBackground(Color.lightGray);

        JLabel titleScore1 = new JLabel("Score Joueur 1");
        titleScore1.setFont(new Font("Serif", Font.PLAIN, 15));
        titleScore1.setForeground(Color.white);
        titleScore1.setHorizontalAlignment(SwingConstants.CENTER);
        pScore1.add(titleScore1, BorderLayout.NORTH);
        nowScore = new JLabel("0");
        nowScore.setFont(new Font("Serif", Font.BOLD, 20));
        nowScore.setForeground(Color.white);
        nowScore.setHorizontalAlignment(SwingConstants.CENTER);
        pScore1.add(nowScore, BorderLayout.SOUTH);
        pJoueur1.add(pScore1);

        //Affichage des infos du joueur 1
        JPanel pJoueur2 = new JPanel();
        //Affichage du score
        JPanel pScore2 = new JPanel();
        pScore2.setLayout(new BorderLayout());
        pScore2.setPreferredSize(new Dimension(90, 40));
        pScore2.setBackground(Color.lightGray);


        JLabel titleScore2 = new JLabel("Score Joueur 2");
        titleScore2.setFont(new Font("Serif", Font.PLAIN, 15));
        titleScore2.setForeground(Color.white);
        titleScore2.setHorizontalAlignment(SwingConstants.CENTER);
        pScore2.add(titleScore2, BorderLayout.NORTH);

        pScore2.add(nowScore, BorderLayout.SOUTH);
        pJoueur2.add(pScore2);

        contentInfo.add(pJoueur1, BorderLayout.WEST);
        contentInfo.add(pTitle, BorderLayout.CENTER);
        contentInfo.add(pJoueur2, BorderLayout.CENTER);
        contentPane.add(contentInfo, BorderLayout.NORTH);

        JPanel pGames = new JPanel(new GridLayout(1, 2, 10, 0));
        JPanel contentGame1 = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize(), -3, -3));

        tabC = new JLabel[jeu.getSize()][jeu.getSize()];

        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(Color.darkGray, 3);
                tabC[i][j] = new JLabel();
                tabC[i][j].setBorder(border);
                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tabC[i][j].setOpaque(true);
                tabC[i][j].setForeground(new Color(255, 255, 255));
                tabC[i][j].setFont(new Font("", Font.PLAIN, 23));

                contentGame1.add(tabC[i][j]);

            }
        }

        JPanel contentGame2 = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize(), -3, -3));

        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(Color.darkGray, 3);
                tabC[i][j] = new JLabel();
                tabC[i][j].setBorder(border);
                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tabC[i][j].setOpaque(true);
                tabC[i][j].setForeground(new Color(255, 255, 255));
                tabC[i][j].setFont(new Font("", Font.PLAIN, 23));

                contentGame2.add(tabC[i][j]);

            }
        }


        pGames.add(contentGame1);
        pGames.add(contentGame2);
        contentPane.add(pGames, BorderLayout.CENTER);
        setContentPane(contentPane);
        rafraichir();
    }


    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     */
    private void rafraichir()  {
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
                        tabC[i][j].setBackground(colorMap.get(val));


                    }
                }
                //On met à jour le score
                nowScore.setText(String.valueOf(jeu.score));

            });
        }
        //si la partie est finie
        else {
            //supprimerEcouteurClavier();
            gameOver();
        }
    }

    /**
     * Fonction qui gère la fin du jeu en affichant une pop up
     */
    private void gameOver(){
        SwingUtilities.invokeLater(() -> {
        nowRecord.setText(String.valueOf(jeu.highScore));});

        JDialog jd = new JDialog(this,"Game Over");

        jd.setLayout(new FlowLayout());

        JLabel gameOver = new JLabel("Game Over !\n", SwingConstants.CENTER);
        gameOver.setFont(gameOver.getFont().deriveFont(Font.BOLD, 16f));
        JLabel message = new JLabel("Voulez vous rejouer?");

        JButton rejouer = new JButton("Rejouer");
        JButton quitter = new JButton("Quitter");
        JButton debloquer = new JButton("Débloquer");
        rejouer.addActionListener(e -> {
            jd.setVisible(false);
            jeu.rnd();
            //ajouterEcouteurClavier();
            System.out.println("Rejouer button pressed.");
        });

        quitter.addActionListener(e -> {
            jd.setVisible(false);
            System.exit(0);
            System.out.println("Quitter button pressed.");
        });

        debloquer.addActionListener(e -> {
            jd.setVisible(false);
            jeu.deblocage();
            jeu.gameRunning=true;
            rafraichir();
            System.out.println("Débloquer button pressed.");
        });

        jd.add(gameOver);
        jd.add(message);
        jd.add(rejouer);
        jd.add(quitter);
        if(jeu.nombreDeblocage>0) jd.add(debloquer);
        jd.setSize(200,150);
        jd.setLocationRelativeTo(this);
        jd.setVisible(true);
    }

    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et déclenche des traitements sur le modèle
     */
    private void ajouterEcouteurClavier() {
        keyAdapter = new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : jeu.update(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : jeu.update(Direction.droite); break;
                    case KeyEvent.VK_DOWN : jeu.update(Direction.bas); break;
                    case KeyEvent.VK_UP : jeu.update(Direction.haut); break;
                    case KeyEvent.VK_ENTER : jeu.rnd(); break;
                }
            }
        };
        addKeyListener(keyAdapter);
    }

    private void supprimerEcouteurClavier() {
        removeKeyListener(keyAdapter);
    }



    @Override
    public void update(Observable o, Object arg) {
        rafraichir();
    }

    /* NE PAS TOUCHER
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawOval(40, 40, 20, 20);
    }
     */

}