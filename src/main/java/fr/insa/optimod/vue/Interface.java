package fr.insa.optimod.vue;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Interface extends Application {

    Stage fenetrePrincipale;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent accueil = FXMLLoader.load(getClass().getResource("/layouts/accueil.fxml"));

        fenetrePrincipale = primaryStage;
        fenetrePrincipale.setTitle("Optimod Lyon");
        fenetrePrincipale.setMaximized(true);

        Scene scene = new Scene(accueil, 1920, 1080);

        fenetrePrincipale.setScene(scene);

        fenetrePrincipale.show();
    }
}
