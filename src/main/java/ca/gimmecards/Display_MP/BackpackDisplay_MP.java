package ca.gimmecards.Display_MP;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.OtherInterfaces.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class BackpackDisplay_MP extends Display {

    private User user;
    private User mention;
    private UserInfo mentionInfo;

    public BackpackDisplay_MP(String ui) {
        super(ui);
    }

    public User getUser() { return user; }
    public User getMention() { return mention; }
    public UserInfo getMentionInfo() { return mentionInfo; }
    //
    public void setUser(User u) { user = u; }
    public void setMention(User m) { mention = m; }
    public void setMentionInfo(UserInfo mi) { mentionInfo = mi; }

    @Override
    public BackpackDisplay_MP findDisplay() {
        String userId = getUserId();

        for(BackpackDisplay_MP b : IDisplays.backpackDisplays_MP) {
            if(b.getUserId().equals(userId)) {
                return b;
            }
        }
        IDisplays.backpackDisplays_MP.add(0, new BackpackDisplay_MP(userId));
        return IDisplays.backpackDisplays_MP.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += IEmotes.XP + " " + GameManager.formatNumber(mention.getXP()) + "/" + GameManager.formatNumber(mention.getMaxXP()) + " until next level\n";
        desc += "┅┅\n";
        desc += IEmotes.token + " **Tokens** ┇ " + GameManager.formatNumber(mention.getTokens()) + "\n";
        desc += IEmotes.credits + " **Credits** ┇ " + GameManager.formatNumber(mention.getCredits()) + "\n";
        desc += IEmotes.star + " **Stars** ┇ " + GameManager.formatNumber(mention.getStars()) + "\n";
        desc += IEmotes.key + " **Keys** ┇ " + GameManager.formatNumber(mention.getKeys()) + "\n";
        desc += "┅┅\n";

        if(mention.getBadges().size() > 0) {
            desc += "**Badges** ┇ ";
            for(String badge : mention.getBadges()) {
                if(badge.equalsIgnoreCase("dev")) {
                    desc += IEmotes.devBadge + " ";
                    break;
                }
            }
            for(String badge : mention.getBadges()) {
                if(badge.equalsIgnoreCase("staff")) {
                    desc += IEmotes.staffBadge + " ";
                    break;
                }
            }
            for(String badge : mention.getBadges()) {
                if(badge.equalsIgnoreCase("veteran")) {
                    desc += IEmotes.veteranBadge + " ";
                    break;
                }
            }
            for(String badge : mention.getBadges()) {
                if(badge.equalsIgnoreCase("master")) {
                    desc += IEmotes.masterBadge + " ";
                    break;
                }
            }
            for(String badge : mention.getBadges()) {
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
