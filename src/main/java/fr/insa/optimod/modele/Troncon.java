package fr.insa.optimod.modele;

public class Troncon {
    protected Long destination;
    protected Long origine;
    protected Double longueur;
    protected String nomRue;


    public Troncon(Long destination, Long origine, Double longueur, String nomRue) {
        this.destination = destination;
        this.origine = origine;
        this.longueur = longueur;
        this.nomRue = nomRue;
    }

    public Long getDestination() {
        return destination;
    }

    public Long getOrigine() {
        return origine;
    }

    public Double getLongueur() {
        return longueur;
    }

    public String getNomRue() {
        return nomRue;
    }
}
