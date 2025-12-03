package fr.insa.optimod.controleur;

import fr.insa.optimod.modele.Carte;
import fr.insa.optimod.vue.Interface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class AccueilControleur {

    private Interface interfaceUtilisateur;
    private Controleur controleurMetier;

    @FXML
    private SVGPath svgValid;
    @FXML
    private SVGPath svgWrong;
    @FXML
    private Button buttonTourne;
    private File fichierPlan;

    private FileChooser explorateur = new FileChooser();

    public void setInterface(Interface interfaceUtilisateur) {
        this.interfaceUtilisateur = interfaceUtilisateur;
    }

    public void setControleurMetier(Controleur controleurMetier) {
        this.controleurMetier = controleurMetier;
    }

    @FXML
    private void initialize() {
        svgValid.setVisible(false);
        svgWrong.setVisible(false);
        buttonTourne.setDisable(true);
        System.out.println("initialize AccueilControleur");
    }

    @FXML
    private void clicZoneCarte(MouseEvent event) {
        System.out.println("Le fichier plan va être choisi");
        fichierPlan = explorateur.showOpenDialog(null);
        traiterFichierPlan();
    }

    @FXML
    void fichierAuDessusCarte(DragEvent event)
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
    void ficherLacheCarte(DragEvent event)
    {
        Dragboard db = event.getDragboard();
        if (db.hasFiles())
        {
            fichierPlan = db.getFiles().getFirst();
            traiterFichierPlan();
        }
        event.consume();

    }

    @FXML
    private void afficherCarte(){
        System.out.println("Le fichier plan " + fichierPlan.getAbsolutePath());
        long startTime = System.nanoTime();
        controleurMetier.initialiserCarte(fichierPlan.getAbsolutePath());
        long endTime = System.nanoTime();
        long executionTime = (endTime - startTime) / 1000000;

        System.out.println("Loading map takes "+ executionTime + "ms");
//        Carte carte = controleurMetier.getCarte();
//        System.out.print(carte.getListeNoeuds());
//        System.out.println(carte.getListeTroncon());

        try {
            interfaceUtilisateur.afficherCarte();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void traiterFichierPlan() {
        if(isValidXML(fichierPlan)) {
            svgWrong.setVisible(false);
            svgValid.setVisible(true);
            buttonTourne.setDisable(false);
        }
        else {
            svgValid.setVisible(false);
            svgWrong.setVisible(true);
            buttonTourne.setDisable(true);
        }
    }

    //Une partie par IA
    public static boolean isValidXML(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file); // Si parse OK = XML valide
            NodeList nodeNoeud = document.getElementsByTagName("noeud");
            NodeList nodeReseau = document.getElementsByTagName("reseau");
            if(nodeNoeud.getLength() == 0) {return false;}
            if(nodeReseau.getLength() == 0) {return false;}
            return true;
        } catch (Exception e) {
            return false; // Parse error = pas un XML valide
        }
    }

}
