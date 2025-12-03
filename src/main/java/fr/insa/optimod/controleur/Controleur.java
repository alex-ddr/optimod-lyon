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

    protected Carte carte = null;
    protected ArrayList<Troncon> troncons = null;
    protected ArrayList<PointLivraison> chemin = null;
    protected ArrayList<PointLivraison> tspListe = null;

    protected DemandeDeLivraions demandeDeLivraions = null;

    public ArrayList<Troncon> getTronconsItineraire() {
        return troncons;
    }

    public ArrayList<PointLivraison> getPointsItineraire() {
        return tspListe;
    }

    public ArrayList<PointLivraison> getChemin() {return chemin;}


    public DemandeDeLivraions getDemandeDeLivraions() {
        return demandeDeLivraions;
    }


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
                Double tentative = courant.getG() + t.getLongueur()/4.1666666667;

                if (fait.contains(voisin))
                    continue;

                if (!score.containsKey(voisin) || tentative < score.get(voisin)) {

                    Double h = heuristique(noeudVoisin, adresseFin, carte);

                    PointLivraison suivant = new PointLivraison(noeudVoisin, tentative, h, courant);
                    suivant.setAntecedent(t);
                    score.put(voisin, tentative);
                    livrable.add(suivant);
                }

            }


        }

        return null;
    }








    public HashMap<Long, HashMap<Long, PointLivraison> > preparerPlanTournee(Carte carte, DemandeDeLivraions demande)
    {
        this.ajouterAdjacense(carte);
        List<Long> listeIds = new ArrayList<>();
        //ArrayList<Livraison> listeLivraions = demande.getListeLivraisons();
        listeIds.add(demande.getEntrepot().getAdresss());
        for(Livraison livraison : demande.getListeLivraisons()){
            listeIds.add(livraison.getAdresseEnlevement());
            listeIds.add(livraison.getAdresseLivraison());
        }

        //System.out.println("listeIds : " + listeIds.size());

        HashMap<Long,Integer> mapIdAIndex = new HashMap<>();
        HashMap<Integer,Long> mapIndexAId = new HashMap<>();

        for (int k = 0; k < listeIds.size(); k++) {
            mapIdAIndex.put(listeIds.get(k), k);
            mapIndexAId.put(k, listeIds.get(k));
        }
        long s = (1L << (listeIds.size() - 1)) - 1L;
        double[][] cout = new double[listeIds.size()][listeIds.size()];
        HashMap<Long, HashMap<Long,PointLivraison> > tournee = new HashMap<>();


        HashMap<Long, PointLivraison> courtCheminEntrepot = new HashMap<>();
        PointLivraison astarEntrepotL, astarEntrepotE;
        Noeud entrepot = carte.obtenirNoeud(demande.getEntrepot().getAdresss());
        for (Livraison livraison2 : demande.getListeLivraisons()) {


                Integer ent = mapIdAIndex.get(entrepot.getId());
                Integer liv = mapIdAIndex.get(livraison2.getAdresseLivraison());
                Integer enl = mapIdAIndex.get(livraison2.getAdresseEnlevement());

                astarEntrepotL = astar(carte, entrepot, carte.obtenirNoeud(livraison2.getAdresseLivraison()));
                astarEntrepotE = astar(carte, entrepot, carte.obtenirNoeud(livraison2.getAdresseEnlevement()));

                cout[ent][liv] = astarEntrepotL.getG() + livraison2.getDureeLivraison();
                cout[ent][enl] = astarEntrepotE.getG() + livraison2.getDureeEnlevement();

                astarEntrepotE.setG(cout[ent][enl]);
                astarEntrepotL.setG(cout[ent][liv]);

                courtCheminEntrepot.put(livraison2.getAdresseLivraison(), astarEntrepotL);
                courtCheminEntrepot.put(livraison2.getAdresseEnlevement(), astarEntrepotE);




        }
        tournee.put(entrepot.getId(), courtCheminEntrepot);

        for (Livraison livraison : demande.getListeLivraisons()) {

            HashMap<Long, PointLivraison> courtCheminL = new HashMap<>();
            HashMap<Long, PointLivraison> courtCheminE = new HashMap<>();

            Noeud L = carte.obtenirNoeud(livraison.getAdresseLivraison());
            Noeud E= carte.obtenirNoeud(livraison.getAdresseEnlevement());
            Integer idL = mapIdAIndex.get(L.getId());
            Integer idE = mapIdAIndex.get(E.getId());
            PointLivraison astarL, astarE =  astar(carte, L, E), astarLEnlevement, astarLLivraison, astarEEnlevement, astarELivraison;
            astarL = astarE;
            cout[idL][idE] = astarE.getG() +  livraison.getDureeEnlevement();
            cout[idE][idL] = astarL.getG() + livraison.getDureeLivraison();

            astarE.setG(cout[idL][idE]);
            astarL.setG(cout[idE][idL]);
            //courtCheminE.put(livraison.getAdresseLivraison(), new ArrayList<>());
            courtCheminL.put(E.getId(), astarE);
            courtCheminE.put(L.getId(), astarL);


            for (Livraison livraison2 : demande.getListeLivraisons()) {
                if (livraison!=livraison2) {

                Integer liv = mapIdAIndex.get(livraison2.getAdresseLivraison());
                Integer enl = mapIdAIndex.get(livraison2.getAdresseEnlevement());

                astarLEnlevement = astar(carte, L, carte.obtenirNoeud(livraison2.getAdresseEnlevement()));
                astarLLivraison = astar(carte, L, carte.obtenirNoeud(livraison2.getAdresseLivraison()));

                astarEEnlevement = astar(carte, E, carte.obtenirNoeud(livraison2.getAdresseEnlevement()));
                astarELivraison = astar(carte, E, carte.obtenirNoeud(livraison2.getAdresseLivraison()));

                cout[idL][liv] = astarLLivraison.getG() + livraison2.getDureeLivraison();
                cout[idE][liv] = astarELivraison.getG() +  livraison2.getDureeLivraison();
                cout[idL][enl] = astarLEnlevement.getG() + livraison2.getDureeEnlevement();
                cout[idE][enl] = astarEEnlevement.getG() +  livraison2.getDureeEnlevement();

                astarLLivraison.setG(cout[idL][liv]);
                astarELivraison.setG(cout[idE][liv]);
                astarLEnlevement.setG(cout[idL][enl]);
                astarLLivraison.setG(cout[idE][enl]);

                courtCheminL.put(livraison2.getAdresseEnlevement(),  astarLEnlevement);
                courtCheminL.put(livraison2.getAdresseLivraison(), astarLLivraison);

                courtCheminE.put(livraison2.getAdresseEnlevement(), astarEEnlevement);
                courtCheminE.put(livraison2.getAdresseLivraison(), astarELivraison);

                }



            }

            Integer ent = mapIdAIndex.get(entrepot.getId());

            astarLEnlevement = astar(carte, L, carte.obtenirNoeud(entrepot.getId()));
            astarEEnlevement = astar(carte, E, carte.obtenirNoeud(entrepot.getId()));

            cout[idL][ent] = astarLEnlevement.getG();
            cout[idE][ent] = astarEEnlevement.getG();

            astarLEnlevement.setG(cout[idL][ent]);
            astarEEnlevement.setG(cout[idE][ent]);

            courtCheminL.put(entrepot.getId(),  astarLEnlevement);
            courtCheminE.put(entrepot.getId(), astarEEnlevement);


            tournee.put(L.getId(), courtCheminL);
            tournee.put(E.getId(), courtCheminE);
            }

        TSP tsp = new TSP();


        PointLivraison l = tsp.computeD(0, s, listeIds.size(), cout, carte, mapIndexAId);
        //System.out.println(l.getG());
        //System.out.println(l.getParent().getNoeud().getId());

        ArrayList<Troncon> troncons = new ArrayList<>();
        ArrayList<PointLivraison> chemin = new ArrayList<>();
        ArrayList<PointLivraison> tspListe = new ArrayList<>();

        PointLivraison courant = l;
        PointLivraison t = l;
        PointLivraison prochain = courant.getParent();

        while (t != null) {
            tspListe.add(t);
            t = t.getParent();

        }


        PointLivraison astar;


        while (courant != null && prochain != null) {
            //System.out.println(" ----- " + courant.getNoeud().getId());
            //System.out.println(" ----- " + prochain.getNoeud().getId());
            astar = tournee.get(prochain.getNoeud().getId()).get(courant.getNoeud().getId());
            ArrayList<Troncon> listeTampon = new ArrayList<>();
            while (astar != null)
            {

                chemin.add(astar);
                if (astar.getAntecedent() != null)
                {
                    //chemin.add(astar);
                    listeTampon.add(astar.getAntecedent());
                    //System.out.println("ID - " + astar.getNoeud().getId());
//                    System.out.println("NOM - " + astar.getAntecedent().getNomRue());
//                    System.out.println("DEST - " + astar.getAntecedent().getDestination());
//                    System.out.println("ORI - " + astar.getAntecedent().getOrigine());
                }

                astar = astar.getParent();

            }
            //Collections.reverse(listeTampon);
            troncons.addAll(listeTampon);


            //System.out.println(courant.getNoeud().getId());
            courant = courant.getParent();
            prochain = courant.getParent();


        }

        chemin.add(courant);
        /*
        for (Troncon troncon : troncons)
        {
            System.out.println("NOM - " + troncon.getNomRue());
            System.out.println("DEST - " + troncon.getDestination());
            System.out.println("ORI - " + troncon.getOrigine());
        }
        */

       /* System.out.println("Chemin TSP :");

        for (PointLivraison p : chemin) {
            if (p != null) {
                System.out.println(" - " + p.getNoeud().getId());
                System.out.println(" - " + p.getSuivant().getNomRue());
                System.out.println(" - " + p.getSuivant().getDestination());
                System.out.println(" - " + p.getSuivant().getOrigine());


            }
        }
        System.out.println((l.getParent()));
        */

        this.troncons = troncons;
        this.chemin = chemin;
        this.tspListe = tspListe;

        return null;
    }

    public Carte getCarte() {
        return carte;
    }

    public void initialiserCarte(String fichierPlan) {
        try {
            File xmlFile = new File(fichierPlan);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            ArrayList<Noeud> listeNoeuds = new ArrayList<>();
            HashMap<Long, Noeud> mapNoeuds = new HashMap<>();
            ArrayList<Troncon> listeTroncons = new ArrayList<>();
            Double minLat = null;
            Double minLong = null;
            Double maxLat = null;
            Double maxLong = null;

            NodeList Noeuds = doc.getElementsByTagName("noeud");

            for (int i = 0; i < Noeuds.getLength(); i++) {
                Element e = (Element) Noeuds.item(i);

                Long id = Long.parseLong(e.getAttribute("id"));
                Double lat = Double.parseDouble(e.getAttribute("latitude"));
                Double lon = Double.parseDouble(e.getAttribute("longitude"));

                listeNoeuds.add(new Noeud(id, lon, lat));
                mapNoeuds.put(id, new Noeud(id, lon, lat));
                //System.out.println(id + " " + lon + " " + lat);
                if (minLat == null || lat < minLat) {
                    minLat = lat.doubleValue();
                }
                if (maxLat == null || lat > maxLat) {
                    maxLat = lat.doubleValue();
                }
                if (minLong == null || lon < minLong) {
                    minLong = lon.doubleValue();
                }
                if (maxLong == null || lon > maxLong) {
                    maxLong = lon.doubleValue();
                }

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


            /*
            System.out.println("Noeuds lus : " + listeNoeuds.size());
            System.out.println("Tronçons lus : " + listeTroncons.size());
            System.out.println("MinLat : " + minLat + " MaxLat : " + maxLat);
            System.out.println("MinLong : " + minLong + " MaxLong : " + maxLong);
            */

        carte = new Carte(listeNoeuds, listeTroncons, mapNoeuds, minLat, minLong, maxLat, maxLong);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void initialiserDemandeDeLivraions(String fichierDemande) {

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

            //System.out.println("Heure départ = " + heureDepart);


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


            //System.out.println("Livraisons lus : " + listeLivraison.size());

            demandeDeLivraions = new DemandeDeLivraions(entrepot, listeLivraison);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






}
