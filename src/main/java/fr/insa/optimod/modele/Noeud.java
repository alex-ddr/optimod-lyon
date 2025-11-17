package fr.insa.optimod.modele;

public class Noeud {
    protected Integer id;
    protected Double longitude;
    protected Double latitude;

    public Noeud(Double longitude, Integer id, Double latitude) {
        this.longitude = longitude;
        this.id = id;
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }

    public Integer getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }
}
