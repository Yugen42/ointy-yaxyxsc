package us.yugen.yaxyxsc;

import com.google.gson.Gson;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import us.yugen.yaxyxsc.entities.ShoppingList;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@EnableAutoConfiguration
final class MainController {

    private static final Gson GSON = new Gson();

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
    ResponseEntity addItemToList(@PathVariable int id, @RequestBody String Item) {
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

    @RequestMapping(value = "/getListsRelevantForUser/{userId}", method = RequestMethod.GET)
    ResponseEntity<String> getListsRelevantForUser(@PathVariable("userId") final int userId) {


        return Oh.ok();
    }
}

class Oh {
    public static ResponseEntity ok() {
        return ResponseEntity.ok(null);
    }

    static class Not {
        static public ResponseEntity ok() {
            return ResponseEntity.badRequest().body(null);
        }
    }

}