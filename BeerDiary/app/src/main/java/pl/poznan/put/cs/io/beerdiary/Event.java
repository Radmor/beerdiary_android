package pl.poznan.put.cs.io.beerdiary;

import java.util.Date;

/* klasa przechowująca informację o wydarzeniu */

public class Event {
    private Date date;
    private String name;

    /** konstruktor domyślny
     * @param name          nazwa wydarzenia
     * @param date          data wydarzenia
     */
    public Event(String name, Date date){
        this.name = name;
        this.date = new Date();
    }

    /** metoda aktualizująca informacje o wydarzeniu
     * date             data wydarzenia
     * name             nazwa wydarzenia
     */
    public void update(String name, Date date){
        this.date = date;
        this.name = name;
    }

    public Date getDate() {return date;}
    public String getName() {return name;}
    public void setDate(Date value) {date = value;}
    public void setName(String value) {name = value;}
}
