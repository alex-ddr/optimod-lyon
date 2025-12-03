package fr.insa.optimod.modele;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Couleur {
    static protected double nombreOr = 137.5077;
    static protected Color derniereCouleur = Color.hsb(nombreOr, 1.0, 1.0);
    static protected ArrayList<Color> listeCouleurs = new ArrayList<>(java.util.Collections.singletonList(derniereCouleur));

    public Couleur() {}

    static public Color getCouleur(int index) {
        if (index < listeCouleurs.size()) {
            return listeCouleurs.get(index);
        } else {
            while (listeCouleurs.size() <= index) {
                prochaineCouleur();
            }
            return listeCouleurs.get(index);
        }
    }


    static public Color prochaineCouleur() {
        double nouvelleHue = (derniereCouleur.getHue() + nombreOr) % 360.0;
        if (nouvelleHue < 0) nouvelleHue += 360.0;
        Color nouvelleCouleur = Color.hsb(nouvelleHue, derniereCouleur.getSaturation(), derniereCouleur.getBrightness(), derniereCouleur.getOpacity());
        derniereCouleur = nouvelleCouleur;
        listeCouleurs.add(nouvelleCouleur);
        return nouvelleCouleur;
    }

    static public String getHexaCouleur(int index) {
        Color couleur = Couleur.getCouleur(index);
        int r = (int) Math.round(couleur.getRed() * 255);
        int g = (int) Math.round(couleur.getGreen() * 255);
        int b = (int) Math.round(couleur.getBlue() * 255);
        
        return String.format("#%02X%02X%02X", r, g, b);
    }
}
