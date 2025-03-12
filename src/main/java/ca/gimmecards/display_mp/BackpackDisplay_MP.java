package ca.gimmecards.display_mp;
import ca.gimmecards.consts.*;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.FormatUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class BackpackDisplay_MP extends Display {

    public BackpackDisplay_MP(SlashCommandInteractionEvent event, User target) {
        super(event, target);
    }

    @Override
    public EmbedBuilder buildEmbed(User target, Server server) {
        UserInfo userInfo = getUserInfo(), targetInfo = getTargetInfo();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += EmoteConsts.XP + " " + FormatUtils.formatNumber(target.getXP()) + "/" + FormatUtils.formatNumber(target.getMaxXP()) + " until next level\n";
        desc += "┅┅\n";
        desc += EmoteConsts.TOKEN + " **Tokens** ┇ " + FormatUtils.formatNumber(target.getTokens()) + "\n";
        desc += EmoteConsts.CREDITS + " **Credits** ┇ " + FormatUtils.formatNumber(target.getCredits()) + "\n";
        desc += EmoteConsts.STAR + " **Stars** ┇ " + FormatUtils.formatNumber(target.getStars()) + "\n";
        desc += EmoteConsts.KEY + " **Keys** ┇ " + FormatUtils.formatNumber(target.getKeys()) + "\n";
        desc += "┅┅\n";

        if(target.getBadges().size() > 0) {
            desc += "**Badges** ┇ ";
            for(String badge : target.getBadges()) {
                if(badge.equalsIgnoreCase("dev")) {
                    desc += EmoteConsts.DEV_BADGE + " ";
                    break;
                }
            }
            for(String badge : target.getBadges()) {
                if(badge.equalsIgnoreCase("staff")) {
                    desc += EmoteConsts.STAFF_BADGE + " ";
                    break;
                }
            }
            for(String badge : target.getBadges()) {
                if(badge.equalsIgnoreCase("veteran")) {
                    desc += EmoteConsts.VETERAN_BADGE + " ";
                    break;
                }
            }
            for(String badge : target.getBadges()) {
                if(badge.equalsIgnoreCase("master")) {
                    desc += EmoteConsts.MASTER_BADGE + " ";
                    break;
                }
            }
            for(String badge : target.getBadges()) {
                if(badge.equalsIgnoreCase("bday")) {
                    desc += EmoteConsts.BDAY_BADGE + " ";
                    break;
                }
            }
            for(String badge : target.getBadges()) {
                if(badge.equalsIgnoreCase("original")) {
                    desc += EmoteConsts.ORIGINAL_BADGE + " ";
                    break;
                }
            }
        }
        if(!target.getPinnedCard().equals("") && target.ownsPinnedCard()) {
            embed.setImage(target.getPinnedCard());
        }
        embed.setTitle(userInfo.getUserName() + " ➜ " + targetInfo.getUserName()
        + " ┇ Level " + target.getLevel());
        embed.setThumbnail(targetInfo.getUserIcon());
        embed.setDescription(desc);
        embed.setColor(target.getGameColor());
        return embed;
    }
}
