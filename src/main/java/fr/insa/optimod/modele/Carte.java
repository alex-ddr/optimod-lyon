package fr.insa.optimod.modele;
import java.util.ArrayList;
import java.util.Map;

import java.util.HashMap;

public class Carte {
    protected ArrayList<Noeud> listeNoeuds;
    protected ArrayList<Troncon> listeTroncon;
    protected HashMap<Long, Noeud> mapNoeuds;

    protected Double minLat;
    protected Double minLong;
    protected Double maxLat;
    protected Double maxLong;

    public Carte(ArrayList<Noeud> listeNoeuds, ArrayList<Troncon> listeTroncon, HashMap<Long, Noeud> mapNoeuds, Double minLat, Double minLong, Double maxLat, Double maxLong) {
        this.listeNoeuds = listeNoeuds;
        this.listeTroncon = listeTroncon;
        this.mapNoeuds = mapNoeuds;
        this.minLat = minLat;
        this.minLong = minLong;
        this.maxLat = maxLat;
        this.maxLong = maxLong;
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

    public Double getMinLat() {
        return minLat;
    }
    public Double getMinLong() {
        return minLong;
    }
    public Double getMaxLat() {
        return maxLat;
    }
    public Double getMaxLong() {
        return maxLong;
    }
}
