package fr.insa.optimod;
import fr.insa.optimod.controleur.Controleur;
import fr.insa.optimod.modele.*;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class AstarTest {

    @Test
    public void testAstar() {

        System.out.println("Test 1: ");
        // 1) Charger le XML de test depuis src/test/resources
        String xmlPath = getClass().getResource("/xml/testAstar.xml").getPath();
        assertNotNull(xmlPath, "Fichier introuvable");

        // 2) Créer le contrôleur et charger la carte
        Controleur controleur = new Controleur();
        controleur.initialiserCarte(xmlPath);
        Carte carte = controleur.getCarte();
        controleur.ajouterAdjacense(carte);


        // 3) Récupérer les noeuds 100 et 300
        Noeud depart = carte.obtenirNoeud(100L);
        Noeud arrivee = carte.obtenirNoeud(300L);

        assertNotNull(depart, "Noeud 100 introuvable dans la carte");
        assertNotNull(arrivee, "Noeud 300 introuvable dans la carte");
        //System.out.println("tron = " + carte.getListeTroncon().size());

        // 4) Appeler A*
        PointLivraison resultat = controleur.astar(carte, depart, arrivee);

        assertNotNull(resultat, "A* n'a retourné aucun chemin");

        double cout = resultat.getG();
        System.out.println("Coût A* = " + cout);
        System.out.println("Coût théorique = " + 100.0 / 4.166666667);

        // 5) Vérifier que le coût est bien 100.0 (100 -> 200 -> 300)
        assertEquals(100.0 / 4.1666666667, cout, 1e-3,
                "Le coût A* n'est pas celui du plus court chemin 100->200->300");

        // 6) (Optionnel) Vérifier que le chemin est bien 100 -> 200 -> 300
        StringBuilder sb = new StringBuilder();
        PointLivraison courant = resultat;
        while (courant != null) {
            // on insère au début pour avoir l'ordre départ -> arrivée
            if (sb.length() == 0) {
                sb.insert(0, courant.getNoeud().getId());
            } else {
                sb.insert(0, courant.getNoeud().getId() + " -> ");
            }
            courant = courant.getParent();
        }

        String chemin = sb.toString();
        System.out.println("Chemin trouvé : " + chemin);

        assertEquals("100 -> 200 -> 300", chemin,
                "Le chemin A* n'est pas 100 -> 200 -> 300");

    }

    @Test
    public void testAstar2() {
        System.out.println("Test 2: ");

        String xmlPath = getClass().getResource("/xml/testAstarComplexe.xml").getPath();
        assertNotNull(xmlPath, "Fichier introuvable");

        // 2) Créer le contrôleur et charger la carte
        Controleur  controleur = new Controleur();
        controleur.initialiserCarte(xmlPath);
        Carte carte = controleur.getCarte();
        controleur.ajouterAdjacense(carte);


        // 3) Récupérer les noeuds 100 et 500
        Noeud depart = carte.obtenirNoeud(100L);
        Noeud arrivee = carte.obtenirNoeud(500L);

        assertNotNull(depart, "Noeud 100 introuvable dans la carte");
        assertNotNull(arrivee, "Noeud 500 introuvable dans la carte");
        //System.out.println("tron = " + carte.getListeTroncon().size());

        // 4) Appeler A*
        PointLivraison resultat = controleur.astar(carte, depart, arrivee);

        assertNotNull(resultat, "A* n'a retourné aucun chemin");

        double cout = resultat.getG();
        System.out.println("Coût A* = " + cout);
        System.out.println("Coût théorique = " + 200.0 / 4.1666666667);

        // 5) Vérifier que le coût est bien 100.0 (100 -> 200 -> 300)
        assertEquals(200.0 / 4.1666666667, cout, 1e-4,
                "A* devrait trouver le chemin 100->200->300->400->500 de coût 200.0");

        // 6) Vérifier que le chemin est bien 100 -> 200 -> 300
        StringBuilder sb = new StringBuilder();
        PointLivraison courant = resultat;
        while (courant != null) {
            // on insère au début pour avoir l'ordre départ -> arrivée
            if (sb.length() == 0) {
                sb.insert(0, courant.getNoeud().getId());
            } else {
                sb.insert(0, courant.getNoeud().getId() + " -> ");
            }
            courant = courant.getParent();
        }

        String chemin = sb.toString();
        System.out.println("Chemin trouvé : " + chemin);

        assertEquals("100 -> 200 -> 300 -> 400 -> 500", chemin,
                "Le chemin A* n'est pas celui attendu 100 -> 200 -> 300 -> 400 -> 500");

    }

    @Test
    public void testAstar3() {
        System.out.println("Test 3 (Non relié): ");

        String xmlPath = getClass().getResource("/xml/testAstarPasRelie.xml").getPath();
        assertNotNull(xmlPath, "Fichier introuvable");

        Controleur controleur = new Controleur();
        controleur.initialiserCarte(xmlPath);
        Carte carte = controleur.getCarte();
        controleur.ajouterAdjacense(carte);


        Noeud depart = carte.obtenirNoeud(100L);
        Noeud arrivee = carte.obtenirNoeud(400L);

        assertNotNull(depart, "Noeud 100 introuvable dans la carte");
        assertNotNull(arrivee, "Noeud 400 introuvable dans la carte");
        //System.out.println("tron = " + carte.getListeTroncon().size());

        PointLivraison resultat = controleur.astar(carte, depart, arrivee);

        assertNull(resultat, "A* a retourné un chemin");


        System.out.println("Resultat = " + resultat);
    }
}







