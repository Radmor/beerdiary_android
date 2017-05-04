package pl.poznan.put.cs.io.beerdiary;

/* klasa przechowująca informacje o stylu piwa */

import java.io.Serializable;

public class Style implements Serializable {
    private int id;
    private String name;

    /** konstruktor domyślny
     * @param id               id na serwerze
     * @param name             nazwa stylu
     */
    public Style(int id, String name){
        this.id = id;
        this.name = name;
    }

    public String getName() {return name;}
    public void setName(String value) {name = value;}

    public int getId() { return id; }

    /** metoda aktualizująca informacje o stylu
     * @param name          nazwa stylu
     */
    public void update(String name) {this.name = name;}
}
