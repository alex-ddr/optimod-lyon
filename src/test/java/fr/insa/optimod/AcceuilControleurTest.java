package fr.insa.optimod;

import fr.insa.optimod.controleur.AccueilControleur;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class AcceuilControleurTest {

    static AccueilControleur controleur;
    static File grandPlan;
    static File demande;
    static File tournee;


    @BeforeAll
    public static void setUpClass(){
        controleur = new AccueilControleur();
        String grandPlanPath = AcceuilControleurTest.class.getResource("/xml/grandPlan.xml").getPath();
        String demandeGrand = AcceuilControleurTest.class.getResource("/xml/demandeGrand9.xml").getPath();
        String tourneePath = AcceuilControleurTest.class.getResource("/pdf/LivreurDay.pdf").getPath();
        assertNotNull(grandPlanPath, "Fichier introuvable");
        assertNotNull(demandeGrand, "Fichier introuvable");
        assertNotNull(tourneePath, "Fichier introuvable");
        grandPlan = new File(grandPlanPath);
        demande = new File(demandeGrand);
        tournee = new File(tourneePath);

    }

    @Test
    public void validTestXml() {
        System.out.println("Test valid: ");
        assertTrue(controleur.isValidXML(grandPlan));
    }

    @Test
    public void wrongTestXml() {
        System.out.println("Test wrong: ");
        assertFalse(controleur.isValidXML(demande));
    }

    @Test
    public void wrongTestPdf() {
        System.out.println("Test wrong 2: ");
        assertFalse(controleur.isValidXML(tournee));
    }

}
