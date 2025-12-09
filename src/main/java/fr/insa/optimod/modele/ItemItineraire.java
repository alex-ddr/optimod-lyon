package fr.insa.optimod.modele;

public class ItemItineraire {
    private int id;
    private Boolean estPickup;
    private String titre;
    private String adresse;
    private String heure;
    private int index;

    private boolean peutMonter = true;
    private boolean peutDescendre = true;

    public boolean isPeutMonter() { return peutMonter; }
    public void setPeutMonter(boolean peutMonter) { this.peutMonter = peutMonter; }

    public boolean isPeutDescendre() { return peutDescendre; }
    public void setPeutDescendre(boolean peutDescendre) { this.peutDescendre = peutDescendre; }

    public ItemItineraire(int id, Boolean estPickup, String titre, String adresse, String heure, int index) {
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


    public static String convertirHeure(String heureEnSecondes) {
        try {
            double totalSecondes = Double.parseDouble(heureEnSecondes);

            int h = (int) (totalSecondes / 3600);
            int m = (int) ((totalSecondes % 3600) / 60);

            h = h % 24;

            return String.format("%02dh%02d", h, m);
        } catch (NumberFormatException e) {
            return "--:--";
        }
    }

}
