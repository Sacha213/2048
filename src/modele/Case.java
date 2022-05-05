package modele;

public class Case {
    private int valeur;
    private Jeu jeu;


    public Case(int _valeur, Jeu _jeu) {
        valeur = _valeur;
        jeu = _jeu;
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

            //Fusion
            if (v.valeur == this.valeur) {
              v.valeur=this.valeur*2;
              //Suppression de la case
                jeu.delete(this);
                break;
            }
        }
    }

}
