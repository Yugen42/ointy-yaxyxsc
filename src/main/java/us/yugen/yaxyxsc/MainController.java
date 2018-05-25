package us.yugen.yaxyxsc;

import com.google.gson.Gson;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import us.yugen.yaxyxsc.entities.ShoppingList;

import java.util.ArrayList;
import java.util.List;

@Controller
@EnableAutoConfiguration
public class MainController {

    private static final Gson GSON = new Gson();

    @RequestMapping("/ShoppingLists")
    @ResponseBody
    final String getShoppingLists() {
        List<ShoppingList> allLists = new ArrayList<>();

        ShoppingList testListA = new ShoppingList();
        List<String> itemList = new ArrayList<>();
        itemList.add("Condoms XXS");
        itemList.add("tampons");
        testListA.items = itemList;

        allLists.add(testListA);

        return GSON.toJson(DataStore.user);
    }

    @RequestMapping(value = "/shoppingList", method = RequestMethod.POST)
    final String postShoppingList(@RequestBody ShoppingList shoppingList) {
        //TODO

        return GSON.toJson(ResponseEntity.ok());
    }
}