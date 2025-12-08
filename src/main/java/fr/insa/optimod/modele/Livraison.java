package fr.insa.optimod.modele;

public class Livraison {
    private int id;
    protected Long adresseEnlevement;
    protected Long adresseLivraison;
    protected Long dureeEnlevement;
    protected Long dureeLivraison;

    public Livraison(Long adresseEnlevement, Long adresseLivraison, Long dureeLivraison, Long dureeEnlevement) {
        this.adresseEnlevement = adresseEnlevement;
        this.adresseLivraison = adresseLivraison;
        this.dureeLivraison = dureeLivraison;
        this.dureeEnlevement = dureeEnlevement;
        this.id = -1;
    }

    public Long getAdresseEnlevement() {
        return adresseEnlevement;
    }

    public Long getAdresseLivraison() {
        return adresseLivraison;
    }

    public Long getDureeEnlevement() {
        return dureeEnlevement;
    }

    public Long getDureeLivraison() {
        return dureeLivraison;
    }

    public void setAdresseEnlevement(Long adresseEnlevement) {
        this.adresseEnlevement = adresseEnlevement;
    }
    public void setAdresseLivraison(Long adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
