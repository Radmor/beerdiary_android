package pl.poznan.put.cs.io.beerdiary;

import java.util.Date;

/**
 * klasa przechowująca zdjęcie piwa
 */

/* TODO rzeczywista obsługa zdjęć, być może czas utworzenia rozwiązać inaczej*/

public class Photo {
    // private Image image; // zdjęcie
    private Date created; // data zrobienia zdjęcia

    /** konstruktor domyślny, na razie bez parametrów
     */
    public Photo(/* Image image */){
        //this.image = name;
        this.created = new Date();
    }
    //public Image getImage() {return image;}
    //public void setImage(Image value) {image = value;}
    public Date getCreated() {return created;}
    public void setCreated(Date value) {created = value;}

    /** metoda aktualizująca informacje o zdjęciu
     * @param created       data utworzenia
     */
    public void update(/*Image image, */ Date created) {
        //this.image = image;
        this.created = created;
    }
}
