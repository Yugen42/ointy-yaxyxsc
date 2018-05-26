package us.yugen.yaxyxsc;

import com.google.gson.Gson;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import us.yugen.yaxyxsc.entities.ShoppingList;
import us.yugen.yaxyxsc.entities.Tag;
import us.yugen.yaxyxsc.entities.User;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@EnableAutoConfiguration
final class MainController {

    private static final Gson GSON = new Gson();
    private static final double DIST = 0.05d;

    private MainController() {
    }

    @RequestMapping("/shoppingLists")
    @ResponseBody
    final ResponseEntity<List<ShoppingList>> getShoppingLists() {

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(DataStore.SHOPPING_LISTS);
    }

    @RequestMapping(value = "/{ownerId}/shoppingList", method = RequestMethod.POST)
    ResponseEntity<Object> postShoppingList(@PathVariable int ownerId, @RequestBody String shoppingList) {

        var list = new Gson().fromJson(shoppingList, ShoppingList.class);

        var owner = DataStore.USERS.stream().filter((localUser) -> localUser.id == ownerId).findAny().get();
        if (owner == null) {
            return Oh.Not.ok();
        }

        list.owner = owner;
        list.id = DataStore.SHOPPING_LISTS.size();
        DataStore.SHOPPING_LISTS.add(list);

        return ResponseEntity.ok().body(null);
    }


    @RequestMapping(value = "/{ownerId}/shoppingList", method = RequestMethod.GET)
    ResponseEntity<Object> postShoppingList(@PathVariable int ownerId) {

        var list = DataStore.SHOPPING_LISTS.stream().filter((currList) -> currList.owner.id == ownerId).collect(Collectors.toList());

        return ResponseEntity.ok().body(list);
    }


    @RequestMapping(value = "/shoppingList/{shoppingListID}", method = RequestMethod.GET)
    ResponseEntity<String> getShopStringList(@PathVariable("shoppingListID") int shoppingListID) {
        var shoppingList = DataStore.SHOPPING_LISTS.stream()
                .filter(list -> list.id == shoppingListID)
                .findAny().get();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(GSON.toJson(shoppingList));
    }

    @RequestMapping(value = "/getListsOfUser/{userId}", method = RequestMethod.GET)
    ResponseEntity<String> getListsOfUser(@PathVariable("userId") int userId) {

        var user = DataStore.USERS.stream().filter((currUser) -> currUser.id == userId).findFirst().get();
        var lists = DataStore.SHOPPING_LISTS.stream()
                .filter(list -> list.owner.equals(user))
                .collect(Collectors.toList());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(GSON.toJson(lists));
    }

    @RequestMapping(value = "/shoppingList/{id}/item", method = RequestMethod.POST)
    ResponseEntity addItemToList(@PathVariable final int id, @RequestBody final String Item) {
        var list = DataStore.SHOPPING_LISTS.stream().filter((currList) -> currList.id == id).findFirst().get();
        if (list == null) {
            return Oh.Not.ok();
        }
        list.items.add(Item);
        return Oh.ok();
    }

    @ModelAttribute
    public void setVaryResponseHeader(final HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
    }

    @RequestMapping(value = "/getListsRelevantForUser/{userId}/{tag}", method = RequestMethod.GET)
    ResponseEntity<String> getListsRelevantForUser(@PathVariable("userId") final int userId,
                                                   @PathVariable("tag") final String tag) {

        List<ShoppingList> results = new ArrayList<>();

        final Tag tagEnum = Tag.valueOf(tag);

        User u = null;
        final List<User> users = DataStore.USERS;
        for (User user : users) {
            if (user.id == userId) {
                u = user;
                break;
            }
        }
        if (null == u) return Oh.Not.ok();


        final List<ShoppingList> listOfLists = DataStore.SHOPPING_LISTS_BY_TAG.getOrDefault(tagEnum, Collections.emptyList());

        for (final ShoppingList list : listOfLists) {

            var d = Math.sqrt(Math.pow(list.owner.address.latitude - u.address.latitude, 2.0d) + Math.pow(list.owner.address.longitude - u.address.longitude, 2.0d));
            if (d > DIST || d < DIST * -1) {
                results.add(list);
            }
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(GSON.toJson(results));
    }
}

class Oh {
    static ResponseEntity ok() {
        return ResponseEntity.ok(null);
    }

    static class Not {
        static public ResponseEntity ok() {
            return ResponseEntity.badRequest().body(null);
        }
    }

}