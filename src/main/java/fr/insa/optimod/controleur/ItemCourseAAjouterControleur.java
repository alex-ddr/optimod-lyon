package fr.insa.optimod.controleur;

import fr.insa.optimod.modele.Couleur;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

public class ItemCourseAAjouterControleur {
    @FXML private Text adresseLivraison;
    @FXML private Text adressePickup;
    @FXML private SVGPath iconLivraison;
    @FXML private SVGPath iconPickup;
    @FXML private HBox rootBox;
    @FXML private TextField titreField;
    @FXML private StackPane validerBouton;
    @FXML private StackPane couleurCarre;
    @FXML private SVGPath validerBouton1;
    @FXML private SVGPath validerBouton2;

    private String couleur;

    private boolean pickupDefini = false;
    private boolean livraisonDefinie = false;

    private PointsControleur pointsControleur;

    public void setPointsControleur(PointsControleur pointsControleur) {
        this.pointsControleur = pointsControleur;
    }

    @FXML
    public void initialize(int id) {
        couleur = Couleur.getHexaCouleur(id);
        adressePickup.setText("Choisir pickup");
        adresseLivraison.setText("Choisir livraison");

        update();
    }

    public void update() {
        updateStyle();
        updateBoutonValider();
    }

    public void updateStyle() {
        if (pickupDefini) {
            adressePickup.setStyle("-fx-text-fill: " + couleur + ";");
            iconPickup.setStyle("-fx-fill: " + couleur + ";");
            iconPickup.setContent("M5.63271 16.4754C6.11246 16.8712 6.61279 17.233 7.125 17.5861C7.63831 17.2377 8.13622 16.8671 8.61729 16.4754C9.41925 15.8169 10.174 15.1029 10.8759 14.3387C12.4941 12.5693 14.25 10.0043 14.25 7.125C14.25 6.18933 14.0657 5.26283 13.7076 4.39838C13.3496 3.53394 12.8248 2.74848 12.1631 2.08686C11.5015 1.42525 10.7161 0.900423 9.85162 0.542358C8.98717 0.184294 8.06067 0 7.125 0C6.18933 0 5.26283 0.184294 4.39838 0.542358C3.53394 0.900423 2.74848 1.42525 2.08686 2.08686C1.42525 2.74848 0.900423 3.53394 0.542358 4.39838C0.184293 5.26283 -1.39425e-08 6.18933 0 7.125C0 10.0043 1.75592 12.5685 3.37408 14.3387C4.07599 15.1032 4.83072 15.8166 5.63271 16.4754ZM7.125 9.69792C6.44262 9.69792 5.78819 9.42684 5.30567 8.94433C4.82316 8.46181 4.55208 7.80738 4.55208 7.125C4.55208 6.44262 4.82316 5.78819 5.30567 5.30567C5.78819 4.82316 6.44262 4.55208 7.125 4.55208C7.80738 4.55208 8.46181 4.82316 8.94433 5.30567C9.42684 5.78819 9.69792 6.44262 9.69792 7.125C9.69792 7.80738 9.42684 8.46181 8.94433 8.94433C8.46181 9.42684 7.80738 9.69792 7.125 9.69792Z");
        }
        else {
            adressePickup.setStyle("-fx-text-fill:  #666666;");
            iconPickup.setStyle("-fx-fill:  #666666;");
            iconPickup.setContent("M12 18L6 15.9L1.35 17.7C1.01667 17.8333 0.708333 17.796 0.425 17.588C0.141667 17.38 0 17.1007 0 16.75V2.75C0 2.53333 0.0626666 2.34167 0.188 2.175C0.313333 2.00833 0.484 1.88333 0.7 1.8L6 0L12 2.1L16.65 0.3C16.9833 0.166667 17.2917 0.204333 17.575 0.413C17.8583 0.621667 18 0.900667 18 1.25V15.25C18 15.4667 17.9377 15.6583 17.813 15.825C17.6883 15.9917 17.5173 16.1167 17.3 16.2L12 18ZM11 15.55V3.85L7 2.45V14.15L11 15.55Z");
        }

        if (livraisonDefinie) {
            adresseLivraison.setStyle("-fx-text-fill: " + couleur + ";");
            iconLivraison.setStyle("-fx-fill: " + couleur + ";");
            iconLivraison.setContent("M5.63271 16.4754C6.11246 16.8712 6.61279 17.233 7.125 17.5861C7.63831 17.2377 8.13622 16.8671 8.61729 16.4754C9.41925 15.8169 10.174 15.1029 10.8759 14.3387C12.4941 12.5693 14.25 10.0043 14.25 7.125C14.25 6.18933 14.0657 5.26283 13.7076 4.39838C13.3496 3.53394 12.8248 2.74848 12.1631 2.08686C11.5015 1.42525 10.7161 0.900423 9.85162 0.542358C8.98717 0.184294 8.06067 0 7.125 0C6.18933 0 5.26283 0.184294 4.39838 0.542358C3.53394 0.900423 2.74848 1.42525 2.08686 2.08686C1.42525 2.74848 0.900423 3.53394 0.542358 4.39838C0.184293 5.26283 -1.39425e-08 6.18933 0 7.125C0 10.0043 1.75592 12.5685 3.37408 14.3387C4.07599 15.1032 4.83072 15.8166 5.63271 16.4754ZM7.125 9.69792C6.44262 9.69792 5.78819 9.42684 5.30567 8.94433C4.82316 8.46181 4.55208 7.80738 4.55208 7.125C4.55208 6.44262 4.82316 5.78819 5.30567 5.30567C5.78819 4.82316 6.44262 4.55208 7.125 4.55208C7.80738 4.55208 8.46181 4.82316 8.94433 5.30567C9.42684 5.78819 9.69792 6.44262 9.69792 7.125C9.69792 7.80738 9.42684 8.46181 8.94433 8.94433C8.46181 9.42684 7.80738 9.69792 7.125 9.69792Z");
        }
        else {
            adresseLivraison.setStyle("-fx-text-fill:  #666666;");
            iconLivraison.setStyle("-fx-fill:  #666666;");
            iconLivraison.setContent("M12 18L6 15.9L1.35 17.7C1.01667 17.8333 0.708333 17.796 0.425 17.588C0.141667 17.38 0 17.1007 0 16.75V2.75C0 2.53333 0.0626666 2.34167 0.188 2.175C0.313333 2.00833 0.484 1.88333 0.7 1.8L6 0L12 2.1L16.65 0.3C16.9833 0.166667 17.2917 0.204333 17.575 0.413C17.8583 0.621667 18 0.900667 18 1.25V15.25C18 15.4667 17.9377 15.6583 17.813 15.825C17.6883 15.9917 17.5173 16.1167 17.3 16.2L12 18ZM11 15.55V3.85L7 2.45V14.15L11 15.55Z");
        }

        rootBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: " + couleur + "; -fx-border-radius: 10; -fx-border-style: dashed; -fx-border-width: 2;");
        couleurCarre.setStyle("-fx-background-color: " + couleur + "; -fx-background-radius: 8;");

        if (pickupDefini && livraisonDefinie) {
            validerBouton1.setStyle("-fx-fill: " + couleur + "; -fx-opacity: 0.25;");
            validerBouton2.setStyle("-fx-fill: " + couleur + ";");
        }
        else {
            validerBouton1.setStyle("-fx-fill: #b9b9b9; -fx-opacity: 0.25;");
            validerBouton2.setStyle("-fx-fill: #b9b9b9;");
        }
    }

