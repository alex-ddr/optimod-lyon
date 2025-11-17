package fr.insa.optimod.modele;

public class Noeud {
    protected Long id;
    protected Double longitude;
    protected Double latitude;

    public Noeud(Long id, Double longitude, Double latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
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
}
