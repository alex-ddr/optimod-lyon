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

public class PointsControleur {

    Carte carte = null;

    private Interface interfaceUtilisateur;
    private Controleur controleurMetier;

    @FXML
    private Canvas canvasCarte;

    @FXML
    private GridPane itemsGrid;
    private List<Item> items;
    private int itemId;

    private GraphicsContext gc;

    public void setInterface(Interface interfaceUtilisateur) {
        this.interfaceUtilisateur = interfaceUtilisateur;
    }

    public void setControleurMetier(Controleur controleurMetier) {
        this.controleurMetier = controleurMetier;
    }


    @FXML
    private void initialize() {
        System.out.println("initialize CarteControleur");
        gc = canvasCarte.getGraphicsContext2D();
    }

    public void initData() {
        items = new ArrayList<>(data());

        int column = 0;
        int row = 1;

        try {
            for (int i=0; i<items.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/layouts/item.fxml"));

                HBox itemHBox = fxmlLoader.load();

                ItemControleur itemControleur = fxmlLoader.getController();
                itemControleur.setData(items.get(i));

                if (column == 1) {
                    column = 0;
                    ++row;
                }

                itemsGrid.add(itemHBox, column++, row);
                GridPane.setMargin(itemHBox, new javafx.geometry.Insets(8));


            }} catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Item> data() {
        List<Item> ls = new ArrayList<>();

        DemandeDeLivraions demandeDeLivraions = controleurMetier.getDemandeDeLivraions();
        itemId = 1;

        for (Livraison l : demandeDeLivraions.getListeLivraisons()) {
            Item pickup = new Item(itemId, true, "Pickup #" + itemId, l.getAdresseEnlevement().toString());
            ls.add(pickup);


            Item livraison = new Item(itemId, false, "Livraison #" + itemId, l.getAdresseLivraison().toString());
            ls.add(livraison);

            itemId++;
        }

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
                double y1 = gc.getCanvas().getHeight() - ((dep.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat()));
                Noeud arr = carte.getMapNoeuds().get(troncon.getDestination());
                double x2 = (arr.getLongitude() - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
                double y2 = gc.getCanvas().getHeight() - ((arr.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat()));

                gc.setStroke(Color.web("#6B3F3A")); // ou 6B3F3A  ou 8C5752 et couleur pour tracer la tournée --> #D65C4F
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
            double y = gc.getCanvas().getHeight() - ((noeud.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat()));

            gc.setFill(couleur);
            int rayon = 10;
            gc.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);

            noeud = carte.obtenirNoeud(livraison.getAdresseLivraison());
            x = (noeud.getLongitude() - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
            y = gc.getCanvas().getHeight() - ((noeud.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat()));

            gc.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
            index++;
        }

        Noeud noeud = carte.obtenirNoeud(demandeDeLivraions.getEntrepot().getAdresss());
        double x = (noeud.getLongitude() - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
        double y = gc.getCanvas().getHeight() - ((noeud.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat()));

        int rayon = 20;

        double svgW = 576.0;
        double svgH = 512.0;
        double scale = 2.0 * rayon / svgW;
        double tx = x - (svgW * scale) / 2.0;
        double ty = y - (svgH * scale) / 2.0;

        gc.save();
        gc.translate(tx, ty);
        gc.scale(scale, scale);

        gc.setFill(Color.RED);
        gc.beginPath();
        gc.appendSVGPath("M575.8 255.5c0 18-15 32.1-32 32.1h-32l.7 160.2c0 2.7-.2 5.4-.5 8.1v16.2c0 22.1-17.9 40-40 40h-16c-1.1 0-2.2 0-3.3-.1c-1.4.1-2.8.1-4.2.1L416 512h-24c-22.1 0-40-17.9-40-40v-88c0-17.7-14.3-32-32-32h-64c-17.7 0-32 14.3-32 32v88c0 22.1-17.9 40-40 40h-55.9c-1.5 0-3-.1-4.5-.2c-1.2.1-2.4.2-3.6.2h-16c-22.1 0-40-17.9-40-40V360c0-.9 0-1.9.1-2.8v-69.7h-32c-18 0-32-14-32-32.1c0-9 3-17 10-24L266.4 8c7-7 15-8 22-8s15 2 21 7l255.4 224.5c8 7 12 15 11 24");
        gc.fill();
        gc.closePath();

        gc.restore();
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
