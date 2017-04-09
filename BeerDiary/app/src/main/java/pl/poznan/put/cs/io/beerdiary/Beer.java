package pl.poznan.put.cs.io.beerdiary;

/* klasa przechowująca dane o piwie */

public class Beer {
    private String name;
    private float bitterness;
    private float foam;
    private Rating overall;
    private String foamDescription;
    private String bitternessDescription;
    private int IBU;
    private String color;
    private float aroma;
    private String aromaDescription;
    private float taste;
    private String tasteDescription;
    private float palate;
    private String palateDescription;

    /** konstruktor domyślny
     * @param name                      nazwa piwa
     * @param bitterness                stopień gorzkości
     * @param foam                      stopień pienistości
     * @param foamDescription           opis piany
     * @param overall                   ogólna ocena piwa w skali 1-5
     * @param bitternessDescription     opis gorszkości
     * @param IBU                       ocena według International Bittering Unit
     * @param color                     kolor piwa
     * @param aroma                     ocena aromat piwa
     * @param aromaDescription          opis aromatu
     * @param taste                     ocena smaku piwa
     * @param tasteDescription          opis smaku
     * TODO jak przetłumaczyć 'palate'?
     */

    public Beer(String name, float bitterness, float foam, Rating overall, String foamDescription, String bitternessDescription,
                int IBU, String color, float aroma, String aromaDescription, float taste,
                String tasteDescription, float palate, String palateDescription){
        this.name = name;
        this.bitterness = bitterness;
        this.foam = foam;
        this.overall = overall;
        this.foamDescription = foamDescription;
        this.bitternessDescription = bitternessDescription;
        this.IBU = IBU;
        this.color = color;
        this.aroma = aroma;
        this.aromaDescription = aromaDescription;
        this.taste = taste;
        this.tasteDescription = tasteDescription;
        this.palate = palate;
        this.palateDescription = palateDescription;
    }

    public String getName() {return name;}
    public float getBitterness() {return bitterness;}
    public float getFoam() {return foam;}
    public Rating getOverall() {return overall;}
    public String getFoamDescription() {return foamDescription;}
    public String getBitternessDescription() {return bitternessDescription;}
    public int getIBU() {return IBU;}
    public String getColor() {return color;}
    public float getAroma() {return aroma;}
    public String getAromaDescription() {return aromaDescription;}
    public float getTaste() {return taste;}
    public String getTasteDescription() {return tasteDescription;}
    public float getPalate() {return palate;}
    public String getPalateDescription() {return palateDescription;}

    public void setName(String value) {name = value;}
    public void setBitterness(float value) {bitterness = value;}
    public void setFoam(float value) {foam = value;}
    public void setOverall(Rating value) {overall = value;}
    public void setFoamDescription(String value) {foamDescription = value;}
    public void setBitternessDescription(String value) {bitternessDescription = value;}
    public void setIBU(int value) {IBU = value;}
    public void setColor(String value) {color = value;}
    public void setAroma(float value) {aroma = value;}
    public void setAromaDescription(String value) {aromaDescription = value;}
    public void setTaste(float value) {taste = value;}
    public void setTasteDescription(String value) {tasteDescription = value;}
    public void setPalate(float value) {palate = value;}
    public void setPalateDescription(String value) {palateDescription = value;}

    /** metoda aktualizująca informacje o piwie
     * @param name                      nazwa piwa
     * @param bitterness                stopień gorzkości
     * @param foam                      stopień pienistości
     * @param foamDescription           opis piany
     * @param overall                   ogólna ocena piwa w skali 1-5
     * @param bitternessDescription     opis gorszkości
     * @param IBU                       ocena według International Bittering Unit
     * @param color                     kolor piwa
     * @param aroma                     ocena aromat piwa
     * @param aromaDescription          opis aromatu
     * @param taste                     ocena smaku piwa
     * @param tasteDescription          opis smaku
     * TODO jak przetłumaczyć 'palate'?
     */

    public void update(String name, float bitterness, float foam, Rating overall, String foamDescription, String bitternessDescription,
                int IBU, String color, float aroma, String aromaDescription, float taste,
                String tasteDescription, float palate, String palateDescription){
        this.name = name;
        this.bitterness = bitterness;
        this.foam = foam;
        this.overall = overall;
        this.foamDescription = foamDescription;
        this.bitternessDescription = bitternessDescription;
        this.IBU = IBU;
        this.color = color;
        this.aroma = aroma;
        this.aromaDescription = aromaDescription;
        this.taste = taste;
        this.tasteDescription = tasteDescription;
        this.palate = palate;
        this.palateDescription = palateDescription;
    }
}
