package modele;

/**
 * Classe gérant un point 2D pour situer une case dans la grille
 */
public class Point {
    public int x;
    public int y;

    /**
     * Constructeur de la classe Point
     * @param x
     * @param y
     */
    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }

    /**
     * Permet de créer une nouvelle instance de point avec les memes valeurs que l'instance actuelle
     * @return
     */
    public Point clone(){
        return new Point(x,y);
    }

}
