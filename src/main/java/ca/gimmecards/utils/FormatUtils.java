package ca.gimmecards.utils;
import ca.gimmecards.main.*;
import java.text.NumberFormat;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class FormatUtils {
    
    /**
     * inserts commas in a large number to make it more readable
     * @param number the large number
     * @return a formatted number with commas
     */
    public static String formatNumber(int number) {
        return NumberFormat.getInstance().format(number);
    }

    /**
     * formats a command name by slapping a slash in front of it
     * @param cmd the command name to format
     * @return the formatted command name
     */
    public static String formatCmd(String cmd) {
        return "`/" + cmd + "`";
    }

    /**
     * formats the player's username by turning it into a ping
     * @param event a slash event, used to get the username
     * @return the formatted ping username
     */
    public static String formatName(SlashCommandInteractionEvent event) {
        return event.getUser().getAsMention();
    }

    /**
     * formats a mentioned player's username by turning it into a ping
     * @param mention the mentioned player
     * @param event a slash event, used to get the username
     * @return the formatted ping username
     */
    public static String formatName(User mention, SlashCommandInteractionEvent event) {
        net.dv8tion.jda.api.entities.User user = event.getJDA().getUserById(mention.getUserId()+"");

        if(user == null) { return ""; }

        return user.getAsMention();
    }

    public static String formatCooldown(int secsLeft) {
        int hrs = secsLeft / 3600;
        int mins = (secsLeft - (hrs * 3600)) / 60;

        if(hrs > 0)
            return "**" + hrs + " hr " + mins + " min**";
        else if(mins > 0)
            return "**" + mins + " min**";

        int secs = (secsLeft - (hrs * 3600)) % 60;

        return "**" + secs + " sec**";
    }
}
