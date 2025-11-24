package fr.insa.optimod.modele;
import java.time.LocalTime;

public class Entrepot {
    protected Long adresse;
    protected LocalTime heureDepart;


    public Entrepot(LocalTime heureDepart, Long adresse) {
        this.heureDepart = heureDepart;
        this.adresse = adresse;
    }

    public Long getAdresss() {
        return adresse;
    }

    public LocalTime getHeureDepart() {
        return heureDepart;
    }
}
