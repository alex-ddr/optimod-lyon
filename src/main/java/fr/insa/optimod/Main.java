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
        File f = new File("/xml/petitPlan.xml");
        System.out.println(f.exists());
        Carte carte = controleur.initialiserCarte("/xml/petitPlan.xml");

        ArrayList<Noeud> listeNoeuds = carte.getListeNoeuds();

        /*for(Noeud noeud : listeNoeuds){
            System.out.println(noeud.getId());
            System.out.println(noeud.getLatitude());
        }
        */
        DemandeDeLivraions demande = controleur.initialiserDemandeDeLivraions("/xml/demandeMoyen5.xml");

        ArrayList<Livraison> listeLivraions = demande.getListeLivraisons();

        /*for(Livraison livraison : listeLivraions){
            System.out.println(livraison.getDureeLivraison());
        }
        */

        controleur.ajouterAdjacense(carte);
        PointLivraison d = controleur.astar(carte, carte.obtenirNoeud(21992645), carte.obtenirNoeud(55444215));
        PointLivraison d1 = controleur.astar(carte, carte.obtenirNoeud(26155372), carte.obtenirNoeud(1036842078));


        System.out.println(d.getG());
        System.out.println(d1.getG());
        /*
        while (d != null) {
            System.out.println(d.getNoeud().getId());
            d = d.getParent();
        }
        */
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


    }
}

