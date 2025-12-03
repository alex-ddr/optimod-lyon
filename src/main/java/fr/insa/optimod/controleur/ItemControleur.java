package fr.insa.optimod.controleur;

import fr.insa.optimod.modele.Item;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

public class ItemControleur {

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

    @FXML
    void deleteItem(MouseEvent event) {

    }

    public void setDate(Item item) {
        svgLettre.setContent(item.getSvgSrc());
        coloredTag.setStyle("-fx-background-color: " + item.getColoredTag() + ";");
    }

}
