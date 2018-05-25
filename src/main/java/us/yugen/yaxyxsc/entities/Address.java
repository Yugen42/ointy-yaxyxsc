package us.yugen.yaxyxsc.entities;

/**
 * @author Andreas Hartmann
 */
public class Address {
    public int x;
    public int y;
    public int zip;
    public String city;
    public String street;
    public int houseNumber;

    public Address(int x, int y, int zip, String city, String street, int houseNumber) {
        this.x = x;
        this.y = y;
        this.zip = zip;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
    }
}
