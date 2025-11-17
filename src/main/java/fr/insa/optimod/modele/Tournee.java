package fr.insa.optimod.modele;
import java.util.ArrayList;

public class Tournee {
    protected Entrepot entrepot;
    protected ArrayList<Livraison> listeLivraisons;

    public Tournee(Entrepot entrepot, ArrayList<Livraison> listeLivraisons) {
        this.entrepot = entrepot;
        this.listeLivraisons = listeLivraisons;
    }

    public Entrepot getEntrepot() {
        return entrepot;
    }

    public ArrayList<Livraison> getListeLivraisons() {
        return listeLivraisons;
    }
}
