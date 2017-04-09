package pl.poznan.put.cs.io.beerdiary;

/**
 * Klasa przechowujaca dane o pojedynczym pubie.
 * Przygotowana na podstawie UML z poprzedniego semestru, zgodna ze specyfikacja JSONa podawanego przez serwer.
 * design oraz atmosphere przyjmuja tylko wartosci z zakresu 0.0-1.0, co jest sprawdzane ze strony serwera.
 */

public class Pub {
    private int id;
    private String name;
    private String street;
    private String city;
    private Rating overall;
    private float design;
    private String designDescription;
    private float atmosphere;
    private String atmosphereDescription;

    private Pub() {
    }

    /**
     * Default constructor
     * @param name                      nazwa pubu
     * @param street                    adres pubu
     * @param city                      miasto pubu
     * @param overall                   ogolna ocena pubu w skali 1-5
     * @param design                    ocena wystroju pubu
     * @param designDescription         opis wystroju pubu
     * @param atmosphere                ocena atmosfery pubu
     * @param atmosphereDescription     opis atmosfery pubu
     */
    public Pub(int id, String name, String street, String city, Rating overall, float design, String designDescription, float atmosphere, String atmosphereDescription) {
        this.id = id;
        this.name = name;
        this.street = street;
        this.city = city;
        this.overall = overall;
        this.design = design;
        this.designDescription = designDescription;
        this.atmosphere = atmosphere;
        this.atmosphereDescription = atmosphereDescription;
    }

    /**
     * @return  zwraca nazwe pubu
     */
    public String getName() {
        return name;
    }

    /**
     * @param value ustawia nazwe pubu
     */
    public void setName(String value) {
        name = value;
    }

    /**
     * @return  zwraca adres pubu
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param value ustawia adres pubu
     */
    public void setStreet(String value) {
        street = value;
    }

    /**
     * @return  zwraca miasto
     */
    public String getCity() {
        return city;
}

    /**
     * @param value ustawia miasto
     */
    public void setCity(String value) {
        city = value;
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
     * @return  zwraca ocene wystroju
     */
    public float getDesign() {
        return design;
    }

    /**
     * @param value ustawia ocene wystroju
     */
    public void setDesign(float value) {
        design = value;
    }

    /**
     * @return  zwraca opis wystroju
     */
    public String getDesignDescription() {
        return designDescription;
    }

    /**
     * @param value ustawia opis wystroju
     */
    public void setDesignDescription(String value) {
        designDescription = value;
    }

    /**
     * @return  zwraca ocene atmosfery
     */
    public float getAtmosphere() {
        return atmosphere;
    }

    /**
     * @param value ustawia ocene atmosfery
     */
    public void setAtmosphere(float value) {
        atmosphere = value;
    }

    /**
     * @return  zwraca opis oceny
     */
    public String getAtmosphereDescription() {
        return atmosphereDescription;
    }

    /**
     * @param value ustawia opis oceny
     */
    public void setAtmosphereDescription(String value) {
        atmosphereDescription = value;
    }

    /**
     * @return  zwraca id
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
}