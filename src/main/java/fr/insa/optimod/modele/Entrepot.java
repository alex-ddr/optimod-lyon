package fr.insa.optimod.modele;
import java.time.LocalTime;

public class Entrepot {
    protected int adresse;
    protected LocalTime heureDepart;


    public Entrepot(LocalTime heureDepart, int adresse) {
        this.heureDepart = heureDepart;
        this.adresse = adresse;
    }

    public int getAdresss() {
        return adresse;
    }

    public LocalTime getHeureDepart() {
        return heureDepart;
    }
}
