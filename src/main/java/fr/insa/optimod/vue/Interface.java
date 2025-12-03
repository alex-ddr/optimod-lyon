package fr.insa.optimod.vue;

import fr.insa.optimod.controleur.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Interface extends Application {

    private Stage fenetrePrincipale;
    private Controleur controleurMetier;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.fenetrePrincipale = primaryStage;
        this.controleurMetier = new Controleur();

        fenetrePrincipale.setTitle("Optimod'Lyon");
        fenetrePrincipale.setMaximized(true);

        afficherAccueil();

        fenetrePrincipale.show();
    }

    public void afficherAccueil() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/accueil.fxml"));

        Parent accueil = loader.load();

        AccueilControleur accueilControleur = loader.getController();

        accueilControleur.setInterface(this);
        accueilControleur.setControleurMetier(this.controleurMetier);

        Scene scene = new Scene(accueil, 1920, 1080);

        fenetrePrincipale.setScene(scene);
    }

    public void afficherCarte() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/carte.fxml"));

        Parent carte = loader.load();

        CarteControleur carteControleur = loader.getController();

        carteControleur.setInterface(this);
        carteControleur.setControleurMetier(this.controleurMetier);

        carteControleur.afficherCarte();

        Scene scene = new Scene(carte, 1920, 1080);

        fenetrePrincipale.setScene(scene);
    }

    public void afficherPoints() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/points.fxml"));

        Parent points = loader.load();

        PointsControleur pointsController = loader.getController();

        pointsController.setInterface(this);
        pointsController.setControleurMetier(this.controleurMetier);

        pointsController.afficherCarte();
        pointsController.afficherPoints();
        pointsController.afficher_points_textuels();

        Scene scene = new Scene(points, 1920, 1080);

        fenetrePrincipale.setScene(scene);
    }

    public void afficherItineraire() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/itineraire.fxml"));

        Parent points = loader.load();

        ItineraireControleur itineraireControleur = loader.getController();

        itineraireControleur.setInterface(this);
        itineraireControleur.setControleurMetier(this.controleurMetier);

        itineraireControleur.afficherCarte();
        itineraireControleur.afficherPoints();
        itineraireControleur.afficherItineraire();
        itineraireControleur.afficherTextePoints();

        Scene scene = new Scene(points, 1920, 1080);

        fenetrePrincipale.setScene(scene);
    }
}
