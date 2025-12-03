package fr.insa.optimod.controleur;

import fr.insa.optimod.modele.*;
import fr.insa.optimod.vue.Interface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PointsControleur implements Initializable {

    Carte carte = null;

    private Interface interfaceUtilisateur;
    private Controleur controleurMetier;

    @FXML
    private Canvas canvasCarte;


    @FXML
    private GridPane itemsGrid;
    private List<Item> items;

    private GraphicsContext gc;

    public void setInterface(Interface interfaceUtilisateur) {
        this.interfaceUtilisateur = interfaceUtilisateur;
    }

    public void setControleurMetier(Controleur controleurMetier) {
        this.controleurMetier = controleurMetier;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = canvasCarte.getGraphicsContext2D();
        items = new ArrayList<>(data());

        int column = 0;
        int row = 1;

        try {
        for (int i=0; i<items.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/layouts/item.fxml"));

            HBox itemHBox = fxmlLoader.load();

            ItemControleur itemControleur = fxmlLoader.getController();
            if (column == 1) {
                column = 0;
                ++row;
            }

            itemsGrid.add(itemHBox, column++, row);


        }} catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Item> data() {
        List<Item> ls = new ArrayList<>();

        Item item = new Item();
        item.setTitre("Pickup #1");
        item.setColoredTag("#00A0ff");
        item.setSvgSrc("M0.000222027 17.4546V4.3869e-05H7.21045C8.51727 4.3869e-05 9.64511 0.255726 10.594 0.76709C11.5485 1.27277 12.2843 1.98016 12.8014 2.88925C13.3184 3.79266 13.5769 4.84379 13.5769 6.04266C13.5769 7.2472 13.3127 8.30118 12.7843 9.20459C12.2616 10.1023 11.5144 10.7983 10.5428 11.2927C9.57124 11.787 8.41784 12.0341 7.08261 12.0341H2.63374V8.71027H6.29852C6.93488 8.71027 7.46613 8.59948 7.89227 8.37789C8.32409 8.15629 8.65079 7.84663 8.87238 7.44891C9.09397 7.0455 9.20477 6.57675 9.20477 6.04266C9.20477 5.50288 9.09397 5.03698 8.87238 4.64493C8.65079 4.2472 8.32409 3.94039 7.89227 3.72448C7.46045 3.50857 6.9292 3.40061 6.29852 3.40061H4.21897V17.4546H0.000222027Z");
        ls.add(item);

        Item item2 = new Item();
        item2.setTitre("Pickup #2");
        item2.setColoredTag("#00A0ff");
        item2.setSvgSrc("M0.000222027 17.4546V4.3869e-05H7.21045C8.51727 4.3869e-05 9.64511 0.255726 10.594 0.76709C11.5485 1.27277 12.2843 1.98016 12.8014 2.88925C13.3184 3.79266 13.5769 4.84379 13.5769 6.04266C13.5769 7.2472 13.3127 8.30118 12.7843 9.20459C12.2616 10.1023 11.5144 10.7983 10.5428 11.2927C9.57124 11.787 8.41784 12.0341 7.08261 12.0341H2.63374V8.71027H6.29852C6.93488 8.71027 7.46613 8.59948 7.89227 8.37789C8.32409 8.15629 8.65079 7.84663 8.87238 7.44891C9.09397 7.0455 9.20477 6.57675 9.20477 6.04266C9.20477 5.50288 9.09397 5.03698 8.87238 4.64493C8.65079 4.2472 8.32409 3.94039 7.89227 3.72448C7.46045 3.50857 6.9292 3.40061 6.29852 3.40061H4.21897V17.4546H0.000222027Z");
        ls.add(item2);


        return ls;
    }

//    @FXML
//    private void initialize() {
//        System.out.println("initialize CarteControleur");
//        gc = canvasCarte.getGraphicsContext2D();
//    }

    @FXML
    private void retourAccueil() {
        try {
            interfaceUtilisateur.afficherAccueil();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void retourCarte() {
        try {
            interfaceUtilisateur.afficherCarte();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void afficherCarte() {
        carte = controleurMetier.getCarte();
        if (carte != null) {
            System.out.println("Affichage de la carte");
            gc.clearRect(0, 0, canvasCarte.getWidth(), canvasCarte.getHeight());
            gc.setFill(Color.web("#F4E6DF"));
            gc.fillRect(0, 0, canvasCarte.getWidth(), canvasCarte.getHeight());
            for (Troncon troncon : carte.getListeTroncon()) {
                Noeud dep = carte.getMapNoeuds().get(troncon.getOrigine());
                double x1 = (dep.getLongitude() - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
                double y1 = (dep.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat());
                Noeud arr = carte.getMapNoeuds().get(troncon.getDestination());
                double x2 = (arr.getLongitude() - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
                double y2 = (arr.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat());

                gc.setStroke(Color.web("#6B3F3A"));
                gc.setLineWidth(2);
                gc.strokeLine(x1, y1, x2, y2);
            }

        }
    }

    public void afficherPoints() {
        DemandeDeLivraions demandeDeLivraions = controleurMetier.getDemandeDeLivraions();

        int index = 1;
        for (Livraison livraison : demandeDeLivraions.getListeLivraisons()) {
            Color couleur = Couleur.getCouleur(index);

            Noeud noeud = carte.obtenirNoeud(livraison.getAdresseEnlevement());
            double x = (noeud.getLongitude() - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
            double y = (noeud.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat());

            gc.setFill(couleur);
            int rayon = 10;
            gc.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);

            noeud = carte.obtenirNoeud(livraison.getAdresseLivraison());
            x = (noeud.getLongitude() - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
            y = (noeud.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat());

            gc.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
            index++;
        }
    }

    public void afficher_points_textuels() {
        DemandeDeLivraions demandeDeLivraions = controleurMetier.getDemandeDeLivraions();
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (Livraison livraison : demandeDeLivraions.getListeLivraisons()) {
            Color couleur = Couleur.getCouleur(index);
            int r = (int) Math.round(couleur.getRed() * 255);
            int g = (int) Math.round(couleur.getGreen() * 255);
            int b = (int) Math.round(couleur.getBlue() * 255);
            sb.append("Livraison : ").append(String.format("#%02X%02X%02X", r, g, b)).append("\n");
            sb.append("  Adresse d'enlèvement: ").append(livraison.getAdresseEnlevement()).append("\n");
            sb.append("  Adresse de livraison: ").append(livraison.getAdresseLivraison()).append("\n");
            sb.append("  Durée de la livraison: ").append(livraison.getDureeLivraison()).append(" secondes\n\n");
            index++;
        }
        textePoints.setText(sb.toString());
    }

    @FXML
    private void afficherItineraire() {
        controleurMetier.preparerPlanTournee(controleurMetier.getCarte(), controleurMetier.getDemandeDeLivraions());
        try {
            interfaceUtilisateur.afficherItineraire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
