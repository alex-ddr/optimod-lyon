package fr.insa.optimod.modele;

public class Livraison {
    protected Integer adresseEnlevement;
    protected Integer adresseLivraison;
    protected Integer dureeEnlevement;
    protected Integer dureeLivraison;

    public Livraison(Integer adresseEnlevement, Integer adresseLivraison, Integer dureeEnlevement, Integer dureeLivraison) {
        this.adresseEnlevement = adresseEnlevement;
        this.adresseLivraison = adresseLivraison;
        this.dureeEnlevement = dureeEnlevement;
        this.dureeLivraison = dureeLivraison;
    }

    public Integer getadresseEnlevement() {
        return adresseEnlevement;
    }

    public Integer getadresseLivraison() {
        return adresseLivraison;
    }

    public Integer getDureeEnlevement() {
        return dureeEnlevement;
    }

    public Integer getDureeLivraison() {
        return dureeLivraison;
    }
}
