/*package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class EventDisplay extends Display {
    
    public static ArrayList<EventDisplay> displays = new ArrayList<EventDisplay>();

    public EventDisplay(String ui) {
        super(ui);
    }

    public static EventDisplay findEventDisplay(String authorId) {
        for(EventDisplay e : displays) {
            if(e.getUserId().equals(authorId)) {
                return e;
            }
        }
        displays.add(0, new EventDisplay(authorId));
        return displays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "", letter = "";
        int tasks = user.getTasks();

        letter += "Wow, I can't believe it's been a whole year already! I still remember sitting in the "
        + "searing heat of summer, furiously typing away and trying to make Gimme Cards work... "
        + "Thank you guys so much for supporting this project over the months! You are my motivation "
        + "and inspiration, and I am so glad to have such an amazing fanbase. I'd like to give a "
        + "shoutout to ~ziU for being the first person to try my bot, and also to saitama(sh porygon) "
        + "for being an encouraging friend throughout this journey. Of course, we can't forget "
        + "about the rest of the Gimme Cards staff team- a huge shoutout to our managers, moderators, and "
        + "helpers! They are the true backbone of this bot, and will never be forgotten. Whatever "
        + "cards you pull in the future, whatever obstacles you face, always remember to have fun and "
        + "stay ultra rare 🍻\n\n"
        + "*━ Sea*";

        if(tasks >= 25) {
            tasks = 25;
        }
        desc += "*Open 25 packs for a special event gift!*\n";
        desc += "┅┅\n";
        desc += "**Packs Opened** ┇ " + tasks + "/25\n";
        if(user.getGiftClaimed()) {
            desc += "**Gift Status** ┇ ✅ Received\n\n";
        } else {
            desc += "**Gift Status** ┇ ⏳ Not Received\n\n";
        }
        desc += "__**For one week, enjoy:**__\n";
        desc += "🔹25% more XP from selling cards\n";
        desc += "🔹50% off sale in the market\n";
        desc += "🔹Double token and star rewards from leveling up\n";
        desc += "🔹Many giveaway events in our [support server](https://discord.gg/urtHCGcE7y)\n\n";
        desc += "__**Note from the Developer**__\n";
        desc += letter;

        embed.setTitle(pikachu_ + " 🎉 ┇ Gimme Cards 1 Year Anniversary ┇ 🎉 " + pikachu_);
        embed.setDescription(desc);
        embed.setImage("https://ichef.bbci.co.uk/news/976/cpsprodpb/5384/production/_117308312_c18da88c-bb36-4e2d-8ae4-c73fafaa8bf9.jpg");
        embed.setFooter("A big cake for " + ui.getUserName() + "!", ui.getUserIcon());
        embed.setColor(0xFEBE54);
        return embed;
    }
}*/
