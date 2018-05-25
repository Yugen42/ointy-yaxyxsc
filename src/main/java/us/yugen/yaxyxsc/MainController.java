package us.yugen.yaxyxsc;

import com.google.gson.Gson;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@EnableAutoConfiguration
public class MainController {

    @RequestMapping("/buyingLists")
    @ResponseBody
    String home() {
        List<BuyingList> allLists = new ArrayList<>();

        BuyingList testListA = new BuyingList();
        List<String> itemList = new ArrayList<>();
        itemList.add("Condoms XXS");
        itemList.add("tampons");
        testListA.items = itemList;

        allLists.add(testListA);

        Gson gson = new Gson();
        final String s = gson.toJson(allLists);

        return s;
    }
}