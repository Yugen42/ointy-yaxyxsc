package us.yugen.yaxyxsc.entities;

/**
 * @author Andreas Hartmann
 */
public class Address {
    public double latitude;
    public double longitude;
    public int zip;
    public String city;
    public String street;
    public int houseNumber;

    public Address(double latitude, double longitude, int zip, String city, String street, int houseNumber) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.zip = zip;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
    }
}
