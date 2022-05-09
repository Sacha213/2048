import modele.Jeu;
import vue_controleur.Console2048;
import vue_controleur.Swing2048;

/**
 * Classe qui permet de lancer le jeu
 */
public class Main {

    /**
     * Fonction main du jeu, qui permet de choisir si l'on choisit la version graphique ou console
     * @param args
     */
    public static void main(String[] args) {
        //mainConsole();
        mainSwing();

    }

    /**
     * Fonction qui gère la version graphique du 2048
     */
    public static void mainConsole() {
        Jeu jeu = new Jeu(4);
        Console2048 vue = new Console2048(jeu);
        jeu.addObserver(vue);

        vue.start();

    }

    /**
     * Fonction qui gère la version console du 2048
     */
    public static void mainSwing() {

        Jeu jeu = new Jeu(4);
        Swing2048 vue = new Swing2048(jeu);
        jeu.addObserver(vue);

        vue.setVisible(true);


    }



}
