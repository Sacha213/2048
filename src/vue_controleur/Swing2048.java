package vue_controleur;

import modele.Action;
import modele.Jeu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

/**
 * Classe gérant la version graphique du jeu 2048
 */
public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 90;
    private ArrayList<Joueur> joueurs;
    public HashMap<Integer, Color> colorMap;

    //Si il y a un seul joueur alors playType = 1
    //Si il y a deux joueurs alors playType = 2
    public int playType;



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




    public void creerJeuMulti(){
        playType=2;
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
        playType=1;
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

    public void gameOver(){
        SwingUtilities.invokeLater(() -> {


        //nowRecord.setText(String.valueOf(jeu.highScore));});

        JDialog jd = new JDialog(this,"Game Over");

        jd.setLayout(new FlowLayout());

        JLabel gameOver = new JLabel("Game Over !\n", SwingConstants.CENTER);
        gameOver.setFont(gameOver.getFont().deriveFont(Font.BOLD, 16f));
        JLabel message = new JLabel("Voulez vous rejouer?");

        JButton rejouer = new JButton("Rejouer");
        JButton menu = new JButton("Menu");
        JButton debloquer = new JButton("Débloquer");
        rejouer.addActionListener(e -> {
            jd.setVisible(false);

            joueurs = new ArrayList<Joueur>();
            if(playType==1){
                creerJeuEasy();
            } else if (playType==2) {
                creerJeuMulti();
            }




            //ajouterEcouteurClavier();
            System.out.println("Rejouer button pressed.");
        });

        menu.addActionListener(e -> {
            jd.setVisible(false);
            setSize(4 * PIXEL_PER_SQUARE, (5) * (PIXEL_PER_SQUARE));

            joueurs = new ArrayList<Joueur>();
            afficherMenu();
            System.out.println("Menu button pressed.");
        });

            jd.add(gameOver);
            jd.add(message);
            jd.add(rejouer);
            jd.add(menu);

        if(playType==1)
        debloquer.addActionListener(e -> {
            jd.setVisible(false);
            Joueur joueur = joueurs.get(0);
            Jeu jeu = joueur.jeu;
            jeu.deblocage();
            jeu.gameRunning=true;
            joueur.rafraichir();
            System.out.println("Débloquer button pressed.");
            if(jeu.nombreDeblocage>0) jd.add(debloquer);
        });



        jd.setSize(200,150);
        jd.setLocationRelativeTo(this);
        jd.setVisible(true);
        });
    }






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
                Action action;
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : action = Action.gauche; break;
                    case KeyEvent.VK_RIGHT : action = Action.droite; break;
                    case KeyEvent.VK_DOWN : action = Action.bas; break;
                    case KeyEvent.VK_UP : action = Action.haut; break;
                    case KeyEvent.VK_ENTER : action = Action.start; break;
                    case KeyEvent.VK_BACK_SPACE : action = Action.back; break;
                    default: action = null;
                }
                if(action == null) return;
                //On bouge tous les joueurs
                for (Joueur joueur: joueurs) {

                    joueur.jeu.update(action);
                }
            }
        };

        addKeyListener(keyAdapter);

    }

}