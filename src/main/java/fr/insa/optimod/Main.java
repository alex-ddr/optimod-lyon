package fr.insa.optimod;

import fr.insa.optimod.controleur.Controleur;
import fr.insa.optimod.modele.*;
import fr.insa.optimod.vue.Interface;
import javafx.application.Application;

import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {


        Application.launch(Interface.class, args);




        /*

        Controleur controleur = new Controleur();
        File f = new File("src\\main\\resources\\xml\\grandPlan.xml");
        System.out.println(f.exists());
        controleur.initialiserCarte("src\\main\\resources\\xml\\grandPlan.xml");

        ArrayList<Noeud> listeNoeuds = controleur.getCarte().getListeNoeuds();





       controleur.initialiserDemandeDeLivraions("src\\main\\resources\\xml\\demandeGrand7.xml");




        controleur.preparerPlanTournee(controleur.getCarte(), controleur.getDemandeDeLivraions());

        ArrayList<PointLivraison> Tsp = controleur.getTspListe();

        for (PointLivraison p : Tsp) {
            System.out.println(p.getNoeud().getId());
            System.out.println(p.getG());
        }

        */

    }
}

