package us.yugen.yaxyxsc;

import com.google.gson.Gson;
import com.javadocmd.simplelatlng.LatLng;
import com.mashape.unirest.http.Unirest;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;
import se.walkercrou.places.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import us.yugen.yaxyxsc.entities.ShoppingList;
import us.yugen.yaxyxsc.entities.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.crypto.Data;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@EnableAutoConfiguration
@EnableSwagger2
@Configuration
class MainController extends WebMvcConfigurationSupport {
    private static final Gson GSON = new Gson();
    private static final double DIST = 0.05d;

    public MainController() {
    }

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

    @RequestMapping("/shoppingLists")
    @ResponseBody
    final ResponseEntity<List<ShoppingList>> getShoppingLists() {

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(DataStore.SHOPPING_LISTS);
    }

    @RequestMapping("/claimShoppingList/{list}/{claimer}")
    @ResponseBody
    ResponseEntity claimShoppingList(@PathVariable("list") int list, @PathVariable("claimer") int claimer) {
        var shoppinglist = DataStore.SHOPPING_LISTS.stream().filter((currList) -> currList.id == list).findFirst().get();
        var user = DataStore.USERS.stream().filter(currUSer -> currUSer.id == claimer).findFirst().get();

        if (shoppinglist != null && user != null) {
            shoppinglist.claimedByUser = user;
            return Oh.ok();
        } else {
            return Oh.Not.ok();
        }

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
                                                   @PathVariable("lang") final double latitude,
                                                   @PathVariable("log") final double longitude) {

        GooglePlaces client = new GooglePlaces("AIzaSyDY1cPfXT1w_iywCZFFMuXFkPm3K3XDT-c", new UltimateRequestHandler());

        List<Place> places = client.getNearbyPlaces(latitude, longitude, 100, GooglePlaces.MAXIMUM_RESULTS);
        List<JSONObject> jsons = new ArrayList<>();

        for (Place place : places) {
            //try {
            //    place.getClass().getDeclaredField("client").setAccessible(true);
            //    place.getClass().getDeclaredField("client").set(place, null);
            //} catch (Exception e) {
            //    e.printStackTrace();
            //}

            jsons.add(place.getJson());
        }


        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(GSON.toJson(jsons));
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