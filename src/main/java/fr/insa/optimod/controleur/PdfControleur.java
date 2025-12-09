package fr.insa.optimod.controleur;

import fr.insa.optimod.modele.Noeud;
import fr.insa.optimod.modele.PointLivraison;
import fr.insa.optimod.modele.Troncon;
import fr.insa.optimod.vue.Interface;
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
import java.nio.file.Files;
import java.util.List;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PdfControleur {

    private final String name = "LivreurDay.pdf";

    private Interface interfaceUtilisateur;

    private FileChooser explorateur = new FileChooser();

    public void setInterface(Interface interfaceUtilisateur) {
        this.interfaceUtilisateur = interfaceUtilisateur;
    }


    public void extrairePdf_2(ArrayList<Troncon> troncons, ArrayList<PointLivraison> chemin, String canvasBase64) throws IOException {
        try {
            class Instruction {
                public String texte;
                public String SVGPath;
                Instruction(String texte, String SVGPath) {
                    this.texte = texte;
                    this.SVGPath = SVGPath;
                }

                public String getTexte() {
                    return texte;
                }
                public String getSVGPath() {
                    return SVGPath;
                }
            }

            List<Instruction> listeInstructions = new ArrayList<>();

            String text;
            String SVGPath;
            SVGPath = "M8 17V11H12V17H17V9H20L10 0L0 9H3V17H8Z"; // SVG pour entrepot
            text = "Partir de l'entrepot vers le " + donnerDirectionDepart(chemin.get(0).getNoeud(), chemin.get(1).getNoeud()) + " sur la rue " + troncons.getFirst().getNomRue();
            listeInstructions.add(new Instruction(text, SVGPath));

            if(chemin.size() < 3) {return;}  // ca va de l'entrepot à l'entrepot

            for(Troncon troncon: troncons) {
                System.out.println(troncon.getNomRue());
            }
            System.out.println(chemin.size());
            System.out.println(troncons.size());

            for(int i = 0; i < chemin.size()-3; i++) {
                text = ecriteText(chemin.get(i).getNoeud(), chemin.get(i+1).getNoeud(), chemin.get(i+2).getNoeud(), troncons.get(i+1).getNomRue());
                int direction = donnerDirection(chemin.get(i).getNoeud(), chemin.get(i+1).getNoeud(), chemin.get(i+2).getNoeud());
                if (direction == 1) {
                    SVGPath = "M0 11.5C0 7.36 3.36 4 7.5 4H10V0L17 6L10 12V8H7.5C5.57 8 4 9.57 4 11.5V19H0V11.5Z"; // SVG pour tourner à droite
                } else if (direction == 0) {
                    SVGPath = "M17 19H13V11.5C13 9.57 11.43 8 9.5 8H7V12L0 6L7 0V4H9.5C13.64 4 17 7.36 17 11.5V19Z"; // SVG pour tourner à gauche
                } else if (direction == -1) {
                    SVGPath = "M4 9.5L4 7L-3.0598e-07 7L6 -2.62268e-07L12 7L8 7L8 9.5L8 19L4 19L4 9.5Z"; // SVG pour aller tout droit
                }
                else if (direction == -2) {
                    SVGPath = "M19 7.5V15H15V7.5C15 5.57 13.43 4 11.5 4C9.57 4 8 5.57 8 7.5V10H12L6 17L0 10H4V7.5C4 3.36 7.36 0 11.5 0C15.64 0 19 3.36 19 7.5Z"; // SVG demi-tour
                }
                listeInstructions.add(new Instruction(text, SVGPath));
            }

            text = "Retour à l'entrepot : " + ecriteText(chemin.get(chemin.size()-3).getNoeud(),chemin.get(chemin.size()-2).getNoeud(), chemin.getLast().getNoeud(),troncons.getLast().getNomRue());
            SVGPath = "M8 17V11H12V17H17V9H20L10 0L0 9H3V17H8Z"; // SVG pour entrepot
            listeInstructions.add(new Instruction(text, SVGPath));

            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode("HTML");

            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);

            Context context = new Context();
            context.setVariable("dateDuJour", java.time.LocalDate.now().getDayOfMonth() + "/" + java.time.LocalDate.now().getMonthValue() + "/" + java.time.LocalDate.now().getYear());
            context.setVariable("instructions", listeInstructions);
            context.setVariable("canvasBase64", canvasBase64);

            byte[] logoBytes;
            try (java.io.InputStream logoStream = PdfControleur.class.getResourceAsStream("/img/logo.png")) {
                if (logoStream == null) {
                    throw new IOException("Resource not found: /img/logo.png");
                }
                logoBytes = logoStream.readAllBytes();
            }
            String logoBase64 = java.util.Base64.getEncoder().encodeToString(logoBytes);
            context.setVariable("logoBase64", logoBase64);

            String htmlContent = templateEngine.process("/templates/template_pdf", context);
            String htmlContent = templateEngine.process("templates/template_pdf", context);

            explorateur.setTitle("Sauvegarder le PDF");
            explorateur.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));
            explorateur.setInitialFileName(name);
            File fichier = explorateur.showSaveDialog(this.interfaceUtilisateur.getFenetrePrincipale());

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
        else if (direction == -1){text += "Aller tout droit";}
        else if (direction == -2){text += "Demi-tour";}
        text += " vers la rue " + rue;
        return text;
    }

    // Indique si le passage du segment [n1, n2] au [n2, n3] nécéssite de tourner à droite (1), à gauche (0), tout droit (-1) ou demi tour (-2)
    public int donnerDirection(Noeud n1, Noeud n2, Noeud n3){
        double lat1 = n2.getLatitude()-n1.getLatitude();
        double lat2 = n3.getLatitude()-n2.getLatitude();

        double lon1 = n2.getLongitude()-n1.getLongitude();
        double lon2 = n3.getLongitude()-n2.getLongitude();

        double dir1 = lat2 * lon1 - lat1 * lon2;

        double sens   = lon1 * lon2 + lat1 * lat2;

        if (dir1 > 0.0000002)
        {
            //System.out.println("gauche");
            return 0;
        }
        else if (dir1 < -0.0000002)
        {
           //System.out.println("droite");
            return 1;
        }



        else if ( sens < 0 )
        {
            //System.out.println("Tout droit");
            return -2;
        }
        //System.out.println("Tout droit");
        return -1;

    }

    public String donnerDirectionDepart(Noeud n1, Noeud n2){
        double lat1 = n2.getLatitude()-n1.getLatitude();
        double lon1 = n2.getLongitude()-n1.getLongitude();
        String retour = "";
        boolean b = false;
        if (lat1 > 0.0001)
        {
            retour = retour + "Nord";
            b = true;
        }
        else if (lat1 < -0.0001)
        {
            retour = retour + "Sud";
            b = true;
        }

        if (lon1 > 0.0001)
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
        else if (lon1 < -0.0001)
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

}
