package ca.gimmecards.display;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.FormatUtils;
import net.dv8tion.jda.api.EmbedBuilder;

public class BackpackDisplay extends Display {

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += EmoteConsts.XP + " " + FormatUtils.formatNumber(user.getXP()) + "/" + FormatUtils.formatNumber(user.getMaxXP()) + " until next level\n";
        desc += "┅┅\n";
        desc += EmoteConsts.token + " **Tokens** ┇ " + FormatUtils.formatNumber(user.getTokens()) + "\n";
        desc += EmoteConsts.credits + " **Credits** ┇ " + FormatUtils.formatNumber(user.getCredits()) + "\n";
        desc += EmoteConsts.star + " **Stars** ┇ " + FormatUtils.formatNumber(user.getStars()) + "\n";
        desc += EmoteConsts.key + " **Keys** ┇ " + FormatUtils.formatNumber(user.getKeys()) + "\n";
        desc += "┅┅\n";

        if(user.getBadges().size() > 0) {
            desc += "**Badges** ┇ ";
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("dev")) {
                    desc += EmoteConsts.devBadge + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("staff")) {
                    desc += EmoteConsts.staffBadge + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("veteran")) {
                    desc += EmoteConsts.veteranBadge + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("master")) {
                    desc += EmoteConsts.masterBadge + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("bday")) {
                    desc += EmoteConsts.bdayBadge + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("original")) {
                    desc += EmoteConsts.originalBagde + " ";
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
