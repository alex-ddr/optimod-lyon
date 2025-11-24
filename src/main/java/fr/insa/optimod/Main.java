package fr.insa.optimod;

import fr.insa.optimod.controleur.Controleur;
import fr.insa.optimod.modele.Carte;
import fr.insa.optimod.modele.DemandeDeLivraions;
import fr.insa.optimod.modele.Livraison;
import fr.insa.optimod.modele.Noeud;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
        Carte carte = controleur.initialiserCarte(cheminAbsoluCarte);

        ArrayList<Noeud> listeNoeuds = carte.getListeNoeuds();
        /*
        for(Noeud noeud : listeNoeuds){
            System.out.println(noeud.getId());
            System.out.println(noeud.getLatitude());
        }
        */
        DemandeDeLivraions demande = controleur.initialiserDemandeDeLivraions(cheminAbsoluDemande);

        ArrayList<Livraison> listeLivraions = demande.getListeLivraisons();

        for(Livraison livraison : listeLivraions){
            System.out.println(livraison.getDureeLivraison());
        }






    }
}

