package us.yugen.yaxyxsc.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreas Hartmann
 */
public class ShoppingList {
    private static int nextID = 1;
    public User owner;
    public BigDecimal bounty = new BigDecimal("3.50");
    public String tag;
    public List<String> items = new ArrayList<>();
    public int id = nextID++;

    public ShoppingList(User owner, String tag, List<String> items) {
        this.owner = owner;
        this.tag = tag;
        this.items = items;
    }
}