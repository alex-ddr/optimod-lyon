package fr.insa.optimod.modele;

public class Troncon {
    protected Long destination;
    protected Long origine;
    protected Double longueur;
    protected String nomRue;
    protected int sens = 1;


    public Troncon(Long destination, Long origine, Double longueur, String nomRue) {
        this.destination = destination;
        this.origine = origine;
        this.longueur = longueur;
        this.nomRue = nomRue;
    }

    public void setSens(int sens) {
        this.sens = sens;
    }
    
    public int getSens() {
        return sens;
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
