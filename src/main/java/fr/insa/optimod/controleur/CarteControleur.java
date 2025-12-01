package fr.insa.optimod.controleur;

import fr.insa.optimod.modele.Carte;
import fr.insa.optimod.modele.DemandeDeLivraions;
import fr.insa.optimod.modele.Noeud;
import fr.insa.optimod.modele.Troncon;
import fr.insa.optimod.vue.Interface;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class CarteControleur {

    Carte carte = null;

    private Interface interfaceUtilisateur;
    private Controleur controleurMetier;

    private FileChooser explorateur = new FileChooser();

    @FXML
    private Canvas canvasCarte;

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

                gc.setStroke(Color.BLACK);
                gc.setLineWidth(2);
                gc.strokeLine(x1, y1, x2, y2);
            }
            for (Noeud noeud : carte.getListeNoeuds()) {
                double x = (noeud.getLongitude() - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
                double y = (noeud.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat());

                gc.setFill(Color.RED);
                int rayon = 5;
                gc.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
            }
        }
    }

    @FXML
    private void clicZonePoints(MouseEvent event) {
        System.out.println("Le fichier des points va être choisi");
        File fichierPoints = explorateur.showOpenDialog(null);
        traiterFichierPoints(fichierPoints);
    }

    @FXML
    void fichierAuDessusPoints(DragEvent event)
    {
        Dragboard db = event.getDragboard();
        if (db.hasFiles() && db.getFiles().size() == 1)
        {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        } else {
            event.acceptTransferModes(TransferMode.NONE);
        }
        event.consume();
    }

    @FXML
    void ficherLachePoints(DragEvent event)
    {
        Dragboard db = event.getDragboard();
        if (db.hasFiles())
        {
            traiterFichierPoints(db.getFiles().getFirst());
        }
        event.consume();
    }

    private void traiterFichierPoints(File fichierPoints) {
        System.out.println("Le fichier des points " + fichierPoints.getAbsolutePath());
//        controleurMetier.initialiserPoints(fichierPoints.getAbsolutePath());
        controleurMetier.initialiserDemandeDeLivraions(fichierPoints.getAbsolutePath());
        DemandeDeLivraions demande = controleurMetier.getDemandeDeLivraions();
        controleurMetier.preparerPlanTournee(carte, demande);

        try {
            interfaceUtilisateur.afficherPoints();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
