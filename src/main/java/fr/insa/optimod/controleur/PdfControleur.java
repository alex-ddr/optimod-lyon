package fr.insa.optimod.controleur;

import fr.insa.optimod.modele.Noeud;
import fr.insa.optimod.modele.PointLivraison;
import fr.insa.optimod.modele.Troncon;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import com.itextpdf.html2pdf.HtmlConverter;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PdfControleur {

    private final String name = "LivreurDay.pdf";
    private final String chemin = "src\\main\\out";
    private final String downloads = getDownloadsFolder();

    private FileChooser explorateur = new FileChooser();

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

    public void extrairePdf(ArrayList<Troncon> troncons, ArrayList<PointLivraison> chemin) throws IOException {
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

        PDPageContentStream contentStream = null;


        try {
            contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();

            contentStream.setFont(font, fontSizeTitre);
            contentStream.newLineAtOffset(startX, startY);
            contentStream.showText("Trajet pour les livraisons du jour");

            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(0, -2*offsetTitre);

            float currentY = startY - 2 * offsetTitre;

            String text;
            text = "Partir de l'entrepot vers le " + donnerDirectionDepart(chemin.get(0).getNoeud(), chemin.get(1).getNoeud()) + " sur la rue " + troncons.getFirst().getNomRue();
            contentStream.showText(text);
            contentStream.newLineAtOffset(0, -offset);

            if(chemin.size() < 3) {return;}  // ca va de l'entrepot à l'entrepot

            for(Troncon troncon: troncons) {
                System.out.println(troncon.getNomRue());
            }
            System.out.println(chemin.size());
            System.out.println(troncons.size());

            for(int i = 0; i < chemin.size()-3; i++) {
                if (currentY < margin) {
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(font, fontSize);
                    contentStream.newLineAtOffset(startX, startY);
                    currentY = startY;
                }

                text = ecriteText(chemin.get(i).getNoeud(), chemin.get(i+1).getNoeud(), chemin.get(i+2).getNoeud(), troncons.get(i+1).getNomRue());
                contentStream.showText(text);
                contentStream.newLineAtOffset(0, -offset);
                currentY -= offset;
            }

            text = "Retour à l'entrepot : " + ecriteText(chemin.get(chemin.size()-3).getNoeud(),chemin.get(chemin.size()-2).getNoeud(), chemin.getLast().getNoeud(),troncons.getLast().getNomRue());
            contentStream.showText(text);

            contentStream.endText();
            contentStream.close();

            explorateur.setTitle("Sauvegarder le PDF");
            explorateur.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));
            explorateur.setInitialFileName(name);
            File fichier = explorateur.showSaveDialog(null);

            if (fichier != null) {
                document.save(fichier);
            }

//            document.save(new File(downloads, name));
            System.out.println("PDF created");
        }
        finally
        {
            document.close();
        }
    }

    public void extrairePdf_2(ArrayList<Troncon> troncons, ArrayList<PointLivraison> chemin) throws IOException {
        try {
            List<String> listeInstructions = new ArrayList<>();

            String text;
            text = "Partir de l'entrepot vers le " + donnerDirectionDepart(chemin.get(0).getNoeud(), chemin.get(1).getNoeud()) + " sur la rue " + troncons.getFirst().getNomRue();
            listeInstructions.add(text);

            if(chemin.size() < 3) {return;}  // ca va de l'entrepot à l'entrepot

            for(Troncon troncon: troncons) {
                System.out.println(troncon.getNomRue());
            }
            System.out.println(chemin.size());
            System.out.println(troncons.size());

            for(int i = 0; i < chemin.size()-3; i++) {
                text = ecriteText(chemin.get(i).getNoeud(), chemin.get(i+1).getNoeud(), chemin.get(i+2).getNoeud(), troncons.get(i+1).getNomRue());
                listeInstructions.add(text);
            }

            text = "Retour à l'entrepot : " + ecriteText(chemin.get(chemin.size()-3).getNoeud(),chemin.get(chemin.size()-2).getNoeud(), chemin.getLast().getNoeud(),troncons.getLast().getNomRue());
            listeInstructions.add(text);

            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode("HTML");

            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);

            Context context = new Context();
            context.setVariable("dateDuJour", java.time.LocalDate.now().toString());
            context.setVariable("instructions", listeInstructions);

            String htmlContent = templateEngine.process("/templates/template_pdf", context);

            explorateur.setTitle("Sauvegarder le PDF");
            explorateur.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));
            explorateur.setInitialFileName(name);
            File fichier = explorateur.showSaveDialog(null);

            if (fichier != null) {
                try (OutputStream os = new FileOutputStream(fichier)) {
                    HtmlConverter.convertToPdf(htmlContent, new FileOutputStream(fichier));
                }
                System.out.println("PDF created!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Return Tourner à droite vers la rue [n2, n3] en partant de la rue [n1, n2]
    private String ecriteText(Noeud n1, Noeud n2, Noeud n3, String rue){
        String text = "";
        int direction = donnerDirection(n1, n2, n3);
        if (direction== 1){text += "Tourner à droite";}
        else if (direction == 0) {text += "Tourner à gauche";}
        else {text += "Aller tout droit";}
        text += " vers la rue " + rue;
        return text;
    }

    // Indique si le passage du segment [n1, n2] au [n2, n3] nécéssite de tourner à droite (1) ou à gauche (0)
    private int donnerDirection(Noeud n1, Noeud n2, Noeud n3){
        double lat1 = n2.getLatitude()-n1.getLatitude();
        double lat2 = n3.getLatitude()-n2.getLatitude();

        double lon1 = n2.getLongitude()-n1.getLongitude();
        double lon2 = n3.getLongitude()-n2.getLongitude();

        double dir1 = lat2 * lon1 - lat1 * lon2;


        if (dir1 > 0.000001)
        {
            return 0;
        }
        else if (dir1 < -0.000001)
        {
            return 1;
        }
        return -1;

    }

    private String donnerDirectionDepart(Noeud n1, Noeud n2){
        double lat1 = n2.getLatitude()-n1.getLatitude();
        double lon1 = n2.getLongitude()-n1.getLongitude();
        String retour = "";
        boolean b = false;
        if (lat1 > 0.000001)
        {
            retour = retour + "Nord";
            b = true;
        }
        else if (lat1 < -0.000001)
        {
            retour = retour + "Sud";
            b = true;
        }

        if (lon1 > 0.000001)
        {
            if(b)
            {
                retour = retour + "-Est";
            }
            else
            {
                retour = "Est";
                b = true;
            }

        }
        else if (lon1 < -0.000001)
        {
            if(b)
            {
                retour = retour + "-Ouest";
            }
            else
            {
                retour = "Ouest";
                b = true;
            }

        }

        if (!b)
        {
            if((lat1*lat1)>(lon1*lon1))
            {
                if (lat1>0)
                {
                    retour = retour + "Nord";
                }
                else
                {
                    retour = retour + "Sud";
                }
            }
            else
            {
                if (lon1>0)
                {
                    retour = retour + "Est";
                }
                else
                {
                    retour = retour + "Ouest";
                }
            }
        }

        return retour;

    }

    //IA
    private static String getDownloadsFolder() {
        String home = System.getProperty("user.home");
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return home + "\\Downloads";
        } else {
            return home + "/Downloads";  // macOS + Linux
        }
    }

}
