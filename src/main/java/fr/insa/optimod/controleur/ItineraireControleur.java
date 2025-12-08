package fr.insa.optimod.controleur;

import fr.insa.optimod.modele.*;
import fr.insa.optimod.vue.Interface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class ItineraireControleur {

    Carte carte = null;

    private Interface interfaceUtilisateur;
    private Controleur controleurMetier;

    @FXML
    private Canvas canvasCarte;

    @FXML
    private GridPane itemsGrid;

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

    public void initData() {
        List<ItemItineraire> itemItineraireListList = new ArrayList<>(data());

        int column = 0;
        int row = 1;

        try {
            for (ItemItineraire itemItineraire : itemItineraireListList) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/layouts/itemItineraire.fxml"));

                HBox itemHBox = fxmlLoader.load();

                ItemItineraireControleur itemItineraireControleur = fxmlLoader.getController();
                itemItineraireControleur.setData(itemItineraire);
                itemItineraireControleur.setItineraireControleur(this);

                if (column == 1) {
                    column = 0;
                    ++row;
                }

                itemsGrid.add(itemHBox, column++, row);
                GridPane.setMargin(itemHBox, new javafx.geometry.Insets(8));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<ItemItineraire> data() {
        List<ItemItineraire> itemItineraireList = new ArrayList<>();

        ArrayList<PointLivraison> pointsItineraire = controleurMetier.getPointsItineraire();
        DemandeDeLivraions demandeDeLivraions = controleurMetier.getDemandeDeLivraions();

        int itemId = 0;
        for (PointLivraison point : pointsItineraire) {
            Livraison livraison = demandeDeLivraions.getLivraison(point.getNoeud().getId());
            boolean estEntrepot = demandeDeLivraions.estEntrepot(point.getNoeud().getId());
            if (!estEntrepot) {
                boolean estEnlevement = livraison.getAdresseEnlevement().equals(point.getNoeud().getId());
                int index = demandeDeLivraions.getListeLivraisons().indexOf(livraison) + 1;
                ItemItineraire item = new ItemItineraire(itemId, estEnlevement, (estEnlevement ? "Pickup #" : "Livraison #") + index, point.getNoeud().getId().toString(), point.getG().toString(), index);
                itemItineraireList.add(item);
            }
            itemId++;
        }

        return itemItineraireList;
    }

    @FXML
    private void retourAccueil() {
        try {
            controleurMetier.reinitialiserTournee();
            controleurMetier.reinitialiserDemandeDeLivraison();
            controleurMetier.reinitialiserCarte();
            interfaceUtilisateur.afficherAccueil();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void retourPoints() {
        try {
            controleurMetier.reinitialiserTournee();
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
//        ArrayList<PointLivraison> pointsItineraire = controleurMetier.getPointsItineraire();
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
            double y1 = gc.getCanvas().getHeight() - ((dep.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat()));
            Noeud arr = carte.getMapNoeuds().get(troncon.getDestination());
            double x2 = (arr.getLongitude() - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
            double y2 = gc.getCanvas().getHeight() - ((arr.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat()));

            gc.setStroke(Color.web("#D65C4F"));
            gc.setLineWidth(4);
            gc.strokeLine(x1, y1, x2, y2);
        }
    }
    @FXML
    public void genererPDF() throws IOException {
        String canvasBase64 = exporterCanvasEnBase64();
        PdfControleur pdfControleur = new PdfControleur();
        pdfControleur.setInterface(this.interfaceUtilisateur);
        pdfControleur.extrairePdf_2(controleurMetier.getTronconsItineraire(), controleurMetier.getChemin(), canvasBase64);
    }

    public void descendrePoint(int itemItineraireId) {
        ItemItineraire itemToMove = null;
        List<ItemItineraire> itemItineraireList = data();
        for (ItemItineraire item : itemItineraireList) {
            if (item.getId() == itemItineraireId) {
                itemToMove = item;
                break;
            }
        }
        ItemItineraire itemNext = null;
        if (itemToMove != null) {
            int index = itemItineraireList.indexOf(itemToMove);
            if (index < itemItineraireList.size() - 1) {
                itemNext = itemItineraireList.get(index + 1);
                if (itemNext != null) {
                    if (itemToMove.getIndex() != itemNext.getIndex()) { // on peut pas mettre le pickup après la livraison
                        controleurMetier.echangerPointsItineraire(itemToMove.getId(), itemNext.getId());
                        itemsGrid.getChildren().clear();
                        initData();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Action impossible");
                        alert.initOwner(this.interfaceUtilisateur.getFenetrePrincipale());
                        alert.setHeaderText("Impossible de descendre ce point");
                        alert.setContentText("Vous ne pouvez pas descendre un point de pickup après son point de livraison.");
                        alert.showAndWait();
                    }
                }
            }
        }
    }

    public void monterPoint(int itemItineraireId) {
        ItemItineraire itemToMove = null;
        List<ItemItineraire> itemItineraireList = data();
        for (ItemItineraire item : itemItineraireList) {
            if (item.getId() == itemItineraireId) {
                itemToMove = item;
                break;
            }
        }
        ItemItineraire itemPrev = null;
        if (itemToMove != null) {
            int index = itemItineraireList.indexOf(itemToMove);
            if (index > 0) {
                itemPrev = itemItineraireList.get(index - 1);
                if (itemPrev != null){
                    if( itemToMove.getIndex() != itemPrev.getIndex()) { // on peut pas mettre la livraison avant le pickup
                    controleurMetier.echangerPointsItineraire(itemToMove.getId(), itemPrev.getId());
                    itemsGrid.getChildren().clear();
                    initData();
                    afficherCarte();
                    afficherItineraire();
                    afficherPoints();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Action impossible");
                        alert.initOwner(this.interfaceUtilisateur.getFenetrePrincipale());
                        alert.setHeaderText("Impossible de monter ce point");
                        alert.setContentText("Vous ne pouvez pas monter un point de livraison avant son point de pickup.");
                        alert.showAndWait();
                    }
                }
            }
        }
    }

    public String exporterCanvasEnBase64() throws IOException {
        // Capturer le canvas en tant qu'image
        WritableImage writableImage = new WritableImage(
            (int) canvasCarte.getWidth(),
            (int) canvasCarte.getHeight()
        );
        canvasCarte.snapshot(null, writableImage);

        // Convertir en BufferedImage
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);

        // Encoder en base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
