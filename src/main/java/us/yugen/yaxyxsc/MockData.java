package us.yugen.yaxyxsc;

import com.javadocmd.simplelatlng.LatLng;
import us.yugen.yaxyxsc.entities.Address;
import us.yugen.yaxyxsc.entities.User;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MockData {
    public static void mockData() {
        DataStore.user.addAll( generateUser(20));
    }

    static String[] firstNames = {"Hans", "Peter", "Detlev", "Dieter", "Xenofilius"};
    static String[] lastName = {"Redis", "Ointy", "Oynti", "Java Sucks"};
    static String[] streets = {"Kaufstraße", "Lolzstr", "Hackerburg"};
    static String[] cities = {"Regensburg", "Wernberg", "unteraching"};

    static private List<User> generateUser(int amount) {
        return IntStream.range(0, amount).mapToObj((i) -> new User(i, getRandom(firstNames), getRandom(lastName), getRandomAddress())).collect((Collectors.toList()));
    }

    static Address getRandomAddress() {
        var pos = LatLng.random();
        return new Address(pos.getLongitude(),pos.getLatitude(), new Random().nextInt(95000), getRandom(cities), getRandom(streets), new Random().nextInt(5000));
    }


    static String getRandom(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }


}
