package ca.gimmecards.utils;
import java.util.Random;

public class NumberUtils {
    
    /**
     * picks a random int between 2 ints (because Java version 11 doesn't have a built-in function to do this)
     * @param min the lower range int
     * @param max the upper range int
     * @return a random int between the two ints
     */
    public static int randRange(int min, int max) {
        int diff = max - min;

        return new Random().nextInt(diff + 1) + min;
    }
}
