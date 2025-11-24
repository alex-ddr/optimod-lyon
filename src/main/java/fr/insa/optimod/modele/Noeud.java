package fr.insa.optimod.modele;

import java.util.ArrayList;

public class Noeud {
    protected Long id;
    protected Double longitude;
    protected Double latitude;
    protected ArrayList<Troncon> adjacense;

    public Noeud(Long id, Double longitude, Double latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.adjacense = new ArrayList<>();
    }

    public void ajouterTroncon(Troncon troncon) {
        this.adjacense.add(troncon);
        return;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Long getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public ArrayList<Troncon> getAdjacense() {
        return adjacense;
    }
}
