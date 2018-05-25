package us.yugen.yaxyxsc.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreas Hartmann
 */
public class ShoppingList {
    public User owner;
    public List<String> tags = new ArrayList<>();
    public List<String> items = new ArrayList<>();
}