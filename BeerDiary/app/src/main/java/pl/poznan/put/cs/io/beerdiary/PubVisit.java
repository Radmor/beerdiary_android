package pl.poznan.put.cs.io.beerdiary;

import java.util.Date;

/* klasa przechowująca informację o wizycie w pubie */

public class PubVisit {
    private Date date;

    /** konstruktor domyślny
     * date             data wizyty w pubie
     */
    public PubVisit(Date date){
        this.date = date;
    }

    /** metoda aktualizująca informacje o wizycie w pubie
     * date             data wizyty w pubie
     */
    public void update(Date date){
        this.date = date;
    }

    public Date getDate() {return date;}
    public void setDate(Date value) {date = value;}
}
