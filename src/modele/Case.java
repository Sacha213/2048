package modele;

public class Case {
    private int valeur;
    private Jeu jeu;


    public Case(int _valeur) {
        valeur = _valeur;
    }

    public int getValeur() {
        return valeur;
    }

    public void move (Direction d) {
        Case v = jeu.get(d, this);
        while (v.valeur != -1){
            if (v == null) {
                jeu.move(d, this);
            }
            if (v.valeur == this.valeur) {

            }
        }
    }

}
