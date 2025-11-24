package fr.insa.optimod.controleur;

import fr.insa.optimod.modele.Carte;
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
import javafx.stage.FileChooser;

import java.io.File;

public class CarteControleur {

    Carte carte = null;

    private Interface interfaceUtilisateur;
    private Controleur controleurMetier;

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

    public void afficherCarte() {
        carte = controleurMetier.getCarte();
        if (carte != null) {
            System.out.println("Affichage de la carte");
            gc.clearRect(0, 0, canvasCarte.getWidth(), canvasCarte.getHeight());
            gc.setFill(Color.GRAY);
            gc.fillRect(0, 0, canvasCarte.getWidth(), canvasCarte.getHeight());
            for (Troncon troncon : carte.getListeTroncon()) {
                Noeud dep = carte.getMapNoeuds().get(troncon.getOrigine());
                double x1 = (dep.getLongitude() - 4.8568360) * gc.getCanvas().getWidth() / 0.022354;
                double y1 = (dep.getLatitude() - 45.74700) * gc.getCanvas().getHeight() / 0.01578;
                Noeud arr = carte.getMapNoeuds().get(troncon.getDestination());
                double x2 = (arr.getLongitude() - 4.8568360) * gc.getCanvas().getWidth() / 0.022354;
                double y2 = (arr.getLatitude() - 45.74700) * gc.getCanvas().getHeight() / 0.01578;

                gc.setStroke(Color.BLACK);
                gc.setLineWidth(2);
                gc.strokeLine(x1, y1, x2, y2);
            }
            for (Noeud noeud : carte.getListeNoeuds()) {
                double x = (noeud.getLongitude() - 4.8568360) * gc.getCanvas().getWidth() / 0.022354;
                double y = (noeud.getLatitude() - 45.74700) * gc.getCanvas().getHeight() / 0.01578;
                // min -> 45.74706
                // max -> 45.762775

                // min -> 45.74700
                // max -> 45.762780

                // min -> 4.8568363
                // max -> 4.879188

                // min -> 4.8568360
                // max -> 4.879190

                gc.setFill(Color.RED);
                int rayon = 5;
                gc.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
            }
        }
    }

}
