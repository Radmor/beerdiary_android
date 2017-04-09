package pl.poznan.put.cs.io.beerdiary;

import java.util.Date;

/**
 * klasa przechowująca notatkę
 */

/* TODO jak zwykle rozwiązać sprawę z datą utworzenia */

public class Note {
    private String title;
    private String body;
    private Date created; // data utworzenia notatki

    /** konstruktor domyślny
     * @param title           tytuł notatki
     * @param body            treść notatki
     */
    public Note(String title, String body){
        this.title = title;
        this.body = body;
        this.created = new Date();
    }

    /** metoda aktualizująca informację o notatce
     * @param title           tytuł notatki
     * @param body            treść notatki
     */
    public void update(String title, String body){
        this.title = title;
        this.body = body;
        this.created = new Date();
    }

    public String getTitle() {return title;}
    public String getBody() {return body;}
    public Date getCreated() {return created;}
    public void setTitle(String value) {title = value;}
    public void setBody(String value) {body = value;}
    public void setCreated(Date value) {created = value;}
}
