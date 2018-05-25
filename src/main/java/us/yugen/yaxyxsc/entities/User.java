package us.yugen.yaxyxsc.entities;

/**
 * @author Andreas Hartmann
 */
public class User {
    public int id;
    public String firstName;
    public String lastName;
    public Address address;

    public User(int id, String firstName, String lastName, Address address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }
}
