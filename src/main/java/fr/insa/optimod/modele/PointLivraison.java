package fr.insa.optimod.modele;

import java.util.ArrayList;

public class PointLivraison {

        private Noeud noeud;
        private Double g;         // coût réel
        private Double h;         // heuristique
        private PointLivraison parent;
        private Troncon antecedent;
        //private Integer etat;
        //private Boolean DejaVu;

        public PointLivraison(Noeud n, Double g, Double h, PointLivraison parent) {
            this.noeud = n;
            this.g = g;
            this.h = h;
            this.parent = parent;
            //this.DejaVu = DejaVu;
        }

        public Double obtenirCout() {
            return g + h;
        }

        public Noeud getNoeud() {
            return noeud;
        }

        public Double getG() {
            return g;
        }

        public double getH() {
            return h;
        }

        public PointLivraison getParent() {
            return parent;
        }

        public void setNoeud(Noeud noeud) {
            this.noeud = noeud;
        }

        public void setG(double g) {
            this.g = g;
        }

        public void setH(double h) {
            this.h = h;
        }

        public void setParent(PointLivraison parent) {
            this.parent = parent;
        }

        public Troncon getAntecedent() {
            return antecedent;
        }

        public void setG(Double g) {
            this.g = g;
        }

        public void setAntecedent(Troncon suivant) {
            this.antecedent = suivant;
        }

        public void setH(Double h) {
            this.h = h;
        }
}


