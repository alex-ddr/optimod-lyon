package fr.insa.optimod.modele;
import java.util.ArrayList;

public class DemandeDeLivraions {
    protected Entrepot entrepot;
    protected ArrayList<Livraison> listeLivraisons;

    public DemandeDeLivraions(Entrepot entrepot, ArrayList<Livraison> listeLivraisons) {
        this.entrepot = entrepot;
        this.listeLivraisons = listeLivraisons;
    }

    public Entrepot getEntrepot() {
        return entrepot;
    }

    public ArrayList<Livraison> getListeLivraisons() {
        return listeLivraisons;
    }

    public Livraison getLivraison(Long adresse) {
        for (Livraison livraison : listeLivraisons) {
            if (livraison.getAdresseLivraison().equals(adresse) || livraison.getAdresseEnlevement().equals(adresse)) {
                return livraison;
            }
        }
        return null;
    }

    public void ajouterLivraison(Livraison livraison) {
        listeLivraisons.add(livraison);
    }

    public void supprimerLivraison(Livraison livraison) {
        listeLivraisons.remove(livraison);
    }

    public boolean estEntrepot(Long adresse) {
        return this.entrepot.getAdresss().equals(adresse);
    }
}
