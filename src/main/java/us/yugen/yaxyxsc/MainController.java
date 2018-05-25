package us.yugen.yaxyxsc;

import com.google.gson.Gson;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import us.yugen.yaxyxsc.entities.ShoppingList;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

@Controller
@EnableAutoConfiguration
public class MainController {

    @RequestMapping("/ShoppingLists")
    @ResponseBody
    String getShoppingLists() {
        List<ShoppingList> allLists = new ArrayList<>();

        ShoppingList testListA = new ShoppingList();
        List<String> itemList = new ArrayList<>();
        itemList.add("Condoms XXS");
        itemList.add("tampons");
        testListA.items = itemList;

        allLists.add(testListA);

        Gson gson = new Gson();
        final String s = gson.toJson(DataStore.user);

        return s;
    }

    //@RequestMapping(value = "/buyingList", method = RequestMethod.POST)
    //String postShoppingList
}