package fr.insa.optimod;

import fr.insa.optimod.controleur.Controleur;
import fr.insa.optimod.modele.Carte;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Controleur controleur = new Controleur();
        File f = new File("C:\\Users\\robin\\IdeaProjects\\optimod-lyon\\src\\main\\ressources\\xml\\petitPlan.xml");
        System.out.println(f.exists());
        Carte carte = controleur.initialiserCarte("C:\\Users\\robin\\IdeaProjects\\optimod-lyon\\src\\main\\ressources\\xml\\petitPlan.xml");
    }
}

