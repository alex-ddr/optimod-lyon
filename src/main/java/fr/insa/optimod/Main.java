package fr.insa.optimod;

import fr.insa.optimod.controleur.Controleur;
import fr.insa.optimod.modele.Carte;

public class Main {
    public static void main(String[] args) {
        Controleur controleur = new Controleur();
        Carte carte = controleur.initialiserCarte("xml/ada.xml");
    }
}

