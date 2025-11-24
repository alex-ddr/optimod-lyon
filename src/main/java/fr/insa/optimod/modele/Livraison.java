package fr.insa.optimod.modele;

public class Livraison {
    protected Long adresseEnlevement;
    protected Long adresseLivraison;
    protected Long dureeEnlevement;
    protected Long dureeLivraison;

    public Livraison(Long adresseEnlevement, Long adresseLivraison, Long dureeLivraison, Long dureeEnlevement) {
        this.adresseEnlevement = adresseEnlevement;
        this.adresseLivraison = adresseLivraison;
        this.dureeLivraison = dureeLivraison;
        this.dureeEnlevement = dureeEnlevement;
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
}
