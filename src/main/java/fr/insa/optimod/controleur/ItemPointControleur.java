package fr.insa.optimod.controleur;

import fr.insa.optimod.modele.Couleur;
import fr.insa.optimod.modele.itemPoint;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

public class ItemPointControleur {

    @FXML
    private SVGPath svgLettre;

    @FXML
    private Text adresse;

    @FXML
    private VBox aireDuTexte;

    @FXML
    private VBox coloredTag;

    @FXML
    private SVGPath poubelle;

    @FXML
    private Text titre;

    protected PointsControleur pointsControleur;

    protected itemPoint itemPoint;

    public void setPointsControleur(PointsControleur pointsControleur) {
        this.pointsControleur = pointsControleur;
    }

    @FXML
    void deleteItem(MouseEvent event) {
        Couleur.supprimerCouleur(itemPoint.getId());
        pointsControleur.supprimerLivraison(Long.parseLong(itemPoint.getAdresse()));
    }

    public void setData(itemPoint itemPoint) {
        this.itemPoint = itemPoint;
        if (itemPoint.getEstPickup()) {
            svgLettre.setContent("M0.000222027 17.4546V4.3869e-05H7.21045C8.51727 4.3869e-05 9.64511 0.255726 10.594 0.76709C11.5485 1.27277 12.2843 1.98016 12.8014 2.88925C13.3184 3.79266 13.5769 4.84379 13.5769 6.04266C13.5769 7.2472 13.3127 8.30118 12.7843 9.20459C12.2616 10.1023 11.5144 10.7983 10.5428 11.2927C9.57124 11.787 8.41784 12.0341 7.08261 12.0341H2.63374V8.71027H6.29852C6.93488 8.71027 7.46613 8.59948 7.89227 8.37789C8.32409 8.15629 8.65079 7.84663 8.87238 7.44891C9.09397 7.0455 9.20477 6.57675 9.20477 6.04266C9.20477 5.50288 9.09397 5.03698 8.87238 4.64493C8.65079 4.2472 8.32409 3.94039 7.89227 3.72448C7.46045 3.50857 6.9292 3.40061 6.29852 3.40061H4.21897V17.4546H0.000222027Z");
        }
        else {
            svgLettre.setContent("M0.000222027 17.4546V4.3869e-05H4.21897V14.0285H11.4803V17.4546H0.000222027Z");
        }

        svgLettre.setStyle("-fx-fill: white;");

        adresse.setText(itemPoint.getAdresse());

        coloredTag.setStyle("-fx-background-color: " + Couleur.getHexaCouleur(itemPoint.getId()) + ";");

        titre.setText(itemPoint.getTitre());
    }

}
