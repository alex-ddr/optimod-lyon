package fr.insa.optimod.controleur;

import fr.insa.optimod.modele.Carte;
import fr.insa.optimod.vue.Interface;
import javafx.fxml.FXML;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;

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
        controleurMetier.initialiserCarte(fichierPlan.getAbsolutePath());
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
        }
        else {
            svgValid.setVisible(false);
            svgWrong.setVisible(true);
        }
    }

    //IA
    public static boolean isValidXML(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file); // Si parse OK = XML valide
            return true;
        } catch (Exception e) {
            return false; // Parse error = pas un XML valide
        }
    }

}
