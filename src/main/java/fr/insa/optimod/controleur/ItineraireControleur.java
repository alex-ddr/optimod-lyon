package fr.insa.optimod.controleur;

import fr.insa.optimod.modele.*;
import fr.insa.optimod.vue.Interface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.text.Text;

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
    private CheckBox checkBox;

    @FXML
    private Button boutonOptimal;
    private boolean itineraireOptimal = true;

    @FXML
    private Button boutonActualiser;
    private boolean itineraireActualise = true;

    @FXML
    private Button boutonPdf;

    @FXML
    private Text heureTotale;
    @FXML
    private Text minuteTotale;

    private GraphicsContext gc;

    private List<ItemItineraire> itemItineraireList;


    public void setInterface(Interface interfaceUtilisateur) {
        this.interfaceUtilisateur = interfaceUtilisateur;
    }

    public void setControleurMetier(Controleur controleurMetier) {
        this.controleurMetier = controleurMetier;
    }

    @FXML
    private void initialize() {
        System.out.println("initialize ItineraireControleur");
        gc = canvasCarte.getGraphicsContext2D();
        refreshStyles();
    }

    @FXML
    public void initData() {
        this.itemItineraireList = data();
        itemsGrid.getChildren().clear();
        updateGrid();

        afficherCarte();

        if (checkBox.isSelected()) {
            controleurMetier.calculerTournee();
            afficherItineraire();
            itineraireActualise = true;
            miseAJourHeureTotale();
        }
        else if (itineraireActualise) {
            controleurMetier.calculerTournee();
            afficherItineraire();
            miseAJourHeureTotale();
        }

        afficherPoints();
        refreshStyles();
    }

    public void refreshStyles() {
        if (checkBox.isSelected() || itineraireActualise) {
            boutonActualiser.setOpacity(0.25);
            boutonActualiser.setDisable(true);
        }
        else {
            boutonActualiser.setOpacity(1);
            boutonActualiser.setDisable(false);
        }

        if (itineraireOptimal) {
            boutonOptimal.setOpacity(0.25);
            boutonOptimal.setDisable(true);
        }
        else {
            boutonOptimal.setOpacity(1);
            boutonOptimal.setDisable(false);
        }

        if (itineraireActualise) {
            boutonPdf.setOpacity(1);
            boutonPdf.setDisable(false);
        }
        else {
            boutonPdf.setOpacity(0.25);
            boutonPdf.setDisable(true);
        }
    }

    private List<ItemItineraire> data() {
        List<ItemItineraire> newItemItineraireList = new ArrayList<>();

        ArrayList<PointLivraison> pointsItineraire = controleurMetier.getPointsItineraire();
        DemandeDeLivraions demandeDeLivraions = controleurMetier.getDemandeDeLivraions();

        int uniqueIdForItem = 0;

        if (pointsItineraire != null) {
            for (PointLivraison point : pointsItineraire) {
                Livraison livraison = demandeDeLivraions.getLivraison(point.getNoeud().getId());
                boolean estEntrepot = demandeDeLivraions.estEntrepot(point.getNoeud().getId());

                if (!estEntrepot && livraison != null) {
                    boolean estEnlevement = livraison.getAdresseEnlevement().equals(point.getNoeud().getId());
                    int indexCouleur = livraison.getId();

                    String titre = livraison.getTitre() != null ? livraison.getTitre() : (estEnlevement ? "Pickup #" : "Livraison #") + indexCouleur;

                    String adresse = (estEnlevement ? controleurMetier.getRueNoeud(livraison.getAdresseEnlevement()) : controleurMetier.getRueNoeud(livraison.getAdresseLivraison()));

                    ItemItineraire item = new ItemItineraire(
                            uniqueIdForItem,
                            estEnlevement,
                            titre,
                            adresse,
                            point.getG().toString(),
                            indexCouleur
                    );
                    newItemItineraireList.add(item);
                    uniqueIdForItem++;
                }
            }
        }

        for (int i = 0; i < newItemItineraireList.size(); i++) {
            ItemItineraire current = newItemItineraireList.get(i);

            if (i == 0) {
                current.setPeutMonter(false);
            }
            if (i == newItemItineraireList.size() - 1) {
                current.setPeutDescendre(false);
            }

            if (current.getEstPickup() && i < newItemItineraireList.size() - 1) {
                ItemItineraire next = newItemItineraireList.get(i + 1);
                if (!next.getEstPickup() && next.getIndex() == current.getIndex()) {
                    current.setPeutDescendre(false);
                    next.setPeutMonter(false);
                }
            }
        }

        return newItemItineraireList;
    }

    @FXML
    private void calculerOptimal() {
        controleurMetier.preparerPlanTournee(controleurMetier.getCarte(), controleurMetier.getDemandeDeLivraions());
        initData();
        itineraireOptimal = true;
        refreshStyles();
    }

    @FXML
    private void actualiserItineraire() {
        itineraireActualise = true;
        initData();
    }

    private void updateGrid() {
        itemsGrid.getChildren().clear();

        int column = 0;
        int row = 1;

        try {
            for (ItemItineraire itemItineraire : itemItineraireList) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layouts/itemItineraire.fxml"));

                HBox itemHBox = fxmlLoader.load();

                ItemItineraireControleur itemItineraireControleur = fxmlLoader.getController();
                itemItineraireControleur.setHeureDepart(controleurMetier.getHeureDepart());
                itemItineraireControleur.setItineraireControleur(this);
                itemItineraireControleur.setData(itemItineraire);

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

    @FXML
    public void checkboxClic() {
        if (checkBox.isSelected()) {
            controleurMetier.calculerTournee();
            afficherItineraire();
            afficherPoints();
            itineraireActualise = true;
        }
        refreshStyles();
    }

    public void descendrePoint(int itemItineraireId) {
        itineraireOptimal = false;
        itineraireActualise = false;

        int index = -1;
        for (int i = 0; i < itemItineraireList.size(); i++) {
            if (itemItineraireList.get(i).getId() == itemItineraireId) {
                index = i;
                break;
            }
        }

        if (index == -1 || index >= itemItineraireList.size() - 1) return;

        ItemItineraire itemToMove = itemItineraireList.get(index);
        ItemItineraire itemNext = itemItineraireList.get(index + 1);

        if (itemToMove.getIndex() == itemNext.getIndex()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Action impossible");
            alert.setHeaderText("Impossible de descendre ce point");
            alert.setContentText("Vous ne pouvez pas placer le pickup après sa livraison.");
            alert.showAndWait();
            return;
        }

        controleurMetier.echangerPointsItineraire(index + 1, index + 2);

        initData();
    }

    public void monterPoint(int itemItineraireId) {
        itineraireOptimal = false;
        itineraireActualise = false;

        int index = -1;
        for (int i = 0; i < itemItineraireList.size(); i++) {
            if (itemItineraireList.get(i).getId() == itemItineraireId) {
                index = i;
                break;
            }
        }

        if (index <= 0) return;

        ItemItineraire itemToMove = itemItineraireList.get(index);
        ItemItineraire itemPrev = itemItineraireList.get(index - 1);

        if (itemToMove.getIndex() == itemPrev.getIndex()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Action impossible");
            alert.setHeaderText("Impossible de monter ce point");
            alert.setContentText("Vous ne pouvez pas placer la livraison avant son pickup.");
            alert.showAndWait();
            return;
        }

        controleurMetier.echangerPointsItineraire(index + 1, index);

        initData();
    }

    // --- Reste des méthodes d'affichage (Carte, PDF...) inchangées ---

    public void afficherCarte() {
        carte = controleurMetier.getCarte();
        if (carte != null) {
            gc.clearRect(0, 0, canvasCarte.getWidth(), canvasCarte.getHeight());
            gc.setFill(Color.web("#F4E6DF"));
            gc.fillRect(0, 0, canvasCarte.getWidth(), canvasCarte.getHeight());
            for (Troncon troncon : carte.getListeTroncon()) {
                drawTroncon(troncon, Color.web("#6B3F3A"), 2);
            }
        }
    }

    public void afficherPoints() {
        DemandeDeLivraions demandeDeLivraions = controleurMetier.getDemandeDeLivraions();
        int index = 1;
        for (Livraison livraison : demandeDeLivraions.getListeLivraisons()) {
            Color couleur = Couleur.getCouleur(livraison.getId());
            drawPoint(livraison.getAdresseEnlevement(), couleur, 10);
            drawPoint(livraison.getAdresseLivraison(), couleur, 10);
            index++;
        }
        drawEntrepot(demandeDeLivraions.getEntrepot().getAdresss());
    }

    public void afficherItineraire() {

        List<Troncon> troncons = controleurMetier.getTronconsItineraire();
        if (troncons != null) {
            for (Troncon troncon : troncons) {
                drawTroncon(troncon, Color.web("#D65C4F"), 4);
            }
        }
    }

    private void drawTroncon(Troncon troncon, Color color, int width) {
        Noeud dep = carte.getMapNoeuds().get(troncon.getOrigine());
        double x1 = longToX(dep.getLongitude());
        double y1 = latToY(dep.getLatitude());
        Noeud arr = carte.getMapNoeuds().get(troncon.getDestination());
        double x2 = longToX(arr.getLongitude());
        double y2 = latToY(arr.getLatitude());
        gc.setStroke(color);
        gc.setLineWidth(width);
        gc.strokeLine(x1, y1, x2, y2);
    }

    private void drawPoint(Long noeudId, Color color, int rayon) {
        Noeud noeud = carte.obtenirNoeud(noeudId);
        double x = longToX(noeud.getLongitude());
        double y = latToY(noeud.getLatitude());
        gc.setFill(color);
        gc.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
    }

    private void drawEntrepot(Long noeudId) {
        Noeud noeud = carte.obtenirNoeud(noeudId);
        double x = longToX(noeud.getLongitude());
        double y = latToY(noeud.getLatitude());
        int rayon = 20;
        double svgW = 576.0; double svgH = 512.0; double scale = 2.0 * rayon / svgW;
        double tx = x - (svgW * scale) / 2.0; double ty = y - (svgH * scale) / 2.0;
        gc.save(); gc.translate(tx, ty); gc.scale(scale, scale);
        gc.setFill(Color.RED); gc.beginPath();
        gc.appendSVGPath("M575.8 255.5c0 18-15 32.1-32 32.1h-32l.7 160.2c0 2.7-.2 5.4-.5 8.1v16.2c0 22.1-17.9 40-40 40h-16c-1.1 0-2.2 0-3.3-.1c-1.4.1-2.8.1-4.2.1L416 512h-24c-22.1 0-40-17.9-40-40v-88c0-17.7-14.3-32-32-32h-64c-17.7 0-32 14.3-32 32v88c0 22.1-17.9 40-40 40h-55.9c-1.5 0-3-.1-4.5-.2c-1.2.1-2.4.2-3.6.2h-16c-22.1 0-40-17.9-40-40V360c0-.9 0-1.9.1-2.8v-69.7h-32c-18 0-32-14-32-32.1c0-9 3-17 10-24L266.4 8c7-7 15-8 22-8s15 2 21 7l255.4 224.5c8 7 12 15 11 24");
        gc.fill(); gc.closePath(); gc.restore();
    }

    private double longToX(double longitude) {
        return (longitude - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
    }

    private double latToY(double latitude) {
        return gc.getCanvas().getHeight() - ((latitude - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat()));
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
    private void actualisationAuto() { System.out.println("actualisationAuto"); }

    @FXML
    public void genererPDF() throws IOException {
        String canvasBase64 = exporterCanvasEnBase64();
        PdfControleur pdfControleur = new PdfControleur();
        pdfControleur.setInterface(this.interfaceUtilisateur);
        pdfControleur.extrairePdf_2(controleurMetier.getTronconsItineraire(), controleurMetier.getChemin(), canvasBase64);
    }

    public String exporterCanvasEnBase64() throws IOException {
        WritableImage writableImage = new WritableImage((int) canvasCarte.getWidth(), (int) canvasCarte.getHeight());
        canvasCarte.snapshot(null, writableImage);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    private void miseAJourHeureTotale() {
        ArrayList<PointLivraison> points = controleurMetier.getPointsItineraire();

        if (points != null && !points.isEmpty()) {
            PointLivraison dernierPoint = points.get(points.size() - 1);

            double secondesTotales = dernierPoint.getG();

            int heures = (int) (secondesTotales / 3600);
            int minutes = (int) ((secondesTotales % 3600) / 60);

            heureTotale.setText(String.valueOf(heures));
            minuteTotale.setText(String.format("%02d", minutes));
        } else {
            heureTotale.setText("0");
            minuteTotale.setText("00");
        }
    }
}