package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class BackpackDisplay extends Display {
    
    public static ArrayList<BackpackDisplay> displays = new ArrayList<BackpackDisplay>();

    public BackpackDisplay(String ui) {
        super(ui);
    }

    public static BackpackDisplay findBackpackDisplay(String authorId) {
        for(BackpackDisplay b : displays) {
            if(b.getUserId().equals(authorId)) {
                return b;
            }
        }
        displays.add(0, new BackpackDisplay(authorId));
        return displays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += XP_ + " " + UX.formatNumber(user.getXP()) + "/" + UX.formatNumber(user.getMaxXP()) + " until next level\n";
        desc += "┅┅\n";
        desc += token_ + " **Tokens** ┇ " + UX.formatNumber(user.getTokens()) + "\n";
        desc += energy_ + " **Energy** ┇ " + UX.formatNumber(user.getEnergy()) + "\n";
        desc += key_ + " **Keys** ┇ " + UX.formatNumber(user.getKeys()) + "\n";
        desc += star_ + " **Stars** ┇ " + UX.formatNumber(user.getStars()) + "\n";
        desc += "┅┅\n";

        if(user.getBadges().size() > 0) {
            desc += "*Badges* ┇ ";
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("dev")) {
                    desc += devBadge_ + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("staff")) {
                    desc += staffBadge_ + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("community")) {
                    desc += communityBadge_ + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("veteran")) {
                    desc += veteranBadge_ + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("master")) {
                    desc += masterBadge_ + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("bday")) {
                    desc += bdayBadge_ + " ";
                    break;
                }
            }
            desc += "\n┅┅\n";
        }
        if(!user.getBackpackCard().equals("") && State.ownsFavCard(user)) {
            desc += "*" + ui.getUserName() + "'s Favourite*";
            embed.setImage(user.getBackpackCard());
        }
        embed.setTitle(ui.getUserName() + " ┇ Level " + user.getLevel());
        embed.setThumbnail(ui.getUserIcon());
        embed.setDescription(desc);
        embed.setColor(user.getBackpackColor());
        return embed;
    }
}
