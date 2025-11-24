package fr.insa.optimod.modele;
import java.util.ArrayList;
import java.util.HashMap;

public class Carte {
    protected ArrayList<Noeud> listeNoeuds;
    protected ArrayList<Troncon> listeTroncon;
    protected HashMap<Long, Double> Noeuds;

    public Carte(ArrayList<Noeud> listeNoeuds, ArrayList<Troncon> listeTroncon) {
        this.listeNoeuds = listeNoeuds;
        this.listeTroncon = listeTroncon;
    }

    public Carte(ArrayList<Troncon> listeTroncon, HashMap<Long, Double> noeuds) {
        this.listeTroncon = listeTroncon;
        Noeuds = noeuds;
    }

   public Noeud obtenirNoeud(Long id) {

        for (Noeud n : listeNoeuds) {
            if (n.getId().equals(id)) {
                return n;
            }
        }
        return null;
    }
/*
    public Noeud obtenirNoeud(Long id) {

        Noeud noeud = Noeuds.get(id);
        return noeud;
    }
*/
    public ArrayList<Noeud> getListeNoeuds() {
        return listeNoeuds;
    }

    public ArrayList<Troncon> getListeTroncon() {
        return listeTroncon;
    }
}
