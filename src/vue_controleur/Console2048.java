package vue_controleur;

import modele.Action;
import modele.Case;
import modele.Jeu;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Classe gérant la version console du jeu 2048
 */
public class Console2048 extends Thread implements Observer {

    private Jeu jeu;

    /**
     * Constructeur de la classe
     * @param _jeu Référence sur la grille du jeu
     */
    public Console2048(Jeu _jeu) {
        jeu = _jeu;
    }


    @Override
    public void run() {
        while(true) {
            afficher();

            synchronized (this) {
                ecouteEvennementClavier();
                try {
                    wait(); // lorsque le processus s'endort, le verrou sur this est relâché, ce qui permet au processus de ecouteEvennementClavier()
                    // d'entrer dans la partie synchronisée, ce verrou évite que le réveil du processus de la console (update(..)) ne soit exécuté avant
                    // que le processus de la console ne soit endormi

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et déclenche des traitements sur le modèle
     */
    private void ecouteEvennementClavier() {

        final Object _this = this;

        new Thread() {
            public void run() {

                synchronized (_this) {
                    boolean end = false;

                    while (!end) {
                        String s = null;
                        try {
                            s = Character.toString((char)System.in.read());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Action action = null;
                        switch (s){
                            case "2":
                                action = Action.bas;
                                break;
                            case "4":
                                action = Action.gauche;
                                break;
                            case "6":
                                action = Action.droite;
                                break;
                            case "8":
                                action = Action.haut;
                                break;
                        }

                        if(action!=null){
                            end = true;
                            jeu.update(action);
                        }
                    }
                }

            }
        }.start();


    }

    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     */
    private void afficher()  {


        System.out.printf("\033[H\033[J"); // permet d'effacer la console (ne fonctionne pas toujours depuis la console de l'IDE)

        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Case c = jeu.getCase(i, j);
                if (c != null) {
                    System.out.format("%5.5s", c.getValeur());
                } else {
                    System.out.format("%5.5s", "");
                }

            }
            System.out.println();
        }

    }

    /**
     * Méthode permettant de rafraichir l'affichage du jeu
     */
    private void rafraichir() {
        synchronized (this) {
            try {
                notify();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
