package fr.insa.optimod.modele;
import java.util.ArrayList;
public class Carte {
    protected ArrayList<Noeud> listeNoeuds;
    protected ArrayList<Troncon> listeTroncon;

    public Carte(ArrayList<Noeud> listeNoeuds, ArrayList<Troncon> listeTroncon) {
        this.listeNoeuds = listeNoeuds;
        this.listeTroncon = listeTroncon;
    }

    public Noeud obtenirNoeud(Long id) {

        for (Noeud n : listeNoeuds) {
            if (n.getId().equals(id)) {
                return n;
            }
        }
        return null;
    }

    public ArrayList<Noeud> getListeNoeuds() {
        return listeNoeuds;
    }

    public ArrayList<Troncon> getListeTroncon() {
        return listeTroncon;
    }
}
