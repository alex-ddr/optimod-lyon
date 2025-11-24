package fr.insa.optimod.modele;

public class Livraison {
    protected Integer adresseEnlevement;
    protected Integer adresseLivraison;
    protected Integer dureeEnlevement;
    protected Integer dureeLivraison;

    public Livraison(Integer adresseEnlevement, Integer adresseLivraison, Integer dureeLivraison, Integer dureeEnlevement) {
        this.adresseEnlevement = adresseEnlevement;
        this.adresseLivraison = adresseLivraison;
        this.dureeLivraison = dureeLivraison;
        this.dureeEnlevement = dureeEnlevement;
    }

    public Integer getAdresseEnlevement() {
        return adresseEnlevement;
    }

    public Integer getAdresseLivraison() {
        return adresseLivraison;
    }

    public Integer getDureeEnlevement() {
        return dureeEnlevement;
    }

    public Integer getDureeLivraison() {
        return dureeLivraison;
    }
}
