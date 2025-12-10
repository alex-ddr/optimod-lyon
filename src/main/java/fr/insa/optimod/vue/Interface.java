package fr.insa.optimod.vue;

import fr.insa.optimod.controleur.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.IOException;

public class Interface extends Application {

    private Stage fenetrePrincipale;
    private Controleur controleurMetier;

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getFenetrePrincipale() {
        return fenetrePrincipale;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.fenetrePrincipale = primaryStage;
        this.controleurMetier = new Controleur();

        //Set icon on the application bar
        var appIcon = new Image(getClass().getResourceAsStream("/img/icon.png"));
        fenetrePrincipale.getIcons().add(appIcon);

        //Set icon on the taskbar/dock
        if (Taskbar.isTaskbarSupported()) {
            var taskbar = Taskbar.getTaskbar();

            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                var dockIcon = defaultToolkit.getImage(getClass().getResource("/img/icon.png"));
                taskbar.setIconImage(dockIcon);
            }

        }

        fenetrePrincipale.setTitle("Optimod'Lyon");
        fenetrePrincipale.setMaximized(true);

        afficherAccueil();
        //afficherPoints();

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
        pointsController.initData();
        pointsController.afficherCarte();
        pointsController.afficherPoints();

        Scene scene = new Scene(points, 1920, 1080);

        fenetrePrincipale.setScene(scene);
    }

    public void afficherItineraire() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/itineraire.fxml"));

        Parent points = loader.load();

        ItineraireControleur itineraireControleur = loader.getController();

        itineraireControleur.setInterface(this);
        itineraireControleur.setControleurMetier(this.controleurMetier);

        itineraireControleur.initData();
        itineraireControleur.afficherCarte();
        itineraireControleur.afficherItineraire();
        itineraireControleur.afficherPoints();


        Scene scene = new Scene(points, 1920, 1080);

        fenetrePrincipale.setScene(scene);
    }
}
