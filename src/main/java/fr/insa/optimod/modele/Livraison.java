package fr.insa.optimod.modele;

public class Livraison {
    protected int adresseEnlevement;
    protected int adresseLivraison;
    protected int dureeEnlevement;
    protected int dureeLivraison;

    public Livraison(int adresseEnlevement, int adresseLivraison, int dureeLivraison, int dureeEnlevement) {
        this.adresseEnlevement = adresseEnlevement;
        this.adresseLivraison = adresseLivraison;
        this.dureeLivraison = dureeLivraison;
        this.dureeEnlevement = dureeEnlevement;
    }

    public int getAdresseEnlevement() {
        return adresseEnlevement;
    }

    public int getAdresseLivraison() {
        return adresseLivraison;
    }

    public int getDureeEnlevement() {
        return dureeEnlevement;
    }

    public int getDureeLivraison() {
        return dureeLivraison;
    }
}
