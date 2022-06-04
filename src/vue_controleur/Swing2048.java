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

    //1 joueur  : playType = 1
    //2 joueurs : playType = 2
    public int playType;



    /**
     * Constructeur de la classe
     */
    public Swing2048 () {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("2048 par Sacha et Lilian");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

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

    /**
     * Affiche le menu dans la vue
     */
    private void afficherMenu () {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(6, 1, 5, 20));

        contentPane.setBorder(BorderFactory.createEmptyBorder(10,500,10,500));

        JPanel pTitle = new JPanel();
        JLabel title = new JLabel("Menu", SwingConstants.CENTER);
        title.setForeground(Color.darkGray);
        title.setFont(new Font("Serif", Font.BOLD, 40));
        pTitle.add(title);

        JButton buttonPlaySolo = new JButton("Solo");
        JButton buttonPlayMulti = new JButton("Multijoueur");
        JButton buttonQuitter = new JButton("Quitter");

        buttonPlaySolo.addActionListener(e ->{
            creerJeuSolo();
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

        buttonQuitter.addActionListener(e ->{
            System.exit(0);
        });

        contentPane.add(title);
        contentPane.add(buttonPlaySolo);
        contentPane.add(buttonPlayMulti);
        contentPane.add(buttonQuitter);

        setContentPane(contentPane);
        setVisible(true);
    }


    public void creerJeuMulti(){
        joueurs.clear();
        playType=2;
        joueurs.add(new Joueur(4, this));
        joueurs.add(new Joueur(4, this));

        //JPanel principale
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        contentPane.add(afficherRegles(), BorderLayout.NORTH);

        JPanel joueursPanel = new JPanel(new GridLayout(1, 2, 50, 20));
        //affichage du joueur 1
        joueursPanel.add(joueurs.get(0).afficherJoueur(),BorderLayout.WEST);
        //affichage du joueur 2
        joueursPanel.add(joueurs.get(1).afficherJoueur(), BorderLayout.EAST);
        contentPane.add(joueursPanel);
        setContentPane(contentPane);
    }

    public void creerJeuSolo(){
        joueurs.clear();
        playType=1;
        joueurs.add(new Joueur(4, this));

        //JPanel principale
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        contentPane.add(afficherRegles(), BorderLayout.NORTH);

        //affichage du joueur 1
        contentPane.add(joueurs.get(0).afficherJoueur(),BorderLayout.CENTER);
        setContentPane(contentPane);
    }

    public JPanel afficherRegles(){
        //JPanel pour les informations
        JPanel infoPanel = new JPanel(new GridLayout(0, 3));
        infoPanel.setPreferredSize(new Dimension(4 * PIXEL_PER_SQUARE * 2, PIXEL_PER_SQUARE));

        //Affichage du titre
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("2048");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.darkGray);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        titlePanel.add(titleLabel);

        //Affichage des règles
        JPanel rules1Panel = new JPanel(new GridLayout(2,1));
        JLabel rules1Label = new JLabel("Utiliser les flèches pour jouer");
        rules1Label.setHorizontalAlignment(JLabel.RIGHT);
        rules1Label.setForeground(Color.darkGray);
        rules1Label.setFont(new Font("Serif", Font.PLAIN, 15));
        rules1Panel.add(rules1Label);
        JLabel rules2Label = new JLabel("Appuyer sur ECHAP pour revenir au menu");
        rules2Label.setHorizontalAlignment(JLabel.RIGHT);
        rules2Label.setForeground(Color.darkGray);
        rules2Label.setFont(new Font("Serif", Font.PLAIN, 15));
        rules1Panel.add(rules2Label);

        JPanel rules2Panel = new JPanel(new GridLayout(2,1));
        JLabel rules1Label2 = new JLabel("Appuyer sur RETOUR pour revenir un coup en arrière");
        rules1Label2.setHorizontalAlignment(JLabel.LEFT);
        rules1Label2.setForeground(Color.darkGray);
        rules1Label2.setFont(new Font("Serif", Font.PLAIN, 15));
        rules2Panel.add(rules1Label2);
        JLabel rules2Label2 = new JLabel("Appuyer sur ENTREE pour recommencer la partie");
        rules2Label2.setHorizontalAlignment(JLabel.LEFT);
        rules2Label2.setForeground(Color.darkGray);
        rules2Label2.setFont(new Font("Serif", Font.PLAIN, 15));
        rules2Panel.add(rules2Label2);

        infoPanel.add(rules1Panel);
        infoPanel.add(titlePanel);
        infoPanel.add(rules2Panel);
        return infoPanel;
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

                if(playType==1){
                    creerJeuSolo();
                } else if (playType==2) {
                    creerJeuMulti();
                }
            });

            menu.addActionListener(e -> {
                jd.setVisible(false);
                joueurs.clear();
                afficherMenu();
            });

            debloquer.addActionListener(e -> {
                jd.setVisible(false);
                Joueur joueur = joueurs.get(0);
                Jeu jeu = joueur.jeu;
                jeu.deblocage();
                jeu.gameRunning=true;
                joueur.rafraichir();
            });

            jd.add(gameOver);
            jd.add(message);
            jd.add(rejouer);
            jd.add(menu);
            if(joueurs.get(0).jeu.nombreDeblocage>0 && playType==1) jd.add(debloquer);


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
                Action action = null;
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : action = Action.gauche; break;
                    case KeyEvent.VK_RIGHT : action = Action.droite; break;
                    case KeyEvent.VK_DOWN : action = Action.bas; break;
                    case KeyEvent.VK_UP : action = Action.haut; break;
                    case KeyEvent.VK_ENTER : action = Action.start; break;
                    case KeyEvent.VK_BACK_SPACE : action = Action.back; break;
                    case KeyEvent.VK_ESCAPE : afficherMenu(); break;
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