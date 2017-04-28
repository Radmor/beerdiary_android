package pl.poznan.put.cs.io.beerdiary;

/**
 * Klasa przechowujaca dane o pojedynczym browarze.
 * Przygotowana na podstawie UML z poprzedniego semestru, zgodna ze specyfikacja JSONa podawanego przez serwer.
 * design oraz atmosphere przyjmuja tylko wartosci z zakresu 0.0-1.0, co jest sprawdzane ze strony serwera.
 */

public class Brewery {
    private int id;
    private String name;
    private Rating overall;
    private String note;

    private Brewery() {
    }

    /**
     * Default constructor
     * @param name                      nazwa browaru
     * @param overall                   ogolna ocena browaru w skali 1-5
     * @param note                      opis browaru
     */

    public Brewery(int id, String name, Rating overall, String note) {
        this.id = id;
        this.name = name;
        this.overall = overall;
        this.note = note;
    }

    /**
     * @return  zwraca nazwe browaru
     */
    public String getName() {
        return name;
    }

    /**
     * @param value ustawia nazwe browaru
     */
    public void setName(String value) {
        name = value;
    }

    /**
     * @return  zwraca ocene ogólną
     */
    public Rating getOverall() {
        return overall;
    }

    /**
     * @param value ustawia ocene ogólną
     */
    public void setOverall(Rating value) {
        overall = value;
    }

    /**
     * @return  zwraca opis browaru
     */
    public String getNote() {
        return note;
    }

    /**
     * @param value ustawia opis browaru
     */
    public void setNote(String value) {
        note = value;
    }

    /** @return  zwraca id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id    ustawia id
     */
    public void setId(int id) {
        this.id = id;
    }
  
   /*** metoda aktualizująca dane o pubie
     * @param name                      nazwa browaru
     * @param overall                   ogolna ocena browaru w skali 1-5
     * @param note                      opis  browaru

     */

    public void update(String name, Rating overall, String note) {
        this.name = name;
        this.overall = overall;
        this.note = note;
    }
}