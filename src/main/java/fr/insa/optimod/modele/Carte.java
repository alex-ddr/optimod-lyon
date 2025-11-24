package fr.insa.optimod.modele;
import java.util.ArrayList;
import java.util.Map;

import java.util.HashMap;

public class Carte {
    protected ArrayList<Noeud> listeNoeuds;
    protected ArrayList<Troncon> listeTroncon;
    protected HashMap<Long, Noeud> mapNoeuds;

    public Carte(ArrayList<Noeud> listeNoeuds, ArrayList<Troncon> listeTroncon, HashMap<Long, Noeud> mapNoeuds) {
        this.listeNoeuds = listeNoeuds;
        this.listeTroncon = listeTroncon;
        this.mapNoeuds = mapNoeuds;
    }

    public Noeud obtenirNoeud(Long id) {

        Noeud noeud = mapNoeuds.get(id);
        return noeud;
    }

    public ArrayList<Noeud> getListeNoeuds() {
        return listeNoeuds;
    }

    public ArrayList<Troncon> getListeTroncon() {
        return listeTroncon;
    }

    public HashMap<Long, Noeud> getMapNoeuds() {
        return mapNoeuds;
    }
}
