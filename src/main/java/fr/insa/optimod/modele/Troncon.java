package fr.insa.optimod.modele;

public class Troncon {
    protected Integer destination;
    protected Integer origine;
    protected Double longueur;
    protected String nomRue;


    public Troncon(Integer destination, Integer origine, Double longueur, String nomRue) {
        this.destination = destination;
        this.origine = origine;
        this.longueur = longueur;
        this.nomRue = nomRue;
    }

    public Integer getDestination() {
        return destination;
    }

    public Integer getOrigine() {
        return origine;
    }

    public Double getLongueur() {
        return longueur;
    }

    public String getNomRue() {
        return nomRue;
    }
}
