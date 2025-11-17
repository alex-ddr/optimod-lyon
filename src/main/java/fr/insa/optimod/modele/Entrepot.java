package fr.insa.optimod.modele;
import java.time.LocalTime;

public class Entrepot {
    protected Integer adresse;
    protected LocalTime heureDepart;


    public Entrepot(LocalTime heureDepart, Integer adresse) {
        this.heureDepart = heureDepart;
        this.adresse = adresse;
    }

    public Integer getAdresss() {
        return adresse;
    }

    public LocalTime getHeureDepart() {
        return heureDepart;
    }
}
