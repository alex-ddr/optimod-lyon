package fr.insa.optimod.modele;

public class ZoneCliquable {
    protected int livraisonId;
    protected boolean isEnlevement;
    protected double x1, y1, x2, y2;

    public ZoneCliquable(int livraisonId, boolean isEnlevement, double x, double y, int rayon) {
        this.livraisonId = livraisonId;
        this.isEnlevement = isEnlevement;
        this.x1 = x - rayon;
        this.y1 = y - rayon;
        this.x2 = x + rayon;
        this.y2 = y + rayon;
    }

    public boolean contient(double x, double y) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

    public int getLivraisonId() {
        return livraisonId;
    }

    public boolean isEnlevement() {
        return isEnlevement;
    }
}
