package fr.insa.optimod.modele;

public class Item {
    private String titre;
    private String adresse;
    private String svgSrc;
    private String coloredTag;

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getSvgSrc() {
        return svgSrc;
    }

    public void setSvgSrc(String svgSrc) {
        this.svgSrc = svgSrc;
    }

    public String getColoredTag() {
        return coloredTag;
    }

    public void setColoredTag(String coloredTag) {
        this.coloredTag = coloredTag;
    }
}
