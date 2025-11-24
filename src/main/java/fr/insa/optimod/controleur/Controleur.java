package fr.insa.optimod.controleur;
import fr.insa.optimod.modele.*;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Controleur {


    protected Double heuristique(Noeud na, Noeud nb, Carte carte) {

        double dx = na.getLongitude() - nb.getLongitude();
        double dy = na.getLatitude() - nb.getLatitude();

        return Math.sqrt(dx*dx + dy*dy);
    }

    public void ajouterAdjacense(Carte carte)
    {
        for(Troncon troncon: carte.getListeTroncon())
        {
            carte.obtenirNoeud(troncon.getOrigine()).ajouterTroncon(troncon);
            carte.obtenirNoeud(troncon.getDestination()).ajouterTroncon(troncon);
        }
        return;

    }

    public PointLivraison astar(Carte carte, Noeud adresseDebut, Noeud adresseFin) {
        PriorityQueue<PointLivraison> livrable = new PriorityQueue<>(Comparator.comparingDouble(PointLivraison::obtenirCout));
        HashMap<Long, Double> score = new HashMap<>();
        HashSet<Long> fait = new HashSet<>();
        PointLivraison debut = new PointLivraison(adresseDebut, 0.0, heuristique(adresseDebut, adresseFin, carte), null);

        livrable.add(debut);
        score.put(adresseDebut.getId(), 0.0);

        while (!livrable.isEmpty()) {
            PointLivraison courant = livrable.poll();

            //adjacense.put(courant.getNoeud(), courant.getNoeud().getAdjacense());

            if (courant.getNoeud().getId().equals(adresseFin.getId()))
                return courant; //demande

           /* if (courant.getDejaVu())
                continue;
            */
            fait.add(courant.getNoeud().getId());

            for (Troncon t : courant.getNoeud().getAdjacense()) {
                Long voisin;

                if (t.getDestination().equals(courant.getNoeud().getId())) {
                    voisin = t.getOrigine();
                }

                else {
                    voisin = t.getDestination();
                }

                Noeud noeudVoisin = carte.obtenirNoeud(voisin);
                Double tentative = courant.getG() + t.getLongueur();

                if (fait.contains(voisin))
                    continue;

                if (!score.containsKey(voisin) || tentative < score.get(voisin)) {

                    Double h = heuristique(noeudVoisin, adresseFin, carte);

                    PointLivraison suivant = new PointLivraison(noeudVoisin, tentative, h, courant);

                    score.put(voisin, tentative);
                    livrable.add(suivant);
                }

            }


        }

        return null;
    }

    public HashMap<Long, ArrayList<PointLivraison> > preparerPlanTournee(Carte carte, DemandeDeLivraions demande)
    {
        this.ajouterAdjacense(carte);
        HashMap<Long, ArrayList<PointLivraison> > tournee = new HashMap<>();
        for (Livraison livraison : demande.getListeLivraisons()) {

            ArrayList<PointLivraison> courtCheminL = new ArrayList<>();
            ArrayList<PointLivraison> courtCheminE = new ArrayList<>();

            Noeud L = carte.obtenirNoeud(livraison.getAdresseLivraison());
            Noeud E= carte.obtenirNoeud(livraison.getAdresseEnlevement());

            courtCheminL.add(astar(carte, L, E));
            courtCheminE.add(astar(carte, E, L));


            for (Livraison livraison2 : demande.getListeLivraisons()) {
                if (livraison!=livraison2) {

                courtCheminL.add(astar(carte, L, carte.obtenirNoeud(livraison2.getAdresseEnlevement())));
                courtCheminL.add(astar(carte, L, carte.obtenirNoeud(livraison2.getAdresseLivraison())));

                courtCheminE.add(astar(carte, E, carte.obtenirNoeud(livraison2.getAdresseEnlevement())));
                courtCheminE.add(astar(carte, E, carte.obtenirNoeud(livraison2.getAdresseLivraison())));

                }



            }
            tournee.put(L.getId(), courtCheminL);
            tournee.put(E.getId(), courtCheminE);
            }

        return tournee;
    }

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

                Long id = Long.parseLong(e.getAttribute("id"));
                Double lat = Double.parseDouble(e.getAttribute("latitude"));
                Double lon = Double.parseDouble(e.getAttribute("longitude"));

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

        try {
            File xmlFile = new File(fichierDemande);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Entrepot entrepot = null;
            ArrayList<Livraison> listeLivraison = new ArrayList<>();

            NodeList entrepot1 = doc.getElementsByTagName("entrepot");

            Element e = (Element) entrepot1.item(0);
            Long adresse = Long.parseLong(e.getAttribute("adresse"));

            String depart = e.getAttribute("heureDepart");

            DateTimeFormatter f = DateTimeFormatter.ofPattern("H:m:s");
            LocalTime heureDepart = LocalTime.parse(depart, f);

            System.out.println("Heure départ = " + heureDepart);


            entrepot = new Entrepot(heureDepart, adresse);

            NodeList Livraisons = doc.getElementsByTagName("livraison");

            for (int i = 0; i < Livraisons.getLength(); i++) {
                e = (Element) Livraisons.item(i);

                Long adresseEnlevement = Long.parseLong(e.getAttribute("adresseEnlevement"));
                Long adresseLivraison = Long.parseLong(e.getAttribute("adresseLivraison"));
                Long dureeEnlevement = Long.parseLong(e.getAttribute("dureeEnlevement"));
                Long dureeLivraison = Long.parseLong(e.getAttribute("dureeLivraison"));

                listeLivraison.add(new Livraison(adresseEnlevement, adresseLivraison, dureeLivraison, dureeEnlevement));
            }

            // Affichage test
            System.out.println("Livraisons lus : " + listeLivraison.size());

            demande = new DemandeDeLivraions(entrepot, listeLivraison);
        } catch (Exception e) {
            e.printStackTrace();
            return demande;
        }


        return demande;
    }






}
