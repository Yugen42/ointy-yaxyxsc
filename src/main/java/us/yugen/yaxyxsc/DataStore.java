package us.yugen.yaxyxsc;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
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

    public static List<ShoppingList> getShoppingListsByTagInArea(Tag tag, double longitude, double latitude) {
        List<ShoppingList> filteredByTag = SHOPPING_LISTS_BY_TAG.get(tag);

        if(filteredByTag == null) return new ArrayList<>();

        final LatLng coords = new LatLng(latitude, longitude);

        List<ShoppingList> filteredList = new ArrayList<>();

        for (ShoppingList shoppingList : filteredByTag) {
            LatLng listCoord = new LatLng(shoppingList.owner.address.latitude, shoppingList.owner.address.longitude);

            if(LatLngTool.distance(listCoord, coords, LengthUnit.KILOMETER) < 5) {
                filteredList.add(shoppingList);
            }

        }
        return filteredList;
    }
}