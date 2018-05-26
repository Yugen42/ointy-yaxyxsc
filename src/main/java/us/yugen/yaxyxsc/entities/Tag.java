package us.yugen.yaxyxsc.entities;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Andreas Hartmann
 */
public enum Tag {
    GROCERY_STORE,
    BAKERY,
    PHARMACY,
    HARDWARE_STORE,
    SEX_SHOP

    private static final List<Tag> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Tag randomTag()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
