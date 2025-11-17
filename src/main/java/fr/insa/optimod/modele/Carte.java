package fr.insa.optimod.modele;
import java.util.ArrayList;
public class Carte {
    protected ArrayList<Noeud> listeNoeuds;
    protected ArrayList<Troncon> listeTroncon;

    public Carte(ArrayList<Noeud> listeNoeuds, ArrayList<Troncon> listeTroncon) {
        this.listeNoeuds = listeNoeuds;
        this.listeTroncon = listeTroncon;
    }

    public ArrayList<Noeud> getListeNoeuds() {
        return listeNoeuds;
    }

    public ArrayList<Troncon> getListeTroncon() {
        return listeTroncon;
    }
}
