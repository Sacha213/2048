package modele;

import java.awt.Point;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;

public class Jeu extends Observable {

    private Case[][] tabCases;
    private static Random rnd = new Random(4);

    private HashMap<Case,Point> map;

    public Jeu(int size) {
        tabCases = new Case[size][size];
        map = new HashMap<Case,Point>();

        rnd();
    }

    public int getSize() {
        return tabCases.length;
    }

    public Case getCase(int i, int j) {
        return tabCases[i][j];
    }

    public Case getCaseAdj(Direction d, Case c) {
        Point p = (Point)map.get(c).clone();
        System.out.println("exec getCaseAdj x: " + p.x + ", y: " + p.y + ", d : " + d);
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
        Case ret = (p.x<0 || p.y <0 || p.x>=this.getSize() || p.y>=this.getSize())?new Case(-1, this):tabCases[p.x][p.y];
        System.out.println("valeur case adj : " + ret.getValeur());
        return ret;
    }


    public void moveCase(Direction d, Case c) {
        System.out.println("exec jeu.moveCase()");
        Case caseAdj = getCaseAdj(d, c);
        Point pAdj = map.get(caseAdj);

        //On bouge la case dans le tableau
        tabCases[pAdj.x][pAdj.y] = c;
        //On libère la case initiale
        System.out.println("appel jeu.delete()");
        delete(c);
        //On bouge la case dans la map
        map.put(c, pAdj);

    }

    public void delete(Case c) {
        System.out.println("exec jeu.delete()");
        //récupère la position de la case a supprimer
        Point p = map.get(c);
        tabCases[p.x][p.y] = new Case(0, this);
        map.put(tabCases[p.x][p.y],p);
    }

    public void update(Direction d){
        //new Thread() { // permet de libérer le processus graphique ou de la console
            //public void run() {
                System.out.println("parcours");
                //parcours du tableau
                switch (d) {
                    case gauche, haut:
                        System.out.println("parcours a gauche");
                        for (int x = 0; x < tabCases.length; x++) {
                            for (int y = 0; y < tabCases.length; y++) {
                                System.out.println("move case x: " + x + ", y : "+ y);
                                tabCases[x][y].move(d);
                            }
                        }
                        break;
                    case droite:
                        System.out.println("parcours a droite");
                        for (int x = 0; x < tabCases.length; x++) {
                            for (int y = tabCases.length -1; y >= 0; y--) {
                                System.out.println("move case x: " + x + ", y : "+ y);
                                tabCases[x][y].move(d);
                            }
                        }
                        break;
                    case bas:
                        System.out.println("parcours en bas");
                        for (int x = tabCases.length -1; x >= 0; x--) {
                            for (int y = 0; y < tabCases.length; y++) {
                                System.out.println("move case x: " + x + ", y : "+ y);
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
        drawCase();

        setChanged();
        notifyObservers();
    }

    public void drawCase () {
        int x,y,r;
        //choisir une case libre aléatoire
        do {
            x = rnd.nextInt(3);
            y = rnd.nextInt(3);
        }while (tabCases[x][y].getValeur() != 0);

        r = rnd.nextInt(2);
        //entrer la valeur 2 ou 4 dans la case
        tabCases[x][y].setValeur((r + 1) * 2);
    }


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
