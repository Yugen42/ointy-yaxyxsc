package us.yugen.yaxyxsc;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import us.yugen.yaxyxsc.entities.ShoppingList;
import us.yugen.yaxyxsc.entities.Tag;
import us.yugen.yaxyxsc.entities.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@EnableAutoConfiguration
@EnableSwagger2
@Configuration
class MainController extends WebMvcConfigurationSupport {
    private static final Gson GSON = new Gson();

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
    protected void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

    @RequestMapping("/shoppingLists")
    @ResponseBody
    final ResponseEntity<List<ShoppingList>> getShoppingLists() {

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(DataStore.SHOPPING_LISTS);
    }

    @PostMapping("/claimShoppingList/{list}/{claimer}")
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

    @RequestMapping("/getClaimedShoppingList/{userID}")
    @ResponseBody
    ResponseEntity claimShoppingList(@PathVariable("userID") int userId) {
        List<ShoppingList> result = new ArrayList<>();
        for (ShoppingList currList : DataStore.SHOPPING_LISTS) {
            if (currList.claimedByUser != null && currList.claimedByUser.id == userId) {
                result.add(currList);
                break;
            }
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(GSON.toJson(result));
    }

    @RequestMapping("/closeShoppingList/{listId}")
    @ResponseBody
    ResponseEntity closeShoppinglist(@PathVariable("listId") int listId) {
        final var shoppinglist = DataStore.SHOPPING_LISTS.stream().filter((currList) -> currList.id == listId).findFirst().get();
        shoppinglist.items.clear();
        shoppinglist.claimedByUser = null;
        if (shoppinglist != null) {
            return Oh.ok();
        } else {
            return Oh.Not.ok();
        }
    }


    @PostMapping("/{ownerId}/shoppingList")
    ResponseEntity<Object> postShoppingList(@PathVariable int ownerId, @RequestBody String shoppingList) {

        final var list = new Gson().fromJson(shoppingList, ShoppingList.class);

        final var owner = DataStore.USERS.stream().filter((localUser) -> localUser.id == ownerId).findAny().get();
        if (null == owner) {
            return Oh.Not.ok();
        }

        list.owner = owner;
        list.id = DataStore.SHOPPING_LISTS.size();
        DataStore.SHOPPING_LISTS.add(list);

        return ResponseEntity.ok().body(null);
    }


    @GetMapping("/{ownerId}/shoppingList")
    ResponseEntity<Object> postShoppingList(@PathVariable int ownerId) {

        var list = DataStore.SHOPPING_LISTS.stream().filter((currList) -> currList.owner.id == ownerId).collect(Collectors.toList());

        return ResponseEntity.ok().body(list);
    }


    @GetMapping("/shoppingList/{shoppingListID}")
    ResponseEntity<String> getShopStringList(@PathVariable("shoppingListID") final int shoppingListID) {
        ShoppingList found = null;
        for (final ShoppingList list : DataStore.SHOPPING_LISTS) {
            if (list.id == shoppingListID) {
                found = list;
                break;
            }
        }

        if (null == found) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8).body("No such list.");
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(GSON.toJson(found));

    }

    @GetMapping("/getListsOfUser/{userId}")
    ResponseEntity<String> getListsOfUser(@PathVariable("userId") final int userId) {

        final var user = DataStore.USERS.stream().filter((currUser) -> currUser.id == userId).findFirst().get();
        final var lists = DataStore.SHOPPING_LISTS.stream()
                .filter(list -> list.owner.equals(user))
                .collect(Collectors.toList());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(GSON.toJson(lists));
    }

    @PostMapping("/shoppingList/{id}/item")
    ResponseEntity addItemToList(@PathVariable final int id, @RequestBody final String Item) {
        var list = DataStore.SHOPPING_LISTS.stream().filter((currList) -> currList.id == id).findFirst().get();
        if (list == null) {
            return Oh.Not.ok();
        }
        list.items.add(Item);
        return Oh.ok();
    }

    @DeleteMapping("/shoppingList/{id}/item/{index}")
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

    @GetMapping("/getListsRelevantForUser/{userId}/{lang}/{log}")
    ResponseEntity<String> getListsRelevantForUser(@PathVariable("userId") final int userId,
                                                   @PathVariable("lang") final double latitude,
                                                   @PathVariable("log") final double longitude) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyDY1cPfXT1w_iywCZFFMuXFkPm3K3XDT-c&location="+latitude+","+longitude+"&radius=100")
                .build();



        Response response = null;
        try {
            response = client.newCall(request).execute();
            JsonElement json = new JsonParser().parse(response.body().string());

            JsonArray places = json.getAsJsonObject().getAsJsonArray("results");

            final Set<Tag> relevantTags = new HashSet<>();

            for (final JsonElement place : places) {

                final JsonArray types = place.getAsJsonObject().getAsJsonArray("types");

                for (final JsonElement type : types) {
                    relevantTags.add(Tag.mapToTag(type.getAsString()));
                }
            }

            User foundUser = null;
            for (final User user : DataStore.USERS) {
                if(user.id == userId) {
                    foundUser = user;
                }
            }


            Collection<ShoppingList> shoppingLists = new HashSet<>();
            for (final Tag relevantTag : relevantTags) {
                shoppingLists.addAll(DataStore.getShoppingListsByTagInArea(relevantTag, foundUser.address.longitude, foundUser.address.latitude));
            }

            shoppingLists = shoppingLists.stream().filter((list) -> list.items.size()> 0).collect(Collectors.toList());
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(GSON.toJson(shoppingLists));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("not");
        }
    }


    @RequestMapping("/")
    @ResponseBody
    final ResponseEntity<String> abandonHope() {

        final String abandonIt = "Through me you pass into the city of woe:\n" +
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

final class Oh {
    static ResponseEntity ok() {
        return ResponseEntity.ok("{\"ok\":\"ok\"}");
    }

    static class Not {
        static public ResponseEntity ok() {
            return ResponseEntity.badRequest().body("not");
        }
    }

}