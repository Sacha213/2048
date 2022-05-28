package modele;

import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;

import static java.lang.Integer.parseInt;

/**
 * Classe qui gère la grille de jeu
 */
public class Jeu extends Observable {

    private Case[][] tabCases;
    private static Random rnd = new Random();
    public boolean gameRunning = true;
    private HashMap<Case,Point> map;
    public int score, highScore, nombreDeblocage;

    public ArrayList<HashMap<Case,Point>> sauvegarde;

    public Jeu(int size) {
        score = 0;
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
        //On sauvegarde le jeu actuelle
        //sauvegarde.add(map);
        
        // permet de libérer le processus graphique ou de la console
        new Thread(() -> {
            //parcours du tableau
            boolean moved = false;

            switch (d) {
                case gauche, haut:
                    for (int x = 0; x < tabCases.length; x++) {
                        for (int y = 0; y < tabCases.length; y++) {
                            //on regarde s'il y a eu un mouvement
                            if (tabCases[x][y].move(d)) moved = true;
                        }
                    }
                    break;
                case droite, bas:
                    for (int x = tabCases.length - 1; x >= 0; x--) {
                        for (int y = tabCases.length -1; y >= 0; y--) {
                            //on regarde s'il y a eu un mouvement
                            if (tabCases[x][y].move(d)) moved = true;
                        }
                    }
                    break;
                default:
                    System.out.println("default");
                    break;
            }
            if (moved) {
                drawCase();
            }
            //On réinitialise la variable de fusion
            for (int x = 0; x < tabCases.length; x++) {
                for (int y = 0; y < tabCases.length; y++) {
                    tabCases[x][y].aFusionne = false;
                }
            }

            setChanged();
            notifyObservers();

            //on regarde si la partie est finie
            if(isGameOver()) endGame();

        }).start();



    }

    /**
     * Fonction qui permet de revenir en arrière dans le jeu
     * @param indice nombre de coups à enlever
     */
    public void goBack(int indice){
        //On actualise la map
        for (int i=0; i<indice; i++){
            sauvegarde.remove(sauvegarde.size()-i-1);
        }
        map = sauvegarde.get(sauvegarde.size()-1);

        //On actualise le tableau
        for (Case c: map.keySet()) {
            Point p = map.get(c);

            tabCases[p.x][p.y]=c;
        }


    }

    /**
     * Fonction qui permet de débloquer l'utilisateur s'il a perdu la partie
     * Cette fonction supprime les cases qui ont pour valeur 2 ou 4.
     */
    public void deblocage(){
        //On parcours le tableau
        for (int x = 0; x < tabCases.length; x++) {
            for (int y = 0; y < tabCases.length; y++) {
                Case c = tabCases[x][y];
                if (c.getValeur()==2 || c.getValeur() == 4 ) delete(c);
            }
        }
        nombreDeblocage--;
    }

    /**
     * Fonction qui vérifie si la partie est finie, c'est à dire si un mouvement est possible
     * @return true si la partie est finie, faux sinon
     */
    private boolean isGameOver(){
        boolean gameOver=true;

        for (int x = 0; x < tabCases.length; x++) {
            for (int y = 0; y < tabCases.length; y++) {
                Case c = tabCases[x][y];
                int caseValeur = c.getValeur();
                //on regarde si la case est libre
                if (caseValeur == 0) return false;
                //on regarde toutes les cases adjacentes
                if(getCaseAdj(Direction.gauche,c).getValeur() == caseValeur)return false;
                if(getCaseAdj(Direction.droite,c).getValeur() == caseValeur)return false;
                if(getCaseAdj(Direction.haut,c).getValeur() == caseValeur)return false;
                if(getCaseAdj(Direction.bas,c).getValeur() == caseValeur)return false;
            }
        }

        return gameOver;

    }

    public void endGame() {
        System.out.println("Partie finie !");
        if(score > highScore) {
            highScore = score;
            File file = new File("./src/resources/score.txt");
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(String.valueOf(score));
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
        /*
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
        gameRunning = false;
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
        int x,y,r;
        //choisir une case libre aléatoire
        do {
            x = rnd.nextInt(4);
            y = rnd.nextInt(4);
        }while (tabCases[x][y].getValeur() != 0);

        r = rnd.nextInt(2);
        //entrer la valeur 2 ou 4 dans la case
        tabCases[x][y].setValeur((r + 1) * 2);
        return true;
    }

    /**
     * Tire une nouvelle grille aléatoirement, le nombre de cases remplies est aléatoire
     */
    public void rnd() {
        sauvegarde = new ArrayList<>();
        // permet de libérer le processus graphique ou de la console
        new Thread(() -> {
            int r;
            score = 0;
            nombreDeblocage =3;
            File file = new File("./src/resources/score.txt");
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
                highScore = parseInt(br.readLine());
            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture du record : " + e);
            }


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
            gameRunning = true;
            setChanged();
            notifyObservers();
        }).start();
    }

    /**
     * Fonction de debug qui affiche la grille de jeu dans la console
     */
    public void afficherTableau() {
        System.out.println("\n");
        for (int x = 0; x < tabCases.length; x++) {
            System.out.print("|");
            for (int y = 0; y < tabCases.length; y++) {
                System.out.print(" " + tabCases[x][y].getValeur() + " |");
            }
            System.out.println("");
        }
    }

}
