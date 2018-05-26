package us.yugen.yaxyxsc;

import us.yugen.yaxyxsc.entities.ShoppingList;
import us.yugen.yaxyxsc.entities.Tag;
import us.yugen.yaxyxsc.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    public static final List<ShoppingList> SHOPPING_LISTS = new ArrayList<>();
    public static final Map<Tag, List<ShoppingList>> SHOPPING_LISTS_BY_TAG = new HashMap<>();
    public static final List<User> USERS = new ArrayList<>();
}