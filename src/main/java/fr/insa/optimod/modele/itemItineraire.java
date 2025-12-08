package fr.insa.optimod.modele;

public class itemItineraire {
    private int id;
    private Boolean estPickup;
    private String titre;
    private String adresse;
    private String heure;
    private int index;

    public itemItineraire(int id, Boolean estPickup, String titre, String adresse, String heure, int index) {
        this.id = id;
        this.estPickup = estPickup;
        this.titre = titre;
        this.adresse = adresse;
        this.heure = heure;
        this.index = index;
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

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
