package pl.poznan.put.cs.io.beerdiary;

/* klasa przechowująca informacje o stylu piwa */

public class Style {
    private String name;

    /** konstruktor domyślny
     * @param name             nazwa stylu
     */
    public Style(String name){
        this.name = name;
    }

    public String getName() {return name;}
    public void setName(String value) {name = value;}

    /** metoda aktualizująca informacje o stylu
     * @param name          nazwa stylu
     */
    public void update(String name) {this.name = name;}
}
