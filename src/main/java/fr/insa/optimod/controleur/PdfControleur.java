package fr.insa.optimod.controleur;

import fr.insa.optimod.modele.Noeud;
import fr.insa.optimod.modele.PointLivraison;
import fr.insa.optimod.modele.Troncon;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PdfControleur {

    private final String name = "LivreurDay.pdf";
    private final String chemin = "src\\main\\out";

    public void afficherPdf() throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
            contentStream.beginText();
            // Positionne le texte en haut de la page (ex: 100 en X, 700 en Y)
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Hello World");
            contentStream.endText();
        }

        document.save("src\\main\\out\\pdfBoxHelloWorld.pdf");
        System.out.println("PDF created");
        document.close();
    }

    public void afficherPdfRue(ArrayList<Troncon> troncons) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDRectangle mediabox = page.getMediaBox();
        float margin = 72;
        float startX = mediabox.getLowerLeftX() + margin;
        float startY = mediabox.getUpperRightY() - margin;

        float fontSizeTitre = 25;
        float fontSize = 12;
        float offsetTitre = 1.3f * fontSizeTitre;
        float offset = 1.3f * fontSize;

        PDFont font = new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText();

            contentStream.setFont(font, fontSizeTitre);
            contentStream.newLineAtOffset(startX, startY);
            contentStream.showText("Trajet pour les livraisons du jour");

            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(0, -2*offsetTitre);

            for(Troncon troncon : troncons) {
                contentStream.showText(troncon.getNomRue());
                contentStream.newLineAtOffset(0, -offset);
            }
            contentStream.endText();
            contentStream.close();
            document.save(new File(chemin, name));
            System.out.println("PDF created");
        }
        finally
        {
            document.close();
        }
    }

    public void extractPdf(ArrayList<Troncon> troncons, ArrayList<PointLivraison> livraisons) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDRectangle mediabox = page.getMediaBox();
        float margin = 72;
        float startX = mediabox.getLowerLeftX() + margin;
        float startY = mediabox.getUpperRightY() - margin;

        float fontSizeTitre = 25;
        float fontSize = 12;
        float offsetTitre = 1.3f * fontSizeTitre;
        float offset = 1.3f * fontSize;

        PDFont font = new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText();

            contentStream.setFont(font, fontSizeTitre);
            contentStream.newLineAtOffset(startX, startY);
            contentStream.showText("Trajet pour les livraisons du jour");

            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(0, -2*offsetTitre);

            for(int i = 0; i < livraisons.size(); i++) {
                contentStream.showText(troncons.get(i).getNomRue());
                contentStream.newLineAtOffset(0, -offset);
            }
            contentStream.endText();
            contentStream.close();
            document.save(new File(chemin, name));
            System.out.println("PDF created");
        }
        finally
        {
            document.close();
        }
    }

    // Indique si le passage du segment [n1, n2] au [n2, n3] nécéssite de tourner à droite (1) ou à gauche (0)
    private int giveDirection(Noeud n1, Noeud n2, Noeud n3){

    }


}
