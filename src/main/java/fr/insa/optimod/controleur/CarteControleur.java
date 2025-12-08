package fr.insa.optimod.controleur;

import fr.insa.optimod.modele.Carte;
import fr.insa.optimod.modele.DemandeDeLivraions;
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
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;

public class CarteControleur {

    Carte carte = null;

    private Interface interfaceUtilisateur;
    private Controleur controleurMetier;

    private FileChooser explorateur = new FileChooser();


    @FXML
    private Canvas canvasCarte;

    @FXML
    private SVGPath svgWrong;

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
        svgWrong.setVisible(false);
    }

    @FXML
    private void retourAccueil() {
        try {
            controleurMetier.reinitialiserCarte();
            interfaceUtilisateur.afficherAccueil();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void afficherCarte() {
        carte = controleurMetier.getCarte();
        if (carte != null) {
            System.out.println("Affichage de la carte");
            gc.clearRect(0, 0, canvasCarte.getWidth(), canvasCarte.getHeight());
            gc.setFill(Color.web("#F4E6DF"));
            gc.fillRect(0, 0, canvasCarte.getWidth(), canvasCarte.getHeight());
            for (Troncon troncon : carte.getListeTroncon()) {
                Noeud dep = carte.getMapNoeuds().get(troncon.getOrigine());
                double x1 = (dep.getLongitude() - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
                double y1 = gc.getCanvas().getHeight() - ((dep.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat() - carte.getMinLat()));
                Noeud arr = carte.getMapNoeuds().get(troncon.getDestination());
                double x2 = (arr.getLongitude() - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
                double y2 = gc.getCanvas().getHeight() - ((arr.getLatitude() - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat() - carte.getMinLat()));

                gc.setStroke(Color.web("#6B3F3A")); // ou 6B3F3A  ou 8C5752 et couleur pour tracer la tournée --> #D65C4F
                gc.setLineWidth(2);
                gc.strokeLine(x1, y1, x2, y2);
            }
        }
    }

    @FXML
    private void clicZonePoints(MouseEvent event) {
        System.out.println("Le fichier des points va être choisi");
        File fichierPoints = explorateur.showOpenDialog(this.interfaceUtilisateur.getFenetrePrincipale());
        traiterFichierPoints(fichierPoints);
    }

    @FXML
    void fichierAuDessusPoints(DragEvent event)
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
    void ficherLachePoints(DragEvent event)
    {
        Dragboard db = event.getDragboard();
        if (db.hasFiles())
        {
            traiterFichierPoints(db.getFiles().getFirst());
        }
        event.consume();
    }

    private void traiterFichierPoints(File fichierPoints) {
        if (isValidXML(fichierPoints)) {
            System.out.println("Le fichier des points " + fichierPoints.getAbsolutePath());
            //        controleurMetier.initialiserPoints(fichierPoints.getAbsolutePath());
            controleurMetier.initialiserDemandeDeLivraions(fichierPoints.getAbsolutePath());
            DemandeDeLivraions demande = controleurMetier.getDemandeDeLivraions();
            controleurMetier.preparerPlanTournee(carte, demande);

            try {
                interfaceUtilisateur.afficherPoints();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            svgWrong.setVisible(true);
        }
    }

    private boolean isValidXML(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file); // Si parse OK = XML valide

            if(document.getElementsByTagName("livraison").getLength() == 0 && document.getElementsByTagName("request").getLength() == 0) {return false;}
            if(document.getElementsByTagName("entrepot").getLength() == 0 && document.getElementsByTagName("depot").getLength() == 0) {return false;}
            if(document.getElementsByTagName("demandeDeLivraisons").getLength() == 0 && document.getElementsByTagName("planningRequest").getLength() == 0) {return false;}

            String enlevement = "adresseEnlevement";
            String livraison = "adresseLivraison";
            NodeList nodeList = document.getElementsByTagName("livraison");
            if (nodeList.getLength() == 0){
                nodeList = document.getElementsByTagName("request");
                livraison = "deliveryAddress";
                enlevement = "pickupAddress";
            }
            Carte carte = controleurMetier.getCarte();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Long idEnlevement = Long.valueOf(node.getAttributes().getNamedItem(enlevement).getTextContent());
                Long idLivraison = Long.valueOf(node.getAttributes().getNamedItem(livraison).getTextContent());
                System.out.println(carte.getListeNoeuds().getFirst());
                if (carte.getListeNoeuds().stream().noneMatch(n -> n.getId().equals(idEnlevement) || n.getId().equals(idLivraison))) {
                    System.out.println(idEnlevement + " " + idLivraison);
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false; // Parse error = pas un XML valide
        }
    }

}
