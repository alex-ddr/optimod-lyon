package fr.insa.optimod.modele;
import java.util.ArrayList;

public class DemandeDeLivraions {
    protected Entrepot entrepot;
    protected ArrayList<Livraison> listeLivraisons;
    private int compteurId;

    public DemandeDeLivraions(Entrepot entrepot, ArrayList<Livraison> listeLivraisons) {
        this.entrepot = entrepot;
        this.listeLivraisons = listeLivraisons;
        this.compteurId = 1;

        if (this.listeLivraisons != null) {
            for (Livraison l : this.listeLivraisons) {
                l.setId(this.compteurId++);
            }
        }
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
        livraison.setId(this.compteurId++);
        listeLivraisons.add(livraison);
    }

    public Livraison getLivraisonParId(int id) {
        for (Livraison l : listeLivraisons) {
            if (l.getId() == id) {
                return l;
            }
        }
        return null;
    }

    public void supprimerLivraison(Livraison livraison) {
        listeLivraisons.remove(livraison);
    }

    public boolean estEntrepot(Long adresse) {
        return this.entrepot.getAdresss().equals(adresse);
    }

    public int getCompteurId() {
        return compteurId;
    }
}
