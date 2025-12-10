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
import java.util.*;

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

    private ItemCourseAAjouter itemCourseAAjouter = null;
    private ItemCourseAAjouterControleur itemCourseAAjouterControleur = null;

    private int affichageBoutonAjouterCourse = 0; // 0 = bouton ajouter, 1 = course à ajouter
    private int ajoutPickupOuLivraison = 0; // 0 = non, 1 = pickup, 2 = livraison
    private Noeud nouveauPickup = null;
    private Noeud nouvelleLivraison = null;

    private Map<String, ZoneCliquable> zonesCliquables = new HashMap<>();

    private int modififactionAdresse = 0; // 0 pas en train de modifier, 1 = en train de modifier
    private Noeud nouvelleAdresse = null;
    private ItemPoint itemPointAModifier = null;
    private Livraison courseAmodifier = null;

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
            String titrePickup = l.getTitre() != null ? l.getTitre() : "Pickup #" + l.getId();
            ItemPoint pickup = new ItemPoint(l.getId(), true, titrePickup, controleurMetier.getRueNoeud(l.getAdresseEnlevement()));
            newItemPointList.add(pickup);

            String titreLivraison = l.getTitre() != null ? l.getTitre() : "Livraison #" + l.getId();
            ItemPoint livraison = new ItemPoint(l.getId(), false, titreLivraison, controleurMetier.getRueNoeud(l.getAdresseLivraison()));
            newItemPointList.add(livraison);
        }

        return newItemPointList;
    }

    public ItemPoint getItemPoint(int livraisonId, boolean estPickup) {
        for (ItemPoint itemPoint : itemPointList) {
            if (itemPoint.getId() == livraisonId && itemPoint.getEstPickup() == estPickup) {
                return itemPoint;
            }
        }
        return null;
    }

    private void updateGrid() {
        itemsGrid.getChildren().clear();

        int column = 0;
        int row = 1;

        try {

            if (affichageBoutonAjouterCourse == 0) {
                loadItemAjouterCourse(column, row);
                column++;
                if (column == 1) {
                    column = 0;
                    ++row;
                }
            }
            else {
                loadItemCourseAAjouter(column, row);
                column++;
                if (column == 1) {
                    column = 0;
                    ++row;
                }

            }

            for (ItemPoint itemPoint : itemPointList) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layouts/itemPoint.fxml"));

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
        FXMLLoader fxmlLoaderAjout = new FXMLLoader(getClass().getResource("/layouts/itemAjouterCourse.fxml"));

        HBox boutonAjoutHBox = fxmlLoaderAjout.load();

        ItemAjouterCourseControleur controller = fxmlLoaderAjout.getController();

        if (controller != null) {
            controller.setPointsControleur(this);
        }

        itemsGrid.add(boutonAjoutHBox, column, row);
        GridPane.setMargin(boutonAjoutHBox, new javafx.geometry.Insets(8));
    }

    public void creerItemCourseAAjouter() {
        DemandeDeLivraions demandeDeLivraions = controleurMetier.getDemandeDeLivraions();
        this.itemCourseAAjouter = new ItemCourseAAjouter(demandeDeLivraions.getCompteurId());
    }

    public void loadItemCourseAAjouter(int column, int row) throws IOException {
        FXMLLoader fxmlLoaderAjout = new FXMLLoader(getClass().getResource("/layouts/itemCourseAAjouter.fxml"));
        HBox itemCourseAAjouterHBox = fxmlLoaderAjout.load();

        this.itemCourseAAjouterControleur = fxmlLoaderAjout.getController();


        if (this.itemCourseAAjouterControleur != null) {
            this.itemCourseAAjouterControleur.setPointsControleur(this);
            DemandeDeLivraions demande = controleurMetier.getDemandeDeLivraions();
            this.itemCourseAAjouterControleur.initialize(demande.getCompteurId());

            if(nouveauPickup != null) this.itemCourseAAjouterControleur.setAdressePickup(nouveauPickup.getId());
            if(nouvelleLivraison != null) this.itemCourseAAjouterControleur.setAdresseLivraison(nouvelleLivraison.getId());
        }

        itemsGrid.add(itemCourseAAjouterHBox, column, row);
        GridPane.setMargin(itemCourseAAjouterHBox, new javafx.geometry.Insets(8));
    }

    public void setModeSelectionPickup() {
        if (ajoutPickupOuLivraison == 0 && modififactionAdresse == 0) {
            ajoutPickupOuLivraison = 1;
        }
    }

    public void setModeSelectionLivraison() {
        if (ajoutPickupOuLivraison == 0 && modififactionAdresse == 0) {
            ajoutPickupOuLivraison = 2;
        }
    }

    private void setModeModificationAdresse() {
        if (ajoutPickupOuLivraison == 0) {
            modififactionAdresse = 1;
        }
    }

    public void modifierAdressePoint(ItemPoint item) {
        setModeModificationAdresse();
        if (modififactionAdresse == 1) {
            this.itemPointAModifier = item;
        }
    }

    public void annulerAjout() {
        itemCourseAAjouter = null;
        affichageBoutonAjouterCourse = 0;
        ajoutPickupOuLivraison = 0;
        nouveauPickup = null;
        nouvelleLivraison = null;
        updateGrid();
        afficherCarte();
        afficherPoints();
    }

    public void validerAjoutFinal() {
        if (nouveauPickup != null && nouvelleLivraison != null) {
            String titreSaisi = itemCourseAAjouterControleur.getTitre();
            int livraisonId;

            if (titreSaisi != null && !titreSaisi.trim().isEmpty()) {
                livraisonId = controleurMetier.ajouterLivraison(titreSaisi, nouveauPickup.getId(), nouvelleLivraison.getId());
            } else {
                livraisonId = controleurMetier.ajouterLivraison(nouveauPickup.getId(), nouvelleLivraison.getId());
            }

            String clefEnlevement = "livraison_" + livraisonId + "_enlevement";
            zonesCliquables.put(clefEnlevement, new ZoneCliquable(livraisonId, true, longToX(nouveauPickup.getLongitude()), latToY(nouveauPickup.getLatitude()), 10));
            String clefLivraison = "livraison_" + livraisonId + "_livraison";
            zonesCliquables.put(clefLivraison, new ZoneCliquable(livraisonId, false, longToX(nouvelleLivraison.getLongitude()), latToY(nouvelleLivraison.getLatitude()), 10));

            itemCourseAAjouter = null;
            nouveauPickup = null;
            nouvelleLivraison = null;
            itemCourseAAjouterControleur = null;
            affichageBoutonAjouterCourse = 0;
            ajoutPickupOuLivraison = 0;

            initData();
            afficherCarte();
            afficherPoints();
        }
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
            controleurMetier.reinitialiserDemandeDeLivraison();
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

            gc.setFill(Color.WHITE);
            gc.setStroke(couleur);
            gc.setLineWidth(4);
            int rayon = 8;

            gc.fillRect(x - rayon, y - rayon, rayon * 2, rayon * 2);
            gc.strokeRect(x - rayon, y - rayon, rayon * 2, rayon * 2);

            String clefEnlevement = "livraison_" + livraison.getId() + "_enlevement";
            zonesCliquables.put(clefEnlevement, new ZoneCliquable(livraison.getId(), true, x, y, rayon));

            noeud = carte.obtenirNoeud(livraison.getAdresseLivraison());
            x = longToX(noeud.getLongitude());
            y = latToY(noeud.getLatitude());

            gc.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
            gc.strokeOval(x - rayon, y - rayon, rayon * 2, rayon * 2);

            String clefLivraison = "livraison_" + livraison.getId() + "_livraison";
            zonesCliquables.put(clefLivraison, new ZoneCliquable(livraison.getId(), false, x, y, rayon));
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
        affichageBoutonAjouterCourse = 1;
        updateGrid();


    }

    private void dessinerPointsTemporaires() {
        DemandeDeLivraions demande = controleurMetier.getDemandeDeLivraions();
        Color couleur = Couleur.getCouleur(demande.getCompteurId());
        int rayon = 8;

        if (nouveauPickup != null) {
            double x = longToX(nouveauPickup.getLongitude());
            double y = latToY(nouveauPickup.getLatitude());

            gc.setFill(couleur);
            gc.fillRect(x - rayon, y - rayon, rayon * 2, rayon * 2);
        }
        if (nouvelleLivraison != null) {
            double x = longToX(nouvelleLivraison.getLongitude());
            double y = latToY(nouvelleLivraison.getLatitude());

            gc.setFill(couleur);
            gc.fillOval(x - rayon, y - rayon, rayon * 2, rayon * 2);
        }
    }

    private void rafraichirVueComplete() {
        afficherCarte();
        afficherPoints();
        dessinerPointsTemporaires();
    }

    @FXML
    private void clicCarte(MouseEvent event) {

        double x = event.getX();
        double y = event.getY();

        if (modififactionAdresse != 0 || ajoutPickupOuLivraison != 0) {
            for (Noeud noeud : carte.getListeNoeuds()) {
                double nodeX = longToX(noeud.getLongitude());
                double nodeY = latToY(noeud.getLatitude());
                double distance = Math.sqrt(Math.pow(x - nodeX, 2) + Math.pow(y - nodeY, 2));

                if (distance < 10) {
                    double xNoeud = longToX(noeud.getLongitude());
                    double yNoeud = latToY(noeud.getLatitude());

                    if (modififactionAdresse == 1 && itemPointAModifier != null) {
                        DemandeDeLivraions demande = controleurMetier.getDemandeDeLivraions();
                        Livraison livraisonAModifier = demande.getLivraisonParId(itemPointAModifier.getId());

                        gc.setFill(Couleur.getCouleur(livraisonAModifier.getId()));
                        int rayon = 10;
                        gc.fillOval(xNoeud - rayon, yNoeud - rayon, rayon * 2, rayon * 2);

                        if (livraisonAModifier != null) {
                            if (itemPointAModifier.getEstPickup()) {
                                livraisonAModifier.setAdresseEnlevement(noeud.getId());
                                zonesCliquables.put("livraison_" + livraisonAModifier.getId() + "_enlevement", new ZoneCliquable(livraisonAModifier.getId(), true, xNoeud, yNoeud, rayon));
                            } else {
                                livraisonAModifier.setAdresseLivraison(noeud.getId());
                                zonesCliquables.put("livraison_" + livraisonAModifier.getId() + "_livraison", new ZoneCliquable(livraisonAModifier.getId(), false, xNoeud, yNoeud, rayon));
                            }
                        }

                        modififactionAdresse = 0;
                        itemPointAModifier = null;

                        initData();
                        afficherCarte();
                        afficherPoints();

                        return;
                    } else if (ajoutPickupOuLivraison == 1) {
                    nouveauPickup = noeud;

                    if (itemCourseAAjouterControleur != null) {
                        itemCourseAAjouterControleur.setAdressePickup(nouveauPickup.getId());
                    }
                    itemCourseAAjouter.setAdressePickup(nouveauPickup.getId());

                    rafraichirVueComplete();

                    ajoutPickupOuLivraison = 0;
                    return;

                } else if (ajoutPickupOuLivraison == 2) {
                    nouvelleLivraison = noeud;

                    if (itemCourseAAjouterControleur != null) {
                        itemCourseAAjouterControleur.setAdresseLivraison(nouvelleLivraison.getId());
                    }
                    itemCourseAAjouter.setAdresseLivraison(nouvelleLivraison.getId());

                    rafraichirVueComplete();

                    ajoutPickupOuLivraison = 0;
                    return;
                }

                    break;
                }
            }
        } else if (affichageBoutonAjouterCourse != 1){
            for (ZoneCliquable zone : zonesCliquables.values()) {
                if (zone.contient(x, y)) {
                    int livraisonId = zone.getLivraisonId();
                    boolean isEnlevement = zone.isEnlevement();

                    ButtonType buttonTypeModifier = new ButtonType("Modifier");
                    ButtonType buttonTypeSupprimer = new ButtonType("Supprimer");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    DemandeDeLivraions demande = controleurMetier.getDemandeDeLivraions();
                    String titre = demande.getLivraisonParId(zone.getLivraisonId()).getTitre() == null ? "#"+zone.getLivraisonId() : demande.getLivraisonParId(zone.getLivraisonId()).getTitre();

                    alert.setTitle("Détails de la course " + titre);
                    alert.initOwner(this.interfaceUtilisateur.getFenetrePrincipale());
                    alert.getButtonTypes().setAll(buttonTypeModifier, buttonTypeSupprimer, ButtonType.CLOSE);
                    Livraison livraison = controleurMetier.getDemandeDeLivraions().getLivraisonParId(livraisonId);
                    if (isEnlevement) {
                        alert.setHeaderText("Point de pickup de la course " + titre);
                        alert.setContentText("Adresse de pickup: " + controleurMetier.getRueNoeud(livraison.getAdresseEnlevement()));
                    } else {
                        alert.setHeaderText("Point de livraison de la course " + titre);
                        alert.setContentText("Adresse de livraison: " + controleurMetier.getRueNoeud(livraison.getAdresseLivraison()));
                    }
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == buttonTypeSupprimer) {
                        ItemPoint itemPointASupprimer = getItemPoint(livraisonId, isEnlevement);
                        supprimerLivraison(itemPointASupprimer);
                    } else if (result.isPresent() && result.get() == buttonTypeModifier) {
                        itemPointAModifier = getItemPoint(livraisonId, isEnlevement);
                        modifierAdressePoint(itemPointAModifier);
                    }
                    break;
                }
            }
        }
    }
}
