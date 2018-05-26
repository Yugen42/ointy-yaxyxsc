package us.yugen.yaxyxsc.entities;

import java.util.Objects;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
