package modele;

import java.awt.Point;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;

/**
 * Classe qui gère la grille de jeu
 */
public class Jeu extends Observable {

    private Case[][] tabCases;
    private static Random rnd = new Random(4);

    private HashMap<Case,Point> map;

    public Jeu(int size) {
        tabCases = new Case[size][size];
        map = new HashMap<Case,Point>();

        rnd();
    }

    /**
     * Getter pour accéder à la taille de la grille de jeu
     * @return tabCase.length
     */
    public int getSize() {
        return tabCases.length;
    }

    /**
     * Retourne la case de parametres x et y
     * @param x Ligne de la case à retourner
     * @param y Colonne de la case à retourner
     * @return Case de coordonnées x et y
     */
    public Case getCase(int x, int y) {
        return tabCases[x][y];
    }

    /**
     * Retourne la case adjacente de la case c dans la direction d
     * @param d Direction de recherche
     * @param c Case à partir de laquelle on recherche
     * @return Case adjacente de la case c dans la direction d ou une case de valeur -1 si on atteint une bordure
     */
    public Case getCaseAdj(Direction d, Case c) {
        Point p = (Point)map.get(c).clone();
        //se décale d'une case dans la direction d
        switch(d) {
            case gauche :
                p.y -= 1;
                break;

            case droite :
                p.y += 1;
                break;

            case haut :
                p.x -= 1;
                break;

            case bas :
                p.x += 1;
                break;
        }
        // retourne une case contenant -1 si on dépasse les bordures
        return (p.x<0 || p.y <0 || p.x>=this.getSize() || p.y>=this.getSize())?new Case(-1, this):tabCases[p.x][p.y];
    }

    /**
     * Déplace la case c dans la direction
     * @param d Direction dans laquelle déplacer la case
     * @param c Case à déplacer
     */
    public void moveCase(Direction d, Case c) {
        Case caseAdj = getCaseAdj(d, c);
        Point pAdj = map.get(caseAdj);

        //On bouge la case dans le tableau
        tabCases[pAdj.x][pAdj.y] = c;
        //On libère la case initiale
        delete(c);
        //On bouge la case dans la map
        map.put(c, pAdj);

    }

    /**
     * Supprime la case en mettant sa valeur à 0
     * @param c Case à supprimer
     */
    public void delete(Case c) {
        //récupère la position de la case a supprimer
        Point p = map.get(c);
        tabCases[p.x][p.y] = new Case(0, this);
        map.put(tabCases[p.x][p.y],p);
    }

    /**
     * Fonction principale qui déplace toutes les cases dans un direction
     * Le balayage se fait de gauche à droite et de haut en bas pour les direction "gauche" et "haut"
     * Le balayage se fait de droite à gauche et de bas en haut pour les direction "droite" et "bas"
     * @param d Direction dans laquelle déplacer toutes les cases
     */
    public void update(Direction d){
        //new Thread() { // permet de libérer le processus graphique ou de la console
            //public void run() {
                //parcours du tableau
                switch (d) {
                    case gauche, haut:
                        for (int x = 0; x < tabCases.length; x++) {
                            for (int y = 0; y < tabCases.length; y++) {
                                tabCases[x][y].move(d);
                            }
                        }
                        break;
                    case droite, bas:
                        for (int x = tabCases.length - 1; x >= 0; x--) {
                            for (int y = tabCases.length -1; y >= 0; y--) {
                                tabCases[x][y].move(d);
                            }
                        }
                        break;
                    default:
                        System.out.println("default");
                        break;
                }
            //}
        //}.start();
        if (!drawCase()) endGame();

        setChanged();
        notifyObservers();
    }

    /**
     * Tire une nouvelle case dans la grille de jeu
     * La position de la nouvelle case est aléatoire parmi celles libres
     * La valeur de la nouvelle case est aléatoire, soit 2, soit 4
     * @return true si le tirage est effectué avec succès, false si la grille est pleine
     */
    public boolean drawCase () {
        //on regarde si la partie est finie
        if (boardIsFull()) return false;

        int x,y,r;
        //choisir une case libre aléatoire
        do {
            x = rnd.nextInt(3);
            y = rnd.nextInt(3);
        }while (tabCases[x][y].getValeur() != 0);

        r = rnd.nextInt(2);
        //entrer la valeur 2 ou 4 dans la case
        tabCases[x][y].setValeur((r + 1) * 2);
        return true;
    }

    /**
     * Vérifie si une case est disponible (valeur = 0) dans la grille
     * @return true si la grille est pleine, false sinon
     */
    public boolean boardIsFull () {
        for (int x = 0; x < tabCases.length; x++) {
            for (int y = 0; y < tabCases.length; y++) {
                if (tabCases[x][y].getValeur() == 0) return false;
            }
        }
        return true;
    }

    /**
     * Termine la  partie en cours
     */
    public void endGame () {

    }

    /**
     * Tire une nouvelle grille aléatoirement, le nombre de cases remplies est aléatoire
     */
    public void rnd() {
        new Thread() { // permet de libérer le processus graphique ou de la console
            public void run() {
                int r;

                for (int i = 0; i < tabCases.length; i++) {
                    for (int j = 0; j < tabCases.length; j++) {
                        r = rnd.nextInt(3);

                        switch (r) {
                            case 0:
                                tabCases[i][j] = new Case(0, Jeu.this);
                                break;
                            case 1:
                                tabCases[i][j] = new Case(2, Jeu.this);
                                break;
                            case 2:
                                tabCases[i][j] = new Case(4,Jeu.this);
                                break;
                        }
                        Point p = new Point(i,j);
                        map.put(tabCases[i][j], p);
                    }
                }
            }

        }.start();


        setChanged();
        notifyObservers();


    }

}
