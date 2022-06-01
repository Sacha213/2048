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
import java.util.*;

/**
 * Classe gérant la version graphique du jeu 2048
 */
public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 90;
    private ArrayList<Joueur> joueurs;
    public HashMap<Integer, Color> colorMap;



    /**
     * Constructeur de la classe
     */
    public Swing2048 () {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(4 * PIXEL_PER_SQUARE, (5) * (PIXEL_PER_SQUARE));

        joueurs = new ArrayList<Joueur>();
        remplirColorMap();
        afficherMenu();

        //Affiche l'icône personnalisée
        try {
            setIconImage(ImageIO.read(Objects.requireNonNull(getClass().getResource("../resources/icon.png"))));
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
            creerJeuEasy();
            //On redonne le focus pour avoir les keyevents
            requestFocus();
            ajouterEcouteurClavier();

        });

        buttonPlayMulti.addActionListener(e ->{
            creerJeuMulti();
            //On redonne le focus pour avoir les keyevents
            requestFocus();
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
     /*
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
    */


    public void creerJeuMulti(){
        joueurs.add(new Joueur(4, this));
        joueurs.add(new Joueur(4, this));

        setSize(4 * PIXEL_PER_SQUARE * 2 + PIXEL_PER_SQUARE, (7) * (PIXEL_PER_SQUARE));
        setTitle("2/0/4/8");

        //JPanel principale
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //JPanel pour les informations
        JPanel infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(4 * PIXEL_PER_SQUARE * 2, PIXEL_PER_SQUARE));

        //Affichage du titre
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("2048");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.darkGray);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        titlePanel.add(titleLabel);
        infoPanel.add(titlePanel);
        contentPane.add(infoPanel, BorderLayout.NORTH);

        //affichage du joueur 1
        contentPane.add(joueurs.get(0).afficherJoueur(),BorderLayout.WEST);
        //affichage du joueur 2
        contentPane.add(joueurs.get(1).afficherJoueur(), BorderLayout.EAST);
        setContentPane(contentPane);
    }

    public void creerJeuEasy(){
        joueurs.add(new Joueur(4, this));

        setSize(4 * PIXEL_PER_SQUARE + PIXEL_PER_SQUARE, (7) * (PIXEL_PER_SQUARE));
        setTitle("2/0/4/8");

        //JPanel principale
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //JPanel pour les informations
        JPanel infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(4 * PIXEL_PER_SQUARE * 2, PIXEL_PER_SQUARE));

        //Affichage du titre
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("2048");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.darkGray);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        titlePanel.add(titleLabel);
        infoPanel.add(titlePanel);
        contentPane.add(infoPanel, BorderLayout.NORTH);

        //affichage du joueur 1
        contentPane.add(joueurs.get(0).afficherJoueur(),BorderLayout.CENTER);
        setContentPane(contentPane);
    }


    /**
     * Fonction qui gère la fin du jeu en affichant une pop up
     */
    /*
    public void gameOver(){
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
*/






    @Override
    public void update(Observable o, Object arg) {
        for (Joueur joueur: joueurs) {
            joueur.rafraichir();
        }
    }

    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et déclenche des traitements sur le modèle
     */
    private void ajouterEcouteurClavier() {
        KeyAdapter keyAdapter = new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                Direction direction = Direction.bas;
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : direction = Direction.gauche; break;
                    case KeyEvent.VK_RIGHT : direction = Direction.droite; break;
                    case KeyEvent.VK_DOWN : direction = Direction.bas; break;
                    case KeyEvent.VK_UP : direction = Direction.haut; break;
                    //case KeyEvent.VK_ENTER : jeu.rnd(); break;
                }

                //On bouge tous les joueurs
                for (Joueur joueur: joueurs) {
                    joueur.jeu.update(direction);
                }
            }
        };

        addKeyListener(keyAdapter);

    }

    /* NE PAS TOUCHER
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawOval(40, 40, 20, 20);
    }
     */

}