package fr.insa.optimod.controleur;
import fr.insa.optimod.modele.*;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;


public class Controleur {


    public Carte initialiserCarte(String fichierPlan) {
    Carte carte = null;
        try {
            File xmlFile = new File(fichierPlan);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            ArrayList<Noeud> listeNoeuds = new ArrayList<>();
            ArrayList<Troncon> listeTroncons = new ArrayList<>();

            NodeList Noeuds = doc.getElementsByTagName("noeud");

            for (int i = 0; i < Noeuds.getLength(); i++) {
                Element e = (Element) Noeuds.item(i);

                long id = Long.parseLong(e.getAttribute("id"));
                double lat = Double.parseDouble(e.getAttribute("latitude"));
                double lon = Double.parseDouble(e.getAttribute("longitude"));

                listeNoeuds.add(new Noeud(id, lon, lat));
            }

            NodeList Troncons = doc.getElementsByTagName("troncon");

            for (int i = 0; i < Troncons.getLength(); i++) {
                Element e = (Element) Troncons.item(i);

                Long origine = Long.parseLong(e.getAttribute("origine"));
                Long destination = Long.parseLong(e.getAttribute("destination"));
                Double longueur = Double.parseDouble(e.getAttribute("longueur"));
                String nomRue = e.getAttribute("nomRue");

                listeTroncons.add(new Troncon(destination, origine, longueur, nomRue));
            }

            // Affichage test
            System.out.println("Noeuds lus : " + listeNoeuds.size());
            System.out.println("Tronçons lus : " + listeTroncons.size());
        carte = new Carte(listeNoeuds, listeTroncons);
        } catch (Exception e) {
            e.printStackTrace();
            return carte;
        }
    return carte;
    }




    public DemandeDeLivraions initialiserDemandeDeLivraions(String fichierDemande) {
        DemandeDeLivraions demande = null;
        /*
        try {
            File xmlFile = new File(fichierDemande);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Entrepot entrepot = null;
            ArrayList<Livraison> listeLivraison = new ArrayList<>();

            NodeList Noeuds = doc.getElementsByTagName("entrepot");

            for (int i = 0; i < Noeuds.getLength(); i++) {
                Element e = (Element) Noeuds.item(i);

                long id = Long.parseLong(e.getAttribute("id"));
                double lat = Double.parseDouble(e.getAttribute("latitude"));
                double lon = Double.parseDouble(e.getAttribute("longitude"));

                listeNoeuds.add(new Noeud(id, lon, lat));
            }

            NodeList Troncons = doc.getElementsByTagName("troncon");

            for (int i = 0; i < Troncons.getLength(); i++) {
                Element e = (Element) Troncons.item(i);

                Long origine = Long.parseLong(e.getAttribute("origine"));
                Long destination = Long.parseLong(e.getAttribute("destination"));
                Double longueur = Double.parseDouble(e.getAttribute("longueur"));
                String nomRue = e.getAttribute("nomRue");

                listeTroncons.add(new Troncon(destination, origine, longueur, nomRue));
            }

            // Affichage test
            System.out.println("Noeuds lus : " + listeNoeuds.size());
            System.out.println("Tronçons lus : " + listeTroncons.size());
            carte = new Carte(listeNoeuds, listeTroncons);
        } catch (Exception e) {
            e.printStackTrace();
            return carte;
        }
        */


        return demande;
    }






}
