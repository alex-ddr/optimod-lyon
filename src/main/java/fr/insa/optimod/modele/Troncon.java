package fr.insa.optimod.modele;

public class Troncon {
    protected int destination;
    protected int origine;
    protected Double longueur;
    protected String nomRue;


    public Troncon(int destination, int origine, Double longueur, String nomRue) {
        this.destination = destination;
        this.origine = origine;
        this.longueur = longueur;
        this.nomRue = nomRue;
    }

    public int getDestination() {
        return destination;
    }

    public int getOrigine() {
        return origine;
    }

    public Double getLongueur() {
        return longueur;
    }

    public String getNomRue() {
        return nomRue;
    }
}
