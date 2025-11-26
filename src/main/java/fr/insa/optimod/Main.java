package fr.insa.optimod;

import fr.insa.optimod.controleur.Controleur;
import fr.insa.optimod.modele.*;
import fr.insa.optimod.vue.Interface;
import javafx.application.Application;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        Application.launch(Interface.class, args);




        /*
        Controleur controleur = new Controleur();

        String cheminRessourceCarte = "xml/petitPlan.xml";
        String cheminRessouceDemande = "xml/demandePetit1.xml";
        URL urlCarte = Main.class.getClassLoader().getResource(cheminRessourceCarte);
        URL urlDemande = Main.class.getClassLoader().getResource(cheminRessouceDemande);

        if (urlCarte == null) {
            System.err.println("Fichier non trouvé : " + cheminRessourceCarte);
            return;
        }
        // Convertir l'URL en chemin de fichier
        String cheminAbsoluCarte = null;
        try {
            cheminAbsoluCarte = Paths.get(urlCarte.toURI()).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Chemin absolu Carte : " + cheminAbsoluCarte);

        if (urlDemande == null) {
            System.err.println("Fichier non trouvé : " + cheminRessouceDemande);
            return;
        }
        // Convertir l'URL en chemin de fichier
        String cheminAbsoluDemande = null;
        try {
            cheminAbsoluDemande = Paths.get(urlDemande.toURI()).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Chemin absolu Demande : " + cheminAbsoluDemande);


        File f = new File(cheminAbsoluCarte);
        System.out.println(f.exists());
        controleur.initialiserCarte(cheminAbsoluCarte);
        Carte carte = controleur.getCarte();

        ArrayList<Noeud> listeNoeuds = carte.getListeNoeuds();
        *//*
        for(Noeud noeud : listeNoeuds){
            System.out.println(noeud.getId());
            System.out.println(noeud.getLatitude());
        }
        *//*
        DemandeDeLivraions demande = controleur.initialiserDemandeDeLivraions(cheminAbsoluDemande);

        ArrayList<Livraison> listeLivraions = demande.getListeLivraisons();

        *//*for(Livraison livraison : listeLivraions){
            System.out.println(livraison.getDureeLivraison());
        }
        *//*
        *//*
        controleur.ajouterAdjacense(carte);
        PointLivraison d = controleur.astar(carte, carte.obtenirNoeud(21992645L), carte.obtenirNoeud(55444215L));
        PointLivraison d1 = controleur.astar(carte, carte.obtenirNoeud(26155372L), carte.obtenirNoeud(1036842078L));


        System.out.println(d.getG());
        System.out.println(d1.getG());

        while (d != null) {
            System.out.println(d.getNoeud().getId());
            d = d.getParent();
        }

        HashMap<Long, ArrayList<PointLivraison> > tournee = controleur.preparerPlanTournee(carte, demande);

        for (Map.Entry<Long, ArrayList<PointLivraison>> entry : tournee.entrySet()) {

            Long idDepart = entry.getKey();
            ArrayList<PointLivraison> liste = entry.getValue();

            System.out.println("Tournee pour le point " + idDepart + " :");

            for (PointLivraison p : liste) {
                PointLivraison m = p;
                System.out.println("  Point " + p.getNoeud().getId() + " -> g = " + p.getG());
                while (m != null) {
                    System.out.println(m.getNoeud().getId());
                    m = m.getParent();
                }
            }

            System.out.println("-----------------------");
        }
        *//*
*/

    }
}

