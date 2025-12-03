package fr.insa.optimod.controleur;

import fr.insa.optimod.modele.*;
import fr.insa.optimod.vue.Interface;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.util.ArrayList;

public class ItineraireControleur {

    Carte carte = null;

    private Interface interfaceUtilisateur;
    private Controleur controleurMetier;

    @FXML
    private Canvas canvasCarte;

    @FXML
    private TextArea textePoints;

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

    @FXML
    private void retourAccueil() {
        try {
            interfaceUtilisateur.afficherAccueil();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void retourPoints() {
        try {
            interfaceUtilisateur.afficherPoints();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void actualisationAuto() {
        System.out.println("actualisationAuto");
    }

    public void afficherCarte() {
        carte = controleurMetier.getCarte();
        if (carte != null) {
            System.out.println("Affichage de la carte");
            gc.clearRect(0, 0, canvasCarte.getWidth(), canvasCarte.getHeight());
            gc.setFill(Color.GRAY);
            gc.fillRect(0, 0, canvasCarte.getWidth(), canvasCarte.getHeight());
            for (Troncon troncon : carte.getListeTroncon()) {
                Noeud dep = carte.getMapNoeuds().get(troncon.getOrigine());
                double x1 = (dep.getLongitude() - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
                double y1 = (dep.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat());
                Noeud arr = carte.getMapNoeuds().get(troncon.getDestination());
                double x2 = (arr.getLongitude() - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
                double y2 = (arr.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat());

                gc.setStroke(Color.web("#F4E6DF")); //carte
                gc.setLineWidth(2);
                gc.strokeLine(x1, y1, x2, y2);
            }

        }
    }

    public void afficherPoints() {
//        ArrayList<PointLivraison> pointsItineraire = controleurMetier.getPointsItineraire();
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

        Noeud noeud = carte.obtenirNoeud(demandeDeLivraions.getEntrepot().getAdresss());
        double x = (noeud.getLongitude() - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
        double y = (noeud.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat());

        gc.setFill(Color.web("#D65C4F"));
        int rayon = 10;
        gc.fillRoundRect(x - rayon, y - rayon, rayon * 2, rayon * 2, (double) rayon /4.0, (double) rayon /4.0);
    }


    public void afficherTextePoints() {
        ArrayList<PointLivraison> pointsItineraire = controleurMetier.getPointsItineraire();
        DemandeDeLivraions demandeDeLivraions = controleurMetier.getDemandeDeLivraions();
        StringBuilder sb = new StringBuilder();

        for (PointLivraison point : pointsItineraire) {
            Livraison livraison = demandeDeLivraions.getLivraison(point.getNoeud().getId());
            boolean estEntrepot = demandeDeLivraions.estEntrepot(point.getNoeud().getId());

            if (estEntrepot) {
                sb.append("Entrepôt : \n");
                sb.append("  Adresse: ").append(point.getNoeud().getId()).append("\n").append("\n");
            } else {
                boolean estEnlevement = livraison.getAdresseEnlevement().equals(point.getNoeud().getId());
                int index = demandeDeLivraions.getListeLivraisons().indexOf(livraison) + 1;
                Color couleur = Couleur.getCouleur(index);
                int r = (int) Math.round(couleur.getRed() * 255);
                int g = (int) Math.round(couleur.getGreen() * 255);
                int b = (int) Math.round(couleur.getBlue() * 255);
                sb.append("Livraison : ").append(String.format("#%02X%02X%02X", r, g, b)).append("\n");
                sb.append("  Type: ").append(estEnlevement ? "Enlèvement" : "Livraison").append("\n");
                sb.append("  Adresse: ").append(point.getNoeud().getId()).append("\n").append("\n");
            }

        }
        textePoints.setText(sb.toString());
    }



    public void afficherItineraire() {
        for (Troncon troncon : controleurMetier.getTronconsItineraire()) {
            Noeud dep = carte.getMapNoeuds().get(troncon.getOrigine());
            double x1 = (dep.getLongitude() - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
            double y1 = (dep.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat());
            Noeud arr = carte.getMapNoeuds().get(troncon.getDestination());
            double x2 = (arr.getLongitude() - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
            double y2 = (arr.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat());

            gc.setStroke(Color.RED);
            gc.setLineWidth(4);
            gc.strokeLine(x1, y1, x2, y2);
        }
    }
    @FXML
    public void genererPDF() throws IOException {
        PdfControleur pdfControleur = new PdfControleur();
        pdfControleur.extractPdf(controleurMetier.getTronconsItineraire(), controleurMetier.getPointsItineraire());
    }
}
