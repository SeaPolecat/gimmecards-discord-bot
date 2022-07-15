package com.wixsite.seapolecat.Display_;
import com.wixsite.seapolecat.Main.*;
import com.wixsite.seapolecat.Display.*;
import com.wixsite.seapolecat.Helpers.*;
import net.dv8tion.jda.api.EmbedBuilder;
import java.util.ArrayList;

public class InspectionDisplay_ extends Display {
    
    public static ArrayList<InspectionDisplay_> displays = new ArrayList<InspectionDisplay_>();
    //
    private User user;
    private User mention;
    private String mentionName;

    public InspectionDisplay_(String ui) {
        super(ui);
    }

    public User getUser() { return user; }
    public User getMention() { return mention; }
    public String getMentionName() { return mentionName; }
    //
    public void setUser(User u) { user = u; }
    public void setMention(User m) { mention = m; }
    public void setMentionName(String mn) { mentionName = mn; }

    public static InspectionDisplay_ findInspectionDisplay_(String authorId) {
        for(InspectionDisplay_ i : displays) {
            if(i.getUserId().equals(authorId)) {
                return i;
            }
        }
        displays.add(0, new InspectionDisplay_(authorId));
        return displays.get(0);
    }

    @Override
    public EmbedBuilder buildEmbed(User user, UserInfo ui, Server server, int page) {
        InspectionDisplay_ disp = findInspectionDisplay_(user.getUserId());
        User mention = disp.getMention();
        int startIndex = page - 1;
        int maxPage = mention.getCards().size();
        Card c = mention.getCards().get(startIndex);
        Data data = c.getData();
        EmbedBuilder embed = new EmbedBuilder();
        String desc = "";

        desc += "**Rarity** ┇ " + UX.findRarityEmote(data) + " " + data.getCardRarity() + "\n";
        desc += "**Card Set** ┇ " + data.getSetEmote() + " " + data.getSetName() + "\n";
        desc += "**XP Value** ┇ " + UX.formatXPPrice(data, c.getSellable()) + "\n\n";
        desc += "*Click on image for zoomed view*";

        embed.setTitle(ui.getUserName() + " ➤ " + disp.getMentionName() + "'s " + UX.findCardTitle(data, c.getIsFav()));
        embed.setDescription(desc);
        embed.setImage(data.getCardImage());
        embed.setFooter("Page " + page + " of " + maxPage, ui.getUserIcon());
        embed.setColor(UX.findEmbedColour(data));
        return embed;
    }
}
