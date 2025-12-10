package fr.insa.optimod;

import fr.insa.optimod.controleur.CarteControleur;
import fr.insa.optimod.controleur.Controleur;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class CarteControlleurTest {

    static CarteControleur controleur;
    static File demandeMoyen;
    static File demandeEnglish;
    static File demandeGrand;

    @BeforeAll
    public static void setUp(){
        controleur = new CarteControleur();
        Controleur controleurMetier = new Controleur();
        String planPath = AcceuilControleurTest.class.getResource("/xml/moyenPlan.xml").getPath();
        assertNotNull(planPath, "Fichier introuvable");
        controleurMetier.initialiserCarte(planPath);
        controleur.setControleurMetier(controleurMetier);
        String demandeMoyenPath = AcceuilControleurTest.class.getResource("/xml/demandeMoyen5.xml").getPath();
        String demandeEnglishPath = AcceuilControleurTest.class.getResource("/xml/requestsSmall1.xml").getPath();
        String demandeGrandPath = AcceuilControleurTest.class.getResource("/xml/demandeGrand9.xml").getPath();
        assertNotNull(demandeMoyenPath, "Fichier introuvable");
        assertNotNull(demandeEnglishPath, "Fichier introuvable");
        assertNotNull(demandeGrandPath, "Fichier introuvable");
        demandeMoyen = new File(demandeMoyenPath);
        demandeEnglish = new File(demandeEnglishPath);
        demandeGrand = new File(demandeGrandPath);
    }
    @Test
    public void testValidLivraisons(){
        assertTrue(controleur.isValidXML(demandeMoyen));
    }

    @Test
    public void testEnglish(){
        assertTrue(controleur.isValidXML(demandeEnglish));
    }

    @Test
    public void testDemandeGrandSurCarteMoyenne(){
        assertFalse(controleur.isValidXML(demandeGrand));
    }

}
