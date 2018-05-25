package us.yugen.yaxyxsc;

import com.github.javafaker.Faker;
import com.javadocmd.simplelatlng.LatLng;
import us.yugen.yaxyxsc.entities.Address;
import us.yugen.yaxyxsc.entities.ShoppingList;
import us.yugen.yaxyxsc.entities.User;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MockData {
    public static void mockData() {
        DataStore.USERS.addAll( generateUser(20));
    }

    static Faker faker = new Faker();
    static String[] streets = {"Kaufstraße", "Lolzstr", "Hackerburg"};

    static private List<User> generateUser(int amount) {
        return IntStream.range(0, amount).mapToObj((i) -> {
            var user = new User(i, faker.name().firstName(), faker.name().lastName(), getRandomAddress());
            if( new Random().nextInt(100) >= 33) {
                DataStore.SHOPPING_LISTS.add(getRandomShopingLists(user));
            }
            return  user;
        }).collect((Collectors.toList()));
    }

    static Address getRandomAddress() {
        var pos = LatLng.random();
        return new Address(pos.getLongitude(),pos.getLatitude(), new Random().nextInt(95000), faker.address().city(), getRandom(streets), new Random().nextInt(5000));
    }

    static ShoppingList getRandomShopingLists(User owner){
        var items = IntStream.range(0, new Random().nextInt(15)).mapToObj((i) ->  i % 2 == 0 ? faker.food().ingredient() : faker.commerce().productName()).collect(Collectors.toList());
        var tags =  IntStream.range(0, new Random().nextInt(4)).mapToObj((i) -> faker.commerce().department()).collect(Collectors.toList());

        return new ShoppingList(owner,tags, items);
    }


    static String getRandom(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }


}
