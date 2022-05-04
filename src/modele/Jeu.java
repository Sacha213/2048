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

    public Case get(Direction d, Case c) {
        Point p = map.get(c);

        switch(d) {
            case gauche :
                p.x -= 1;
                break;

            case droite :
                p.x += 1;
                break;

            case haut :
                p.y -= 1;
                break;

            case bas :
                p.y += 1;
                break;
        }

        return tabCases[p.x][p.y];
    }

    public void move(Direction d, Case c) {
        Case caseAdj = get(d, c);
        caseAdj = c;
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
                                tabCases[i][j] = null;
                                break;
                            case 1:
                                tabCases[i][j] = new Case(2);
                                break;
                            case 2:
                                tabCases[i][j] = new Case(4);
                                break;
                        }
                    }
                }
            }

        }.start();


        setChanged();
        notifyObservers();


    }

}
