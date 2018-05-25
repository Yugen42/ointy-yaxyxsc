package us.yugen.yaxyxsc;

import com.google.gson.Gson;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import us.yugen.yaxyxsc.entities.ShoppingList;

import java.util.ArrayList;
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
    final String getShoppingLists() {

        return GSON.toJson(DataStore.SHOPPING_LISTS);
    }

    @RequestMapping(value = "/shoppingList", method = RequestMethod.POST)
    String postShoppingList(@RequestBody ShoppingList shoppingList) {
        //TODO

        return GSON.toJson(ResponseEntity.ok());
    }

    @RequestMapping(value = "/shoppingList/{id}", method = RequestMethod.GET)
    String getShopStringList(@PathVariable int shoppingListID) {
        var shoppingList = DataStore.SHOPPING_LISTS.stream()
                .filter(list -> list.id == shoppingListID)
                .findAny().get();
        return GSON.toJson(shoppingList);
    }

    @RequestMapping(value = "/getListsOfUSer/{userId}", method = RequestMethod.GET)
    String getListsOfUSer(@PathVariable int userId) {
        
        var user = DataStore.USERS.stream().filter((currUser) -> currUser.id == userId).findFirst().get();
        var lists = DataStore.SHOPPING_LISTS.stream()
                .filter(list -> list.owner.equals(user))
                .collect(Collectors.toList());
        return GSON.toJson(lists);
    }
}