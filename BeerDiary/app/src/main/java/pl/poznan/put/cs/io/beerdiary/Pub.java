package pl.poznan.put.cs.io.beerdiary;

/**
 * Klasa przechowująca dane o pojedynczym pubie.
 * Przygotowana na podstawie UML z poprzedniego semestru, zgodna ze specyfikacją JSONa podawanego przez serwer.
 * design oraz atmosphere przyjmują tylko wartości z zakresu 0.0-1.0, co jest sprawdzane ze strony serwera.
 */

public class Pub {

    /**
     * Default constructor
     */
    public Pub() {
    }

    /**
     *
     */
    protected String name;

    /**
     *
     */
    protected String street;

    /**
     *
     */
    protected String city;

    /**
     *
     */
    protected Rating overall;

    /**
     *
     */
    protected float design;

    /**
     *
     */
    protected String designDescription;

    /**
     *
     */
    protected float atmosphere;

    /**
     *
     */
    protected String atmosphereDescription;


    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param value
     */
    public void setName(String value) {
        name = value;
    }

    /**
     * @return
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param value
     */
    public void setStreet(String value) {
        street = value;
    }

    /**
     * @return
     */
    public String getCity() {
        return city;
}

    /**
     * @param value
     */
    public void setCity(String value) {
        city = value;
    }

    /**
     * @return
     */
    public Rating getOverall() {
        return overall;
    }

    /**
     * @param value
     */
    public void setOverall(Rating value) {
        overall = value;
    }

    /**
     * @return
     */
    public float getDesign() {
        return design;
    }

    /**
     * @param value
     */
    public void setDesign(float value) {
        design = value;
    }

    /**
     * @return
     */
    public String getDesignDescription() {
        return designDescription;
    }

    /**
     * @param value
     */
    public void setDesignDescription(String value) {
        designDescription = value;
    }

    /**
     * @return
     */
    public float getAtmosphere() {
        return atmosphere;
    }

    /**
     * @param value
     */
    public void setAtmosphere(float value) {
        atmosphere = value;
    }

    /**
     * @return
     */
    public String getAtmosphereDescription() {
        return atmosphereDescription;
    }

    /**
     * @param value
     */
    public void setAtmosphereDescription(String value) {
        atmosphereDescription = value;
    }

}