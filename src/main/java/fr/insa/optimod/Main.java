package fr.insa.optimod;

import fr.insa.optimod.controleur.Controleur;
import fr.insa.optimod.modele.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Controleur controleur = new Controleur();
        File f = new File("src\\main\\resources\\xml\\petitPlan.xml");
        System.out.println(f.exists());
        Carte carte = controleur.initialiserCarte("src\\main\\resources\\xml\\petitPlan.xml");

        ArrayList<Noeud> listeNoeuds = carte.getListeNoeuds();

        /*for(Noeud noeud : listeNoeuds){
            System.out.println(noeud.getId());
            System.out.println(noeud.getLatitude());
        }
        */
        DemandeDeLivraions demande = controleur.initialiserDemandeDeLivraions("src\\main\\resources\\xml\\demandePetit2.xml");

        ArrayList<Livraison> listeLivraions = demande.getListeLivraisons();

        /*for(Livraison livraison : listeLivraions){
            System.out.println(livraison.getDureeLivraison());
        }
        */

        controleur.ajouterAdjacense(carte);
        PointLivraison d = controleur.astar(carte, carte.obtenirNoeud(1679901320l), carte.obtenirNoeud(208769457l));
        PointLivraison d1 = controleur.astar(carte, carte.obtenirNoeud(208769457l), carte.obtenirNoeud(1679901320l));


        System.out.println(d.getG());
        System.out.println(d1.getG());
        /*
        while (d != null) {
            System.out.println(d.getNoeud().getId());
            d = d.getParent();
        }
        */
        HashMap<int, ArrayList<PointLivraison> > tournee = controleur.preparerPlanTournee(carte, demande);

        for (Map.Entry<int, ArrayList<PointLivraison>> entry : tournee.entrySet()) {

            int idDepart = entry.getKey();
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


    }
}

