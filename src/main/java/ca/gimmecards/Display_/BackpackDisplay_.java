package ca.gimmecards.Display_;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;

public class BackpackDisplay_ extends Display {

    private User user;
    private User mention;
    private UserInfo mentionInfo;

    public BackpackDisplay_(String ui) {
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
    public BackpackDisplay_ findDisplay() {
        String userId = getUserId();

        for(BackpackDisplay_ b : backpackDisplays_) {
            if(b.getUserId().equals(userId)) {
                return b;
            }
        }
        backpackDisplays_.add(0, new BackpackDisplay_(userId));
        return backpackDisplays_.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += XP_ + " " + UX.formatNumber(mention.getXP()) + "/" + UX.formatNumber(mention.getMaxXP()) + " until next level\n";
        desc += "┅┅\n";
        desc += token_ + " **Tokens** ┇ " + UX.formatNumber(mention.getTokens()) + "\n";
        desc += credits_ + " **Credits** ┇ " + UX.formatNumber(mention.getCredits()) + "\n";
        desc += star_ + " **Stars** ┇ " + UX.formatNumber(mention.getStars()) + "\n";
        desc += key_ + " **Keys** ┇ " + UX.formatNumber(mention.getKeys()) + "\n";
        desc += "┅┅\n";

        if(mention.getBadges().size() > 0) {
            desc += "**Badges** ┇ ";
            for(String badge : mention.getBadges()) {
                if(badge.equalsIgnoreCase("dev")) {
                    desc += devBadge_ + " ";
                    break;
                }
            }
            for(String badge : mention.getBadges()) {
                if(badge.equalsIgnoreCase("staff")) {
                    desc += staffBadge_ + " ";
                    break;
                }
            }
            for(String badge : mention.getBadges()) {
                if(badge.equalsIgnoreCase("community")) {
                    desc += communityBadge_ + " ";
                    break;
                }
            }
            for(String badge : mention.getBadges()) {
                if(badge.equalsIgnoreCase("patreon")) {
                    desc += patreonBadge_ + " ";
                    break;
                }
            }
            for(String badge : mention.getBadges()) {
                if(badge.equalsIgnoreCase("veteran")) {
                    desc += veteranBadge_ + " ";
                    break;
                }
            }
            for(String badge : mention.getBadges()) {
                if(badge.equalsIgnoreCase("master")) {
                    desc += masterBadge_ + " ";
                    break;
                }
            }
            for(String badge : mention.getBadges()) {
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
        if(!mention.getPinCard().equals("") && Check.ownsFavCard(mention)) {
            embed.setImage(mention.getPinCard());
        }
        embed.setTitle(ui.getUserName() + " ➜ " + mentionInfo.getUserName()
        + " ┇ Level " + mention.getLevel());
        embed.setThumbnail(mentionInfo.getUserIcon());
        embed.setDescription(desc);
        embed.setColor(mention.getGameColor());
        return embed;
    }
}
