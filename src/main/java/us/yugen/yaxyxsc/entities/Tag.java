package us.yugen.yaxyxsc.entities;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * @author Andreas Hartmann
 */
public enum Tag {
    GROCERY_STORE,
    BAKERY,
    PHARMACY;

    private static final List<Tag> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new SecureRandom();

    public static Tag randomTag() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static Tag mapToTag(final String type) {
        switch (type) {
            case "accounting":
            case "airport":
            case "amusement_park":
            case "aquarium":
            case "art_gallery":
            case "atm":
            case "bakery":
            case "bank":
            case "bar":
            case "beauty_salon":
            case "bicycle_store":
            case "book_store":
            case "bowling_alley":
            case "bus_station":
            case "cafe":
            case "campground":
            case "car_dealer":
            case "car_rental":
            case "car_repair":
            case "car_wash":
            case "casino":
            case "cemetery":
            case "church":
            case "city_hall":
            case "clothing_store":
            case "convenience_store":
            case "courthouse":
            case "dentist":
            case "department_store":
            case "doctor":
            case "electrician":
            case "electronics_store":
            case "embassy":
            case "fire_station":
            case "florist":
            case "funeral_home":
            case "furniture_store":
            case "gas_station":
            case "gym":
            case "hair_care":
            case "hardware_store":
            case "hindu_temple":
            case "home_goods_store":
            case "hospital":
            case "insurance_agency":
            case "jewelry_store":
            case "laundry":
            case "lawyer":
            case "library":
            case "liquor_store":
            case "local_government_office":
            case "locksmith":
            case "lodging":
            case "meal_delivery":
            case "meal_takeaway":
            case "mosque":
            case "movie_rental":
            case "movie_theater":
            case "moving_company":
            case "museum":
            case "night_club":
            case "painter":
            case "park":
            case "parking":
            case "pet_store":
            case "pharmacy":
            case "physiotherapist":
            case "plumber":
            case "police":
            case "post_office":
            case "real_estate_agency":
            case "restaurant":
            case "roofing_contractor":
            case "rv_park":
            case "school":
            case "shoe_store":
            case "shopping_mall":
            case "spa":
            case "stadium":
            case "storage":
            case "store":
            case "subway_station":
            case "supermarket":
            case "synagogue":
            case "taxi_stand":
            case "train_station":
            case "transit_station":
            case "travel_agency":
            case "veterinary_care":
            case "zoo":
            default:
                return GROCERY_STORE;
        }
    }
}
