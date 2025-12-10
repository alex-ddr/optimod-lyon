package fr.insa.optimod;

import fr.insa.optimod.controleur.PdfControleur;
import fr.insa.optimod.modele.Noeud;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PdfControlleurTest {

    static PdfControleur pdfControleur;
    static Noeud n1;
    static Noeud n2;
    static Noeud n3;
    static Noeud n4;
    static Noeud n5;
    static Noeud n6;

    @BeforeAll
    public static void setUp() {
        pdfControleur = new PdfControleur();
        n1 = new Noeud(0L,0D,0D);  // centre
        n2 = new Noeud(1L, 0D, 1D);  // nord
        n3 = new Noeud(2L,1D,0D);  // est
        n4 = new Noeud(3L, 0D, -1D);  // sud
        n5 = new Noeud(4L, -0.000001D, 0D); // ouest proche
        n6 = new Noeud(5L, 0D, 0.000001D);  // nord proche
    }

    @Test
    public void donnerDirectionRight(){
        assertEquals(pdfControleur.donnerDirection(n1, n2, n3), 1);
    }

    @Test
    public void donnerDirectionLeft(){
        assertEquals(pdfControleur.donnerDirection(n3, n1, n4), 0);
    }

    @Test
    public void donnerDirectioDemiTour(){
        assertEquals(pdfControleur.donnerDirection(n1, n2, n1), -2);
    }

    @Test
    public void donnerDirectioToutDroit(){
        assertEquals(pdfControleur.donnerDirection(n4, n1, n2), -1);
    }

    @Test
    public void donnerDirectioDepartNord(){
        assertEquals(pdfControleur.donnerDirectionDepart(n1, n2), "Nord");
    }

    @Test
    public void donnerDirectioDepartSudEst(){
        assertEquals(pdfControleur.donnerDirectionDepart(n2, n3), "Sud-Est");
    }

    @Test
    public void donnerDirectioDepartOuest(){
        assertEquals(pdfControleur.donnerDirectionDepart(n3, n1), "Ouest");
    }

    @Test
    public void donnerDirectioDepartOuestProche(){
        assertEquals(pdfControleur.donnerDirectionDepart(n1, n5), "Ouest");
    }

    @Test
    public void donnerDirectioDepartNordEstProche(){
        assertEquals(pdfControleur.donnerDirectionDepart(n1, n6), "Nord");
    }



}
