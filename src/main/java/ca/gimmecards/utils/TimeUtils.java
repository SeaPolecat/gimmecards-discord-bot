package ca.gimmecards.utils;
import java.util.Calendar;

public class TimeUtils {
    
    /**
     * finds the amount of cooldown time left for a specific command
     * @param cooldown the cooldown (in seconds) of the command that's being checked
     * @param epoch one of the player's epoch times above
     * @return the number of seconds left before the command can be called again
     */
    public static int findCooldownLeft(int cooldown, long epoch) {
        long current = Calendar.getInstance().getTimeInMillis() / 1000;
        long secsPassed = current - epoch; // seconds passed since calling the command
        int secsLeft = (int) Math.max(cooldown - secsPassed, 0);

        return secsLeft;
    }
}
