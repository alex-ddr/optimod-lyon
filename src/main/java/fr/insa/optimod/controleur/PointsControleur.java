package fr.insa.optimod.controleur;

import fr.insa.optimod.modele.*;
import fr.insa.optimod.vue.Interface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PointsControleur {

    Carte carte = null;

    private Interface interfaceUtilisateur;
    private Controleur controleurMetier;

    @FXML
    private Canvas canvasCarte;

    @FXML
    private GridPane itemsGrid;

    private GraphicsContext gc;

    @FXML
    private Button boutonAjoutCourse;

    private List<ItemPoint> itemPointList;

    private int ajoutCourse = 0; // 0 = non, 1 = enlevement, 2 = livraison
    private int modifCourse = 0; // 0 = non, 1 = enlevement, 2 = livraison
    private Livraison courseAmodifier = null;
    private Noeud nouvelEnlevement = null;
    private Noeud nouvelleLivraison = null;

    private ArrayList<double[]> listeCoordonnees = new ArrayList<double[]>();

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
    }

    public void initData() {
        this.itemPointList = data();
        itemsGrid.getChildren().clear();
        updateGrid();
    }

    private List<ItemPoint> data() {
        List<ItemPoint> newItemPointList = new ArrayList<>();

        DemandeDeLivraions demandeDeLivraions = controleurMetier.getDemandeDeLivraions();

        for (Livraison l : demandeDeLivraions.getListeLivraisons()) {
            ItemPoint pickup = new ItemPoint(l.getId(), true, "Pickup #" + l.getId(), l.getAdresseEnlevement().toString());
            newItemPointList.add(pickup);


            ItemPoint livraison = new ItemPoint(l.getId(), false, "Livraison #" + l.getId(), l.getAdresseLivraison().toString());
            newItemPointList.add(livraison);
        }

        return newItemPointList;
    }
    private void updateGrid() {
        itemsGrid.getChildren().clear();

        int column = 0;
        int row = 1;

        try {

            loadItemAjouterCourse(column, row);

            column++;
            if (column == 1) {
                column = 0;
                ++row;
            }

            for (ItemPoint itemPoint : itemPointList) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/layouts/ItemPoint.fxml"));

                HBox itemHBox = fxmlLoader.load();

                ItemPointControleur itemPointControleur = fxmlLoader.getController();
                itemPointControleur.setData(itemPoint);
                itemPointControleur.setPointsControleur(this);

                if (column == 1) {
                    column = 0;
                    ++row;
                }
                itemsGrid.add(itemHBox, column++, row);
                GridPane.setMargin(itemHBox, new javafx.geometry.Insets(8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadItemAjouterCourse(int column, int row) throws IOException {
        FXMLLoader fxmlLoaderAjout = new FXMLLoader();
        fxmlLoaderAjout.setLocation(getClass().getResource("/layouts/itemAjouterCourse.fxml"));

        HBox boutonAjoutHBox = fxmlLoaderAjout.load();

        itemsGrid.add(boutonAjoutHBox, column, row);
        GridPane.setMargin(boutonAjoutHBox, new javafx.geometry.Insets(8));
    }

    public void supprimerLivraison(ItemPoint item) {
        int idLivraison = item.getId();
        DemandeDeLivraions demande = controleurMetier.getDemandeDeLivraions();
        Livraison aSupprimer = demande.getLivraisonParId(idLivraison);

        if (aSupprimer != null) {
            controleurMetier.supprimerLivraison(aSupprimer);

            initData();
            afficherCarte();
            afficherPoints();
        }
    }

    public void supprimerLivraisonAdresse(Long adresse) {
        controleurMetier.supprimerLivraison(adresse);
        itemsGrid.getChildren().clear();
        initData();
        afficherCarte();
        afficherPoints();
    }

    @FXML
    private void retourAccueil() {
        try {
            controleurMetier.reinitialiserDemandeDeLivraison();
            controleurMetier.reinitialiserCarte();
            interfaceUtilisateur.afficherAccueil();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void retourCarte() {
        try {
            interfaceUtilisateur.afficherCarte();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double longToX(double longitude) {
        return (longitude - carte.getMinLong()) * gc.getCanvas().getWidth() / (carte.getMaxLong()- carte.getMinLong());
    }

    private double latToY(double latitude) {
        return gc.getCanvas().getHeight() - ((latitude - carte.getMinLat()) * gc.getCanvas().getHeight() / (carte.getMaxLat()- carte.getMinLat()));
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
                double x1 = longToX(dep.getLongitude());
                double y1 = latToY(dep.getLatitude());
                Noeud arr = carte.getMapNoeuds().get(troncon.getDestination());
                double x2 = longToX(arr.getLongitude());
                double y2 = latToY(arr.getLatitude());

                gc.setStroke(Color.web("#6B3F3A")); // ou 6B3F3A  ou 8C5752 et couleur pour tracer la tournée --> #D65C4F
                gc.setLineWidth(2);
                gc.strokeLine(x1, y1, x2, y2);
            }

        }
    }

    public void afficherPoints() {
        DemandeDeLivraions demandeDeLivraions = controleurMetier.getDemandeDeLivraions();

        for (Livraison livraison : demandeDeLivraions.getListeLivraisons()) {
            Color couleur = Couleur.getCouleur(livraison.getId());

            Noeud noeud = carte.obtenirNoeud(livraison.getAdresseEnlevement());
            double x = longToX(noeud.getLongitude());
            double y = latToY(noeud.getLatitude());

            gc.setFill(couleur);
            int rayon = 10;
            gc.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
            listeCoordonnees.add(new double[]{x - rayon, y - rayon, x + rayon, y + rayon});

            noeud = carte.obtenirNoeud(livraison.getAdresseLivraison());
            x = longToX(noeud.getLongitude());
            y = latToY(noeud.getLatitude());

            gc.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
            listeCoordonnees.add(new double[]{x - rayon, y - rayon, x + rayon, y + rayon});
        }

        Noeud noeud = carte.obtenirNoeud(demandeDeLivraions.getEntrepot().getAdresss());
        double x = longToX(noeud.getLongitude());
        double y = latToY(noeud.getLatitude());

        int rayon = 20;

        double svgW = 576.0;
        double svgH = 512.0;
        double scale = 2.0 * rayon / svgW;
        double tx = x - (svgW * scale) / 2.0;
        double ty = y - (svgH * scale) / 2.0;

        gc.save();
        gc.translate(tx, ty);
        gc.scale(scale, scale);

        gc.setFill(Color.RED);
        gc.beginPath();
        gc.appendSVGPath("M575.8 255.5c0 18-15 32.1-32 32.1h-32l.7 160.2c0 2.7-.2 5.4-.5 8.1v16.2c0 22.1-17.9 40-40 40h-16c-1.1 0-2.2 0-3.3-.1c-1.4.1-2.8.1-4.2.1L416 512h-24c-22.1 0-40-17.9-40-40v-88c0-17.7-14.3-32-32-32h-64c-17.7 0-32 14.3-32 32v88c0 22.1-17.9 40-40 40h-55.9c-1.5 0-3-.1-4.5-.2c-1.2.1-2.4.2-3.6.2h-16c-22.1 0-40-17.9-40-40V360c0-.9 0-1.9.1-2.8v-69.7h-32c-18 0-32-14-32-32.1c0-9 3-17 10-24L266.4 8c7-7 15-8 22-8s15 2 21 7l255.4 224.5c8 7 12 15 11 24");
        gc.fill();
        gc.closePath();

        gc.restore();
    }


    @FXML
    private void afficherItineraire() {
        controleurMetier.preparerPlanTournee(controleurMetier.getCarte(), controleurMetier.getDemandeDeLivraions());
        try {
            interfaceUtilisateur.afficherItineraire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clicBoutonCourse() {
        if (modifCourse != 0) {
            modifCourse = 0;
            courseAmodifier = null;
            afficherCarte();
            afficherPoints();
            boutonAjoutCourse.setText("Ajouter une course");
        } else if (ajoutCourse == 0) {
            boutonAjoutCourse.setText("Annuler l'ajout");
            ajoutCourse = 1;
        } else {
            nouvelEnlevement = null;
            nouvelleLivraison = null;
            afficherCarte();
            afficherPoints();
            ajoutCourse = 0;
            boutonAjoutCourse.setText("Ajouter une course");
        }
    }


    @FXML
    private void clicCarte(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        if (ajoutCourse != 0) {
            for (Noeud noeud : carte.getListeNoeuds()) {
                double nodeX = longToX(noeud.getLongitude());
                double nodeY = latToY(noeud.getLatitude());
                double distance = Math.sqrt(Math.pow(x - nodeX, 2) + Math.pow(y - nodeY, 2));
                if (distance < 10) { // seuil de proximité
                    DemandeDeLivraions demandeDeLivraions = controleurMetier.getDemandeDeLivraions();
                    gc.setFill(Couleur.getCouleur(demandeDeLivraions.getCompteurId()));
                    int rayon = 7;
                    double xNoeud = longToX(noeud.getLongitude());
                    double yNoeud = latToY(noeud.getLatitude());
                    gc.fillOval(xNoeud - rayon, yNoeud - rayon, rayon * 2, rayon * 2);
                    listeCoordonnees.add(new double[]{xNoeud - rayon, yNoeud - rayon, xNoeud + rayon, yNoeud + rayon});

                    if (ajoutCourse == 1) {
                        nouvelEnlevement = noeud;
                        ajoutCourse = 2;
                    } else if (ajoutCourse == 2) {
                        nouvelleLivraison = noeud;
                        ajoutCourse = 0;
                        controleurMetier.ajouterLivraison(nouvelEnlevement.getId(), nouvelleLivraison.getId());
                        itemsGrid.getChildren().clear();
                        initData();
                        afficherCarte();
                        afficherPoints();
                        boutonAjoutCourse.setText("Ajouter une course");
                    }

                    break;
                }
            }
        } else if (modifCourse != 0) {
            for (Noeud noeud : carte.getListeNoeuds()) {
                double nodeX = longToX(noeud.getLongitude());
                double nodeY = latToY(noeud.getLatitude());
                double distance = Math.sqrt(Math.pow(x - nodeX, 2) + Math.pow(y - nodeY, 2));
                if (distance < 10) { // seuil de proximité
                    int indexLivraison = controleurMetier.getDemandeDeLivraions().getListeLivraisons().indexOf(courseAmodifier);
                    gc.setFill(Couleur.getCouleur(indexLivraison + 1));
                    int rayon = 10;
                    double xNoeud = longToX(noeud.getLongitude());
                    double yNoeud = latToY(noeud.getLatitude());
                    gc.fillOval(xNoeud - rayon, yNoeud - rayon, rayon * 2, rayon * 2);
                    // mise à jour des coordonnées pour le clic (remplacer l'ancien double[] par le nouveau), donc pas un .add()

                    if (modifCourse == 1) {
                        controleurMetier.modifierLivraison(indexLivraison, noeud.getId(), courseAmodifier.getAdresseLivraison());
                    } else if (modifCourse == 2) {
                        controleurMetier.modifierLivraison(indexLivraison, courseAmodifier.getAdresseEnlevement(), noeud.getId());
                    }
                    modifCourse = 0;
                    itemsGrid.getChildren().clear();
                    initData();
                    afficherCarte();
                    afficherPoints();
                    boutonAjoutCourse.setText("Ajouter une course");

                    break;
                }
            }
        } else {
            for (double[] coord : listeCoordonnees) {
                if (x >= coord[0] && x <= coord[2] && y >= coord[1] && y <= coord[3]) {
                    int index = listeCoordonnees.indexOf(coord);
                    int livraisonIndex = index / 2;
                    int isPickup = index % 2;

                    ButtonType buttonTypeModifier = new ButtonType("Modifier");
                    ButtonType buttonTypeSupprimer = new ButtonType("Supprimer");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Détails de la livraison " + (livraisonIndex + 1));
                    alert.initOwner(this.interfaceUtilisateur.getFenetrePrincipale());
                    alert.getButtonTypes().setAll(buttonTypeModifier, buttonTypeSupprimer, ButtonType.CLOSE);
                    Livraison livraison = controleurMetier.getDemandeDeLivraions().getListeLivraisons().get(livraisonIndex);
                    if (isPickup == 0) {
                        alert.setHeaderText("Point de retrait");
                        alert.setContentText("Adresse de retrait: " + livraison.getAdresseEnlevement().toString());
                    } else {
                        alert.setHeaderText("Point de livraison");
                        alert.setContentText("Adresse de livraison: " + livraison.getAdresseLivraison().toString());
                    }
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == buttonTypeSupprimer) {
                        supprimerLivraison(livraison.getAdresseEnlevement());
                    } else if (result.isPresent() && result.get() == buttonTypeModifier) {
                        modifCourse = isPickup == 0 ? 1 : 2;
                        courseAmodifier = livraison;
                        boutonAjoutCourse.setText("Annuler la modification");
                    }
                    break;
                }
            }
        }
    }

}
