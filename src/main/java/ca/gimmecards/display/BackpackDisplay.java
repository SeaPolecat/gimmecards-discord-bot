package ca.gimmecards.display;
import ca.gimmecards.consts.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.FormatUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class BackpackDisplay extends Display {

    public BackpackDisplay(SlashCommandInteractionEvent event) {
        super(event);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, Server server) {
        UserInfo ui = getUserInfo();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += EmoteConsts.XP + " " + FormatUtils.formatNumber(user.getXP()) + "/" + FormatUtils.formatNumber(user.getMaxXP()) + " until next level\n";
        desc += "┅┅\n";
        desc += EmoteConsts.TOKEN + " **Tokens** ┇ " + FormatUtils.formatNumber(user.getTokens()) + "\n";
        desc += EmoteConsts.CREDITS + " **Credits** ┇ " + FormatUtils.formatNumber(user.getCredits()) + "\n";
        desc += EmoteConsts.STAR + " **Stars** ┇ " + FormatUtils.formatNumber(user.getStars()) + "\n";
        desc += EmoteConsts.KEY + " **Keys** ┇ " + FormatUtils.formatNumber(user.getKeys()) + "\n";
        desc += "┅┅\n";

        if(user.getBadges().size() > 0) {
            desc += "**Badges** ┇ ";
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("dev")) {
                    desc += EmoteConsts.DEV_BADGE + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("staff")) {
                    desc += EmoteConsts.STAFF_BADGE + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("veteran")) {
                    desc += EmoteConsts.VETERAN_BADGE + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("master")) {
                    desc += EmoteConsts.MASTER_BADGE + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("bday")) {
                    desc += EmoteConsts.BDAY_BADGE + " ";
                    break;
                }
            }
            for(String badge : user.getBadges()) {
                if(badge.equalsIgnoreCase("original")) {
                    desc += EmoteConsts.ORIGINAL_BADGE + " ";
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
