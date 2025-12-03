package fr.insa.optimod.modele;

public class itemPoint {
    private int id;
    private Boolean estPickup;
    private String titre;
    private String adresse;

    public itemPoint(int id, Boolean estPickup, String titre, String adresse) {
        this.id = id;
        this.estPickup = estPickup;
        this.titre = titre;
        this.adresse = adresse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEstPickup(Boolean estPickup) {
        this.estPickup = estPickup;
    }

    public Boolean getEstPickup() {
        return estPickup;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

}
