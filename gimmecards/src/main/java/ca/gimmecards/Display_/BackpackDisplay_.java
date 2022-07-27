package ca.gimmecards.Display_;
import ca.gimmecards.Main.*;
import ca.gimmecards.Display.*;
import ca.gimmecards.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class BackpackDisplay_ extends Display {
    
    public static ArrayList<BackpackDisplay_> displays = new ArrayList<BackpackDisplay_>();
    //
    private User user;
    private User mention;
    private String mentionName;
    private String mentionIcon;

    public BackpackDisplay_(String ui) {
        super(ui);
    }

    public User getUser() { return user; }
    public User getMention() { return mention; }
    public String getMentionName() { return mentionName; }
    public String getMentionIcon() { return mentionIcon; }
    //
    public void setUser(User u) { user = u; }
    public void setMention(User m) { mention = m; }
    public void setMentionName(String mn) { mentionName = mn; }
    public void setMentionIcon(String mi) { mentionIcon = mi; }

    public static BackpackDisplay_ findBackpackDisplay_(String authorId) {
        for(BackpackDisplay_ b : displays) {
            if(b.getUserId().equals(authorId)) {
                return b;
            }
        }
        displays.add(0, new BackpackDisplay_(authorId));
        return displays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        BackpackDisplay_ disp = BackpackDisplay_.findBackpackDisplay_(user.getUserId());
        User mention = disp.getMention();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += XP_ + " " + UX.formatNumber(mention.getXP()) + "/" + UX.formatNumber(mention.getMaxXP()) + " until next level\n";
        desc += "┅┅\n";
        desc += token_ + " **Tokens** ┇ " + UX.formatNumber(mention.getTokens()) + "\n";
        desc += energy_ + " **Energy** ┇ " + UX.formatNumber(mention.getEnergy()) + "\n";
        desc += key_ + " **Keys** ┇ " + UX.formatNumber(mention.getKeys()) + "\n";
        desc += star_ + " **Stars** ┇ " + UX.formatNumber(mention.getStars()) + "\n";
        desc += "┅┅\n";

        if(mention.getBadges().size() > 0) {
            desc += "*Badges* ┇ ";
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
            desc += "\n┅┅\n";
        }
        if(!mention.getBackpackCard().equals("") && State.ownsFavCard(mention)) {
            desc += "*" + disp.getMentionName() + "'s Favourite*";
            embed.setImage(mention.getBackpackCard());
        }
        embed.setTitle(disp.getMentionName() + " ┇ Level " + mention.getLevel());
        embed.setThumbnail(disp.getMentionIcon());
        embed.setDescription(desc);
        embed.setColor(mention.getBackpackColor());
        return embed;
    }
}
