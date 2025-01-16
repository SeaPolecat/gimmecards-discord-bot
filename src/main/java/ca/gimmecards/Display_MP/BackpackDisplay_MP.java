package ca.gimmecards.display_mp;
import ca.gimmecards.consts.*;
import ca.gimmecards.display.*;
import ca.gimmecards.main.*;
import ca.gimmecards.utils.FormatUtils;
import net.dv8tion.jda.api.EmbedBuilder;

public class BackpackDisplay_MP extends Display {

    private User user;
    private User mention;
    private UserInfo mentionInfo;

    public BackpackDisplay_MP() {
        super();
    }

    public User getUser() { return user; }
    public User getMention() { return mention; }
    public UserInfo getMentionInfo() { return mentionInfo; }
    //
    public void setUser(User u) { user = u; }
    public void setMention(User m) { mention = m; }
    public void setMentionInfo(UserInfo mi) { mentionInfo = mi; }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += EmoteConsts.XP + " " + FormatUtils.formatNumber(mention.getXP()) + "/" + FormatUtils.formatNumber(mention.getMaxXP()) + " until next level\n";
        desc += "┅┅\n";
        desc += EmoteConsts.token + " **Tokens** ┇ " + FormatUtils.formatNumber(mention.getTokens()) + "\n";
        desc += EmoteConsts.credits + " **Credits** ┇ " + FormatUtils.formatNumber(mention.getCredits()) + "\n";
        desc += EmoteConsts.star + " **Stars** ┇ " + FormatUtils.formatNumber(mention.getStars()) + "\n";
        desc += EmoteConsts.key + " **Keys** ┇ " + FormatUtils.formatNumber(mention.getKeys()) + "\n";
        desc += "┅┅\n";

        if(mention.getBadges().size() > 0) {
            desc += "**Badges** ┇ ";
            for(String badge : mention.getBadges()) {
                if(badge.equalsIgnoreCase("dev")) {
                    desc += EmoteConsts.devBadge + " ";
                    break;
                }
            }
            for(String badge : mention.getBadges()) {
                if(badge.equalsIgnoreCase("staff")) {
                    desc += EmoteConsts.staffBadge + " ";
                    break;
                }
            }
            for(String badge : mention.getBadges()) {
                if(badge.equalsIgnoreCase("veteran")) {
                    desc += EmoteConsts.veteranBadge + " ";
                    break;
                }
            }
            for(String badge : mention.getBadges()) {
                if(badge.equalsIgnoreCase("master")) {
                    desc += EmoteConsts.masterBadge + " ";
                    break;
                }
            }
            for(String badge : mention.getBadges()) {
                if(badge.equalsIgnoreCase("bday")) {
                    desc += EmoteConsts.bdayBadge + " ";
                    break;
                }
            }
            for(String badge : mention.getBadges()) {
                if(badge.equalsIgnoreCase("original")) {
                    desc += EmoteConsts.originalBagde + " ";
                    break;
                }
            }
        }
        if(!mention.getPinnedCard().equals("") && mention.ownsPinnedCard()) {
            embed.setImage(mention.getPinnedCard());
        }
        embed.setTitle(ui.getUserName() + " ➜ " + mentionInfo.getUserName()
        + " ┇ Level " + mention.getLevel());
        embed.setThumbnail(mentionInfo.getUserIcon());
        embed.setDescription(desc);
        embed.setColor(mention.getGameColor());
        return embed;
    }
}
