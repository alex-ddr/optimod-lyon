package fr.insa.optimod.modele;

import fr.insa.optimod.controleur.ItemCourseAAjouterControleur;
import fr.insa.optimod.modele.Couleur;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;

import java.awt.*;

public class ItemCourseAAjouter{
    private int id;

    private String titre;
    private long adressePickup;
    private long adresseLivraison;

    public ItemCourseAAjouter(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public long getAdressePickup() {
        return adressePickup;
    }

    public void setAdressePickup(long adressePickup) {
        this.adressePickup = adressePickup;
    }

    public long getAdresseLivraison() {
        return adresseLivraison;
    }

    public void setAdresseLivraison(long adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }
}