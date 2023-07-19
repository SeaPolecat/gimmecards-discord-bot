package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class BackpackDisplay extends Display {

    public BackpackDisplay(String ui) {
        super(ui);
    }

    @Override
    public BackpackDisplay findDisplay() {
        String userId = getUserId();
        
        for(BackpackDisplay b : backpackDisplays) {
            if(b.getUserId().equals(userId)) {
                return b;
            }
        }
        backpackDisplays.add(0, new BackpackDisplay(userId));
        return backpackDisplays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += XP_ + " " + GameObject.formatNumber(user.getXP()) + "/" + GameObject.formatNumber(user.getMaxXP()) + " until next level\n";
        desc += "┅┅\n";
        desc += token_ + " **Tokens** ┇ " + GameObject.formatNumber(user.getTokens()) + "\n";
        desc += credits_ + " **Credits** ┇ " + GameObject.formatNumber(user.getCredits()) + "\n";
        desc += star_ + " **Stars** ┇ " + GameObject.formatNumber(user.getStars()) + "\n";
        desc += key_ + " **Keys** ┇ " + GameObject.formatNumber(user.getKeys()) + "\n";
        desc += "┅┅\n";

        if(user.getBadges().size() > 0) {
            desc += "**Badges** ┇ ";
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
                if(badge.equalsIgnoreCase("patreon")) {
                    desc += patreonBadge_ + " ";
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
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("original")) {
                    desc += originalBagde_ + " ";
                    break;
                }
            }
        }
        if(!user.getPinnedCard().equals("") && user.ownsFavCard()) {
            embed.setImage(user.getPinnedCard());
        }
        embed.setTitle(ui.getUserName() + " ┇ Level " + user.getLevel());
        embed.setThumbnail(ui.getUserIcon());
        embed.setDescription(desc);
        embed.setColor(user.getGameColor());
        return embed;
    }
}
