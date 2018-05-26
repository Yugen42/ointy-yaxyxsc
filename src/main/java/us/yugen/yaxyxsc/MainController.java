package us.yugen.yaxyxsc;

import com.google.gson.Gson;
import com.javadocmd.simplelatlng.LatLng;
import com.mashape.unirest.http.Unirest;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import us.yugen.yaxyxsc.entities.ShoppingList;
import us.yugen.yaxyxsc.entities.User;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@EnableAutoConfiguration
@EnableSwagger2
@Configuration
class MainController extends WebMvcConfigurationSupport {
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();

    }
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    private static final Gson GSON = new Gson();
    private static final double DIST = 0.05d;

    public MainController() {
    }


    @RequestMapping("/shoppingLists")
    @ResponseBody
    final ResponseEntity<List<ShoppingList>> getShoppingLists() {

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(DataStore.SHOPPING_LISTS);
    }

    @PostMapping(value = "/{ownerId}/shoppingList")
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


    @GetMapping(value = "/{ownerId}/shoppingList")
    ResponseEntity<Object> postShoppingList(@PathVariable int ownerId) {

        var list = DataStore.SHOPPING_LISTS.stream().filter((currList) -> currList.owner.id == ownerId).collect(Collectors.toList());

        return ResponseEntity.ok().body(list);
    }


    @GetMapping(value = "/shoppingList/{shoppingListID}")
    ResponseEntity<String> getShopStringList(@PathVariable("shoppingListID") int shoppingListID) {
        var shoppingList = DataStore.SHOPPING_LISTS.stream()
                .filter(list -> list.id == shoppingListID)
                .findAny().get();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(GSON.toJson(shoppingList));
    }

    @GetMapping(value = "/getListsOfUser/{userId}")
    ResponseEntity<String> getListsOfUser(@PathVariable("userId") int userId) {

        var user = DataStore.USERS.stream().filter((currUser) -> currUser.id == userId).findFirst().get();
        var lists = DataStore.SHOPPING_LISTS.stream()
                .filter(list -> list.owner.equals(user))
                .collect(Collectors.toList());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(GSON.toJson(lists));
    }

    @PostMapping(value = "/shoppingList/{id}/item")
    ResponseEntity addItemToList(@PathVariable final int id, @RequestBody final String Item) {
        var list = DataStore.SHOPPING_LISTS.stream().filter((currList) -> currList.id == id).findFirst().get();
        if (list == null) {
            return Oh.Not.ok();
        }
        list.items.add(Item);
        return Oh.ok();
    }

    @DeleteMapping(value = "/shoppingList/{id}/item/{index}")
    ResponseEntity removeItemFromListByIndex(@PathVariable final int id, @PathVariable final int index) {
        var list = DataStore.SHOPPING_LISTS.stream().filter((currList) -> currList.id == id).findFirst().get();
        if (list == null) {
            return Oh.Not.ok();
        }
        list.items.remove(index);
        return Oh.ok();
    }

    @ModelAttribute
    public void setVaryResponseHeader(final HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
    }

    @GetMapping(value = "/getListsRelevantForUser/{userId}/{lang}/{log}")
    ResponseEntity<String> getListsRelevantForUser(@PathVariable("userId") final int userId,
                                                   @PathVariable("lang") final double lang,
                                                   @PathVariable("log") final double log) {

        List<ShoppingList> results = new ArrayList<>();

        final LatLng curPoss = new LatLng(lang,log);
        var url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyDY1cPfXT1w_iywCZFFMuXFkPm3K3XDT-c&location="+lang+","+log+"&radius=100";
        var body = Unirest.get(url);

        System.out.println(body);

        User u = null;
        final List<User> users = DataStore.USERS;
        for (User user : users) {
            if (user.id == userId) {
                u = user;
                break;
            }
        }
        if (null == u) return Oh.Not.ok();
        final User notAllowedUser = u;

        for (final ShoppingList list : DataStore.SHOPPING_LISTS.stream().filter((currList) -> !currList.owner.equals(notAllowedUser)).collect(Collectors.toList())) {

            var d = Math.sqrt(Math.pow(list.owner.address.latitude - u.address.latitude, 2.0d) + Math.pow(list.owner.address.longitude - u.address.longitude, 2.0d));
            if (d > DIST || d < DIST * -1) {
                results.add(list);
            }
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(GSON.toJson(results));
    }

    @RequestMapping("/")
    @ResponseBody
    final ResponseEntity<String> abandonHope() {

        String abandonIt = "Through me you pass into the city of woe:\n" +
                "Through me you pass into eternal pain:\n" +
                "Through me among the people lost for aye.\n" +
                "\n" +
                "Justice the founder of my fabric mov'd:\n" +
                "To rear me was the task of power divine,\n" +
                "Supremest wisdom, and primeval love.\n" +
                "\n" +
                "Before me things create were none, save things\n" +
                "Eternal, and eternal I endure.\n" +
                "All hope abandon ye who enter here.";

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(abandonIt);
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