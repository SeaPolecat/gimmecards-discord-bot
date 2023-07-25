package ca.gimmecards.Display;
import ca.gimmecards.Main.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class BackpackDisplay extends Display {

    public BackpackDisplay(String ui) {
        super(ui);
    }

    @Override
    public BackpackDisplay findDisplay() {
        String userId = getUserId();
        
        for(BackpackDisplay b : IDisplays.backpackDisplays) {
            if(b.getUserId().equals(userId)) {
                return b;
            }
        }
        IDisplays.backpackDisplays.add(0, new BackpackDisplay(userId));
        return IDisplays.backpackDisplays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += IEmotes.XP + " " + GameManager.formatNumber(user.getXP()) + "/" + GameManager.formatNumber(user.getMaxXP()) + " until next level\n";
        desc += "┅┅\n";
        desc += IEmotes.token + " **Tokens** ┇ " + GameManager.formatNumber(user.getTokens()) + "\n";
        desc += IEmotes.credits + " **Credits** ┇ " + GameManager.formatNumber(user.getCredits()) + "\n";
        desc += IEmotes.star + " **Stars** ┇ " + GameManager.formatNumber(user.getStars()) + "\n";
        desc += IEmotes.key + " **Keys** ┇ " + GameManager.formatNumber(user.getKeys()) + "\n";
        desc += "┅┅\n";

        if(user.getBadges().size() > 0) {
            desc += "**Badges** ┇ ";
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("dev")) {
                    desc += IEmotes.devBadge + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("staff")) {
                    desc += IEmotes.staffBadge + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("veteran")) {
                    desc += IEmotes.veteranBadge + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("master")) {
                    desc += IEmotes.masterBadge + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("bday")) {
                    desc += IEmotes.bdayBadge + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("original")) {
                    desc += IEmotes.originalBagde + " ";
                    break;
                }
            }
        }
        if(!user.getPinnedCard().equals("") && user.ownsPinnedCard()) {
            embed.setImage(user.getPinnedCard());
        }
        embed.setTitle(ui.getUserName() + " ┇ Level " + user.getLevel());
        embed.setThumbnail(ui.getUserIcon());
        embed.setDescription(desc);
        embed.setColor(user.getGameColor());
        return embed;
    }
}
