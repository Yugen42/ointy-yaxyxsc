package us.yugen.yaxyxsc;

import com.github.javafaker.Faker;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LatLngConfig;
import com.javadocmd.simplelatlng.util.LengthUnit;
import us.yugen.yaxyxsc.entities.Address;
import us.yugen.yaxyxsc.entities.ShoppingList;
import us.yugen.yaxyxsc.entities.Tag;
import us.yugen.yaxyxsc.entities.User;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MockData {
    public static void mockData() {
        DataStore.USERS.addAll(generateUser(20));
    }

    static Faker faker = new Faker();
    static LatLng pos = LatLng.random();

    static private List<User> generateUser(int amount) {
        return IntStream.range(0, amount).mapToObj((i) -> {
            var user = new User(i, faker.name().firstName(), faker.name().lastName(), getRandomAddress());
            if (new Random().nextInt(100) >= -1) {
                for(var tag : Tag.values()) {
                    final ShoppingList randomShoppingList = getRandomShoppingList(user, tag);
                    DataStore.SHOPPING_LISTS.add(randomShoppingList);
                    DataStore.SHOPPING_LISTS_BY_TAG.get(randomShoppingList.tag);
                }
            }
            return user;
        }).collect((Collectors.toList()));
    }

    static Address getRandomAddress() {
        var pos = getNearPos(5);
        return new Address(pos.getLongitude(), pos.getLatitude(), new Random().nextInt(95000), faker.address().city(), faker.address().streetPrefix() + faker.address().streetName() + faker.address().streetSuffix(), new Random().nextInt(5000));
    }

    static ShoppingList getRandomShoppingList(User owner, Tag tag) {
        var items = IntStream.range(0, new Random().nextInt(15) + 1).mapToObj((i) -> i % 2 == 0 ? faker.food().ingredient() : faker.commerce().productName()).collect(Collectors.toList());

        return new ShoppingList(owner, tag, items);
    }

    static LatLng getNearPos(double distanceInKm) {
         return new LatLng(pos.getLatitude()+ new Random().nextDouble()*.5f, pos.getLongitude() + new Random().nextDouble() *.5f);
    }

    static String getRandom(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }


}
