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
    public void setValeur(int _valeur) {
        valeur = _valeur;
    }

    public void move (Direction d) {
        if(valeur == 0) return;

        Case caseAdj;
        do{
            caseAdj = jeu.getCaseAdj(d, this);
            if (caseAdj.getValeur() == 0) {
                System.out.println("appel jeu.moveCase()");
                jeu.moveCase(d, this);
                System.out.println("retour jeu.moveCase()");
            }

            //Fusion
            if (caseAdj.valeur == this.valeur) {
              caseAdj.valeur=this.valeur*2;
              //Suppression de la case
              jeu.delete(this);
              break;
            }

        }while (caseAdj.valeur == 0);
    }

}
