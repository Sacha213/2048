package modele;

/**
 * Classe gérant chaque case de la grille
 */
public class Case {
    private int valeur;
    private Jeu jeu;
    public boolean aFusionne;

    /**
     * Constructeur de la classe
     * @param _valeur Valeur d'une case, 0 pour une case vide
     * @param _jeu Référence sur la classe Jeu qui instancie la case
     */
    public Case(int _valeur, Jeu _jeu) {
        valeur = _valeur;
        jeu = _jeu;
        aFusionne = false;
    }

    /**
     * Getter pour accéder à la valeur d'une case
     * @return Valeur de la case
     */
    public int getValeur() {
        return valeur;
    }

    /**
     * Setter pour changer la valeur d'une case
     * @param _valeur Nouvelle valeur de la case
     */
    public void setValeur(int _valeur) {
        valeur = _valeur;
    }

    /**
     * Décide si la case doit de déplacer ou fusionner dans la direction d
     * @param d Direction dans laquelle la case veut se déplacer
     */
    public boolean move (Action d) {
        boolean ret = false;
        if(valeur == 0) return ret;

        Case caseAdj;
        do{
            caseAdj = jeu.getCaseAdj(d, this);
            if (caseAdj.getValeur() == 0) {
                jeu.moveCase(d, this);
                ret = true;
            }

            //Fusion
            if (caseAdj.aFusionne) break;
            if (caseAdj.valeur == this.valeur) {
                caseAdj.valeur=this.valeur*2;
                jeu.score += caseAdj.valeur;
                //Suppression de la case
                jeu.delete(this);
                ret = true;
                break;
            }

        }while (caseAdj.valeur == 0);
        return ret;
    }

}
