package fr.insa.optimod.controleur;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class ItemAjouterCourseControleur {

    @FXML
    private HBox rootBox;

    private PointsControleur pointsControleur;

    public void setPointsControleur(PointsControleur pointsControleur) {
        this.pointsControleur = pointsControleur;
    }

    @FXML
    void ajouterCourse(MouseEvent event) {
        if (pointsControleur != null) {
            pointsControleur.clicBoutonCourse();
        }
    }

}
