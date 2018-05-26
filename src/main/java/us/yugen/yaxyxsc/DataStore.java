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

class DataStore {
    static final List<ShoppingList> SHOPPING_LISTS = new ArrayList<>();
    static final Map<Tag, List<ShoppingList>> SHOPPING_LISTS_BY_TAG = new HashMap<>();
    static final List<User> USERS = new ArrayList<>();

    static List<ShoppingList> getShoppingListsByTagInArea(final Tag tag,
                                                          final double longitude,
                                                          final double latitude) {
        List<ShoppingList> filteredByTag = SHOPPING_LISTS_BY_TAG.get(tag);

        if(null == filteredByTag) return new ArrayList<>();

        final LatLng coords = new LatLng(latitude, longitude);

        final List<ShoppingList> filteredList = new ArrayList<>();

        for (final ShoppingList shoppingList : filteredByTag) {
            LatLng listCoord = new LatLng(shoppingList.owner.address.latitude, shoppingList.owner.address.longitude);

            //hehehe
            //if(LatLngTool.distance(listCoord, coords, LengthUnit.KILOMETER) < 5) {
                filteredList.add(shoppingList);
            //}
        }
        return filteredList;
    }
}