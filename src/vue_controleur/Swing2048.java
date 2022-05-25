package vue_controleur;

import modele.Case;
import modele.Direction;
import modele.Jeu;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
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

    private HashMap<Integer, Color> colorMap;

    /**
     * Constructeur de la classe
     * @param _jeu Référence sur la grille du jeu
     */
    public Swing2048(Jeu _jeu) {
        jeu = _jeu;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(jeu.getSize() * PIXEL_PER_SQUARE, (jeu.getSize() +1) * (PIXEL_PER_SQUARE));
        JPanel contentPane = new JPanel(new BorderLayout());
        JPanel contentInfo = new JPanel();
        contentInfo.setPreferredSize(new Dimension(jeu.getSize() * PIXEL_PER_SQUARE, PIXEL_PER_SQUARE));
        contentInfo.setLayout(new BoxLayout(contentInfo,BoxLayout.X_AXIS));
        contentInfo.setBackground(Color.blue);
        JLabel title = new JLabel("2048");
        title.setFont(new Font("Serif", Font.PLAIN, 40));
        contentInfo.add(title);

        contentPane.add(contentInfo, BorderLayout.NORTH);


        setTitle("2/0/4/8");
        tabC = new JLabel[jeu.getSize()][jeu.getSize()];

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

        JPanel contentGame = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize()));

        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(Color.darkGray, 1);
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
        ajouterEcouteurClavier();
        rafraichir();

    }




    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     */
    private void rafraichir()  {
        //si la partie n'est pas finie
        if (jeu.gameRunning) {
            SwingUtilities.invokeLater(new Runnable() { // demande au processus graphique de réaliser le traitement
                @Override
                public void run() {
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
                }
            });
        }
        //si la partie est finie
        else {
            gameOver();
        }
    }

    private void gameOver(){
        System.out.println("game over");

        JDialog jd = new JDialog(this);

        jd.setLayout(new FlowLayout());

        jd.setBounds(500, 300, 400, 300);

        JLabel gameOver = new JLabel("Game Over");
        JLabel message = new JLabel("Voulez vous rejouer?");

        JButton rejouer = new JButton("Rejouer");
        JButton quitter = new JButton("Quitter");
        rejouer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jd.setVisible(false);
                System.out.println("Rejouer button pressed.");
            }
        });

        quitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jd.setVisible(false);
                System.out.println("Quitter button pressed.");
            }
        });

        jd.add(gameOver);
        jd.add(message);
        jd.add(rejouer);
        jd.add(quitter);
        jd.setVisible(true);
    }

    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et déclenche des traitements sur le modèle
     */
    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
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
        });
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