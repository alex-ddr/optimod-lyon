package fr.insa.optimod;

import fr.insa.optimod.controleur.Controleur;
import fr.insa.optimod.modele.Carte;
import fr.insa.optimod.modele.DemandeDeLivraions;
import fr.insa.optimod.modele.Livraison;
import fr.insa.optimod.modele.Noeud;

import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Controleur controleur = new Controleur();
        File f = new File("C:\\Users\\robin\\IdeaProjects\\optimod-lyon\\src\\main\\resources\\xml\\petitPlan.xml");
        System.out.println(f.exists());
        Carte carte = controleur.initialiserCarte("C:\\Users\\robin\\IdeaProjects\\optimod-lyon\\src\\main\\resources\\xml\\petitPlan.xml");

        ArrayList<Noeud> listeNoeuds = carte.getListeNoeuds();
        /*
        for(Noeud noeud : listeNoeuds){
            System.out.println(noeud.getId());
            System.out.println(noeud.getLatitude());
        }
        */
        DemandeDeLivraions demande = controleur.initialiserDemandeDeLivraions("C:\\Users\\robin\\IdeaProjects\\optimod-lyon\\src\\main\\resources\\xml\\demandePetit1.xml");

        ArrayList<Livraison> listeLivraions = demande.getListeLivraisons();

        for(Livraison livraison : listeLivraions){
            System.out.println(livraison.getDureeLivraison());
        }






    }
}

