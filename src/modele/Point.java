package modele;

/**
 * Classe gérant un point 2D pour situer une case dans la grille
 */
public class Point {
    public int x;
    public int y;

    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }
    public Point clone(){
        return new Point(x,y);
    }

}