    public void setAdressePickup(Long adresse) {
        adressePickup.setText(adresse.toString());
        pickupDefini = true;
        update();
    }

    public void setAdresseLivraison(Long adresse) {
        adresseLivraison.setText(adresse.toString());
        livraisonDefinie = true;
        update();
    }

    private void updateBoutonValider() {
        if (pickupDefini && livraisonDefinie) {
            // Activer le bouton
            validerBouton.setDisable(false);
            validerBouton.setOpacity(1.0);
            validerBouton.setStyle("-fx-cursor: hand;");
        } else {
            // Désactiver le bouton
            validerBouton.setDisable(true);
            validerBouton.setOpacity(0.5); // Grisé
            validerBouton.setStyle("-fx-cursor: default;");
        }
    }

    public String getTitre() {
        return titreField.getText();
    }

    @FXML
    void annulerAjout(MouseEvent event) {
        if (pointsControleur != null) {
            pointsControleur.annulerAjout();
        }
    }

    @FXML
    void choisirLivraison(MouseEvent event) {
        pointsControleur.setModeSelectionLivraison();

        adresseLivraison.setText("Choisir sur la carte");
        livraisonDefinie = false;
        update();
    }

    @FXML
    void choisirPickup(MouseEvent event) {
        if (pointsControleur != null) pointsControleur.setModeSelectionPickup();

        adressePickup.setText("Choisir sur la carte");
        pickupDefini = false;
        update();
    }

    @FXML
    void validerAjout(MouseEvent event) {
        if (pointsControleur != null && !validerBouton.isDisable()) {
            pointsControleur.validerAjoutFinal();
        }
    }
}
