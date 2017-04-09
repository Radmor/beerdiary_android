package pl.poznan.put.cs.io.beerdiary;

/**
 * klasa przechowująca dane o pojedynczej recenzji piwa
 */

import java.util.Date;

/** TODO niektóre tak samo nazywające się pola w klasie Beer są innego typu
 * jak to faktycznie powinno być? */

public class RateBeerReview {
    private String body;
    private int aroma;
    private int appearance;
    private int taste;
    private int palate;
    private int overall;
    private Date created; // data utworzenia recenzji

    /** konstruktor domyślny
     * @param body                  treść recenzji
     * @param aroma                 ocena aromat piwa
     * @param appearance            ocena wyglądu piwa
     * @param taste                 ocena smaku piwa
     * @param palate                TODO przetłumaczyć
     * @param overall               ogólna ocena piwa
     */

    public RateBeerReview(String body, int aroma, int appearance, int taste, int palate, int overall){
        this.body = body;
        this.aroma = aroma;
        this.appearance = appearance;
        this.taste = taste;
        this.palate = palate;
        this.overall = overall;
        this.created = new Date(); // TODO być może to zmienić
    }

    public String getBody() {return body;}
    public int getAroma() {return aroma;}
    public int getAppearance() {return appearance;}
    public int getTaste() {return taste;}
    public int getPalate() {return palate;}
    public int getOverall() {return overall;}
    public Date getCreated() {return created;}
    public void setBody(String value) {body = value;}
    public void setAroma(int value) {aroma = value;}
    public void setAppearance(int value) {appearance = value;}
    public void setTaste(int value) {taste = value;}
    public void setPalate(int value) {palate = value;}
    public void setOverall(int value) {overall = value;}
    public void setDate(Date value) {created = value;}

    /** metoda aktualizująca recenzję
     * @param body                  treść recenzji
     * @param aroma                 ocena aromat piwa
     * @param appearance            ocena wyglądu piwa
     * @param taste                 ocena smaku piwa
     * @param palate                TODO przetłumaczyć
     * @param overall               ogólna ocena piwa
     */

    public void update(String body, int aroma, int appearance, int taste, int palate, int overall){
        this.body = body;
        this.aroma = aroma;
        this.appearance = appearance;
        this.taste = taste;
        this.palate = palate;
        this.overall = overall;
        this.created = new Date(); // TODO być może to zmienić
    }
}
